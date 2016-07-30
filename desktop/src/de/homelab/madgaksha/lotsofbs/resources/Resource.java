package de.homelab.madgaksha.lotsofbs.resources;

import java.net.URL;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.homelab.madgaksha.lotsofbs.logging.LoggerFactory;

public class Resource {

	public static final Logger LOG = LoggerFactory.getLogger(Resource.class);

	public enum EIcon implements IResource {
		DIALOG_CLOSE("iconConfirmClose.gif"),
		LEVEL_01("level01.jpg");
		private final static int LIMIT = 20;
		private final URL url;

		private EIcon(String file) {
			url = getResourceURL("icon", file);
		}

		@Override
		public URL getUrl() {
			return url;
		}

		@Override
		public EIcon getEnum() {
			return this;
		}

		@Override
		public int getLimit() {
			return LIMIT;
		}

		@Override
		public Icon getObject() {
			return new ImageIcon(this.getUrl());
		}
	}

	private static URL getResourceURL(String type, String file) {
		final String path = new StringBuilder(1 + type.length() + file.length()).append(type).append('/').append(file)
				.toString();
		final URL url = ResourceLoader.class.getResource(path);
		return url;
	}
}
