package oz.temp.zip;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class ZPFSCreate {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		logger.info("starting ...");
		try {
			/* Define ZIP File System Properies in HashMap */
			Map<String, String> zip_properties = new HashMap<String, String>();
			/* set create to true if you want to create a new ZIP file */
			zip_properties.put("create", "true");
			/* specify encoding to UTF-8 */
			zip_properties.put("encoding", "UTF-8");
			/* Locate File on disk for creation */
			URI zipDisk = URI.create("jar:file:/c:/temp/1.zip");
			/* Create ZIP file System */
			FileSystem zipfs = FileSystems.newFileSystem(zipDisk, zip_properties);
			/* Path to source file */
			Path file_to_zip = Paths.get("c:\\temp\\tut2.docx");
			logger.info(file_to_zip.toString());
			/* Path inside ZIP File */
			Path pathInZipfile = zipfs.getPath("/tut2.docx");
			/* Add file to archive */
			Files.copy(file_to_zip, pathInZipfile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("all done ...");
	}
}
