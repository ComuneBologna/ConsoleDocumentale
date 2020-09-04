package it.eng.cobo.consolepec.util.file;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author GiacomoFM
 * @since 19/mar/2019
 */
public class FileUtilTest {

	@Test
	public void sequentialFileNameVoid() {
		assertNull("Controllo su null", FileUtil.createSequentialFileName(null, 1));
		assertTrue("Controllo su empty", "".equals(FileUtil.createSequentialFileName("", 1)));
	}

	@Test
	public void sequentialFileName1() {
		assertTrue("file.txt".equals(FileUtil.createSequentialFileName("file.txt", 0)));
		assertTrue("file (2).txt".equals(FileUtil.createSequentialFileName("file.txt", 1)));
		assertTrue("file (100).txt".equals(FileUtil.createSequentialFileName("file.txt", 99)));
	}

	@Test
	public void sequentialFileName2() {
		assertTrue("file. (2)".equals(FileUtil.createSequentialFileName("file.", 1)));
		assertTrue(".file (2)".equals(FileUtil.createSequentialFileName(".file", 1)));
		assertTrue("file (2)".equals(FileUtil.createSequentialFileName("file", 1)));
	}

	@Test
	public void originalFileNameVoid() {
		assertNull("Controllo su null", FileUtil.recoverOriginalFileName(null));
		assertTrue("Controllo su empty", "".equals(FileUtil.recoverOriginalFileName("")));
	}

	@Test
	public void originalFileName1() {
		assertTrue("file.txt".equals(FileUtil.recoverOriginalFileName("file.txt")));
		assertTrue("file.txt".equals(FileUtil.recoverOriginalFileName("file (2).txt")));
		assertTrue("file.txt".equals(FileUtil.recoverOriginalFileName("file (10000).txt")));
	}

	@Test
	public void originalFileName2() {
		assertTrue("file.".equals(FileUtil.recoverOriginalFileName("file. (2)")));
		assertTrue(".file".equals(FileUtil.recoverOriginalFileName(".file (2)")));
		assertTrue("file".equals(FileUtil.recoverOriginalFileName("file (2)")));
	}

}
