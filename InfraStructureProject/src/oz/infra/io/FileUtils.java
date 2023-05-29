package oz.infra.io;

import static oz.infra.constants.OzConstants.INT_1000;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.UnmappableCharacterException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.bytes.ByteUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.http.HTTPUtils;
import oz.infra.logging.VerbosityLevel;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;
import oz.infra.string.StringUtils.PaddingDirection;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;
import oz.infra.varargs.VarArgsUtils;

public final class FileUtils {
	public static final String WINDOWS_1255 = "windows-1255";
	private static final int BUFFER_SIZE = 20000;
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final Logger logger = JulUtils.getLogger();

	/*
	 * appendFile
	 */
	public static void appendFile(final File appenFile, final String aContents) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(appenFile, true));
			out.write(aContents);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * appendFile
	 */
	public static void appendFile(final String appenFilePath, final String aContents) {
		appendFile(new File(appenFilePath), aContents);
	}

	public static int backupFile(final String inFilePath, final String backupFolder, boolean... deleteSource) {
		int returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
		File inFile = new File(inFilePath);
		if (inFile.exists() && inFile.isFile()) {
			String inFileName = inFile.getName();
			String backupFilePath = PathUtils.getFullPath(backupFolder, inFileName);
			backupFilePath = backupFilePath + OzConstants.DOT + PathUtils.getTemporaryFileName();
			logger.info("backupFilePath: " + backupFilePath + " inFilePath" + inFilePath);
			if (copyFile(inFilePath, backupFilePath)) {
				returnCode = OzConstants.EXIT_STATUS_OK;
				if ((deleteSource.length == 1) && deleteSource[0]) {
					inFile.delete();
				}
			}
		}
		return returnCode;
	}

	/*
	 * copyFiles method
	 */
	/**
	 * @param inFile
	 * @param outFile
	 * @param inFileCharsetString
	 * @param outFileCharsetString
	 * @param lrecl
	 * @return
	 */
	public static final boolean convertFile(final File inFile, final File outFile, final String inFileCharsetString,
			final String outFileCharsetString, final int lrecl, final boolean trim, final boolean convertNulls2Blanks, final File exceptionsFile) {
		logger.info("Starting file conversion");
		final String EBCDIC_DisplayName = "IBM424";
		final String WINDOWS_1255 = "windows-1255";
		char nullCharacter = Character.MIN_VALUE;
		char blank = ' ';
		int bufferSize = BUFFER_SIZE;
		final int exceptionsFileRecordLimit = 500;
		if (lrecl > 0) {
			bufferSize = lrecl * 200;
		}
		if (!inFile.exists()) {
			logger.warning("Input file does not exist !");
			return false;
		}
		int recordNumber = 0;
		try {
			InputStream in = new FileInputStream(inFile);
			Charset inFileCharset = Charset.forName(inFileCharsetString);
			CharsetDecoder inFileDecoder = inFileCharset.newDecoder();
			ByteBuffer inFileByteBuffer = null;
			CharBuffer inFileCharBuffer = null;
			//
			OutputStream out = new FileOutputStream(outFile);
			Charset outFileCharset = Charset.forName(outFileCharsetString);
			CharsetEncoder outFileEncoder = outFileCharset.newEncoder();
			CharBuffer outFileCharBuffer = null;
			ByteBuffer outFileByteBuffer = null;
			// Transfer bytes from in to out
			byte[] filebuffer = new byte[bufferSize];
			int readLength;
			int nullCharacterIndex;
			int nullCharctersFound = 0;
			StopWatch stopWatch = new StopWatch();
			StringBuffer exceptionsStringBuffer = new StringBuffer();
			while ((readLength = in.read(filebuffer)) > 0) {
				logger.finest(String.valueOf(readLength));
				int recorsInBuffer = readLength / lrecl;
				if ((readLength % lrecl) != 0 && inFileCharsetString.equals(EBCDIC_DisplayName)) {
					logger.warning("record " + recordNumber + " not equal to lecrel: " + String.valueOf(lrecl));
				}
				StringBuffer outFileStringBuffer = new StringBuffer();
				for (int i = 0; i < recorsInBuffer; i++) {
					recordNumber++;
					int bufferPtr = i * lrecl;
					inFileByteBuffer = ByteBuffer.wrap(filebuffer, bufferPtr, lrecl);
					inFileCharBuffer = inFileDecoder.decode(inFileByteBuffer);
					String inFileString = inFileCharBuffer.toString();
					if (convertNulls2Blanks) {
						nullCharacterIndex = inFileString.indexOf(nullCharacter);
						if (nullCharacterIndex > -1) {
							nullCharctersFound++;
							inFileString = inFileString.replace(nullCharacter, blank);
							if (nullCharctersFound < exceptionsFileRecordLimit) {
								exceptionsStringBuffer.append(inFileString);
								exceptionsStringBuffer.append("\n");
							}
						}
					}
					String string2Append = inFileString;
					if (trim) {
						string2Append = inFileString.trim();
					} else {
						if (outFileCharsetString.equals(EBCDIC_DisplayName) && string2Append.length() < lrecl) {
							string2Append = StringUtils.pad(string2Append, ' ', lrecl, PaddingDirection.RIGHT);
						}
					}
					outFileStringBuffer.append(string2Append);
					if (lrecl > 0 && outFileCharsetString.equalsIgnoreCase(WINDOWS_1255)) {
						outFileStringBuffer.append("\r\n");
					}
				}
				outFileCharBuffer = CharBuffer.wrap(outFileStringBuffer.toString());
				try {
					outFileByteBuffer = outFileEncoder.encode(outFileCharBuffer);
					out.write(outFileByteBuffer.array(), 0, outFileByteBuffer.capacity());
				} catch (UnmappableCharacterException unmappableCharacterException) {
					unmappableCharacterException.printStackTrace();
					logger.severe(unmappableCharacterException.getMessage());
					logger.info("Failed processing record " + String.valueOf(recordNumber) + ".");
				}
			}
			in.close();
			out.flush();
			out.close();
			long elapseTimeInMilliSeconds = stopWatch.getElapsedTimeLong();
			// outFile.setLastModified(inFile.lastModified());
			logger.info("\n" + inFile.getAbsolutePath() + " has been successfully converted to " + outFile.getAbsolutePath() + ".\nRecord count: "
					+ String.valueOf(recordNumber) + ".\nElspased time: " + DateTimeUtils.formatTime(elapseTimeInMilliSeconds) + ".");
			if (nullCharctersFound > 0) {
				logger.info("\n" + String.valueOf(nullCharctersFound) + "  records had nulls. (have been converted to blanks) !");
				String exceptionsString = exceptionsStringBuffer.toString();
				if (exceptionsString.length() > 0) {
					writeFile(exceptionsFile, exceptionsString);
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.severe(ex.getMessage());
			logger.info(String.valueOf(recordNumber) + " records have been processed");
			return false;
		}
	}

	/*
	 * copyFiles method
	 */
	public static boolean copyFile(final File inFile, final File outFile) {
		try {
			InputStream in = new FileInputStream(inFile);
			OutputStream out = new FileOutputStream(outFile);
			// Transfer bytes from in to out
			byte[] buf = new byte[BUFFER_SIZE];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.flush();
			out.close();
			outFile.setLastModified(inFile.lastModified());
			logger.finer(inFile.getAbsolutePath() + " has been successfully copied to " + outFile.getAbsolutePath() + " !");
			return (inFile.length() == outFile.length());
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			logger.warning(ExceptionUtils.getStackTrace(ex));
			return false;
		}
	}

	public static boolean copyFile(final String inFilePath, final String outFilePath) {
		boolean rc = false;
		if (inFilePath != null && outFilePath != null) {
			File inFile = new File(inFilePath);
			File outFile = new File(outFilePath);
			if (inFile.exists()) {
				rc = copyFile(inFile, outFile);
			}
		}
		String outcome = rc ? "succeeded" : "failed";
		logger.info(StringUtils.concat("copy of ", inFilePath, " to ", outFilePath, " has ", outcome));
		return rc;

	}

	/*
	 * copyHTTPFile
	 */
	public static void copyHTTPFile(final String url, final File outFile) {
		writeFile(outFile, HTTPUtils.getPageContents(url));
	}

	public static boolean copyTextFile(final File inFile, final File outFile) {
		boolean returnCode = false;
		if (copyTextFile(inFile, outFile, 0, Integer.MAX_VALUE)) {
			returnCode = inFile.length() == outFile.length();
		}
		return returnCode;
	}

	public static boolean copyTextFile(final File inFile, final File outFile, final int fromRecord, final int toRecord) {
		boolean returnCode = false;
		int recordCounter = 0;
		int recordsProcessed = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(inFile));
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			String line;
			for (recordCounter = 1; (line = in.readLine()) != null && recordCounter <= toRecord; recordCounter++) {
				if (recordCounter > fromRecord) {
					out.newLine();
				}
				if (recordCounter >= fromRecord) {
					out.write(line);
					recordsProcessed++;
				}
			}
			in.close();
			out.close();
			logger.info(inFile.getAbsolutePath() + " has been copied to " + outFile.getAbsolutePath());
			logger.info(String.valueOf(recordsProcessed) + " records have been processed. (From record " + String.valueOf(fromRecord) + " to record "
					+ String.valueOf(toRecord) + ")");
			returnCode = true;
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		return returnCode;
	}

	public static boolean deleteAndLogResult(final File file2Delete) {
		boolean rc = file2Delete.delete();
		if (rc) {
			logger.info(file2Delete.getAbsolutePath() + " has been successfully deleted.");
		} else {
			logger.warning("Failed to delete " + file2Delete.getAbsolutePath() + " !");
		}
		return rc;
	}

	public static boolean deleteAndLogResult(final String file2DeletePath) {
		return deleteAndLogResult(new File(file2DeletePath));
	}

	public static boolean deleteFolderContents(final File folder, final String regExpression) {
		boolean rc = false;
		if (folder.exists() && folder.isDirectory()) {
			File[] files2Delete = folder.listFiles();
			rc = true;
			for (File file1 : files2Delete) {
				if (RegexpUtils.matches(file1.getAbsolutePath(), regExpression)) {
					rc = rc && file1.delete();
					if (rc) {
						logger.info(file1.getAbsolutePath() + " has been successfully deleted");
					} else {
						logger.info("Failed to delete " + file1.getAbsolutePath());
					}
				}
			}
		}
		return rc;
	}

	public static boolean deleteFolderContents(final String folderPath, final String regExpression) {
		return deleteFolderContents(new File(folderPath), regExpression);
	}

	public static void dos2unix(final String infilePath, final String outFilePath) {
		long inLength = new File(infilePath).length();
		String contentsString = readTextFile(infilePath, OzConstants.UNIX_LINE_SEPARATOR);
		int contentsLength = contentsString.length();
		writeFile(outFilePath, contentsString);
		long outLength = new File(outFilePath).length();
		logger.info(StringUtils.concat("input file length: ", String.valueOf(inLength), " contents length: " + String.valueOf(contentsLength),
				" output file length: ", String.valueOf(outLength)));
	}

	public static void exitIfFileDoesNoteExist(final String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			logger.warning(filePath + " does not exist !. Processing has been terminated.");
			System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
		} else {
			logger.info(filePath + " exists.");
		}
	}

	public static String getAllRootsFreeSpace() {
		File[] roots = File.listRoots();
		StringBuilder stringBuilder = new StringBuilder();
		for (File root : roots) {
			stringBuilder.append(root.getAbsolutePath() + " " + String.valueOf(root.getFreeSpace()) + "\n");
		}
		return stringBuilder.toString();
	}

	public static String getCurrentDir() {
		File currentDirFile = new File(".");
		String currentDirPath = currentDirFile.getAbsolutePath();
		logger.info("current dir: " + currentDirPath);
		return currentDirPath;
	}

	public static String getExisting(final String[] pathsArray, final FileDirectoryEnum... fileDirectoryParam) {
		String existingPath = null;
		FileDirectoryEnum fileDirectoryEnum = FileDirectoryEnum.ANY;
		if (fileDirectoryParam.length == 1) {
			fileDirectoryEnum = fileDirectoryParam[0];
		}
		for (int i = 0; i < pathsArray.length && existingPath == null; i++) {
			File file1 = new File(pathsArray[i]);
			switch (fileDirectoryEnum) {
			case ANY:
				if (file1.exists()) {
					existingPath = pathsArray[i];
				}
				break;
			case FILE:
				if (file1.isFile()) {
					existingPath = pathsArray[i];
				}
				break;
			case DIRECTORY:
				if (file1.isDirectory()) {
					existingPath = pathsArray[i];
				}
				break;
			}
		}
		return existingPath;
	}

	/*
	 * getFileMessageDigest method
	 */
	public static byte[] getFileMessageDigest(final String digestAlgorithm, final String filePath) {
		try {
			// Obtain a message digest object.
			MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
			// Calculate the digest for the given file.
			DigestInputStream in = new DigestInputStream(new FileInputStream(filePath), md);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (in.read(buffer) != -1) {
				Thread.sleep(10);
			}
			byte[] raw = md.digest();
			in.close();
			return raw;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static int getNumberOfLines(final File inFile) {
		byte[] bytesArray = readFileToByteArray(inFile);
		int numberOfLines = ByteUtils.getNumberOfOccurences(bytesArray, (byte) OzConstants.INT_10);
		return numberOfLines + 1;
	}

	public static boolean isFileExists(final String filePath, final VerbosityLevel... verbosityLevels) {
		VerbosityLevel verbosityLevel = VarArgsUtils.getMyArg(VerbosityLevel.None, verbosityLevels);
		boolean fileExists = false;
		if (filePath != null && filePath.length() > 0) {
			File file = new File(filePath);
			fileExists = file.isFile();
		}
		if (fileExists && verbosityLevel.equals(VerbosityLevel.Full)) {
			logger.info(filePath + " exists and is a file!");
		}
		if (!fileExists && (verbosityLevel.equals(VerbosityLevel.Error) || verbosityLevel.equals(VerbosityLevel.Full))) {
			logger.warning(filePath + " does not exist or is not a file !");
		}

		return fileExists;
	}

	public static boolean isFileStartWithBom(final String textFilePath) {
		String fileContents = readTextFile(textFilePath);
		return ((fileContents != null) && (fileContents.length() > 0) && (fileContents.startsWith(OzConstants.UTF8_BOM)));
	}

	public static boolean moveFile(final String sourceFilepath, final String targetFilePath) {
		final int sleepInterval = INT_1000;
		File sourceFile = new File(sourceFilepath);
		File tagetFile = new File(targetFilePath);
		boolean rc = sourceFile.isFile();
		if (rc) {
			rc = sourceFile.renameTo(tagetFile);
			ThreadUtils.sleep(sleepInterval, Level.INFO);
			if (rc) {
				logger.info(StringUtils.concat(sourceFilepath, " has been moved to ", targetFilePath));

			} else {
				logger.warning(StringUtils.concat("Failed to move ", sourceFilepath, " to ", targetFilePath));
			}
		} else {
			logger.warning(StringUtils.concat(sourceFilepath, " does not exist or is not a file !"));
		}
		return rc;
	}

	public static SystemCommandExecutorResponse moveUsingWindowsShell(final String source, final String target) {
		logger.info("source: " + source + " target: " + target);
		String[] parametersArray1 = { "cmd", "/c", "move ", "/y", source, target };
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parametersArray1);
		logger.info(systemCommandExecutorResponse.getExecutorResponse());
		return systemCommandExecutorResponse;
	}

	public static boolean performFullBinaryComparison(final File file1, final File file2) {
		StopWatch stopWatch = new StopWatch();
		boolean returnCode = false;
		try {
			if (file1.length() != file2.length()) {
				return false;
			}
			if ((file1.length() == 0) && (file2.length() == 0)) {
				return true;
			}
			FileInputStream ins1 = new FileInputStream(file1);
			FileInputStream ins2 = new FileInputStream(file2);
			BufferedInputStream bis1 = new BufferedInputStream(ins1, BUFFER_SIZE);
			BufferedInputStream bis2 = new BufferedInputStream(ins2, BUFFER_SIZE);
			int c1 = 0;
			int c2 = 0;
			boolean keepGoing = true;
			do {
				c1 = bis1.read();
				c2 = bis2.read();
				if (c1 != c2) {
					keepGoing = false;
				}
			} while (keepGoing && c1 != -1 && c2 != -1);
			ins1.close();
			ins2.close();
			bis1.close();
			bis2.close();
			returnCode = (c1 == c2);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
			returnCode = false;
		}
		if (returnCode) {
			logger.info(StringUtils.concat(file1.getAbsolutePath(), " is equal to ", file2.getAbsolutePath()));
		} else {
			logger.warning(StringUtils.concat(file1.getAbsolutePath(), " is equal to ", file2.getAbsolutePath()));
		}
		stopWatch.logElapsedTimeMessage();
		return returnCode;
	}

	/*
	 * performFullBinaryComparison
	 */
	public static boolean performFullBinaryComparison(final String file1Path, final String file2Path) {
		return performFullBinaryComparison(new File(file1Path), new File(file2Path));
	}

	/*
	 * performLengthAndLastModifiedComparison method
	 */
	public static boolean performLengthAndLastModifiedComparison(final File file1, final File file2) {
		boolean returnCode = file1.length() == file2.length() && ((file1.isDirectory() && file2.isDirectory())
				|| (file1.lastModified() == file2.lastModified()) && file1.isFile() && file2.isFile());
		return returnCode;
	}

	/*
	 * performMessageDigestsComparison method
	 */
	public static boolean performMessageDigestsComparison(final String filePath1, final String filePath2, final String messageDigestAlgorithm) {
		StopWatch stopWatch = new StopWatch();
		byte[] md1 = getFileMessageDigest(messageDigestAlgorithm, filePath1);
		byte[] md2 = getFileMessageDigest(messageDigestAlgorithm, filePath2);
		StringBuffer md12Print = new StringBuffer(md1.length * 4);
		StringBuffer md22Print = new StringBuffer(md2.length * 4);
		for (int i = 0; i < md1.length; i++) {
			md12Print.append(Byte.toString(md1[i]) + " ");
			md22Print.append(Byte.toString(md2[i]) + " ");
		}
		logger.finer(filePath1 + "\n md1= " + md12Print.toString());
		logger.finer(filePath2 + "\n md2= " + md22Print.toString());
		boolean returnCode = Arrays.equals(md1, md2);
		if (returnCode) {
			logger.info(StringUtils.concat(filePath1, ", ", filePath2, " are equal !", "  message digest algorithm: ", messageDigestAlgorithm));
		} else {
			logger.info(StringUtils.concat(filePath1, ", ", filePath2, " are equal !", "  message digest algorithm: ", messageDigestAlgorithm));
		}
		stopWatch.logElapsedTimeMessage();
		return returnCode;
	}

	/*
	 * readFile method
	 */
	public static void processTextFileLines(final File myInputFile, final LineProcessor lineProcessor) {
		LineProcessor[] lineProcessorArray = { lineProcessor };
		processTextFileLines(myInputFile, lineProcessorArray);
	}

	/*
	 * readFile method
	 */
	public static void processTextFileLines(final File myInputFile, final LineProcessor[] lineProcessorsArray) {
		// ...checks on myInputFile are elided
		// declared here only to make visible to finally clause
		int recordsProcessed = 0;
		BufferedReader bufferedReader = null;
		try {
			// use buffering
			// this implementation reads one line at a time
			bufferedReader = new BufferedReader(new FileReader(myInputFile));
			String line = null; // not declared within while loop
			while ((line = bufferedReader.readLine()) != null) {
				recordsProcessed++;
				for (LineProcessor lineProcessor1 : lineProcessorsArray) {
					lineProcessor1.processLine(line);
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		} finally {
			try {
				if (bufferedReader != null) {
					// flush and close both "input" and its underlying
					// FileReader
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		for (LineProcessor lineProcessor1 : lineProcessorsArray) {
			lineProcessor1.processEOF(new Integer(recordsProcessed));
		}
		logger.info(String.valueOf(recordsProcessed) + " records have been processed from " + myInputFile.getAbsolutePath());
	}

	public static byte[] readFileToByteArray(final File inFile) {
		byte[] bytesArray = null;
		if (!inFile.exists()) {
			logger.warning(inFile.getAbsolutePath() + " does not exist. Processing has been aborted!");
			return bytesArray;
		}
		long inFileLength = inFile.length();
		InputStream in = null;
		try {
			in = new FileInputStream(inFile);
			bytesArray = new byte[(int) inFileLength];
			int bytesRead = in.read(bytesArray);
			if (bytesRead != inFileLength) {
				logger.warning("Number of bytes read in to eaual to input file length !");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		return bytesArray;
	}

	public static byte[] readFileToByteArray(final String filePath) {
		return readFileToByteArray(new File(filePath));
	}

	/*
	 * readInputStream
	 */
	public static String readInputStream(final InputStream inputStream) {
		StringBuffer buffer = new StringBuffer();
		try {
			InputStreamReader isr = new InputStreamReader(inputStream);
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char) ch);
			}
			in.close();
			return buffer.toString();
		} catch (IOException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			return null;
		}
	}

	public static byte[] readLastBytes(final String inputFilePath, final int numberOfBytes) {
		byte[] bytes = null;
		File inputFile = new File(inputFilePath);
		try {
			RandomAccessFile raf = new RandomAccessFile(inputFile, "r");
			int rafLength = (int) raf.length();
			logger.info("File path: " + inputFilePath + " File length: " + String.valueOf(rafLength) + " number of bytes to read: "
					+ String.valueOf(numberOfBytes));
			int numberOfBytes2Read = numberOfBytes;
			if (rafLength < numberOfBytes2Read) {
				numberOfBytes2Read = rafLength;
			}
			bytes = new byte[numberOfBytes2Read];
			int fileOffset = 0;
			if (rafLength > numberOfBytes2Read) {
				fileOffset = rafLength - numberOfBytes2Read;
			}
			raf.seek(fileOffset);
			int bytesRead = raf.read(bytes, 0, numberOfBytes2Read);
			if (bytesRead != numberOfBytes2Read) {
				logger.warning("Read only " + String.valueOf(bytesRead) + " bytes");
			}
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
		}
		return bytes;
	}

	/*
	 * readFile method
	 */
	public static String readTextFile(final File myInputFile) {
		// ...checks on myInputFile are elided
		StringBuffer contentsSb = new StringBuffer();
		// declared here only to make visible to finally clause
		BufferedReader bufferedReader = null;
		try {
			// use buffering
			// this implementation reads one line at a time
			bufferedReader = new BufferedReader(new FileReader(myInputFile));
			String line = null; // not declared within while loop
			while ((line = bufferedReader.readLine()) != null) {
				contentsSb.append(line);
				contentsSb.append(System.getProperty("line.separator"));
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		} finally {
			try {
				if (bufferedReader != null) {
					// flush and close both "input" and its underlying
					// FileReader
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return contentsSb.toString();
	}

	public static String readTextFile(final String textFilePath, final String... lineTerminatorParam) {
		String lineTerminator = VarArgsUtils.getMyArg(NEW_LINE, lineTerminatorParam);
		String fileContents = null;
		StringBuffer fileStringBuffer = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(textFilePath));
			String line;
			while ((line = in.readLine()) != null) {
				if (fileStringBuffer.length() > 0) {
					fileStringBuffer.append(lineTerminator);
				}
				fileStringBuffer.append(line);
			}
			in.close();
			fileContents = fileStringBuffer.toString();
		} catch (IOException ioe) {
			logger.warning(ioe.getMessage());
			ioe.printStackTrace();
		}
		return fileContents;
	}

	/*
	 * readTextFile2Array method
	 */
	public static String[] readTextFile2Array(final File textFile) {
		String[] recordsArray = null;
		if (textFile.exists() && textFile.isFile()) {
			String recordsString = readTextFile(textFile);
			String lineSeparator = SystemUtils.LINE_SEPARATOR;
			recordsArray = recordsString.split(lineSeparator);
			logger.info(String.valueOf(recordsArray.length) + " records have been read from " + textFile.getAbsolutePath());

		} else {
			logger.warning(textFile.getAbsolutePath() + " does not exist or is not a file !");
		}
		return recordsArray;
	}

	/*
	 * readTextFile2Array method
	 */
	public static String[] readTextFile2Array(final String textFilePath) {
		File textFile = new File(textFilePath);
		return readTextFile2Array(textFile);
	}

	public static String[] readTextFile2Array(final String readFilePath, final String characterSet) {
		/*
		 * Read Input File
		 */
		logger.info("Read input file " + readFilePath + " character set is " + characterSet);
		String recordsString = readTextFile(readFilePath, characterSet);
		return recordsString.split(System.getProperty("line.separator"));
	}

	public static String readTextFileFromClassPath(final Class clazz, final String filePath) {
		logger.info("file path: " + filePath + "  class: " + clazz.toString());
		String contents = null;
		InputStream inputStream = clazz.getResourceAsStream(filePath);
		if (inputStream != null) {
			contents = readInputStream(inputStream);
		} else {
			logger.warning("inputStream is null !");
		}
		return contents;
	}

	public static String readTextFileFromClassPath(final String filePath) {
		return readTextFileFromClassPath(SystemUtils.getCallerClassObject(), filePath);

	}

	/**
	 * @param filePath
	 *            name of file to open. The file can reside anywhere in the classpath
	 */

	public static String readTextFileSkipBom(final String textFilePath) {
		String fileContents = readTextFile(textFilePath);
		if (fileContents != null && fileContents.length() > 0 && fileContents.startsWith(OzConstants.UTF8_BOM)) {
			fileContents = fileContents.substring(1);
		}
		return fileContents;
	}

	/**
	 * Fetch the entire contents of a text file, and return it in a String. This style of implementation does not throw
	 * Exceptions to the caller.
	 * 
	 * @param myInputFile
	 *            is a file which already exists and can be read.
	 */
	public static String readTextFileWithCharSet(final String filePath, final String characterSet) {
		StringBuffer buffer = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			InputStreamReader isr = new InputStreamReader(fis, characterSet);
			Reader bufferedReader = new BufferedReader(isr);
			int ch;
			while ((ch = bufferedReader.read()) > -1) {
				buffer.append((char) ch);
			}
			bufferedReader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readTextFileWithEncoding(final String filePath, final String charsetString) {
		StringBuilder textSb = new StringBuilder();
		try {
			InputStream inputStream = new FileInputStream(filePath);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetString);
			BufferedReader br = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = br.readLine()) != null) {
				textSb.append(line);
				textSb.append(SystemUtils.LINE_SEPARATOR);
			}

			br.close();
			inputStreamReader.close();
			inputStream.close();
		} catch (Exception ex) {
			logger.warning(ExceptionUtils.printMessageAndStackTrace(ex));
		}
		return textSb.toString();
	}

	public static String[] readTextFileWithEncoding2Array(final String filePath, final String charsetString) {
		String[] result = null;
		String contents = readTextFileWithEncoding(filePath, charsetString);
		if (contents != null) {
			result = contents.split(SystemUtils.LINE_SEPARATOR);
		}
		return result;
	}

	public static boolean renameFile(final String sourceFilePath, final String targetFilePath) {
		boolean renameResult = false;
		if (sourceFilePath != null && targetFilePath != null) {

			File sourceFile = new File(sourceFilePath);
			File targetFile = new File(targetFilePath);
			renameResult = sourceFile.renameTo(targetFile);
		}
		if (renameResult) {
			logger.info(StringUtils.concat(sourceFilePath, " has been renamed to ", targetFilePath));
		} else {
			logger.warning(StringUtils.concat("Rename of ", sourceFilePath, " to ", targetFilePath, " has failed."));
		}

		return renameResult;
	}

	public static boolean renameFolder(final String sourceFilePath, final String targetFilePath) {
		return renameFile(sourceFilePath, targetFilePath);
	}

	public static int replaceBytesInFile(final String inFilePath, final String outFilePath, final byte inByte, final byte outByte) {
		int returnCode = -1;
		int bytesReplaced = 0;
		File inFile = new File(inFilePath);
		if (!inFile.exists()) {
			logger.warning(inFilePath + " does not exist. Processing has been aborted!");
			return -1;
		}
		long inFileLength = inFile.length();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(inFilePath);
			out = new FileOutputStream(outFilePath);
			int availableLength = in.available();
			byte[] bytesArray = new byte[(int) inFileLength];
			int bytesRead = in.read(bytesArray);
			if (bytesRead != inFileLength) {
				logger.warning("Number of bytes read in to eaual to input file length !");
			}
			for (int i = 0; i < bytesArray.length; i++) {
				if (bytesArray[i] == inByte) {
					bytesArray[i] = outByte;
					bytesReplaced++;
				}
			}
			out.write(bytesArray);
			returnCode = 0;
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				logger.info("Closing files ...");
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception ex) {
				logger.warning(ex.getMessage());
				ex.printStackTrace();
			}
		}
		logger.info(String.valueOf(bytesReplaced) + " bytes have been replaced.");
		return returnCode;
	}

	public static int replaceStringsInTextFile(final String inFilePath, final String outFilePath, final String inPattern, final String outPattern) {
		logger.info("Start processing inFilePath: " + inFilePath);
		String line;
		int returnCode = -1;
		long inputRecords = 0;
		long replacedRecords = 0;
		try {
			FileInputStream fis = new FileInputStream(inFilePath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			BufferedWriter out = new BufferedWriter(new FileWriter(outFilePath));
			while ((line = reader.readLine()) != null) {
				inputRecords++;
				if (inputRecords > 1) {
					out.write(NEW_LINE);
				}
				if (line.indexOf(inPattern) > -1) {
					line = line.replaceAll(inPattern, outPattern);
					replacedRecords++;
				}
				out.write(line);
			}
			reader.close();
			out.flush();
			out.close();
			File inFile = new File(inFilePath);
			File outFile = new File(outFilePath);
			long inFileLength = inFile.length();
			long outFileLength = outFile.length();
			logger.info("Number of input records: " + inputRecords);
			logger.info("Number of replaced records: " + replacedRecords);
			logger.info(inFilePath + " length is: " + inFileLength);
			logger.info(outFilePath + " length is: " + outFileLength);
			if (inFileLength > 0 && inFileLength == outFileLength) {
				returnCode = 0;
			}
		} catch (Throwable e) {
			logger.warning(e.getMessage());
			e.printStackTrace();
		}
		logger.info("Return code: " + String.valueOf(returnCode));
		return returnCode;
	}

	public static void reverseFile(final String inFilePath, final String outFilePath) {
		String[] inArray = readTextFile2Array(inFilePath);
		String[] outArray = ArrayUtils.reverse(inArray);
		writeFile(outFilePath, outArray, OzConstants.LINE_FEED);
		ArrayUtils.printArray(inArray, Level.INFO);
		ArrayUtils.printArray(outArray, Level.INFO);
	}

	public static void terminateIfFileDoesNotExist(final String filePath, final String... messageArg) {
		String message = VarArgsUtils.getMyArg("", messageArg);
		String exitMessage = null;
		if (filePath != null) {
			File file2Check = new File(filePath);
			if (!file2Check.exists() || !file2Check.isFile()) {
				exitMessage = StringUtils.concat(SystemUtils.getCallerMethodName(), "  ", filePath,
						" does not exist or is not a file. Execution has been terminated!");
			}
		} else {
			exitMessage = "parameter is null.. Execution has been terminated!";
		}
		if (exitMessage != null) {
			SystemUtils.printMessageAndExit(message.concat(exitMessage), OzConstants.EXIT_STATUS_ABNORMAL, false);
		}
	}

	/**
	 * Change the contents of text file in its entirety, overwriting any existing text.
	 * 
	 * This style of implementation throws all exceptions to the caller.
	 * 
	 * @param aFile
	 *            is an existing file which can be written to.
	 * @throws IllegalArgumentException
	 *             if param does not comply.
	 * @throws FileNotFoundException
	 *             if the file does not exist.
	 * @throws IOException
	 *             if problem encountered during write.
	 */
	/*
	 * writeFile method
	 */
	public static Outcome writeFile(final File outFile, final String contentsString) {
		return writeFile(outFile, contentsString, Level.INFO);
	}

	/*
	 * writeFile method
	 */
	public static Outcome writeFile(final File outFile, final String contentsString, final Level level) {
		Outcome outcome = Outcome.FAILURE;
		if (outFile == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		Writer output = null;
		try {
			// use buffering
			output = new BufferedWriter(new FileWriter(outFile));
			output.write(contentsString);
			output.flush();
			output.close();
			logger.log(level, outFile.getAbsolutePath() + " has been successfully written. Size: " + String.valueOf(outFile.length()));
			outcome = Outcome.SUCCESS;
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
			logger.warning(StringUtils.concat("failed to write ", outFile.getAbsolutePath()));
		}
		return outcome;
	}

	/*
	 * writeFile method
	 */
	public static Outcome writeFile(final String outFilePath, final String contentsString) {
		logger.finest("Output file path: " + outFilePath);
		return writeFile(new File(outFilePath), contentsString);
	}

	/*
	 * writeFile method
	 */
	public static Outcome writeFile(final String outFilePath, final String contentsString, final Level level) {
		logger.finest("Output file path: " + outFilePath);
		return writeFile(new File(outFilePath), contentsString, level);
	}

	/*
	 * writeFile method
	 */
	public static Outcome writeFile(final String outFilePath, final String[] contentsStringArray, final String separator) {
		logger.finest("Output file path: " + outFilePath);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < contentsStringArray.length; i++) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(contentsStringArray[i]);
		}
		return writeFile(new File(outFilePath), sb.toString());
	}

	public static void writeTextFileWithEncoding(final String outFilePath, final String charsetString, final String contents) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(outFilePath);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, charsetString);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			bufferedWriter.write(contents);
			bufferedWriter.close();
			outputStreamWriter.close();
			fileOutputStream.close();
		} catch (Exception ex) {
			logger.warning(ExceptionUtils.printMessageAndStackTrace(ex));
		}
	}

	private FileUtils() {
	}
}
