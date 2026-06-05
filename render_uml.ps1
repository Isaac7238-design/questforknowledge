# Renders PlantUML diagrams from UML_DESIGN.md to PNG using the PlantUML web server.
# Uses HTTP POST to the PlantUML server's raw text endpoint (no special encoding needed).

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
$mdPath = Join-Path $root "UML_DESIGN.md"
$outDir = Join-Path $root "uml_diagrams"
if (!(Test-Path $outDir)) { New-Item -ItemType Directory -Path $outDir | Out-Null }

# PlantUML text encoding (deflate + custom base64 alphabet)
$plantumlAlphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"

function Encode6Bit([int]$b) {
    return $plantumlAlphabet[$b]
}

function Append3Bytes([int]$b1, [int]$b2, [int]$b3) {
    $c1 = $b1 -shr 2
    $c2 = (($b1 -band 0x3) -shl 4) -bor ($b2 -shr 4)
    $c3 = (($b2 -band 0xF) -shl 2) -bor ($b3 -shr 6)
    $c4 = $b3 -band 0x3F
    $r = ""
    $r += Encode6Bit ($c1 -band 0x3F)
    $r += Encode6Bit ($c2 -band 0x3F)
    $r += Encode6Bit ($c3 -band 0x3F)
    $r += Encode6Bit ($c4 -band 0x3F)
    return $r
}

function EncodePlantUML([string]$text) {
    $bytes = [System.Text.Encoding]::UTF8.GetBytes($text)
    # Raw DEFLATE compression
    $ms = New-Object System.IO.MemoryStream
    $deflate = New-Object System.IO.Compression.DeflateStream($ms, [System.IO.Compression.CompressionLevel]::Optimal, $true)
    $deflate.Write($bytes, 0, $bytes.Length)
    $deflate.Close()
    $compressed = $ms.ToArray()
    $ms.Close()

    # Encode to PlantUML base64 variant
    $result = ""
    for ($i = 0; $i -lt $compressed.Length; $i += 3) {
        $b1 = $compressed[$i]
        $b2 = if ($i + 1 -lt $compressed.Length) { $compressed[$i + 1] } else { 0 }
        $b3 = if ($i + 2 -lt $compressed.Length) { $compressed[$i + 2] } else { 0 }
        $result += Append3Bytes $b1 $b2 $b3
    }
    return $result
}

# Extract all @startuml...@enduml blocks
$content = Get-Content $mdPath -Raw
$pattern = '(?s)@startuml\s+(\S+).*?@enduml'
$matches = [regex]::Matches($content, $pattern)

Write-Host "Found $($matches.Count) diagrams."

$index = 1
foreach ($m in $matches) {
    $block = $m.Value
    $name = $m.Groups[1].Value
    $fileName = "{0:D2}_{1}.png" -f $index, $name
    $outPath = Join-Path $outDir $fileName

    Write-Host "Rendering $fileName ..."
    $encoded = EncodePlantUML $block
    $url = "https://www.plantuml.com/plantuml/png/$encoded"

    try {
        Invoke-WebRequest -Uri $url -OutFile $outPath -ErrorAction Stop
        Write-Host "  Saved: $outPath"
    } catch {
        Write-Host "  FAILED: $($_.Exception.Message)"
    }
    $index++
}

Write-Host "`nDone. Diagrams saved to: $outDir"
