import javax.swing.*;

class ChoicesUIRefresher {
    private final ChoiceRowBuilder rowBuilder = new ChoiceRowBuilder();

    void refresh(EditorContext context) {
        context.choicesPanel.removeAll();
        if (context.currentChoices != null) {
            for (Choice choice : context.currentChoices) {
                context.choicesPanel.add(rowBuilder.build(context, choice, this));
            }
        }
        context.choicesPanel.revalidate();
        context.choicesPanel.repaint();
    }
}
