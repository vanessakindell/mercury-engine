Function KeyControl(Entity,player#,LazerTyp#,PlayX#)

	If KeyHit(57) Then hit=True
	If KeyDown(57) Then down=True
	If KeyHit(28) Then second=True
	If KeyHit(49) Then nuke=True
	If KeyHit(31) Then share=True

	MovementX#=0
	If KeyDown(203) Then MovementX#=-8
	If KeyDown(205) Then MovementX#=8

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