import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ChoiceDeleteAction implements ActionListener {
    private final EditorContext context;
    private final Choice choice;
    private final ChoicesUIRefresher refresher;

    ChoiceDeleteAction(EditorContext context, Choice choice, ChoicesUIRefresher refresher) {
        this.context = context;
        this.choice = choice;
        this.refresher = refresher;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (context.currentChoices != null) {
            context.currentChoices.remove(choice);
            refresher.refresh(context);
        }
    }
}
