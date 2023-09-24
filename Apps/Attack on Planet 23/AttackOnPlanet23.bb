; Attack On Planet 23
; By Vanessa Games
; Internal source
; Not for external release

; CLINE: /TYPE0 /WIDTH1440X /HEIGHT900 /BPP32 /ADDIT0

Global Version#=2.76
Global IsADemo=0
Global Mode#=3
Global Levelmode=True
Global Online=False
Global GameCode#=27

If CommandLine()="" Then RuntimeError "Please launch using the Launcher"

If IsADemo=True Then
	AppTitle "Attack on Planet 23 v"+Str(Version#)+" Demo","Let the aliens consume everything?"
Else
	AppTitle "Attack on Planet 23 v"+Str(Version#);,"Let the aliens consume everything?"
EndIf

FileStream = WriteFile("GameVersion.txt")
WriteLine FileStream, Version#
CloseFile FileStream

; Some Includes
Include "..\..\Build\Main.bb"
Include "..\..\Build\Src\Launcher\Launcher3d.bb"
Include "MainGame.bb"

load_hiscores()
LoadSettings()

	If FileType("ServerLocation.ini")=1 Then
		File=ReadFile("ServerLocation.ini")
			While Not Eof(File)
				Info$=ReadLine(File)
				If Left(Info$,1)<>";" Or Left(Info$,1)<>"[" Then
					ServerName$=Mid(Info$,Instr(Info$,"=")+1,Len(Info$)-(Instr(Info$,"=")-1))
				EndIf
			Wend
		CloseFile File
	Else
		Online=False
	EndIf

AllowNuke=True

M1# = 5
M2# = 7
M3# = 11
M4# = 6

AddLoad("There are 250 waves to defeat.")
AddLoad("Starbases occur every 25 waves.")
AddLoad("You can share health with your teammate in co-op using [S] or [DPad Up]")
AddLoad("The answer to life the universe and everything is 42.")
AddLoad("Detonating a bullet quickly is a good way to get out of a bind.")
AddLoad(Chr(34)+"Love is space and time measured by the heart"+Chr(34)+" -Marcel Proust")
AddLoad(Chr(34)+"Now I am become Death, the destroyer of worlds."+Chr(34)+" -Bhagavad Gita")
AddLoad("Don't worry if you miss a powerup, it will orbit back around.")

;If IsADemo=False Then InputKey(0,M1#,M2#,M3#,M4#)
font=LoadFont("System",GraphicsHeight()/24)
SetFont Font

LoadingScreen()

If iAdditional=True Then
	Color 255,255,255
	Cls
	Text GraphicsWidth()/2,GraphicsHeight()/2,"Loading...",1,1
	Flip

	cube=CreateCube()
	light=CreateLight()
	
	tempcam1=CreateCamera()
	tempcam2=CreateCamera()
	CameraClsMode tempcam1,0,1
	CameraClsMode tempcam2,0,1
	MoveEntity tempcam1,0,0,-10
	MoveEntity tempcam2,0,0,-10
	PointEntity tempcam1,cube
	PointEntity tempcam2,cube	
	MoveEntity tempcam1,-1,0,0
	MoveEntity tempcam2,1,0,0
	CameraViewport tempcam1,0,0,GraphicsWidth()/2,GraphicsHeight()
	CameraViewport tempcam2,GraphicsWidth()/2,0,GraphicsWidth()/2,GraphicsHeight()
	
	While Not KeyHit(57) Or MouseHit(1) Or JoyHit(1)
		Cls
		TurnEntity cube,1,1,1
		TranslateEntity cube,0,0,-.001
		UpdateWorld()
		RenderWorld()
		Text GraphicsWidth()/2,GraphicsHeight()/3,"Adjust your screen to split mode",1,1
		Text GraphicsWidth()/4,GraphicsHeight()/3+FontHeight(),"So this text only appears once.",1,1
		Text GraphicsWidth()*3/4,GraphicsHeight()/3+FontHeight(),"So this text only appears once.",1,1
		Flip
		
		If KeyHit(1) Then End
	Wend
	
	HideEntity tempcam1
	FreeEntity tempcam1
	HideEntity tempcam2
	FreeEntity tempcam2
	HideEntity cube
	FreeEntity cube
	HideEntity light
	FreeEntity light
EndIf

If Online Then
	success=remote_hiscores()
	If Not success Then
		Cls
		Text 0,0,"Failed to retrieve highscores."
		Flip
		Delay 1000
	EndIf
EndIf

ImportPlaylist("BGM\")
load2DGFX()
load2dSFX()
load3dSFX()
Load3DGFX()
LoadOldGfx()

Intro()
LoadingScreen()
FlushKeys()
MainMenu(1,0,1,True,"BGM\happygalaxy.mp3")
End