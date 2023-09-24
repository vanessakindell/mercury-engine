Function MainMenu(Pointer,Scrolling,Options,PlayerSelectEnabled,OptBGM$,disableTile=False)
	
	MoveMouse(GraphicsWidth()/2,GraphicsHeight()/2)
	
	Local QuitIt=False
	Local BackY#
	
	If iAdditional=False Or disableTile=True
		menuimage = CreateImage(GraphicsWidth(),GraphicsHeight())
	Else
		menuimage = CreateImage(GraphicsWidth()/2,GraphicsHeight())
	EndIf

	CueMusicTrack(OptBGM$)	
	While KeyHit(1)=False And QuitIt=False
	SetBuffer(ImageBuffer(menuimage))
	
	Cls
	
		JoyXval# = JoyX()
		JoyYval# = JoyY()
		
		If Abs(JoyXval#)>.5 Then
			MoveMouse MouseX()+(JoyXval#*8),MouseY()
		EndIf
		
		If Abs(JoyYval#)>.5 Then
			MoveMouse MouseX(),MouseY()+(JoyYval#*8)
		EndIf		
		
		If KeyHit(88) Then
			SaveBuffer(FrontBuffer(),"Screenshot"+Str(Int(Sshot#))+".bmp")
			Sshot#=Sshot#+1
		EndIf
		
		HandleMusic(1)
		
		If Scrolling=True Then
			BackY#=BackY#+1
		EndIf
		
		; Draw menu graphics
		TileImage GFX_Menu_Back,0,BackY#
		DrawImage GFX_Menu_Logo,0,0
		If pointer=True Then
			DrawImage GFX_Menu_Exit_Game,ImageWidth(menuimage)/2,(ImageHeight(menuimage)/2)+(ImageHeight(GFX_Menu_Exit_Game)*4)
			DrawImage GFX_Menu_Options,ImageWidth(menuimage)/2,(ImageHeight(menuimage)/2)+(ImageHeight(GFX_Menu_Options)*2)
			DrawImage GFX_Menu_Player,ImageWidth(menuimage)/2,(ImageHeight(menuimage)/2);(ImageHeight(GFX_Menu_Player))	
			DrawImage GFX_Menu_Pointer,MouseX(),MouseY()
			
			; Menu selections
			If MouseHit(1) Or JoyHit(1) Then
				SetBuffer(BackBuffer())
				; Quit game menu select
				If MouseX()>(ImageWidth(menuimage)/2)-(ImageWidth(GFX_Menu_Exit_Game)/2) And MouseX()<(ImageWidth(menuimage)/2)+(ImageWidth(GFX_Menu_Exit_Game)/2) Then
					If MouseY()>(ImageHeight(menuimage)/2)+(ImageHeight(GFX_Menu_Exit_Game)*3.5) And MouseY()<(ImageHeight(menuimage)/2)+(ImageHeight(GFX_Menu_Exit_Game)*4.5) Then
						Click()
						save_hiscores()
						SaveSettings()
						End
					EndIf
				EndIf
				; Options menu menu select
				If MouseX()>(ImageWidth(menuimage)/2)-(ImageWidth(GFX_Menu_Options)/2) And MouseX()<(ImageWidth(menuimage)/2)+(ImageWidth(GFX_Menu_Exit_Game)/2) Then
					If MouseY()>(ImageHeight(menuimage)/2)+(ImageHeight(GFX_Menu_Options)*1.5) And MouseY()<(ImageHeight(menuimage)/2)+(ImageHeight(GFX_Menu_Options)*2.5) Then
						FreeImage menuimage
						Click()
						FlushKeys()
						FlushMouse()
						If Options Then
							Options(OptBGM$,False,disableTile)
						Else
							If disableTile And iAdditional Then remote_hiscores(1)
							show_hiscores(False,disableTile)
						EndIf
						mainmenu(Pointer,Scrolling,Options,PlayerSelectEnabled,OptBGM$,disableTile)
					EndIf
				EndIf
				; Play game menu select
				If MouseX()>(ImageWidth(menuimage)/2)-(ImageWidth(GFX_Menu_Player)/2) And MouseX()<(ImageWidth(menuimage)/2)+(ImageWidth(GFX_Menu_Player)/2) Then
					If MouseY()>(ImageHeight(menuimage)/2)-(ImageHeight(GFX_Menu_Player)*.5) And MouseY()<(ImageHeight(menuimage)/2)+(ImageHeight(GFX_Menu_Player)*.5) Then
						FreeImage menuimage
						Click()
						FlushKeys()
						FlushMouse()
						If PlayerSelectEnabled=True Then
							PlayerSelect()
						Else
							MainGame()
						EndIf
						mainmenu(Pointer,Scrolling,Options,PlayerSelectEnabled,OptBGM$,disableTile)
					EndIf
				EndIf
				; Click link
				If MouseY()<FontHeight() Then
					ExecFile("http://www.vanessagames.com/")
				EndIf
			EndIf
		Else
			draw_hiscores(True)
			If KeyHit(57) Or KeyHit(28) Or MouseHit(1) Then
				FreeImage menuimage
				FlushKeys()
				FlushMouse()
				If PlayerSelectEnabled=True Then
					PlayerSelect()
				Else
					MainGame()
				EndIf
				mainmenu(Pointer,Scrolling,Options,PlayerSelectEnabled,OptBGM$,disableTile)
			EndIf
		EndIf
		
	Text 0,0,"Created with the Mercury Engine v"+EngineVersion#+" http://www.vanessagames.com/"
	
	SetBuffer(BackBuffer())
	Cls
	TileImage menuimage
	
	Flip
	Wend
	SaveSettings()
	End
End Function