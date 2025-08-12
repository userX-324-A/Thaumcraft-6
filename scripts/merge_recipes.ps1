$ErrorActionPreference = 'Stop'

$root = 'data/thaumcraft/recipes'
$dst  = 'src/main/resources/data/thaumcraft/recipes'
$confRoot = Join-Path $dst 'conflicts/root'
$confReorg = Join-Path $dst 'conflicts/reorg'

function Ensure-Directory([string]$path) {
  if (-not (Test-Path $path)) { [void](New-Item -ItemType Directory -Path $path -Force) }
}

function Get-RelativePath([string]$base, [string]$full) {
  $basePath = (Resolve-Path $base).Path
  return $full.Substring($basePath.Length + 1)
}

function Files-Are-Identical([string]$a, [string]$b) {
  $h1 = (Get-FileHash -Algorithm SHA256 -LiteralPath $a).Hash
  $h2 = (Get-FileHash -Algorithm SHA256 -LiteralPath $b).Hash
  return $h1 -eq $h2
}

function Get-RecipeType([string]$filePath) {
  try {
    $json = Get-Content -LiteralPath $filePath -Raw | ConvertFrom-Json
    return $json.type
  } catch { return $null }
}

function Get-TargetSubdir([string]$type) {
  switch -Regex ($type) {
    '^thaumcraft:crucible$'           { return 'crucible' }
    '^thaumcraft:infusion$'           { return 'infusion' }
    '^thaumcraft:arcane_(shapeless|shaped)$' { return 'arcane_workbench' }
    '^thaumcraft:arcane_workbench$'   { return 'arcane_workbench' }
    '^minecraft:crafting_shaped$'     { return 'shaped' }
    '^minecraft:crafting_shapeless$'  { return 'shapeless' }
    '^minecraft:smelting$'            { return 'smelting' }
    '^thaumcraft:smelting_bonus$'     { return 'smelting_bonus' }
    default { return $null }
  }
}

Write-Host 'Step 1/2: Merge legacy root recipes into standard location...'
if (Test-Path $root) {
  $files = Get-ChildItem -File -Recurse -LiteralPath $root
  foreach ($f in $files) {
    $rel = Get-RelativePath -base $root -full $f.FullName
    $target = Join-Path $dst $rel
    $targetDir = Split-Path -Parent $target
    Ensure-Directory $targetDir

    if (Test-Path $target) {
      if (Files-Are-Identical -a $f.FullName -b $target) {
        Remove-Item -LiteralPath $f.FullName -Force
        Write-Host "Identical, removed duplicate: $rel"
      } else {
        $confTarget = Join-Path $confRoot $rel
        Ensure-Directory (Split-Path -Parent $confTarget)
        Move-Item -LiteralPath $f.FullName -Destination $confTarget -Force
        Write-Host "Conflict, moved root copy to: conflicts/root/$rel"
      }
    } else {
      Ensure-Directory (Split-Path -Parent $target)
      Move-Item -LiteralPath $f.FullName -Destination $target -Force
      Write-Host "Moved: $rel"
    }
  }
  # cleanup empty dirs and root
  Get-ChildItem -Directory -Recurse -LiteralPath $root | Sort-Object FullName -Descending | ForEach-Object {
    if (-not (Get-ChildItem -Recurse $_.FullName)) { Remove-Item -LiteralPath $_.FullName -Force }
  }
  if (-not (Get-ChildItem -Recurse -LiteralPath $root)) { Remove-Item -LiteralPath $root -Force }
} else {
  Write-Host 'No legacy root recipe directory found; skipping merge.'
}

Write-Host 'Step 2/2: Organize top-level recipes in dst by type...'
Ensure-Directory $dst
$topLevelFiles = Get-ChildItem -File -LiteralPath $dst -ErrorAction SilentlyContinue
foreach ($file in $topLevelFiles) {
  $type = Get-RecipeType -filePath $file.FullName
  if (-not $type) { continue }
  $subdir = Get-TargetSubdir -type $type
  if (-not $subdir) { continue }
  $targetDir = Join-Path $dst $subdir
  Ensure-Directory $targetDir
  $targetPath = Join-Path $targetDir $file.Name

  if (Test-Path $targetPath) {
    if (Files-Are-Identical -a $file.FullName -b $targetPath) {
      Remove-Item -LiteralPath $file.FullName -Force
      Write-Host "Identical, removed duplicate at top-level: $($file.Name)"
    } else {
      $confTarget = Join-Path (Join-Path $confReorg $subdir) $file.Name
      Ensure-Directory (Split-Path -Parent $confTarget)
      Move-Item -LiteralPath $file.FullName -Destination $confTarget -Force
      Write-Host "Reorg conflict, stashed: conflicts/reorg/$subdir/$($file.Name)"
    }
  } else {
    Move-Item -LiteralPath $file.FullName -Destination $targetPath -Force
    Write-Host "Reorganized: $($file.Name) -> $subdir/"
  }
}

Write-Host 'Done.'


