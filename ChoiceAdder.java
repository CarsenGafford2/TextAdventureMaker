import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Adds a new choice to the current story node when triggered.
 * Uses ChoiceFactory to create the choice and ChoicesUIRefresher to update the UI.
 * Requires a valid currentNode in the EditorContext.
 * 
 * @author Carsen Gafford
 */
class ChoiceAdder implements ActionListener {
    private final EditorContext context;
    private final ChoiceFactory factory;
    private final ChoicesUIRefresher refresher;

    ChoiceAdder(EditorContext context, ChoiceFactory factory, ChoicesUIRefresher refresher) {
        this.context = context;
        this.factory = factory;
        this.refresher = refresher;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (context.currentNode != null) {
            Choice choice = factory.create("New Choice", "START");
            context.currentNode.choices.add(choice);
            context.currentChoices = context.currentNode.choices;
            refresher.refresh(context);
        }
    }
}
