package oz.utils.FixExcel4RoimRahok.watcher;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Workbook;

import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import oz.infra.constants.OzConstants;
import oz.infra.excel.utils.ExcelUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.process.FileProcessor;
import oz.infra.string.StringUtils;
import oz.infra.thread.ThreadUtils;
import oz.utils.FixExcel4RoimRahok.FixExcel4RoimRahokParameters;

public class FixExcel4RoimRahok implements FileProcessor {
	private static String targetFilePrefix = FixExcel4RoimRahokParameters.getTargetFilePrefix();
	private static String string2Replace = FixExcel4RoimRahokParameters.getString2Replace();
	private static String replacement = FixExcel4RoimRahokParameters.getReplacement();
	private static String[] columnNamesArray = FixExcel4RoimRahokParameters.getColumnNamesArray();
	private static Logger logger = JulUtils.getLogger();

	public FixExcel4RoimRahok() {
		logger.info(StringUtils.concat("\n", OzConstants.ASTERISKS_20, " starting up ... "));
		WatchEvent.Kind<?>[] eventKinds = { StandardWatchEventKind.ENTRY_CREATE };
		// new WatchDirService(FixExcel4RoimRahokParameters.getFolderPath(), false, eventKinds, this);
	}

	public Outcome processFile(final String fileName) {
		ThreadUtils.sleep(OzConstants.INT_1000, Level.INFO);
		try {
			if (fileName.equalsIgnoreCase("QUIT")) {
				logger.info("exiting ...");
				System.exit(OzConstants.EXIT_STATUS_OK);
			}
			String folderPath = FixExcel4RoimRahokParameters.getFolderPath();
			String fileFullPath = PathUtils.getFullPath(folderPath, fileName);
			logger.info("Processing " + fileFullPath + " ...");
			File file = new File(fileFullPath);
			if (file.isFile() && (!fileName.startsWith(targetFilePrefix)) && (!fileName.startsWith(OzConstants.OFFICE_TEMP_FILE_PREFIX))) {
				Workbook wb = ExcelUtils.getWorkBook(fileFullPath);
				for (int i = 0; i < columnNamesArray.length; i++) {
					ExcelUtils.replaceAllAndTrimColumn(wb, columnNamesArray[i], string2Replace, replacement);
				}
				String targetFolderPath = PathUtils.getFullPath(folderPath, FixExcel4RoimRahokParameters.getTargetFolderPath());
				FolderUtils.createFolderIfNotExists(targetFolderPath);
				String outFilePath = PathUtils.getFullPath(targetFolderPath, fileName);
				logger.info(outFilePath);
				ExcelUtils.writeWorkBook(outFilePath, wb);
				if (FixExcel4RoimRahokParameters.isOpenExcelWorkBook()) {
					ExcelUtils.openExcelWorkbook(outFilePath);
				}
			}
		} catch (Exception ex) {
			logger.warning(ExceptionUtils.printMessageAndStackTrace(ex));
		}
		return null;
	}
}
