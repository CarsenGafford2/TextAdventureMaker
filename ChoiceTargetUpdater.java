import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Action listener to update the target node of a choice when selection changes.
 * 
 * @author Carsen Gafford
 */
class ChoiceTargetUpdater implements ActionListener {
    private final Choice choice;

    ChoiceTargetUpdater(Choice choice) { this.choice = choice; }

    @Override
    public void actionPerformed(ActionEvent e) {
        choice.targetNodeId = (String) ((JComboBox<?>) e.getSource()).getSelectedItem();
    }
}
