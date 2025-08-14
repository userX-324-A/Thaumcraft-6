<#
Merge placeholder keys into assets/thaumcraft/lang/en_us.json.

- Reads run/logs/missing_lang_skeleton.json (or a file you pass in)
- Ensures all keys exist in en_us.json, adding any missing with placeholder values
- Removes stale keys optionally (flag)

Usage:
  pwsh -File scripts/merge_lang_placeholders.ps1
  pwsh -File scripts/merge_lang_placeholders.ps1 -Skeleton run/logs/missing_lang_skeleton.json -RemoveStale
#>

param(
  [string]$Skeleton   = $null,
  [string]$LangFile   = $null,
  [switch]$RemoveStale
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

if (-not $Skeleton) { $Skeleton = Join-Path -Path 'run/logs' -ChildPath 'missing_lang_skeleton.json' }
if (-not $LangFile) { $LangFile = 'src/main/resources/assets/thaumcraft/lang/en_us.json' }

if (-not (Test-Path -LiteralPath $Skeleton)) { Write-Error "Skeleton not found: $Skeleton" }
if (-not (Test-Path -LiteralPath $LangFile)) {
  # initialize empty lang file
  '{ }' | Set-Content -LiteralPath $LangFile -NoNewline
}

function New-OrderedDict {
  $dict = New-Object System.Collections.Specialized.OrderedDictionary
  return $dict
}

function ObjectToOrderedDict([psobject]$obj) {
  $map = New-OrderedDict
  if ($obj -ne $null) {
    foreach ($p in $obj.PSObject.Properties) {
      $map.Add($p.Name, $p.Value)
    }
  }
  return $map
}

function OrderedDictToPsObject($dict) {
  $po = New-Object psobject
  foreach ($entry in $dict.GetEnumerator()) {
    $po | Add-Member -NotePropertyName $entry.Key -NotePropertyValue $entry.Value
  }
  return $po
}

$skeletonObj = Get-Content -LiteralPath $Skeleton -Raw | ConvertFrom-Json
$existingObj = Get-Content -LiteralPath $LangFile -Raw | ConvertFrom-Json
if ($existingObj -eq $null) { $existingObj = [pscustomobject]@{} }

$skeletonMap = ObjectToOrderedDict $skeletonObj
$existingMap = ObjectToOrderedDict $existingObj

# Merge: keep existing values; add any missing from skeleton
foreach ($entry in $skeletonMap.GetEnumerator()) {
  if (-not $existingMap.Contains($entry.Key)) {
    $existingMap.Add($entry.Key, $entry.Value)
  }
}

if ($RemoveStale) {
  $skeletonKeys = @($skeletonMap.Keys)
  $stalePrefixes = @('research.', 'entity.')
  $toRemove = @()
  foreach ($entry in $existingMap.GetEnumerator()) {
    $key = $entry.Key
    $isTarget = $false
    foreach ($pref in $stalePrefixes) { if ($key.StartsWith($pref)) { $isTarget = $true; break } }
    if ($isTarget -and -not ($skeletonKeys -contains $key)) {
      $toRemove += $key
    }
  }
  foreach ($k in $toRemove) { $existingMap.Remove($k) }
}

# Sort keys for stable diffs
$sorted = New-OrderedDict
foreach ($entry in ($existingMap.GetEnumerator() | Sort-Object Key)) { $sorted.Add($entry.Key, $entry.Value) }

(
  (OrderedDictToPsObject $sorted) |
    ConvertTo-Json -Depth 5
) | Set-Content -LiteralPath $LangFile -NoNewline
Write-Host "Updated: $LangFile" -ForegroundColor Green


