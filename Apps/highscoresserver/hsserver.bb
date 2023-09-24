Global Version#=3

AppTitle "Mercury Engine High Scores Server v"+Version#

File=OpenFile("gamecodes.ini")
While Not Eof(File)
	Theline$=ReadLine(File)
	If Left(Theline,1)=";" Or theline="" Or Left(Theline,1)=" " Or Left(TheLine,1)="[" Then
		;Do nothing, comment
	Else If Instr(theline,"=")
		theline = Replace$(theline," = ","=")
		G.GameCode = New GameCode
		G\Name$=Mid(Theline,Instr(theline,"=")+1)
		G\Code#=Left(TheLine,Instr(theline,"=")-1)
		info("Gamecode "+Int(G\Code#)+" is now identified as "+G\Name$)
	EndIf
Wend		
CloseFile File

info("Port: "+Int(1364))

SetBuffer(BackBuffer())

Graphics 800,600,16,2

Include "..\..\build\Main.BB"


Global fntArial=LoadFont("Arial",24)
SetFont fntArial

Global svrGame=CreateTCPServer(1364)
If svrGame<>0 Then 
	Info("Server started successfully on port: "+Int(1364))
Else 
	RuntimeError "Server failed to start." 
End If

Global lst=False
Global strStream
Global Hiding
Global bank

TCPTimeouts 0,1000

Timer = CreateTimer(30)

ggTrayCreate(SystemProperty$("AppHWND"))
ggTraySetIconFromFile(CurrentDir()+"\HS.ico")
ggTraySetToolTip("Highscore Server")
ggTrayShowIcon()

.giveup
While Not KeyHit(1)
	Cls
	strStream=AcceptTCPStream(svrGame)
	
	If ggTrayPeekLeftClick()>0
		If hiding Then
			ShowWindow()
			hiding=False
		Else
			HideWindow()
			hiding=True
		EndIf
		ggTrayClearEvents()
	EndIf
	
	If strStream Then
		info("Recieved a message from "+DottedIP(TCPStreamIP(strStream)))
		
		Delay 300 ; Wait for message to process
		
		Strin$=ReadLine$(strStream)
		If Instr(Strin$,"=")=0 Or Instr(Strin$,"|")=0 Then
			CloseTCPStream strStream
			info("Bad message:"+Strin$)
			Goto giveup
		EndIf
		
		name$=Left(Strin$,Instr(Strin$,"=")-1)
		score=Mid(Strin$,Instr(Strin$,"=")+1,Instr(Strin$,"|")-1)
		chkversion#=Mid(Strin$,Instr(Strin$,"|")+1)
				
		For G.GameCode = Each GameCode		
			If G\Code# = chkversion# Then
				gamename$ = G\Name$
			EndIf		
		Next
				
		info("Message recieved for "+gamename$+" gamecode: "+Int(chkversion#))
		info("Loading appropriate hiscore table...")
		
		Cls
		drawinfo()
		Flip
		
		load_hiscores("hiscore"+Str(Int(chkversion))+".dat")
		
		If name$="" Then name$="Nobody"
		
		Select HighScoreAdd(name$,Score)
			Case True:
				info(name$+"'s score of "+Int(Score)+" added to list "+Int(chkversion))
			Default:
				info(name$+"'s score of "+Int(Score)+" was too low for list "+Int(chkversion))
		End Select

		save_hiscores("hiscore"+Str(Int(chkversion))+".dat")
		
		lst=False
		
			bank=CreateBank(220)
			file=ReadFile("hiscore"+Str(Int(chkversion))+".dat")
				test=ReadBytes(bank,file,0,220)
			CloseFile file

			If test>220 Then RuntimeError "Bank overflow"
			
			If test<220 Then
				info("Table preperations failed.")		
				FreeBank bank
				CloseTCPStream(strStream)
				Goto giveup
			EndIf				
					
			info("Successfully prepared table for transmission.")

			Cls
			drawinfo()
			Flip

			info("Sending data back")
			WriteBytes(bank,strStream,0,220)
			info("Sent table to "+DottedIP(TCPStreamIP(strStream)))
			info("Waiting for client response")
			
			Cls
			drawinfo()
			Flip
			
			Delay 1000
			
			success=False
			count=0
			While Not success;Eof(strStream)
				count=count+1
				WriteBytes(bank,strStream,0,220)
				Response$ = ReadLine$(strStream)
				If Response$<>"" Then
					info("Client replied: "+Response$)
					success=True
				EndIf
				If count>10 Then
					info("Client did not respond")
					If strStream CloseTCPStream strStream
					FreeBank bank
					Goto giveup
				EndIf					
				WaitTimer(timer)
			Wend
			
			FreeBank bank
			If strStream CloseTCPStream strStream
	Else 
		If lst=False Then
			info("Listening...")
			lst=True
		EndIf
	End If
	drawinfo()
	Flip
	WaitTimer(Timer)
Wend

If bank FreeBank bank
If strStream CloseTCPStream strStream
If svrGame CloseTCPServer svrGame

ggTrayDestroy()

For G.Gamecode = Each Gamecode
	Delete G
Next

End

; App-specific functions

Function MainGame()
	; Engine compliance
End Function

Function ExitIt()
	; Engine compliance
End Function

Function HideWindow()
	hWnd = SystemProperty$("AppHWND")
	ShowWindowA(hWnd,0)
End Function

Function ShowWindow()
	hWnd = SystemProperty$("AppHWND")
	ShowWindowA(hWnd,5)
End Function

Function drawinfo()
	ttl#=0
	For inf.info = Each info
		ttl#=ttl#+1
	Next
	cnt#=-1
	For inf.info = Each info
		cnt#=cnt#+1
		Text 0,(ttl*FontHeight())-(FontHeight()*cnt),inf\txt$
	Next
End Function

Function info( t$ )
	DebugLog t$
	inf.Info=New Info
	inf\txt$=t$
	Insert inf Before First Info
End Function

; Mini app-specific type bank

Type GameCode
	Field Code#, Name$
End Type

Type Info
	Field txt$
End Type