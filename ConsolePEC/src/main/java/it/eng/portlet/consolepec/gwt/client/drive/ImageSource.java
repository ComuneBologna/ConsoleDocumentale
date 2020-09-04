package it.eng.portlet.consolepec.gwt.client.drive;

import com.google.gwt.user.client.ui.Image;

/**
 * @author Giacomo F.M.
 * @since 2019-06-06
 */
public class ImageSource {

	public static final String FLIPPED_STYLE = "transform: scaleX(-1);";

	private static final String IMG_SRC_FOLDER = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiNGRkEwMDA7IiBkPSJNIDQwIDEyIEwgMjIgMTIgTCAxOCA4IEwgOCA4IEMgNS44MDA3ODEgOCA0IDkuODAwNzgxIDQgMTIgTCA0IDIwIEwgNDQgMjAgTCA0NCAxNiBDIDQ0IDEzLjgwMDc4MSA0Mi4xOTkyMTkgMTIgNDAgMTIgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6I0ZGQ0EyODsiIGQ9Ik0gNDAgMTIgTCA4IDEyIEMgNS44MDA3ODEgMTIgNCAxMy44MDA3ODEgNCAxNiBMIDQgMzYgQyA0IDM4LjE5OTIxOSA1LjgwMDc4MSA0MCA4IDQwIEwgNDAgNDAgQyA0Mi4xOTkyMTkgNDAgNDQgMzguMTk5MjE5IDQ0IDM2IEwgNDQgMTYgQyA0NCAxMy44MDA3ODEgNDIuMTk5MjE5IDEyIDQwIDEyIFogIj48L3BhdGg+PC9nPjwvc3ZnPg==";
	private static final String IMG_SRC_OPEN_FOLDER = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiNGRkEwMDA7IiBkPSJNIDM4IDEyIEwgMjIgMTIgTCAxOCA4IEwgOCA4IEMgNS44MDA3ODEgOCA0IDkuODAwNzgxIDQgMTIgTCA0IDM2IEMgNCAzOC4xOTkyMTkgNS44MDA3ODEgNDAgOCA0MCBMIDM5IDQwIEMgNDAuNjk5MjE5IDQwIDQyIDM4LjY5OTIxOSA0MiAzNyBMIDQyIDE2IEMgNDIgMTMuODAwNzgxIDQwLjE5OTIxOSAxMiAzOCAxMiBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojRkZDQTI4OyIgZD0iTSA0Mi4xOTkyMTkgMTggTCAxNS4zMDA3ODEgMTggQyAxMy4zOTg0MzggMTggMTEuNjk5MjE5IDE5LjM5ODQzOCAxMS4zOTg0MzggMjEuMzAwNzgxIEwgOCA0MCBMIDM5LjY5OTIxOSA0MCBDIDQxLjYwMTU2MyA0MCA0My4zMDA3ODEgMzguNjAxNTYzIDQzLjYwMTU2MyAzNi42OTkyMTkgTCA0Ni4xMDE1NjMgMjIuNjk5MjE5IEMgNDYuNjAxNTYzIDIwLjMwMDc4MSA0NC42OTkyMTkgMTggNDIuMTk5MjE5IDE4IFogIj48L3BhdGg+PC9nPjwvc3ZnPg==";
	private static final String IMG_SRC_GENERIC = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiM5MENBRjk7IiBkPSJNIDQwIDQ1IEwgOCA0NSBMIDggMyBMIDMwIDMgTCA0MCAxMyBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojRTFGNUZFOyIgZD0iTSAzOC41IDE0IEwgMjkgMTQgTCAyOSA0LjUgWiAiPjwvcGF0aD48L2c+PC9zdmc+";
	private static final String IMG_SRC_ZIP = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiNDRkQ4REM7IiBkPSJNIDE3IDI0IEwgNyAxNy40Njg3NSBDIDcgMTcuNDY4NzUgOCAxNS42MDE1NjMgOCAxMy43MzQzNzUgQyA4IDExLjg2NzE4OCA3IDEwIDcgMTAgTCAxNyAxNi41MzEyNSBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojQ0ZEOERDOyIgZD0iTSAxNyA0MCBMIDcgMzIuNTMxMjUgQyA3IDMyLjUzMTI1IDggMzEuNTk3NjU2IDggMjkuNzM0Mzc1IEMgOCAyNy44NjcxODggNyAyNiA3IDI2IEwgMTcgMzIuNTMxMjUgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6I0NGRDhEQzsiIGQ9Ik0gMTcgMzIgTCA3IDI0LjUzMTI1IEMgNyAyNC41MzEyNSA4IDIzLjU5NzY1NiA4IDIxLjczNDM3NSBDIDggMTkuODY3MTg4IDcgMTggNyAxOCBMIDE3IDI0LjUzMTI1IFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiMzRjUxQjU7IiBkPSJNIDE3LjYyNSAzNCBDIDE3LjQwNjI1IDM0IDE3LjE4MzU5NCAzMy45Mjk2ODggMTcgMzMuNzgxMjUgTCA2LjM3NSAyNS43ODEyNSBDIDUuOTQxNDA2IDI1LjQzMzU5NCA1Ljg3NSAyNC44MDQ2ODggNi4yMTg3NSAyNC4zNzUgQyA2LjU2MjUgMjMuOTQ1MzEzIDcuMTk1MzEzIDIzLjg3NSA3LjYyNSAyNC4yMTg3NSBMIDE4LjI1IDMyLjIxODc1IEMgMTguNjc5Njg4IDMyLjU2NjQwNiAxOC43NSAzMy4xOTUzMTMgMTguNDA2MjUgMzMuNjI1IEMgMTguMjA3MDMxIDMzLjg3MTA5NCAxNy45MTc5NjkgMzQgMTcuNjI1IDM0IFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiMzRjUxQjU7IiBkPSJNIDQyIDI1IEwgMTYgMjUgQyAxNiAyNSAxNyAyNi43NDIxODggMTcgMjkuMjQ2MDk0IEMgMTcgMzEuNzUgMTYgMzMgMTYgMzMgTCA0MiAzMyBDIDQyIDMzIDQzIDMyIDQzIDI5IEMgNDMgMjYgNDIgMjUgNDIgMjUgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzlDMjdCMDsiIGQ9Ik0gMTcuNjI1IDI2IEMgMTcuNDA2MjUgMjYgMTcuMTgzNTk0IDI1LjkyOTY4OCAxNyAyNS43ODEyNSBMIDYuMzc1IDE3Ljc4MTI1IEMgNS45NDE0MDYgMTcuNDMzNTk0IDUuODc1IDE2LjgwNDY4OCA2LjIxODc1IDE2LjM3NSBDIDYuNTYyNSAxNS45NDUzMTMgNy4xOTUzMTMgMTUuODcxMDk0IDcuNjI1IDE2LjIxODc1IEwgMTguMjUgMjQuMjE4NzUgQyAxOC42Nzk2ODggMjQuNTY2NDA2IDE4Ljc1IDI1LjE5NTMxMyAxOC40MDYyNSAyNS42MjUgQyAxOC4yMDcwMzEgMjUuODcxMDk0IDE3LjkxNzk2OSAyNiAxNy42MjUgMjYgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzlDMjdCMDsiIGQ9Ik0gNDIgMTcgTCAxNiAxNyBDIDE2IDE3IDE3IDE4Ljc0MjE4OCAxNyAyMS4yNDYwOTQgQyAxNyAyMy43NSAxNiAyNSAxNiAyNSBMIDQyIDI1IEMgNDIgMjUgNDMgMjQgNDMgMjEgQyA0MyAxOCA0MiAxNyA0MiAxNyBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojOEJDMzRBOyIgZD0iTSAxOC42MDkzNzUgNDEgQyAxOC42MDkzNzUgNDAuNzA3MDMxIDE4LjQ5NjA5NCA0MC40MTQwNjMgMTguMjUgNDAuMjE4NzUgTCA3LjYyNSAzMi4yMTg3NSBDIDcuMTk1MzEzIDMxLjg3NSA2LjU2MjUgMzEuOTQ1MzEzIDYuMjE4NzUgMzIuMzc1IEMgNS44NzUgMzIuODA4NTk0IDUuOTQ1MzEzIDMzLjQzNzUgNi4zNzUgMzMuNzgxMjUgTCAxNS45NjA5MzggNDEgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzhCQzM0QTsiIGQ9Ik0gNDIgMzMgTCAxNiAzMyBDIDE2IDMzIDE3IDM0Ljc0MjE4OCAxNyAzNy4yNDYwOTQgQyAxNyAzOS43NSAxNiA0MSAxNiA0MSBMIDQyIDQxIEMgNDIgNDEgNDMgNDAgNDMgMzcgQyA0MyAzNCA0MiAzMyA0MiAzMyBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojNjg5RjM4OyIgZD0iTSA0MiAzMyBMIDE2IDMzIEMgMTYgMzMgMTYuNDQxNDA2IDMzLjc1NzgxMyAxNi43MzQzNzUgMzUgTCA0Mi43Njk1MzEgMzUgQyA0Mi40NzI2NTYgMzMuNTU4NTk0IDQyIDMzIDQyIDMzIFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiMzMDNGOUY7IiBkPSJNIDQyIDI1IEwgMTYgMjUgQyAxNiAyNSAxNi40NDE0MDYgMjUuNzU3ODEzIDE2LjczNDM3NSAyNyBMIDQyLjc2OTUzMSAyNyBDIDQyLjQ3MjY1NiAyNS41NTg1OTQgNDIgMjUgNDIgMjUgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6I0ZERDgzNTsiIGQ9Ik0gMjEuMDM1MTU2IDMyIEMgMjEuMTQ0NTMxIDMxLjU1MDc4MSAyMS4zMzIwMzEgMzAuNjIxMDk0IDIxLjMzMjAzMSAyOS41IEMgMjEuMzMyMDMxIDI4LjM3ODkwNiAyMS4xNDQ1MzEgMjcuNDQ5MjE5IDIxLjAzNTE1NiAyNyBMIDE5IDI3IEMgMTkuMDAzOTA2IDI3LjAwNzgxMyAxOS4zMzIwMzEgMjguMjY1NjI1IDE5LjMzMjAzMSAyOS41IEMgMTkuMzMyMDMxIDMwLjczNDM3NSAxOS4wMDM5MDYgMzEuOTkyMTg4IDE5IDMyIFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiNGREQ4MzU7IiBkPSJNIDIxLjAzNTE1NiAyNCBDIDIxLjE0NDUzMSAyMy41NTA3ODEgMjEuMzMyMDMxIDIyLjYyMTA5NCAyMS4zMzIwMzEgMjEuNSBDIDIxLjMzMjAzMSAyMC4zNzg5MDYgMjEuMTQ0NTMxIDE5LjQ0OTIxOSAyMS4wMzUxNTYgMTkgTCAxOSAxOSBDIDE5LjAwMzkwNiAxOS4wMDc4MTMgMTkuMzMyMDMxIDIwLjI2NTYyNSAxOS4zMzIwMzEgMjEuNSBDIDE5LjMzMjAzMSAyMi43MzQzNzUgMTkuMDAzOTA2IDIzLjk5MjE4OCAxOSAyNCBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojRkREODM1OyIgZD0iTSAyMS4wMzUxNTYgNDAgQyAyMS4xNDQ1MzEgMzkuNTUwNzgxIDIxLjMzMjAzMSAzOC42MjEwOTQgMjEuMzMyMDMxIDM3LjUgQyAyMS4zMzIwMzEgMzYuMzc4OTA2IDIxLjE0NDUzMSAzNS40NDkyMTkgMjEuMDM1MTU2IDM1IEwgMTkgMzUgQyAxOS4wMDM5MDYgMzUuMDA3ODEzIDE5LjMzMjAzMSAzNi4yNjU2MjUgMTkuMzMyMDMxIDM3LjUgQyAxOS4zMzIwMzEgMzguNzM0Mzc1IDE5LjAwMzkwNiAzOS45OTIxODggMTkgNDAgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzdCMUZBMjsiIGQ9Ik0gNDIuNzY5NTMxIDE5IEMgNDIuNDcyNjU2IDE3LjU1ODU5NCA0MiAxNyA0MiAxNyBMIDMwLjg0Mzc1IDguNTg5ODQ0IEMgMzAuODQzNzUgOC41ODk4NDQgMzAuMDYyNSA4IDI5IDggQyAyNy43Njk1MzEgOCA3IDggNyA4IEwgNy4wMTk1MzEgOC4wMTk1MzEgQyA2LjcwMzEyNSA4LjAxMTcxOSA2LjM5MDYyNSA4LjEzNjcxOSA2LjE5MTQwNiA4LjQxMDE1NiBDIDUuODY3MTg4IDguODU5Mzc1IDUuOTY0ODQ0IDkuNDg0Mzc1IDYuNDEwMTU2IDkuODA4NTk0IEwgMTYuMTQ4NDM4IDE3LjI3NzM0NCBDIDE2LjMwODU5NCAxNy42MDE1NjMgMTYuNTY2NDA2IDE4LjE5OTIxOSAxNi43NTM5MDYgMTkgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6I0FGNzAwMDsiIGQ9Ik0gMzIgMTYgTCAyMSA4IEwgMTYgOCBMIDI3IDE2IEMgMjguNzUgMTcuMjUgMjkgMTcuNjI1IDI5IDE5IEMgMjkgMjAuMzc1IDI5IDM4IDI5IDM4IEMgMjkgMzggMjkgNDEgMjcgNDEgQyAyOC4wNTQ2ODggNDEgMzAuNjc5Njg4IDQxIDMyIDQxIEMgMzQgNDEgMzQgMzggMzQgMzggQyAzNCAzOCAzNCAyMCAzNCAxOSBDIDM0IDE3IDMyLjQzNzUgMTYuNDM3NSAzMiAxNiBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojRkZDMTA3OyIgZD0iTSAzNCAyNyBMIDM0IDMxIEwgMjkgMzEgTCAyOSAyNyBMIDM0IDI3IE0gMzQuMjUgMjYgTCAyOC43NSAyNiBDIDI4LjMzNTkzOCAyNiAyOCAyNi4zMzU5MzggMjggMjYuNzUgTCAyOCAzMS4yNSBDIDI4IDMxLjY2NDA2MyAyOC4zMzU5MzggMzIgMjguNzUgMzIgTCAzNC4yNSAzMiBDIDM0LjY2NDA2MyAzMiAzNSAzMS42NjQwNjMgMzUgMzEuMjUgTCAzNSAyNi43NSBDIDM1IDI2LjMzNTkzOCAzNC42NjQwNjMgMjYgMzQuMjUgMjYgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzVCM0IwNzsiIGQ9Ik0gMzIgMjkgQyAzMiAyOS4yNzczNDQgMzEuNzc3MzQ0IDI5LjUgMzEuNSAyOS41IEMgMzEuMjIyNjU2IDI5LjUgMzEgMjkuMjc3MzQ0IDMxIDI5IEMgMzEgMjguNzIyNjU2IDMxLjIyMjY1NiAyOC41IDMxLjUgMjguNSBDIDMxLjc3NzM0NCAyOC41IDMyIDI4LjcyMjY1NiAzMiAyOSBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojRkZFQjNCOyIgZD0iTSAzMS41IDI5IEMgMzEuMjIyNjU2IDI5IDMxIDI4Ljc3NzM0NCAzMSAyOC41IEwgMzEgMjUuNSBDIDMxIDI1LjIyMjY1NiAzMS4yMjI2NTYgMjUgMzEuNSAyNSBDIDMxLjc3NzM0NCAyNSAzMiAyNS4yMjI2NTYgMzIgMjUuNSBMIDMyIDI4LjUgQyAzMiAyOC43NzczNDQgMzEuNzc3MzQ0IDI5IDMxLjUgMjkgWiAiPjwvcGF0aD48L2c+PC9zdmc+";
	private static final String IMG_SRC_SCROLL = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCA0OCA0ODs7IGZpbGw6IzAwMDAwMDsiPjxwYXRoIHN0eWxlPSJmaWxsOiM2NEI1RjY7IiBkPSJNMzEsMTRoMTN2LTRjMC0yLjIwOS0xLjc5MS00LTQtNGgtOVYxNHoiPjwvcGF0aD48cGF0aCBzdHlsZT0iZmlsbDojQkJERUZCOyIgZD0iTTQwLDZIMTRjLTIuMjA5LDAtNCwxLjc5MS00LDR2MjhjMCwyLjIwOSwxLjc5MSw0LDQsNGgxOGMyLjIwOSwwLDQtMS43OTEsNC00VjEwICBDMzYsNy43OTEsMzcuNzkxLDYsNDAsNnoiPjwvcGF0aD48cGF0aCBzdHlsZT0iZmlsbDojNjRCNUY2OyIgZD0iTTMyLDQySDhjLTIuMjA5LDAtNC0xLjc5MS00LTR2LTNoMjR2M0MyOCw0MC4yMDksMjkuNzkxLDQyLDMyLDQyeiI+PC9wYXRoPjxnPgk8cmVjdCB4PSIxNiIgeT0iMTQiIHN0eWxlPSJmaWxsOiM2NEI1RjY7IiB3aWR0aD0iMTQiIGhlaWdodD0iMiI+PC9yZWN0Pgk8cmVjdCB4PSIxNiIgeT0iMTkiIHN0eWxlPSJmaWxsOiM2NEI1RjY7IiB3aWR0aD0iMTAiIGhlaWdodD0iMiI+PC9yZWN0Pgk8cmVjdCB4PSIxNiIgeT0iMjQiIHN0eWxlPSJmaWxsOiM2NEI1RjY7IiB3aWR0aD0iMTQiIGhlaWdodD0iMiI+PC9yZWN0Pgk8cmVjdCB4PSIxNiIgeT0iMjkiIHN0eWxlPSJmaWxsOiM2NEI1RjY7IiB3aWR0aD0iMTAiIGhlaWdodD0iMiI+PC9yZWN0PjwvZz48L3N2Zz4=";
	private static final String IMG_SRC_DOCUMENT = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiM5MENBRjk7IiBkPSJNIDQwIDQ1IEwgOCA0NSBMIDggMyBMIDMwIDMgTCA0MCAxMyBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojRTFGNUZFOyIgZD0iTSAzOC41IDE0IEwgMjkgMTQgTCAyOSA0LjUgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzE5NzZEMjsiIGQ9Ik0gMTYgMjEgTCAzMyAyMSBMIDMzIDIzIEwgMTYgMjMgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzE5NzZEMjsiIGQ9Ik0gMTYgMjUgTCAyOSAyNSBMIDI5IDI3IEwgMTYgMjcgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzE5NzZEMjsiIGQ9Ik0gMTYgMjkgTCAzMyAyOSBMIDMzIDMxIEwgMTYgMzEgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzE5NzZEMjsiIGQ9Ik0gMTYgMzMgTCAyOSAzMyBMIDI5IDM1IEwgMTYgMzUgWiAiPjwvcGF0aD48L2c+PC9zdmc+";
	private static final String IMG_SRC_IMAGE = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiM5MENBRjk7IiBkPSJNIDQwIDQ1IEwgOCA0NSBMIDggMyBMIDMwIDMgTCA0MCAxMyBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojRTFGNUZFOyIgZD0iTSAzOC41IDE0IEwgMjkgMTQgTCAyOSA0LjUgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzE1NjVDMDsiIGQ9Ik0gMjEgMjMgTCAxNCAzMyBMIDI4IDMzIFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiMxOTc2RDI7IiBkPSJNIDI4IDI2LjM5ODQzOCBMIDIzIDMzIEwgMzMgMzMgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzE5NzZEMjsiIGQ9Ik0gMzMgMjQuNSBDIDMzIDI1LjMyODEyNSAzMi4zMjgxMjUgMjYgMzEuNSAyNiBDIDMwLjY3MTg3NSAyNiAzMCAyNS4zMjgxMjUgMzAgMjQuNSBDIDMwIDIzLjY3MTg3NSAzMC42NzE4NzUgMjMgMzEuNSAyMyBDIDMyLjMyODEyNSAyMyAzMyAyMy42NzE4NzUgMzMgMjQuNSBaICI+PC9wYXRoPjwvZz48L3N2Zz4=";
	private static final String IMG_SRC_INFO = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCA0OCA0ODs7IGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMV84OV8iPgk8cGF0aCBzdHlsZT0iZmlsbDojMjE5NkYzOyIgZD0iTTQ0LDI0YzAsMTEuMDQ1LTguOTU1LDIwLTIwLDIwUzQsMzUuMDQ1LDQsMjRTMTIuOTU1LDQsMjQsNFM0NCwxMi45NTUsNDQsMjR6Ij48L3BhdGg+CTxwYXRoIHN0eWxlPSJmaWxsOiNGRkZGRkY7IiBkPSJNMjIsMjJoNHYxMWgtNFYyMnoiPjwvcGF0aD4JPHBhdGggc3R5bGU9ImZpbGw6I0ZGRkZGRjsiIGQ9Ik0yNi41LDE2LjVjMCwxLjM3OS0xLjEyMSwyLjUtMi41LDIuNXMtMi41LTEuMTIxLTIuNS0yLjVTMjIuNjIxLDE0LDI0LDE0UzI2LjUsMTUuMTIxLDI2LjUsMTYuNXoiPjwvcGF0aD48L2c+PC9zdmc+";
	private static final String IMG_SRC_OK = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCA0OCA0ODs7IGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMV80XyI+CTxwYXRoIHN0eWxlPSJmaWxsOiM0Q0FGNTA7IiBkPSJNNDQsMjRjMCwxMS4wNDUtOC45NTUsMjAtMjAsMjBTNCwzNS4wNDUsNCwyNFMxMi45NTUsNCwyNCw0UzQ0LDEyLjk1NSw0NCwyNHoiPjwvcGF0aD4JPHBhdGggc3R5bGU9ImZpbGw6I0NDRkY5MDsiIGQ9Ik0zNC42MDIsMTQuNjAyTDIxLDI4LjE5OWwtNS42MDItNS41OThsLTIuNzk3LDIuNzk3TDIxLDMzLjgwMWwxNi4zOTgtMTYuNDAyTDM0LjYwMiwxNC42MDJ6Ij48L3BhdGg+PC9nPjwvc3ZnPg==";
	private static final String IMG_SRC_CANCEL = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCA0OCA0ODs7IGZpbGw6IzAwMDAwMDsiPjxwYXRoIHN0eWxlPSJmaWxsOiNGNDQzMzY7IiBkPSJNNDQsMjRjMCwxMS4wNDUtOC45NTUsMjAtMjAsMjBTNCwzNS4wNDUsNCwyNFMxMi45NTUsNCwyNCw0UzQ0LDEyLjk1NSw0NCwyNHoiPjwvcGF0aD48cGF0aCBzdHlsZT0iZmlsbDojRkZGRkZGOyIgZD0iTTI5LjY1NiwxNS41MTZsMi44MjgsMi44MjhsLTE0LjE0LDE0LjE0bC0yLjgyOC0yLjgyOEwyOS42NTYsMTUuNTE2eiI+PC9wYXRoPjxwYXRoIHN0eWxlPSJmaWxsOiNGRkZGRkY7IiBkPSJNMzIuNDg0LDI5LjY1NmwtMi44MjgsMi44MjhsLTE0LjE0LTE0LjE0bDIuODI4LTIuODI4TDMyLjQ4NCwyOS42NTZ6Ij48L3BhdGg+PC9zdmc+";
	private static final String IMG_SRC_EDIT = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiNFNTczNzM7IiBkPSJNIDQyLjU4MjAzMSA5LjA2NjQwNiBMIDM4LjkzMzU5NCA1LjQxNzk2OSBDIDM4LjM3ODkwNiA0Ljg1OTM3NSAzNy40NzI2NTYgNC44NTkzNzUgMzYuOTE3OTY5IDUuNDE3OTY5IEwgMzUuMTk5MjE5IDcuMTM2NzE5IEwgNDAuODYzMjgxIDEyLjgwMDc4MSBMIDQyLjU4MjAzMSAxMS4wODIwMzEgQyA0My4xNDA2MjUgMTAuNTI3MzQ0IDQzLjE0MDYyNSA5LjYyNSA0Mi41ODIwMzEgOS4wNjY0MDYgIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiNGRjk4MDA7IiBkPSJNIDM4LjAzMTI1IDE1LjYzMjgxMyBMIDEyLjU3MDMxMyA0MS4wOTM3NSBMIDYuOTA2MjUgMzUuNDI5Njg4IEwgMzIuMzY3MTg4IDkuOTY4NzUgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6I0IwQkVDNTsiIGQ9Ik0gMzIuMzYzMjgxIDkuOTY4NzUgTCAzNS4xOTUzMTMgNy4xMzY3MTkgTCA0MC44NjMyODEgMTIuODAwNzgxIEwgMzguMDMxMjUgMTUuNjMyODEzIFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiNGRkMxMDc7IiBkPSJNIDYuOTA2MjUgMzUuNDI5Njg4IEwgNSA0MyBMIDEyLjU3MDMxMyA0MS4wOTM3NSBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojMzc0NzRGOyIgZD0iTSA1Ljk2NDg0NCAzOS4xNzE4NzUgTCA1IDQzIEwgOC44MjgxMjUgNDIuMDM1MTU2IFogIj48L3BhdGg+PC9nPjwvc3ZnPg==";
	private static final String IMG_SRC_CLOSE = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDE3MiAxNzIiCnN0eWxlPSIgZmlsbDojMDAwMDAwOyI+PGcgZmlsbD0ibm9uZSIgZmlsbC1ydWxlPSJub256ZXJvIiBzdHJva2U9Im5vbmUiIHN0cm9rZS13aWR0aD0iMSIgc3Ryb2tlLWxpbmVjYXA9ImJ1dHQiIHN0cm9rZS1saW5lam9pbj0ibWl0ZXIiIHN0cm9rZS1taXRlcmxpbWl0PSIxMCIgc3Ryb2tlLWRhc2hhcnJheT0iIiBzdHJva2UtZGFzaG9mZnNldD0iMCIgZm9udC1mYW1pbHk9Im5vbmUiIGZvbnQtd2VpZ2h0PSJub25lIiBmb250LXNpemU9Im5vbmUiIHRleHQtYW5jaG9yPSJub25lIiBzdHlsZT0ibWl4LWJsZW5kLW1vZGU6IG5vcm1hbCI+PHBhdGggZD0iTTAsMTcydi0xNzJoMTcydjE3MnoiIGZpbGw9Im5vbmUiPjwvcGF0aD48ZyBmaWxsPSIjNjY2NjY2Ij48ZyBpZD0ic3VyZmFjZTEiPjxwYXRoIGQ9Ik0xMjkuMDY5OTksMzAuMjYyMzdsMTIuNjgxNjQsMTIuNjY3NjRsLTk4LjgyMTYxLDk4LjgwNzYybC0xMi42Njc2NCwtMTIuNjY3NjV6Ij48L3BhdGg+PHBhdGggZD0iTTE0MS43Mzc2MywxMjkuMDgzOTlsLTEyLjY2NzY1LDEyLjY2NzY0bC05OC44MDc2MSwtOTguODM1NjFsMTIuNjY3NjQsLTEyLjY2NzY1eiI+PC9wYXRoPjwvZz48L2c+PC9nPjwvc3ZnPg==";
	private static final String IMG_SRC_PLUS = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCA0OCA0ODs7IGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMV8zNF8iPgk8cGF0aCBzdHlsZT0iZmlsbDojNENBRjUwOyIgZD0iTTQ0LDI0YzAsMTEuMDQ1LTguOTU1LDIwLTIwLDIwUzQsMzUuMDQ1LDQsMjRTMTIuOTU1LDQsMjQsNFM0NCwxMi45NTUsNDQsMjR6Ij48L3BhdGg+CTxwYXRoIHN0eWxlPSJmaWxsOiNGRkZGRkY7IiBkPSJNMjEsMTRoNnYyMGgtNlYxNHoiPjwvcGF0aD4JPHBhdGggc3R5bGU9ImZpbGw6I0ZGRkZGRjsiIGQ9Ik0xNCwyMWgyMHY2SDE0VjIxeiI+PC9wYXRoPjwvZz48L3N2Zz4=";
	private static final String IMG_SRC_UNAVAILABLE = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCA0OCA0ODs7IGZpbGw6IzAwMDAwMDsiPjxyZWN0IHg9IjUuNyIgeT0iMjIiIHRyYW5zZm9ybT0ibWF0cml4KDAuNzA3MSAtMC43MDcxIDAuNzA3MSAwLjcwNzEgLTkuOTU2MSAyNC4wMzYxKSIgc3R5bGU9ImZpbGw6I0Q1MDAwMDsiIHdpZHRoPSIzNi44IiBoZWlnaHQ9IjQiPjwvcmVjdD48cGF0aCBzdHlsZT0iZmlsbDojRDUwMDAwOyIgZD0iTTI0LDRDMTMsNCw0LDEzLDQsMjRzOSwyMCwyMCwyMHMyMC05LDIwLTIwUzM1LDQsMjQsNHogTTI0LDQwYy04LjgsMC0xNi03LjItMTYtMTZTMTUuMiw4LDI0LDggIHMxNiw3LjIsMTYsMTZTMzIuOCw0MCwyNCw0MHoiPjwvcGF0aD48L3N2Zz4=";
	private static final String IMG_SRC_EXTERNAL_LINK = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiNCQkRFRkI7IiBkPSJNIDYgMTAgTCAzOCAxMCBMIDM4IDQyIEwgNiA0MiBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojM0Y1MUI1OyIgZD0iTSA0MiA2IEwgNDIgMjEgTCAyNyA2IFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiMzRjUxQjU7IiBkPSJNIDMyLjYwNTQ2OSA5LjczNDM3NSBMIDM4LjI2NTYyNSAxNS4zOTQ1MzEgTCAyNy42NTYyNSAyNiBMIDIyIDIwLjM0Mzc1IFogIj48L3BhdGg+PC9nPjwvc3ZnPg==";
	private static final String IMG_SRC_SHARE = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iZW5hYmxlLWJhY2tncm91bmQ6bmV3IDAgMCA0OCA0ODs7IGZpbGw6IzAwMDAwMDsiPjxnPjwvZz48cGF0aCBzdHlsZT0iZmlsbDojNjA3RDhCOyIgZD0iTTM2LDQySDEyYy0zLjMwOSwwLTYtMi42OTEtNi02VjZoNHYzMGMwLDEuMTAzLDAuODk3LDIsMiwyaDI0YzEuMTAzLDAsMi0wLjg5NywyLTJ2LTJoNHYyICBDNDIsMzkuMzA5LDM5LjMwOSw0MiwzNiw0MnoiPjwvcGF0aD48cG9seWdvbiBzdHlsZT0iZmlsbDojMjE5NkYzOyIgcG9pbnRzPSI0MiwxMyAzMywyMCAzMyw2ICI+PC9wb2x5Z29uPjxwYXRoIHN0eWxlPSJmaWxsOiMyMTk2RjM7IiBkPSJNMjAsMzJoLTR2LTljMC02LjYxNyw1LjM4My0xMiwxMi0xMmg3djRoLTdjLTQuNDExLDAtOCwzLjU4OS04LDhWMzJ6Ij48L3BhdGg+PC9zdmc+";
	private static final String IMG_SRC_SETTINGS = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiM2MDdEOEI7IiBkPSJNIDM5LjYwMTU2MyAyNy4xOTkyMTkgQyAzOS42OTkyMTkgMjYuNSAzOS44MDA3ODEgMjUuODAwNzgxIDM5LjgwMDc4MSAyNSBDIDM5LjgwMDc4MSAyNC4xOTkyMTkgMzkuNjk5MjE5IDIzLjUgMzkuNjAxNTYzIDIyLjgwMDc4MSBMIDQ0LjEwMTU2MyAxOS42MDE1NjMgQyA0NC41IDE5LjMwMDc4MSA0NC42OTkyMTkgMTguNjk5MjE5IDQ0LjM5ODQzOCAxOC4xOTkyMTkgTCA0MCAxMC44MDA3ODEgQyAzOS42OTkyMTkgMTAuMzAwNzgxIDM5LjE5OTIxOSAxMC4xMDE1NjMgMzguNjk5MjE5IDEwLjM5ODQzOCBMIDMzLjY5OTIxOSAxMi42OTkyMTkgQyAzMi41IDExLjgwMDc4MSAzMS4zMDA3ODEgMTEuMTAxNTYzIDI5Ljg5ODQzOCAxMC41IEwgMjkuMzk4NDM4IDUgQyAyOS4zMDA3ODEgNC41IDI4Ljg5ODQzOCA0LjEwMTU2MyAyOC4zOTg0MzggNC4xMDE1NjMgTCAxOS44MDA3ODEgNC4xMDE1NjMgQyAxOS4zMDA3ODEgNC4xMDE1NjMgMTguODAwNzgxIDQuNSAxOC44MDA3ODEgNSBMIDE4LjMwMDc4MSAxMC41IEMgMTYuODk4NDM4IDExLjEwMTU2MyAxNS42MDE1NjMgMTEuODAwNzgxIDE0LjUgMTIuNjk5MjE5IEwgOS41IDEwLjM5ODQzOCBDIDkgMTAuMTk5MjE5IDguMzk4NDM4IDEwLjM5ODQzOCA4LjE5OTIxOSAxMC44MDA3ODEgTCAzLjg5ODQzOCAxOC4xOTkyMTkgQyAzLjYwMTU2MyAxOC42OTkyMTkgMy44MDA3ODEgMTkuMzAwNzgxIDQuMTk5MjE5IDE5LjYwMTU2MyBMIDguNjk5MjE5IDIyLjgwMDc4MSBDIDguNjAxNTYzIDIzLjUgOC41IDI0LjE5OTIxOSA4LjUgMjUgQyA4LjUgMjUuODAwNzgxIDguNjAxNTYzIDI2LjUgOC42OTkyMTkgMjcuMTk5MjE5IEwgNCAzMC4zOTg0MzggQyAzLjYwMTU2MyAzMC42OTkyMTkgMy4zOTg0MzggMzEuMzAwNzgxIDMuNjk5MjE5IDMxLjgwMDc4MSBMIDggMzkuMTk5MjE5IEMgOC4zMDA3ODEgMzkuNjk5MjE5IDguODAwNzgxIDM5Ljg5ODQzOCA5LjMwMDc4MSAzOS42MDE1NjMgTCAxNC4zMDA3ODEgMzcuMzAwNzgxIEMgMTUuNSAzOC4xOTkyMTkgMTYuNjk5MjE5IDM4Ljg5ODQzOCAxOC4xMDE1NjMgMzkuNSBMIDE4LjYwMTU2MyA0NSBDIDE4LjY5OTIxOSA0NS41IDE5LjEwMTU2MyA0NS44OTg0MzggMTkuNjAxNTYzIDQ1Ljg5ODQzOCBMIDI4LjE5OTIxOSA0NS44OTg0MzggQyAyOC42OTkyMTkgNDUuODk4NDM4IDI5LjE5OTIxOSA0NS41IDI5LjE5OTIxOSA0NSBMIDI5LjY5OTIxOSAzOS41IEMgMzEuMTAxNTYzIDM4Ljg5ODQzOCAzMi4zOTg0MzggMzguMTk5MjE5IDMzLjUgMzcuMzAwNzgxIEwgMzguNSAzOS42MDE1NjMgQyAzOSAzOS44MDA3ODEgMzkuNjAxNTYzIDM5LjYwMTU2MyAzOS44MDA3ODEgMzkuMTk5MjE5IEwgNDQuMTAxNTYzIDMxLjgwMDc4MSBDIDQ0LjM5ODQzOCAzMS4zMDA3ODEgNDQuMTk5MjE5IDMwLjY5OTIxOSA0My44MDA3ODEgMzAuMzk4NDM4IFogTSAyNCAzNSBDIDE4LjUgMzUgMTQgMzAuNSAxNCAyNSBDIDE0IDE5LjUgMTguNSAxNSAyNCAxNSBDIDI5LjUgMTUgMzQgMTkuNSAzNCAyNSBDIDM0IDMwLjUgMjkuNSAzNSAyNCAzNSBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojNDU1QTY0OyIgZD0iTSAyNCAxMyBDIDE3LjM5ODQzOCAxMyAxMiAxOC4zOTg0MzggMTIgMjUgQyAxMiAzMS42MDE1NjMgMTcuMzk4NDM4IDM3IDI0IDM3IEMgMzAuNjAxNTYzIDM3IDM2IDMxLjYwMTU2MyAzNiAyNSBDIDM2IDE4LjM5ODQzOCAzMC42MDE1NjMgMTMgMjQgMTMgWiBNIDI0IDMwIEMgMjEuMTk5MjE5IDMwIDE5IDI3LjgwMDc4MSAxOSAyNSBDIDE5IDIyLjE5OTIxOSAyMS4xOTkyMTkgMjAgMjQgMjAgQyAyNi44MDA3ODEgMjAgMjkgMjIuMTk5MjE5IDI5IDI1IEMgMjkgMjcuODAwNzgxIDI2LjgwMDc4MSAzMCAyNCAzMCBaICI+PC9wYXRoPjwvZz48L3N2Zz4=";
	private static final String IMG_SRC_UPLOAD = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiNCM0U1RkM7IiBkPSJNIDIyIDI3IEMgMjIgMzIuNTIzNDM4IDE3LjUyMzQzOCAzNyAxMiAzNyBDIDYuNDc2NTYzIDM3IDIgMzIuNTIzNDM4IDIgMjcgQyAyIDIxLjQ3NjU2MyA2LjQ3NjU2MyAxNyAxMiAxNyBDIDE3LjUyMzQzOCAxNyAyMiAyMS40NzY1NjMgMjIgMjcgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6I0IzRTVGQzsiIGQ9Ik0gMzYgMjEgQyAzNiAyNy42Mjg5MDYgMzAuNjI4OTA2IDMzIDI0IDMzIEMgMTcuMzcxMDk0IDMzIDEyIDI3LjYyODkwNiAxMiAyMSBDIDEyIDE0LjM3MTA5NCAxNy4zNzEwOTQgOSAyNCA5IEMgMzAuNjI4OTA2IDkgMzYgMTQuMzcxMDk0IDM2IDIxIFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiNCM0U1RkM7IiBkPSJNIDQ2IDI4LjUgQyA0NiAzMy4xOTUzMTMgNDIuMTk1MzEzIDM3IDM3LjUgMzcgQyAzMi44MDQ2ODggMzcgMjkgMzMuMTk1MzEzIDI5IDI4LjUgQyAyOSAyMy44MDQ2ODggMzIuODA0Njg4IDIwIDM3LjUgMjAgQyA0Mi4xOTUzMTMgMjAgNDYgMjMuODA0Njg4IDQ2IDI4LjUgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6I0IzRTVGQzsiIGQ9Ik0gMTIgMjcgTCAzNyAyNyBMIDM3IDM3IEwgMTIgMzcgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzNGNTFCNTsiIGQ9Ik0gMjIgMjIgTCAyNiAyMiBMIDI2IDMyIEwgMjIgMzIgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzNGNTFCNTsiIGQ9Ik0gMjQgMTcgTCAxOCAyNCBMIDMwIDI0IFogIj48L3BhdGg+PC9nPjwvc3ZnPg==";
	private static final String IMG_SRC_SERVICES = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiNFNjUxMDA7IiBkPSJNIDI1LjYwMTU2MyAzNC4zOTg0MzggQyAyNS42OTkyMTkgMzQgMjUuNjk5MjE5IDMzLjUgMjUuNjk5MjE5IDMzIEMgMjUuNjk5MjE5IDMyLjUgMjUuNjk5MjE5IDMyLjEwMTU2MyAyNS42MDE1NjMgMzEuNjAxNTYzIEwgMjguMzk4NDM4IDI5LjYwMTU2MyBDIDI4LjY5OTIxOSAyOS4zOTg0MzggMjguODAwNzgxIDI5IDI4LjYwMTU2MyAyOC42OTkyMTkgTCAyNS44OTg0MzggMjQuMTAxNTYzIEMgMjUuNjk5MjE5IDIzLjgwMDc4MSAyNS4zOTg0MzggMjMuNjk5MjE5IDI1LjEwMTU2MyAyMy44MDA3ODEgTCAyMiAyNS4zMDA3ODEgQyAyMS4zMDA3ODEgMjQuNjk5MjE5IDIwLjUgMjQuMzAwNzgxIDE5LjYwMTU2MyAyMy44OTg0MzggTCAxOS4zMDA3ODEgMjAuNSBDIDE5LjMwMDc4MSAyMC4xOTkyMTkgMTkgMTkuODk4NDM4IDE4LjY5OTIxOSAxOS44OTg0MzggTCAxMy4zOTg0MzggMTkuODk4NDM4IEMgMTMuMTAxNTYzIDE5Ljg5ODQzOCAxMi44MDA3ODEgMjAuMTk5MjE5IDEyLjgwMDc4MSAyMC41IEwgMTIuMzk4NDM4IDI0IEMgMTEuNSAyNC4zMDA3ODEgMTAuODAwNzgxIDI0LjgwMDc4MSAxMCAyNS4zOTg0MzggTCA2Ljg5ODQzOCAyNCBDIDYuNjAxNTYzIDIzLjg5ODQzOCA2LjE5OTIxOSAyNCA2LjEwMTU2MyAyNC4zMDA3ODEgTCAzLjM5ODQzOCAyOC44OTg0MzggQyAzLjE5OTIxOSAyOS4xOTkyMTkgMy4zMDA3ODEgMjkuNjAxNTYzIDMuNjAxNTYzIDI5LjgwMDc4MSBMIDYuMzk4NDM4IDMxLjgwMDc4MSBDIDYuMzAwNzgxIDMyLjE5OTIxOSA2LjMwMDc4MSAzMi42OTkyMTkgNi4zMDA3ODEgMzMuMTk5MjE5IEMgNi4zMDA3ODEgMzMuNjk5MjE5IDYuMzAwNzgxIDM0LjEwMTU2MyA2LjM5ODQzOCAzNC42MDE1NjMgTCAzLjYwMTU2MyAzNi42MDE1NjMgQyAzLjMwMDc4MSAzNi44MDA3ODEgMy4xOTkyMTkgMzcuMTk5MjE5IDMuMzk4NDM4IDM3LjUgTCA2LjEwMTU2MyA0Mi4xMDE1NjMgQyA2LjMwMDc4MSA0Mi4zOTg0MzggNi42MDE1NjMgNDIuNSA2Ljg5ODQzOCA0Mi4zOTg0MzggTCAxMCA0MSBDIDEwLjY5OTIxOSA0MS42MDE1NjMgMTEuNSA0MiAxMi4zOTg0MzggNDIuMzk4NDM4IEwgMTIuNjk5MjE5IDQ1LjgwMDc4MSBDIDEyLjY5OTIxOSA0Ni4xMDE1NjMgMTMgNDYuMzk4NDM4IDEzLjMwMDc4MSA0Ni4zOTg0MzggTCAxOC42MDE1NjMgNDYuMzk4NDM4IEMgMTguODk4NDM4IDQ2LjM5ODQzOCAxOS4xOTkyMTkgNDYuMTAxNTYzIDE5LjE5OTIxOSA0NS44MDA3ODEgTCAxOS41IDQyLjM5ODQzOCBDIDIwLjM5ODQzOCA0Mi4xMDE1NjMgMjEuMTAxNTYzIDQxLjYwMTU2MyAyMS44OTg0MzggNDEgTCAyNSA0Mi4zOTg0MzggQyAyNS4zMDA3ODEgNDIuNSAyNS42OTkyMTkgNDIuMzk4NDM4IDI1LjgwMDc4MSA0Mi4xMDE1NjMgTCAyOC41IDM3LjUgQyAyOC42OTkyMTkgMzcuMTk5MjE5IDI4LjYwMTU2MyAzNi44MDA3ODEgMjguMzAwNzgxIDM2LjYwMTU2MyBaIE0gMTYgMzggQyAxMy4xOTkyMTkgMzggMTEgMzUuODAwNzgxIDExIDMzIEMgMTEgMzAuMTk5MjE5IDEzLjE5OTIxOSAyOCAxNiAyOCBDIDE4LjgwMDc4MSAyOCAyMSAzMC4xOTkyMTkgMjEgMzMgQyAyMSAzNS44MDA3ODEgMTguODAwNzgxIDM4IDE2IDM4IFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiNGRkEwMDA7IiBkPSJNIDQxLjg5ODQzOCAxNS4zMDA3ODEgQyA0MiAxNC44MDA3ODEgNDIgMTQuMzk4NDM4IDQyIDE0IEMgNDIgMTMuNjAxNTYzIDQyIDEzLjE5OTIxOSA0MS44OTg0MzggMTIuNjk5MjE5IEwgNDQuMzk4NDM4IDEwLjg5ODQzOCBDIDQ0LjY5OTIxOSAxMC42OTkyMTkgNDQuNjk5MjE5IDEwLjM5ODQzOCA0NC42MDE1NjMgMTAuMTAxNTYzIEwgNDIuMTAxNTYzIDUuODAwNzgxIEMgNDEuODk4NDM4IDUuNSA0MS42MDE1NjMgNS4zOTg0MzggNDEuMzAwNzgxIDUuNjAxNTYzIEwgMzguMzk4NDM4IDYuODk4NDM4IEMgMzcuNjk5MjE5IDYuMzk4NDM4IDM3IDYgMzYuMTk5MjE5IDUuNjAxNTYzIEwgMzUuODk4NDM4IDIuNSBDIDM2IDIuMTk5MjE5IDM1LjgwMDc4MSAyIDM1LjUgMiBMIDMwLjYwMTU2MyAyIEMgMzAuMzAwNzgxIDIgMzAgMi4xOTkyMTkgMzAgMi41IEwgMjkuNjk5MjE5IDUuNjAxNTYzIEMgMjguODk4NDM4IDUuODk4NDM4IDI4LjE5OTIxOSA2LjMwMDc4MSAyNy41IDYuODk4NDM4IEwgMjQuNjAxNTYzIDUuNjAxNTYzIEMgMjQuMzAwNzgxIDUuNSAyNCA1LjYwMTU2MyAyMy44MDA3ODEgNS44MDA3ODEgTCAyMS4zMDA3ODEgMTAuMTAxNTYzIEMgMjEuMTAxNTYzIDEwLjM5ODQzOCAyMS4xOTkyMTkgMTAuNjk5MjE5IDIxLjUgMTAuODk4NDM4IEwgMjQgMTIuNjk5MjE5IEMgMjQgMTMuMTk5MjE5IDI0IDEzLjYwMTU2MyAyNCAxNCBDIDI0IDE0LjM5ODQzOCAyNCAxNC44MDA3ODEgMjQuMTAxNTYzIDE1LjMwMDc4MSBMIDIxLjYwMTU2MyAxNy4xMDE1NjMgQyAyMS4zMDA3ODEgMTcuMzAwNzgxIDIxLjMwMDc4MSAxNy42MDE1NjMgMjEuMzk4NDM4IDE3Ljg5ODQzOCBMIDIzLjg5ODQzOCAyMi4xOTkyMTkgQyAyNC4xMDE1NjMgMjIuNSAyNC4zOTg0MzggMjIuNjAxNTYzIDI0LjY5OTIxOSAyMi4zOTg0MzggTCAyNy42MDE1NjMgMjEuMTAxNTYzIEMgMjguMzAwNzgxIDIxLjYwMTU2MyAyOSAyMiAyOS44MDA3ODEgMjIuMzk4NDM4IEwgMzAuMTAxNTYzIDI1LjUgQyAzMC4xMDE1NjMgMjUuODAwNzgxIDMwLjM5ODQzOCAyNiAzMC42OTkyMTkgMjYgTCAzNS42MDE1NjMgMjYgQyAzNS44OTg0MzggMjYgMzYuMTk5MjE5IDI1LjgwMDc4MSAzNi4xOTkyMTkgMjUuNSBMIDM2LjUgMjIuMzk4NDM4IEMgMzcuMzAwNzgxIDIyLjEwMTU2MyAzOCAyMS42OTkyMTkgMzguNjk5MjE5IDIxLjEwMTU2MyBMIDQxLjYwMTU2MyAyMi4zOTg0MzggQyA0MS44OTg0MzggMjIuNSA0Mi4xOTkyMTkgMjIuMzk4NDM4IDQyLjM5ODQzOCAyMi4xOTkyMTkgTCA0NC44OTg0MzggMTcuODk4NDM4IEMgNDUuMTAxNTYzIDE3LjYwMTU2MyA0NSAxNy4zMDA3ODEgNDQuNjk5MjE5IDE3LjEwMTU2MyBaIE0gMzMgMTkgQyAzMC4xOTkyMTkgMTkgMjggMTYuODAwNzgxIDI4IDE0IEMgMjggMTEuMTk5MjE5IDMwLjE5OTIxOSA5IDMzIDkgQyAzNS44MDA3ODEgOSAzOCAxMS4xOTkyMTkgMzggMTQgQyAzOCAxNi44MDA3ODEgMzUuODAwNzgxIDE5IDMzIDE5IFogIj48L3BhdGg+PC9nPjwvc3ZnPg==";
	private static final String IMG_SRC_GROUP = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiMzRjUxQjU7IiBkPSJNIDE1IDExIEMgMTUgMTIuNjU2MjUgMTMuNjU2MjUgMTQgMTIgMTQgQyAxMC4zNDM3NSAxNCA5IDEyLjY1NjI1IDkgMTEgQyA5IDkuMzQzNzUgMTAuMzQzNzUgOCAxMiA4IEMgMTMuNjU2MjUgOCAxNSA5LjM0Mzc1IDE1IDExIFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiMzRjUxQjU7IiBkPSJNIDI1IDExIEMgMjUgMTMuMjEwOTM4IDIzLjIxMDkzOCAxNSAyMSAxNSBDIDE4Ljc4OTA2MyAxNSAxNyAxMy4yMTA5MzggMTcgMTEgQyAxNyA4Ljc4OTA2MyAxOC43ODkwNjMgNyAyMSA3IEMgMjMuMjEwOTM4IDcgMjUgOC43ODkwNjMgMjUgMTEgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzNGNTFCNTsiIGQ9Ik0gMzggMTEuNSBDIDM4IDE0LjUzOTA2MyAzNS41MzkwNjMgMTcgMzIuNSAxNyBDIDI5LjQ2MDkzOCAxNyAyNyAxNC41MzkwNjMgMjcgMTEuNSBDIDI3IDguNDYwOTM4IDI5LjQ2MDkzOCA2IDMyLjUgNiBDIDM1LjUzOTA2MyA2IDM4IDguNDYwOTM4IDM4IDExLjUgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzNGNTFCNTsiIGQ9Ik0gNDEgNDIgTCA0MSAyOC41IEMgNDEgMjMuODA0Njg4IDM3LjE5NTMxMyAyMCAzMi41IDIwIEMgMjcuODA0Njg4IDIwIDI0IDIzLjgwNDY4OCAyNCAyOC41IEwgMjQgNDIgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzNGNTFCNTsiIGQ9Ik0gMjcgMzYgTCAyNyAyMy41IEMgMjcgMjAuNDY0ODQ0IDI0LjUzOTA2MyAxOCAyMS41IDE4IEMgMTguNDYwOTM4IDE4IDE2IDIwLjQ2NDg0NCAxNiAyMy41IEwgMTYgMzYgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzNGNTFCNTsiIGQ9Ik0gMTcgMzEgTCAxNyAyMiBDIDE3IDE5LjIzODI4MSAxNC43NjE3MTkgMTcgMTIgMTcgQyA5LjIzODI4MSAxNyA3IDE5LjIzODI4MSA3IDIyIEwgNyAzMSBaICI+PC9wYXRoPjwvZz48L3N2Zz4=";
	private static final String IMG_SRC_TRASH = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iNDgwIiBoZWlnaHQ9IjQ4MCIKdmlld0JveD0iMCAwIDQ4IDQ4IgpzdHlsZT0iIGZpbGw6IzAwMDAwMDsiPjxnIGlkPSJzdXJmYWNlMSI+PHBhdGggc3R5bGU9IiBmaWxsOiM5NTc1Q0Q7IiBkPSJNIDM0IDEyIEwgMjggNiBMIDIwIDYgTCAxNCAxMiBMIDExIDEyIEwgMTEgNDAgQyAxMSA0Mi4xOTkyMTkgMTIuODAwNzgxIDQ0IDE1IDQ0IEwgMzMgNDQgQyAzNS4xOTkyMTkgNDQgMzcgNDIuMTk5MjE5IDM3IDQwIEwgMzcgMTIgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6Izc0NTRCMzsiIGQ9Ik0gMjQuNSAzOSBMIDIzLjUgMzkgQyAyMi42OTkyMTkgMzkgMjIgMzguMzAwNzgxIDIyIDM3LjUgTCAyMiAxOC41IEMgMjIgMTcuNjk5MjE5IDIyLjY5OTIxOSAxNyAyMy41IDE3IEwgMjQuNSAxNyBDIDI1LjMwMDc4MSAxNyAyNiAxNy42OTkyMTkgMjYgMTguNSBMIDI2IDM3LjUgQyAyNiAzOC4zMDA3ODEgMjUuMzAwNzgxIDM5IDI0LjUgMzkgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6Izc0NTRCMzsiIGQ9Ik0gMzEuNSAzOSBDIDMwLjY5OTIxOSAzOSAzMCAzOC4zMDA3ODEgMzAgMzcuNSBMIDMwIDE4LjUgQyAzMCAxNy42OTkyMTkgMzAuNjk5MjE5IDE3IDMxLjUgMTcgQyAzMi4zMDA3ODEgMTcgMzMgMTcuNjk5MjE5IDMzIDE4LjUgTCAzMyAzNy41IEMgMzMgMzguMzAwNzgxIDMyLjMwMDc4MSAzOSAzMS41IDM5IFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiM3NDU0QjM7IiBkPSJNIDE2LjUgMzkgQyAxNS42OTkyMTkgMzkgMTUgMzguMzAwNzgxIDE1IDM3LjUgTCAxNSAxOC41IEMgMTUgMTcuNjk5MjE5IDE1LjY5OTIxOSAxNyAxNi41IDE3IEMgMTcuMzAwNzgxIDE3IDE4IDE3LjY5OTIxOSAxOCAxOC41IEwgMTggMzcuNSBDIDE4IDM4LjMwMDc4MSAxNy4zMDA3ODEgMzkgMTYuNSAzOSBaICI+PC9wYXRoPjxwYXRoIHN0eWxlPSIgZmlsbDojQjM5RERCOyIgZD0iTSAxMSA4IEwgMzcgOCBDIDM4LjEwMTU2MyA4IDM5IDguODk4NDM4IDM5IDEwIEwgMzkgMTIgTCA5IDEyIEwgOSAxMCBDIDkgOC44OTg0MzggOS44OTg0MzggOCAxMSA4IFogIj48L3BhdGg+PC9nPjwvc3ZnPg==";
	private static final String IMG_SRC_SAVE = "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHg9IjBweCIgeT0iMHB4Igp3aWR0aD0iOTYiIGhlaWdodD0iOTYiCnZpZXdCb3g9IjAgMCA0OCA0OCIKc3R5bGU9IiBmaWxsOiMwMDAwMDA7Ij48ZyBpZD0ic3VyZmFjZTEiPjxwYXRoIHN0eWxlPSIgZmlsbDojRjQ0MzM2OyIgZD0iTSA0MiA0MiBMIDYgNDIgTCA2IDYgTCAzNyA2IEwgNDIgMTEgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6I0ZGRkZGRjsiIGQ9Ik0gMzkgMzkgQyAzOSAzOS41NTQ2ODggMzguNTU0Njg4IDQwIDM4IDQwIEwgMTAgNDAgQyA5LjQ0NTMxMyA0MCA5IDM5LjU1NDY4OCA5IDM5IEwgOSAyNSBDIDkgMjQuNDQ1MzEzIDkuNDQ1MzEzIDI0IDEwIDI0IEwgMzggMjQgQyAzOC41NTQ2ODggMjQgMzkgMjQuNDQ1MzEzIDM5IDI1IFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiNDRkQ4REM7IiBkPSJNIDEzIDMxIEwgMzUgMzEgTCAzNSAzMyBMIDEzIDMzIFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiNDRkQ4REM7IiBkPSJNIDEzIDI3IEwgMzUgMjcgTCAzNSAyOSBMIDEzIDI5IFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiNDRkQ4REM7IiBkPSJNIDEzIDM1IEwgMzUgMzUgTCAzNSAzNyBMIDEzIDM3IFogIj48L3BhdGg+PHBhdGggc3R5bGU9IiBmaWxsOiNDNjI4Mjg7IiBkPSJNIDkgNiBMIDkgMTYgQyA5IDE3LjEwNTQ2OSA5Ljg5NDUzMSAxOCAxMSAxOCBMIDI2IDE4IEMgMjcuMTA1NDY5IDE4IDI4IDE3LjEwNTQ2OSAyOCAxNiBMIDI4IDYgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6I0IwQkVDNTsiIGQ9Ik0gMTUgNiBMIDE1IDE2IEMgMTUgMTcuMTA1NDY5IDE1Ljg5NDUzMSAxOCAxNyAxOCBMIDMyIDE4IEMgMzMuMTA1NDY5IDE4IDM0IDE3LjEwNTQ2OSAzNCAxNiBMIDM0IDYgWiAiPjwvcGF0aD48cGF0aCBzdHlsZT0iIGZpbGw6IzI2MzIzODsiIGQ9Ik0gMjYgOCBMIDMwIDggTCAzMCAxNiBMIDI2IDE2IFogIj48L3BhdGg+PC9nPjwvc3ZnPg==";

	private static final String IMG_SRC_CUSTOM_SEARCH = "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDgwIiBoZWlnaHQ9IjQwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KIDwhLS0gQ3JlYXRlZCB3aXRoIE1ldGhvZCBEcmF3IC0gaHR0cDovL2dpdGh1Yi5jb20vZHVvcGl4ZWwvTWV0aG9kLURyYXcvIC0tPgoKIDxnPgogIDx0aXRsZT5iYWNrZ3JvdW5kPC90aXRsZT4KICA8cmVjdCBmaWxsPSJub25lIiBpZD0iY2FudmFzX2JhY2tncm91bmQiIGhlaWdodD0iNDAyIiB3aWR0aD0iNDgyIiB5PSItMSIgeD0iLTEiLz4KICA8ZyBkaXNwbGF5PSJub25lIiBvdmVyZmxvdz0idmlzaWJsZSIgeT0iMCIgeD0iMCIgaGVpZ2h0PSIxMDAlIiB3aWR0aD0iMTAwJSIgaWQ9ImNhbnZhc0dyaWQiPgogICA8cmVjdCBmaWxsPSJ1cmwoI2dyaWRwYXR0ZXJuKSIgc3Ryb2tlLXdpZHRoPSIwIiB5PSIwIiB4PSIwIiBoZWlnaHQ9IjEwMCUiIHdpZHRoPSIxMDAlIi8+CiAgPC9nPgogPC9nPgogPGc+CiAgPHRpdGxlPkxheWVyIDE8L3RpdGxlPgogIDxlbGxpcHNlIHJ5PSIxMzUiIHJ4PSIxMzUiIGlkPSJzdmdfMyIgY3k9IjE2MCIgY3g9IjIwMCIgc3Ryb2tlLW9wYWNpdHk9Im51bGwiIHN0cm9rZS13aWR0aD0iMCIgc3Ryb2tlPSIjMDAwIiBmaWxsPSIjNjE2MTYxIi8+CiAgPGVsbGlwc2Ugcnk9IjExMCIgcng9IjExMCIgaWQ9InN2Z182IiBjeT0iMTYwIiBjeD0iMjAwIiBzdHJva2Utb3BhY2l0eT0ibnVsbCIgc3Ryb2tlLXdpZHRoPSIwIiBzdHJva2U9IiMwMDAiIGZpbGw9IiM2NGI1ZjYiLz4KICA8cmVjdCB0cmFuc2Zvcm09InJvdGF0ZSg0NSwgMzA4LjQ0NywgMjcyLjE2NykiIGlkPSJzdmdfOCIgaGVpZ2h0PSIzMCIgd2lkdGg9IjUxIiB5PSIyNTcuMTY2Nzc4IiB4PSIyODIuOTQ3Mjk0IiBzdHJva2Utd2lkdGg9IjAiIHN0cm9rZT0iIzAwMCIgZmlsbD0iIzYxNjE2MSIvPgogIDxyZWN0IHRyYW5zZm9ybT0icm90YXRlKDQ1LCAzNjMuOTEzLCAzMjcuNTQzKSIgaWQ9InN2Z18xMSIgaGVpZ2h0PSIzMCIgd2lkdGg9IjExMCIgeT0iMzEyLjU0MzM3OSIgeD0iMzA4LjkxMjk3OSIgc3Ryb2tlLXdpZHRoPSIwIiBzdHJva2U9IiMwMDAiIGZpbGw9IiMzNzQ3NGYiLz4KICA8ZWxsaXBzZSByeT0iODAiIHJ4PSI4MCIgaWQ9InN2Z18xOSIgY3k9IjE2MCIgY3g9IjE5OS44MjEyODIiIHN0cm9rZS1vcGFjaXR5PSJudWxsIiBzdHJva2Utd2lkdGg9IjAiIHN0cm9rZT0iIzAwMCIgZmlsbD0iI2JiZGVmYiIvPgogIDxyZWN0IGlkPSJzdmdfMjIiIGhlaWdodD0iMTE5LjMzMzMzNCIgd2lkdGg9IjE3MCIgeT0iOTkuNjY2NjY3IiB4PSIxMTUiIHN0cm9rZS13aWR0aD0iMCIgc3Ryb2tlPSIjMDAwIiBmaWxsPSIjNjRiNWY2Ii8+CiAgPHJlY3QgaWQ9InN2Z18yNCIgaGVpZ2h0PSI1MCIgd2lkdGg9IjExOSIgeT0iMjAwIiB4PSIxNDAiIGZpbGwtb3BhY2l0eT0ibnVsbCIgc3Ryb2tlLW9wYWNpdHk9Im51bGwiIHN0cm9rZS13aWR0aD0iMCIgc3Ryb2tlPSIjMDAwIiBmaWxsPSIjNjRiNWY2Ii8+CiAgPGVsbGlwc2UgdHJhbnNmb3JtPSJyb3RhdGUoNjAuMzU0MiwgMTYxLCAxMDEuNjY3KSIgcnk9IjE5LjY2ODU2MiIgcng9IjEwIiBpZD0ic3ZnXzI1IiBjeT0iMTAxLjY2NjY2NyIgY3g9IjE2MSIgc3Ryb2tlLXdpZHRoPSIwIiBzdHJva2U9IiMwMDAiIGZpbGw9IiNiYmRlZmIiLz4KICA8ZWxsaXBzZSB0cmFuc2Zvcm09InJvdGF0ZSgxMTkuODcyLCAyMzkuMzMzLCAxMDIpIiByeT0iMTkuNjY4NTYyIiByeD0iMTAiIGlkPSJzdmdfMjYiIGN5PSIxMDIiIGN4PSIyMzkuMzMzMzQzIiBzdHJva2Utd2lkdGg9IjAiIHN0cm9rZT0iIzAwMCIgZmlsbD0iI2JiZGVmYiIvPgogIDxlbGxpcHNlIHJ5PSI2NSIgcng9IjY1IiBpZD0ic3ZnXzI3IiBjeT0iMTU5LjY2NjY2OCIgY3g9IjIwMCIgc3Ryb2tlLXdpZHRoPSIwIiBzdHJva2U9IiMwMDAiIGZpbGw9IiM2NGI1ZjYiLz4KIDwvZz4KPC9zdmc+";

	public static Image loadFile(final String mimetype) {
		if (mimetype == null || mimetype.isEmpty()) {
			return new Image(IMG_SRC_GENERIC);
		}
		if (mimetype.equals("application/zip")) {
			return new Image(IMG_SRC_ZIP);
		}
		if (mimetype.equals("application/xml")) {
			return new Image(IMG_SRC_SCROLL);
		}
		if (mimetype.startsWith("image")) {
			return new Image(IMG_SRC_IMAGE);
		}
		if (mimetype.startsWith("text")) {
			return new Image(IMG_SRC_DOCUMENT);
		}
		return new Image(IMG_SRC_GENERIC);
	}

	public static Image loadFolder(final boolean open) {
		if (open) {
			return new Image(IMG_SRC_OPEN_FOLDER);
		}
		return new Image(IMG_SRC_FOLDER);
	}

	public static Image loadInfo() {
		return new Image(IMG_SRC_INFO);
	}

	public static Image loadOk() {
		return new Image(IMG_SRC_OK);
	}

	public static Image loadCancel() {
		return new Image(IMG_SRC_CANCEL);
	}

	public static Image loadEdit() {
		return new Image(IMG_SRC_EDIT);
	}

	public static Image loadClose() {
		return new Image(IMG_SRC_CLOSE);
	}

	public static Image loadPlus() {
		return new Image(IMG_SRC_PLUS);
	}

	public static Image loadUnavailable() {
		return new Image(IMG_SRC_UNAVAILABLE);
	}

	public static Image loadExternalLink() {
		return new Image(IMG_SRC_EXTERNAL_LINK);
	}

	public static Image loadShare() {
		return new Image(IMG_SRC_SHARE);
	}

	public static Image loadSettings() {
		return new Image(IMG_SRC_SETTINGS);
	}

	public static Image loadUpload() {
		return new Image(IMG_SRC_UPLOAD);
	}

	public static Image loadServices() {
		return new Image(IMG_SRC_SERVICES);
	}

	public static Image loadGroup() {
		return new Image(IMG_SRC_GROUP);
	}

	public static Image loadTrash() {
		return new Image(IMG_SRC_TRASH);
	}

	public static Image loadSave() {
		return new Image(IMG_SRC_SAVE);
	}

	public static Image loadSearch() {
		return new Image(IMG_SRC_CUSTOM_SEARCH);
	}

}
