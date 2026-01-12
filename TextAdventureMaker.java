import javax.swing.*;

/**
 * Main class to launch the Text Adventure Maker application.
 * 
 * @author Carsen Gafford
 */
public class TextAdventureMaker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new EditorFrame().setVisible(true);
        });
    }
}