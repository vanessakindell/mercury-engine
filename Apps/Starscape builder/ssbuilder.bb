AppTitle "Starscape builder v1.00"

Graphics 640,480,32,2

Type Star
	Field X#,Y#,Speed#
End Type

World=CreateImage(640,480)
SetBuffer(ImageBuffer(world))

For times = 1 To 640
	Cls
	Createstar()
	UpdateStars()
Next

SetBuffer(BackBuffer())

Cls
DrawImage world,0,0
Flip

SaveImage world,"starscape.bmp"

WaitKey()
End

Function CreateStar()
	S.Star=New Star
	S\X#=Rand(1,GraphicsWidth())
	S\Y#=0
	S\Speed#=Rand(1,4)
End Function

Function UpdateStars()
For S.Star = Each Star
	S\Y#=(S\Y#+S\Speed#)*(GraphicsHeight()/480)
	Rect(S\X#,S\Y#,GraphicsWidth()/640,GraphicsWidth()/640,1)
	If S\Y#>GraphicsHeight() Then Delete S
Next
End Function