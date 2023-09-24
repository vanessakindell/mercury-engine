; Mercury Engine Updater

GameName$="Attack on Planet 23"
GameShort$="23"
Version#=2.74

AppTitle GameName$+" Updater v"+Version#,"Data May Become Corrupt Cancel Update?"

Include "..\..\Build\Main.bb"

SetBuffer(BackBuffer())

Global ExeName$=GameName$
ExeName$=Replace(ExeName$," ","")

.tryagain

BlitzGet("http://www.vanessagames.com/23Version.txt", CurrentDir(), "23Version.txt")

If FileType(GameShort$+"Version.txt")<>1 Then
	Cls
	Text GraphicsWidth()/2,GraphicsHeight()/2,"File download failed.",1,1
	Flip
	Delay 500
	ExecFile(Chr(34)+ExeName$+".exe"+Chr(34)+" "+CommandLine$())
	End
EndIf

Filestream = ReadFile("23Version.txt")
LatestVersion# = ReadLine(FileStream)
ConvertFrom# = ReadLine(FileStream)
CloseFile Filestream

FileStream = ReadFile("GameVersion.txt")
CurrentVersion# = ReadLine(FileStream)
CloseFile FileStream

If LatestVersion#>CurrentVersion# And CurrentVersion#<ConvertFrom# Then
	ExecFile("http://www.vanessagames.com/downloads")
	RuntimeError "You'll have to download this update manually."	
	End
	
Else If LatestVersion#>CurrentVersion# Then
	Cls
	Text GraphicsWidth()/2,GraphicsHeight()/2,"Patching...",1,1
	Flip
	
	filegot = BlitzGet("http://www.vanessagames.com/"+GameShort$+"Update.zip", CurrentDir(), GameShort$+"Update.zip")=False
	
	If FileType(GameShort$+"Update.zip")<>1 Then
		Cls
		Text GraphicsWidth()/2,GraphicsHeight()/2,"Update Failed.",1,1
		Delay 1000
		Goto donehere
	EndIf
	
	zipIn = ZipApi_Open(GameShort$+"Update.zip")
	
	ZipApi_GotoFirstFile(zipIn)
	
	; Begin iterating through files
	Repeat
		
		; Get the current file's information
		Local fileInfo.ZIPAPI_UnzFileInfo	= ZipApi_GetCurrentFileInfo(zipIn)
		
		; Generate fancy file data, containing file name, size and compression ratio
		Local fileData$	= ""
		Local compressionRatio# = Float((fileInfo\CompressedSize) / Float(fileInfo\UnCompressedSize)) * 100.0
		
		filename$	= fileInfo\FileName
		fileData$	= LSet(filename, 30)
		fileData 	= fileData + RSet(fileInfo\CompressedSize, 14)
		fileData 	= fileData + RSet(fileInfo\UnCompressedSize, 14)
		fileData 	= fileData + RSet(compressionRatio + "%", 10)
		
		DebugLog "File: ("+filename+")"
		DebugLog "Extract: "+CurrentDir()+fileData
		
		; Cleanup & output
		ZIPAPI_UnzFileInfo_Dispose(fileInfo)
		If Not Instr(filename,".") Then
			foldername=filename
			foldername=Replace(foldername,"\","")
			CreateDir CurrentDir$()+"\"+foldername
		Else
			ZipApi_ExtractFile(zipIn, filename,CurrentDir()+"\"+filename)
		EndIf
		If FileType(CurrentDir()+filename)=0 Then RuntimeError "Patch fail: "+CurrentDir()+filename
		
	Until ZipApi_GotoNextFile(zipIn) = ZIPAPI_END_OF_LIST_OF_FILE
		
	If FileType(CurrentDir()+"\23Version.txt")=1 Then DeleteFile(CurrentDir()+"\23Version.txt")
	If FileType(GameShort$+"Update.zip")=1 Then DeleteFile(GameShort$+"Update.zip")
Else If LatestVersion#<CurrentVersion# Then
	SetBuffer(BackBuffer())
	Cls
	Text GraphicsWidth()/2,GraphicsHeight()/2,"You're running a development version!",1,1
	Flip
	Delay 1000	
Else

	SetBuffer(BackBuffer())
	Cls
	Text GraphicsWidth()/2,GraphicsHeight()/2,"Already latest version",1,1
	Flip
	Delay 1000
EndIf

	If FileType("PatchNotes.txt")=1 Then
		PatchNotes = ReadFile("PatchNotes.txt")
		Graphics 800,600,32,2
		Cls
		Locate 0,0
		While Not Eof(PatchNotes)
			Print ReadLine(PatchNotes)
		Wend
		Print
		Print "Press any Key To Continue"
		Flip
		WaitKey()
	EndIf
.donehere

ExecFile(Chr(34)+ExeName$+".exe"+Chr(34)+" "+CommandLine$())
End

; Engine compliance nonsense
Function Exitit()

End Function

Function MainGame()

End Function