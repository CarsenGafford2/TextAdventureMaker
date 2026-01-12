import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Focus listener to save the description of a choice when editing is finished.
 * 
 * @author Carsen Gafford
 */
class ChoiceDescriptionSaver extends FocusAdapter {
    private final Choice choice;

    ChoiceDescriptionSaver(Choice choice) { this.choice = choice; }

    @Override
    public void focusLost(FocusEvent e) {
        choice.description = ((javax.swing.JTextField) e.getComponent()).getText();
    }
}
