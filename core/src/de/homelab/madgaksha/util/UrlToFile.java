package de.homelab.madgaksha.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

public class UrlToFile {

	private static HashMap<URL,File> map = new HashMap<URL,File>();
	public static File convert(final URL url) {
		if (url.getProtocol().equalsIgnoreCase("file")) {
			return new File(url.getFile());
		}
		else if (map.containsKey(url)) {
			final File file = map.get(url);
			if (file.exists() && !file.isDirectory()) {
				return file;
			}
			else
				return downloadFile(url);
		}
		else {
			// Download the file.
			return downloadFile(url);
		}
	}
	public static File downloadFile(final URL url) {
		File file = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			file = File.createTempFile("url2file", ".data");
			if (file == null) return null;
			fos = new FileOutputStream(file);
			is = url.openStream();
			IOUtils.copy(is,fos);
		} catch (IOException e) {
			file = null;
		}
		finally {
			try {
				if(is!=null) is.close();
			}
			catch(IOException e){}
			try {
				if (fos!=null) fos.close();
			}
			catch(IOException e){}			
		}
		return file;
	}
}