import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

class EditorFrame extends JFrame {
    private final EditorContext context = new EditorContext();

    EditorFrame() {
        super("Text Adventure Maker");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        context.frame = this;
        context.nodeMap = new HashMap<>();
        context.listModel = new DefaultListModel<>();
        context.nodeList = new JList<>(context.listModel);
        context.idField = new JTextField();
        context.narrativeArea = new JTextArea();
        context.choicesPanel = new JPanel();

        StoryNodeFactory nodeFactory = new StoryNodeFactory();
        ChoiceFactory choiceFactory = new ChoiceFactory();
        NodeCreator nodeCreator = new NodeCreator(nodeFactory);
        NarrativeSaver narrativeSaver = new NarrativeSaver();
        ChoicesUIRefresher refresher = new ChoicesUIRefresher();
        NodeSelectionHandler selectionHandler = new NodeSelectionHandler(context, narrativeSaver, refresher);
        ChoiceAdder choiceAdder = new ChoiceAdder(context, choiceFactory, refresher);
        TestRunner testRunner = new TestRunner(context);
        GameExporter exporter = new GameExporter(context);
        NodeAdder nodeAdder = new NodeAdder(context, nodeCreator);
        NodeRemover nodeRemover = new NodeRemover(context);

        JToolBar toolBar = new JToolBar();
        JButton btnAdd = new JButton("Add Node");
        JButton btnRemove = new JButton("Remove Node");
        JButton btnTest = new JButton("Test Run");
        JButton btnExport = new JButton("Export to JAR");

        btnAdd.addActionListener(nodeAdder);
        btnRemove.addActionListener(nodeRemover);
        btnTest.addActionListener(ev -> { narrativeSaver.save(context); testRunner.actionPerformed(ev); });
        btnExport.addActionListener(ev -> { narrativeSaver.save(context); exporter.actionPerformed(ev); });

        toolBar.add(btnAdd);
        toolBar.add(btnRemove);
        toolBar.addSeparator();
        toolBar.add(btnTest);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(btnExport);
        add(toolBar, BorderLayout.NORTH);

        context.nodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        context.nodeList.addListSelectionListener(selectionHandler);
        context.nodeList.setCellRenderer(new StoryNodeCellRenderer());
        JScrollPane listScroll = new JScrollPane(context.nodeList);
        listScroll.setPreferredSize(new Dimension(200, 0));
        add(listScroll, BorderLayout.WEST);

        JPanel editorPanel = new JPanel(new BorderLayout(10, 10));
        editorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(new JLabel("Node ID: "), BorderLayout.WEST);
        context.idField.setEditable(false);
        headerPanel.add(context.idField, BorderLayout.CENTER);
        editorPanel.add(headerPanel, BorderLayout.NORTH);

        context.narrativeArea.setLineWrap(true);
        context.narrativeArea.setWrapStyleWord(true);
        context.narrativeArea.addFocusListener(new NarrativeFocusSaver(context, narrativeSaver));
        JScrollPane textScroll = new JScrollPane(context.narrativeArea);
        textScroll.setBorder(BorderFactory.createTitledBorder("Story Text"));
        editorPanel.add(textScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Choices"));
        context.choicesPanel.setLayout(new BoxLayout(context.choicesPanel, BoxLayout.Y_AXIS));
        JScrollPane choicesScroll = new JScrollPane(context.choicesPanel);
        choicesScroll.setPreferredSize(new Dimension(0, 150));
        JButton addChoiceBtn = new JButton("+ Add Choice");
        addChoiceBtn.addActionListener(choiceAdder);
        bottomPanel.add(choicesScroll, BorderLayout.CENTER);
        bottomPanel.add(addChoiceBtn, BorderLayout.SOUTH);
        editorPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(editorPanel, BorderLayout.CENTER);

        StoryNode startNode = nodeCreator.create(context, "START");
        context.currentNode = startNode;
        context.currentChoices = startNode.choices;
        context.idField.setText(startNode.id);
        context.narrativeArea.setText(startNode.narrativeText);
        refresher.refresh(context);
    }
}
