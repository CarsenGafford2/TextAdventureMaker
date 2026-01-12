import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action listener to add a new story node with a unique ID.
 * 
 * @author Carsen Gafford
 */
class NodeAdder implements ActionListener {
    private final EditorContext context;
    private final NodeCreator creator;

    NodeAdder(EditorContext context, NodeCreator creator) {
        this.context = context;
        this.creator = creator;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = JOptionPane.showInputDialog(context.frame, "Enter Unique Node ID (e.g. 'forest_entry'):");
        if (id != null && !id.trim().isEmpty() && !context.nodeMap.containsKey(id.trim())) {
            creator.create(context, id.trim());
        }
    }
}
