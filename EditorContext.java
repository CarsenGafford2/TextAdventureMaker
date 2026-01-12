import javax.swing.*;
import java.util.List;
import java.util.Map;

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
