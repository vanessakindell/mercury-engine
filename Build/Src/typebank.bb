Type CreditSlide
	Field Image
End Type

Type Bullet2D
	Field X#,Y#,Playersrc#,LazerType#
End Type

Type Adversary2D
	Field X#,Y#,Speed#,XSpeed#,Health#
End Type

Type Bullet3D
	Field Roll#,Entity,RollSpeed#,Playersrc#,LazerType#,FromEnemy
End Type

Type Adversary3D
	Field X#,Y#,Z#,Speed#,XSpeed#,Health#,Entity,Timing,StartHealth#,CHN_WRN,Offset#,Intelligence,Hostility
End Type

Type Star
	Field X#,Y#,Speed#
End Type

Type Star3D
	Field X#,Y#,Z#,Speed#
End Type

Type PowerUp
	Field X#,Y#,UpType#
End Type

Type PowerUp3D
	Field Entity,UpType#
End Type

Type Explosion
	Field X#,Y#,Frame#
End Type

Type Explosion3D
	Field Size#,Entity,Damage#
End Type

Type Asteroid3D
	Field Entity,Speed#,RotP#,RotY#,RotR#,Timing
End Type

Type Starbase3D
	Field Entity,Health#
End Type

Type Sun
	Field Entity
End Type

Type Dialog
	Field File$,Subtitle$
End Type

Type LoadText
	Field LoadText$
End Type