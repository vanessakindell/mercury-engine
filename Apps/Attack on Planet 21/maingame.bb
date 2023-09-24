Global Gamecode#=21

Function MainGame()

CueMusicTrack("BGM\load.mp3")

While KeyHit(57)=False And KeyHit(28)=False And MouseHit(1)=False
	Cls
	HandleMusic()
	Text GraphicsWidth()/2,GraphicsHeight()/2+FontHeight()*-2, "MISSION:",1,1
	Text GraphicsWidth()/2,GraphicsHeight()/2+FontHeight()*0, "The dark red ship is the enemy, the orange are neutral!",1,1
	Text GraphicsWidth()/2,GraphicsHeight()/2+FontHeight()*1, "Destroy the enemy and protect the neutral!",1,1
	Text GraphicsWidth()/2,GraphicsHeight()/2+FontHeight()*2, "Press fire to start!",1,1
	Flip
Wend

	Cls
	Text 0,0,"Loading..."
	Flip
	
Local Direction#
Local Change#
Local Alive=True
LazerType#=1
Lives#=3
Score#=0

Local BackY#=0

PlayerX#=GraphicsWidth()/2

MoveMouse GraphicsWidth()/2,GraphicsHeight()/2

Wave#=1
Create2DWave(1,1)

For Rept = 1 To GraphicsHeight()
	CreateStar()
		UpdateStars()
Next

timer = CreateTimer(30)

CueMusicTrack("BGM\game.mp3")

SetBuffer(BackBuffer())
While KeyHit(1)=False And Alive=True
Cls
	
	HandleMusic()

	TileImage GFX_Menu_Back,0,BackY#
	BackY#=BackY#+1
	If BackY#>ImageHeight(GFX_Menu_Back) Then
		BackY#=0
	EndIf

	Direction#=MouseXSpeed()+1
	
	If Direction#<0 Then Direction#=0
	If Direction#>2 Then Direction#=2
	
	If KeyDown(203) And MouseX()>0 Then
		Direction#=0
		MoveMouse MouseX()-4,MouseY()
	EndIf
	
	If KeyDown(205) And MouseX()<GraphicsWidth()-ImageWidth(GFX_Game_Player1) Then
		Direction#=2
		MoveMouse MouseX()+4,MouseY()
	EndIf

	PlayerX#=MouseX()
	
	If PlayerX#>GraphicsWidth()-ImageWidth(GFX_Game_Player1) Then PlayerX#=GraphicsWidth()-(ImageWidth(GFX_Game_Player1)/2)
	If PlayerX#<ImageWidth(GFX_Game_Player1)/2 Then PlayerX#=ImageWidth(GFX_Game_Player1)/2

	If LazerType#<5 Then
		If KeyHit(57) Or MouseHit(1) Then
			Create2DBullet(PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	Else
		If KeyDown(57) Or MouseDown(1) Then
			Create2DBullet(PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),1,LazerType#)
		EndIf
	EndIf
	If MouseHit(2) Then
		For B.Bullet2D = Each Bullet2d
			CreateExplosion(B\X#,B\Y#)
			CHN_SFX=PlaySound(SFX_Boom)
			ChannelPan CHN_SFX,(B\X#-(GraphicsWidth()/2))/(GraphicsWidth()/2)
			Delete B
		Next
	EndIf
	
	DrawImage GFX_Game_Player1,PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),Direction#

	Update2DBullet()
	UpdatePowerups()
	UpdateExplosions()

	AdvNumb#=0
	For A.Adversary2D = Each Adversary2d
		Update2DAdversary(A,LazerType#,0,1,0,0)
		AdvNumb#=AdvNumb#+1
	Next
	If AdvNumb# = 0 Then
		Wave#=Wave#+1
		Create2DWave(1,Wave#)
		PlaySound SFX_Round
;		Cls
;		Text GraphicsWidth()/2,GraphicsHeight()/2,"NEW WAVE INCOMMING!",True,True
;		Flip
;		Delay 1000
	EndIf
	
	If Lives#<=-1 Then
		Cls
		TileImage GFX_Menu_Back,0,BackY#
		DrawImage GFX_Game_GameOver,GraphicsWidth()/2,GraphicsHeight()/2
		Text 0,FontHeight()*0,"Health: "+Int(Lives#)
		Text 0,FontHeight()*1,"Level: "+Int(Wave#)
		Text 0,FontHeight()*2,"Score: "+Int(Score#)
		Flip
			lowest = hiscore$(9,2)							; Get lowest hiscore.
			If score# < lowest								; Score lower than that?
				show_hiscores()								; Show current hiscores.
			Else											; Score higher than lowest hiscore?
				FlushKeys()
				enter_hiscore()								; Let the player enter his/hers name.
			EndIf
		Alive=False
	EndIf
	
	Text 0,FontHeight()*0,"Health: "+Int((Lives#/3)*100)+"%"
	Text 0,FontHeight()*1,"Level: "+Int(Wave#)
	Text 0,FontHeight()*2,"Score: "+Int(Score#)
	
Flip
WaitTimer(timer)
Wend

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

ExitIt()

MainMenu(False,True,False,False,"BGM\MainMenu.mp3",True)

End Function

Function ExitIt()
	lowest = hiscore$(9,2)							; Get lowest hiscore.
	If score# < lowest								; Score lower than that?
		show_hiscores(0,1)								; Show current hiscores.
	Else											; Score higher than lowest hiscore?
		FlushKeys()
		enter_hiscore()								; Let the player enter his/hers name.
		If iAdditional Then remote_hiscores(1)
	EndIf
End Function

Function Load2dGFX()
; Menu Graphics
GFX_Menu_Back=LoadImage("GFX\Menu\Back.jpg")
	ResizeImage GFX_Menu_Back,GraphicsWidth(),GraphicsWidth()*1.5625
;GFX_Menu_Pointer=LoadImage("GFX\Menu\pointer.bmp")
;	MaskImage GFX_Menu_Pointer,255,0,255
;GFX_Menu_Exit_Game=LoadImage("GFX\Menu\Exit_Game.bmp")
;	ResizeImage GFX_Menu_Exit_Game,GraphicsWidth()/5,GraphicsHeight()/10
;	MaskImage GFX_Menu_Exit_Game,255,0,255
;	MidHandle GFX_Menu_Exit_Game
;GFX_Menu_Options=LoadImage("GFX\Menu\Options.bmp")
;	ResizeImage GFX_Menu_Options,GraphicsWidth()/5,GraphicsHeight()/10
;	MaskImage GFX_Menu_Options,255,0,255
;	MidHandle GFX_Menu_Options
;GFX_Menu_Network=LoadImage("GFX\Menu\Network.bmp")
;	ResizeImage GFX_Menu_Network,GraphicsWidth()/5,GraphicsHeight()/10
;	MaskImage GFX_Menu_Network,255,0,255
;	MidHandle GFX_Menu_Network
;GFX_Menu_Player=LoadImage("GFX\Menu\Player.bmp")
;	ResizeImage GFX_Menu_Player,GraphicsWidth()/5,GraphicsHeight()/10
;	MaskImage GFX_Menu_Player,255,0,255
;	MidHandle GFX_Menu_Player
GFX_Menu_Logo=LoadImage("GFX\Menu\Logo.bmp")
	MaskImage GFX_Menu_Logo,255,0,255
	ResizeImage GFX_Menu_Logo,GraphicsWidth(),GraphicsHeight()

; Game Graphics
GFX_Game_Player1=LoadAnimImage("GFX\Game\player1.bmp",50,50,0,3)
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
GFX_Game_GameOver=LoadImage("GFX\Game\GameOver.bmp")
	ResizeImage GFX_Game_GameOver,GraphicsWidth(),GraphicsHeight()
	MaskImage GFX_Game_GameOver,255,0,255
	MidHandle GFX_Game_GameOver
GFX_Game_Explosion=LoadAnimImage("GFX\Game\Explosion.bmp",32,32,0,9)
	MaskImage GFX_Game_Explosion,255,0,255
	ResizeImage GFX_Game_Explosion,GraphicsWidth()/20,GraphicsWidth()/20
	MidHandle GFX_Game_Explosion

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