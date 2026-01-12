import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Action listener to remove a selected story node from the editor context.
 * Prevents removal of the "START" node.
 * 
 * @author Carsen Gafford
 */
class NodeRemover implements ActionListener {
    private final EditorContext context;

    NodeRemover(EditorContext context) { this.context = context; }

    @Override
    public void actionPerformed(ActionEvent e) {
        StoryNode selected = context.nodeList.getSelectedValue();
        if (selected != null && !"START".equals(selected.id)) {
            context.nodeMap.remove(selected.id);
            context.listModel.removeElement(selected);
        } else {
            JOptionPane.showMessageDialog(context.frame, "Cannot remove START node or no node selected.");
        }
    }
}
