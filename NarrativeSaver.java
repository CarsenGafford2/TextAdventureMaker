/**
 * Saves the narrative text from the editor context to the current story node.
 * 
 * @author Carsen Gafford
 */
class NarrativeSaver {
    void save(EditorContext context) {
        if (context.currentNode != null) {
            context.currentNode.narrativeText = context.narrativeArea.getText();
        }
    }
}
