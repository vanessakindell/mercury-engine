Function Joy1Control(Entity,player#,LazerTyp#,PlayX#)

	If JoyHit(1) Or JoyHit(6) Then hit=True
	If JoyDown(1) Or JoyDown(6) Or JoyZDir()=-1 Then down=True
	If JoyHit(2) Or JoyHit(5) Or JoyZDir()=1 Then second=True
	If JoyHit(3) Then nuke=True
	prevHAT=Hat
	Hat=JoyHat()
	If prevHat<>180 And Hat=180 Then share=True
	
	If Abs(JoyX())<.2 Then
		MovementX#=0
	Else
		MovementX#=JoyX()*8
	EndIf

	If mode#=3 Then
		UseKey3D(hit,down,second,entity,Player#,LazerTyp#,nuke,share)
	Else
		UseKey2D(hit,down,second,entity,Player#,LazerTyp#,nuke)
	EndIf
	
End Function

Function Joy1Camera(Camera,Entity,player#)
	JoyPit = JoyPitch()
	If JoyPit < 10 And JoyPit > 0 Then JoyPit = 0
	If JoyPit > -10 And JoyPit < 0 Then JoyPit = 0
	
	If players=2 Then
		RotateEntity Camera,10,-JoyPit/4,0,True
		PositionEntity Camera,EntityX(Entity),EntityY(Entity)+15,EntityZ(Entity)-40-(JoyYaw()/18),True
	Else
		RotateEntity Camera,30,-JoyPit/4,0,True
		PositionEntity Camera,EntityX(Entity),EntityY(Entity)+15,EntityZ(Entity)-17.5-(JoyYaw()/18),True
	EndIf
End Function
