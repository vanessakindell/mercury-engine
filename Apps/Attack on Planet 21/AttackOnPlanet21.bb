Global Mode#=2
Global Version#=2.5

AppTitle "Attack on Planet 21 v"+Version#,"Let the ailens abduct the world?"

If CommandLine$()="" Then RuntimeError("Please launch using the Launcher.")

; Some Includes
Include "..\..\Build\Main.bb"
Include "..\..\Build\Src\Launcher\Launcher2d.bb"
Include "MainGame.bb"

If Version#<>EngineVersion# Then RuntimeError "Engine version mismatch!"

LoadSettings()
load_hiscores()

font=LoadFont("System",GraphicsHeight()/24)
SetFont Font

Global online = Int(iAdditional)

If iAdditional Then remote_hiscores(True)
Cls
Text 0,0,"Loading..."
Flip

SetBuffer(BackBuffer())

Load2DGFX()
LoadSFX()
;IntroVideo("avi")
FlushKeys()
MainMenu(False,True,False,False,"BGM\MainMenu.mp3",True)
End