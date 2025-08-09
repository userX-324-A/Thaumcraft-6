<#
Generates Forge/Minecraft tag JSON files under src/main/resources/data.

Behavior:
- Idempotent: merges with existing files, dedups and sorts values.
- Creates directories as needed.
- Easy to extend: edit $TagSpecs or add more entries.

Usage:
  pwsh -File scripts/generate_tags.ps1
  powershell.exe -ExecutionPolicy Bypass -File scripts\generate_tags.ps1
#>

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Ensure-Directory([string]$Path) {
  if (-not (Test-Path -Path $Path)) {
    New-Item -ItemType Directory -Force -Path $Path | Out-Null
  }
}

function Read-ExistingValues([string]$FilePath) {
  if (-not (Test-Path -Path $FilePath)) { return @() }
  try {
    $json = Get-Content -Path $FilePath -Raw | ConvertFrom-Json -ErrorAction Stop
    if ($null -ne $json.values) { return [string[]]$json.values }
    return @()
  } catch {
    Write-Warning "Failed parsing existing JSON: $FilePath. Overwriting with fresh content."
    return @()
  }
}

function Write-TagFile([string]$Namespace, [string]$Kind, [string]$TagPath, [string[]]$Values) {
  $root = Join-Path -Path $PSScriptRoot -ChildPath '..\src\main\resources\data'
  $dir = Join-Path -Path $root -ChildPath (Join-Path -Path $Namespace -ChildPath (Join-Path -Path 'tags' -ChildPath $Kind))
  $file = Join-Path -Path $dir -ChildPath ($TagPath.TrimStart('/').TrimEnd('/') + '.json')
  Ensure-Directory (Split-Path -Path $file -Parent)

  $existing = Read-ExistingValues -FilePath $file
  $merged = @($existing + $Values) | Where-Object { $_ -and $_.Trim().Length -gt 0 } | Select-Object -Unique | Sort-Object

  $obj = [ordered]@{
    replace = $false
    values  = $merged
  }
  $out = ($obj | ConvertTo-Json -Depth 5)

  $shouldWrite = $true
  if (Test-Path -Path $file) {
    $current = Get-Content -Path $file -Raw
    if ($current -eq $out) { $shouldWrite = $false }
  }
  if ($shouldWrite) {
    $out | Set-Content -Path $file -NoNewline
    Write-Host "Wrote: $file" -ForegroundColor Green
  } else {
    Write-Host "Unchanged: $file" -ForegroundColor DarkGray
  }
}

# ----------------------------------------------------------------------------
# Define tag specs. Each entry:
#   Namespace: 'forge' | 'minecraft' | 'thaumcraft'
#   Kind     : 'items' | 'blocks'
#   Path     : e.g. 'ingots/void' or 'ores/cinnabar'
#   Values   : array of item/block ids or tag refs (e.g. '#forge:gems/amber')
# ----------------------------------------------------------------------------

$TagSpecs = @()

# Forge ores (blocks + items) for Thaumcraft-specific ores
$TagSpecs += @{ Namespace='forge'; Kind='blocks'; Path='ores/amber';   Values=@('thaumcraft:ore_amber') }
$TagSpecs += @{ Namespace='forge'; Kind='blocks'; Path='ores/cinnabar';Values=@('thaumcraft:ore_cinnabar') }
$TagSpecs += @{ Namespace='forge'; Kind='blocks'; Path='ores/quartz';  Values=@('thaumcraft:ore_quartz') }
$TagSpecs += @{ Namespace='forge'; Kind='items';  Path='ores/amber';   Values=@('thaumcraft:ore_amber') }
$TagSpecs += @{ Namespace='forge'; Kind='items';  Path='ores/cinnabar';Values=@('thaumcraft:ore_cinnabar') }
$TagSpecs += @{ Namespace='forge'; Kind='items';  Path='ores/quartz';  Values=@('thaumcraft:ore_quartz') }

# Forge gems / plates / ingots / nuggets for Thaumcraft materials
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='gems/amber';     Values=@('thaumcraft:amber') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='plates/brass';   Values=@('thaumcraft:plate_brass') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='plates/iron';    Values=@('thaumcraft:plate_iron') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='plates/thaumium';Values=@('thaumcraft:plate_thaumium') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='plates/void';    Values=@('thaumcraft:plate_void') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='ingots/brass';   Values=@('thaumcraft:ingot_brass') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='ingots/thaumium';Values=@('thaumcraft:thaumium_ingot') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='ingots/void';    Values=@('thaumcraft:void_ingot') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='nuggets/brass';  Values=@('thaumcraft:nugget_brass') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='nuggets/thaumium';Values=@('thaumcraft:thaumium_nugget') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='nuggets/void';   Values=@('thaumcraft:void_nugget') }

# Minecraft/Forge convenience tags used in recipes
$TagSpecs += @{ Namespace='minecraft'; Kind='items'; Path='planks';     Values=@('thaumcraft:plank_greatwood','thaumcraft:plank_silverwood') }
$TagSpecs += @{ Namespace='forge';     Kind='items'; Path='glass';      Values=@('minecraft:glass','minecraft:glass_pane') }
$TagSpecs += @{ Namespace='forge';     Kind='items'; Path='glass_panes';Values=@('minecraft:glass_pane') }
$TagSpecs += @{ Namespace='forge';     Kind='items'; Path='rods/wooden';Values=@('minecraft:stick') }

# Dyes commonly used (black/red)
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='dyes/black'; Values=@('minecraft:ink_sac') }
$TagSpecs += @{ Namespace='forge'; Kind='items'; Path='dyes/red';   Values=@('minecraft:red_dye') }

# Storage blocks for Thaumcraft metals (if present as items/blocks)
$TagSpecs += @{ Namespace='forge'; Kind='items';  Path='storage_blocks/brass';   Values=@('thaumcraft:block_brass') }
$TagSpecs += @{ Namespace='forge'; Kind='items';  Path='storage_blocks/thaumium';Values=@('thaumcraft:block_thaumium') }
$TagSpecs += @{ Namespace='forge'; Kind='items';  Path='storage_blocks/void';    Values=@('thaumcraft:block_void') }
$TagSpecs += @{ Namespace='forge'; Kind='blocks'; Path='storage_blocks/brass';   Values=@('thaumcraft:block_brass') }
$TagSpecs += @{ Namespace='forge'; Kind='blocks'; Path='storage_blocks/thaumium';Values=@('thaumcraft:block_thaumium') }
$TagSpecs += @{ Namespace='forge'; Kind='blocks'; Path='storage_blocks/void';    Values=@('thaumcraft:block_void') }

# Logs/planks convenience for worldgen or recipes if used
$TagSpecs += @{ Namespace='minecraft'; Kind='blocks'; Path='logs';   Values=@('thaumcraft:log_greatwood','thaumcraft:log_silverwood') }
$TagSpecs += @{ Namespace='minecraft'; Kind='items';  Path='logs';   Values=@('thaumcraft:log_greatwood','thaumcraft:log_silverwood') }

# Generic stone (used by some recipes as fallback)
$TagSpecs += @{ Namespace='forge'; Kind='blocks'; Path='stone'; Values=@('minecraft:stone') }
$TagSpecs += @{ Namespace='forge'; Kind='items';  Path='stone'; Values=@('minecraft:stone') }

# Thaumcraft-specific logical group
$TagSpecs += @{ Namespace='thaumcraft'; Kind='items'; Path='crystals';  Values=@('thaumcraft:crystal_aer','thaumcraft:crystal_ignis','thaumcraft:crystal_aqua','thaumcraft:crystal_terra','thaumcraft:crystal_ordo','thaumcraft:crystal_perditio','#forge:gems/amber') }

# ----------------------------------------------------------------------------
# Generate all tag files
# ----------------------------------------------------------------------------

foreach ($spec in $TagSpecs) {
  Write-TagFile -Namespace $spec.Namespace -Kind $spec.Kind -TagPath $spec.Path -Values $spec.Values
}

Write-Host "Tag generation complete." -ForegroundColor Cyan


