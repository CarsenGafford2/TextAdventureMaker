import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class NodeSelectionHandler implements ListSelectionListener {
    private final EditorContext context;
    private final NarrativeSaver saver;
    private final ChoicesUIRefresher refresher;

    NodeSelectionHandler(EditorContext context, NarrativeSaver saver, ChoicesUIRefresher refresher) {
        this.context = context;
        this.saver = saver;
        this.refresher = refresher;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        saver.save(context);
        context.currentNode = context.nodeList.getSelectedValue();
        if (context.currentNode == null) {
            context.idField.setText("");
            context.narrativeArea.setText("");
            context.currentChoices = null;
        } else {
            context.idField.setText(context.currentNode.id);
            context.narrativeArea.setText(context.currentNode.narrativeText);
            context.currentChoices = context.currentNode.choices;
        }
        refresher.refresh(context);
    }
}
