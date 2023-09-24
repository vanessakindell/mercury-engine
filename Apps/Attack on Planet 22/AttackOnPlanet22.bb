Global Mode#=2
Global Version#=2.74
AppTitle "Attack on Planet 22 v"+Version#,"Let the ailens abduct the world?"

If CommandLine$()="" Then RuntimeError "Please start using the Launcher"

; Some Includes
Include "..\..\Build\Main.bb"
Include "..\..\Build\Src\Launcher\Launcher2d.bb"
Include "MainGame.bb"

If Version#<>EngineVersion# Then RuntimeError "Engine version mismatch!"
SetBuffer(BackBuffer())

load_hiscores()
LoadSettings()

Global online = Int(iAdditional)

If iAdditional Then 
	SyncServer()
	remote_hiscores(1)
EndIf

Cls
Text 0,0,"Loading..."
Flip

;InputKey(0,5,2,8,7)
Load2DGFX()
LoadSFX()
;IntroVideo()
FlushKeys()
MainMenu(True,False,False,False,"BGM\MainMenu.mp3",True)
End