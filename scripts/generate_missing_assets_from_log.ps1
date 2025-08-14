$ErrorActionPreference = 'Stop'

# Paths
$repoRoot = Split-Path -Parent $PSScriptRoot
$logPath = Join-Path $repoRoot 'run/logs/latest.log'
$assetsRoot = Join-Path $repoRoot 'src/main/resources/assets/thaumcraft'

if (!(Test-Path $logPath)) {
	Write-Error "Log file not found: $logPath"
}

# Regex to capture missing JSON references from the log
$pattern = 'FileNotFoundException:\s+thaumcraft:(?<type>models(?:/item)?|blockstates)/(?<path>[\w/]+\.json)'

$entries = @()
Select-String -Path $logPath -Pattern $pattern -AllMatches | ForEach-Object {
	foreach ($m in $_.Matches) {
		$entries += [PSCustomObject]@{
			Type = $m.Groups['type'].Value
			Path = $m.Groups['path'].Value
		}
	}
}

if ($entries.Count -eq 0) {
	Write-Host 'No missing asset JSON references found in the log.'
	return
}

# De-duplicate
$entries = $entries | Sort-Object Type, Path -Unique

Write-Host ("Found {0} missing JSON references" -f $entries.Count)

$created = @()

foreach ($e in $entries) {
	$outDir = Join-Path $assetsRoot $e.Type
	$outFile = Join-Path $outDir $e.Path
	
	if (Test-Path $outFile) {
		continue
	}

	New-Item -ItemType Directory -Force -Path (Split-Path $outFile) | Out-Null

	# Generate minimal placeholder content
    switch ($e.Type) {
        'blockstates' {
            # Minimal blockstate pointing to a vanilla stone model to avoid texture errors
            $content = @"
{
	"variants": {
		"": { "model": "minecraft:block/stone" }
	}
}
"@
        }
        'models' {
            # Safe placeholder: simple stone cube so it doesn't recurse
            $content = @"
{
    "parent": "minecraft:block/cube_all",
    "textures": {
        "all": "minecraft:block/stone"
    }
}
"@
        }
        'models/item' {
            # Safe placeholder: simple stone item so it renders without missingno
            $content = @"
{
    "parent": "minecraft:item/generated",
    "textures": {
        "layer0": "minecraft:item/stone"
    }
}
"@
        }
    }

	Set-Content -Path $outFile -Value $content -Encoding UTF8
	$created += $outFile
	Write-Host "Created $outFile"
}

Write-Host ("Created {0} files" -f $created.Count)


