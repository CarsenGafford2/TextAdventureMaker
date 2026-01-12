/**
 * Factory class for creating StoryNode instances with default values.
 * 
 * @author Carsen Gafford
 */
class StoryNodeFactory {
    StoryNode create(String id) {
        StoryNode node = new StoryNode();
        node.id = id;
        node.narrativeText = "Enter story text here...";
        return node;
    }
}
