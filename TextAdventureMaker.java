import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Base Node class
class Node {
    String text;
    Node next;

    public Node(String text) {
        this.text = text;
        this.next = null;
    }

    public void setNext(Node next) {
        if (this.next == null) { // Only allow setting if there is no current connection
            this.next = next;
        }
    }
    public String getText() {
        return text;
    }

    public Node getNext() {
        return next;
    }
    public boolean isConnected() {
        return next != null;
    }    
}

// Dialogue Node class
class DialogueNode extends Node {
    public DialogueNode(String text) {
        super(text);
    }
}

// Panel for each node
class NodePanel extends JPanel {
    private DialogueNode node;
    private Point mousePoint;
    private boolean dragging = false;
    private static NodePanel selectedNode = null; // Track selected node for connections

    public NodePanel(DialogueNode node) {
        this.node = node;
        setBorder(new LineBorder(Color.BLACK));
        setPreferredSize(new Dimension(150, 100));
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        JLabel label = new JLabel(node.getText());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 0, 150, 80);
        add(label);

        // Right connection dot
        JPanel rightDot = createDotPanel(Color.RED);
        rightDot.setBounds(140, 40 - 5, 10, 10);
        add(rightDot);

        // Left connection dot
        JPanel leftDot = createDotPanel(Color.GREEN);
        leftDot.setBounds(0, 40 - 5, 10, 10);
        add(leftDot);

        // Mouse listeners for dragging the entire node
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePoint = e.getPoint();
                dragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false; // Stop dragging
                getParent().repaint(); // Refresh the panel
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

        // Mouse listeners for connection
        rightDot.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Select this node for connection
                selectedNode = NodePanel.this;
                repaint(); // Optionally highlight this node
            }
        });

        leftDot.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedNode != null && selectedNode != NodePanel.this) {
                    // Only connect if the current node and selected node are both unconnected
                    if (!node.isConnected() && !selectedNode.getNode().isConnected()) {
                        // Connect the selected node to this node
                        selectedNode.getNode().setNext(node); // Connect
                        selectedNode = null; // Reset selected node
                        getParent().repaint(); // Refresh the panel
                    } else {
                        JOptionPane.showMessageDialog(getParent(), 
                            "One of the nodes is already connected to another node.", 
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
            // Highlight the selected node
            g.setColor(Color.BLUE);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // Highlight selected node
        }
    }
}

// Main application class
public class TextAdventureMaker extends JFrame {
    private JPanel canvas;
    private DialogueNode head; // To keep track of the head of the dialogue linked list

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
        add(canvas, BorderLayout.CENTER);

        JButton addDialogueButton = new JButton("Add Dialogue Node");
        addDialogueButton.addActionListener(e -> addNode(new DialogueNode("New Dialogue")));

        JButton runAdventureButton = new JButton("Run Adventure");
        runAdventureButton.addActionListener(e -> runAdventure());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addDialogueButton);
        buttonPanel.add(runAdventureButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addNode(DialogueNode newNode) {
        NodePanel nodePanel = new NodePanel(newNode);
        int x = 50 * canvas.getComponentCount();
        int y = 50;
        nodePanel.setBounds(x, y, 150, 100);
        canvas.add(nodePanel);
        canvas.repaint();
        canvas.revalidate();
    
        // Don't automatically connect the new node to the previous one.
        // The connection logic will now solely rely on user interaction.
    }    

    private void drawConnections(Graphics g) {
        for (Component comp : canvas.getComponents()) {
            if (comp instanceof NodePanel) {
                NodePanel source = (NodePanel) comp;
                Node currentNode = source.getNode();
                while (currentNode != null && currentNode.getNext() != null) {
                    NodePanel target = findNodePanel(currentNode.getNext());
                    if (target != null) {
                        g.drawLine(source.getX() + 150, source.getY() + 40,
                                   target.getX(), target.getY() + 40);
                    }
                    currentNode = currentNode.getNext();
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

    private void runAdventure() {
        if (head != null) {
            StringBuilder sb = new StringBuilder();
            Node current = head;
            while (current != null) {
                sb.append(current.getText()).append("\n");
                current = current.getNext();
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Adventure Output", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No dialogue nodes available to run the adventure.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextAdventureMaker frame = new TextAdventureMaker();
            frame.setVisible(true);
        });
    }
}