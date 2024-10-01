import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class TextAdventureMaker {
    private static ArrayList<String> dialogues = new ArrayList<>();
    private static ArrayList<ArrayList<String>> choices = new ArrayList<>();
    private static ArrayList<ArrayList<String>> choiceDialogues = new ArrayList<>();
    private static JTextArea textArea;
    private static JTextArea gameOutputArea;
    private static int currentDialogueIndex = 0;

    public static void main(String[] args) {
        JFrame jframe = new JFrame("Text Adventure Maker");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(600, 500);
        jframe.setLayout(new BorderLayout());

        JMenuBar topMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        topMenuBar.add(fileMenu);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        gameOutputArea = new JTextArea();
        gameOutputArea.setEditable(false);
        JScrollPane gameScrollPane = new JScrollPane(gameOutputArea);
        
        JMenuBar bottomMenuBar = new JMenuBar();
        JButton dialogueButton = new JButton("Add Dialogue");
        JButton choiceButton = new JButton("Add Choices");
        JButton runGameButton = new JButton("Run Game");
        JButton removeLastButton = new JButton("Remove Last Entry");

        dialogueButton.addActionListener(e -> {
            String dialogue = JOptionPane.showInputDialog("Enter Dialogue:");
            if (dialogue != null && !dialogue.isEmpty()) {
                dialogues.add(dialogue);
                textArea.append("Dialogue: " + dialogue + "\n");
            }
        });

        choiceButton.addActionListener(e -> {
            String choiceInput = JOptionPane.showInputDialog("Enter Choices (separated by commas):");
            if (choiceInput != null && !choiceInput.isEmpty()) {
                String[] choiceArray = choiceInput.split(",");
                ArrayList<String> followUpDialogues = new ArrayList<>();

                for (String choice : choiceArray) {
                    String followUp = JOptionPane.showInputDialog("Enter follow-up dialogue for choice: " + choice.trim());
                    followUpDialogues.add(followUp);
                }

                if (!followUpDialogues.isEmpty()) {
                    ArrayList<String> choicesList = new ArrayList<>();
                    for (String choice : choiceArray) {
                        choicesList.add(choice.trim());
                    }
                    choices.add(choicesList);
                    choiceDialogues.add(followUpDialogues);
                    textArea.append("Choices added: " + String.join(", ", choicesList) + "\n");
                }
            }
        });

        runGameButton.addActionListener(e -> {
            currentDialogueIndex = 0; // Reset index for each run
            gameOutputArea.setText(""); // Clear previous output
            showNextDialogue();
        });

        removeLastButton.addActionListener(e -> removeLastEntry());

        saveItem.addActionListener(e -> saveGame());
        loadItem.addActionListener(e -> loadGame());

        bottomMenuBar.add(dialogueButton);
        bottomMenuBar.add(choiceButton);
        bottomMenuBar.add(runGameButton);
        bottomMenuBar.add(removeLastButton);

        jframe.getContentPane().add(topMenuBar, BorderLayout.NORTH);
        jframe.getContentPane().add(scrollPane, BorderLayout.WEST);
        jframe.getContentPane().add(gameScrollPane, BorderLayout.CENTER);
        jframe.getContentPane().add(bottomMenuBar, BorderLayout.SOUTH);

        jframe.setVisible(true);
    }

    private static void showNextDialogue() {
        if (currentDialogueIndex < dialogues.size()) {
            String dialogue = dialogues.get(currentDialogueIndex);
            gameOutputArea.append("Narrator: " + dialogue + "\n");
            currentDialogueIndex++;

            // Show a dialog and wait for user to acknowledge
            JOptionPane.showMessageDialog(null, "Press OK to continue...");

            // Only show choices if there's a corresponding choice for the current dialogue
            if (currentDialogueIndex - 1 < choices.size()) {
                showChoicesIfAvailable();
            } else {
                // If there are no choices, just show the next dialogue
                showNextDialogue();
            }
        } else {
            gameOutputArea.append("The End.\n");
        }
    }

    private static void showChoicesIfAvailable() {
        int choiceIndex = currentDialogueIndex - 1; // Current dialogue index minus one

        if (choiceIndex < choices.size()) {
            ArrayList<String> availableChoices = choices.get(choiceIndex);
            ArrayList<String> followUpDialogues = choiceDialogues.get(choiceIndex);

            // Present the choices to the user
            int userChoiceIndex = JOptionPane.showOptionDialog(null,
                    "Choose an option:",
                    "Choices",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    availableChoices.toArray(),
                    availableChoices.get(0));

            if (userChoiceIndex >= 0 && userChoiceIndex < followUpDialogues.size()) {
                gameOutputArea.append("You chose: " + availableChoices.get(userChoiceIndex) + "\n");
                gameOutputArea.append("Narrator: " + followUpDialogues.get(userChoiceIndex) + "\n");
            }

            // Proceed to the next dialogue after handling the choice
            showNextDialogue();
        } else {
            showNextDialogue();
        }
    }

    private static void removeLastEntry() {
        if (!dialogues.isEmpty()) {
            dialogues.remove(dialogues.size() - 1);
            if (!choices.isEmpty()) {
                choices.remove(choices.size() - 1);
                choiceDialogues.remove(choiceDialogues.size() - 1);
            }
            textArea.setText(""); // Clear text area
            for (String dialogue : dialogues) {
                textArea.append("Dialogue: " + dialogue + "\n");
            }
            JOptionPane.showMessageDialog(null, "Last entry removed successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "No entries to remove!");
        }
    }

    private static void saveGame() {
        try {
            String filename = JOptionPane.showInputDialog("Enter filename to save (without extension):");
            if (filename != null && !filename.isEmpty()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".tgame"));
                for (String dialogue : dialogues) {
                    writer.write("DIALOGUE:" + dialogue);
                    writer.newLine();
                }
                for (int i = 0; i < choices.size(); i++) {
                    writer.write("CHOICES:" + String.join(",", choices.get(i)));
                    writer.newLine();
                    writer.write("FOLLOWUP:" + String.join(",", choiceDialogues.get(i)));
                    writer.newLine();
                }
                writer.close();
                JOptionPane.showMessageDialog(null, "Game saved successfully!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving game: " + e.getMessage());
        }
    }

    private static void loadGame() {
        try {
            String filename = JOptionPane.showInputDialog("Enter filename to load (without extension):");
            if (filename != null && !filename.isEmpty()) {
                BufferedReader reader = new BufferedReader(new FileReader(filename + ".tgame"));
                dialogues.clear();
                choices.clear();
                choiceDialogues.clear();
                textArea.setText(""); // Clear current text area
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("DIALOGUE:")) {
                        String dialogue = line.substring("DIALOGUE:".length());
                        dialogues.add(dialogue);
                        textArea.append("Dialogue: " + dialogue + "\n");
                    } else if (line.startsWith("CHOICES:")) {
                        String[] choice = line.substring("CHOICES:".length()).split(",");
                        ArrayList<String> choicesList = new ArrayList<>();
                        for (String c : choice) {
                            choicesList.add(c.trim());
                        }
                        choices.add(choicesList);
                        line = reader.readLine(); // Get the follow-up dialogues
                        String[] followUps = line.substring("FOLLOWUP:".length()).split(",");
                        ArrayList<String> followUpList = new ArrayList<>();
                        for (String f : followUps) {
                            followUpList.add(f.trim());
                        }
                        choiceDialogues.add(followUpList);
                        textArea.append("Choices added: " + String.join(", ", choicesList) + "\n");
                    }
                }
                reader.close();
                JOptionPane.showMessageDialog(null, "Game loaded successfully!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading game: " + e.getMessage());
        }
    }
}
