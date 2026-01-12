import javax.swing.*;
import java.awt.*;

/**
 * Builds a UI row for a choice in the editor.
 * 
 * @author Carsen Gafford
 */
class ChoiceRowBuilder {
    JPanel build(EditorContext context, Choice choice, ChoicesUIRefresher refresher) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBorder(new javax.swing.border.CompoundBorder(
            new ModernTheme.RoundedBorder(ModernTheme.BORDER, ModernTheme.BORDER_RADIUS),
            new javax.swing.border.EmptyBorder(8, 12, 8, 12)
        ));
        ModernTheme.stylePanel(row);
        
        JTextField descField = new JTextField(choice.description);
        descField.addFocusListener(new ChoiceDescriptionSaver(choice));
        ModernTheme.styleTextField(descField);

        JComboBox<String> targetBox = new JComboBox<>(context.nodeMap.keySet().toArray(new String[0]));
        targetBox.setSelectedItem(choice.targetNodeId);
        targetBox.addActionListener(new ChoiceTargetUpdater(choice));
        ModernTheme.styleComboBox(targetBox);

        JButton deleteButton = new JButton("X");
        deleteButton.addActionListener(new ChoiceDeleteAction(context, choice, refresher));
        ModernTheme.styleButton(deleteButton);

        JLabel textLabel = new JLabel("Text: ");
        ModernTheme.styleLabel(textLabel);
        row.add(textLabel, BorderLayout.WEST);
        row.add(descField, BorderLayout.CENTER);
        
        JPanel right = new JPanel(new GridLayout(1, 2, 8, 0));
        ModernTheme.stylePanel(right);
        right.add(targetBox);
        right.add(deleteButton);
        row.add(right, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return row;
    }
}
