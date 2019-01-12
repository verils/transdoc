@echo off

set cmd_dir=%~dp0
set doc_dir=%cmd_dir%docs

rem delete all directories and files of .\docs
rd /s/q %doc_dir%
md %doc_dir%

echo Delete done!
echo;
pause