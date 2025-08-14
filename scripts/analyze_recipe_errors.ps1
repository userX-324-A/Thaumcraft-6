<#
Analyze latest.log for recipe parsing errors and suggest concrete fixes.

Outputs a concise report to console and writes a markdown report to:
  migration/analysis/recipe_errors.md

Checks performed (1.16.5 port specifics):
 - Maps log entries like "Parsing error loading recipe <ns>:<path>" to JSON files
 - Validates presence of file and parses JSON
 - For thaumcraft:crucible recipes:
   - Ensure key 'catalyst' exists (old JSON sometimes used 'input')
   - Validate 'result.item' is a registered item id
   - Suggest 1.13+ rename: minecraft:web -> minecraft:cobweb
   - Validate 'aspects' is an object map {"aer":1, ...}, not an array
 - Highlights any unknown item ids (neither registered in ModItems/ModBlocks nor known vanilla)

Usage:
  .\scripts\analyze_recipe_errors.ps1
  .\scripts\analyze_recipe_errors.ps1 -LogPath run\logs\latest.log
#>

param(
    [string]$LogPath = $(Join-Path -Path 'run/logs' -ChildPath 'latest.log'),
    [string]$RepoRoot = (Get-Location).Path,
    [string]$OutReport = $(Join-Path -Path 'migration/analysis' -ChildPath 'recipe_errors.md')
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Get-RegisteredThaumcraftIds {
    param(
        [string]$RepoRoot
    )
    $ids = New-Object System.Collections.Generic.HashSet[string]

    # Items registered in ModItems.java
    $modItemsPath = Join-Path $RepoRoot 'src/main/java/thaumcraft/common/registers/ModItems.java'
    if (Test-Path -LiteralPath $modItemsPath) {
        $content = Get-Content -LiteralPath $modItemsPath -Raw
        $rx = [regex]'ITEMS\.register\("(?<id>[a-z0-9_\-\.]+)"'
        foreach ($m in $rx.Matches($content)) { [void]$ids.Add("thaumcraft:" + $m.Groups['id'].Value) }
    }

    # BlockItems implicitly created for blocks registered in ModBlocks.java
    $modBlocksPath = Join-Path $RepoRoot 'src/main/java/thaumcraft/common/registers/ModBlocks.java'
    if (Test-Path -LiteralPath $modBlocksPath) {
        $content = Get-Content -LiteralPath $modBlocksPath -Raw
        $rx = [regex]'registerBlock\("(?<id>[a-z0-9_\-\.]+)"'
        foreach ($m in $rx.Matches($content)) { [void]$ids.Add("thaumcraft:" + $m.Groups['id'].Value) }
    }

    return $ids
}

function Get-KnownRenames {
    # Minimal 1.13+ renames relevant to logs we've seen
    # Extend as needed
    return [ordered]@{
        'minecraft:web' = 'minecraft:cobweb'
    }
}

function Test-ItemIdExists {
    param(
        [string]$ItemId,
        [System.Collections.Generic.HashSet[string]]$TcIds,
        [hashtable]$Renames
    )
    if (-not $ItemId) { return $false }
    if ($TcIds.Contains($ItemId)) { return $true }
    if ($Renames.ContainsKey($ItemId)) { return $false }
    # Heuristic: assume most minecraft:* exist except a few renamed; we cannot fully validate here
    if ($ItemId -like 'minecraft:*') { return $true }
    return $false
}

function Test-CrucibleRecipeJson {
    param(
        [pscustomobject]$Json,
        [System.Collections.Generic.HashSet[string]]$TcIds,
        [hashtable]$Renames
    )

    $issues = @()
    $suggestions = @()

    # Key mismatch: 'input' (old) vs 'catalyst' (new serializer expects 'catalyst')
    $hasCatalyst = $Json.PSObject.Properties.Name -contains 'catalyst'
    $hasInput = $Json.PSObject.Properties.Name -contains 'input'
    if (-not $hasCatalyst -and $hasInput) {
        $issues += "Uses 'input' key; serializer expects 'catalyst'"
        $suggestions += "Rename 'input' to 'catalyst' and keep the same structure (item/tag)"
    } elseif (-not $hasCatalyst -and -not $hasInput) {
        $issues += "Missing required 'catalyst' ingredient"
    }

    # Validate 'result.item'
    if ($Json.result -and $Json.result.item) {
        $resId = [string]$Json.result.item
        if (-not (Test-ItemIdExists -ItemId $resId -TcIds $TcIds -Renames $Renames)) {
            $issues += "Unknown result item id: $resId"
            if ($Renames.ContainsKey($resId)) {
                $suggestions += "Rename result item to '$($Renames[$resId])'"
            }
        }
    } else {
        $issues += 'Missing result.item'
    }

    # Validate catalyst item/tag if present
    $catObj = $null
    if ($Json.catalyst) { $catObj = $Json.catalyst }
    elseif ($Json.input) { $catObj = $Json.input }
    if ($catObj) {
        if ($catObj.item) {
            $catId = [string]$catObj.item
            if (-not (Test-ItemIdExists -ItemId $catId -TcIds $TcIds -Renames $Renames)) {
                $issues += "Unknown catalyst item id: $catId"
                if ($Renames.ContainsKey($catId)) {
                    $suggestions += "Rename catalyst item to '$($Renames[$catId])'"
                } elseif ($catId -match '^thaumcraft:(web|web_result)$') {
                    $suggestions += "Likely intended: catalyst 'minecraft:cobweb' (was 'web' in 1.12)"
                }
            }
        } elseif ($catObj.tag) {
            # tags usually fine; no further validation here
        } else {
            $issues += "'catalyst' should contain 'item' or 'tag'"
        }
    }

    # Aspects structure: must be an object map, not array
    if ($Json.PSObject.Properties.Name -contains 'aspects') {
        $aspects = $Json.aspects
        if ($aspects -is [System.Collections.IEnumerable] -and $aspects.GetType().Name -eq 'Object[]') {
            $issues += "'aspects' is an array; expected an object map like {aer:1}"
            # Try to propose a conversion
            $map = [ordered]@{}
            foreach ($entry in $aspects) {
                if ($entry.name -and $entry.amount) { $map[$entry.name] = [int]$entry.amount }
            }
            if ($map.Count -gt 0) {
                $jsonSnippet = ($map | ConvertTo-Json -Depth 3 -Compress)
                $suggestions += "Replace 'aspects' with: $jsonSnippet"
            }
        }
    }

    # Type check
    if (-not $Json.type -or [string]$Json.type -ne 'thaumcraft:crucible') {
        $issues += "Unexpected or missing type; expected 'thaumcraft:crucible'"
    }

    [pscustomobject]@{
        Issues = $issues
        Suggestions = $suggestions
    }
}

function Get-RecipeFilePathFromId {
    param(
        [string]$RepoRoot,
        [string]$RecipeId
    )
    $split = $RecipeId.Split(':', 2)
    if ($split.Count -ne 2) { return $null }
    $ns = $split[0]
    $path = $split[1]
    return Join-Path $RepoRoot (Join-Path "src/main/resources/data/$ns/recipes" ("$path.json"))
}

# Ensure output directory
$outDir = Split-Path -Parent $OutReport
New-Item -ItemType Directory -Force -Path $outDir | Out-Null

if (-not (Test-Path -LiteralPath $LogPath)) {
    Write-Error "Log file not found: $LogPath"
}

$log = Get-Content -LiteralPath $LogPath -Raw

# Capture parse errors from RecipeManager
$rx = [regex]'(?m)^.*?RecipeManager.*?Parsing error loading recipe\s+(?<id>[a-z0-9_\-\.]+:[a-z0-9_\-\.\/]+)\s*$'
    $parseMatches = $rx.Matches($log)
    if ($parseMatches.Count -eq 0) {
    Write-Host 'No recipe parsing errors found in log.'
    '# Recipe error analysis`n`nNo recipe parsing errors found in `$LogPath`' | Set-Content -LiteralPath $OutReport -Encoding UTF8
    exit 0
}

$tcIds = Get-RegisteredThaumcraftIds -RepoRoot $RepoRoot
$renames = Get-KnownRenames

$report = @()

foreach ($m in $parseMatches) {
    $id = $m.Groups['id'].Value
    # Try to capture the next exception summary line after this match
    $exception = ''
    $startIdx = $m.Index + $m.Length
    $tail = $log.Substring($startIdx, [Math]::Min(500, $log.Length - $startIdx))
    $rxEx = [regex]'(?m)^(?<line>\S.*?(JsonSyntaxException|IllegalStateException|IOException|FileNotFoundException).*?)$'
    $exm = $rxEx.Match($tail)
    if ($exm.Success) { $exception = $exm.Groups['line'].Value.Trim() }

    $filePath = Get-RecipeFilePathFromId -RepoRoot $RepoRoot -RecipeId $id
    $entry = [ordered]@{
        id = $id
        path = $filePath
        exists = $false
        exception = $exception
        issues = @()
        suggestions = @()
    }

    if ($filePath -and (Test-Path -LiteralPath $filePath)) {
        $entry.exists = $true
        try {
            $jsonText = Get-Content -LiteralPath $filePath -Raw
            $jsonObj = $jsonText | ConvertFrom-Json -ErrorAction Stop

            switch -Regex ($jsonObj.type) {
                '^thaumcraft:crucible$' {
                    $res = Test-CrucibleRecipeJson -Json $jsonObj -TcIds $tcIds -Renames $renames
                    $entry.issues = $res.Issues
                    $entry.suggestions = $res.Suggestions
                }
                default {
                    # Generic checks: missing result.item
                    if (-not ($jsonObj.result -and $jsonObj.result.item)) {
                        $entry.issues += 'Missing result.item'
                    }
                }
            }
        } catch {
            $msg = 'Invalid JSON: ' + $_.Exception.Message
            $entry.issues += $msg
        }
    } else {
        $entry.issues += 'Recipe JSON file not found'
    }

    $report += [pscustomobject]$entry
}

# Write console summary
$count = [int]$report.Count
Write-Host ('Recipe parse error count: {0}' -f $count)
foreach ($r in $report) {
    $line = ('ID: {0} -> {1}' -f $r.id, $r.path)
    Write-Host $line -ForegroundColor Cyan
    if ($r.exception) { Write-Host ('  Exception: {0}' -f $r.exception) }
    foreach ($i in $r.issues) { Write-Host ('  Issue: {0}' -f $i) -ForegroundColor Yellow }
    foreach ($s in $r.suggestions) { Write-Host ('  Suggestion: {0}' -f $s) -ForegroundColor Green }
}

# Build markdown report
$md = @()
$md += "# Recipe error analysis"
$md += ""
$md += "Source log: `$LogPath = $LogPath"
$md += ""
foreach ($r in $report) {
    $md += "## $($r.id)"
    $md += ""
    $md += "- **file**: `$($r.path)"
    $md += "- **exists**: $($r.exists)"
    if ($r.exception) { $md += ("- **exception**: " + $r.exception) }
    if ($r.issues.Count -gt 0) {
        $md += '- **issues**:'
        foreach ($i in $r.issues) { $md += "  - $i" }
    }
    if ($r.suggestions.Count -gt 0) {
        $md += '- **suggestions**:'
        foreach ($s in $r.suggestions) { $md += "  - $s" }
    }
    $md += ""
}

$mdText = ($md -join [Environment]::NewLine)
$mdText | Set-Content -LiteralPath $OutReport -Encoding UTF8

Write-Host "Wrote report: $OutReport" -ForegroundColor Green


