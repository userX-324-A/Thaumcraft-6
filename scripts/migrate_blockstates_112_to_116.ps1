$ErrorActionPreference = 'Stop'

# Migrate all blockstate JSONs under assets/thaumcraft/blockstates that use 1.12 forge_marker format
# to 1.16+ variants format.

$repoRoot = Split-Path -Parent $PSScriptRoot
$blockstatesDir = Join-Path $repoRoot 'src/main/resources/assets/thaumcraft/blockstates'

if (!(Test-Path $blockstatesDir)) {
    Write-Error "Blockstates directory not found: $blockstatesDir"
}

function Normalize-ModelPath([string]$model) {
    if ([string]::IsNullOrWhiteSpace($model)) { return $null }
    # If already references a block path or another namespace with a slash, keep as-is
    if ($model -match '^[\w-]+:') {
        $parts = $model.Split(':',2)
        $ns = $parts[0]
        $name = $parts[1]
        if ($name -like 'block/*') { return $model }
        # Route to block models by default
        return "$ns:block/$name"
    }
    # No namespace → assume thaumcraft
    if ($model -like 'block/*') { return "thaumcraft:$model" }
    return "thaumcraft:block/$model"
}

$files = Get-ChildItem -Path $blockstatesDir -Filter *.json -Recurse
$converted = 0

foreach ($f in $files) {
    $raw = Get-Content -Raw -LiteralPath $f.FullName
    if ($raw -notmatch 'forge_marker') { continue }

    try {
        $json = $raw | ConvertFrom-Json -ErrorAction Stop
    } catch {
        Write-Warning "Skipping unparsable JSON: $($f.Name) - $_"
        continue
    }

    # Gather defaults
    $defaultModel = $null
    if ($json.PSObject.Properties.Name -contains 'defaults') {
        $defaultModel = $json.defaults.model
    }

    # Build new variants
    $newVariants = @{}
    $hadExplicit = $false
    if ($json.PSObject.Properties.Name -contains 'variants') {
        foreach ($prop in $json.variants.PSObject.Properties) {
            $k = [string]$prop.Name
            $v = $prop.Value
            # Keep only explicit state variants that contain '=' (e.g., facing=north)
            if ($k -match '=') {
                $hadExplicit = $true
                # Normalize possible array wrappers like [{ ... }]
                if ($v -is [System.Array]) { $v = $v | Select-Object -First 1 }
                # Normalize model path
                if ($v.PSObject.Properties.Name -contains 'model') {
                    $v.model = Normalize-ModelPath $v.model
                } elseif ($defaultModel) {
                    $v | Add-Member -NotePropertyName model -NotePropertyValue (Normalize-ModelPath $defaultModel)
                }
                $newVariants[$k] = $v
            }
        }
    }

    if (-not $hadExplicit) {
        # Fallback to default (no property variants present or only normal/inventory were found)
        $modelPath = $null
        if ($defaultModel) {
            $modelPath = Normalize-ModelPath $defaultModel
        } else {
            $base = [System.IO.Path]::GetFileNameWithoutExtension($f.Name)
            $modelPath = "thaumcraft:block/$base"
        }
        $newVariants[""] = @{ model = $modelPath }
    }

    # Emit new JSON
    $outObj = @{ variants = $newVariants }
    $pretty = $outObj | ConvertTo-Json -Depth 8
    Set-Content -LiteralPath $f.FullName -Value $pretty -Encoding UTF8
    $converted++
}

Write-Host ("Converted {0} blockstate files from forge_marker format to 1.16 variants." -f $converted)


