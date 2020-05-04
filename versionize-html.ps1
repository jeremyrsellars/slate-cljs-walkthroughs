param (
  [string]$html,
  [string]$version
)

if ([string]::IsNullOrWhiteSpace($version)) {
    $version = (get-filehash $html -algorithm sha1).Hash
}

Write-Output "Versionizing $($html) as $($version)"

(Get-Content $html) `
    -replace '(?<="[^:"]+\.(?:js|css|png|jpg))(?=")', "?v=$($version)" `
    |
  Out-File $html -Force -Encoding ASCII
