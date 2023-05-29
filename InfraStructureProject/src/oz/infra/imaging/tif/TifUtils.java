package oz.infra.imaging.tif;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFEncodeParam;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;

public class TifUtils {
	public static final String TIFF = "tiff";
	private static Logger logger = JulUtils.getLogger();

	public static void combineToMultiPageTif(final String inputFolder, final String outputPath) {
		File inputFolderFile = new File(inputFolder);
		File[] tifFilesArray = inputFolderFile.listFiles();
		combineToMultiPageTif(tifFilesArray, outputPath);
	}

	public static void combineToMultiPageTif(final File[] tifFilesArray, final String outputPath) {
		logger.info("files are " + Arrays.toString(tifFilesArray));
		int numberOfFiles = tifFilesArray.length;
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		try {
			for (int i = 0; i < numberOfFiles; i++) {
				SeekableStream ss = new FileSeekableStream(tifFilesArray[i]);
				com.sun.media.jai.codec.ImageDecoder decoder = ImageCodec.createImageDecoder(TIFF,
						ss, null);
				int numPages = decoder.getNumPages();
				for (int j = 0; j < numPages; j++) {
					PlanarImage op = new NullOpImage(decoder.decodeAsRenderedImage(j), null, null,
							OpImage.OP_IO_BOUND);
					images.add(op.getAsBufferedImage());
				}
			}
			TIFFEncodeParam tiffEncodeParam = new TIFFEncodeParam();
			tiffEncodeParam.setCompression(TIFFEncodeParam.COMPRESSION_PACKBITS);
			// tiffEncodeParam.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE);
			OutputStream out = new FileOutputStream(outputPath);
			ImageEncoder encoder = ImageCodec.createImageEncoder(TIFF, out, tiffEncodeParam);
			List<BufferedImage> imageList = new ArrayList<BufferedImage>();
			for (int i = 1; i < images.size(); i++) {
				imageList.add(images.get(i));
			}
			tiffEncodeParam.setExtraImages(imageList.iterator());
			encoder.encode(images.get(0));
			out.close();
			File outputFile = new File(outputPath);
			logger.info(outputFile.getAbsolutePath() + " has been successfully created. Length: "
					+ String.valueOf(outputFile.length()));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}
}