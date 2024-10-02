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
        this.next = next;
    }

    public String getText() {
        return text;
    }

    public Node getNext() {
        return next;
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
    private boolean isConnecting = false;
    private Point connectionEndPoint;
    
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
                isConnecting = true;
                connectionEndPoint = getLocation();
                connectionEndPoint.translate(150, 40); // Start from the right dot
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isConnecting) {
                    // Check for other nodes to connect to
                    for (Component comp : getParent().getComponents()) {
                        if (comp instanceof NodePanel && comp != NodePanel.this) {
                            NodePanel targetNode = (NodePanel) comp;
                            if (targetNode.getBounds().contains(e.getPoint())) {
                                // Connect this node to the target node
                                node.setNext(targetNode.getNode());
                                break;
                            }
                        }
                    }
                }
                isConnecting = false;
                connectionEndPoint = null; // Reset end point
                getParent().repaint(); // Refresh the panel
            }
        });

        rightDot.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isConnecting) {
                    connectionEndPoint = e.getLocationOnScreen();
                    getParent().repaint();
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
        if (isConnecting && connectionEndPoint != null) {
            // Draw the connection line
            g.setColor(Color.BLACK);
            g.drawLine(
                getX() + 150, getY() + 40,
                (int) (connectionEndPoint.getX()), (int) (connectionEndPoint.getY())
            );
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

        // Connect to the previous node
        if (head == null) {
            head = newNode; // This is the first node
        } else {
            DialogueNode current = head;
            while (current.getNext() != null) {
                current = (DialogueNode) current.getNext(); // Cast to DialogueNode
            }
            current.setNext(newNode); // Link the new node to the last node
        }
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