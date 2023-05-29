package oz.infra.excel;

import oz.infra.excel.utils.ExcelUtils;
import oz.infra.excel.utils.ExcelUtils.ExcelUtilsEnum;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.io.FileUtils;

public class ExcelUtilsMain {

	public static void main(final String[] args) {
		ExcelUtilsEnum excelUtilsEnum = ExcelUtilsEnum.valueOf(args[0]);
		switch (excelUtilsEnum) {
		case changeBgColorOnValueChange:
			String csvFilePath = args[1];
			int column = Integer.parseInt(args[2]);
			String xlsFilePath = args[3];
			String sheetName = args[4];
			String colorString = null;
			if (args.length > 5) {
				colorString = args[5];
			}
			FileUtils.terminateIfFileDoesNotExist(csvFilePath);
			String contents = ExcelUtils.changeBgColorOnValueChange(csvFilePath, column,
					colorString);
			ExcelWorkbook.writeWorkbook(xlsFilePath, contents, sheetName);
		}

	}

}
