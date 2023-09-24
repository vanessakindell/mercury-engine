Function LoadSettings()
	If FileType("GameSettings.dat")=1 Then
		FileStream = ReadFile("GameSettings.dat")
		BGMVolume# = ReadLine(FileStream)
		Online = ReadLine(FileStream)
		SFXVolume# = ReadLine(FileStream)
		CloseFile FileStream
	Else
		SaveSettings()
	EndIf
End Function

Function SaveSettings()
	FileStream = WriteFile("GameSettings.dat")
	WriteLine FileStream, BGMVolume#
	WriteLine FileStream, Online
	WriteLine FileStream, SFXVolume#
	CloseFile FileStream
End Function