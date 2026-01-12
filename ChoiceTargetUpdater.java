import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class ChoiceTargetUpdater implements ActionListener {
    private final Choice choice;

    ChoiceTargetUpdater(Choice choice) { this.choice = choice; }

    @Override
    public void actionPerformed(ActionEvent e) {
        choice.targetNodeId = (String) ((JComboBox<?>) e.getSource()).getSelectedItem();
    }
}
