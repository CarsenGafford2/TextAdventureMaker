import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a choice in a text adventure game, linking to a target story node.
 * Each choice has a description and the ID of the target story node it leads to.
 * 
 * @author Carsen Gafford
 */
class Choice implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    String description;
    String targetNodeId;
}
