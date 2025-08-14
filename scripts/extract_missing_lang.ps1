<#
Extract missing localization keys from run/logs/latest.log.

Outputs:
- run/logs/missing_lang_keys.txt (unique sorted keys)
- run/logs/missing_lang_skeleton.json (JSON object with TODO placeholders)

Usage:
  pwsh -File scripts/extract_missing_lang.ps1
  powershell.exe -ExecutionPolicy Bypass -File scripts\extract_missing_lang.ps1
#>

param(
  [string]$LogPath = $null,
  [string]$OutTxt  = $null,
  [string]$OutJson = $null
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

if (-not $LogPath) { $LogPath = Join-Path -Path 'run/logs' -ChildPath 'latest.log' }
if (-not $OutTxt)  { $OutTxt  = Join-Path -Path 'run/logs' -ChildPath 'missing_lang_keys.txt' }
if (-not $OutJson) { $OutJson = Join-Path -Path 'run/logs' -ChildPath 'missing_lang_skeleton.json' }

if (-not (Test-Path -LiteralPath $LogPath)) {
  Write-Error "Log file not found: $LogPath"
}

$content = Get-Content -LiteralPath $LogPath -Raw

# Patterns seen in logs (multiline enabled)
$rx1 = [regex]'(?m)Missing lang key:\s*(?<key>.+?)\s*$'
$rx2 = [regex]'(?m)Missing lang key for .*?:\s*(?<key>.+?)\s*$'

$keys = New-Object System.Collections.Generic.HashSet[string]

foreach ($m in $rx1.Matches($content)) { [void]$keys.Add(($m.Groups['key'].Value.Trim())) }
foreach ($m in $rx2.Matches($content)) { [void]$keys.Add(($m.Groups['key'].Value.Trim())) }

if ($keys.Count -eq 0) {
  Write-Host 'No missing language keys found in log.'
  exit 0
}

# Keep only namespaces we own or common ones used by this mod
$keepPrefixes = @('research.', 'entity.', 'item.thaumcraft.', 'block.thaumcraft.', 'gui.thaumcraft.', 'jei.thaumcraft.', 'tc.research_category.')
$filtered = $keys | Where-Object { $p = $_; $keepPrefixes | Where-Object { $p.StartsWith($_) } | Select-Object -First 1 }

$sorted = $filtered | Sort-Object -Unique

# Write TXT
$sorted | Set-Content -LiteralPath $OutTxt -NoNewline:$false

# Build simple JSON skeleton with TODO placeholders
$obj = [ordered]@{}
foreach ($k in $sorted) {
  $placeholder = switch -Regex ($k) {
    '^research\.(.+?)\.title$' {
      $raw = $Matches[1]
      $spaced = ($raw -replace '_',' ' -replace '([a-z])([A-Z])','$1 $2')
      $lower = $spaced.ToLowerInvariant()
      # Title-case
      $ti = [System.Globalization.CultureInfo]::InvariantCulture.TextInfo
      $ti.ToTitleCase($lower)
      break
    }
    '^entity\.(.+?)\.name$'   {
      $raw = $Matches[1]
      ($raw -replace '_',' ' -replace '([a-z])([A-Z])','$1 $2')
    }
    default { 'TODO' }
  }
  $obj[$k] = $placeholder
}

($obj | ConvertTo-Json -Depth 5) | Set-Content -LiteralPath $OutJson -NoNewline

Write-Host "Wrote: $OutTxt" -ForegroundColor Green
Write-Host "Wrote: $OutJson" -ForegroundColor Green


