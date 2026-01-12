import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
