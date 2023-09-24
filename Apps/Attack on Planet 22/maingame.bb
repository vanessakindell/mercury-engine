Global GameCode#=23
Global SShot#=0

Function MainGame()
Local Direction#
Local Change#
Local Alive=True
Local FrameWait=CreateTimer(30)
Local PlanetY#=ImageHeight(GFX_Game_Planet)/8
LazerType#=1
Lives#=3
Score#=0

PlayerX#=GraphicsWidth()/2

MoveMouse GraphicsWidth()/2,GraphicsHeight()/2

Wave#=1
Create2DWave(0,5)

For Rept = 1 To GraphicsHeight()
	CreateStar()
		UpdateStars()
Next

CueMusicTrack("BGM\Game.mp3")

While Alive=True
Cls

	HandleMusic()
	
	If KeyHit(61) Then
		If Music=True Then
			Music=False
			StopChannel CHN_BGM
		Else
			Music=True
		EndIf
	EndIf
	
	CreateStar()
	UpdateStars()
	
	TileImage GFX_Game_Planet,0,PlanetY#
	PlanetY#=PlanetY#+1
	If PlanetY#>ImageHeight(GFX_Game_Planet) Then PlanetY#=0
	
	UpdateExplosions()

	Direction#=MouseXSpeed()+1
	
	If Direction#<0 Then Direction#=0
	If Direction#>2 Then Direction#=2
	
	If KeyDown(203) And MouseX()>0 Then
		Direction#=0
		MoveMouse MouseX()-8,MouseY()
	EndIf
	
	If KeyDown(205) And MouseX()<GraphicsWidth()-ImageWidth(GFX_Game_Player1) Then
		Direction#=2
		MoveMouse MouseX()+8,MouseY()
	EndIf
	
	
	If KeyHit(1) Then
		FlushKeys()
		While Not KeyHit(28)
			Cls
			Text GraphicsWidth()/2,GraphicsHeight()/2+(FontHeight()*0),"Game paused",1,1
			Text GraphicsWidth()/2,(GraphicsHeight()/2)+(FontHeight()*1),"Press enter to continue.",1,1
			Flip
			If KeyHit(1) Then Exitit()
		Wend
	EndIf

	PlayerX#=MouseX()
	
	If PlayerX#>GraphicsWidth()-ImageWidth(GFX_Game_Player1) Then PlayerX#=GraphicsWidth()-(ImageWidth(GFX_Game_Player1)/2)
	If PlayerX#<ImageWidth(GFX_Game_Player1)/2 Then PlayerX#=ImageWidth(GFX_Game_Player1)/2

	If LazerType#<=5 Then
		If KeyHit(57) Or MouseHit(1) Then
			Create2DBullet(PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	Else If LazerType<=10 Then
		If KeyHit(57) Or MouseHit(1) Then
			Create2DBullet(PlayerX#+(ImageWidth(GFX_Game_Player1)/3),GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
			Create2DBullet(PlayerX#-(ImageWidth(GFX_Game_Player1)/3),GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	Else If LazerType<=15 Then
		If KeyDown(57) Or MouseDown(1) Then
			Create2DBullet(PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	Else If LazerType>15 Then
		If KeyDown(57) Or MouseDown(1) Then
			Create2DBullet(PlayerX#+(ImageWidth(GFX_Game_Player1)/3),GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
			Create2DBullet(PlayerX#-(ImageWidth(GFX_Game_Player1)/3),GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	EndIf
	If MouseHit(2) Then
		For B.Bullet2D = Each Bullet2d
			CreateExplosion(B\X#,B\Y#)
			Delete B
		Next
		CHN_SFX=Playsfx(SFX_Boom)
	EndIf

	Update2DBullet()
	UpdatePowerups()
	
	DrawImage GFX_Game_Player1,PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),Direction#

	AdvNumb#=0
	For A.Adversary2D = Each Adversary2d
		Update2DAdversary(A,LazerType#,1,1,0,0)
		AdvNumb#=AdvNumb#+1
	Next
	If AdvNumb# = 0 Then
		Wave#=Wave#+1
		Create2DWave(0,5)
		Playsfx SFX_Round
;		Cls
;		Text GraphicsWidth()/2,GraphicsHeight()/2,"NEW WAVE INCOMMING!",True,True
;		Flip
;		Delay 1000
	EndIf
	
	; Game overs
	If Lives#<=-1 Then
		Cls
		UpdateStars()
		Text 0,FontHeight()*0,"Health: 0"
		Text 0,FontHeight()*1,"Level: "+Int(Wave#)
		Text 0,FontHeight()*2,"Lazer: "+Int(LazerType#)
		Text 0,FontHeight()*3,"Score: "+Int(Score#)
		TileImage GFX_Game_Planet,0,PlanetY#
		Text GraphicsWidth()/2,GraphicsHeight()/2,"Game Over",1,1
		Flip
		Alive=False
	EndIf

	; Draw HUD
	Text 0,FontHeight()*0,"Health: "+Int((Lives#/3)*100)+"%"
	Text 0,FontHeight()*1,"Level: "+Int(Wave#)
	Text 0,FontHeight()*2,"Lazer: "+Int(LazerType#)
	Text 0,FontHeight()*3,"Score: "+Int(Score#)
	
		If KeyHit(88) Then
			SaveBuffer(FrontBuffer(),"Screenshot"+Str(Int(Sshot#))+".bmp")
			Sshot#=Sshot#+1
		EndIf
	
Flip
WaitTimer(FrameWait)
Wend
End Function

Function Exitit()
	lowest = hiscore$(9,2)							; Get lowest hiscore.
	If score# < lowest								; Score lower than that?
		show_hiscores(0,1)								; Show current hiscores.
	Else											; Score higher than lowest hiscore?
		FlushKeys()
		enter_hiscore()								; Let the player enter his/hers name.
		If iAdditional Then remote_hiscores(1)
		show_hiscores(0,1)
	EndIf
	
	For b.bullet2d = Each Bullet2d
		Delete B
	Next
	
	For A.Adversary2d = Each Adversary2D
		Delete A
	Next
	
	For S.Star = Each Star
		Delete S
	Next
	
	For P.PowerUp = Each PowerUp
		Delete P
	Next
	
	FlushMouse()
	FlushKeys()
	MainMenu(1,0,0,False,"BGM\MainMenu.mp3",True)
End Function

Function NetworkGame()
	
End Function

Function Load2dGFX()
; Menu Graphics
GFX_Menu_Back=LoadImage("GFX\Menu\Back.jpg")
	ResizeImage GFX_Menu_Back,GraphicsWidth(),GraphicsHeight()
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
GFX_Menu_Network=LoadImage("GFX\Menu\Network.bmp")
	ResizeImage GFX_Menu_Network,GraphicsWidth()/5,GraphicsHeight()/10
	MaskImage GFX_Menu_Network,255,0,255
	MidHandle GFX_Menu_Network
GFX_Menu_Player=LoadImage("GFX\Menu\Player.bmp")
	ResizeImage GFX_Menu_Player,GraphicsWidth()/5,GraphicsHeight()/10
	MaskImage GFX_Menu_Player,255,0,255
	MidHandle GFX_Menu_Player
GFX_Menu_Logo=LoadImage("GFX\Menu\Logo.bmp")
	MaskImage GFX_Menu_Logo,255,0,255
	ResizeImage GFX_Menu_Logo,GraphicsWidth(),GraphicsHeight()

; Game Graphics
GFX_Game_Player1=LoadAnimImage("GFX\Game\player1.bmp",174,210,0,3)
	MaskImage GFX_Game_Player1,255,0,255
	ResizeImage GFX_Game_Player1,GraphicsHeight()/10,GraphicsHeight()/10
	MidHandle GFX_Game_Player1
GFX_Game_Adversary=LoadImage("GFX\Game\Adversary.bmp")
	MaskImage GFX_Game_Adversary,255,0,255
	ResizeImage GFX_Game_Adversary,GraphicsHeight()/10,GraphicsHeight()/10
	MidHandle GFX_Game_Adversary
GFX_Game_Bullet=LoadImage("GFX\Game\Bullet.bmp")
	MaskImage GFX_Game_Bullet,255,0,255
	ResizeImage GFX_Game_Bullet,GraphicsHeight()/20,GraphicsHeight()/20
	MidHandle GFX_Game_Bullet
GFX_Game_Health=LoadImage("GFX\Game\Health.bmp")
	MaskImage GFX_Game_Health,255,0,255
	ResizeImage GFX_Game_Health,GraphicsWidth()/20,GraphicsHeight()/20
	MidHandle GFX_Game_Health
GFX_Game_PowerUp=LoadImage("GFX\Game\PowerUp.bmp")
	MaskImage GFX_Game_PowerUp,255,0,255
	ResizeImage GFX_Game_PowerUp,GraphicsWidth()/20,GraphicsHeight()/20
	MidHandle GFX_Game_PowerUp
GFX_Game_Explosion=LoadAnimImage("GFX\Game\Explosion.bmp",64,64,0,9)
	MaskImage GFX_Game_Explosion,0,0,0
	ResizeImage GFX_Game_Explosion,GraphicsWidth()/20,GraphicsWidth()/20
	MidHandle GFX_Game_Explosion
GFX_Game_Planet=LoadImage("GFX\Game\Planet.bmp")
	MaskImage GFX_Game_Planet,255,0,255
	ResizeImage GFX_Game_Planet,GraphicsWidth(),GraphicsHeight()*8
End Function

Function LoadSFX()
	
	SFX_Click=LoadSound("SFX\Click.wav")
	SFX_Fire=LoadSound("SFX\Fire.wav")
	SFX_Boom=LoadSound("SFX\Boom.wav")
	SFX_Powerup=LoadSound("SFX\Powerup.wav")
	SFX_Heal=LoadSound("SFX\Heal.wav")
	SFX_Round=LoadSound("SFX\Round.wav")
	SFX_Bang=LoadSound("SFX\Bang.wav")
	
End Function