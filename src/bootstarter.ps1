chcp 1254
$ext = ".sgn"
$filename = Read-Host "Sigun dosyanizin ismini yazin"
Write-Host "C'de $filename$ext ismindeki dosyalar araniyor..."

$files = Get-ChildItem -Path "C:\" -Filter "$filename$ext" -Recurse -Depth 3 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty FullName

if ($null -eq $files) {
    Write-Host "Girdiginiz isimde dosya bulunamadi."
    pause
    exit
}

# Check if $files is an array or a single file object
if ($files -is [System.Array]) {
    for ($i = 0; $i -lt $files.Count; $i++) {
        Write-Host "$($i + 1). $($files[$i])"
    }

    $choice = Read-Host "Calistirmak istediginiz dosyanin numarasini girin"
    $file = $files[$choice - 1]
}
else {

    $file = $files
}


    & ".\boot.bat" $file
