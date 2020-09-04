package it.eng.cobo.consolepec.util.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

public class ConsoleIOUtils {

	public static void fastCopy(final InputStream src, final OutputStream dest) throws IOException {
		final ReadableByteChannel inputChannel = Channels.newChannel(src);
		final WritableByteChannel outputChannel = Channels.newChannel(dest);
		fastCopy(inputChannel, outputChannel);
		inputChannel.close();
		outputChannel.close();
	}

	private static void fastCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

		while (src.read(buffer) != -1) {
			buffer.flip();
			dest.write(buffer);
			buffer.compact();
		}

		buffer.flip();

		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}

	public static void closeStreams(InputStream... streams) {

		for (InputStream stream : streams) {
			if (stream != null) {
				try {
					stream.close();

				} catch (IOException e) {}
			}
		}

	}

	public static void deleteParentFolder(File... files) {
		for (File file : files) {
			try {
				if (file != null) {
					FileUtils.deleteDirectory(file.getParentFile());
				}

			} catch (Exception e) {}
		}
	}

	public static void deleteFiles(File... files) {
		for (File file : files) {
			try {
				if (file != null && file.exists() && !file.isDirectory()) {
					Files.delete(file.toPath());
				}

			} catch (Exception e) {}
		}
	}

	public static void deleteFolder(File... files) {
		for (File file : files) {
			try {
				if (file != null && file.exists() && file.isDirectory()) {
					FileUtils.deleteDirectory(file);
				}

			} catch (Exception e) {}
		}
	}

	public static void closeStreams(OutputStream... streams) {

		for (OutputStream stream : streams) {
			if (stream != null) {
				try {
					stream.close();

				} catch (IOException e) {}
			}
		}
	}

	public static File createTempDirectory(String tmpFolder) throws IOException {
		Path path = Paths.get(tmpFolder + File.separator + UUID.randomUUID().toString());

		if (!Files.exists(path)) {
			Files.createDirectory(path);
		}

		return path.toFile();
	}

	public static File createTempDirectory(String tmpFolder, String dirName) throws IOException {
		Path path = Paths.get(tmpFolder + File.separator + UUID.randomUUID().toString() + File.separator + dirName);

		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}

		return path.toFile();
	}

	public static File createSubTempFile(String tmpFolder, String fileName) throws IOException {
		Path path = Paths.get(tmpFolder + File.separator + UUID.randomUUID().toString() + File.separator + fileName);

		if (!Files.exists(path.getParent())) {
			Files.createDirectory(path.getParent());
		}

		if (!Files.exists(path)) {
			Files.createFile(path);
		}

		return path.toFile();
	}

	public static File createTempFile(String tmpFolder, String fileName) throws IOException {
		Path path = Paths.get(tmpFolder + File.separator + fileName);

		if (!Files.exists(path.getParent())) {
			Files.createDirectory(path.getParent());
		}

		if (!Files.exists(path)) {
			Files.createFile(path);
		}

		return path.toFile();
	}

}
