/**
 * Generates the Java code for the game runner.
 * 
 * @author Carsen Gafford
 */
class RunnerCodeGenerator {
    String generate(String base64Data) {
        return """
import java.io.*;
import java.util.*;
import java.util.Base64;

public class GameRunner {
    static class StoryNode implements Serializable {
        private static final long serialVersionUID = 1L;
        String id;
        String narrativeText;
        List<Choice> choices;

        public StoryNode(String id) {
            this.id = id;
            this.narrativeText = "Enter story text here...";
            this.choices = new ArrayList<>();
        }
    }

    static class Choice implements Serializable {
        private static final long serialVersionUID = 1L;
        String description;
        String targetNodeId;

        public Choice(String description, String targetNodeId) {
            this.description = description;
            this.targetNodeId = targetNodeId;
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        byte[] dataBytes = Base64.getDecoder().decode("%s");
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBytes));
        Map<String, Object> nodeMap = (Map<String, Object>) ois.readObject();

        Scanner scanner = new Scanner(System.in);
        StoryNode current = (StoryNode) nodeMap.get("START");
        while (true) {
            System.out.println("\\n" + "=".repeat(40));
            System.out.println(current.narrativeText);
            if (current.choices.isEmpty()) { System.out.println("THE END"); break; }
            for (int i = 0; i < current.choices.size(); i++) System.out.println((i+1) + ". " + current.choices.get(i).description);
            System.out.print("> ");
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input)-1;
                if (choice >= 0 && choice < current.choices.size()) current = (StoryNode) nodeMap.get(current.choices.get(choice).targetNodeId);
                else System.out.println("Invalid selection.");
            } catch (Exception e) { System.out.println("Enter a number."); }
        }
    }
}
""".formatted(base64Data);
    }
}
