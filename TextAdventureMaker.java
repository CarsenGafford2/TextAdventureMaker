import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Node {
    String text;
    Node connectedNode;

    public Node(String text) {
        this.text = text;
        this.connectedNode = null;
    }

    public void connect(Node node) {
        if (this.connectedNode == null) {
            this.connectedNode = node;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Node getConnectedNode() {
        return connectedNode;
    }

    public boolean isConnected() {
        return connectedNode != null;
    }
}

// Dialogue Node class
class DialogueNode extends Node {
    public DialogueNode(String text) {
        super(text);
    }
}

class NodePanel extends JPanel {
    private DialogueNode node;
    private Point mousePoint;
    private boolean dragging = false;
    private static NodePanel selectedNode = null;
    private JTextField textField;
    public NodePanel(DialogueNode node, boolean isStartNode) {
        this.node = node;
        setBorder(new LineBorder(Color.BLACK));
        setPreferredSize(new Dimension(150, 100));
        setBackground(isStartNode ? Color.BLUE : Color.LIGHT_GRAY); // Blue for start node
        setLayout(null);

        if (!isStartNode) { // Only create text field if it's not the start node
            textField = new JTextField(node.getText());
            textField.setHorizontalAlignment(SwingConstants.CENTER);
            textField.setBounds(10, 10, 130, 40);
            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    node.setText(textField.getText());
                }
            });
            add(textField);
        }

        // Allow connection from the start node as well
        JPanel rightDot = createDotPanel(Color.RED);
        rightDot.setBounds(140, 40 - 5, 10, 10);
        add(rightDot);
        
        JPanel leftDot = createDotPanel(Color.GREEN);
        leftDot.setBounds(0, 40 - 5, 10, 10);
        if (!isStartNode) {
            add(leftDot);
        }

        // Mouse listeners to handle dragging and connections
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePoint = e.getPoint();
                dragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
                getParent().repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    Point p = getLocation();
                    int newX = p.x + e.getX() - mousePoint.x;
                    int newY = p.y + e.getY() - mousePoint.y;
                    setLocation(newX, newY);
                    getParent().repaint();
                }
            }
        });

        rightDot.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedNode == null && !node.isConnected()) {
                    selectedNode = NodePanel.this;
                    repaint();
                }
            }
        });
        
        leftDot.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedNode != null && selectedNode != NodePanel.this) {
                    if (!node.isConnected() && !selectedNode.getNode().isConnected()) {
                        selectedNode.getNode().connect(node);
                        selectedNode = null;
                        getParent().repaint();
                    } else {
                        JOptionPane.showMessageDialog(getParent(),
                            "One of the nodes is already connected.",
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private JPanel createDotPanel(Color color) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(color);
                g.fillOval(0, 0, 10, 10);
            }
        };
    }

    public DialogueNode getNode() {
        return node;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selectedNode == this) {
            g.setColor(Color.BLUE);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }
}
public class TextAdventureMaker extends JFrame {
    private JPanel canvas;
    private Point lastMousePosition;
    private boolean draggingCanvas = false;

    public TextAdventureMaker() {
        setTitle("Text Adventure Maker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawConnections(g);
            }
        };
        canvas.setBackground(Color.WHITE);
        add(canvas, BorderLayout.CENTER);

        JButton addDialogueButton = new JButton("Add Dialogue Node");
        addDialogueButton.addActionListener(e -> addNode(new DialogueNode("New Dialogue"), false)); // Specify false for regular nodes

        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> runDialogue());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addDialogueButton);
        buttonPanel.add(runButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Automatically add the start node
        addNode(new DialogueNode("Start Dialogue"), true);

        // Add mouse listeners for dragging the canvas
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePosition = e.getPoint();
                draggingCanvas = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggingCanvas = false;
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingCanvas) {
                    int deltaX = e.getX() - lastMousePosition.x;
                    int deltaY = e.getY() - lastMousePosition.y;
                    Point location = canvas.getLocation();
                    canvas.setLocation(location.x + deltaX, location.y + deltaY);
                    lastMousePosition = e.getPoint();
                    canvas.revalidate();
                    canvas.repaint();
                }
            }
        });
    }

    private void addNode(DialogueNode newNode, boolean isStartNode) {
        NodePanel nodePanel = new NodePanel(newNode, isStartNode);
        int x = 50 * canvas.getComponentCount();
        int y = 50;
        nodePanel.setBounds(x, y, 150, 100);
        canvas.add(nodePanel);
        canvas.repaint();
        canvas.revalidate();
    }

    private void drawConnections(Graphics g) {
        for (Component comp : canvas.getComponents()) {
            if (comp instanceof NodePanel) {
                NodePanel source = (NodePanel) comp;
                Node targetNode = source.getNode().getConnectedNode();
                if (targetNode != null) {
                    NodePanel target = findNodePanel(targetNode);
                    if (target != null) {
                        g.drawLine(source.getX() + 150, source.getY() + 40,
                                   target.getX(), target.getY() + 40);
                    }
                }
            }
        }
    }

    private NodePanel findNodePanel(Node node) {
        for (Component comp : canvas.getComponents()) {
            if (comp instanceof NodePanel) {
                NodePanel panel = (NodePanel) comp;
                if (panel.getNode() == node) {
                    return panel;
                }
            }
        }
        return null;
    }

    private void runDialogue() {
        NodePanel startNodePanel = findStartNode();
        if (startNodePanel != null) {
            String dialogue = new String();
            Node currentNode = startNodePanel.getNode().getConnectedNode();

            while (currentNode != null) {
                dialogue = currentNode.getText();
                currentNode = currentNode.getConnectedNode();
                JOptionPane.showMessageDialog(this, dialogue.toString(), "Dialogue Chain", JOptionPane.INFORMATION_MESSAGE);
            }

            // JOptionPane.showMessageDialog(this, dialogue.toString(), "Dialogue Chain", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No start node found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private NodePanel findStartNode() {
        for (Component comp : canvas.getComponents()) {
            if (comp instanceof NodePanel) {
                NodePanel panel = (NodePanel) comp;
                if (panel.getNode().getText().equals("Start Dialogue")) {
                    return panel;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextAdventureMaker frame = new TextAdventureMaker();
            frame.setVisible(true);
        });
    }
}