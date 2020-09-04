package it.eng.cobo.consolepec.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

/**
 * 
 * @author biagiot
 * 
 */
public class ZipUtils {

	public static void zipFolder(final Path sourceFolderPath, Path zipPath) throws Exception {

		final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));

		Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
				Files.copy(file, zos);
				zos.closeEntry();
				return FileVisitResult.CONTINUE;
			}
		});

		zos.close();
	}

	/**
	 * Estrae nella directory outputFolder i file presenti all'interno dello zip
	 *
	 * @param zipFile
	 * @param outputFolder
	 * @throws IOException
	 */
	public static void unZip(File zipFile, File outputFolder) throws IOException {
		FileInputStream fis = new FileInputStream(zipFile);
		unZip(fis, outputFolder);
		if (fis != null)
			fis.close();
	}

	public static void unZip(InputStream zipStream, File outputFolder) throws IOException {
		unZip(zipStream, outputFolder, StandardCharsets.UTF_8);
	}

	public static void unZip(InputStream zipStream, File outputFolder, Charset charset) throws IOException {
		byte[] buffer = new byte[1024];
		if (!outputFolder.exists())
			outputFolder.mkdir();

		ZipInputStream zis = new ZipInputStream(zipStream, charset);
		ZipEntry entry = zis.getNextEntry();

		while (entry != null) {
			if (entry.isDirectory()) {
				new File(outputFolder.getAbsolutePath() + File.separator + entry.getName()).mkdir();
			} else {
				FileOutputStream fos = new FileOutputStream(new File(outputFolder.getAbsolutePath() + File.separator + entry.getName()));
				int len;
				while ((len = zis.read(buffer)) > 0)
					fos.write(buffer, 0, len);
				fos.close();
			}
			entry = zis.getNextEntry();
		}

		zis.closeEntry();
		// zis.close();
	}

	/**
	 * Estrae nella directory outputFolder i file presenti all'interno dello zip con le librerie di apache commons-io
	 *
	 * @param zipFile
	 * @param outputFolder
	 * @throws IOException
	 */
	public static void unZipApache(File zipFile, File outputFolder) throws IOException {
		ZipFile compressedFile = new ZipFile(zipFile.getAbsolutePath());
		Enumeration<? extends ZipEntry> entries = compressedFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File entryDestination = new File(outputFolder, entry.getName());
			if (entry.isDirectory()) {
				entryDestination.mkdirs();
			} else {
				entryDestination.getParentFile().mkdirs();
				InputStream in = compressedFile.getInputStream(entry);
				OutputStream out = new FileOutputStream(entryDestination);
				IOUtils.copy(in, out);

				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception e) {}

				out.close();
			}
		}
		compressedFile.close();
	}

	/**
	 * Estrae nella directory outputFolder i file presenti all'interno del rar
	 *
	 * @param rarFile
	 * @param outputFolder
	 * @throws RarException
	 * @throws IOException
	 */
	public static void unRar(File rarFile, File outDir) throws IOException {
		Archive archive = null;
		FileOutputStream os = null;

		try {
			if (!outDir.exists())
				outDir.mkdir();

			String mainFolder = outDir.getAbsolutePath();
			try {
				archive = new Archive(new FileVolumeManager(rarFile));
			} catch (RarException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
			if (archive != null) {
				archive.getMainHeader().print();
				FileHeader fh = archive.nextFileHeader();
				while (fh != null) {
					try {
						String[] split = fh.getFileNameString().trim().replaceAll("\\\\", "/").split("/");
						for (int i = 0; i < split.length - 1; i++) {
							if (!(split[i].contains(".")))
								mainFolder = mainFolder + "/" + split[i];
						}

						if (fh.getDataSize() == 0)
							mainFolder = mainFolder + "/" + split[split.length - 1];

						File folder = new File(mainFolder);
						if (!folder.exists())
							folder.mkdirs();

						if (split[split.length - 1].contains(".")) {
							File out = new File(folder.getAbsolutePath(), split[split.length - 1]);
							os = new FileOutputStream(out);
							try {
								archive.extractFile(fh, os);
							} catch (RarException e) {
								throw new RuntimeException(e);
							}
							os.close();
						}
						fh = archive.nextFileHeader();
					} finally {
						if (os != null)
							os.close();
					}
				}
			}
		} finally {
			if (archive != null)
				archive.close();
		}
	}
}
