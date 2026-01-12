import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Custom cell renderer for displaying StoryNode IDs in a JList.
 * 
 * @author Carsen Gafford
 */
class StoryNodeCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String text = value instanceof StoryNode node ? node.id : String.valueOf(value);
        JLabel label = (JLabel) super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        
        // Apply modern styling
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setBorder(new EmptyBorder(8, 12, 8, 12));
        
        if (isSelected) {
            label.setBackground(ModernTheme.ACCENT_PRIMARY);
            label.setForeground(ModernTheme.TEXT_PRIMARY);
        } else {
            label.setBackground(ModernTheme.BACKGROUND_SECONDARY);
            label.setForeground(ModernTheme.TEXT_PRIMARY);
        }
        
        label.setOpaque(true);
        return label;
    }
}
