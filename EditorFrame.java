import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

/**
 * Main frame for the Text Adventure Maker application.
 * 
 * @author Carsen Gafford
 */
class EditorFrame extends JFrame {
    private final EditorContext context = new EditorContext();

    EditorFrame() {
        super("Text Adventure Maker");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        toolBar.add(Box.createHorizontalStrut(8));
        toolBar.add(btnAdd);
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnRemove);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalStrut(4));
        toolBar.add(btnTest);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(btnExport);
        toolBar.add(Box.createHorizontalStrut(8));
        ModernTheme.styleToolBar(toolBar);
        ModernTheme.styleButton(btnAdd);
        ModernTheme.styleButton(btnRemove);
        ModernTheme.styleButton(btnTest);
        ModernTheme.styleButton(btnExport);
        add(toolBar, BorderLayout.NORTH);

        context.nodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        context.nodeList.addListSelectionListener(selectionHandler);
        context.nodeList.setCellRenderer(new StoryNodeCellRenderer());
        ModernTheme.styleList(context.nodeList);
        JScrollPane listScroll = new JScrollPane(context.nodeList);
        listScroll.setPreferredSize(new Dimension(220, 0));
        ModernTheme.styleScrollPane(listScroll);
        add(listScroll, BorderLayout.WEST);

        JPanel editorPanel = new JPanel(new BorderLayout(10, 10));
        editorPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        ModernTheme.stylePanel(editorPanel);

        JPanel headerPanel = new JPanel(new BorderLayout(8, 0));
        ModernTheme.stylePanel(headerPanel);
        JLabel idLabel = new JLabel("Node ID: ");
        ModernTheme.styleLabel(idLabel);
        headerPanel.add(idLabel, BorderLayout.WEST);
        context.idField.setEditable(false);
        ModernTheme.styleTextField(context.idField);
        headerPanel.add(context.idField, BorderLayout.CENTER);
        editorPanel.add(headerPanel, BorderLayout.NORTH);

        context.narrativeArea.setLineWrap(true);
        context.narrativeArea.setWrapStyleWord(true);
        context.narrativeArea.addFocusListener(new NarrativeFocusSaver(context, narrativeSaver));
        ModernTheme.styleTextArea(context.narrativeArea);
        JScrollPane textScroll = new JScrollPane(context.narrativeArea);
        textScroll.setBorder(BorderFactory.createTitledBorder(
            new ModernTheme.RoundedBorder(ModernTheme.BORDER, ModernTheme.BORDER_RADIUS),
            "Story Text",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 12),
            ModernTheme.TEXT_SECONDARY
        ));
        ModernTheme.styleScrollPane(textScroll);
        editorPanel.add(textScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(
            new ModernTheme.RoundedBorder(ModernTheme.BORDER, ModernTheme.BORDER_RADIUS),
            "Choices",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 12),
            ModernTheme.TEXT_SECONDARY
        ));
        ModernTheme.stylePanel(bottomPanel);
        context.choicesPanel.setLayout(new BoxLayout(context.choicesPanel, BoxLayout.Y_AXIS));
        ModernTheme.stylePanel(context.choicesPanel);
        JScrollPane choicesScroll = new JScrollPane(context.choicesPanel);
        choicesScroll.setPreferredSize(new Dimension(0, 180));
        ModernTheme.styleScrollPane(choicesScroll);
        JButton addChoiceBtn = new JButton("+ Add Choice");
        ModernTheme.styleButton(addChoiceBtn);
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
        
        getContentPane().setBackground(ModernTheme.BACKGROUND_PRIMARY);
    }
}
