; -----------------------------------------------------------------------------
; File download function
; -----------------------------------------------------------------------------
; webFile$  -- file to download
; saveDir$  -- directory to download into
; saveFile$ -- filename to save as (use "" to use name of downloaded file automatically)
; -----------------------------------------------------------------------------
; Note that if you just provide a web server address, the document downloaded will
; be named "Unknown file.txt"
; -----------------------------------------------------------------------------

Global header$
Global bytesToRead
Global date$
Global server$
Global contentType$
Global initialReply$

Function BlitzGet (webFile$, saveDir$, saveFile$)

	SetBuffer(FrontBuffer())
	
	.requestfile ; NEW/MODIFIED 2009 (re-direct support)
	
	; RE-OPEN STREAM!
	
	DebugLog webFile$
	
	; -------------------------------------------------------------------------
	; Strip "http://" if provided
	; -------------------------------------------------------------------------
	If Left (webFile$, 7) = "http://" Then webFile$ = Right (webFile$, Len (webFile$) - 7)

	; -------------------------------------------------------------------------
	; Split into hostname and path/filename to download
	; -------------------------------------------------------------------------
	slash = Instr (webFile$, "/")
	If slash
		webHost$ = Left (webFile$, slash - 1)
		webFile$ = Right (webFile$, Len (webFile$) - slash + 1)
	Else
		webHost$ = webFile$
		webFile$ = "/"
	EndIf
	
	DebugLog webHost$
	DebugLog webFile$
	
	If Not www ; NEW/MODIFIED 2009 (re-direct support)

		; -------------------------------------------------------------------------
		; Add trailing slash to download dir if not given
		; -------------------------------------------------------------------------
		If Right (saveDir$, 1) <> "\" Then saveDir$ = saveDir$ + "\"
	
		; -------------------------------------------------------------------------
		; Save filename -- get from webFile$ if not provided
		; -------------------------------------------------------------------------
		If saveFile$ = ""
			If webFile = "/"
				saveFile$ = "Unknown file.txt"
			Else
				For findSlash = Len (webFile$) To 1 Step - 1
					testForSlash$ = Mid (webFile$, findSlash, 1)
					If testForSlash$ = "/"
						saveFile$ = Right (webFile$, Len (webFile$) - findSlash)
						Exit
					EndIf
				Next
				If saveFile$ = "" Then saveFile$ = "Unknown file.txt"
			EndIf
		EndIf

	EndIf
	
	; DEBUG
	; RuntimeError "Web host: " + webHost$ + Chr (10) + "Web file: " + webFile$ + Chr (10) + "Save dir: " + saveDir$ + Chr (10) + "Save file: " + saveFile$

	www = OpenTCPStream (webHost$, 80)

	If www
	
		WriteLine www, "GET " + webFile$ + " HTTP/1.1" ; GET / gets default page...
		WriteLine www, "Host: " + webHost$
		WriteLine www, "User-Agent: BlitzGet Deluxe"
		WriteLine www, "Accept: */*"
		WriteLine www, ""
		
		; ---------------------------------------------------------------------
		; NEW/MODIFIED 2009 (re-direct support)
		; ---------------------------------------------------------------------
		; Get HTTP response (200 = OK, 404 = not found, etc)...
		; ---------------------------------------------------------------------

		response$ = ReadLine (www)

		DebugLog "Server response: " + response$
		
		If Left (response$, 5) = "HTTP/"
			pos = Instr (response$, " ")
			replycode$ = Mid (response$, pos + 1, 3)
		EndIf

		; ---------------------------------------------------------------------
		; Find blank line after header data, where the action begins...
		; ---------------------------------------------------------------------
		
		; Store various information along the way...
		
		Repeat
	
			Cls
			
			header$ = ReadLine (www)
	
			reply$ = ""
			pos = Instr (header$, ": ")
			If pos
				reply$ = Left (header$, pos + 1)
			EndIf
	
			Select Lower (reply$)
				Case "content-length: "
					bytesToRead = ReplyContent (header$, reply$)
				Case "date: "
					date$ = ReplyContent (header$, reply$)
				Case "server: "
					server$ = ReplyContent (header$, reply$)
				Case "content-type: "
					contentType$ = ReplyContent (header$, reply$)
				Case "location: " ; NEW/MODIFIED 2009 (re-direct support)
					location$ = ReplyContent (header$, reply$) ; NEW/MODIFIED 2009 (re-direct support)
				Default
					If gotReply = 0 Then initialReply$ = header$: gotReply = 1
			End Select
	
			DisplayResponse ()
	
			Flip
			
		Until header$ = "" Or (Eof (www))

		; ---------------------------------------------------------------------
		; NEW/MODIFIED 2009 (re-direct support)
		; ---------------------------------------------------------------------
		
		downloadfile = 0

		Select replycode$

			Case "200" ; File found...

				downloadfile = True
				
			Case "404" ; File not found...
			
				Goto skipDownLoad
			
			Case "301" ; File permanently moved...
			
				webFile$ = location$
				
				CloseTCPStream www
				www = 0 ; NEW/MODIFIED 2009 (re-direct support)

				Goto requestfile
	
			Case "302" ; File temporarily moved...
	
				webFile$ = location$

				CloseTCPStream www
				www = 0 ; NEW/MODIFIED 2009 (re-direct support)

				Goto requestfile
			
			Case "303" ; File moved...

				webFile$ = location$

				CloseTCPStream www
				www = 0 ; NEW/MODIFIED 2009 (re-direct support)

				Goto requestfile

			Case "307" ; Naughty...

				webFile$ = location$

				CloseTCPStream www
				www = 0 ; NEW/MODIFIED 2009 (re-direct support)

				Goto requestfile
				
		End Select

		; ---------------------------------------------------------------------

		If Not downloadfile Then Goto skipDownLoad ; NEW/MODIFIED 2009 (re-direct support)
		
		If bytesToRead = 0 Then Goto skipDownLoad
		
		; ---------------------------------------------------------------------
		; Create new file to write downloaded bytes into
		; ---------------------------------------------------------------------
		save = WriteFile (saveDir$ + saveFile$)
		If Not save Then Goto skipDownload

		; ---------------------------------------------------------------------
		; Incredibly complex download-to-file routine...
		; ---------------------------------------------------------------------

		For readWebFile = 1 To bytesToRead
		
			If Not Eof (www) Then WriteByte save, ReadByte (www)
			
			; Call BytesReceived with position and size every 100 bytes (slows down a LOT with smaller updates)
			
			tReadWebFile = readWebFile
			If tReadWebFile Mod 100 = 0 Then BytesReceived (readWebFile, bytesToRead)

		Next

		CloseFile save
		
		; Fully downloaded?
		If (readWebFile - 1) = bytesToRead
			success = 1
		EndIf
		
		; Final update (so it's not rounded to nearest 100 bytes!)
		BytesReceived (bytesToRead, bytesToRead)
		
		.skipDownload ; NEW/MODIFIED 2009 (re-direct support)
		CloseTCPStream www
		www = 0 ; NEW/MODIFIED 2009 (re-direct support)
		
	Else
	
		Return False	
		
	EndIf
	
	Return success
	
End Function

; -----------------------------------------------------------------------------
; User-defined update function, called every 100 bytes of download -- alter to suit!
; -----------------------------------------------------------------------------
; TIP: Pass a user-defined type instead, with all data (this stuff plus URL, local filename, etc)
; -----------------------------------------------------------------------------
Function BytesReceived (posByte, totalBytes)
	; Example update code...
	Cls
	Text 0, 10, "Downloading file -- please wait..."
	Text 0, 30, "Received: " + posByte + "/" + totalBytes + " bytes (" + Percent (posByte, totalBytes) + "%)"
	DisplayResponse ()
	Flip
End Function

; -----------------------------------------------------------------------------
; Handy percentage function
; -----------------------------------------------------------------------------
Function Percent (part#, total#)
	Return Int (100 * (part / total))
End Function

Function ReplyContent$ (header$, reply$)
	Return Right (header$, Len (header$) - Len (reply$))
End Function

; Temporary, for quick results...

Function DisplayResponse ()
;	Text 0, 80, "Header: " + initialReply$
;	Text 0, 100, "Date: " + date$
;	Text 0, 120, "Server: " + server$
;	Text 0, 140, "Content-Type: " + contentType$
;	Text 0, 160, "Content-Length: " + bytesToRead
End Function