import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TestRunner implements ActionListener {
    private final EditorContext context;

    TestRunner(EditorContext context) { this.context = context; }

    @Override
    public void actionPerformed(ActionEvent e) {
        JDialog dialog = new JDialog(context.frame, "Test Run", true);
        dialog.setSize(600, 400);
        JTextArea consoleOut = new JTextArea();
        consoleOut.setEditable(false);
        consoleOut.setBackground(Color.BLACK);
        consoleOut.setForeground(Color.GREEN);
        consoleOut.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JTextField inputField = new JTextField();
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.GREEN);
        inputField.setCaretColor(Color.GREEN);
        dialog.add(new JScrollPane(consoleOut), BorderLayout.CENTER);
        dialog.add(inputField, BorderLayout.SOUTH);

        final StoryNode[] currentRuntimeNode = {context.nodeMap.get("START")};
        Runnable printNode = () -> {
            StoryNode node = currentRuntimeNode[0];
            consoleOut.append("\n--------------------------------\n");
            consoleOut.append(node.narrativeText + "\n\n");
            if (node.choices.isEmpty()) {
                consoleOut.append("[End of Story]\n");
                inputField.setEditable(false);
            } else {
                for (int i = 0; i < node.choices.size(); i++) {
                    consoleOut.append((i + 1) + ". " + node.choices.get(i).description + "\n");
                }
                consoleOut.append("\n> ");
            }
            consoleOut.setCaretPosition(consoleOut.getDocument().getLength());
        };

        inputField.addActionListener(ev -> {
            String input = inputField.getText().trim();
            inputField.setText("");
            consoleOut.append(input + "\n");
            try {
                int choiceIdx = Integer.parseInt(input) - 1;
                StoryNode node = currentRuntimeNode[0];
                if (choiceIdx >= 0 && choiceIdx < node.choices.size()) {
                    String targetId = node.choices.get(choiceIdx).targetNodeId;
                    if (context.nodeMap.containsKey(targetId)) {
                        currentRuntimeNode[0] = context.nodeMap.get(targetId);
                        printNode.run();
                    } else {
                        consoleOut.append("[Error: Node '" + targetId + "' not found]\n> ");
                    }
                } else {
                    consoleOut.append("Invalid choice.\n> ");
                }
            } catch (NumberFormatException ex) {
                consoleOut.append("Please enter a number.\n> ");
            }
        });

        printNode.run();
        dialog.setVisible(true);
    }
}
