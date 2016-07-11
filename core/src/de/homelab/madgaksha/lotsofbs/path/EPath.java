package de.homelab.madgaksha.lotsofbs.path;

import java.util.Scanner;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.provider.FileCutsceneProvider;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * For deserializing paths.
 * 
 * @author madgaksha
 */
public enum EPath {
	PATH_LINE {
		@Override
		public APath readNextObject(float tmax, boolean relative, float scaleX, float scaleY, Scanner s) {
			Float x = FileCutsceneProvider.nextNumber(s);
			Float y = FileCutsceneProvider.nextNumber(s);
			if (x == null || y == null) {
				LOG.error("line: expected position");
				return null;
			}
			return new PathLine(tmax, relative, scaleX * x, scaleY * y);
		}
	},

	PATH_PARABOLA {
		@Override
		public APath readNextObject(float tmax, boolean relative, float scaleX, float scaleY, Scanner s) {
			Float tau = FileCutsceneProvider.nextNumber(s);
			if (tau == null) {
				LOG.error("parabola: expected tau");
				return null;
			}

			Float x1 = FileCutsceneProvider.nextNumber(s);
			Float y1 = FileCutsceneProvider.nextNumber(s);
			if (x1 == null || y1 == null) {
				LOG.error("parabola: expected middle point");
				return null;
			}

			Float x2 = FileCutsceneProvider.nextNumber(s);
			Float y2 = FileCutsceneProvider.nextNumber(s);
			if (x2 == null || y2 == null) {
				LOG.error("parabola: expected end point");
				return null;
			}

			return new PathParabola(tmax, relative, tau, x1 * scaleX, y1 * scaleY, x2 * scaleX, y2 * scaleY);
		}
	};

	private final static Logger LOG = Logger.getLogger(EPath.class);

	/**
	 * Reads the next path from the scanner.
	 * 
	 * @param tmax
	 *            End time.
	 * @param relative
	 *            Whether coordinates are relative.
	 * @param scaleX
	 *            Scale factor by which coordinates must be scale.
	 * @param scaleY
	 *            Scale factor by which coordinates must be scale
	 * @param s
	 *            Scanner to read from.
	 * @return The path, or null if it could not be read.
	 */
	public abstract APath readNextObject(float tmax, boolean relative, float scaleX, float scaleY, Scanner s);
}
