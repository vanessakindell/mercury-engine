Function ConfigFileScript(name$)

If FileType(name$)=1

	fhandle=ReadFile(name$)
	If fhandle<>1
		While Eof(fhandle)=0
			cmd$=ReadLine$(fhandle)
			If Left$(cmd$,1)<>";" And Len(cmd$)<>0
				parse.scriptentry=New scriptentry
				parse\sdat=cmd$
			EndIf
		Wend
		CloseFile fhandle

		parse.scriptentry=First scriptentry
		While parse<>Null
			If Left$(parse\sdat,1)="@"
			cmd$=Mid$(parse\sdat,2,Len(parse\sdat)-2)
				Select cmd$		
					Case "player_data"

					parse=After parse
					Repeat
						cpos=1

						Repeat
							sep1=Instr(parse\sdat,"=",cpos)
							del1$=Mid$(parse\sdat,cpos,sep1-cpos)
							sep2=Instr(parse\sdat,",",cpos)
							del2$=Mid$(parse\sdat,sep1+1,sep2-sep1-1)

							Select Lower(del1$)
							Case "default_map"
								leveltoselect=del2
							
							Case "player_forward"
								player_forward=del2
							
							Case "player_strafe_left"
								player_strafe_left=del2							
							
							Case "player_strafe_right"
								player_strafe_right=del2
								
							Case "player_backward"
								player_backward=del2
							
							Case "player_crouch"
								player_crouch=del2
								
							Case "player_jump"
								player_jump=del2
								
							Case "mouse_sensitivity"
								mouse_sensitivity=del2
								
							Case "player_speed"
								player_speed=del2
								player_standing_speed=del2
							
							Case "player_eyeheight"
								player_eyeheight=del2
								player_normal_eyeheight=del2
								
							Case "player_crouching_speed"
								player_crouching_speed=del2
								
							Case "player_crouching_ratio"
								player_crouching_ratio=del2
							
							Case "player_crouching_move_speed"
								player_crouching_move_speed=del2

							Case "player_jump_height"
								player_jump_height=del2

							Case "player_jump_loose_velocity"
								player_jump_loose_velocity=del2

							Case "player_max_speed"
								player_max_speed=del2
								
							Case "music_volume"
								MusicVolume#=del2
							
							Default:
								info("Config file case "+del2$+" unrecognized.")

							End Select
							cpos=sep2+1
						Until sep1=0 Or sep2=0
						parse=After parse
					Until parse\sdat=")"



				End Select
			EndIf
			parse=After parse
		Wend
	Else
		RuntimeError"Script File "+Chr$(34)+name$+Chr$(34)+" is not readable."
	EndIf

Else
	RuntimeError"Script File "+Chr$(34)+name$+Chr$(34)+" is not a file."
EndIf	

End Function

Function WeaponScript(name$)

If FileType(name$)=1

	fhandle=ReadFile(name$)
	If fhandle<>1
		While Eof(fhandle)=0
			cmd$=ReadLine$(fhandle)
			If Left$(cmd$,1)<>";" And Len(cmd$)<>0
				parse.scriptentry=New scriptentry
				parse\sdat=cmd$
			EndIf
		Wend
		CloseFile fhandle

		parse.scriptentry=First scriptentry
		While parse<>Null
			If Left$(parse\sdat,1)="@"
			cmd$=Mid$(parse\sdat,2,Len(parse\sdat)-2)
				Select cmd$		
					Case "weapons"

					parse=After parse
					Repeat
						w.gun=New gun
						cpos=1

						Repeat
							sep1=Instr(parse\sdat,"=",cpos)
							del1$=Mid$(parse\sdat,cpos,sep1-cpos)
							sep2=Instr(parse\sdat,",",cpos)
							del2$=Mid$(parse\sdat,sep1+1,sep2-sep1-1)

							Select Lower(del1$) 
							Case "weapon"
								w\class=del2
							
							Case "name"
								w\gun$=del2$

							Case "auto"
								w\auto=del2

							Case "reaction"
								w\reaction=del2

							Case "speed"
								w\speed=del2

							Case "delay"
								w\delaytime=del2
							
							Case "projectiletype"
								w\projectiletype=del2
							
							Case "scale"
								w\scale=del2
							
							Case "explosive"
								w\explosive=del2
								
							Case "ammo"
								w\maxammo=del2
							
							Case "spawnholding"
								w\selected=del2

							Case "replenish"
								w\replenish=del2
							
							Case "replenishtime"
								w\replenishtime=del2
							
							Case "alt_projectiletype"
								w\alt_projectiletype=del2
							
							Case "alt_reaction"
								w\alt_reaction=del2
								
							Case "alt_auto"
								w\alt_auto=del2
							
							Case "alt_delay"
								w\alt_delay=del2
							
							Case "alt_scale"
								w\alt_scale=del2
							
							Case "alt_speed"
								w\alt_speed=del2
							
							Case "alt_explosive"
								w\alt_explosive=del2
								
							Default
								info("Weapon file case "+del2$+" unrecognized.")

							End Select
							cpos=sep2+1
						Until sep1=0 Or sep2=0
						
						w\ammo=w\maxammo
						w\model=LoadMD2("resources\weapons\"+w\gun$+"\"+w\gun$+".md2",playerbox)
						guntex=LoadTexture("resources\weapons\"+w\gun$+"\"+w\gun$+".pcx")
						EntityTexture w\model,guntex
						ScaleEntity w\model,.1,.1,.1
						If viewmode=1 Then
							EntityParent w\model,0
							RotateEntity w\model,EntityPitch(camera),EntityYaw(camera),EntityRoll(camera)
							EntityParent w\model,camera
							PositionEntity w\model,3,-9,0
						Else
							EntityParent w\model,0
							RotateEntity w\model,EntityPitch(playerbox),EntityYaw(playerbox),EntityRoll(playerbox)
							EntityParent w\model,playerbox
							PositionEntity w\model, 0,.4,-1
						EndIf
						
						;w\muzzle=LoadMesh("resources\weapons"+w\gun$+"muzzle.3ds",w\model)
						;EntityAlpha w\muzzle, 0
						;RotateEntity w\model,0,180,0
						;PositionEntity w\muzzle,3,-2,4
						
						;w\alt_sprite
						;w\alt_ammo
						;alt_decal
						
						;ScaleEntity w\model, 0.01, 0.01, 0.01
						w\crosshair = LoadSprite("resources\weapons\"+w\gun$+"\crosshair.bmp", 4, camera)
						PositionEntity w\crosshair, 0,-5, 100
						ScaleSprite w\crosshair,10,10
						EntityOrder w\crosshair, -1
						
						w\decal = LoadSprite("resources\weapons\"+w\gun$+"\decal1.bmp",4)
						ScaleSprite w\decal,10,10
						EntityAlpha w\decal, 0
						SpriteViewMode w\decal, 2
						
						If FileType("resources\weapons\"+w\gun$+"\bullet.x")=1 Then
							w\bullet = LoadMesh("resources\weapons\"+w\gun$+"\bullet.x")
							ScaleEntity w\bullet,w\scale,w\scale,w\scale
							EntityColor w\bullet,50,50,50
							HideEntity w\bullet
						EndIf
						
						If FileType("resources\weapons\"+w\gun$+"\bullet.bmp")=1 Then
							w\sprite = LoadSprite("resources\weapons\"+w\gun$+"\bullet.bmp",4)
							HideEntity w\sprite
						EndIf
						
						If FileType("resources\weapons\"+w\gun$+"\alt_bullet.x")=1 Then
							w\alt_bullet = LoadMesh("resources\weapons\"+w\gun$+"\alt_bullet.x")
							ScaleEntity w\alt_bullet,w\scale,w\scale,w\scale
							EntityColor w\alt_bullet,50,50,50
							HideEntity w\alt_bullet
						EndIf
						
						If FileType("resources\weapons\"+w\gun$+"\alt_bullet.bmp")=1 Then
							w\alt_sprite = LoadSprite("resources\weapons\"+w\gun$+"\alt_bullet.bmp",4)
							HideEntity w\alt_sprite
						EndIf
						
						w\sound = Load3DSound("resources\weapons\"+w\gun$+"\fire.wav")
						w\alt_sound = Load3DSound("resources\weapons\"+w\gun$+"\alt_fire.wav")
						w\collide = Load3DSound("resources\weapons\"+w\gun$+"\collide.wav")

						;ScaleEntity w\model, 0.01, 0.01, 0.01
						parse=After parse
					Until parse\sdat=")"



				End Select
			EndIf
			parse=After parse
		Wend
	Else
		RuntimeError"Script File "+Chr$(34)+name$+Chr$(34)+" is not readable."
	EndIf

Else
	RuntimeError"Script File "+Chr$(34)+name$+Chr$(34)+" is not a file."
EndIf	

End Function

Function MapListScript(name$)

If FileType(name$)=1

	fhandle=ReadFile(name$)
	If fhandle<>1
		While Eof(fhandle)=0
			cmd$=ReadLine$(fhandle)
			If Left$(cmd$,1)<>";" And Len(cmd$)<>0
				parse.scriptentry=New scriptentry
				parse\sdat=cmd$
			EndIf
		Wend
		CloseFile fhandle

		parse.scriptentry=First scriptentry
		While parse<>Null
			If Left$(parse\sdat,1)="@"
			cmd$=Mid$(parse\sdat,2,Len(parse\sdat)-2)
				Select cmd$		
					Case "maps"

					parse=After parse
					Repeat
						w.maps=New maps
						cpos=1

						Repeat
							sep1=Instr(parse\sdat,"=",cpos)
							del1$=Mid$(parse\sdat,cpos,sep1-cpos)
							sep2=Instr(parse\sdat,",",cpos)
							del2$=Mid$(parse\sdat,sep1+1,sep2-sep1-1)

							Select Lower(del1$) 
							Case "map"
								w\number=del2
							
							Case "filename"
								w\filename$=del2$

							Case "displayname"
								w\displayname=del2

							Case "fogred"
								w\fogred=del2

							Case "foggreen"
								w\foggreen=del2

							Case "fogblue"
								w\fogblue=del2
							
							Case "fogclose"
								w\fogclose=del2
							
							Case "fogfar"
								w\fogfar=del2
							
							Case "maptype"
								w\maptype=del2
								
							Case "scale"
								w\scale=del2
								
							Case "offset"
								w\offset=del2
							
							Default
								info("Map list case "+del2$+" unrecognized.")

							End Select
							cpos=sep2+1
						Until sep1=0 Or sep2=0					
						
						parse=After parse
					Until parse\sdat=")"



				End Select
			EndIf
			parse=After parse
		Wend
	Else
		RuntimeError"Script File "+Chr$(34)+name$+Chr$(34)+" is not readable."
	EndIf

Else
	RuntimeError"Script File "+Chr$(34)+name$+Chr$(34)+" is not a file."
EndIf	

End Function


Function dumpentrys()
	For s.scriptentry = Each scriptentry
		Delete s
	Next
End Function

Function BuildConfigFile()
	cfgfile=WriteFile("config.spt")
	WriteLine(cfgfile,"@player_data(")
	WriteLine(cfgfile,"default_map="+leveltoselect+",player_forward="+player_forward+",player_strafe_left="+player_strafe_left+",player_strafe_right="+player_strafe_right+",player_backward="+player_backward+",player_crouch="+player_crouch+",player_jump="+player_jump+",mouse_sensitivity="+mouse_sensitivity+",player_speed="+player_speed+",player_eyeheight="+player_eyeheight+",player_crouching_speed="+player_crouching_speed+",player_crouching_ratio="+player_crouching_ratio+",player_crouching_move_speed="+player_crouching_move_speed+",player_jump_height="+player_jump_height+",player_jump_loose_velocity="+player_jump_loose_velocity+",player_max_speed="+player_max_speed+",music_volume="+Int(MusicVolume))
	WriteLine(cfgfile,")")
	CloseFile cfgfile	
End Function