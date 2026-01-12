/**
 * Refreshes the choices UI panel based on the current choices in the editor context.
 * Utilizes ChoiceRowBuilder to create UI components for each choice.
 * 
 * @author Carsen Gafford
 */
class ChoicesUIRefresher {
    private final ChoiceRowBuilder rowBuilder = new ChoiceRowBuilder();

    void refresh(EditorContext context) {
        context.choicesPanel.removeAll();
        if (context.currentChoices != null) {
            for (Choice choice : context.currentChoices) {
                context.choicesPanel.add(rowBuilder.build(context, choice, this));
                context.choicesPanel.add(javax.swing.Box.createVerticalStrut(8));
            }
        }
        context.choicesPanel.revalidate();
        context.choicesPanel.repaint();
    }
}
