package oz.temp;

public class Zvl1 {

	String[] scriptArray ={
			"if [[ ! -d  %targetFolderPath%  ]] ; then \t mkdir -p 	%targetFolderPath% %separator% fi %separator%);",
			"cd  %targetFolderPath% %separator%);",
			"if [[ -d  %sourceFolderPath%  ]] ; then )",
			"%TAB% /usr/bin/find  %targetFolderPath%  | /usr/bin/grep -v \"Temp/\" | /usr/bin/xargs zip -q  %remoteBackupFilePath% %separator%",
			"%TAB% echo folder  %targetFolderPath% 	has been backed up to %remoteBackupFilePath% %separator%",
			"%TAB% /usr/bin/cp -pr %sourceFolderPath%/* %targetFolderPath%%separator%",
			"%TAB% /usr/bin/echo copy of   %sourceFolderPath%/*  to %targetFolderPath% has completed%separator%",		
			"%TAB% /usr/bin/find %sourceFolderPath%  > %findFile%  %separator%"
	};

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
