; Murcury Engine
; Created by Sonic Waves TMs
; Main Include File

; Last Modified 8/24/10

; Sets Engine Version Number
Const EngineVersion#=2.75

If Version#<>EngineVersion# Then RuntimeError "Engine version mismatch: "+Version#+"?"+EngineVersion#

SeedRnd(MilliSecs())
SetBuffer(BackBuffer())
TFormFilter False

; Active Includes
;Include "..\..\Build\src\CDKeyTest.bb"

; Function Includes
Include "..\..\Build\src\TypeBank.bb"
Include "..\..\Build\src\Functions.bb"
Include "..\..\Build\src\MusicSystem.bb"
Include "..\..\Build\src\Settings.bb"
Include "..\..\Build\src\Control\All.bb"
Include "..\..\Build\src\Control\Keyboard.bb"
Include "..\..\Build\src\Control\Mouse.bb"
Include "..\..\Build\src\Control\Joy1.bb"
Include "..\..\Build\src\Control\Joy2.bb"
Include "..\..\Build\src\Menu\MainMenu.bb"
Include "..\..\Build\src\Menu\Player.bb"
Include "..\..\Build\src\Menu\Options.bb"
Include "..\..\Build\src\AISide\Adversary.bb"
Include "..\..\Build\src\AISide\Powerup.bb"
Include "..\..\Build\src\AISide\Explosives.bb"
Include "..\..\Build\src\HSTfuncs.bb"
Include "..\..\Build\src\CDKeyTest.bb"
Include "..\..\Build\src\BlitzGet.bb"
Include "..\..\Build\src\ZIPAPI\Blitz_File_ZipApi.bb"
Include "..\..\Build\src\ZIPAPI\Blitz_File_FileName.bb"
Include "..\..\Build\src\ZIPAPI\Blitz_Basic_Bank.bb"

; Globals

; Menu Graphics
Global GFX_Menu_Back
Global GFX_Menu_Pointer
Global GFX_Menu_Exit_Game
Global GFX_Menu_Options
Global GFX_Menu_Player
Global GFX_Menu_Logo

; Player Menu Graphics
Global GFX_Menu_Key
Global GFX_Menu_Mouse
Global GFX_Menu_Joy1
Global GFX_Menu_Joy2

; 2D Game Objects
Global GFX_Game_Player1
Global GFX_Game_Player2
Global GFX_Game_Adversary
Global GFX_Game_Bullet
Global GFX_Game_Health
Global GFX_Game_PowerUp
Global GFX_Game_Explosion
Global GFX_Game_GameOver
Global GFX_Game_Planet
Global GFX_Game_Asteroid
Global GFX_Game_StarBase
Global GFX_Game_TempInv
Global GFX_Game_Revive

; Sound Effects
Global SFX_Click
Global SFX_Fire
Global SFX_Boom
Global SFX_Powerup
Global SFX_Heal
Global SFX_Bang
Global SFX_FORCEFI
Global SFX_Prout
Global SFX_AHHHH
Global SFX_Nuke
Global SFX_Round

; 3D Sound Effects
Global SFX3_Click
Global SFX3_Fire
Global SFX3_Boom
Global SFX3_Powerup
Global SFX3_Heal
Global SFX3_Bang
Global SFX3_MOD
Global SFX3_MOD3
Global SFX3_CRASHBUZ
Global SFX3_QUESTION
Global SFX3_SCORPIO

; Channels
Global CHN_Click
Global CHN_SFX
Global CHN_BGM
Global CHN_ADVLOC
Global CHN_PWRUP
Global CHN_DIALOG

; Meshes
Global MSH_Player
Global MSH_Player2
Global MSH_Adversary
Global MSH_Bullet
Global MSH_Starbase
Global Camera
Global Camera2
Global Camera_1
Global Camera2_1
Global Pivot
Global Pivot2
Global Ear
Global box1
Global box2
Global world
Global world2
Global Shield
Global Shield2

; Collision Types

Const Type_Player=		1
Const Type_Adversary=	2
Const Type_Bullet=		3

; Subtitle
Global Subtitle$=""
Global ShowSubtitle#=0

; Whatnot
Global num_stars=		400
Global Wave#
Global PlayerX#
Global Player2X#
Global Direction2#
Global Lives#
Global Lives2#
Global LazerType#=		1
Global LazerType2#=		1
Global ColorType#=		1
Global ColorType2#=		1
Global score#=			0
Global Name$
Global ControlMode#=5
Global ControlMode2#=5
Global MovementX#
Global draw=0
Global Alive=True
Global hat
Global hat2
Global players#=1
Global skip=0
Global TempInv#
Global TempInv2#
Global ServerName$
Global TotalPlayers#
Global RevivePlayer1
Global RevivePlayer2
Global Player1Ctrl#
Global Player2Ctrl#
Global Fliped
Global InvType#
Global InvType2#
Global AllowNuke=False
Global AdvNumb#
Global HUDImage
