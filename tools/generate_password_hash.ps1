param(
    [int]$Count = 250,
    [string]$Password = "123456",
    [string]$OutFile = "tools/password_hash_output.txt"
)

$iterations = 65536
$keyLength = 256

$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$output = New-Object System.Collections.Generic.List[string]

# Header for reference
$output.Add("# Password: $Password")
$output.Add("# Iterations: $iterations")
$output.Add("# KeyLength: $keyLength bits")
$output.Add("id,salt_base64,hash_base64")

for ($i = 1; $i -le $Count; $i++) {
    $salt = New-Object byte[] 16
    $rng.GetBytes($salt)

    $pbkdf2 = New-Object System.Security.Cryptography.Rfc2898DeriveBytes(
        $Password,
        $salt,
        $iterations,
        [System.Security.Cryptography.HashAlgorithmName]::SHA256
    )

    $hashBytes = $pbkdf2.GetBytes($keyLength / 8)
    
    $saltBase64 = [Convert]::ToBase64String($salt)
    $hashBase64 = [Convert]::ToBase64String($hashBytes)

    $output.Add(("{0},{1},{2}" -f $i, $saltBase64, $hashBase64))
    
    if ($i % 50 -eq 0) {
        Write-Host "Generated $i hashes..."
    }
}

$output | Out-File -FilePath $OutFile -Encoding UTF8
Write-Host "Success! Created $Count hashes in $OutFile"
