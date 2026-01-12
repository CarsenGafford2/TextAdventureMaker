import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class ChoiceDescriptionSaver extends FocusAdapter {
    private final Choice choice;

    ChoiceDescriptionSaver(Choice choice) { this.choice = choice; }

    @Override
    public void focusLost(FocusEvent e) {
        choice.description = ((javax.swing.JTextField) e.getComponent()).getText();
    }
}
