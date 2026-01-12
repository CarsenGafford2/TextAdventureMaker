import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class StoryNode implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    String id;
    String narrativeText;
    List<Choice> choices = new ArrayList<>();
}
