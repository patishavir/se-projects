package oz.infra.io.bom.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import oz.infra.io.bom.UnicodeBOMInputStream;

public final class UnicodeBOMInputStreamUsage {
	public static void main(final String[] args) throws Exception {
		String bomFilePath = "C:\\oj\\projects\\utilsProject\\args\\db\\runSqlScript\\sqlScriptsFolder2\\dbchanges_52810.sql";
		FileInputStream fis = new FileInputStream(bomFilePath);
		UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(fis);

		System.out.println("detected BOM: " + ubis.getBOM());

		System.out.print("Reading the content of the file without skipping the BOM: ");
		InputStreamReader isr = new InputStreamReader(ubis);
		BufferedReader br = new BufferedReader(isr);
		String line = br.readLine();
		System.out.println(line + "***** length: " + String.valueOf(line.length()));

		br.close();
		isr.close();
		ubis.close();
		fis.close();

		fis = new FileInputStream(bomFilePath);
		ubis = new UnicodeBOMInputStream(fis);
		isr = new InputStreamReader(ubis);
		br = new BufferedReader(isr);

		ubis.skipBOM();

		System.out.print("Reading the content of the file after skipping the BOM: ");
		line = br.readLine();
		System.out.println(line + "***** length: " + String.valueOf(line.length()));
		

		br.close();
		isr.close();
		ubis.close();
		fis.close();
	}
} // UnicodeBOMInputStreamUsage