import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class MainClass {
	private final static int BUFFER_LENGTH = 100000;

	public static void main(String[] args) throws Exception {
		String[] strAr = { "1", "xxx", "6yhn", "gfdsgfdsgfsfd" };
		for (String str1 : strAr) {
			System.out.println(str1);
		}
		System.exit(0);
		String filePath = "C:\\temp\\test.txt";
		File aFile = new File(filePath);
		FileInputStream inFile = null;

		inFile = new FileInputStream(aFile);

		FileChannel inChannel = inFile.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(BUFFER_LENGTH);

		while (inChannel.read(buf) != -1) {
			char ch = ((ByteBuffer) (buf.flip())).asCharBuffer().get(0);
			System.out.println(ch);
			buf.clear();
		}
		inFile.close();
	}
}