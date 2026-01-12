import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern dark theme styling for the Text Adventure Maker.
 * Provides a sleek, modern dark mode with smooth animations.
 * 
 * @author Carsen Gafford
 */
class ModernTheme {
    static final Color BACKGROUND_PRIMARY = new Color(24, 24, 27);      // #18181B
    static final Color BACKGROUND_SECONDARY = new Color(39, 39, 42);    // #27272A
    static final Color BACKGROUND_TERTIARY = new Color(52, 52, 55);     // #343437
    static final Color SURFACE = new Color(63, 63, 70);                 // #3F3F46
    static final Color SURFACE_HOVER = new Color(82, 82, 91);           // #52525B
    
    static final Color ACCENT_PRIMARY = new Color(99, 102, 241);        // #6366F1
    static final Color ACCENT_HOVER = new Color(129, 140, 248);         // #818CF8
    static final Color ACCENT_PRESSED = new Color(79, 70, 229);         // #4F46E5
    
    static final Color SUCCESS = new Color(34, 197, 94);                // #22C55E
    static final Color DANGER = new Color(239, 68, 68);                 // #EF4444
    
    static final Color TEXT_PRIMARY = new Color(250, 250, 250);         // #FAFAFA
    static final Color TEXT_SECONDARY = new Color(161, 161, 170);       // #A1A1AA
    static final Color TEXT_DISABLED = new Color(113, 113, 122);        // #71717A
    
    static final Color BORDER = new Color(63, 63, 70);                  // #3F3F46
    static final Color BORDER_FOCUS = new Color(99, 102, 241);          // #6366F1
    
    static final int ANIMATION_DURATION = 200;
    static final int BORDER_RADIUS = 8;

    static void apply(Container container) {
        applyToComponent(container);
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                apply((Container) comp);
            }
        }
    }

    private static void applyToComponent(Component comp) {
        if (comp instanceof JButton) {
            styleButton((JButton) comp);
        } else if (comp instanceof JTextField) {
            styleTextField((JTextField) comp);
        } else if (comp instanceof JTextArea) {
            styleTextArea((JTextArea) comp);
        } else if (comp instanceof JList) {
            styleList((JList<?>) comp);
        } else if (comp instanceof JComboBox) {
            styleComboBox((JComboBox<?>) comp);
        } else if (comp instanceof JScrollPane) {
            styleScrollPane((JScrollPane) comp);
        } else if (comp instanceof JPanel) {
            stylePanel((JPanel) comp);
        } else if (comp instanceof JLabel) {
            styleLabel((JLabel) comp);
        } else if (comp instanceof JToolBar) {
            styleToolBar((JToolBar) comp);
        }
    }

    static void styleButton(JButton button) {
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(SURFACE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        
        String text = button.getText();
        if (text.contains("Export") || text.contains("Test")) {
            button.setBackground(ACCENT_PRIMARY);
            button.setForeground(TEXT_PRIMARY);
        } else if (text.contains("Remove") || text.equals("X")) {
            button.setBackground(DANGER);
            button.setForeground(TEXT_PRIMARY);
            if (text.equals("X")) {
                button.setBorder(new EmptyBorder(4, 8, 4, 8));
                button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            }
        } else if (text.contains("Add")) {
            button.setBackground(SUCCESS);
            button.setForeground(TEXT_PRIMARY);
        }
        
        button.addMouseListener(new MouseAdapter() {
            private Timer hoverTimer;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                Color targetColor = button.getBackground().brighter();
                animateBackgroundColor(button, button.getBackground(), targetColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                Color originalColor = getOriginalButtonColor(button.getText());
                animateBackgroundColor(button, button.getBackground(), originalColor);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }
        });
    }

    private static Color getOriginalButtonColor(String text) {
        if (text.contains("Export") || text.contains("Test")) {
            return ACCENT_PRIMARY;
        } else if (text.contains("Remove") || text.equals("X")) {
            return DANGER;
        } else if (text.contains("Add")) {
            return SUCCESS;
        }
        return SURFACE;
    }

    static void styleTextField(JTextField field) {
        field.setBackground(BACKGROUND_SECONDARY);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER, BORDER_RADIUS),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(BORDER_FOCUS, BORDER_RADIUS),
                    new EmptyBorder(8, 12, 8, 12)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(BORDER, BORDER_RADIUS),
                    new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    static void styleTextArea(JTextArea area) {
        area.setBackground(BACKGROUND_SECONDARY);
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(TEXT_PRIMARY);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(8, 12, 8, 12));
    }

    static void styleList(JList<?> list) {
        list.setBackground(BACKGROUND_SECONDARY);
        list.setForeground(TEXT_PRIMARY);
        list.setSelectionBackground(ACCENT_PRIMARY);
        list.setSelectionForeground(TEXT_PRIMARY);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setBorder(new EmptyBorder(4, 4, 4, 4));
        list.setFixedCellHeight(36);
    }

    static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(BACKGROUND_SECONDARY);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBorder(new RoundedBorder(BORDER, BORDER_RADIUS));
        
        if (comboBox.getRenderer() instanceof JLabel) {
            comboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, 
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setBackground(isSelected ? ACCENT_PRIMARY : BACKGROUND_SECONDARY);
                    setForeground(TEXT_PRIMARY);
                    setBorder(new EmptyBorder(6, 10, 6, 10));
                    return this;
                }
            });
        }
    }

    static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBackground(BACKGROUND_PRIMARY);
        scrollPane.setBorder(new RoundedBorder(BORDER, BORDER_RADIUS));
        scrollPane.getViewport().setBackground(BACKGROUND_SECONDARY);
        
        // Style scrollbars
        styleScrollBar(scrollPane.getVerticalScrollBar());
        styleScrollBar(scrollPane.getHorizontalScrollBar());
    }

    private static void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new ModernScrollBarUI());
        scrollBar.setUnitIncrement(16);
        scrollBar.setBackground(BACKGROUND_PRIMARY);
    }

    static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND_PRIMARY);
        
        if (panel.getBorder() instanceof TitledBorder) {
            TitledBorder titled = (TitledBorder) panel.getBorder();
            titled.setTitleColor(TEXT_SECONDARY);
            titled.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
            titled.setBorder(new RoundedBorder(BORDER, BORDER_RADIUS));
        }
    }

    static void styleLabel(JLabel label) {
        label.setForeground(TEXT_SECONDARY);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    static void styleToolBar(JToolBar toolBar) {
        toolBar.setBackground(BACKGROUND_SECONDARY);
        toolBar.setBorderPainted(false);
        toolBar.setFloatable(false);
    }

    private static void animateBackgroundColor(JComponent component, Color from, Color to) {
        final int steps = 10;
        final int delay = ANIMATION_DURATION / steps;
        
        Timer timer = new Timer(delay, null);
        final int[] step = {0};
        
        timer.addActionListener(e -> {
            step[0]++;
            float ratio = (float) step[0] / steps;
            
            int r = (int) (from.getRed() + (to.getRed() - from.getRed()) * ratio);
            int g = (int) (from.getGreen() + (to.getGreen() - from.getGreen()) * ratio);
            int b = (int) (from.getBlue() + (to.getBlue() - from.getBlue()) * ratio);
            
            component.setBackground(new Color(r, g, b));
            
            if (step[0] >= steps) {
                timer.stop();
            }
        });
        
        timer.start();
    }

    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = 2;
            return insets;
        }
    }

    static class ModernScrollBarUI extends BasicScrollBarUI {
        private static final int THUMB_SIZE = 8;
        
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = SURFACE;
            this.thumbDarkShadowColor = SURFACE;
            this.thumbHighlightColor = SURFACE;
            this.thumbLightShadowColor = SURFACE;
            this.trackColor = BACKGROUND_PRIMARY;
            this.trackHighlightColor = BACKGROUND_PRIMARY;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createInvisibleButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createInvisibleButton();
        }

        private JButton createInvisibleButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color color = isThumbRollover() ? SURFACE_HOVER : SURFACE;
            g2.setColor(color);
            
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                int x = thumbBounds.x + (thumbBounds.width - THUMB_SIZE) / 2;
                g2.fillRoundRect(x, thumbBounds.y + 2, THUMB_SIZE, thumbBounds.height - 4, 
                                THUMB_SIZE, THUMB_SIZE);
            } else {
                int y = thumbBounds.y + (thumbBounds.height - THUMB_SIZE) / 2;
                g2.fillRoundRect(thumbBounds.x + 2, y, thumbBounds.width - 4, THUMB_SIZE,
                                THUMB_SIZE, THUMB_SIZE);
            }
            
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(trackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
    }
}
