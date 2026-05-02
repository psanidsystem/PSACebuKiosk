$ErrorActionPreference = 'Stop'

$folderUrl = 'https://drive.google.com/drive/folders/1G5aivL107HWiYnWcwRxkGct87LGpZ9U7?usp=sharing'
$folderId = '1G5aivL107HWiYnWcwRxkGct87LGpZ9U7'
$outDir = Join-Path $PSScriptRoot '..\app\src\main\assets\employee_photos'

New-Item -ItemType Directory -Force -Path $outDir | Out-Null

function Decode-DriveEscapes([string]$text) {
    return [regex]::Replace($text, '\\x([0-9A-Fa-f]{2})', {
        param($match)
        [char][Convert]::ToInt32($match.Groups[1].Value, 16)
    })
}

function Remove-Diacritics([string]$value) {
    $normalized = $value.Normalize([System.Text.NormalizationForm]::FormD)
    $builder = New-Object System.Text.StringBuilder

    foreach ($character in $normalized.ToCharArray()) {
        $category = [System.Globalization.CharUnicodeInfo]::GetUnicodeCategory($character)
        if ($category -ne [System.Globalization.UnicodeCategory]::NonSpacingMark) {
            [void]$builder.Append($character)
        }
    }

    return $builder.ToString().Normalize([System.Text.NormalizationForm]::FormC)
}

function Normalize-NamePart([string]$value) {
    $plainValue = Remove-Diacritics $value
    return (($plainValue.ToUpperInvariant().ToCharArray() | Where-Object {
        [char]::IsLetterOrDigit($_)
    }) -join '')
}

function Get-EmployeePhotoKey([string]$name) {
    $baseName = [System.IO.Path]::GetFileNameWithoutExtension($name)
    $parts = $baseName.Split(',', 2)
    $surname = Normalize-NamePart $parts[0].Trim()
    $initial = ''

    if ($parts.Count -gt 1) {
        foreach ($character in $parts[1].Trim().ToCharArray()) {
            if ([char]::IsLetter($character)) {
                $initial = [char]::ToUpperInvariant($character).ToString()
                break
            }
        }
    }

    return "$surname|$initial"
}

function Get-SafeFileName([string]$name) {
    $baseName = (Remove-Diacritics ([System.IO.Path]::GetFileNameWithoutExtension($name))).ToLowerInvariant()
    $extension = [System.IO.Path]::GetExtension($name).ToLowerInvariant()
    if ([string]::IsNullOrWhiteSpace($extension)) {
        $extension = '.jpg'
    }

    $safeName = [regex]::Replace($baseName, '[^a-z0-9]+', '_').Trim('_')
    return "$safeName$extension"
}

$html = (Invoke-WebRequest -Uri $folderUrl -UseBasicParsing -TimeoutSec 30).Content
$escapedPattern = '\\x22([A-Za-z0-9_-]{20,})\\x22,\\x5b\\x22' + [regex]::Escape($folderId) + '\\x22\\x5d,\\x22(.+?)\\x22,\\x22(.+?)\\x22'
$htmlEntityPattern = '&quot;([A-Za-z0-9_-]{20,})&quot;\],null,null,null,&quot;(image/[^&]+)&quot;.*?&quot;([^&]+?\.(?:png|jpe?g|webp))&quot;'
$escapedMatches = [regex]::Matches($html, $escapedPattern)
$htmlEntityMatches = [regex]::Matches($html, $htmlEntityPattern, [System.Text.RegularExpressions.RegexOptions]::Singleline)
$manifest = New-Object System.Collections.Generic.List[string]
$manifest.Add('key,filename')
$downloaded = 0
$seenKeys = @{}

foreach ($match in $escapedMatches) {
    $fileId = $match.Groups[1].Value
    $name = Decode-DriveEscapes $match.Groups[2].Value
    $mimeType = Decode-DriveEscapes $match.Groups[3].Value

    if (-not $mimeType.StartsWith('image/')) {
        continue
    }

    $key = Get-EmployeePhotoKey $name
    if ([string]::IsNullOrWhiteSpace($key) -or $key.EndsWith('|') -or $seenKeys.ContainsKey($key)) {
        continue
    }

    $fileName = Get-SafeFileName $name
    $outFile = Join-Path $outDir $fileName
    $downloadUrl = "https://drive.google.com/uc?export=download&id=$fileId"

    Invoke-WebRequest -Uri $downloadUrl -UseBasicParsing -TimeoutSec 60 -OutFile $outFile
    $manifest.Add("$key,$fileName")
    $seenKeys[$key] = $true
    $downloaded++
}

foreach ($match in $htmlEntityMatches) {
    $fileId = $match.Groups[1].Value
    $mimeType = [System.Net.WebUtility]::HtmlDecode($match.Groups[2].Value)
    $name = [System.Net.WebUtility]::HtmlDecode($match.Groups[3].Value)

    if (-not $mimeType.StartsWith('image/')) {
        continue
    }

    $key = Get-EmployeePhotoKey $name
    if ([string]::IsNullOrWhiteSpace($key) -or $key.EndsWith('|') -or $seenKeys.ContainsKey($key)) {
        continue
    }

    $fileName = Get-SafeFileName $name
    $outFile = Join-Path $outDir $fileName
    $downloadUrl = "https://drive.google.com/uc?export=download&id=$fileId"

    Invoke-WebRequest -Uri $downloadUrl -UseBasicParsing -TimeoutSec 60 -OutFile $outFile
    $manifest.Add("$key,$fileName")
    $seenKeys[$key] = $true
    $downloaded++
}

$manifestPath = Join-Path $outDir 'manifest.csv'
$manifest | Set-Content -Path $manifestPath -Encoding ASCII

Write-Host "Downloaded $downloaded employee photos."
Write-Host "Manifest: $manifestPath"
