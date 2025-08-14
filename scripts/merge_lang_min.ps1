param(
  [string]$Skeleton = 'run/logs/missing_lang_skeleton.json',
  [string]$LangFile = 'src/main/resources/assets/thaumcraft/lang/en_us.json'
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

if (-not (Test-Path -LiteralPath $Skeleton)) { throw "Skeleton not found: $Skeleton" }
if (-not (Test-Path -LiteralPath $LangFile)) { '{ }' | Set-Content -LiteralPath $LangFile -NoNewline }

$s = Get-Content -LiteralPath $Skeleton -Raw | ConvertFrom-Json
$e = Get-Content -LiteralPath $LangFile -Raw | ConvertFrom-Json
if ($e -eq $null) { $e = [pscustomobject]@{} }

$eNames = @($e.PSObject.Properties.Name)

# Add any missing keys from skeleton
foreach ($p in $s.PSObject.Properties) {
  if (-not ($eNames -contains $p.Name)) {
    $e | Add-Member -NotePropertyName $p.Name -NotePropertyValue $p.Value
  }
}

# Remove stale research./entity. keys not in skeleton
$sNames = @($s.PSObject.Properties.Name)
foreach ($p in @($e.PSObject.Properties)) {
  $name = $p.Name
  if ($name.StartsWith('research.') -or $name.StartsWith('entity.')) {
    if (-not ($sNames -contains $name)) {
      $e.PSObject.Properties.Remove($name) | Out-Null
    }
  }
}

($e | ConvertTo-Json -Depth 5) | Set-Content -LiteralPath $LangFile -NoNewline
Write-Host "Merged (min) to: $LangFile" -ForegroundColor Green


