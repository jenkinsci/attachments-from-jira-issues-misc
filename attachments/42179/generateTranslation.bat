set toolPath=C:/Develop/Qt/5.9.4/5.9.4/msvc2015/bin
set sourcePath=C:/Develop/mainline
set rptPath=C:/Develop/rpt
set networkPath=\\xxx\development

echo Generating xls related translations
call attrib "%sourcePath%\kiss\lang\packages.txt" -r
%SYSTEMROOT%\System32\cscript.exe generateTranslation.vbs

REM ... some mor ecode ...