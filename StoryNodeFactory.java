class StoryNodeFactory {
    StoryNode create(String id) {
        StoryNode node = new StoryNode();
        node.id = id;
        node.narrativeText = "Enter story text here...";
        return node;
    }
}
