Function CreateExplosion(X#,Y#)
	E.Explosion = New Explosion
	E\X#=X#
	E\Y#=Y#
	E\Frame#=0
End Function

Function UpdateExplosions()
	For E.Explosion = Each Explosion
		DrawImage GFX_Game_Explosion,E\X#,E\Y#,E\Frame#
		E\Y#=(E\Y#+5)*(GraphicsHeight()/480)
		E\Frame#=E\Frame#+1
		If E\Frame#>8 Then Delete E
	Next
End Function				

Function Create3DExplosion(X#,Y#,Z#,Damage#,makesound=True)
	E.Explosion3D = New Explosion3D
	E\Size#=1
	E\Entity=CreateSphere()
	E\Damage#=Damage#
	BoomTex=LoadTexture("GFX\Game\Boomtex.jpg")
	EntityTexture E\Entity,BoomTex
	PositionEntity E\Entity,X#,Y#,Z#
	If makesound CHN_SFX3=EmitSFX(SFX3_Boom,E\Entity)
End Function

Function Update3DExplosions()
	For E.Explosion3D = Each Explosion3D
		ScaleEntity E\Entity,E\Size#*4,E\Size#*4,E\Size#*4
		If draw=1 Then
			EntityAlpha E\Entity, (8-E\Size#) / 20
			DrawImage GFX_Game_Explosion,((EntityX(E\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(E\Entity)+120)/240)*GraphicsHeight(),E\Size#
		Else If draw= 0 Then
			EntityAlpha E\Entity, (8-E\Size#) / 20
		Else If draw=3 Then
			EntityAlpha E\Entity, 0
			DrawImage GFX_Game_Explosion,((EntityX(E\Entity)+150)/300)*GraphicsWidth(),((-EntityZ(E\Entity)+120)/240)*GraphicsHeight(),E\Size#
		EndIf
		For A.Adversary3D=Each Adversary3D
			If EntityDistance(A\Entity,E\Entity)<E\Size#*4 And E\Size#=8 Then
				A\Health#=A\Health#-E\Damage#
			EndIf
		Next
		For As.Asteroid3D=Each Asteroid3D
			If EntityDistance(As\Entity,E\Entity)<E\Size#*4 And E\Size#=8 Then
				HideEntity As\Entity
				Delete As
			EndIf
		Next
		E\Size#=E\Size#+1
		If E\Size#>8 Then
			HideEntity E\Entity
			Delete E
		EndIf
	Next
End Function				