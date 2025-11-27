set toolPath=C:/Develop/Qt/5.9.4/5.9.4/msvc2015/bin
set sourcePath=C:/Develop/mainline
set rptPath=C:/Develop/rpt
set networkPath=\\ACADIA\development

echo Generating xls related translations
call attrib "%sourcePath%\kiss\lang\packages.txt" -r

reg Query "HKLM\Hardware\Description\System\CentralProcessor\0" | find /i "x86" > NUL && set OS=32BIT || set OS=64BIT
If %OS%==32BIT %SYSTEMROOT%\System32\cscript.exe generateTranslation.vbs
If %OS%==64BIT %SYSTEMROOT%\SysWOW64\cscript.exe generateTranslation.vbs
