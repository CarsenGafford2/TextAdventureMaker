import javax.swing.*;
import java.awt.*;

class StoryNodeCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String text = value instanceof StoryNode node ? node.id : String.valueOf(value);
        return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
    }
}
