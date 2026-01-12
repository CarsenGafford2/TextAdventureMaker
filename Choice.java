import java.io.Serial;
import java.io.Serializable;

class Choice implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    String description;
    String targetNodeId;
}
