import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * Utility class to add files to a JAR output stream.
 * 
 * @author Carsen Gafford
 */
class JarFileAdder {
    void add(JarOutputStream jos, File file) throws IOException {
        if (!file.exists()) return;
        JarEntry entry = new JarEntry(file.getName());
        entry.setTime(file.lastModified());
        jos.putNextEntry(entry);
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1) break;
                jos.write(buffer, 0, count);
            }
        }
        jos.closeEntry();
    }
}
