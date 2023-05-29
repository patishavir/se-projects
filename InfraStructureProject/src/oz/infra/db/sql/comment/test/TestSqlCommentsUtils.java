package oz.infra.db.sql.comment.test;

import java.util.logging.Logger;

import oz.infra.db.sql.comment.SqlCommentsUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

public class TestSqlCommentsUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testRemoveComments();
		// logger.info(PrintUtils.getSeparatorLine(""));
		// testRemoveBlockComments();
		// logger.info(PrintUtils.getSeparatorLine(""));
		// testRemoveBlockComments1();
		testRemoveCommentsFromFile();
	}

	private static void testRemoveBlockComments() {
		String in = "baba\r\n/*\n hhaa\n*/\n/*\r\nuuuuuuuuuuuuuuuuuuuuuu\r\n*/\n\n\nQQQQQQQQQQQ\r\nhooooooooooo\n/*jkjkjk\n   *////\n\rlast\n /*\n     *///* remove */include";
		String out = SqlCommentsUtils.removeBlockComments(in);
		logger.info("in:\n" + in);
		logger.info("out:\n" + out);
		FileUtils.writeFile("c:\\temp\\remo.txt", out);
	}

	private static void testRemoveBlockComments1() {
		// String in =
		// "baba\r\n/*/n
		// hhaa\n*/\n/*\r\nuuuuuuuuuuuuuuuuuuuuuu\r\n*/\n\n\nQQQQQQQQQQQ\r\nhooooooooooo\n/*jkjkjk\n
		// *////\n\rlast\n /*\n *//";
		String in = "123/*456OUT*/inbab/*8OUT//76*/5a/*XXXOUT*/LALA/*y*/inxyz";
		String out = SqlCommentsUtils.removeBlockComments(in);
		logger.info("in:\n" + in);
		logger.info("out:\n" + out);
		FileUtils.writeFile("c:\\temp\\remo.txt", out);
	}

	private static void testRemoveComments() {
		String in = "-- bala1\n==bla2ccc\n bubu\n------------\r\ndududu --yuyuy/*rrrrrreeee*//";
		String out = SqlCommentsUtils.removeComments(in);
		logger.info(out);
		FileUtils.writeFile("c:\\temp\\remo.txt", out);
	}

	private static void testRemoveCommentsFromFile() {
		String inputFilePath = "C:/oj/projects/se/testData/sqlComments/dbchanges_53405_Final.sql";
		String sqlStatementString = FileUtils.readTextFile(inputFilePath);

		String outFilePath0 = "c:/temp/originalFile.sql";
		FileUtils.writeFile(outFilePath0, sqlStatementString);

		String r1 = SqlCommentsUtils.removeBlockComments(sqlStatementString);
		String outFilePath1 = "c:/temp/afterremoveBlockComments.sql";
		FileUtils.writeFile(outFilePath1, r1);

		String r2 = SqlCommentsUtils.removeComments(r1);
		String outFilePath2 = "c:/temp/afterremoveComments.sql";
		FileUtils.writeFile(outFilePath2, r2);
	}
}
