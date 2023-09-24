Function Joy2Control(Entity,player#,LazerTyp#,PlayX#)

	If JoyHit(1,1) Or JoyHit(6,1) Then hit=True
	If JoyDown(1,1) Or JoyDown(6,1) Or JoyZDir(1)=-1 Then down=True
	If JoyHit(2,1) Or JoyHit(5,1) Or JoyZDir(1)=1 Then second=True
	If JoyHit(3,1) Then nuke=True
	prevHAT2=Hat2
	Hat2=JoyHat(1)
	If prevHat2<>180 And Hat2=180 Then share=True
			
	If Abs(JoyX(1))<.2 Then
		MovementX#=0
	Else
		MovementX#=JoyX(1)*8
	EndIf
	
	If mode#=3 Then
		UseKey3D(hit,down,second,entity,Player#,LazerTyp#,nuke,share)
	Else
		UseKey2D(hit,down,second,entity,Player#,LazerTyp#,nuke)
	EndIf
	
End Function

Function Joy2Camera(Camera,Entity,player#)
	JoyPit = JoyPitch(1)
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
