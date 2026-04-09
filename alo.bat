@echo off
set "PS_SCRIPT=%~dp0FlattenScript.ps1"

:: Tạo file PowerShell tạm thời với thuật toán tối ưu hơn
echo $sourceDir = Get-Location > "%PS_SCRIPT%"
echo $outputFile = Join-Path $sourceDir "Full_Pp.txt" >> "%PS_SCRIPT%"
echo $excludeExtensions = @(".exe", ".png", ".jpg", ".jpeg", ".gif", ".zip", ".dll", ".pyc", ".node", ".pdf", ".class") >> "%PS_SCRIPT%"
echo Write-Host "Dang chuan bi du lieu..." -ForegroundColor Cyan >> "%PS_SCRIPT%"

echo $finalContent = New-Object System.Text.StringBuilder >> "%PS_SCRIPT%"
echo [void]$finalContent.AppendLine("======================================================================") >> "%PS_SCRIPT%"
echo [void]$finalContent.AppendLine("DU AN: $($sourceDir.Leaf)") >> "%PS_SCRIPT%"
echo [void]$finalContent.AppendLine("NGAY TAO: $(Get-Date -Format 'dd/MM/yyyy HH:mm')") >> "%PS_SCRIPT%"
echo [void]$finalContent.AppendLine("======================================================================") >> "%PS_SCRIPT%"
echo [void]$finalContent.AppendLine("`nCAU TRUC FILE:") >> "%PS_SCRIPT%"
echo [void]$finalContent.AppendLine((tree /f /a ^| Out-String)) >> "%PS_SCRIPT%"
echo [void]$finalContent.AppendLine("`n======================================================================") >> "%PS_SCRIPT%"

echo $files = Get-ChildItem -Path $sourceDir -Recurse -File ^| Where-Object { $_.Extension -notin $excludeExtensions -and $_.FullName -ne $outputFile -and $_.FullName -ne "%~f0" -and $_.FullName -ne "%PS_SCRIPT%" } >> "%PS_SCRIPT%"

echo foreach ($file in $files) { >> "%PS_SCRIPT%"
echo     $relativeName = Resolve-Path $file.FullName -Relative >> "%PS_SCRIPT%"
echo     Write-Host "Dang xu ly: $relativeName" -ForegroundColor Gray >> "%PS_SCRIPT%"
echo     [void]$finalContent.AppendLine("`n----------------------------------------------------------------------") >> "%PS_SCRIPT%"
echo     [void]$finalContent.AppendLine("FILE: $relativeName") >> "%PS_SCRIPT%"
echo     [void]$finalContent.AppendLine("----------------------------------------------------------------------") >> "%PS_SCRIPT%"
echo     try { $content = Get-Content -Path $file.FullName -Raw -ErrorAction SilentlyContinue; [void]$finalContent.AppendLine($content) } catch { [void]$finalContent.AppendLine("[Loi: Khong the doc file]") } >> "%PS_SCRIPT%"
echo } >> "%PS_SCRIPT%"

echo Write-Host "Dang ghi du lieu ra file..." -ForegroundColor Yellow >> "%PS_SCRIPT%"
echo $finalContent.ToString() ^| Out-File -FilePath $outputFile -Encoding utf8 -Force >> "%PS_SCRIPT%"
echo Write-Host "`nHOAN THANH! File luu tai: Full_Project_Dump.txt" -ForegroundColor Green >> "%PS_SCRIPT%"

:: Chạy file PowerShell
powershell -NoProfile -ExecutionPolicy Bypass -File "%PS_SCRIPT%"

:: Xóa file tạm
if exist "%PS_SCRIPT%" del "%PS_SCRIPT%"

pause