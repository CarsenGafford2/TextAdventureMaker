import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Focus listener to save the narrative text when editing is finished.
 * 
 * @author Carsen Gafford
 */
class NarrativeFocusSaver extends FocusAdapter {
    private final EditorContext context;
    private final NarrativeSaver saver;

    NarrativeFocusSaver(EditorContext context, NarrativeSaver saver) {
        this.context = context;
        this.saver = saver;
    }

    @Override
    public void focusLost(FocusEvent e) {
        saver.save(context);
    }
}
