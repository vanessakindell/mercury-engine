Function IntroVideo()
	;play video
	Local Intro
	If FileType("video\intro.avi")=0 Then
		Intro=OpenMovie("video\Intro.mpg")
	Else
		Intro=OpenMovie("video\Intro.avi")
	EndIf
	If Intro=0 Then
		RuntimeError "Error - Movie Not Loaded"
	EndIf
	If Not MoviePlaying(Intro) Then
		RuntimeError "Error - Movie Not Playing"
	EndIf
	
	While MoviePlaying(Intro)=True And KeyHit(1)=False And KeyHit(28)=False And KeyHit(57)=False
	Cls
	DrawMovie Intro,0,0,GraphicsWidth(),GraphicsHeight()
	Flip
	Wend
	
	CloseMovie(Intro)
End Function

Function CueDialog(Dialog$,tSubtitle$)
	D.Dialog = New Dialog
	Insert D After Last Dialog
	D\File$=Dialog$
	D\Subtitle$=tSubtitle$
End Function

Function UpdateDialog()
	If ShowSubtitle#>0 Then
		Text ImageWidth(HUDImage)/2,ImageHeight(HUDImage)-FontHeight(),Subtitle$,1
		ShowSubtitle#=ShowSubtitle#-1
	EndIf
	If Not ChannelPlaying(CHN_DIALOG) Then
		DiCount#=0
		For D.Dialog = Each Dialog
			DiCount#=DiCount#+1
		Next
		If DiCount#>0 Then
			D.Dialog = First Dialog
			If FileType("SFX\"+D\File$)<>1 Then RuntimeError "Could not find "+"SFX\"+D\File$
			CHN_DIALOG=PlaySFXFile(CurrentDir()+"\SFX\"+D\File$)
			If Not CHN_DIALOG Then RuntimeError "Could not start dialog playback"
			Subtitle$=D\Subtitle$
			ShowSubtitle#=300
			Delete D
		EndIf
	EndIf
End Function	

Function CreateSun()
	S.Sun = New Sun
	S\Entity = CreateSphere(32)
	SunTexture = LoadTexture("GFX\Game\Sun.png")
	EntityTexture S\Entity,SunTexture
	EntityFX S\Entity,1
	ScaleEntity S\Entity,400,400,400
	PositionEntity S\Entity,0,0,1500
End Function
	
Function UpdateSun()
	For S.Sun = Each Sun
		MoveEntity S\Entity,0,0,-1
		If EntityDistance(S\Entity,MSH_Player)<400 Then
			Create3DExplosion(EntityX(MSH_Player),EntityY(MSH_Player),EntityZ(MSH_Player),0,True)
			HideEntity MSH_Player
			If Players=2 Then
				Create3DExplosion(EntityX(MSH_Player2),EntityY(MSH_Player2),EntityZ(MSH_Player2),0,True)
				HideEntity MSH_Player2
			EndIf
			For extime = 0 To 20
				Cls
				Update3DStarsUp()
				Update3DStarsDown()
				Update3DExplosions()
				MoveEntity S\Entity,0,0,-1
				UpdateWorld()
				RenderWorld()
				Flip
				WaitTimer(FrameWait)
			Next
			While KeyHit(1)=False And KeyHit(28)=False And KeyHit(57)=False And JoyHit(1)=False
				Cls
				Text GraphicsWidth()/2,(GraphicsHeight()/2)-(FontHeight()/2),"You either go out in a blaze of glory,",1,1
				Text GraphicsWidth()/2,(GraphicsHeight()/2)+(FontHeight()/2),"or live long enough to find yourself the villian.",1,1
				Flip
				WaitTimer(FrameWait)
			Wend
			HideEntity S\Entity
			Delete S
			exitit()
		EndIf
	Next
End Function

Function Click()
	CHN_Click=PlaySFX(SFX_Click)
	While ChannelPlaying(CHN_Click)=True
	Wend
End Function

Function Create3DAsteroid()
	R.Asteroid3D = New Asteroid3D
	R\Entity = CreateSphere()
	AsterTex=LoadTexture("GFX\Game\AsterTex.jpg")
	ScaleEntity R\Entity,10,10,10
	EntityTexture R\Entity,AsterTex
	PositionEntity R\Entity,Rand(-130,130),0,120
	R\Speed = Rand(1,10)
	R\RotP# = Rand(-10,10)
	R\RotY# = Rand(-10,10)
	R\RotR# = Rand(-10,10)
End Function

Function Update3DAsteroids()
	Ascnt#=0
	For A.Asteroid3D = Each Asteroid3D
		AsCnt#=AsCnt#+1
		A\Timing=A\Timing+1
		
		TurnEntity A\Entity, A\RotP#,A\RotY#,A\RotR#
		TranslateEntity A\Entity,0,0,-(A\Speed/10)
		
		If EntityZ(A\Entity)<-120 Then
			HideEntity A\Entity
			Delete A
			Return
		EndIf
		
		If EntityDistance(A\Entity, MSH_Player)<10 Then
			If TempInv#<1 Then
				TempInv#=30
				InvType#=0
				Lives#=Lives#-1
				PlaySFX(SFX_AHHHH)
			EndIf
			Create3DExplosion(EntityX(A\entity),EntityY(A\entity),EntityZ(A\entity),0,False)
			CHN_SFX3=EmitSFX(SFX3_Bang,A\Entity)
			HideEntity A\Entity
			Delete A
			Return
		Else If EntityDistance(A\Entity, MSH_Player2)<10 And players=2 And TempInv2#<1 Then
			If TempInv#<1 Then
				TempInv#=30
				InvType#=0
				Lives#=Lives#-1
				PlaySFX(SFX_AHHHH)
			EndIf
			Create3DExplosion(EntityX(A\entity),EntityY(A\entity),EntityZ(A\entity),0,False)
			CHN_SFX3=EmitSFX(SFX3_Bang,A\Entity)
			HideEntity A\Entity
			Delete A
			Return
		EndIf
		
		If draw=1 Then
			EntityAlpha A\Entity,.5
			DrawImage GFX_Game_Asteroid,((EntityX(A\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(A\Entity)+120)/240)*GraphicsHeight()
		Else If draw=3 Then
			EntityAlpha A\Entity, 0
			DrawImage GFX_Game_Asteroid,((EntityX(A\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(A\Entity)+120)/240)*GraphicsHeight()
		Else
			EntityAlpha A\Entity,1
		EndIf
		
		For B.Bullet3D = Each Bullet3D
			If EntityDistance(A\Entity, B\Entity)<10 Then
					Create3DExplosion(EntityX(A\Entity),EntityY(A\Entity),EntityZ(A\Entity),0,False)
					CHN_SFX3=EmitSFX(SFX3_Bang,A\Entity)
					HideEntity A\Entity
					HideEntity B\Entity
					Delete A
					Delete B
					Return
			EndIf
		Next
		As2Cnt#=0
		For As.Asteroid3D = Each Asteroid3D
			As2Cnt#=As2Cnt#+1
			If AsCnt#<>As2Cnt# Then
				If EntityDistance(A\Entity,As\Entity)<10 And A\Timing>25 Then
					Create3dExplosion(EntityX(As\Entity),EntityY(As\Entity),EntityZ(As\Entity),0,False)
					HideEntity As\Entity
					Delete As
					Return
				EndIf
			EndIf
		Next
	Next
End Function

Function LoadingScreen()
	Cls
	Loads#=0
	For L.LoadText = Each LoadText
		Loads#=LoadS#+1
	Next
	LoadSelect# = Rand(Loads#)
	LoadCnt#=0
	For L.LoadText = Each LoadText
		LoadCnt#=LoadCnt#+1
		If LoadCnt#=LoadSelect# Then
			Text GraphicsWidth()/2,GraphicsHeight()/2-FontHeight()/2,"Loading...",1,1
			Text GraphicsWidth()/2,GraphicsHeight()/2+FontHeight()/2,L\LoadText$,1,1
		EndIf
	Next
	Flip
End Function

Function AddLoad(thetext$)
	L.LoadText = New LoadText
	L\LoadText$ = thetext$
End Function

Function Create3DStarbase()
	S.Starbase3D = New Starbase3D
	S\Entity = CopyEntity(MSH_Starbase)
	PositionEntity S\Entity,0,0,200
	S\Health = Wave#^2
End Function

Function Update3DStarbase(DropP,DropH,DropI,DropR)
	For S.Starbase3D = Each Starbase3D
		TurnEntity S\Entity,0,1,0
		
		If EntityZ(S\Entity)>0 Then TranslateEntity S\Entity,0,0,-1

		For B.Bullet3D = Each Bullet3D
			If EntityDistance(S\Entity, B\Entity)<30 Then
					score=score+1
					S\Health#=S\Health#-B\LazerType#
					
					If S\Health#>=1 Then
						CHN_SFX3=EmitSFX(SFX3_Bang,B\Entity)
					EndIf
					HideEntity B\Entity
					Delete B
			EndIf
		Next
		If AdvNumb#<Wave# Then Create3DAdversary(Rand(1,5),EntityX(S\Entity)+50,EntityZ(S\Entity),4+(Wave#/25),Rand(-4,4))
		If draw=1 Then
			EntityAlpha S\Entity,.5
			DrawImage GFX_Game_Starbase,((EntityX(S\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(S\Entity)+120)/240)*GraphicsHeight()
		Else If draw=3 Then
			EntityAlpha S\Entity, 0
			DrawImage GFX_Game_Starbase,((EntityX(S\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(S\Entity)+120)/240)*GraphicsHeight()
		Else
			EntityAlpha S\Entity,1
		EndIf
		
		If S\Health < 0 Then
			Create3DExplosion(EntityX(S\Entity),EntityY(S\Entity),EntityZ(S\Entity),0,True)
			Drop3DPowerup(EntityX(S\Entity),EntityY(S\Entity),EntityZ(S\Entity),DropP,DropH,DropI,DropR)
			HideEntity S\Entity
			Delete S
			Return
		EndIf
	Next

End Function
			
Function Create2DBullet(X#,Y#,player,LazerTyp#)
	B.Bullet2D = New Bullet2D
	B\X#=X#
	B\Y#=Y#
	B\PlayerSrc#=player
	B\LazerType#=LazerTyp#
	CHN_SFX=PlaySFX(SFX_Fire)
	ChannelPan CHN_SFX,(B\X#-(GraphicsWidth()/2))/(GraphicsWidth()/2)
End Function

Function Update2DBullet()
For b.bullet2d = Each bullet2d
	DrawImage GFX_Game_Bullet,B\X#,B\Y#
	B\Y#=(B\Y#-5)*(GraphicsHeight()/480)
	If B\Y#=-(ImageHeight(GFX_Game_Bullet)) Then
		Delete b.bullet2D
	EndIf
Next
End Function

Function Create3DBullet(X#,Y#,Z#,Yaw#,player,LazerTyp#,FromEnemy=False)
	B.Bullet3D = New Bullet3D
	B\Roll#=0
	B\RollSpeed#=Rand(-10,10)
	B\Playersrc=Player
	B\LazerType#=LazerTyp#
;	RuntimeError X#+","+Y#+","+Z#
	B\FromEnemy=FromEnemy
	
	B\Entity=CopyEntity(MSH_Bullet)
		EntityType B\Entity, Type_Bullet
		PositionEntity B\Entity,X#,Y#-1,Z#
		RotateEntity B\Entity,0,Yaw#,0
		EntityAlpha B\Entity,.75
	
	If Player = 1 Then
		Select ColorType#
			Case 1:
				EntityColor B\Entity,255,0,0
			Case 2:
				EntityColor B\Entity,255,150,0
			Case 3:
				EntityColor B\Entity,255,255,0
			Case 4:
				EntityColor B\Entity,0,255,0
			Case 5:
				EntityColor B\Entity,0,0,255
			Case 6:
				EntityColor B\Entity,255,0,255		
			Default:
				EntityColor B\Entity,123,55,123
		End Select
	Else If Player = 2
		Select ColorType2#
			Case 1:
				EntityColor B\Entity,255,0,0
			Case 2:
				EntityColor B\Entity,255,150,0
			Case 3:
				EntityColor B\Entity,255,255,0
			Case 4:
				EntityColor B\Entity,0,255,0
			Case 5:
				EntityColor B\Entity,0,0,255
			Case 6:
				EntityColor B\Entity,255,0,255		
			Default:
				EntityColor B\Entity,123,55,123
		End Select
	Else
		EntityColor B\Entity,180,180,180
	EndIf
			
	CHN_SFX3=EmitSFX(SFX3_Fire,B\Entity)	
End Function

Function Update3DBullet()
For b.bullet3d = Each bullet3d
	MoveEntity B\Entity,0,0,4
	B\Roll#=B\Roll#+B\RollSpeed#
	RotateEntity B\Entity,EntityPitch(B\Entity),EntityYaw(B\Entity),B\Roll#
	
	If draw=1 Then
		EntityAlpha B\Entity, .2
		DrawImage GFX_Game_Bullet,((EntityX(B\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(B\Entity)+120)/240)*GraphicsHeight()
	Else If draw=3 Then
		EntityAlpha B\Entity, 0
		DrawImage GFX_Game_Bullet,((EntityX(B\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(B\Entity)+120)/240)*GraphicsHeight()
	Else If draw=0 Then
		EntityAlpha B\Entity,.75
	EndIf
	
	If EntityZ(B\Entity)>120 Or EntityZ(B\Entity)<-120 Then
		HideEntity B\Entity
		Delete b.bullet3D
	Else If EntityDistance(B\Entity, MSH_Player)<10 And B\FromEnemy Then
		If TempInv#<1 Then
			TempInv#=30
			InvType#=0
			Lives#=Lives#-1
			PlaySFX(SFX_AHHHH)
		EndIf
		Create3DExplosion(EntityX(B\entity),EntityY(B\entity),EntityZ(B\entity),0,True)
		HideEntity B\Entity
		Delete B
		Return
	Else If EntityDistance(B\Entity, MSH_Player2)<10 And players=2 And B\FromEnemy Then
		If TempInv2#<1 Then
			TempInv2#=30
			InvType2#=0
			Lives2#=Lives2#-1
			PlaySFX(SFX_AHHHH)
		EndIf
		Create3DExplosion(EntityX(B\entity),EntityY(B\entity),EntityZ(B\entity),0)	
		HideEntity B\Entity
		Delete B
		Return
	EndIf
Next
End Function

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

Function Create3DStar()
	S.Star3D=New Star3D
	S\X#=Rand(-800,800)
	S\Y#=Rand(-200,200)
	S\Z#=400
	S\Speed#=Rand(1,8)
End Function

Function Update3DStarsDown(move=True)
For S.Star3D = Each Star3D
	If move=True Then S\Z#=S\Z#-S\Speed#
	CameraProject Camera,S\X#,S\Y#,S\Z#
	If S\Y#>0 And iAdditional=False Then WritePixel(ProjectedX(),ProjectedY(),-1)
	If S\Z#<-400 Then
		Delete S
		Create3DStar()
	EndIf
Next
End Function

Function Update3DStarsUp(move=True)
For S.Star3D = Each Star3D
	If move Then S\Z#=S\Z#-S\Speed#
	CameraProject Camera,S\X#,S\Y#,S\Z#
	If S\Y#<0 And iAdditional=False Then WritePixel(ProjectedX(),ProjectedY(),-1)
	If S\Z#<-400 Then
		Delete S
		Create3DStar()
	EndIf
	Next
End Function

Function Update3DStarsDown2()
For S.Star3D = Each Star3D
	S\Z#=S\Z#-S\Speed#
	CameraProject Camera2,S\X#,S\Y#,S\Z#
	If S\Y#<=0 And iAdditional=False Then WritePixel(ProjectedX(),ProjectedY()+GraphicsHeight()/2,-1)
	If S\Z#<-400 Then Delete S
Next
End Function

Function Update3DStarsUp2()
For S.Star3D = Each Star3D
	CameraProject Camera2,S\X#,S\Y#,S\Z#
	If S\Y#<=0 And iAdditional=False Then WritePixel(ProjectedX(),ProjectedY()+GraphicsHeight()/2,-1)
	If S\Z#<-400 Then Delete S
Next
End Function

Function DrawStars()
For S.Star = Each Star
	Rect(S\X#,S\Y#,GraphicsWidth()/640,GraphicsWidth()/640,1)
Next
End Function

Function Nuke()
	If AllowNuke=True Then
		For A2.Adversary2D = Each Adversary2D
			score=score-10
			CreateExplosion(A2\X#,A2\Y#)
			Delete A2
		Next
		For A3.Adversary3D = Each Adversary3D
			score=score-10
			Create3DExplosion(EntityX(A3\Entity),EntityY(A3\Entity),EntityZ(A3\Entity),0,False)
			PlaySFX SFX_Nuke
			HideEntity A3\Entity
			Delete A3
		Next
	EndIf
End Function