import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * Holds the context of the editor, including references to UI components and the current story state.
 * 
 * @author Carsen Gafford
 */
class EditorContext {
    JFrame frame;
    Map<String, StoryNode> nodeMap;
    DefaultListModel<StoryNode> listModel;
    JList<StoryNode> nodeList;
    JTextField idField;
    JTextArea narrativeArea;
    JPanel choicesPanel;
    StoryNode currentNode;
    List<Choice> currentChoices;
}
