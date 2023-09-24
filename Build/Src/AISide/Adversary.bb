Function Create2DAdversary(Speed#,X#,Y#,Health#,XSpeed#)
	A.Adversary2D = New Adversary2D
	A\X#=X#
	A\Y#=Y#
	A\Speed#=Speed#
	A\XSpeed#=XSpeed#
	A\Health#=Health
End Function

Function Update2DAdversary(A.Adversary2D,LazerType#,DropP,DropH,DropI,DropR)
	If A\Health#<1 Then
		CHN_SFX=PlaySFX(SFX_Boom)
		ChannelPan CHN_SFX,(A\X#-(GraphicsWidth()/2))/(GraphicsWidth()/2)
		If Rand(1,(Wave#*2))=1 Then
			DropPowerUp(A\X#,A\Y#,DropP,DropH,DropI,DropR)
		EndIf
		score=score+1
		CreateExplosion(A\X#,A\Y#)
		Delete A
		Return
	EndIf
	DrawImage GFX_Game_Adversary,A\X#,A\Y#
	If A\Health#>1 Then Text A\X#,A\Y#,Int(A\Health#),1,1
	A\Y#=(A\Y#+A\Speed#)*(GraphicsHeight()/480)
	A\X#=(A\X#+A\XSpeed#)*(GraphicsHeight()/480)
	If A\X#<0 Or A\X#>GraphicsWidth() Then A\XSpeed#=-A\XSpeed#
	If A\Y#>GraphicsHeight() + ImageHeight(GFX_Game_Adversary) Then
		A\Y#=-(ImageHeight(GFX_Game_Adversary))
	EndIf
	For B.Bullet2d = Each Bullet2d
		If ImagesCollide(GFX_Game_Bullet,B\X#,B\Y#,0,GFX_Game_Adversary,A\X#,A\Y#,0) Then
			score=score+1
			If LazerType#<5 Then
				A\Health#=A\Health#-LazerType#
			Else If LazerType#<10 Then
				A\Health#=A\Health#-(LazerType#-3)
			Else If LazerType#<15 Then
				A\Health#=A\Health#-(LazerType#-6)
			Else If LazerType#>=15 Then
				A\Health#=A\Health#-(LazerType#-9)
			EndIf
			If A\Health#>0 Then
			CHN_SFX=PlaySFX(SFX_Bang)
			ChannelPan CHN_SFX,(A\X#-(GraphicsWidth()/2))/(GraphicsWidth()/2)
			EndIf
			Delete B
		EndIf
	Next
	If ImagesCollide(GFX_Game_Adversary,A\X#,A\Y#,0,GFX_Game_Player1,PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),0) Then
			Lives#=Lives#-1
			CHN_SFX=PlaySFX(SFX_Boom)
			ChannelPan CHN_SFX,(A\X#-(GraphicsWidth()/2))/(GraphicsWidth()/2)
			CreateExplosion(A\X#,A\Y#)
			Delete A
			Return
	EndIf
End Function

Function Create2dWave(Typeof#,Speed#)
	For Numb# = 1 To Wave#
		If TypeOf#=1 Then
			Create2DAdversary(Speed#+(Wave#/Numb#),(GraphicsWidth()/(Wave#+1))*Numb#,-(ImageHeight(GFX_Game_Adversary)),1,Rand(-4,4))
		Else
			Create2DAdversary(Speed#+(Wave#/Numb#),(GraphicsWidth()/(Wave#+1))*Numb#,-(ImageHeight(GFX_Game_Adversary)),Wave#,Rand(-4,4))
		EndIf
	Next
End Function

Function Create3DAdversary(Speed#,X#,Z#,Health#,XSpeed#)
	A.Adversary3D = New Adversary3D
	A\X#=X#
	A\Y#=20
	A\Z#=Z#
	A\Speed#=Speed#
	A\XSpeed#=XSpeed#
	A\Health#=Health#
	A\StartHealth#=Health#
	A\Offset#=Rand(0,8)
	If Not MSH_Adversary Then RuntimeError "Adversary not loaded."
	A\Entity=CopyEntity(MSH_Adversary)
	If Not A\Entity Then RuntimeError "Adversary not activated."
	EntityType A\Entity,Type_Adversary
	ScaleEntity A\Entity,.5,.5,.5
	EntityBox A\Entity,-20,-20,-20,40,40,40
	PositionEntity A\Entity,A\X#-50,0,A\Z#
	Hostile = Rand(10)
	If Hostile=1 Then
		A\Hostility=True
	Else
		A\Hostility=False
	EndIf
	Smart = Rand(10)
	If Smart=1 Then
		A\Intelligence=True
	Else
		A\Intelligence=False
	EndIf
End Function

Function Update3DAdversary(A.Adversary3D,LazerType#,DropP,DropH,DropI,DropR,intromode=False)
	If EntityX(A\Entity)>150 Or EntityX(A\Entity)<-150 Then A\XSpeed#=-A\XSpeed#
	If EntityZ(A\Entity)<-180 Then PositionEntity A\Entity,EntityX(A\Entity),EntityY(A\Entity),120
	If intromode Then MoveEntity A\Entity,A\XSpeed#/4,0,-(A\Speed#/4)
	
	If EntityX(A\Entity)>160 Or EntityX(A\Entity)<-160 Then
		PositionEntity A\Entity,0,0,120
	EndIf
	
	EntityColor A\Entity,255,(A\Health#/A\StartHealth#)*255,(A\Health#/A\StartHealth#)*255
			
	A\Timing=A\Timing+1
	
	If Not intromode Then
		If EntityX(A\Entity)-10<EntityX(MSH_Player) And EntityX(A\Entity)+10>EntityX(MSH_Player) And skip=A\Offset# And EntityZ(A\Entity)>-100 Then
			Create3dbullet(EntityX(A\Entity),EntityY(A\Entity),EntityZ(A\Entity),180,0,1,True)
		EndIf
		If EntityX(A\Entity)-10<EntityX(MSH_Player2) And EntityX(A\Entity)+10>EntityX(MSH_Player2) And skip=A\Offset# And EntityZ(A\Entity)>-100 And Players=2 Then
			Create3dbullet(EntityX(A\Entity),EntityY(A\Entity),EntityZ(A\Entity),180,0,1,True)
		EndIf
		
		If A\Hostility Then
			If Int(EntityX(A\Entity))>Int(EntityX(MSH_Player)) And EntityX(A\Entity)-40<EntityX(MSH_Player) And EntityX(A\Entity)>-100 Then
				MoveEntity A\Entity,-1,0,-(A\Speed#/4)
			Else If Int(EntityX(A\Entity))<Int(EntityX(MSH_Player)) And EntityX(A\Entity)+40>EntityX(MSH_Player) And EntityX(A\Entity)<100 Then
				MoveEntity A\Entity,1,0,-(A\Speed#/4)
			Else
				MoveEntity A\Entity,A\XSpeed#/4,0,-(A\Speed#/4)
			EndIf
		Else
			MoveEntity A\Entity,A\XSpeed#/4,0,-(A\Speed#/4)
		EndIf
		
		If EntityDistance(A\Entity,MSH_Player)<40 Then
			If Not ChannelPlaying(A\CHN_WRN) Then
				A\CHN_WRN=EmitSFX(SFX3_CRASHBUZ,A\Entity)
			EndIf
			EntityColor A\Entity,0,0,255
		EndIf
	
		For B.Bullet3D = Each Bullet3D
			If A\Intelligence Then
				If Int(EntityX(A\Entity))>Int(EntityX(B\Entity)) And EntityX(A\Entity)-40<EntityX(B\Entity) And B\FromEnemy=False And EntityZ(A\Entity)<EntityZ(B\Entity) And EntityDistance(A\Entity,B\Entity)<100  And EntityX(A\Entity)>-140 Then
					MoveEntity A\Entity,1,0,0
				Else If Int(EntityX(A\Entity))<Int(EntityX(B\Entity)) And EntityX(A\Entity)+40>EntityX(B\Entity) And B\FromEnemy=False And EntityZ(A\Entity)<EntityZ(B\Entity) And EntityDistance(A\Entity,B\Entity)<100 And EntityZ(A\Entity)<140 Then
					MoveEntity A\Entity,-1,0,0
				EndIf
			EndIf
			If EntityDistance(A\Entity, B\Entity)<10 And B\FromEnemy=False Then
					score=score+1
					A\Health#=A\Health#-B\LazerType#			
					If A\Health#>=1 Then
						CHN_SFX3=EmitSFX(SFX3_Bang,A\Entity)
					EndIf
					HideEntity B\Entity
					Delete B
			EndIf
		Next
		
		For As.Asteroid3D = Each Asteroid3D
			If EntityDistance(A\Entity, As\Entity)<10 Then
				A\Health#=A\Health#-1
				If A\Health#>=1 Then
					CHN_SFX3=EmitSFX(SFX3_Bang,A\Entity)
				EndIf
				Create3DExplosion(EntityX(As\Entity),EntityY(As\Entity),EntityZ(As\Entity),0,False)
				CHN_SFX3=EmitSFX(SFX3_Bang,As\Entity)
				HideEntity As\Entity
				Delete As
			EndIf
		Next
	EndIf
	
	If draw=1 Then
		DrawImage GFX_Game_Adversary,((EntityX(A\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(A\Entity)+120)/240)*GraphicsHeight()
		If A\Health#>1 Then Text ((EntityX(A\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(A\Entity)+120)/240)*GraphicsHeight(), Int(A\Health#)
		EntityAlpha A\Entity,.5
	Else If draw=0 Then
		EntityAlpha A\Entity,1
	Else If draw=3 Then
		DrawImage GFX_Game_Adversary,((EntityX(A\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(A\Entity)+120)/240)*GraphicsHeight()
		If A\Health#>1 Then Text ((EntityX(A\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(A\Entity)+120)/240)*GraphicsHeight(), Int(A\Health#)
		EntityAlpha A\Entity,0
	EndIf
	
	If A\Health#<1 Then
		If Rand(Int(Wave#))=1 Then
			Drop3DPowerUp(EntityX(A\Entity),EntityY(A\Entity),EntityZ(A\Entity),DropP,DropH,DropI,DropR)
		EndIf
		score=score+1
		Create3DExplosion(EntityX(A\entity),EntityY(A\entity),EntityZ(A\entity),0,True)
		HideEntity A\Entity
		Delete A
		Return
	EndIf
	
	If Not intromode Then
		If EntityDistance(A\Entity, MSH_Player)<20 Then
			If TempInv#<1 Then
				TempInv#=30
				InvType#=0
				Lives#=Lives#-1
				PlaySFX(SFX_AHHHH)
			EndIf
			Create3DExplosion(EntityX(A\entity),EntityY(A\entity),EntityZ(A\entity),0,True)
			HideEntity A\Entity
			Delete A
			Return
		Else If EntityDistance(A\Entity, MSH_Player2)<20 And players=2 Then
			If TempInv2#<1 Then
				TempInv2#=30
				InvType2#=0
				Lives2#=Lives2#-1
				PlaySFX(SFX_AHHHH)
			EndIf
			Create3DExplosion(EntityX(A\entity),EntityY(A\entity),EntityZ(A\entity),0)	
			HideEntity A\Entity
			Delete A
			Return
		EndIf
	EndIf
End Function

Function Create3dWave(Typeof#,Speed#)
	PlaySFX(SFX_PROUT)
	score=score+1
	For Numb# = 1 To Wave#
		If TypeOf#=1 Then
			XSpeed#=Rand(-4,4)
			Create3DAdversary(Speed#+Sin(Wave#/Numb#),(100/(Wave#+1))*Numb#,-160+Rand(20),1,XSpeed#)
		Else
			XSpeed#=Rand(-4,4)
			Create3DAdversary(Speed#+Sin(Wave#/Numb#),(100/(Wave#+1))*Numb#,-160+Rand(20),Rand(Int(Wave#/2)+1,Wave#),XSpeed#)
		EndIf
	Next
End Function