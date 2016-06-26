package path;

import java.util.Scanner;

import de.homelab.madgaksha.cutscenesystem.provider.FileCutsceneProvider;

public enum EPath {
	PATH_LINE {
		@Override
		public APath readNextObject(float tmax, boolean relative, float scaleX, float scaleY, Scanner s) {
			Float x = FileCutsceneProvider.nextNumber(s);
			Float y = FileCutsceneProvider.nextNumber(s);
			if (x == null || y == null)
				return null;
			return new PathLine(tmax, relative, scaleX * x, scaleY * y);
		}
	};

	/**
	 * Reads the next path from the scanner.
	 * 
	 * @param tmax
	 *            End time.
	 * @param relative
	 *            Whether coordinates are relative.
	 * @param s
	 *            Scanner to read from.
	 * @return The path, or null if it could not be read.
	 */
	public abstract APath readNextObject(float tmax, boolean relative, float scaleX, float scaleY, Scanner s);
}
