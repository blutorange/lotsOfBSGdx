package de.homelab.madgaksha.desktop;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Launches a class's main method as a new process.
 * http://stackoverflow.com/a/723914/3925216
 *
 * I added some logic for passing data to the new process via
 * stdin.
 *  
 * @author hallidave
 *
 */
public final class JavaProcess {

    private JavaProcess() {}        

    /**
     * Starts the main method of the given class as a new process
     * and writes the given data to stdin.
     * Blocks until the process finishes.
     * @param c Class to launch.
     * @param data Data to pass via stdin.
     * @return Exit code.
     * @throws IOException 
     * @throws InterruptedException
     */
    public static Process exec(Class<?> c, byte[] data) throws IOException,
                                               InterruptedException {
        final String javaHome = System.getProperty("java.home");
        final String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        final String classpath = System.getProperty("java.class.path");
        final String className = c.getCanonicalName();

        final ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classpath, className);
        
        final Process process = builder.start();
        
        // Send data to the process.
        if (data != null) {
        	final OutputStream stdin = process.getOutputStream();
        	stdin.write(data);
        }

        return process;
    }
    
}