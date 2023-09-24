AppTitle "Mercury Engine List Server v0.01","Shut down?"

SetBuffer(BackBuffer())

Graphics 800,600,16,2

Type Server
	Field name$
	Field ip$
End Type

Type Info
	Field txt$
End Type

svrGame=CreateTCPServer(1339)

If svrGame<>0 Then 
	Info("Server started successfully.")
Else 
	RuntimeError "Server failed to start." 
End If 

While Not KeyHit(1) 
	strStream$=AcceptTCPStream(svrGame) 
	If strStream$<>"" Then 
		s.server=New server
		s\name$=strStream$
		s\ip$=TCPStreamIP(strStream)
		info("Server "+s\name$+"@"+s\ip$+" added to list.")
	End If 
	y=FontHeight()*7
	r=255
	For inf.Info=Each Info
		If r>0
			Text 2,y,inf\txt$
			y=y-FontHeight()
			r=r-12
		EndIf
	Next
	Flip
Wend

CloseTCPServer svrGame
End

Function info( t$ )
	inf.Info=New Info
	inf\txt$=t$
	Insert inf Before First Info
End Function