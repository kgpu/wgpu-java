package com.noahcharlton.wgpuj.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.zip.CRC32;

/**
 * Loads shared libraries from a natives jar file (desktop) or arm folders (Android). For desktop projects, have the natives jar
 * in the classpath, for Android projects put the shared libraries in the libs/armeabi and libs/armeabi-v7a folders.
 *
 * @author mzechner
 * @author Nathan Sweet
 */
public class SharedLibraryLoader {

    static public boolean isWindows = System.getProperty("os.name").contains("Windows");
    static public boolean isLinux = System.getProperty("os.name").contains("Linux");
    static public boolean isMac = System.getProperty("os.name").contains("Mac");
    static public boolean isARM = System.getProperty("os.arch").startsWith("arm");

    // JDK 8 only.
    private static String abi = (System.getProperty("sun.arch.abi") != null ? System.getProperty("sun.arch.abi") : "");

    static private final HashSet<String> loadedLibraries = new HashSet<>();

    /**
     * Returns a CRC of the remaining bytes in the stream.
     */
    private String crc(InputStream input) {
        if(input == null) throw new IllegalArgumentException("input cannot be null.");
        CRC32 crc = new CRC32();
        byte[] buffer = new byte[4096];
        try {
            while(true) {
                int length = input.read(buffer);
                if(length == -1) break;
                crc.update(buffer, 0, length);
            }
        } catch(Exception ex) {
        } finally {
            closeQuietly(input);
        }
        return Long.toString(crc.getValue(), 16);
    }

    /**
     * Maps a platform independent library name to a platform dependent name.
     */
    private String mapLibraryName(String libraryName) {
        if(isWindows) return libraryName + ".dll";
        if(isLinux) return "lib" + libraryName + ".so";
        if(isMac) return "lib" + libraryName + ".dylib";

        return libraryName;
    }

    /**
     * Loads a shared library for the platform the application is running on.
     *
     * @param libraryName The platform independent library name. If not contain a prefix (eg lib) or suffix (eg .dll).
     */
    public void load(String libraryName) {
        synchronized(SharedLibraryLoader.class) {
            if(isLoaded(libraryName)) return;
            String platformName = mapLibraryName(libraryName);
            try {
                loadFile(platformName);
                setLoaded(libraryName);
            } catch(Throwable ex) {
                throw new RuntimeException("Couldn't load shared library '" + platformName + "' for target: "
                        + System.getProperty("os.name"), ex);
            }
        }
    }

    private InputStream readFile(String path) {
            InputStream input = SharedLibraryLoader.class.getResourceAsStream("/" + path);
            if(input == null) throw new RuntimeException("Unable to read file for extraction: " + path);
            return input;

    }

    private File extractFile(String sourcePath, String sourceCrc, File extractedFile) throws IOException {
        String extractedCrc = null;
        if(extractedFile.exists()) {
            try {
                extractedCrc = crc(new FileInputStream(extractedFile));
            } catch(FileNotFoundException ignored) {
            }
        }

        // If file doesn't exist or the CRC doesn't match, extract it to the temp dir.
        if(extractedCrc == null || !extractedCrc.equals(sourceCrc)) {
            InputStream input = null;
            FileOutputStream output = null;
            try {
                input = readFile(sourcePath);
                extractedFile.getParentFile().mkdirs();
                output = new FileOutputStream(extractedFile);
                byte[] buffer = new byte[4096];
                while(true) {
                    int length = input.read(buffer);
                    if(length == -1) break;
                    output.write(buffer, 0, length);
                }
            } catch(IOException ex) {
                throw new RuntimeException("Error extracting file: " + sourcePath + "\nTo: " + extractedFile.getAbsolutePath(),
                        ex);
            } finally {
                closeQuietly(input);
                closeQuietly(output);
            }
        }

        return extractedFile;
    }

    private void closeQuietly(Closeable closeable){
        if(closeable == null)
            return;

        try{
            closeable.close();
        } catch(IOException e) {
            System.err.println("Failed to close dll: " + e);
        }
    }

    /**
     * Extracts the source file and calls System.load. Attemps to extract and load from multiple locations. Throws runtime
     * exception if all fail.
     */
    private void loadFile(String sourcePath) {
        String sourceCrc = crc(readFile(sourcePath));

        String fileName = new File(sourcePath).getName();

        // Temp directory with username in path.
        File file = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + sourceCrc,
                fileName);
        Throwable ex = loadFile(sourcePath, sourceCrc, file);
        if(ex == null) return;

        // System provided temp directory.
        try {
            file = File.createTempFile(sourceCrc, null);
            if(file.delete() && loadFile(sourcePath, sourceCrc, file) == null) return;
        } catch(Throwable ignored) {
        }

        // User home.
        file = new File(System.getProperty("user.home") + "/.libgdx/" + sourceCrc, fileName);
        if(loadFile(sourcePath, sourceCrc, file) == null) return;

        // Relative directory.
        file = new File(".temp/" + sourceCrc, fileName);
        if(loadFile(sourcePath, sourceCrc, file) == null) return;

        // Fallback to java.library.path location, eg for applets.
        file = new File(System.getProperty("java.library.path"), sourcePath);
        if(file.exists()) {
            System.load(file.getAbsolutePath());
            return;
        }

        throw new RuntimeException(ex);
    }

    private Throwable loadFile(String sourcePath, String sourceCrc, File extractedFile) {
        try {
            System.load(extractFile(sourcePath, sourceCrc, extractedFile).getAbsolutePath());
            return null;
        } catch(Throwable ex) {
            return ex;
        }
    }

    private static synchronized void setLoaded(String libraryName) {
        loadedLibraries.add(libraryName);
    }

    private static synchronized boolean isLoaded(String libraryName) {
        return loadedLibraries.contains(libraryName);
    }
}
