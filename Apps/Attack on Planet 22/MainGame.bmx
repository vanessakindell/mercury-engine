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

PlayerX#=dev.blitz3d.GraphicsWidth()/2

MoveMouse dev.blitz3d.GraphicsWidth()/2,dev.blitz3d.GraphicsHeight()/2

Wave#=1
Create2DWave(0,5)

For Rept = 1 To dev.blitz3d.GraphicsHeight()
	CreateStar()
		UpdateStars()
Next

CueMusicTrack("BGM\Game.mp3")

While Alive=True
Cls

	HandleMusic()
	
	If VKeyHit(61) Then
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
	
	If VKeyDown(203) And MouseX()>0 Then
		Direction#=0
		MoveMouse MouseX()-8,MouseY()
	EndIf
	
	If VKeyDown(205) And MouseX()<dev.blitz3d.GraphicsWidth()-ImageWidth(GFX_Game_Player1) Then
		Direction#=2
		MoveMouse MouseX()+8,MouseY()
	EndIf
	
	
	If VKeyHit(1) Then
		FlushKeys()
		While Not VKeyHit(28)
			Cls
			Text dev.blitz3d.GraphicsWidth()/2,dev.blitz3d.GraphicsHeight()/2+(FontHeight()*0),"Game paused",1,1
			Text dev.blitz3d.GraphicsWidth()/2,(dev.blitz3d.GraphicsHeight()/2)+(FontHeight()*1),"Press enter to continue.",1,1
			Flip
			If VKeyHit(1) Then Exitit()
		Wend
	EndIf

	PlayerX#=MouseX()
	
	If PlayerX#>dev.blitz3d.GraphicsWidth()-ImageWidth(GFX_Game_Player1) Then PlayerX#=dev.blitz3d.GraphicsWidth()-(ImageWidth(GFX_Game_Player1)/2)
	If PlayerX#<ImageWidth(GFX_Game_Player1)/2 Then PlayerX#=ImageWidth(GFX_Game_Player1)/2

	If LazerType#<=5 Then
		If VKeyHit(57) | MouseHit(1) Then
			Create2DBullet(PlayerX#,dev.blitz3d.GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	Else If LazerType<=10 Then
		If VKeyHit(57) | MouseHit(1) Then
			Create2DBullet(PlayerX#+(ImageWidth(GFX_Game_Player1)/3),dev.blitz3d.GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
			Create2DBullet(PlayerX#-(ImageWidth(GFX_Game_Player1)/3),dev.blitz3d.GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	Else If LazerType<=15 Then
		If VKeyDown(57) | MouseDown(1) Then
			Create2DBullet(PlayerX#,dev.blitz3d.GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	Else If LazerType>15 Then
		If VKeyDown(57) | MouseDown(1) Then
			Create2DBullet(PlayerX#+(ImageWidth(GFX_Game_Player1)/3),dev.blitz3d.GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
			Create2DBullet(PlayerX#-(ImageWidth(GFX_Game_Player1)/3),dev.blitz3d.GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	EndIf
	If MouseHit(2) Then
		For B:bbBullet2D = EachIn Bullet2d_list
			CreateExplosion(B.X#,B.Y#)
			CHN_SFX=Playsfx(SFX_Boom)
			SetChannelPan CHN_SFX,(B.X#-(dev.blitz3d.GraphicsWidth()/2))/(dev.blitz3d.GraphicsWidth()/2)
			B.Remove()
		Next
	EndIf

	Update2DBullet()
	UpdatePowerups()
	
	DrawImage GFX_Game_Player1,PlayerX#,dev.blitz3d.GraphicsHeight()-ImageHeight(GFX_Game_Player1),Direction#

	AdvNumb#=0
	For A:bbAdversary2D = EachIn Adversary2d_list
		Update2DAdversary(A,LazerType#,1,1,0,0)
		AdvNumb#=AdvNumb#+1
	Next
	If AdvNumb# = 0 Then
		Wave#=Wave#+1
		Create2DWave(0,5)
		Playsfx SFX_Round
'		Cls
'		Text GraphicsWidth()/2,GraphicsHeight()/2,"NEW WAVE INCOMMING!",True,True
'		Flip
'		Delay 1000
	EndIf
	
	' Game overs
	If Lives#<=-1 Then
		Cls
		UpdateStars()
		Text 0,FontHeight()*0,"Health: 0"
		Text 0,FontHeight()*1,"Level: "+Int(Wave#)
		Text 0,FontHeight()*2,"Lazer: "+Int(LazerType#)
		Text 0,FontHeight()*3,"Score: "+Int(Score#)
		TileImage GFX_Game_Planet,0,PlanetY#
		Text dev.blitz3d.GraphicsWidth()/2,dev.blitz3d.GraphicsHeight()/2,"Game Over",1,1
		Flip
		Alive=False
	EndIf

	' Draw HUD
	Text 0,FontHeight()*0,"Health: "+Int((Lives#/3)*100)+"%"
	Text 0,FontHeight()*1,"Level: "+Int(Wave#)
	Text 0,FontHeight()*2,"Lazer: "+Int(LazerType#)
	Text 0,FontHeight()*3,"Score: "+Int(Score#)
	
		If VKeyHit(88) Then
			SaveBuffer(FrontBuffer(),"Screenshot"+String(Int(Sshot#))+".bmp")
			Sshot#=Sshot#+1
		EndIf
	
Flip
WaitTimer(FrameWait)
Wend
End Function

Function Exitit()
	lowest = hiscore$(9,2)							' Get lowest hiscore.
	If score# < lowest								' Score lower than that?
		show_hiscores(0,1)								' Show current hiscores.
	Else											' Score higher than lowest hiscore?
		FlushKeys()
		enter_hiscore()								' Let the player enter his/hers name.
		If iAdditional Then remote_hiscores(1)
		show_hiscores(0,1)
	EndIf
	
	For b:bbbullet2d = EachIn Bullet2d_list
		B.Remove()
	Next
	
	For A:bbAdversary2d = EachIn Adversary2D_list
		A.Remove()
	Next
	
	For S:bbStar = EachIn Star_list
		S.Remove()
	Next
	
	For P:bbPowerUp = EachIn PowerUp_list
		P.Remove()
	Next
	
	FlushMouse()
	FlushKeys()
	MainMenu(1,0,0,False,"BGM\MainMenu.mp3",True)
End Function

Function NetworkGame()
	
End Function

Function Load2dGFX()
' Menu Graphics
GFX_Menu_Back=LoadImage("GFX\Menu\Back.jpg")
	ResizeImage GFX_Menu_Back,dev.blitz3d.GraphicsWidth(),dev.blitz3d.GraphicsHeight()
GFX_Menu_Pointer=LoadImage("GFX\Menu\pointer.bmp")
	MaskImage GFX_Menu_Pointer,255,0,255
GFX_Menu_Exit_Game=LoadImage("GFX\Menu\Exit_Game.bmp")
	ResizeImage GFX_Menu_Exit_Game,dev.blitz3d.GraphicsWidth()/5,dev.blitz3d.GraphicsHeight()/10
	MaskImage GFX_Menu_Exit_Game,255,0,255
	MidHandle GFX_Menu_Exit_Game
GFX_Menu_Options=LoadImage("GFX\Menu\Options.bmp")
	ResizeImage GFX_Menu_Options,dev.blitz3d.GraphicsWidth()/5,dev.blitz3d.GraphicsHeight()/10
	MaskImage GFX_Menu_Options,255,0,255
	MidHandle GFX_Menu_Options
GFX_Menu_Network=LoadImage("GFX\Menu\Network.bmp")
	ResizeImage GFX_Menu_Network,dev.blitz3d.GraphicsWidth()/5,dev.blitz3d.GraphicsHeight()/10
	MaskImage GFX_Menu_Network,255,0,255
	MidHandle GFX_Menu_Network
GFX_Menu_Player=LoadImage("GFX\Menu\Player.bmp")
	ResizeImage GFX_Menu_Player,dev.blitz3d.GraphicsWidth()/5,dev.blitz3d.GraphicsHeight()/10
	MaskImage GFX_Menu_Player,255,0,255
	MidHandle GFX_Menu_Player
GFX_Menu_Logo=LoadImage("GFX\Menu\Logo.bmp")
	MaskImage GFX_Menu_Logo,255,0,255
	ResizeImage GFX_Menu_Logo,dev.blitz3d.GraphicsWidth(),dev.blitz3d.GraphicsHeight()

' Game Graphics
GFX_Game_Player1=LoadAnimImage("GFX\Game\player1.bmp",174,210,0,3)
	MaskImage GFX_Game_Player1,255,0,255
	ResizeImage GFX_Game_Player1,dev.blitz3d.GraphicsHeight()/10,dev.blitz3d.GraphicsHeight()/10
	MidHandle GFX_Game_Player1
GFX_Game_Adversary=LoadImage("GFX\Game\Adversary.bmp")
	MaskImage GFX_Game_Adversary,255,0,255
	ResizeImage GFX_Game_Adversary,dev.blitz3d.GraphicsHeight()/10,dev.blitz3d.GraphicsHeight()/10
	MidHandle GFX_Game_Adversary
GFX_Game_Bullet=LoadImage("GFX\Game\Bullet.bmp")
	MaskImage GFX_Game_Bullet,255,0,255
	ResizeImage GFX_Game_Bullet,dev.blitz3d.GraphicsHeight()/20,dev.blitz3d.GraphicsHeight()/20
	MidHandle GFX_Game_Bullet
GFX_Game_Health=LoadImage("GFX\Game\Health.bmp")
	MaskImage GFX_Game_Health,255,0,255
	ResizeImage GFX_Game_Health,dev.blitz3d.GraphicsWidth()/20,dev.blitz3d.GraphicsHeight()/20
	MidHandle GFX_Game_Health
GFX_Game_PowerUp=LoadImage("GFX\Game\PowerUp.bmp")
	MaskImage GFX_Game_PowerUp,255,0,255
	ResizeImage GFX_Game_PowerUp,dev.blitz3d.GraphicsWidth()/20,dev.blitz3d.GraphicsHeight()/20
	MidHandle GFX_Game_PowerUp
GFX_Game_Explosion=LoadAnimImage("GFX\Game\Explosion.bmp",64,64,0,9)
	MaskImage GFX_Game_Explosion,0,0,0
	ResizeImage GFX_Game_Explosion,dev.blitz3d.GraphicsWidth()/20,dev.blitz3d.GraphicsWidth()/20
	MidHandle GFX_Game_Explosion
GFX_Game_Planet=LoadImage("GFX\Game\Planet.bmp")
	MaskImage GFX_Game_Planet,255,0,255
	ResizeImage GFX_Game_Planet,dev.blitz3d.GraphicsWidth(),dev.blitz3d.GraphicsHeight()*8
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
