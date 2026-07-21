package newStuff.canvas.swing;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.function.Consumer;

public class VectorFieldPanel extends JPanel {

    public static final class Vec3 {
        public double x, y, z;
        public Vec3(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
        public Vec3 copy() { return new Vec3(x, y, z); }
    }

    private final JTextField xField = new JTextField(7);
    private final JTextField yField = new JTextField(7);
    private final JTextField zField = new JTextField(7);

    private final JLabel xLabel = makeAxisLabel("X", new Color(220, 85, 85));
    private final JLabel yLabel = makeAxisLabel("Y", new Color(95, 190, 95));
    private final JLabel zLabel = makeAxisLabel("Z", new Color(85, 140, 230));

    private final double baseStep;
    private Vec3 value = new Vec3(0, 0, 0);
    private Consumer<Vec3> onValueChanged;

    private boolean updatingFromCode = false;

    public VectorFieldPanel(String title, double step) {
        super(new GridBagLayout());
        this.baseStep = step <= 0 ? 0.1 : step;
        setBorder(BorderFactory.createTitledBorder(title));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0;

        gc.gridx = 0; gc.gridy = 0;
        add(xLabel, gc);
        gc.gridx = 1; gc.weightx = 1;
        add(xField, gc);

        gc.gridx = 2; gc.weightx = 0;
        add(yLabel, gc);
        gc.gridx = 3; gc.weightx = 1;
        add(yField, gc);

        gc.gridx = 4; gc.weightx = 0;
        add(zLabel, gc);
        gc.gridx = 5; gc.weightx = 1;
        add(zField, gc);

        installScrubber(xLabel, Axis.X);
        installScrubber(yLabel, Axis.Y);
        installScrubber(zLabel, Axis.Z);

        installFieldParser(xField, Axis.X);
        installFieldParser(yField, Axis.Y);
        installFieldParser(zField, Axis.Z);

        refreshFields();
    }

    public void setOnValueChanged(Consumer<Vec3> listener) {
        this.onValueChanged = listener;
    }

    public void setValue(double x, double y, double z) {
        value.x = x; value.y = y; value.z = z;
        refreshFields();
    }

    public Vec3 getValue() {
        return value.copy();
    }

    private enum Axis { X, Y, Z }

    private JLabel makeAxisLabel(String txt, Color c) {
        JLabel l = new JLabel(txt, SwingConstants.CENTER);
        l.setOpaque(true);
        l.setForeground(Color.WHITE);
        l.setBackground(c);
        l.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        l.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        return l;
    }

    private void installFieldParser(JTextField field, Axis axis) {
        field.addActionListener(e -> parseAndApply(field, axis));
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                parseAndApply(field, axis);
            }
        });

        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { }
            @Override public void removeUpdate(DocumentEvent e) { }
            @Override public void changedUpdate(DocumentEvent e) { }
        });
    }

    private void parseAndApply(JTextField field, Axis axis) {
        if (updatingFromCode) return;
        String t = field.getText().trim().replace(',', '.');
        try {
            double v = Double.parseDouble(t);
            setAxis(axis, v);
            emit();
        } catch (NumberFormatException ex) {
            refreshFields();
        }
    }

    private void installScrubber(JLabel label, Axis axis) {
        final Point[] startScreen = new Point[1];
        final double[] startValue = new double[1];

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startScreen[0] = e.getLocationOnScreen();
                startValue[0] = getAxis(axis);
//                label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (startScreen[0] == null) return;

                Point now = e.getLocationOnScreen();
                int dx = now.x - startScreen[0].x;

                double speed = baseStep;
                if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) speed *= 0.1;
                if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) speed *= 10.0;

                double next = startValue[0] + dx * speed;
                setAxis(axis, next);
                refreshFields();
                emit();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startScreen[0] = null;
//                label.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            }
        };

        label.addMouseListener(ma);
        label.addMouseMotionListener(ma);
    }

    private double getAxis(Axis axis) {
        return switch (axis) {
            case X -> value.x;
            case Y -> value.y;
            case Z -> value.z;
        };
    }

    private void setAxis(Axis axis, double v) {
        switch (axis) {
            case X -> value.x = v;
            case Y -> value.y = v;
            case Z -> value.z = v;
        }
    }

    private void refreshFields() {
        updatingFromCode = true;
        xField.setText(fmt(value.x));
        yField.setText(fmt(value.y));
        zField.setText(fmt(value.z));
        updatingFromCode = false;
    }

    private String fmt(double v) {
        return String.format(Locale.US, "%.3f", v);
    }

    private void emit() {
        if (onValueChanged != null) onValueChanged.accept(value.copy());
    }
}