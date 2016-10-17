package de.homelab.madgaksha.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;

/**
 * Implements the {@link Serializer#deserialize(Path)} method.
 * @author madgaksha
 * @param <T> Object that can be serialized/deserialized.
 */
public abstract class AbstractSerializer<T> implements Serializer<T> {
	@Override
	public T deserialize(final Path path) throws IOException {
		InputStream is = null;
		BufferedInputStream bis = null;
		ObjectInputStream dis = null;
		IOException exception = null;
		T object = null;
		try {
			is = Files.newInputStream(path, StandardOpenOption.READ);
			bis = new BufferedInputStream(is);
			dis = new ObjectInputStream(bis);
			try {
				object = deserialize(dis);
			}
			catch (final IOException e) {
				exception = e;
			}
		}
		finally {
			IOUtils.closeQuietly(dis);
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(is);
		}
		if (object == null) throw exception == null ? new IOException("Failed to deserialize object from stream") : exception;
		return object;
	}

	@Override
	public T deserialize(final byte[] bytes) throws IOException {
		ByteArrayInputStream bis = null;
		ObjectInputStream dis = null;
		T object = null;
		IOException exception = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			dis = new ObjectInputStream(bis);
			try {
				object = deserialize(dis);
			}
			catch (final IOException e) {
				exception = e;
			}
		}
		finally {
			IOUtils.closeQuietly(dis);
			IOUtils.closeQuietly(bis);
		}
		if (object == null) throw exception == null ? new IOException("Failed to deserialize object from byte array.") : exception;
		return object;
	}

	@Override
	public void serialize(final Path path) throws IOException {
		OutputStream os = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream dos = null;
		IOException exception = null;
		try {
			os = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			bos = new BufferedOutputStream(os);
			dos = new ObjectOutputStream(bos);
			serialize(dos);
		}
		catch (final IOException e) {
			exception = e;
		}
		finally {
			IOUtils.closeQuietly(dos);
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(os);
		}
		if (exception != null) throw exception;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream dos = null;
		IOException exception = null;
		try {
			bos = new ByteArrayOutputStream();
			dos = new ObjectOutputStream(bos);
			serialize(dos);
		}
		catch (final IOException e) {
			exception = e;
		}
		finally {
			IOUtils.closeQuietly(dos);
			IOUtils.closeQuietly(bos);
		}
		if (exception != null) throw exception;
		return bos.toByteArray();
	}
}
