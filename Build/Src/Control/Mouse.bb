Function MouseControl(Entity,player#,LazerTyp#,PlayX#)

	If MouseHit(1) Then hit=True
	If MouseDown(1) Then down=True
	If MouseHit(2) Then second=True
	If MouseHit(3) Then nuke=True
	If KeyHit(35) Then share=True
	
	MovementX#=MouseXSpeed()
	MoveMouse GraphicsWidth()/2,GraphicsHeight()/2
	
	If MovementX#>=8 Then MovementX#=8
	If MovementX#<=-8 Then MovementX#=-8

	If mode#=3 Then
		UseKey3D(hit,down,second,entity,Player#,LazerTyp#,nuke,share)
	Else
		UseKey2D(hit,down,second,entity,Player#,LazerTyp#,nuke)
	EndIf

	If players=2 Then
		RotateEntity Camera,10,0,0,True
		PositionEntity Camera,EntityX(Entity),EntityY(Entity)+15,EntityZ(Entity)-40,True
	Else
		RotateEntity Camera,30,0,0,True
		PositionEntity Camera,EntityX(Entity),EntityY(Entity)+15,EntityZ(Entity)-17.5,True
	EndIf
	
End Function