package oz.clearcase;

import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

public class RemoveViews {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		generateRmviewCommands();
	}

	private static void generateRmviewCommands() {
		String[] uuidsArray = FileUtils.readTextFile2Array("O:\\docs\\support\\clearcase\\9.0.1\\S284UE01.views");
		StringBuilder sb = new StringBuilder();
		for (String line : uuidsArray) {
			String[] lineArray = line.split("uuid ");
			String uuid = lineArray[1].substring(0, lineArray[1].length() - 1);
			logger.finest(lineArray[1] + " " + uuid);
			sb.append("\ncleartool.exe rmview -vob \\projects -uuid " + uuid);
		}
		logger.info(sb.toString());
		FileUtils.writeFile("c:\\temp\\rmviewUuid.bat", sb.toString());
	}
}
