package GUI.Util;

import org.apache.log4j.Logger;
import java.io.*;

public class NativeLoader {

    public static final Logger LOG = Logger.getLogger(NativeLoader.class);
    public static String libraryName = "unknown library";

    public NativeLoader() {
    }

    public void loadLibrary() {
        try {
            System.load(saveLibrary());
        } catch (IOException e) {
            LOG.warn("Could not find library " + libraryName +
                    " as resource, trying fallback lookup through System.loadLibrary");
        }
    }


    private String getOSSpecificLibraryName() {
        String osArch = System.getProperty("os.arch");
        String osName = System.getProperty("os.name").toLowerCase();
        String name;
        String path = "sigar-bin/slib/";
        boolean isLinux = true;
        if (osName.startsWith("win")) {
            isLinux = false;
            if (osArch.equalsIgnoreCase("x86")) {
                name = "sigar-x86-winnt.dll";
            } else if (osArch.equalsIgnoreCase("amd64")){
                name = "sigar-amd64-winnt.dll";
            }else{
                throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
            }
        } else if (osName.startsWith("linux")) {
            if (osArch.equalsIgnoreCase("amd64")) {
                name = "libsigar-amd64-linux.so";
            } else if (osArch.equalsIgnoreCase("ia64")) {
                name = "libsigar-ia64-linux.so";
            } else if (osArch.equalsIgnoreCase("i386")) {
                name = "libsigar-x86-linux.so";
            } else {
                throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
            }
        } else {
            throw new UnsupportedOperationException("Platform " + osName + ":" + osArch + " not supported");
        }
        libraryName = name.split("\\.")[0];
        String currentLibPath = System.getProperty("java.library.path");
        if (isLinux){
            System.setProperty("java.library.path", currentLibPath + ":" + this.getClass().getClassLoader().getResource(path).getPath());
        }else{
            System.setProperty("java.library.path", currentLibPath + ";" + this.getClass().getClassLoader().getResource(path).getPath());
        }
        return path + name;
    }

    private String saveLibrary() throws IOException {
        InputStream in = null;
        OutputStream out = null;

        try {
            String libraryName = getOSSpecificLibraryName();
            in = this.getClass().getClassLoader().getResourceAsStream(libraryName);
            String tmpDirName = System.getProperty("java.io.tmpdir");
            File tmpDir = new File(tmpDirName);
            if (!tmpDir.exists()) {
                tmpDir.mkdir();
            }
            File file = File.createTempFile(this.libraryName + "-", ".tmp", tmpDir);
            // Clean up the file when exiting
            file.deleteOnExit();
            out = new FileOutputStream(file);

            int cnt;
            byte buf[] = new byte[16 * 1024];
            // copy until done.
            while ((cnt = in.read(buf)) >= 1) {
                out.write(buf, 0, cnt);
            }
            LOG.info("Saved libfile: " + file.getAbsoluteFile());
            return file.getAbsolutePath();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}