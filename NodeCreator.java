class NodeCreator {
    private final StoryNodeFactory factory;

    NodeCreator(StoryNodeFactory factory) { this.factory = factory; }

    StoryNode create(EditorContext context, String id) {
        if (context.nodeMap.containsKey(id)) return context.nodeMap.get(id);
        StoryNode node = factory.create(id);
        context.nodeMap.put(id, node);
        context.listModel.addElement(node);
        context.nodeList.setSelectedValue(node, true);
        return node;
    }
}
