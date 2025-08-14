<#
Generate a language skeleton by scanning sources (Java + research JSON).

Outputs:
- run/logs/source_lang_keys.txt (unique, sorted keys)
- run/logs/source_lang_skeleton.json (JSON object with placeholders)

Usage:
  ./scripts/generate_lang_from_sources.ps1
  ./scripts/generate_lang_from_sources.ps1 -OutTxt run/logs/missing_lang_keys.txt -OutJson run/logs/missing_lang_skeleton.json
#>

param(
  [string]$JavaDir = 'src/main/java',
  [string]$ResearchDir = 'src/main/resources/assets/thaumcraft/research',
  [string]$OutTxt = 'run/logs/source_lang_keys.txt',
  [string]$OutJson = 'run/logs/source_lang_skeleton.json'
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Ensure-Dir([string]$path) {
  $dir = Split-Path -Parent -Path $path
  if ($dir -and -not (Test-Path -LiteralPath $dir)) {
    New-Item -ItemType Directory -Force -Path $dir | Out-Null
  }
}

function New-StringSet {
  return (New-Object System.Collections.Generic.HashSet[string])
}

function Add-Key([object]$set, [string]$key) {
  if ($null -eq $set) { return }
  if ([string]::IsNullOrWhiteSpace($key)) { return }
  [void]$set.Add($key.Trim())
}

function Get-JavaTranslationKeys([string]$root) {
  $keys = New-StringSet
  if (-not (Test-Path -LiteralPath $root)) { return $keys }
  $javaFiles = Get-ChildItem -LiteralPath $root -Recurse -Filter '*.java' -File -ErrorAction SilentlyContinue
  foreach ($f in $javaFiles) {
    $text = Get-Content -LiteralPath $f.FullName -Raw
    foreach ($m in [regex]::Matches($text, 'TranslationTextComponent\(\s*"([^"]+)"')) { Add-Key $keys $m.Groups[1].Value }
    foreach ($m in [regex]::Matches($text, "TranslationTextComponent\(\s*'([^']+)'") ) { Add-Key $keys $m.Groups[1].Value }
    foreach ($m in [regex]::Matches($text, 'I18n\.get\(\s*"([^"]+)"')) { Add-Key $keys $m.Groups[1].Value }
    foreach ($m in [regex]::Matches($text, "I18n\.get\(\s*'([^']+)'") ) { Add-Key $keys $m.Groups[1].Value }
  }
  return $keys
}

function Get-ResearchTranslationKeys([string]$root) {
  $keys = New-StringSet
  if (-not (Test-Path -LiteralPath $root)) { return $keys }
  $jsonFiles = Get-ChildItem -LiteralPath $root -Recurse -Filter '*.json' -File -ErrorAction SilentlyContinue
  foreach ($f in $jsonFiles) {
    try {
      $obj = Get-Content -LiteralPath $f.FullName -Raw | ConvertFrom-Json
    } catch { continue }
    if ($null -eq $obj) { continue }
    if ($obj.entries) {
      foreach ($e in $obj.entries) {
        if ($e.name) { Add-Key $keys ([string]$e.name) }
        if ($e.stages) {
          foreach ($st in $e.stages) { if ($st.text) { Add-Key $keys ([string]$st.text) } }
        }
        $hasAddenda = $false
        try { if ($e.PSObject.Properties.Match('addenda').Count -gt 0 -and $e.addenda) { $hasAddenda = $true } } catch {}
        if ($hasAddenda) { foreach ($ad in $e.addenda) { if ($ad.text) { Add-Key $keys ([string]$ad.text) } } }
      }
    }
  }
  return $keys
}

$all = New-StringSet
$javaKeys = Get-JavaTranslationKeys -root $JavaDir
foreach ($k in $javaKeys) { Add-Key $all $k }
$researchKeys = Get-ResearchTranslationKeys -root $ResearchDir
foreach ($k in $researchKeys) { Add-Key $all $k }

# Keep only namespaces we care about
$keepPrefixes = @('research.', 'entity.', 'item.thaumcraft.', 'block.thaumcraft.', 'gui.thaumcraft.', 'jei.thaumcraft.', 'tc.research_category.')
$filtered = @()
foreach ($k in $all) {
  foreach ($pref in $keepPrefixes) {
    if ($k.StartsWith($pref)) { $filtered += $k; break }
  }
}
$sorted = $filtered | Sort-Object -Unique

Ensure-Dir -path $OutTxt
Ensure-Dir -path $OutJson

# Write keys
$sorted | Set-Content -LiteralPath $OutTxt -NoNewline:$false

# Build skeleton JSON
$obj = [ordered]@{}
foreach ($k in $sorted) {
  $placeholder = switch -Regex ($k) {
    '^research\.(.+?)\.title$' {
      $raw = $Matches[1]
      $spaced = ($raw -replace '_',' ' -replace '([a-z])([A-Z])','$1 $2')
      $lower = $spaced.ToLowerInvariant()
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



