import javax.swing.*;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.Base64;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Exports the current story as a runnable JAR file.
 * 
 * @author Carsen Gafford
 */
class GameExporter implements java.awt.event.ActionListener {
    private final EditorContext context;
    private final RunnerCodeGenerator generator = new RunnerCodeGenerator();
    private final JarFileAdder jarFileAdder = new JarFileAdder();

    GameExporter(EditorContext context) { this.context = context; }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game as JAR");
        fileChooser.setSelectedFile(new File("MyAdventure.jar"));

        if (fileChooser.showSaveDialog(context.frame) == JFileChooser.APPROVE_OPTION) {
            File jarFile = fileChooser.getSelectedFile();
            if (!jarFile.getName().toLowerCase().endsWith(".jar")) {
                jarFile = new File(jarFile.getParentFile(), jarFile.getName() + ".jar");
            }

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(context.nodeMap);
                oos.close();
                String base64Data = Base64.getEncoder().encodeToString(baos.toByteArray());

                String sourceCode = generator.generate(base64Data);

                File parentDir = jarFile.getParentFile();
                File sourceFile = new File(parentDir, "GameRunner.java");
                try (PrintWriter out = new PrintWriter(sourceFile)) { out.println(sourceCode); }

                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                if (compiler == null) {
                    JOptionPane.showMessageDialog(context.frame, "Error: Java Compiler not found.\nPlease run this program using a JDK (not just a JRE).");
                    return;
                }

                int compilationResult = compiler.run(null, null, null, sourceFile.getPath());
                if (compilationResult != 0) {
                    JOptionPane.showMessageDialog(context.frame, "Compilation failed.");
                    return;
                }

                Manifest manifest = new Manifest();
                manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
                manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, "GameRunner");

                try (JarOutputStream target = new JarOutputStream(new FileOutputStream(jarFile), manifest)) {
                    jarFileAdder.add(target, new File(parentDir, "GameRunner.class"));
                    jarFileAdder.add(target, new File(parentDir, "GameRunner$StoryNode.class"));
                    jarFileAdder.add(target, new File(parentDir, "GameRunner$Choice.class"));
                }

                sourceFile.delete();
                new File(parentDir, "GameRunner.class").delete();
                new File(parentDir, "GameRunner$StoryNode.class").delete();
                new File(parentDir, "GameRunner$Choice.class").delete();

                JOptionPane.showMessageDialog(context.frame, "Export Successful!\nCreated: " + jarFile.getName() + "\nRun it with: java -jar " + jarFile.getName());

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(context.frame, "Export failed: " + ex.getMessage());
            }
        }
    }
}
