
; Vanessa's Music System Include File version 2
; Adapted for use with the Mercury Engine

; Changelog: Added playlist system

; Globals that might need adjusted for your program:
; Global BGMVolume# = #(0-1)	<Sets the maximum volume for a track
; Global BGMFalloff# = #>0 #<1	<Sets the transition time between tracks, the closer to zero it is, the slower the transition will be

; IMPORTANT! Be sure to remove the LOG related sections if new program doesn't have them.

Global BGMOld
Global BGMNew
Global BGMOldVol#
Global BGMNewVol#
Global BGMPlaying$
Global BGMPrevious$
Global BGMVolume#=1
Global SFXVolume#=1
Global BGMFalloff#=.01
Global TotalTracks#
Global ShowPlaying
Global NowPlaying$

Type Track
	Field Name$,Title$
End Type

Function CueMusicTrack(TrackName$)
	If FileType(TrackName$)<>1 Then
		DebugLog("Could not find track "+TrackName$)
	EndIf
	StopChannel BGMOld
	BGMOld=BGMNew
	BGMNew=PlayMusic(TrackName$)
	If Not ChannelPlaying(BGMNew) Then DebugLog("Music didn't cue properly.")
	BGMOldVol#=BGMNewVol#
	BGMNewVol#=0
	ChannelVolume BGMNew,0
	BGMPrevious=BGMPlaying$
	BGMPlaying$=TrackName$
	DebugLog "Cueing track "+TrackName$
End Function

Function PlaySFX(SFXFileHandle)
	SFX = PlaySound(SFXFileHandle)
	ChannelVolume SFX,SFXVolume#
	Return SFX
End Function

Function PlaySFXFile(SFXFileHandle$)
	SFX = PlayMusic(SFXFileHandle)
	ChannelVolume SFX,SFXVolume#
	Return SFX
End Function

Function EmitSFX(SFXFileHandle,Entity)
	SFX = EmitSound(SFXFileHandle,Entity)
	ChannelVolume SFX,SFXVolume#
	Return SFX
End Function	

Function StopMusic()
	StopChannel BGMOld
	StopChannel BGMNew
End Function

Function HandleMusic(Playlistmode=0)

;	If ShowPlaying>0 Then
;		Color ShowPlaying/2,0,0
;		Text 0,0,"Now playing: "+NowPlaying$
;		ShowPlaying=ShowPlaying-1
;	EndIf

	BGMNewVol#=BGMNewVol#+BGMFalloff#
	BGMOldVol#=BGMOldVol#-BGMFalloff#

	If BGMNewVol#>BGMVolume# Then BGMNewVol#=BGMVolume#
	If BGMOldVol#<0 Then
		BGMOldVol#=0
		StopChannel BGMOld
	EndIf

	If ChannelPlaying(BGMOld) Then
		ChannelVolume BGMOld,BGMOldVol#
	EndIf
	
	If Not ChannelPlaying(BGMNew) Then
		If playlistmode Then
			CueNextTrack()
		Else
			BGMNew=PlayMusic(BGMPlaying$)
		EndIf
	EndIf

	ChannelVolume BGMNew,BGMNewVol#
		
		If JoyHat()=90 And OldHat<>90 Then CueNextTrack() ; Pressed right, next track
		If JoyHat()=290 And OldHat<>270 And BGMPrevious$<>"" Then ; Pressed left, last track
			CueMusicTrack(BGMPrevious$)
			NowPlaying$="Previous track"
		EndIf
		OldHat=JoyHat()
	
	If KeyDown(157) Or KeyDown(29) Then ; Heald Control
		If KeyHit(205) Then CueNextTrack() ; Pressed right, next track
		If KeyHit(203) And BGMPrevious$<>"" Then ; Pressed left, last track
			CueMusicTrack(BGMPrevious$)
			NowPlaying$="Previous track"
		EndIf
	EndIf
End Function

Function ImportPlaylist(Dir$)
	If FileType(Dir$)<>2 Then
		DebugLog "Could not find BGM folder."
	EndIf
	Directory = ReadDir(Dir$)
	File$ = NextFile$(Directory)
	If File$="" Then DebugLog "No tracks found."
	While Not File$ = ""

			;raw/mod/s3m/xm/it/mid/rmi/wav/mp2/mp3/ogg/wma/asf 
			If Instr(Lower(File$),".raw")>0 Or Instr(Lower(File$),".mod")>0 Or Instr(Lower(File$),".s3m")>0 Or Instr(Lower(File$),".xm")>0 Or Instr(Lower(File$),".it")>0 Or Instr(Lower(File$),".mid")>0 Or Instr(Lower(File$),".rmi")>0 Or Instr(Lower(File$),".wav")>0 Or Instr(Lower(File$),".mp2")>0 Or Instr(Lower(File$),".mp3")>0 Or Instr(Lower(File$),".ogg")>0 Or Instr(Lower(File$),".wma")>0 Or Instr(Lower(File$),".asf")>0 Then
				
				T.Track = New Track
					T\Title$=Left(File$,Instr(File$,".")-1)
					T\Name$=Dir$+"/"+File$
				TotalTracks#=TotalTracks#+1					
				DebugLog "Added track "+File$+" to playlist."
			
			EndIf
		File$ = NextFile$(Directory)
	Wend
End Function

Function CueNextTrack()	
	.RetryTrackSelect
	Chosen#=Rand(1,TotalTracks#)
	
	Counter#=0
	For T.Track = Each Track
		Counter#=Counter#+1
		If Counter#=Chosen# Then
			If T\Name$<>BGMPlaying$ And T\Name$<>BGMPrevious$ Then
				CueMusicTrack(T\Name$)
				NowPlaying$=T\Title$
				ShowPlaying=510
				Return
			Else
				Goto RetryTrackSelect
			EndIf
		EndIf
	Next
End Function