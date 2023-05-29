package oz.infra.zip4j.test;

import java.io.File;

import oz.infra.io.PathUtils;
import oz.infra.zip.ZipUtils;
import oz.infra.zip4j.Zip4jUtils;

public class TestZip4jUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testExtractFile();
		testExtractAll();
		// testAddFolder();
		// testMatafEAR();
		// testAddFilesDeflateComp();
	}

	private static void testAddFilesDeflateComp() {
		String folder2AddPath = "C:\\oj\\testData\\expandedWar";
		String zipFilePath = "C:\\oj\\testData\\1777.zip";
		String rootFolderInZip = "/";
		rootFolderInZip = "expandedWar/";
		Zip4jUtils.addFilesDeflateComp(zipFilePath, folder2AddPath);
	}

	private static void testAddFolder() {
		String folder2AddPath = "C:\\oj\\testData\\expandedWar";
		String zipFilePath = "C:\\oj\\testData\\1777.zip";
		String rootFolderInZip = "/";
		rootFolderInZip = "expandedWar/";
		Zip4jUtils.addFolder(zipFilePath, folder2AddPath, rootFolderInZip);

	}

	private static void testExtractAll() {
		String zipFilePath = "C:\\temp\\scripts_2015.12.29_19.35.31\\scripts_2015.12.29_19.35.31.zip";
		String targetFolderPath = "C:\\temp\\scripts_2015.12.29_19.35.31";
		Zip4jUtils.extractAllFiles(zipFilePath, targetFolderPath);
	}

	private static void testExtractFile() {
		String zipFilePath = "c:\\temp\\Projects_ODEDZ_2014.09.21_8.12.27.21.zip";
		String file2ExtractRelativePath = "Projects_ODEDZ_2014.09.21_8.12.27.21/se/eclipse.ini";
		String targetFolderPath = "c:\\temp\\test\\";
		String password = null;
		// Zip4jUtils.extractSingleFile(zipFilePath, file2ExtractRelativePath,
		// targetFolderPath, password);
		String file2AddPath = "C:\\temp\\test\\Projects_ODEDZ_2014.09.21_8.12.27.21\\se\\eclipse.ini";
		String rootFolderInZip = "Projects_ODEDZ_2014.09.21_8.12.27.21/se";
		Zip4jUtils.addFileDeflateComp(zipFilePath, file2AddPath, rootFolderInZip);
	}

	private static void testMatafEAR() {
		String rootFolderPath = "C:\\oj\\testData";
		String earFile = "MatafEAR.ear";
		String expandedEar = "expandedEar";
		String warFile = "MatafServer.war";
		String expandedWar = "expandedWar";
		String earFilePath = PathUtils.getFullPath(rootFolderPath, earFile);
		String expandedEarFolderPath = PathUtils.getFullPath(rootFolderPath, expandedEar);
		Zip4jUtils.extractAllFiles(earFilePath, expandedEarFolderPath);
		String warFilePath = PathUtils.getFullPath(expandedEarFolderPath, warFile);
		String expandedWarFolderPath = PathUtils.getFullPath(rootFolderPath, expandedWar);
		Zip4jUtils.extractAllFiles(warFilePath, expandedWarFolderPath);
		File warFileFile = new File(warFile);
		warFileFile.delete();
		ZipUtils.zipFolder(warFilePath, expandedWarFolderPath, expandedWarFolderPath);
		ZipUtils.zipFolder(earFilePath.concat(".ear"), expandedEarFolderPath, expandedEarFolderPath);
		
	}
}
