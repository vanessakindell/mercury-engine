Global SShot#=0
Global FrameWait=CreateTimer(30)

Function MainGame()

LoadingScreen()

FlushKeys()
FlushJoy()
FlushMouse()

Local Direction#
Local Change#
Alive=True
LazerType#=1
LazerType2#=1
Lives#=3
Lives2#=3
score=0
skip=0

PlayerX#=GraphicsWidth()/2

MoveMouse GraphicsWidth()/2,GraphicsHeight()/2

Wave#=1
Create3DWave(0,5)

For Rept = 1 To GraphicsHeight()
	CreateStar()
	UpdateStars()
Next

For Rept = 1 To 800
	Create3DStar()
	Update3DStarsDown()
Next

Box1=CreateCube()
PositionEntity Box1,155,10,0
EntityColor Box1,255,0,0
ScaleEntity Box1,1,1,120

Box2=CreateCube()
PositionEntity Box2,-155,10,0
EntityColor Box2,255,0,0
ScaleEntity Box2,1,1,120

World=CreateSphere(64)
ScaleEntity World,800,800,800
worldtex=LoadTexture("GFX\Game\Planet.jpg")
EntityTexture World,worldtex
RotateEntity world,0,0,90
PositionEntity World,0,-930,0

World2=CreateSphere(64)
ScaleEntity World2,800,800,800
EntityTexture World2,worldtex
EntityColor World2,255,0,0
RotateEntity world2,0,0,90
PositionEntity World2,0,-2000,0

shield=CreateSphere(8,MSH_Player)
ScaleEntity Shield,250,250,250
EntityColor Shield,0,0,255
EntityAlpha Shield,0

shield2=CreateSphere(8,MSH_Player2)
ScaleEntity Shield2,250,250,250
EntityColor Shield2,0,0,255
EntityAlpha Shield2,0

StopChannel(CHN_BGM)

If players=1 Then
	PositionEntity MSH_Player,0,3,-100
	EntityAlpha MSH_Player2,0
Else
	PositionEntity MSH_Player,50,3,-100
	PositionEntity MSH_Player2,-50,3,-100
	EntityAlpha MSH_Player2,1
EndIf

Fliped=False
Local ShowWaveText=0

TempInv#=0
TempInv2#=0

RevivePlayer1=False
RevivePlayer2=False

Player1Ctrl#=ControlMode#
Player2Ctrl#=ControlMode2#

TotalPlayers=Players

	If iAdditional Then
		HUDImage = CreateImage(GraphicsWidth()/2,GraphicsHeight())
	Else
		HUDImage = CreateImage(GraphicsWidth(),GraphicsHeight())
	EndIf

SetBuffer(BackBuffer())

CueMusicTrack("BGM\hyperblaster.mp3")

While Alive=True
Cls

	If KeyDown(56) And KeyHit(62) Then End
	
	If RevivePlayer1=True Then
		ControlMode#=Player1Ctrl#
		ControlMode2#=Player2Ctrl#
		Lives2#=Lives#
		Lives#=3
		Fliped=False
		LazerType2#=LazerType#
		LazerType#=1
		TempInv2#=TempInv#
		TempInv#=0
		Players=2
		revtex=LoadTexture("Mesh\Player1\Fighter.jpg")
		EntityTexture MSH_Player,revtex
		RevivePlayer1=False
	EndIf	
	
	If RevivePlayer2=True Then
		ControlMode2#=Player2Ctrl#
		Lives2#=3
		Fliped=False
		LazerType2#=1
		Players=2
		RevivePlayer2=False
	EndIf	
	
	Skip=Skip+1
	If Skip=>8 Then Skip=0
	
	CameraProjMode camera,1
	
	If players=1 Then
		CameraViewport camera,0,0,GraphicsWidth(),GraphicsHeight()
		CameraProjMode camera_1,0
		CameraProjMode camera2,0
		CameraProjMode camera2_1,0
	Else If players=2 Then
		CameraViewport camera,0,0,GraphicsWidth(),GraphicsHeight()/2
		CameraViewport camera2,0,GraphicsHeight()/2,GraphicsWidth(),GraphicsHeight()/2
		CameraProjMode camera_1,0
		CameraProjMode camera2,1
		CameraProjMode camera2_1,0
	EndIf
	
	If players=1 Then
		EntityAlpha MSH_Player2,0
	EndIf
	
	If players=1 And iAdditional=True Then
		CameraViewport camera,0,0,GraphicsWidth()/2,GraphicsHeight()
		CameraViewport camera_1,GraphicsWidth()/2,0,GraphicsWidth()/2,GraphicsHeight()
		CameraProjMode camera_1,1
		CameraProjMode camera2,0
		CameraProjMode camera2_1,0
	EndIf

	If players=2 And iAdditional=True Then
		CameraProjMode camera_1,1
		CameraProjMode camera2,1
		CameraProjMode camera2_1,1
		CameraViewport camera,0,0,GraphicsWidth()/2,GraphicsHeight()/2
		CameraViewport camera_1,GraphicsWidth()/2,0,GraphicsWidth()/2,GraphicsHeight()/2
		CameraViewport camera2,0,GraphicsHeight()/2,GraphicsWidth()/2,GraphicsHeight()/2
		CameraViewport camera2_1,GraphicsWidth()/2,GraphicsHeight()/2,GraphicsWidth()/2,GraphicsHeight()/2
	EndIf

	If Wave#>=10 And IsADemo=True Then
		GameOver(1)
	EndIf
	
	TurnEntity World,0,-.1,0
	TurnEntity World2,0,-.1,0
	
	If Wave#>=10 And Wave#<55 And EntityY(World)>-2000 Then TranslateEntity World,0,-1,0
	
	If Wave#>=25 And Wave#<40 And EntityY(World2)<-830 Then TranslateEntity World2,0,1,0
	
	If Wave#>=40 And EntityY(World2)>-2000 Then TranslateEntity World2,0,-1,0

	If Wave#>=55 And Wave#<60 And EntityY(World)<-930 Then TranslateEntity World,0,1,0
	
	If Wave#>=60 And EntityY(World)>-2000 Then TranslateEntity World,0,-1,0	
	
	If Wave#>10 And Wave#<20 Then
		RoidCnt#=0
		For Ro.Asteroid3D = Each Asteroid3D
			RoidCnt#=RoidCnt#+1
		Next
		If RoidCnt#<10 Then
			Create3DAsteroid()
		EndIf
	EndIf
	
	If Wave#>40 And Wave#<50 Then
		RoidCnt#=0
		For Ro.Asteroid3D = Each Asteroid3D
			RoidCnt#=RoidCnt#+1
		Next
		If RoidCnt#<10 Then
			Create3DAsteroid()
		EndIf
	EndIf
	
	If Wave#>50 And Wave#<>75 And Wave#<>100 And Wave#<>125 And Wave#<>150 And Wave#<>175 And Wave#<200 Then
		RoidCnt#=0
		For Ro.Asteroid3D = Each Asteroid3D
			RoidCnt#=RoidCnt#+1
		Next
		If RoidCnt#<10 Then
			Create3DAsteroid()
		EndIf
	EndIf
	
		
	If KeyHit(25) Then ; ____________________________________________________________________________ CUE PASSWORDS!
		Online=False
		Locate (GraphicsWidth()/2)-(FontWidth()*StringWidth("Password:")),GraphicsHeight()/2
		FlushKeys()
		Passkey$=Input$("Password:")
		Password(Passkey$)
		FlushKeys()
	EndIf
	
	If TempInv>0 Then
		TempInv=TempInv-1
	EndIf
	
	If TempInv2>0 Then
		TempInv2=TempInv2-1
	EndIf
	
	If TempInv#>0 Then
		If InvType#=1 Then
			EntityAlpha Shield,.5
		Else
			EntityColor MSH_Player,255,0,0
		EndIf
	Else
		EntityAlpha Shield,0
		EntityColor MSH_Player,255,255,255
	EndIf
	
	If TempInv2#>0 Then
		If InvType2#=1 Then
			EntityAlpha Shield2,.5
		Else
			EntityColor MSH_Player2,255,0,0
		EndIf
	Else
		EntityColor MSH_Player2,255,255,255
		EntityAlpha Shield2,0
	EndIf
	
	Update3DStarbase(1,1,1,1)
	Update3DAsteroids()
	UpdateSun()
	
	If Draw=1 Then
		EntityAlpha World,.2
		EntityAlpha MSH_Player,.2
		If players=2 Then
			EntityAlpha MSH_Player2,.2
			CameraProjMode Camera2,1
		EndIf
		CameraProjMode Camera,1
		If iAdditional Then CameraProjMode Camera_1,1
	Else If draw = 0 Then
		EntityAlpha World,1
		EntityAlpha MSH_Player,1
		If players=2 Then
			EntityAlpha MSH_Player2,1
			CameraProjMode Camera2,1
			If iAdditional Then CameraProjMode Camera2_1,1
		EndIf
		CameraProjMode Camera,1
		If iAdditional Then CameraProjMode Camera_1,1
	Else If draw=3 Then
		EntityAlpha World,0
		EntityAlpha MSH_Player,0
		If players=2 Then EntityAlpha MSH_Player2,0
		CameraProjMode Camera,0
		CameraProjMode Camera_1,0
		CameraProjMode Camera2,0
		CameraProjMode Camera2_1,0
	EndIf
	
	HandleMusic(1)
	
	If draw<>3 Then
		Update3DStarsDown()
		If players=2 Then Update3DStarsDown2()
		DrawStars()
	Else
		CreateStar()
		UpdateStars()
	EndIf

	Update3DExplosions()

	If KeyHit(1) Or JoyHit(4) Or JoyHit(4,1) Or JoyHit(8) Or JoyHit(8,1) Or MouseHit(4) Then
		FlushKeys()
		FlushJoy()
		FlushMouse()
		paused=True
		StopChannel BGMNew
		Options(True,True)
		paused=False
	EndIf

	PlayerX#=((EntityX(MSH_Player)+150)/300)*GraphicsWidth()
	Player2X#=((EntityX(MSH_Player2)+150)/300)*GraphicsWidth()
	
	;If PlayerX#>GraphicsWidth()-ImageWidth(GFX_Game_Player1) Then PlayerX#=GraphicsWidth()-(ImageWidth(GFX_Game_Player1)/2)
	;If PlayerX#<ImageWidth(GFX_Game_Player1)/2 Then PlayerX#=ImageWidth(GFX_Game_Player1)/2

	Update3DBullet()
	Update3DPowerups()
	
	If LazerType#>100 Then LazerType#=100
	If LazerType2#>100 Then LazerType2#=100
	
	If draw=1 Or draw=3 Then
		If TempInv#>0 Then
			If InvType#=1 Then
				Color 0,0,255
			Else
				Color 255,0,0
			EndIf
			Oval (((EntityX(MSH_Player)+150)/300)*GraphicsWidth())-(ImageWidth(GFX_Game_Player1)/2),(((-EntityZ(MSH_Player)+120)/240)*GraphicsHeight())-(ImageHeight(GFX_Game_Player1)/2),ImageWidth(GFX_Game_Player1),ImageHeight(GFX_Game_Player1)
		EndIf
		If TempInv2#>0 Then
			If InvType2#=1 Then
				Color 0,0,255
			Else
				Color 255,0,0
			EndIf
			Oval (((EntityX(MSH_Player2)+150)/300)*GraphicsWidth())-(ImageWidth(GFX_Game_Player2)/2),(((-EntityZ(MSH_Player2)+120)/240)*GraphicsHeight())-(ImageHeight(GFX_Game_Player2)/2),ImageWidth(GFX_Game_Player2),ImageHeight(GFX_Game_Player2)
		EndIf
		If fliped=False Then
			DrawImage GFX_Game_Player1,((EntityX(MSH_Player)+150)/300)*GraphicsWidth(),((-EntityZ(MSH_Player)+120)/240)*GraphicsHeight(),1
		Else
			DrawImage GFX_Game_Player2,((EntityX(MSH_Player)+150)/300)*GraphicsWidth(),((-EntityZ(MSH_Player)+120)/240)*GraphicsHeight(),1
		EndIf
		If players=2 Then DrawImage GFX_Game_Player2,((EntityX(MSH_Player2)+150)/300)*GraphicsWidth(),((-EntityZ(MSH_Player2)+120)/240)*GraphicsHeight(),1
	EndIf
	
	Color 255,255,255
		
	If KeyHit(15) Or JoyHit(7) Then
		If iAdditional
			WaveText$="Classic mode not supported in 3D"
			Showwavetext=60
		EndIf
		If draw=1 Then
			draw=3
		Else If draw = 3 Then
			draw=0
		Else If draw = 0 Then
			draw=1
		EndIf
	EndIf
	
	Select ControlMode#:
		Case 1:
			Mousecontrol(MSH_Player,1,LazerType#,blah)
		Case 2:
			Keycontrol(MSH_Player,1,LazerType#,blah)
		Case 3:
			Joy1control(MSH_Player,1,LazerType#,blah)
		Case 4:
			Joy2control(MSH_Player,1,LazerType#,blah)
	End Select
	
	TranslateEntity MSH_Player,MovementX#,0,0
	
	If players=2 Then
		Select ControlMode2#:
			Case 1:
				Mousecontrol(MSH_Player2,2,LazerType2#,blah)
			Case 2:
				Keycontrol(MSH_Player2,2,LazerType2#,blah)
			Case 3:
				Joy1control(MSH_Player2,2,LazerType2#,blah)
			Case 4:
				Joy2control(MSH_Player2,2,LazerType2#,blah)
		End Select

		TranslateEntity MSH_Player2,MovementX#,0,0
	EndIf
	
	EntityAlpha Box1,0
	EntityAlpha Box2,0
	
	If EntityX(MSH_Player)>150 Then
		PositionEntity MSH_Player,150,EntityY(MSH_Player),EntityZ(MSH_Player)
		If Not ChannelPlaying(CHN_WALL) Then
				CHN_WALL=EmitSFX(SFX3_SCORPIO,Box1)
		EndIf
		If draw<>3 Then EntityAlpha Box1,.5
	EndIf
	If EntityX(MSH_Player)<-150 Then
		PositionEntity MSH_Player,-150,EntityY(MSH_Player),EntityZ(MSH_Player)
		If Not ChannelPlaying(CHN_WALL) Then
				CHN_WALL=EmitSFX(SFX3_SCORPIO,Box2)
		EndIf
		If draw<>3 Then EntityAlpha Box2,.5
	EndIf
	
	If players=2 Then
		If EntityX(MSH_Player2)>150 Then
			PositionEntity MSH_Player2,150,EntityY(MSH_Player2),EntityZ(MSH_Player2)
			If Not ChannelPlaying(CHN_WALL) Then
				CHN_WALL=EmitSFX(SFX3_SCORPIO,Box1)
			EndIf
			If draw<>3 Then EntityAlpha Box1,.5
		EndIf
		If EntityX(MSH_Player2)<-150 Then
			PositionEntity MSH_Player2,-150,EntityY(MSH_Player2),EntityZ(MSH_Player2)
			If Not ChannelPlaying(CHN_WALL) Then
				CHN_WALL=EmitSFX(SFX3_SCORPIO,Box2)
			EndIf
			If draw<>3 Then EntityAlpha Box2,.5
		EndIf
	EndIf

	If score<0 Then GameOver(0)
	
	AdvNumb#=0
	For A3.Adversary3D = Each Adversary3d
		Update3DAdversary(A3,LazerType#,1,1,1,1)
		AdvNumb#=AdvNumb#+1
	Next
	For S.Starbase3D = Each Starbase3D
		AdvNumb#=AdvNumb#+1
	Next
	If AdvNumb# = 0 And Wave#<251 Then
		Wave#=Wave#+1
		If Wave#<>25 And Wave#<>50 And Wave#<>75 And Wave#<>100 And Wave#<>125 And Wave#<>150 And Wave#<>175 And Wave#<>200 And Wave#<>250 And Wave#<>251 Then
			Create3DWave(0,5)
			wavetext$="Wave "+Int(Wave#)
		Else If Wave#=250 Then
			Create3DStarbase()
			wavetext$="Approaching final starbase"
		Else If Wave#=20 Or Wave#=50 Or Wave#=75 Or Wave#=100 Or Wave#=125 Or Wave#=150 Or Wave#=175 Or Wave#=200 Or Wave#=250 Then
			Create3DStarbase()
			wavetext$="Approaching enemy starbase"
		Else If Wave#=251 Then
			wavetext$="Mission Complete"
			Music=False
			StopChannel(CHN_BGM)
			CueDialog("Outro\Outro1.wav","As I blasted the last of those alien scum into oblivion")
			CueDialog("Outro\Outro2.wav","I realized the weight of what I had just done.")
			CueDialog("Outro\Outro3.wav","I had murdered an entire species.")
			CueDialog("Outro\Outro4.wav","Gone. Wiped from existance.")
			CueDialog("Outro\Outro5.wav","This was not a heroic act.")
			CueDialog("Outro\Outro6.wav","Though it had to be done to save my dear Planet 23.")
			CueDialog("Outro\Outro7.wav","Those bastards would have not stopped until everything was destroyed.")
			CueDialog("Outro\Outro8.wav","For they were a product of a more violent age.")
			CueDialog("Outro\Outro9.wav","One that I, too had vicaroiously become apart of.")
			CueDialog("Outro\Outro10.wav","This leaves one last thing to be done...")
			CreateSun()
			For Ast.Asteroid3D = Each Asteroid3D
				HideEntity Ast\Entity
				Delete Ast
			Next
		EndIf
		showwavetext=60
	EndIf
	
	; Game overs
	If Lives#<=-1 And Players=1 Then
		gameover(0)
	EndIf
	
	; Game overs
	If Lives2#<=-1 And Players=2 Then
		WaveText$="Player 2 Died."
		Showwavetext=60
		Players=1
	EndIf
	
	; Game overs
	If Lives#<=-1 And Players=2 Then
		WaveText$="Player 1 Died."
		Showwavetext=60
		Fliped=True
		Lives#=Lives2#
		LazerType#=LazerType2#
		Players=1
		ControlMode#=ControlMode2#
		fliptex=LoadTexture("Mesh\Player2\Fighter.jpg")
		EntityTexture MSH_Player,fliptex
	EndIf

	UpdateWorld()
	RenderWorld()
	
	If draw<>3 Then
		Update3DStarsUp()
		If players=2 Then
			Update3DStarsUp2()
;			Rect 0,GraphicsHeight()/2,GraphicsWidth(),1
		EndIf
	EndIf

				If LazerType#<5 Then
					LazerWorth#=LazerType#
				Else If LazerType#<10 Then
					LazerWorth#=LazerType#/2
				Else If LazerType#<15 Then
					LazerWorth#=LazerType#
				Else If LazerType#<25 Then
					LazerWorth#=LazerType#/2
				Else If LazerType#>=25 Then
					LazerWorth#=LazerType#/4
				EndIf
	

	
	SetBuffer(ImageBuffer(HUDImage))
		Cls
		; Draw HUD
		Color 0,0,255
		Rect 0,FontHeight()*1,Lives#*10,FontHeight()
		Color 255,0,0
		Rect 0,FontHeight()*2,LazerType#*10,FontHeight()
		Color 255,255,255
		Text 0,FontHeight()*1,"Health: "+Int((Lives#/3)*100)+"%"
		Text 0,FontHeight()*2,"Lazer: "+Int(LazerType#)
		Text 0,FontHeight()*3,"Wave: "+Int(Wave#)
		Text 0,FontHeight()*4,"Score: "+Int(score)
		Text 0,FontHeight()*5,"Nuke Cost: "+Str(Int(AdvNumb#*10))
		
		If players=2 Then
			Text 0,0,"Player 1"
			; Draw HUD 2
			Color 0,0,255
			Rect 0,GraphicsHeight()/2+FontHeight()*1,Lives2#*10,FontHeight()
			Color 255,0,0
			Rect 0,GraphicsHeight()/2+FontHeight()*2,LazerType2#*10,FontHeight()
			Color 255,255,255
			Text 0,FontHeight()*0+GraphicsHeight()/2,"Player 2"
			Text 0,FontHeight()*1+GraphicsHeight()/2,"Health: "+Int((Lives2#/3)*100)
			Text 0,FontHeight()*3+GraphicsHeight()/2,"Wave: "+Int(Wave#)
			Text 0,FontHeight()*2+GraphicsHeight()/2,"Lazer: "+Int(LazerType2#)
			Text 0,FontHeight()*4+GraphicsHeight()/2,"Score: "+Int(score)
			Text 0,FontHeight()*5+GraphicsHeight()/2,"Nuke Cost: "+Str(Int(AdvNumb#*10))
		EndIf
			
		If draw<>3 Then
			For a.adversary3D = Each adversary3D
				If a\Health#>1 And EntityZ(A\Entity)>=-120 Then
					CameraProject camera,EntityX(A\Entity),EntityY(A\Entity)+5,EntityZ(A\Entity)
					Color 255,0,0
					Text ProjectedX(),ProjectedY(),Int(A\Health#)
					CameraProject camera2,EntityX(A\Entity),EntityY(A\Entity)+5,EntityZ(A\Entity)
					If players=2 Then Text ProjectedX(),ProjectedY()+GraphicsHeight()/2,Int(A\Health#)
				EndIf
			Next
			For S.Starbase3D = Each Starbase3D
				If S\Health#>1 Then
					CameraProject camera,EntityX(S\Entity),EntityY(S\Entity)+5,EntityZ(S\Entity)
					Color 255,0,0
					Text ProjectedX(),ProjectedY(),Int(S\Health#)
					CameraProject camera2,EntityX(S\Entity),EntityY(S\Entity)+5,EntityZ(S\Entity)
					If Players=2 Then Text ProjectedX(),ProjectedY()+GraphicsHeight()/2,Int(S\Health#)
				EndIf
			Next
		EndIf
		Color 255,255,255
		
		If Showwavetext>0 Then
			Color 255,0,0
			Text ImageWidth(HUDImage)/2,ImageHeight(HUDImage)/2,wavetext$,True,True
			Showwavetext=showwavetext-1
			Color 255,255,255
		EndIf
		UpdateDialog()
	SetBuffer(BackBuffer())
	TileImage HUDImage
	
	If KeyHit(88) Then
		SaveBuffer(FrontBuffer(),"Screenshot"+Str(Int(Sshot#))+".bmp")
		Sshot#=Sshot#+1
	EndIf
;Cls ;------------------------------------------------------------------------------------------------REMOVE THIS!!!
VWait
Flip 0
WaitTimer(FrameWait)
Wend
Exitit()
End Function

Function Exitit()
	tex1=LoadTexture("Mesh\Player1\Fighter.jpg")
	EntityTexture MSH_Player,tex1
	tex2=LoadTexture("Mesh\Player2\Fighter.jpg")
	EntityTexture MSH_Player2,tex2

StopChannel SFX_Outro
FreeImage pauseScreen
FreeImage HUDImage

For b.bullet3d = Each Bullet3d
	HideEntity B\Entity
	Delete B
Next

For Su.Sun = Each sun
	HideEntity Su\Entity
	Delete Su
Next

For A3.Adversary3d = Each Adversary3D
	HideEntity A3\Entity
	Delete A3
Next

For A2.Adversary2d = Each Adversary2D
	Delete A2
Next

For S.Star = Each Star
	Delete S
Next

For S3.Star3D = Each Star3D
	Delete S3
Next

For P.PowerUp = Each PowerUp
	Delete P
Next

For P3.PowerUp3D = Each PowerUp3D
	HideEntity P3\Entity
	Delete P3
Next

For As.Asteroid3D = Each Asteroid3D
	HideEntity As\Entity
	Delete As
Next

For St.Starbase3D = Each Starbase3D
	HideEntity St\Entity
	Delete St
Next

HideEntity box1
HideEntity box2
HideEntity World
HideEntity World2
HideEntity Shield
HideEntity Shield2

FlushMouse()
FlushKeys()
DebugLog "Score:"+Str(score)
		lowest = hiscore$(9,2)							; Get lowest hiscore.
		If score < lowest								; Score lower than that?
		Else											; Score higher than lowest hiscore?
			FlushKeys()
			enter_hiscore()								; Let the player enter his/hers name.
		EndIf
Show_hiscores(False)
MainMenu(1,0,1,1,"BGM\happygalaxy.mp3")
End Function

Function Load2dGFX()
; Menu Graphics
GFX_Menu_Back=LoadImage("GFX\Menu\Back.jpg")
	If Not iAdditional
		ResizeImage GFX_Menu_Back,GraphicsWidth(),GraphicsHeight()
	Else
		ResizeImage GFX_Menu_Back,GraphicsWidth()/2,GraphicsHeight()
	EndIf
GFX_Menu_Pointer=LoadImage("GFX\Menu\pointer.bmp")
	MaskImage GFX_Menu_Pointer,255,0,255
GFX_Menu_Exit_Game=LoadImage("GFX\Menu\Exit_Game.bmp")
	ResizeImage GFX_Menu_Exit_Game,GraphicsWidth()/5,GraphicsHeight()/10
	MaskImage GFX_Menu_Exit_Game,255,0,255
	MidHandle GFX_Menu_Exit_Game
GFX_Menu_Options=LoadImage("GFX\Menu\Options.bmp")
	ResizeImage GFX_Menu_Options,GraphicsWidth()/5,GraphicsHeight()/10
	MaskImage GFX_Menu_Options,255,0,255
	MidHandle GFX_Menu_Options
GFX_Menu_Player=LoadImage("GFX\Menu\Player.bmp")
	ResizeImage GFX_Menu_Player,GraphicsWidth()/5,GraphicsHeight()/10
	MaskImage GFX_Menu_Player,255,0,255
	MidHandle GFX_Menu_Player
GFX_Menu_Logo=LoadImage("GFX\Menu\Logo.bmp")
	MaskImage GFX_Menu_Logo,255,0,255
	If Not iAdditional
		ResizeImage GFX_Menu_Logo,GraphicsWidth(),GraphicsHeight()
	Else
		ResizeImage GFX_Menu_Logo,GraphicsWidth()/2,GraphicsHeight()
	EndIf
GFX_Menu_Mouse=LoadImage("GFX\Menu\Mouse.bmp")
	MaskImage GFX_Menu_Mouse,255,0,255
	MidHandle GFX_Menu_Mouse
GFX_Menu_Key=LoadImage("GFX\Menu\Key.bmp")
	MaskImage GFX_Menu_Key,255,0,255
	MidHandle GFX_Menu_Key
GFX_Menu_Joy1=LoadImage("GFX\Menu\Joy1.bmp")
	MaskImage GFX_Menu_Joy1,255,0,255
	MidHandle GFX_Menu_Joy1
GFX_Menu_Joy2=LoadImage("GFX\Menu\Joy2.bmp")
	MaskImage GFX_Menu_Joy2,255,0,255
	MidHandle GFX_Menu_Joy2
End Function

Function LoadOldGFX() ; <--------------------------------------------------------

; Game Graphics
GFX_Game_Player1=LoadAnimImage("GFX\Game\player1.bmp",174,210,0,3)
	MaskImage GFX_Game_Player1,255,0,255
	ResizeImage GFX_Game_Player1,GraphicsHeight()/10,GraphicsHeight()/10
	MidHandle GFX_Game_Player1
GFX_Game_Player2=LoadAnimImage("GFX\Game\player2.bmp",174,210,0,3)
	MaskImage GFX_Game_Player2,255,0,255
	ResizeImage GFX_Game_Player2,GraphicsHeight()/10,GraphicsHeight()/10
	MidHandle GFX_Game_Player2
GFX_Game_Adversary=LoadImage("GFX\Game\Adversary.bmp")
	MaskImage GFX_Game_Adversary,255,0,255
	ResizeImage GFX_Game_Adversary,GraphicsHeight()/10,GraphicsHeight()/10
	MidHandle GFX_Game_Adversary
GFX_Game_Bullet=LoadImage("GFX\Game\Bullet.bmp")
	MaskImage GFX_Game_Bullet,255,0,255
	ResizeImage GFX_Game_Bullet,GraphicsHeight()/20,GraphicsHeight()/20
	MidHandle GFX_Game_Bullet
GFX_Game_Health=LoadImage("GFX\Game\Health.bmp")
	MaskImage GFX_Game_Health,0,0,0
	ResizeImage GFX_Game_Health,GraphicsWidth()/20,GraphicsHeight()/20
	MidHandle GFX_Game_Health
GFX_Game_Revive=LoadImage("GFX\Game\Revive.bmp")
	MaskImage GFX_Game_Revive,0,0,0
	ResizeImage GFX_Game_Revive,GraphicsWidth()/20,GraphicsHeight()/20
	MidHandle GFX_Game_Revive
GFX_Game_PowerUp=LoadImage("GFX\Game\PowerUp.bmp")
	MaskImage GFX_Game_PowerUp,0,0,0
	ResizeImage GFX_Game_PowerUp,GraphicsWidth()/20,GraphicsHeight()/20
	MidHandle GFX_Game_PowerUp
GFX_Game_TempInv=LoadImage("GFX\Game\TempInv.bmp")
	MaskImage GFX_Game_TempInv,0,0,0
	ResizeImage GFX_Game_TempInv,GraphicsWidth()/20,GraphicsHeight()/20
	MidHandle GFX_Game_TempInv
GFX_Game_Explosion=LoadAnimImage("GFX\Game\Explosion.bmp",64,64,0,9)
	MaskImage GFX_Game_Explosion,0,0,0
	ResizeImage GFX_Game_Explosion,GraphicsWidth()/20,GraphicsWidth()/20
	MidHandle GFX_Game_Explosion
GFX_Game_Asteroid=LoadImage("GFX\Game\Asteroid.bmp")
	MaskImage GFX_Game_Asteroid,255,0,255
	ResizeImage GFX_Game_Asteroid,GraphicsHeight()/10,GraphicsHeight()/10
	MidHandle GFX_Game_Asteroid
GFX_Game_StarBase=LoadImage("GFX\Game\StarBase.bmp")
	MaskImage GFX_Game_StarBase,255,0,255
	ResizeImage GFX_Game_StarBase,(GraphicsHeight()/10)*2,(GraphicsHeight()/10)*2
	MidHandle GFX_Game_StarBase
End Function

Function Load3dGFX()

	MSH_Bullet=LoadMesh("Mesh\bullet.x")
		ScaleEntity MSH_Bullet,50,50,50
		HideEntity MSH_Bullet
	MSH_Player=LoadMesh("Mesh\Player1\player.3ds")
		RotateEntity MSH_Player,0,180,0
		ScaleEntity MSH_Player,.035,.035,.035
		EntityType MSH_Player, Type_Player
		EntityBox MSH_Player,-20,-20,-20,40,40,40
	MSH_Player2=LoadMesh("Mesh\Player2\player.3ds")
		RotateEntity MSH_Player2,0,180,0
		ScaleEntity MSH_Player2,.035,.035,.035
		EntityType MSH_Player2, Type_Player
		EntityBox MSH_Player2,-20,-20,-20,40,40,40		
	MSH_Adversary=LoadMesh("Mesh\Adversary.3ds")
		ScaleEntity MSH_Adversary,.5,.5,.5
		EntityType MSH_Adversary,Type_Adversary
		HideEntity MSH_Adversary
	MSH_Starbase=LoadMesh("Mesh\Starbase.3ds")
		ScaleEntity MSH_Starbase,.005,.005,.005
		HideEntity MSH_Starbase
	Camera=CreateCamera()
			CameraClsMode Camera,0,1
	Camera2=CreateCamera()
		CameraClsMode Camera2,0,1
	Ear=CreateListener(Camera,0,1,1)
	Light=CreateLight(2)
		PositionEntity Light,0,400,-200

	MoveEntity camera,-1,0,0
	Camera_1=CreateCamera(camera)
	MoveEntity camera_1,2,0,0
	CameraClsMode Camera,0,1
	CameraClsMode Camera_1,0,1
	MoveEntity camera2,-1,0,0
	CameraProjMode camera_1,0
	Camera2_1=CreateCamera(camera2)
	MoveEntity camera2_1,2,0,0
	CameraClsMode Camera2,0,1
	CameraClsMode Camera2_1,0,1
	CameraProjMode camera2_1,0
	
End Function

Function Load2DSFX()
	
	SFX_Click=LoadSound("SFX\Click.wav")
	SFX_Fire=LoadSound("SFX\Fire.wav")
	SFX_Boom=LoadSound("SFX\Boom.wav")
	SFX_Powerup=LoadSound("SFX\Powerup.wav")
	SFX_Heal=LoadSound("SFX\Heal.wav")
	SFX_Bang=LoadSound("SFX\Bang.wav")
	SFX_FORCEFI=LoadSound("SFX\FORCEFI.WAV")
	SFX_PROUT=LoadSound("SFX\PROUT.WAV")
	SFX_AHHHH=LoadSound("SFX\AHHHH.WAV")
	SFX_Nuke=LoadSound("SFX\Nuke.wav")
	
End Function

Function Load3DSFX()
	
	SFX3_Click=Load3DSound("SFX\Click.wav")
	SFX3_Fire=Load3DSound("SFX\Fire.wav")
	SFX3_Boom=Load3DSound("SFX\Boom.wav")
	SFX3_Powerup=Load3DSound("SFX\Powerup.wav")
	SFX3_Heal=Load3DSound("SFX\Heal.wav")
	SFX3_Bang=Load3DSound("SFX\ELECTRI.wav")
	SFX3_MOD=Load3DSound("SFX\MODULAT1.wav")
	SFX3_MOD3=Load3DSound("SFX\MODULAT3.wav")
	SFX3_CRASHBUZ=Load3DSound("SFX\CRASHBUZ.WAV")
	SFX3_WHAUE=Load3DSound("SFX\WHAUE.WAV")
	SFX3_SCORPIO=Load3DSound("SFX\SCORPIO.WAV")
	
	SoundVolume SFX3_MOD,.3
	SoundVolume SFX3_MOD3,.3
	
	MenuMusic$="SATELL.S3M"
	
End Function

Function GameOver(demomode)
	Cls
	If demomode=True Then
		Text GraphicsWidth()/2,GraphicsHeight()/2,"Demo Over",1,1
		Text GraphicsWidth()/2,(GraphicsHeight()/2)+FontHeight(),"Please buy the full version.",1,1
	Else
		Text GraphicsWidth()/2,GraphicsHeight()/2,"Game Over",1,1
	EndIf
	Flip
	Delay 4000
	Alive=False
End Function

Function Password(Passkey$)
	If Instr(PassKey$,"=")<>0 Then
		Pass$=Left(PassKey$,Instr(PassKey$,"=")-1)
		Key$=Mid(PassKey$,Instr(PassKey$,"=")+1,StringWidth(PassKey$)-Instr(PassKey$,"=")+1)
		Select Lower(Pass$):
			Case "warp"
				For A.Adversary3D = Each Adversary3D
					HideEntity A\Entity
					Delete A
				Next
				Wave#=Key
				Wave#=Wave#-1
			Case "zap"
				LazerType#=key
				LazerType2#=key
			Case "energy"
				Lives#=key
				Lives2#=key
		End Select
	Else
		Select Lower(PassKey$):
			Case "tank"
				InvType#=1
				TempInv#=TempInv#+255
				If Players=2 Then
					TempInv2#=TempInv2#+255
					InvType2#=1
				EndIf					
			Case "revive"
				If TotalPlayers=2 And Players=1 Then
					If Fliped=False Then
						RevivePlayer2=True
					Else
						RevivePlayer1=True
					EndIf
				EndIf
			Case "plastic monkey"
				Cls
				Text GraphicsWidth()/2,GraphicsHeight()/2,"Tim farted on this.",1,1
				Flip
				Delay 1000
			Case "fat people"
				ScaleEntity MSH_Player,20,20,20
				ScaleEntity MSH_Player2,20,20,20
		End Select
	EndIf
	
	DebugLog("Used Password: "+Passkey$)

End Function

Function Intro()
	Cls
	
	CueMusicTrack("BGM\galacticmist.mp3")

	If iAdditional Then
		CameraViewport camera,0,0,GraphicsWidth()/2,GraphicsHeight()
		CameraViewport camera_1,GraphicsWidth()/2,0,GraphicsWidth()/2,GraphicsHeight()
		CameraProjMode camera_1,1
		CameraProjMode camera2,0
		CameraProjMode camera2_1,0
	Else
		CameraProjMode camera_1,0
		CameraProjMode camera2,0
		CameraProjMode camera2_1,0
	EndIf
	
	For Rept = 1 To 800
		Create3DStar()
		Update3DStarsDown()
	Next
	
	Create3DStarbase()
	
	CueDialog("Intro\Intro1.wav","When a team of scientists discovered an ancient starbase,")
	CueDialog("Intro\Intro2.wav","it looked like it would be the greatest scientific breakthrough in decades.")
	CueDialog("Intro\Intro3.wav","Alas, when they approached, it activated.")
	CueDialog("Intro\Intro4.wav","A swarm of alien vessel began pouring out of the thing.")
	CueDialog("Intro\Intro5.wav","They began destroying everything in sight,")
	CueDialog("Intro\Intro6.wav","making short work of the scientists and devastating the area.")
	CueDialog("Intro\Intro7.wav","Alarmed, the Planet 23 government enlisted the best pilots")
	CueDialog("Intro\Intro8.wav","to launch a counter assault.")
	CueDialog("Intro\Intro5.wav","I am one of those pilots.")
	
	If iAdditional Then
		HUDImage = CreateImage(GraphicsWidth()/2,GraphicsHeight())
	Else
		HUDImage = CreateImage(GraphicsWidth(),GraphicsHeight())
	EndIf
	
	PositionEntity MSH_Player,50,3,-100
	PositionEntity MSH_Player2,-50,3,-100
	
	EntityAlpha MSH_Player,0
	EntityAlpha MSH_Player2,0
	PositionEntity Camera,0,18,-117.5
	
	Timeout=0
	While Not KeyHit(1) Or JoyHit(1) Or timeout=900 Or KeyHit(57) Or JoyHit(8) Or MouseHit(1)
	timeout = Timeout+1
		Cls
		Wave#=50		
		AdvNumb#=0
		For A3D.Adversary3D = Each Adversary3d
			Update3DAdversary(A3D,1,0,0,0,0,1)
			AdvNumb#=AdvNumb#+1
		Next
		Update3DStarbase(0,0,0,0)
		
		Update3DStarsUp()
		Update3DStarsDown()
		Update3DExplosions()
		
		UpdateWorld()
		RenderWorld()
		
		SetBuffer(ImageBuffer(HUDImage))
		Cls
		UpdateDialog()
		SetBuffer(BackBuffer())
		TileImage HUDImage
		Flip
		WaitTimer(FrameWait)
	Wend
	StopChannel(CHN_DIALOG)
	FreeImage HUDImage
	For A3.Adversary3d = Each Adversary3D
		HideEntity A3\Entity
		Delete A3
	Next
	For St.Starbase3D = Each Starbase3D
		HideEntity St\Entity
		Delete St
	Next
	For S3.Star3D = Each Star3D
		Delete S3
	Next
	For Di.Dialog = Each Dialog
		Delete Di
	Next
	ShowSubtitle#=0
End Function
