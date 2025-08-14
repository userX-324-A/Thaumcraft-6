param(
  [string]$Skeleton = 'run/logs/missing_lang_skeleton.json',
  [string]$LangFile = 'src/main/resources/assets/thaumcraft/lang/en_us.json'
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Read-JsonObjectProps([string]$path) {
  if (-not (Test-Path -LiteralPath $path)) { return @{} }
  $obj = Get-Content -LiteralPath $path -Raw | ConvertFrom-Json
  if ($obj -eq $null) { return @{} }
  $map = @{}
  foreach ($p in $obj.PSObject.Properties) { $map[$p.Name] = $p.Value }
  return $map
}

$skeleton = Read-JsonObjectProps -path $Skeleton
$existing = Read-JsonObjectProps -path $LangFile

# Keep non-research/entity keys from existing
$keepPrefixes = @('gui.thaumcraft.', 'item.thaumcraft.', 'jei.thaumcraft.', 'tc.research_category.', 'tooltip.thaumcraft.')
$kept = @{}
foreach ($k in $existing.Keys) {
  foreach ($pref in $keepPrefixes) {
    if ($k.StartsWith($pref)) { $kept[$k] = $existing[$k]; break }
  }
}

# Build merged ordered object: kept first (sorted), then skeleton (sorted)
$ordered = New-Object System.Collections.Specialized.OrderedDictionary
foreach ($k in ($kept.Keys | Sort-Object)) { $ordered.Add($k, $kept[$k]) }
foreach ($k in ($skeleton.Keys | Sort-Object)) { $ordered[$k] = $skeleton[$k] }

$po = New-Object psobject
foreach ($entry in $ordered.GetEnumerator()) { $po | Add-Member -NotePropertyName $entry.Key -NotePropertyValue $entry.Value }

($po | ConvertTo-Json -Depth 5) | Set-Content -LiteralPath $LangFile -NoNewline
Write-Host "Merged to: $LangFile" -ForegroundColor Green


