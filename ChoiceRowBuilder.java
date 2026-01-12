import javax.swing.*;
import java.awt.*;

class ChoiceRowBuilder {
    JPanel build(EditorContext context, Choice choice, ChoicesUIRefresher refresher) {
        JPanel row = new JPanel(new BorderLayout(5, 0));
        JTextField descField = new JTextField(choice.description);
        descField.addFocusListener(new ChoiceDescriptionSaver(choice));

        JComboBox<String> targetBox = new JComboBox<>(context.nodeMap.keySet().toArray(new String[0]));
        targetBox.setSelectedItem(choice.targetNodeId);
        targetBox.addActionListener(new ChoiceTargetUpdater(choice));

        JButton deleteButton = new JButton("X");
        deleteButton.addActionListener(new ChoiceDeleteAction(context, choice, refresher));

        row.add(new JLabel("Text: "), BorderLayout.WEST);
        row.add(descField, BorderLayout.CENTER);
        JPanel right = new JPanel(new GridLayout(1, 2));
        right.add(targetBox);
        right.add(deleteButton);
        row.add(right, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return row;
    }
}
