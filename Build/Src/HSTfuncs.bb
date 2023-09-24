;TCPTimeouts 0,1000

; Arrays used by hiscore code.
Dim hiscore$(10,2)											;Holds the hiscore table (1 = names, 2=scores).
Dim hiname$(12)												;Needed for entering a name into the table.

; This functions saves the hiscore
; list to disk.
Function save_hiscores(filename$="hiscore.dat")
	For q = 0 To 9												; Add spaces to fill up empty spots to all 10 entries.
		sl = 12 - Len(hiscore$(q,1))							; Get length of name.
		If sl > 0												; Needs padding?
			For p = 1 To sl										
				z$ = z$ + " "									; Add a space to the name.
			Next
			hiscore$(q,1) = hiscore$(q,1) + z$					; Put the space padded name back in the array.
		EndIf
		z$ = ""													; Clear the temp string.
		sl = 10 - Len(hiscore$(q,2))							; Get length of score.
		If sl > 0												; Needs padding?
			For p = 1 To sl										
				z$ = z$ + " "									; Add a space to the score.
			Next
			hiscore$(q,2) = z$ + hiscore$(q,2)					; Put padded score back in the array.
		EndIf
		z$ = ""													; Clear the temp string.
	Next

	hi = WriteFile(filename$)								; Open the file for writing.
	For q = 0 To 9												; Go through all 10 entries.
		For p = 1 To Len(hiscore$(q,1))							; Write each letter.
			a$ = Mid$(hiscore$(q,1),p,1)						; Get letter.
			as = Asc(a$)										; Make it ascii format.
			WriteByte hi,as - 20								; Decrease by 20 to make different character.
		Next
		For p = 1 To Len(hiscore$(q,2))							; Write each string number.
			a$ = Mid$(hiscore$(q,2),p,1)						; Get number from string.
			as = Asc(a$)										; Make it ascii format.
			WriteByte hi,as - 20								; Decrease by 20 to make different character.
		Next
	Next
	CloseFile hi												; Close the file.
End Function

; This function loads the hiscores from the
; hiscores file. If the file doesn't exist
; then it will create a new one.
Function load_hiscores(filename$="hiscore.dat")
	If FileType(filename$)<>1 Then								; Does the file exist?
		hiscore$(0,1) = "NONE"									; If not then we fill the hiscore table with these.
		hiscore$(0,2) = "0"
		hiscore$(1,1) = "NONE"
		hiscore$(1,2) = "0"
		hiscore$(2,1) = "NONE"
		hiscore$(2,2) = "0"
		hiscore$(3,1) = "NONE"
		hiscore$(3,2) = "0"
		hiscore$(4,1) = "NONE"
		hiscore$(4,2) = "0"
		hiscore$(5,1) = "NONE"
		hiscore$(5,2) = "0"
		hiscore$(6,1) = "NONE"
		hiscore$(6,2) = "0"
		hiscore$(7,1) = "NONE"
		hiscore$(7,2) = "0"
		hiscore$(8,1) = "NONE"
		hiscore$(8,2) = "0"
		hiscore$(9,1) = "NONE"  
		hiscore$(9,2) = "0"
		save_hiscores(filename$)
	Else														; File does exist.
		hi = OpenFile(filename$)								; Open the file.
		For q = 0 To 9											; Read all 10 hiscore entries.
			For p = 1 To 12										; Read all bytes of the name.
				a = ReadByte(hi)								; Read a single byte.
				a = a + 20										; Increase by 20 to get good character.
				ac$ = ac$ + Chr$(a)								; Put character in temp string.
			Next
			hiscore$(q,1) = ac$									; Put loaded name in hiscore array.
			ac$ = ""											; Clear temp string.
			For p = 1 To 10										; Read all bytes of the score.
				a = ReadByte(hi)								; Read a single byte.
				a = a + 20										; Increase by 20 to get good character.
				ac$ = ac$ + Chr$(a)								; Put character in temp string.
			Next
			hiscore$(q,2) = ac$									; Put loaded score in hiscore array.
			ac$ = ""											; Clear the temp string.
		Next
		CloseFile hi											; Close the file
	EndIf
End Function

; This function will physically sort the
; hiscore table to put highest on top and
; lowest at the bottom. It uses a simple
; Bubble sort.
Function sort_hiscores()
	lowest = hiscore$(9,2)										; Get lowest score from table.
	If score > lowest											; check the score of the player against it.
		hiscore$(10,1) = name$									; Not saved part of hiscore array gets the entered name.
		hiscore$(10,2) = score									; Not saved part of hiscore array gets the gotten score.
		; A bubble sort!
		; It checks to see if the score below is higher than the one above.
		; If so it will swap them.. after going through the entire
		; array it will have sorted it.. slow but effective.
	EndIf
		For bub1 = 0 To 10
			counter = 0
			For bub2 = 0 To 9
				a = hiscore$(counter,2)
				b = hiscore$(counter + 1, 2)
				If b > a
					n1$ = hiscore$(counter,1)
					n2$ = hiscore$(counter + 1,1)
					hiscore$(counter,2) = b
					hiscore$(counter + 1,2) = a
					hiscore$(counter,1) = n2$
					hiscore$(counter + 1,1) = n1$
				EndIf
				counter = counter + 1
			Next
		Next
End Function

Function remove_hiscore(TargetScore)
		DebugLog "Attempting to remove "+targetscore
		; A bubble sort!
		; It checks to see if the score below is higher than the one above.
		; If so it will swap them.. after going through the entire
		; array it will have sorted it.. slow but effective.
		For counter = 0 To 10
			DebugLog "Is "+Int(hiscore(counter,2))+"="+Int(TargetScore)+"?"
			If Int(hiscore(counter,2)) = Int(TargetScore)
				hiscore$(counter,1) = "NONE"
				hiscore$(counter,2) = 0
				sort_hiscores()
				Return True
			EndIf
		Next
		Return False
End Function

; This function will nicely show the highscores.
; The waving background poses problems on cheaper
; and older videocards as it fills up memory REAL
; quick on those and will eventually start swapping
; to disk. It will take a LONG time to return to
; the main menu if you watch too long and have
; one of those cards. This seems to be a DirectX
; 'feature'.
Function show_hiscores(savehighscores=False,disableTile=False)
	;remote_hiscores()
	cool = CreateImage(800,10)									; Make an image for the wavey background.
	hfnt = LoadAnimImage("gfx\high\hifont.bmp",32,27,0,60)				; Load the font and cut it.
	hlgo = LoadImage("gfx\high\hilogo.bmp")								; Load big hiscore logo.

	If savehighscores Then
		Cls
		For hx = 0 To 9											; Draw all 10 scores and names.
			For let = 1 To 12									; Draw all 12 characters off name.
				a$ = Mid$(hiscore$(hx,1),let,1)					; Get character.
				as = Asc(a$)									; Get ascii code.
				If a$ <> " "									; Not space?
					; Draw the character on screen..
					; The as - 32 will make sure the
					; correct character is taken from
					; the font.
					If as>91 Then as=91
					DrawImage hfnt,((let * 32) - 12)*(GraphicsWidth()/600),(140 + (32 * hx))*(GraphicsHeight()/480),as - 32
				EndIf
			Next
			For let = 1 To 10									; Draw all 10 score characters.
				a$ = Mid$(hiscore$(hx,2),let,1)					; Get character.
				as = Asc(a$)									; Get ascii code.
				If a$ <> " "
					; Draw the character on screen..
					; The as - 32 will make sure the
					; correct character is taken from
					; the font.
					If as>91 Then as=91
					DrawImage hfnt,(300 + (let * 32))*(GraphicsWidth()/600),(140 + (32 * hx))*(GraphicsHeight()/480),as - 32
				EndIf
			Next
		Next

		; This draws the bouncing logo and moves sideways.
		DrawImage hlgo,(68 - (Sin(cnt / 2) * 60))*(GraphicsWidth()/600),(70 - Abs(Sin(cnt) * 60))*(GraphicsHeight()/480)
		Flip													; Flip screen.
	SaveBuffer(FrontBuffer(),"HighScores.bmp")
	EndIf
	
	If iAdditional=True And disableTile=False Then
		highscreen=CreateImage(GraphicsWidth()/2,GraphicsHeight())
	Else
		highscreen=CreateImage(GraphicsWidth(),GraphicsHeight())
	EndIf
	
	FlushKeys()
	While KeyHit(1)=False And JoyHit(3)=False And JoyHit(3,1)=False And JoyHit(10)=False And JoyHit(10,1)=False And MouseHit(1)=False And KeyHit(57)=False And JoyHit(1)=False And JoyHit(1,1)=False And JoyHit(6)=False And JoyHit(6,1)=False And JoyHit(8)=False And JoyHit(8,1)=False And MouseHit(2)=False And MouseHit(3)=False And KeyHit(28)=False
		SetBuffer(ImageBuffer(highscreen))
		Cls
		For hx = 0 To 9											; Draw all 10 scores and names.
			For let = 1 To 12									; Draw all 12 characters off name.
				a$ = Mid$(hiscore$(hx,1),let,1)					; Get character.
				as = Asc(a$)									; Get ascii code.
				If a$ <> " "									; Not space?
					; Draw the character on screen..
					; The as - 32 will make sure the
					; correct character is taken from
					; the font.
					If as>91 Then as=91
					DrawImage hfnt,((let * 32) - 12)*(ImageWidth(highscreen)/600),(140 + (32 * hx))*(ImageHeight(highscreen)/480),as - 32
				EndIf
			Next
			For let = 1 To 10									; Draw all 10 score characters.
				a$ = Mid$(hiscore$(hx,2),let,1)					; Get character.
				as = Asc(a$)									; Get ascii code.
				If a$ <> " "
					; Draw the character on screen..
					; The as - 32 will make sure the
					; correct character is taken from
					; the font.
					If as>91 Then as=91
					DrawImage hfnt,(300 + (let * 32))*(ImageWidth(highscreen)/600),(140 + (32 * hx))*(ImageHeight(highscreen)/480),as - 32
				EndIf
			Next
		Next
		
		If ChannelPlaying(CHN_BGM)=False And Music=True
			CHN_BGM=PlayMusic("BGM\MainMenu.mp3")
		EndIf

		If KeyHit(61) Then
			If Music=True Then
				Music=False
				StopChannel CHN_BGM
			Else
				Music=True
			EndIf
		EndIf
	
		; This draws the bouncing logo and moves sideways.
		DrawImage hlgo,(68 - (Sin(cnt / 2) * 60))*(GraphicsWidth()/600),(70 - Abs(Sin(cnt) * 60))*(GraphicsHeight()/460)
		SetBuffer(BackBuffer())
		Cls
		TileImage highscreen
		Flip													; Flip screen.
	Wend
	FlushKeys()
	FlushMouse()
	FreeImage highscreen
End Function

Function draw_hiscores(FromMenu=False)
	If FromMenu Then
		For hx = 0 To 9											; Draw all 10 scores and names.
			For let = 1 To 12									; Draw all 12 characters off name.
				a$ = Mid$(hiscore$(hx,1),let,1)					; Get character.
				Text (let * 32) + 300,350 + (32 * hx),a$
			Next
			For let = 1 To 10									; Draw all 10 score characters.
				a$ = Mid$(hiscore$(hx,2),let,1)					; Get character.
				Text (let * 32) + 400,350 + (32 * hx),a$
			Next
		Next
	Else
		For hx = 0 To 9											; Draw all 10 scores and names.
			For let = 1 To 12									; Draw all 12 characters off name.
				a$ = Mid$(hiscore$(hx,1),let,1)					; Get character.
				Text (let * 32) + 300,140 + (32 * hx),a$
			Next
			For let = 1 To 10									; Draw all 10 score characters.
				a$ = Mid$(hiscore$(hx,2),let,1)					; Get character.
				Text (let * 32) + 400,140 + (32 * hx),a$
			Next
		Next
	EndIf
End Function

; This function lets the player
; enter his/hers name into the
; hiscore table.
Function enter_hiscore()
	enter = LoadImage("GFX\High\highscore.bmp")							; Load the hiscore logo.
	hfnt  = LoadAnimImage("GFX\High\hifont.bmp",32,27,0,60)				; Load the font, cut it.

	fntcnt = 33														; Start of font parts needed. (frame in image)
	lets = 1														; Number of entered characters.
	done = False													; Set check for player done to false.
	name$= ""													; Empty the name.
	FlushKeys()
	While done = False
		Cls
		DrawImage enter,106,64									; Place the hiscore logo.

		For fy = 1 To 2											; Draw all the font parts needed.
			For fx = 1 To 13
				DrawImage hfnt,(fx * 48) - 30,128 + (fy * 64), fntcnt
				fntcnt = fntcnt + 1
			Next
		Next
		fntcnt = 33
		
		For lt = 1 To 12										; Draw entered letters or dots if not entered.
			If hiname$(lt) <> ""
				If lt = lets
					DrawImage hfnt,lt * 48,400 - Abs(Sin(cnt * 2) * 20),Asc(hiname$(lt)) - 32
				Else
					DrawImage hfnt,lt * 48,400,Asc(hiname$(lt)) - 32
				EndIf
			Else
				If lt = lets
					DrawImage hfnt,lt * 48,400 - Abs(Sin(cnt * 2) * 20),14
				Else
					DrawImage hfnt,lt * 48,400,14
				EndIf
			EndIf
		Next 

		If KeyHit(205)											; Right key
			lets = lets + 1										; Move one letter to right.
			If lets > 12 Then lets = 1							; Wrap to first letter if past letter 12.
		EndIf
		If KeyHit(203)											; Left key.
			lets = lets - 1										; Move one letter to left.
			If lets < 1 Then lets = 12							; Wrap to last letter if past letter 1.
		EndIf
		If KeyHit(14)											; Backspace key.
			lets = lets - 1										; Delete letter.
			If lets < 1 Then lets = 1							; Keep on first if needed.
			hiname$(lets) = ""									; Clear letter from array.
		EndIf

		; 97 - 122
		ltr = GetKey()											; Get all the keys that can be entered.
		If ltr > 96 And ltr < 123
			If lets < 13
				hiname$(lets) = Upper$(Chr$(ltr))				; Put letter in array.
				lets = lets + 1									; Move to next letter position.
			EndIf
		EndIf
				
		cnt = cnt + 1

		If KeyHit(28) Or JoyHit(1)											; Return key.
			For pl = 1 To 12									; Put name in hiscore list.
				If hiname$(pl) = ""
					name$ = name$ + " "
				Else
					name$ = name$ + hiname$(pl)
				EndIf
			Next
			If Online Then Remote_hiscores()
			done = True											; Player is ready.
			sort_hiscores()										; Sort hiscore table.
			save_hiscores()										; Save hiscore table.
			load_hiscores()										; Load again just in case.
			FlushKeys()
			FlushMouse()
		EndIf

		Flip
		Cls
	Wend
End Function

Function HighScoreAdd(name$,score,filename$="hiscore.dat")
	If name$="" Then name$="Nobody"
	lowest = hiscore$(9,2)							; Get lowest hiscore.
	If score > lowest								; Score lower than that?
			For pl = 1 To 12									; Put name in hiscore list.
				If hiname$(pl) = ""
					name$ = name$ + " "
				Else
					name$ = name$ + hiname$(pl)
				EndIf
			Next
		showname$=Replace$(name$," ","")
		sort_hiscores()
		save_hiscores(filename$)
		load_hiscores(filename$)
		Return True
	Else
		showname$=Replace$(name$," ","")
		Return False
	EndIf
End Function

Function remote_hiscores(disableTile=False)
			If online
				If ServerName$="" Then SyncServer()
				If iAdditional=True And disableTile=False Then
					remoteimage=CreateImage(GraphicsWidth()/2,GraphicsHeight())
				Else
					remoteimage=CreateImage(GraphicsWidth(),GraphicsHeight())
				EndIf
				SetBuffer(ImageBuffer(remoteimage))
				Cls
				Text 0,0,"Attempting to connect via port "+Str(Int(1364))
				Text 0,FontHeight(),"Please wait..."
				SetBuffer(BackBuffer())
				Cls
				TileImage remoteimage
				Flip
				strmGame=OpenTCPStream(ServerName$,1364)
						If Not strmGame Then
							SetBuffer(ImageBuffer(remoteimage))								
							Text 0,FontHeight()*2,"Failed to connect."
							SetBuffer(BackBuffer())
							Cls
							TileImage remoteimage
							Flip
							Delay 1000
							DebugLog("Server failed to connect")
						Else
							Cls
							Text 0,0,"Connected."
							Flip
							
							WriteLine strmGame,name$+"="+Int(score)+"|"+Gamecode#
							
							frametimer = CreateTimer(30)
							bank=CreateBank(220)
							
							Cls
							Text 0,0,"Getting updated highscores from server..."
							Flip
							
							Delay 1000
							
							success=False
							While Not Eof(strmGame)
								gotem=ReadBytes(bank,strmGame,0,220)
								If gotem>=220 Then
									WriteLine strmGame,"Success"
									success=True
								EndIf
							Wend
							
							If Not success Then
								WriteLine strmGame,"Not success"
								FreeBank bank
								CloseTCPStream(strmGame)
								Return False
							EndIf
														
							Cls
							Text 0,0,"Recieved! Applying updated hiscores..."
							Flip
									
							file=WriteFile("hiscore.dat")
							WriteBytes(bank,file,0,220)
							CloseFile file
							load_hiscores()											
							
							name$=""
							score=0
							
							FreeBank bank
							CloseTCPStream(strmGame)
						EndIf
			FreeImage remoteimage
		Else
			RuntimeError "ONLINE FALSE"
		EndIf	
			Return True
End Function

Function SyncServer()
	BlitzGet("http://www.VanessaGames.com/ServerLocation.ini", CurrentDir(), "ServerLocation.ini")
	
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
		RuntimeError "ONLINE FAILED"
	EndIf
End Function