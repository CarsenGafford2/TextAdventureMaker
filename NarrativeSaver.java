class NarrativeSaver {
    void save(EditorContext context) {
        if (context.currentNode != null) {
            context.currentNode.narrativeText = context.narrativeArea.getText();
        }
    }
}
