<#
Run a headless dev server once, wait for "Done", then stop and analyze logs.

Steps:
- Ensure EULA accepted in `run/eula.txt`
- Rotate `run/logs/latest.log` to `latest.prev.log`
- Start `gradlew runServer` (nogui already set in build.gradle)
- Poll `run/logs/latest.log` until it contains "Done (" or timeout
- Stop server process
- Generate missing lang keys via `scripts/extract_missing_lang.ps1`
 - Optionally merge placeholders into `assets/thaumcraft/lang/en_us.json` and remove stale keys
- Write `run/logs/server_scan_summary.txt` with key findings

Usage:
  ./scripts/headless_server_once.ps1 [-IncludeOptionalMods] [-MergeLang]
#>

param(
    [switch]$IncludeOptionalMods,
    [int]$TimeoutSeconds = 480,
    [switch]$MergeLang
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Write-Info([string]$msg) { Write-Host "[INFO] $msg" -ForegroundColor Cyan }
function Write-Warn([string]$msg) { Write-Host "[WARN] $msg" -ForegroundColor Yellow }
function Write-Ok([string]$msg)   { Write-Host "[OK]  $msg"  -ForegroundColor Green }

$runDir   = Join-Path -Path $PSScriptRoot -ChildPath '..' | Resolve-Path | ForEach-Object { Join-Path $_ 'run' }
$logsDir  = Join-Path -Path $runDir -ChildPath 'logs'
$logPath  = Join-Path -Path $logsDir -ChildPath 'latest.log'
$prevPath = Join-Path -Path $logsDir -ChildPath 'latest.prev.log'

New-Item -ItemType Directory -Force -Path $runDir | Out-Null
New-Item -ItemType Directory -Force -Path $logsDir | Out-Null

# Accept EULA
$eulaPath = Join-Path -Path $runDir -ChildPath 'eula.txt'
if (-not (Test-Path -LiteralPath $eulaPath)) {
    Write-Info 'Creating eula.txt (accepted)'
    "# Automatically accepted by headless_server_once.ps1`r`neula=true`r`n" | Set-Content -LiteralPath $eulaPath -NoNewline
} else {
    $eulaContent = Get-Content -LiteralPath $eulaPath -Raw
    if ($eulaContent -notmatch 'eula\s*=\s*true') {
        Write-Warn 'Updating eula.txt to accept EULA'
        "# Automatically accepted by headless_server_once.ps1`r`neula=true`r`n" | Set-Content -LiteralPath $eulaPath -NoNewline
    }
}

# Rotate previous log (graceful if locked)
$baselineChars = 0
if (Test-Path -LiteralPath $logPath) {
    Write-Info 'Rotating previous latest.log to latest.prev.log'
    try {
        Move-Item -Force -LiteralPath $logPath -Destination $prevPath
    } catch {
        Write-Warn 'latest.log appears locked; copying contents instead of moving'
        try {
            $raw = Get-Content -LiteralPath $logPath -Raw -ErrorAction Stop
            $raw | Set-Content -LiteralPath $prevPath -NoNewline
            # Establish a baseline so we only read new content
            $baselineChars = $raw.Length
        } catch {
            Write-Warn 'Unable to copy locked latest.log; proceeding without rotation'
        }
    }
}

$gradleArgs = @('runServer')
if ($IncludeOptionalMods) { $gradleArgs += '-PincludeOptionalMods=true' }

Write-Info ("Starting server: ./gradlew {0}" -f ($gradleArgs -join ' '))

# Launch gradle in a child process
$gradleExe = if (Test-Path -LiteralPath (Join-Path $PSScriptRoot '..' | Resolve-Path | ForEach-Object { Join-Path $_ 'gradlew.bat' })) {
    (Join-Path $PSScriptRoot '..' | Resolve-Path | ForEach-Object { Join-Path $_ 'gradlew.bat' })
} else { 'gradlew' }

$startInfo = New-Object System.Diagnostics.ProcessStartInfo
$startInfo.FileName  = $gradleExe
$startInfo.Arguments = ($gradleArgs -join ' ')
$startInfo.WorkingDirectory = (Join-Path $PSScriptRoot '..' | Resolve-Path).Path
$startInfo.UseShellExecute = $false
$startInfo.RedirectStandardOutput = $true
$startInfo.RedirectStandardError  = $true

$proc = New-Object System.Diagnostics.Process
$proc.StartInfo = $startInfo
[void]$proc.Start()

# Discard gradle stdout/stderr in this process; logs of interest are in run/logs/*
Start-Sleep -Milliseconds 100

# Wait for log creation
$sw = [System.Diagnostics.Stopwatch]::StartNew()
while (-not (Test-Path -LiteralPath $logPath)) {
    if ($sw.Elapsed.TotalSeconds -gt 30) { break }
    Start-Sleep -Milliseconds 200
}

# Poll for "Done (" in latest.log (only consider new content after baseline)
$done = $false
$lastSize = 0
Write-Info 'Waiting for server to reach "Done"...'
while ($sw.Elapsed.TotalSeconds -lt $TimeoutSeconds) {
    if (Test-Path -LiteralPath $logPath) {
        $fi = Get-Item -LiteralPath $logPath -ErrorAction SilentlyContinue
        if ($fi -and $fi.Length -ne $lastSize) {
            $lastSize = $fi.Length
            $content = Get-Content -LiteralPath $logPath -Raw -ErrorAction SilentlyContinue
            if ($null -eq $content) { $content = '' }
            $segment = $content
            if ($baselineChars -gt 0 -and $segment.Length -gt $baselineChars) {
                $segment = $segment.Substring([Math]::Min($baselineChars, $segment.Length))
            }
            # Match Forge 1.16.5 style server ready line, e.g.:
            # [..] [Server thread/INFO] [...]: Done (4.638s)! For help, type "help"
            # Be tolerant of prefixes and whitespace; don't anchor to start of line
            if ($segment -match '(?m)(\]:\s*Done \(|\sDone \([0-9\.]+s\)!)') {
                $done = $true
                break
            }
        }
    }
    if ($proc.HasExited) { break }
    Start-Sleep -Milliseconds 500
}

if ($done) {
    Write-Ok 'Server reported Done. Collecting diagnostics and stopping server...'
} else {
    Write-Warn 'Timed out before Done was detected or process exited early.'
}

# Try to locate and stop the Java server process first, then the gradle wrapper
try {
    $serverProcs = Get-CimInstance Win32_Process | Where-Object {
        $_.Name -ieq 'java.exe' -and ($_.CommandLine -match 'net\.minecraft\.server\.Main' -or $_.CommandLine -match 'forge' -and $_.CommandLine -match 'server')
    }
    foreach ($jp in $serverProcs) {
        try { Stop-Process -Id $jp.ProcessId -Force -ErrorAction Stop } catch {}
    }
} catch {}

if (-not $proc.HasExited) {
    try { Stop-Process -Id $proc.Id -Force -ErrorAction Stop } catch {}
}

# Give file system a moment to flush
Start-Sleep -Seconds 2

# Extract missing language keys
Write-Info 'Extracting missing language keys...'
& (Join-Path $PSScriptRoot 'extract_missing_lang.ps1') -LogPath $logPath -OutTxt (Join-Path $logsDir 'missing_lang_keys.txt') -OutJson (Join-Path $logsDir 'missing_lang_skeleton.json')

# Optionally merge placeholders and remove stale research./entity. keys
if ($MergeLang) {
    Write-Info 'Merging localization placeholders into en_us.json and removing stale keys...'
    & (Join-Path $PSScriptRoot 'merge_lang_placeholders.ps1') -Skeleton (Join-Path $logsDir 'missing_lang_skeleton.json') -RemoveStale
}

# Scan latest.log for common error patterns and save a brief summary
$summaryPath = Join-Path -Path $logsDir -ChildPath 'server_scan_summary.txt'
$findings = @()
if (Test-Path -LiteralPath $logPath) {
    $log = Get-Content -LiteralPath $logPath -Raw
    $patterns = @(
        'Exception',
        'ERROR',
        'NoSuchMethodError',
        'NoClassDefFoundError',
        'ClassNotFoundException',
        'Mixin apply error',
        'Missing registry data',
        'Duplicate key',
        'Failed to load',
        'Fatal',
        'CRASH'
    )
    foreach ($p in $patterns) {
        $m = [regex]::Matches($log, [regex]::Escape($p))
        $count = if ($null -ne $m) { try { [int]$m.Count } catch { 0 } } else { 0 }
        if ($count -gt 0) { $findings += ("{0}: {1}" -f $p, $count) }
    }
    $firstErrors = ($log -split "\r?\n") | Where-Object { $_ -match 'ERROR|Exception' } | Select-Object -First 25
    $summary = @()
    $summary += "DoneDetected: $done"
    $summary += "Findings: " + ($findings -join '; ')
    $summary += ''
    $summary += 'First error-like lines:'
    $summary += $firstErrors
    $summary -join "`r`n" | Set-Content -LiteralPath $summaryPath -NoNewline:$false
} else {
    "latest.log not found" | Set-Content -LiteralPath $summaryPath -NoNewline
}

Write-Ok ("Wrote: {0}" -f $summaryPath)
Write-Ok ("Wrote: {0}" -f (Join-Path $logsDir 'missing_lang_keys.txt'))
Write-Ok ("Wrote: {0}" -f (Join-Path $logsDir 'missing_lang_skeleton.json'))


