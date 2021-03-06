package de.homelab.madgaksha.lotsofbs.desktop;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;

/**
 * Launches a class's main method as a new process.
 * http://stackoverflow.com/a/723914/3925216
 *
 * I added some logic for passing data to the new process via stdin.
 * 
 * @author hallidave
 *
 */
public final class JavaProcess {

	private JavaProcess() {
	}

	/**
	 * Starts the main method of the given class as a new process and writes the
	 * given data to stdin. Blocks until the process finishes.
	 * 
	 * @param c
	 *            Class to launch.
	 * @param data
	 *            Data to pass via stdin.
	 * @return Exit code.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Process exec(Class<?> c, byte[] data) throws IOException, InterruptedException {
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classpath = System.getProperty("java.class.path");
		String className = c.getCanonicalName();

		ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath,
				"-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005", className);

		// Make sure we receive console output.
		builder.redirectOutput(Redirect.INHERIT);
		builder.redirectError(Redirect.INHERIT);
		Process process = builder.start();

		// Send data to the process.
		if (data != null) {
			final OutputStream stdin = process.getOutputStream();
			stdin.write(data);
			stdin.close();
		}

		return process;
	}

}