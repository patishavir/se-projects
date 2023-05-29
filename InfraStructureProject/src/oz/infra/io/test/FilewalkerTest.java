package oz.infra.io.test;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.io.Filewalker;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class FilewalkerTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Filewalker fw = new Filewalker();
		List<File> list = fw.walk("C:\\temp\\logs115\\");
		ListUtils.getAsDelimitedString(list, SystemUtils.LINE_SEPARATOR, Level.INFO);
	}

}
