Function playerselect(disableTile=False)
	players=0
	ControlMode#=5
	ControlMode2#=0
	
	If iAdditional=True And disableTile=False Then
		ctImage=CreateImage(GraphicsWidth()/2,GraphicsHeight())
	Else
		ctImage=CreateImage(GraphicsWidth(),GraphicsHeight())
	EndIf

	While Not KeyHit(1) Or JoyHit(7)
		SetBuffer(ImageBuffer(ctImage))
		Cls
		
		Text ImageWidth(ctImage)/2,FontHeight(),"Press Fire to Select Control Mode",1,1
		If players<>2 Then Text ImageWidth(ctImage)/2,FontHeight()*2,"Select player "+Str(Int(players+1))+" control mode.",1,1
		If players<>0 Then Text ImageWidth(ctImage)/2,FontHeight()*3,"Press start to begin.",1,1		
		
		Select ControlMode#:
			Case 1:
				DrawImage GFX_Menu_Mouse,(ImageWidth(ctImage)/2),ImageHeight(ctImage)/4
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*0),"Left/Right:       Move Left/Right"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*1),"Fire Lazer:       Left Click"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*2),"Detonate Lazers:  Right Click"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*3),"Start/Pause:      Keyboard ESC"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*4),"Nuke:             Center Click"
			Case 2:
				DrawImage GFX_Menu_Key,(ImageWidth(ctImage)/2),ImageHeight(ctImage)/4
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*0),"Left/Right:       Arrow Left/Right"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*1),"Fire Lazer:       Space"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*2),"Detonate Lazers:  Enter"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*3),"Nuke:             N"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*4),"Start/Pause:      Esc"
			Case 3:
				DrawImage GFX_Menu_Joy1,(ImageWidth(ctImage)/2),ImageHeight(ctImage)/4
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*0),"Left/Right:       Left Stick"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*1),"Fire Lazer:       A, Right Bumper or Right Trigger"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*2),"Detonate Lazers:  B, Left Bumper or Left Trigger"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*3),"Nuke:             X"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*4),"Start/Pause:      Start"
			Case 4:
				DrawImage GFX_Menu_Joy2,(ImageWidth(ctImage)/2),ImageHeight(ctImage)/4
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*0),"Left/Right:       Left Stick"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*1),"Fire Lazer:       A, Right Bumper or Right Trigger"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*2),"Detonate Lazers:  B, Left Bumper or Left Trigger"
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*3),"Nuke:             X"			
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*4),"Start/Pause:      Start"
			Default:
				Text ImageWidth(ctImage)/2,ImageHeight(ctImage)/4,"Select Control Mode",1,1
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*0),"Left/Right:       "
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*1),"Fire Lazer:       "
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*2),"Detonate Lazers:  "
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*3),"Nuke:             "
				Text FontWidth(),(ImageHeight(ctImage)/4)+(FontHeight()*4),"Start/Pause:      "
		End Select
		Select ControlMode2#:
			Case 1:
				DrawImage GFX_Menu_Mouse,(ImageWidth(ctImage)/2),ImageHeight(ctImage)*3/4
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*0),"Left/Right:       Move Left/Right"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*1),"Fire Lazer:       Left Click"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*2),"Detonate Lazers:  Right Click"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*3),"Start/Pause:      None (use keyboard ESC)"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*4),"Nuke:             Center Click"
			Case 2:
				DrawImage GFX_Menu_Key,(ImageWidth(ctImage)/2),ImageHeight(ctImage)*3/4
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*0),"Left/Right:       Arrow Left/Right"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*1),"Fire Lazer:       Space"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*2),"Detonate Lazers:  Enter"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*3),"Nuke:             N"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*4),"Start/Pause:      Esc"
			Case 3:
				DrawImage GFX_Menu_Joy1,(ImageWidth(ctImage)/2),ImageHeight(ctImage)*3/4
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*0),"Left/Right:       Left Stick"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*1),"Fire Lazer:       A, Right Bumper or Right Trigger"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*2),"Detonate Lazers:  B, Left Bumper or Left Trigger"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*3),"Nuke:             X"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*4),"Start/Pause:      Start"
			Case 4:
				DrawImage GFX_Menu_Joy2,(ImageWidth(ctImage)/2),ImageHeight(ctImage)*3/4
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*0),"Left/Right:       Left Stick"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*1),"Fire Lazer:       A, Right Bumper or Right Trigger"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*2),"Detonate Lazers:  B, Left Bumper or Left Trigger"
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*3),"Nuke:             X"			
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*4),"Start/Pause:      Start"
			Default:
				Text ImageWidth(ctImage)/2,ImageHeight(ctImage)*3/4,"No Player 2",1,1
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*0),"Left/Right:       "
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*1),"Fire Lazer:       "
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*2),"Detonate Lazers:  "
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*3),"Nuke:             "
				Text FontWidth(),(ImageHeight(ctImage)*3/4)+(FontHeight()*4),"Start/Pause:      "
		End Select
		If players=0 Then 
			If MouseHit(1) Then
				players=players+1
				ControlMode#=1
			EndIf	
			
			If KeyHit(57) Then
				players=players+1
				ControlMode#=2
			EndIf
	
			If JoyHit(1) Or JoyHit(6) Then
				players=players+1
				ControlMode#=3
			EndIf
			
			If JoyHit(1,1) Or JoyHit(6,1) Then
				players=players+1
				ControlMode#=4
			EndIf
			
			If KeyHit(6) Then
				players=players+1
				ControlMode#=2
			EndIf
		EndIf
		If players=1 Then 
			If MouseHit(1) Then
				players=players+1
				ControlMode2#=1
			EndIf	
			
			If KeyHit(57) Then
				players=players+1
				ControlMode2#=2
			EndIf
	
			If JoyHit(1) Or JoyHit(6) Then
				players=players+1
				ControlMode2#=3
			EndIf
			
			If JoyHit(1,1) Or JoyHit(6,1) Then
				players=players+1
				ControlMode2#=4
			EndIf
			
			If KeyHit(6) Then
				players=players+1
				ControlMode2#=2
			EndIf
		EndIf
		
		If ControlMode#=ControlMode2# Then
			players=0
			ControlMode#=5
			ControlMode2#=0
		EndIf
		
		If KeyHit(28) Or JoyHit(8) Or JoyHit(2) Or JoyHit(8,1) Or JoyHit(2,1) Then
			If players>0 Then
				SetBuffer(BackBuffer())
				FreeImage ctImage
				MainGame()
			EndIf
		EndIf
		
		SetBuffer(BackBuffer())
		Cls
		TileImage ctImage
		Flip
	Wend
	FreeImage(ctImage)
End Function