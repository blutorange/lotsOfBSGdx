package de.homelab.madgaksha.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public interface Serializer<T> {
	public void serialize(ObjectOutputStream stream) throws IOException;
	public byte[] serialize() throws IOException;
	public void serialize(Path path) throws IOException;
	public T deserialize(ObjectInputStream stream) throws IOException;
	public T deserialize(Path path) throws IOException;
	public T deserialize(byte[] bytes) throws IOException;
}
