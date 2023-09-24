Function Usekey3D(hit,down,second,entity,Player#,LazerTyp#,nuke,share)
	
	If hit=True Then
			If LazerTyp#<5 Then
				Create3dbullet(EntityX(entity),EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			Else If LazerTyp#<10 Then
				Create3dbullet(EntityX(entity)+6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)-6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			Else If LazerTyp#>=15 Then
				Create3dbullet(EntityX(entity),EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)+6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)-6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			EndIf
	EndIf
	If share=True Then
		If Players=2 Then
			If Player#=1 Then
				If Lives#>1 Then
					Lives#=Lives#-1
					Lives2#=Lives2#+1
				EndIf
			Else If Player#=2 Then
				If Lives2#>1 Then
					Lives2#=Lives2#-1
					Lives#=Lives#+1
				EndIf			
			EndIf
		EndIf
	EndIf			
	If down=True
		If skip=0 Then
			If LazerTyp#<5 Then
				Create3dbullet(EntityX(entity),EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			Else If LazerTyp#<10 Then
				Create3dbullet(EntityX(entity)+6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)-6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			Else If LazerTyp#<15 And LazerTyp#>=10 Then
				Create3dbullet(EntityX(entity)+6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)-6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			Else If LazerTyp#>=15 Then
				Create3dbullet(EntityX(entity),EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)+6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)-6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			EndIf
		Else If skip=4 Then
			If LazerTyp#<20 And LazerTyp#>=10 Then
				Create3dbullet(EntityX(entity),EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			EndIf
			If LazerTyp#>=20 Then
				Create3dbullet(EntityX(entity),EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)+6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)-6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			EndIf
		Else If skip=2 Or skip=8
			If LazerTyp#>=25 Then
				Create3dbullet(EntityX(entity),EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)+6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)-6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			EndIf
		Else
			If LazerTyp#>=30 Then
				Create3dbullet(EntityX(entity),EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)+6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
				Create3dbullet(EntityX(entity)-6,EntityY(entity),EntityZ(entity),0,player,LazerTyp#)
			EndIf
		EndIf
	EndIf
	If second=True Then
		did=False
		For B.Bullet3d = Each Bullet3d
			If B\Playersrc=player Then
				Create3DExplosion(EntityX(B\Entity),EntityY(B\Entity),EntityZ(B\Entity),LazerTyp#/2,False)
				HideEntity B\Entity
				Delete B
				did=True
			EndIf			
		Next
		If did CHN_SFX=PlaySound(SFX_Boom)
	EndIf
	If nuke=True Then
		If score>AdvNumb#*10 Then
			Nuke()
		EndIf
	EndIf
	
	RotateEntity entity,0,180,MovementX#

End Function

Function UseKey2D(hit,down,second,Player#,LazerTyp#,PlayX#,nuke)
	If LazerTyp#<=5 Then
		If hit=True Then
			Create2DBullet(PlayX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),player,LazerTyp#)
		EndIf
	Else If LazerTyp#<=10 Then
		If hit=True Then
			Create2DBullet(PlayX#+(ImageWidth(GFX_Game_Player1)/3),GraphicsHeight()-ImageHeight(GFX_Game_Player1),Player#,LazerTyp#)
			Create2DBullet(PlayX#-(ImageWidth(GFX_Game_Player1)/3),GraphicsHeight()-ImageHeight(GFX_Game_Player1),player,LazerTyp#)
		EndIf
	Else If LazerTyp#<=15 Then
		If down=True  And skip=0Then
			Create2DBullet(PlayX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),player,LazerTyp#)
		EndIf
	Else If LazerTyp#>15 Then
		If down=True And skip=0 Then
			Create2DBullet(PlayX#+(ImageWidth(GFX_Game_Player1)/3),GraphicsHeight()-ImageHeight(GFX_Game_Player1),player,LazerTyp#)
			Create2DBullet(PlayX#-(ImageWidth(GFX_Game_Player1)/3),GraphicsHeight()-ImageHeight(GFX_Game_Player1),player,LazerTyp#)
		EndIf
	EndIf
	If second=True Then
		For B.Bullet2D = Each Bullet2d
			If B\Playersrc# = Player# Then
				CreateExplosion(B\X#,B\Y#)
				CHN_SFX=PlaySound(SFX_Boom)
				ChannelPan CHN_SFX,(B\X#-(GraphicsWidth()/2))/(GraphicsWidth()/2)
				Delete B
			EndIf
		Next
	EndIf
	If nuke=True Then
		Nuke()
	EndIf
End Function