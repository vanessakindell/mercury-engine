; Mercury Engine High Score Client Test

Global Version#=2.74
Global GameCode#
Global Online=True

; Some Includes
Include "..\..\Build\Main.bb"

SetBuffer(BackBuffer())
Graphics 800,600,16,2

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
	EndIf

If Online=False Then RuntimeError "Could not find ServerLocation.ini"

GameCode# = Int(Input("Table number:"))

If GameCode#=<0 Then RuntimeError "Table number invalid"

remote_hiscores()
show_hiscores()
WaitKey()
End

; Engine Compliance

Function exitit()
; Do nothing
End Function

Function MainGame()
; Do Nothing
End Function