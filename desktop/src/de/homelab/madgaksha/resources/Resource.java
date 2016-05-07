package de.homelab.madgaksha.resources;

	

import java.net.URL;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.homelab.madgaksha.logging.LoggerFactory;

public class Resource {
	
	public static final Logger LOG = LoggerFactory.getLogger(Resource.class);
	
	public enum EIcon implements IResource {
		DIALOG_CLOSE("iconConfirmClose.gif");
		private final static int LIMIT = 20;
		private final URL url;
		private EIcon(String file) {
			url = getResourceURL("icon", file);
		}
		public URL getUrl() {
			return url;
		}
		public EIcon getEnum() {
			return this;
		}
		public int getLimit() {
			return LIMIT;
		}
		public Icon getObject() {
			return new ImageIcon(this.getUrl());
		}
	}

	private static URL getResourceURL(String type, String file) {
		final String path = new StringBuilder(1+type.length()+file.length()).append(type).append('/').append(file).toString();
		final URL url = ResourceLoader.class.getResource(path);
		return url;
	}
}

