Function DropPowerup(X#,Y#,DropP,DropH,DropI,DropR)
	If DropP=False And DropH=False And DropI=False And DropR=False Then
	
	Else
		P.PowerUp = New PowerUp
		P\X#=X#
		P\Y#=Y#
		.retrypower
		P\UpType=Rand(1,4)
		
		If P\UpType=1 And DropP=False Then Goto retrypower
		If P\UpType=2 And DropH=False Then Goto retrypower
		If P\UpType=3 And DropI=False Then Goto retrypower
		If P\UpType=4 And DropR=False Then Goto retrypower
		If P\UpType=4 And TotalPlayers=1 Then Goto retrypower
	EndIf
	
End Function

Function UpdatePowerups()
	For P.PowerUp = Each PowerUp
		P\Y#=(P\Y#+5)*(GraphicsHeight()/480)
		If P\Y#>GraphicsHeight() Then P\Y#=0
		Select P\UpType#
			Case 1:
				DrawImage GFX_Game_PowerUp,P\X#,P\Y#
				If ImagesCollide(GFX_Game_Player1,PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),0,GFX_Game_PowerUp,P\X#,P\Y#,0) Then
					score=score+1
					LazerType#=LazerType#+1
					CHN_SFX=PlaySFX(SFX_PowerUp)
					ChannelPan CHN_SFX,(P\X#-(GraphicsWidth()/2))/(GraphicsWidth()/2)
					Delete P
				EndIf
			Case 2:
				DrawImage GFX_Game_Health,P\X#,P\Y#
				If ImagesCollide(GFX_Game_Player1,PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),0,GFX_Game_Health,P\X#,P\Y#,0) Then
					score=score+1
					Lives#=Lives#+1
					CHN_SFX=PlaySFX(SFX_Heal)
					ChannelPan CHN_SFX,(P\X#-(GraphicsWidth()/2))/(GraphicsWidth()/2)
					Delete P
				EndIf
			Case 3:
				DrawImage GFX_Game_TempInv,P\X#,P\Y#
				If ImagesCollide(GFX_Game_Player1,PlayerX#,GraphicsHeight()-ImageHeight(GFX_Game_Player1),0,GFX_Game_TempInv,P\X#,P\Y#,0) Then
					score=score+1
					TempInv#=TempInv#+255
					CHN_SFX=PlaySFX(SFX_Heal)
					ChannelPan CHN_SFX,(P\X#-(GraphicsWidth()/2))/(GraphicsWidth()/2)
					Delete P
				EndIf
			Default:
				RuntimeError "Unrecognized/incompatable powerup type"
		End Select
	Next
End Function				

Function Drop3DPowerup(X#,Y#,Z#,DropP,DropH,DropI,DropR)
	P.PowerUp3D = New PowerUp3D
	
	.retrypower
	P\UpType=Rand(1,4)
	If P\UpType=1 And DropP=False Then Goto retrypower
	If P\UpType=2 And DropH=False Then Goto retrypower
	If P\UpType=3 And DropI=False Then Goto retrypower
	If P\UpType=4 And DropR=False Then Goto retrypower
	If P\UpType=4 And Players=2 Then Goto retrypower
	If P\UpType=4 And TotalPlayers=1 Then Goto retrypower

	P\Entity=CreateSphere()
	
	If P\UpType#=1 Then
		powertexture=LoadTexture("GFX\Game\Powerup.bmp")
	Else If P\UpType#=2 Then
		powertexture=LoadTexture("GFX\Game\Health.bmp")
	Else If P\UpType#=3 Then
		powertexture=LoadTexture("GFX\Game\TempInv.bmp")
	Else
		powertexture=LoadTexture("GFX\Game\Revive.bmp")
	EndIf
	TextureFilter powertexture,4
	EntityTexture P\Entity,powertexture
	ScaleEntity P\Entity,3,3,3
	PositionEntity P\Entity,X#,Y#,Z#
	EntityAlpha P\Entity,.9
End Function

Function Update3DPowerups()
	For P.PowerUp3D = Each PowerUp3D
		MoveEntity P\Entity,0,0,-(25/24)
		
		;If EntityX(P\Entity)-10<EntityX(MSH_Player) And EntityX(P\Entity)+10>EntityX(MSH_Player) Then
		;	If Not ChannelPlaying(CHN_ADVLOC) Then
		;		CHN_ADVLOC=PlaySFX(SFX_FORCEFI)
		;	EndIf
		;EndIf
		
		If Not ChannelPlaying(CHN_PWRUP) Then
			CHN_PWRUP=EmitSFX(SFX3_WHAUE,P\Entity)
		EndIf
				
		If draw=3 Then
			EntityAlpha P\Entity,0
		Else
			EntityAlpha P\Entity,.9
		EndIf
		If EntityZ(P\Entity)<-120 Then PositionEntity P\Entity,EntityX(P\Entity),EntityY(P\Entity),120
		If draw=1 Or draw=3 Then
			Select P\UpType#:
				Case 1:
					DrawImage GFX_Game_PowerUp,((EntityX(P\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(P\Entity)+120)/240)*GraphicsHeight()
				Case 3:
					DrawImage GFX_Game_TempInv,((EntityX(P\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(P\Entity)+120)/240)*GraphicsHeight()
				Case 4:
					DrawImage GFX_Game_Revive,((EntityX(P\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(P\Entity)+120)/240)*GraphicsHeight()
				Default:
					DrawImage GFX_Game_Health,((EntityX(P\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(P\Entity)+120)/240)*GraphicsHeight()
			End Select
		EndIf
		If EntityDistance(P\Entity, MSH_Player)<10 Then
			score=score+1
			Select P\UpType#:
				Case 1:
					LazerType#=LazerType#+1
					ColorType#=LazerType# Mod 5
					LazerType2#=LazerType2#+1
					ColorType2#=LazerType2# Mod 5
				Case 3:
					TempInv#=TempInv#+255
					InvType#=1
				Case 4:
					If Fliped=False Then
						RevivePlayer2=True
					Else
						RevivePlayer1=True
					EndIf
				Default:
					Lives#=Lives#+1
			End Select
			StopChannel CHN_PWRUP
			CHN_SFX3=EmitSFX(SFX3_PowerUp,P\Entity)
			HideEntity P\Entity
			Delete P
		Else If EntityDistance(P\Entity, MSH_Player2)<10 And players=2 Then
			score=score+1
			Select P\UpType#:
				Case 1:
					LazerType#=LazerType#+1
					LazerType2#=LazerType2#+1
					ColorType#=LazerType# Mod 5
					ColorType2#=LazerType2# Mod 5
				Case 3:
					TempInv2#=TempInv2#+255
					InvType2#=1
				Case 4:
					RevivePlayer1=True
				Default:
					Lives2#=Lives2#+1
			End Select
			StopChannel CHN_PWRUP
			CHN_SFX3=EmitSFX(SFX3_PowerUp,P\Entity)
			HideEntity P\Entity
			Delete P
		EndIf
	Next
End Function				