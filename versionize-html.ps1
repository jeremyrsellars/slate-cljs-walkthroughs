param (
  [string]$html,
  [string]$version
)

Write-Output "Versionizing $($html)."

Function Get-ContentAddressable {
    Param($match)
    $link = $match.Groups[0].Value
    $file = "target/public/$link"
    #echo "Getting file hash for $file"
    try {
        $version = (get-filehash $file -algorithm sha1).Hash
    } catch {
        $version = [DateTime]::Now
    }
    #echo $version
    Write-Host "$match`t$version"
    $version
}

$content = [System.Text.RegularExpressions.Regex]::Replace( `
    (Get-Content $html), `
    '(?<==")[^:"]+\.(?:js|css|png|jpg)(?=")', `
    {param($match) "$($match)?v=$(Get-ContentAddressable $match)"})
$content | Out-File $html -Force -Encoding ASCII
