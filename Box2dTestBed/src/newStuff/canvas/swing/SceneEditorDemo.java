package newStuff.canvas.swing;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SceneEditorDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Scene Editor (Swing)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            SceneEditorPanel contentPane = new SceneEditorPanel();
            frame.setContentPane(contentPane);
            frame.setJMenuBar(contentPane.createMenuBar());
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }





    static class SceneEditorPanel extends JPanel {

        private final CanvasPanel canvas = new CanvasPanel();
        private final JLabel status = new JLabel("Ready");
        private JTree hierarchyTree;
        private DefaultMutableTreeNode rootNode;
        private DefaultMutableTreeNode objectsNode;
        private boolean suppressTreeSelectionEvent = false;

        SceneEditorPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(6, 6, 6, 6));

            add(createToolbar(), BorderLayout.NORTH);
            add(createMainSplitLayout(), BorderLayout.CENTER);   // <- use split layout
            add(createStatusBar(), BorderLayout.SOUTH);// <- bottom statuses
            canvas.setSceneChangedListener(this::rebuildHierarchyTree);
            canvas.setSelectionChangedListener(this::selectTreeNodeForObject);
            rebuildHierarchyTree();
        }

        public JMenuBar createMenuBar(){
            JMenuBar bar = new JMenuBar();

            JMenu file = new JMenu("File");
            JMenuItem newScene = new JMenuItem("New");
            JMenuItem open = new JMenuItem("Open...");
            JMenuItem save = new JMenuItem("Save");
            JMenuItem saveAs = new JMenuItem("Save As...");
            file.add(newScene);
            file.addSeparator();
            file.add(open);
            file.add(save);
            file.add(saveAs);

            JMenu insert = new JMenu("Insert");
            JMenuItem rect = new JMenuItem("Rectangle");
            JMenuItem circle = new JMenuItem("Circle");
            JMenuItem createComplex = new JMenuItem("Create Complex Object from Selection");
            JMenuItem placeComplex = new JMenuItem("Place Complex Object...");
            insert.add(rect);
            insert.add(circle);
            insert.addSeparator();
            insert.add(createComplex);
            insert.add(placeComplex);

            bar.add(file);
            bar.add(insert);
            return bar;
        }

        private JComponent createMainSplitLayout() {
            // Left: tree view
            JComponent treePanel = createTreePanel();

            // Right-bottom: properties
            JComponent propertiesPanel = createPropertiesPanel();

            // Right: canvas (top) + properties (bottom)
            JSplitPane rightSplit = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    canvas,
                    propertiesPanel
            );
            rightSplit.setResizeWeight(0.80);      // keep more space for canvas
            rightSplit.setContinuousLayout(true);
            rightSplit.setOneTouchExpandable(true);
            rightSplit.setDividerLocation(520);

            // Whole area: left tree + right side
            JSplitPane mainSplit = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    treePanel,
                    rightSplit
            );
            mainSplit.setResizeWeight(0.20);       // 20% tree, 80% right side
            mainSplit.setContinuousLayout(true);
            mainSplit.setOneTouchExpandable(true);
            mainSplit.setDividerLocation(260);

            return mainSplit;
        }

        private JComponent createTreePanel() {
            rootNode = new DefaultMutableTreeNode("Scene");
            objectsNode = new DefaultMutableTreeNode("Objects");
            rootNode.add(objectsNode);

            DefaultTreeModel model = new DefaultTreeModel(rootNode);
            hierarchyTree = new JTree(model);
            hierarchyTree.setRootVisible(true);
            hierarchyTree.setShowsRootHandles(true);
            hierarchyTree.setDragEnabled(true);
            hierarchyTree.setDropMode(DropMode.ON_OR_INSERT);
            hierarchyTree.setTransferHandler(new TransferHandler(){
                @Override
                public boolean canImport(TransferSupport support) {
                    return true;
                }

                @Override
                public boolean importData(TransferSupport support) {
                    return true;
                }
            });
            hierarchyTree.addTreeSelectionListener(e -> {
                if (suppressTreeSelectionEvent) return;
                Object n = hierarchyTree.getLastSelectedPathComponent();
                if (n instanceof ObjectNode on) {
                    canvas.setSelected(on.object);
                    status.setText("Selected: " + on.label);
                } else {
                    canvas.setSelected(null);
                }
            });

            JScrollPane scroll = new JScrollPane(hierarchyTree);
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Hierarchy"));
            panel.add(scroll, BorderLayout.CENTER);
            panel.setMinimumSize(new Dimension(180, 120));
            return panel;
        }

        private void rebuildHierarchyTree() {
            if (objectsNode == null || hierarchyTree == null) return;

            objectsNode.removeAllChildren();

            int i = 1;
            for (SceneObject o : canvas.getObjectsView()) {
                String type = o.getClass().getSimpleName();
                objectsNode.add(new ObjectNode(o, i + " - " + type));
                i++;
            }

            ((DefaultTreeModel) hierarchyTree.getModel()).reload(rootNode);
            hierarchyTree.expandRow(0);
            hierarchyTree.expandRow(1);
        }

        private void selectTreeNodeForObject(SceneObject target) {
            if (hierarchyTree == null) return;

            suppressTreeSelectionEvent = true;
            try {
                if (target == null) {
                    hierarchyTree.clearSelection();
                    return;
                }

                java.util.Enumeration<?> en = objectsNode.depthFirstEnumeration();
                while (en.hasMoreElements()) {
                    Object n = en.nextElement();
                    if (n instanceof ObjectNode on && on.object == target) {
                        TreePath path = new TreePath(on.getPath());
                        hierarchyTree.setSelectionPath(path);
                        hierarchyTree.scrollPathToVisible(path);
                        return;
                    }
                }
                hierarchyTree.clearSelection();
            } finally {
                suppressTreeSelectionEvent = false;
            }
        }

        private JComponent createPropertiesPanel() {
            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(4, 6, 4, 6);
            gc.anchor = GridBagConstraints.WEST;
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 1.0;

            VectorFieldPanel positionField = new VectorFieldPanel("Position", 0.1);
            VectorFieldPanel rotationField = new VectorFieldPanel("Rotation", 0.25);
            VectorFieldPanel scaleField = new VectorFieldPanel("Scale", 0.01);
            scaleField.setValue(1, 1, 1);

            gc.gridx = 0; gc.gridy = 0;
            form.add(positionField, gc);
            gc.gridy++;
            form.add(rotationField, gc);
            gc.gridy++;
            form.add(scaleField, gc);
            form.add(Box.createVerticalGlue(), gc);

            JScrollPane scroll = new JScrollPane(form);



            positionField.setOnValueChanged(v -> {
                SceneObject s = canvas.getSelected();
                if (s == null) return;
                Point2D.Double a = s.getAnchor();
                s.translate(v.x - a.x, v.y - a.y);
                canvas.repaint();
            });

            rotationField.setOnValueChanged(v -> {
                SceneObject s = canvas.getSelected();
                if (s == null) return;
                s.setRotation(Math.toRadians(v.z)); // use Z as 2D rotation
                canvas.repaint();
            });

            scaleField.setOnValueChanged(v -> {
                SceneObject s = canvas.getSelected();
                if (s == null) return;
                s.scaleFromAnchor(v.x, v.y);
                canvas.repaint();
                scaleField.setValue(1, 1, 1); // treat as delta
            });


            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Properties"));
            panel.add(scroll, BorderLayout.CENTER);
            panel.setMinimumSize(new Dimension(180, 120));
            return panel;
        }

        private JComponent createToolbar() {

            JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));

            JComboBox<Tool> toolBox = new JComboBox<>(Tool.values());
            toolBox.setSelectedItem(Tool.SELECT_MOVE);
            toolBox.addActionListener(e -> {
                Tool t = (Tool) toolBox.getSelectedItem();
                canvas.setTool(t);
                status.setText("Tool: " + t);
            });

            JButton clearBtn = new JButton("Clear");
            clearBtn.addActionListener(e -> {
                canvas.clearScene();
                status.setText("Scene cleared");
            });

            JButton loadSpriteBtn = new JButton("Load Sprite");
            loadSpriteBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                int r = chooser.showOpenDialog(SceneEditorPanel.this);
                if (r == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    try {
                        BufferedImage img = ImageIO.read(f);
                        if (img != null) {
                            canvas.setPendingSprite(img, f.getName());
                            canvas.setTool(Tool.ADD_SPRITE);
                            toolBox.setSelectedItem(Tool.ADD_SPRITE);
                            status.setText("Sprite loaded: " + f.getName() + " (click to place)");
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(SceneEditorPanel.this,
                                "Failed to load image: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            bar.add(new JLabel("Tool:"));
            bar.add(toolBox);
            bar.add(loadSpriteBtn);
            bar.add(clearBtn);
            bar.add(new JLabel("Tips: wheel=zoom, middle-drag=pan, left-drag object=move"));
            return bar;


        }

        private JComponent createStatusBar() {
            JPanel p = new JPanel(new BorderLayout());
            p.setBorder(new EmptyBorder(4, 8, 4, 8));
            p.add(status, BorderLayout.WEST);
            return p;
        }
    }

}
