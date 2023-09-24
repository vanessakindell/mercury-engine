Function Options(OptBGM$,FromGame=False,disableTile=False)
	
	CueMusicTrack(OptBGM$)
	
	If iAdditional=False Or disableTile=True Then
		OptionImage=CreateImage(GraphicsWidth(),GraphicsHeight())
	Else
		OptionImage=CreateImage(GraphicsWidth()/2,GraphicsHeight())
	EndIf
	
	Local Selector=1
	Local Leavehere=False
	Local JoyYD=JoyYDir()
	Local Movement=0
	
	While KeyHit(1)=False And JoyHit(8)=False And leavehere=False
		
		If FromGame = False Then
			HandleMusic(1)
		Else
			StopMusic()
		EndIf
	
		SetBuffer(ImageBuffer(OptionImage))
		Cls
		Text (ImageWidth(OptionImage)/2),(ImageHeight(OptionImage)/2)-(FontHeight()*5),"--Options--",0,1
		Text (ImageWidth(OptionImage)/2)+StringWidth("--> "),(ImageHeight(OptionImage)/2),"Sound Volume: "+Int(SFXVolume#*100)+"%",0,1
		Text (ImageWidth(OptionImage)/2)+StringWidth("--> "),(ImageHeight(OptionImage)/2)+FontHeight(),"Music Volume: "+Int(BGMVolume#*100)+"%",0,1
		If Not FromGame Then
			If iAdditional Then
				Text (ImageWidth(OptionImage)/2)+StringWidth("--> "),(ImageHeight(OptionImage)/2)+(FontHeight()*2),"3D: Enabled",0,1
			Else
				Text (ImageWidth(OptionImage)/2)+StringWidth("--> "),(ImageHeight(OptionImage)/2)+(FontHeight()*2),"3D: Disabled",0,1
			EndIf
			If Online Then
				Text (ImageWidth(OptionImage)/2)+StringWidth("--> "),(ImageHeight(OptionImage)/2)+(FontHeight()*4),"Online Highscores: Enabled",0,1
			Else
				Text (ImageWidth(OptionImage)/2)+StringWidth("--> "),(ImageHeight(OptionImage)/2)+(FontHeight()*4),"Online Highscores: Disabled",0,1
			EndIf
		EndIf
		Text (ImageWidth(OptionImage)/2)+StringWidth("--> "),(ImageHeight(OptionImage)/2)+(FontHeight()*5),"Return",0,1
		If fromgame Text (ImageWidth(OptionImage)/2)+StringWidth("--> "),(ImageHeight(OptionImage)/2)+(FontHeight()*6),"Quit Game",0,1
		Text (ImageWidth(OptionImage)/2)+StringWidth("--> "),(ImageHeight(OptionImage)/2)+(FontHeight()*3),"Show Highscores",0,1

		Text ImageWidth(OptionImage)/2,(ImageHeight(OptionImage)/2)+(FontHeight()*Selector),"-->",0,1
		
		Movement = 0
		
		Local OldJoyYD = JoyYD
		JoyYD = JoyYDir()
		Local MouseYS = MouseYSpeed()
		
		If OldJoyYD <> JoyYD
			If JoyYD = -1 Then Movement = -1
			If JoyYD = 1 Then Movement = 1
		EndIf
		
		If MouseYS < -5 Then
			Movement = -1
		Else If MouseYS > 5 Then
			Movement = 1
		EndIf
				
		If KeyHit(200) Or Movement = -1 Then ; Up
			Selector=Selector-1
			If Selector < 0 Then Selector = 0
		EndIf		
		If KeyHit(208) Or Movement = 1 Then ; Down
			Selector=Selector+1
			If Selector > 6 And fromgame Then Selector = 6
			If Selector > 5 And fromgame=False Then Selector = 5
		EndIf
		
		If KeyHit(205) Or JoyHit(1) Or MouseHit(1) Or KeyHit(28) Then
			Select Selector
				Case 0:
					SFXVolume#=SFXVolume#+.1
					If SFXVolume#>1 Then SFXVolume#=1
				Case 1:
					BGMVolume#=BGMVolume#+.1
					If BGMVolume#>1 Then BGMVolume#=1
				Case 2:
					If Not fromgame
						If iAdditional Then
							iAdditional=False
						Else
							iAdditional=True
						EndIf
						FreeImage OptionImage
						If Not iAdditional Then
							OptionImage=CreateImage(GraphicsWidth(),GraphicsHeight())
						Else
							OptionImage=CreateImage(GraphicsWidth()/2,GraphicsHeight())
						EndIf
					EndIf
				Case 3:
					SetBuffer(BackBuffer())
					Cls
					Text 0,0,"Saving image, please wait..."
					Flip
					show_hiscores(True)
				Case 4:
					If Not fromgame
						If Online=True Then
							Online=False
						Else
							Online=True
							SetBuffer(BackBuffer())
							remote_hiscores()
						EndIf
					EndIf
				Case 5:
					LeaveHere=True
				Case 6:
					If fromgame Exitit()
				Default:
					DebugLog("Option undefined")
			End Select
		EndIf
		
		If JoyHit(2) Or MouseHit(2) Or KeyHit(203) Then
			Select Selector
				Case 0:
					SFXVolume#=SFXVolume#-.1
					If SFXVolume#<0 Then SFXVolume#=0
				Case 1:
					BGMVolume#=BGMVolume#-.1
					If BGMVolume#<0 Then BGMVolume#=0
				Default:
					DebugLog "Option undefined"
			End Select
		EndIf					
		
		SetBuffer(BackBuffer())
		Cls
		TileImage OptionImage
		Flip
		Wend
		SaveSettings()
		FreeImage OptionImage
End Function