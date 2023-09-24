; Used for removing bad highscores from the highscore table

Version# = 1.1

AppTitle "Highscore Remover v"+Version#

SetBuffer(BackBuffer())

Graphics 800,600,16,2

Include "..\..\build\Main.BB"

Global fntArial=LoadFont("Arial",24)
SetFont fntArial

Global Target#

If FileType("TargetScore.txt")=1 Then
	Flip
	FileStream = ReadFile("TargetScore.txt")
	chkversion = ReadLine(FileStream)
	Target = ReadLine(FileStream)
	CloseFile FileStream
Else
	chkversion = Int(Input("Gamecode:"))
	Target# = Input("Offending score:")
EndIf

load_hiscores("hiscore"+Str(Int(chkversion))+".dat")
draw_hiscores()

removed = remove_hiscore(Target)

If removed = True
	Cls
	Text 0,0,"Succesfully removed hiscore"
	Flip
	Delay 1000
Else
	Cls
	Text 0,0,"Failed to remove hiscore"
	Flip
	Delay 1000
EndIf

save_hiscores("hiscore"+Str(Int(chkversion))+".dat")
draw_hiscores()
Flip
WaitKey()

End


; Engine compliance

Function Exitit()

End Function

Function MainGame()

End Function