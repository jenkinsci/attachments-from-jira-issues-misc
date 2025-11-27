   @rem --------------------------------------------------------------------------
         @rem This batch files removes all QC client-side files
         @rem --------------------------------------------------------------------------
         @setlocal
         @set CLIENT_DIR="C:\Program Files\Common Files\Mercury Interactive\Quality Center"
         @set CLIENT11_DIR_XP_USER="%USERPROFILE%\Local Settings\Application Data\HP\ALM-Client"
         @set CLIENT11_DIR_XP_ALLUSERS="C:\Documents and Settings\All Users\Application Data\HP\ALM-Client"
         @set CLIENT11_DIR_XP_ALLUSERS_PLATFORM="C:\Documents and Settings\All Users\Application Data\HP\ALM-Platform"
         @set CLIENT11_DIR_XP_USER_OLD="%USERPROFILE%\Local Settings\Application Data\HP\ALM-Platform"
         @set CLIENT11_DIR_XP_ALLUSERS_OLD="C:\Documents and Settings\All Users\Application Data\HP\ALM-Client"
         @set CLIENT11_DIR_VISTA_7_USER="%LOCALAPPDATA%\HP\ALM-Client"
         @set CLIENT11_DIR_VISTA_7_ALLUSERS="%ALLUSERSPROFILE%\HP\ALM-Client"
         @set CLIENT11_DIR_VISTA_7_USER_OLD="%LOCALAPPDATA%\HP\ALM-Platform"
         @set CLIENT11_DIR_VISTA_7_ALLUSERS_OLD="%ALLUSERSPROFILE%\HP\ALM-Platform"
         @echo.
         @echo Unregistering components ...
         @echo.
         for /R %CLIENT_DIR% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT_DIR% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT_DIR% %%f in (*.dll) do regasm /u /s "%%f"
         for /R %CLIENT11_DIR_XP_USER% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT11_DIR_XP_USER% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT11_DIR_XP_USER% %%f in (*.dll) do regasm /u /s "%%f"
         for /R %CLIENT11_DIR_XP_ALLUSERS% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT11_DIR_XP_ALLUSERS% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT11_DIR_XP_ALLUSERS% %%f in (*.dll) do regasm /u /s "%%f"
         for /R %CLIENT11_DIR_XP_USER_OLD% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT11_DIR_XP_USER_OLD% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT11_DIR_XP_USER_OLD% %%f in (*.dll) do regasm /u /s "%%f"
         for /R %CLIENT11_DIR_XP_ALLUSERS_OLD% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT11_DIR_XP_ALLUSERS_OLD% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT11_DIR_XP_ALLUSERS_OLD% %%f in (*.dll) do regasm /u /s "%%f"
         for /R %CLIENT11_DIR_VISTA_7_USER% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT11_DIR_VISTA_7_USER% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT11_DIR_VISTA_7_USER% %%f in (*.dll) do regasm /u /s "%%f"
         for /R %CLIENT11_DIR_VISTA_7_ALLUSERS% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT11_DIR_VISTA_7_ALLUSERS% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT11_DIR_VISTA_7_ALLUSERS% %%f in (*.dll) do regasm /u /s "%%f"
         for /R %CLIENT11_DIR_VISTA_7_USER_OLD% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT11_DIR_VISTA_7_USER_OLD% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT11_DIR_VISTA_7_USER_OLD% %%f in (*.dll) do regasm /u /s "%%f"
         for /R %CLIENT11_DIR_VISTA_7_ALLUSERS_OLD% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT11_DIR_VISTA_7_ALLUSERS_OLD% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT11_DIR_VISTA_7_ALLUSERS_OLD% %%f in (*.dll) do regasm /u /s "%%f"
         for /R %CLIENT11_DIR_XP_ALLUSERS_PLATFORM% %%i in (*.dll) do regsvr32 /u /s %%i
         for /R %CLIENT11_DIR_XP_ALLUSERS_PLATFORM% %%j in (*.ocx) do regsvr32 /u /s %%j
         for /R %CLIENT11_DIR_XP_ALLUSERS_PLATFORM% %%f in (*.dll) do regasm /u /s "%%f"
         @echo.
         @echo Done Unregister components
         @echo.
         @rem --------------------------------------------------------------------------
         @echo.
         @echo Removing folders and files ...
         @echo.
         rd /s /q %CLIENT_DIR%
         rd /s /q "%TEMP%\TD_80"
         rd /s /q %CLIENT11_DIR_XP_USER%
         rd /s /q %CLIENT11_DIR_XP_ALLUSERS%
         rd /s /q %CLIENT11_DIR_XP_USER_OLD%
         rd /s /q %CLIENT11_DIR_XP_ALLUSERS_OLD%
         rd /s /q %CLIENT11_DIR_VISTA_7_USER%
         rd /s /q %CLIENT11_DIR_VISTA_7_ALLUSERS%
         rd /s /q %CLIENT11_DIR_VISTA_7_USER_OLD%
         rd /s /q %CLIENT11_DIR_VISTA_7_ALLUSERS_OLD%
         rd /s /q %CLIENT11_DIR_XP_ALLUSERS_PLATFORM%
         @echo.
         @echo Done removing files and folders
         @echo.
         @rem --------------------------------------------------------------------------
         @echo.
         @echo Removing spiders ...
         @echo.
         regsvr32 /u /s "%WINDIR%\Downloaded Program Files\Spider91.ocx"
         regsvr32 /u /s "%WINDIR%\Downloaded Program Files\ALM-Platform-Loader.11.ocx"
         regsvr32 /u /s "%WINDIR%\Downloaded Program Files\ALM-Platform-Loader.12.ocx"
         del /s "%WINDIR%\Downloaded Program Files\Spider91.*"
         del /s "%WINDIR%\Downloaded Program Files\Loader Class v*"
         del /s "%WINDIR%\Downloaded Program Files\ALM Platform Loader v*"
         del /s /q "%temp%\interop.ALPLoader.dll"
         del /s /q "%temp%\ALM-Platform-Loader.11.cab "
         del /s /q "%temp%\ALM-Platform-Loader.11.ocx "
         del /s /q "%temp%\ALM-Platform-Loader.12.cab "
         del /s /q "%temp%\ALM-Platform-Loader.12.ocx "
         @echo.
         @echo Done removing spiders
         @echo Done. Client cleaned
         @echo.
         @endlocal
         pause