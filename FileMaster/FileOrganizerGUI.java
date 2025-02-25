package FileMaster;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class FileOrganizerGUI extends JFrame {

    private JTextField organizeDirField, dupDirField1, dupDirField2;
    private JTextArea organizeLogArea, dupLogArea;

    public FileOrganizerGUI() {
        setTitle("File Organizer and Duplicate Finder");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Prevents closing main program
        setSize(600, 500);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Organize Files", createOrganizePanel());
        tabbedPane.addTab("Find Duplicates", createDuplicatePanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createOrganizePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        organizeDirField = new JTextField();
        JButton browseButton = new JButton("Browse");
        JButton organizeButton = new JButton("Organize Files");
        organizeLogArea = new JTextArea();
        organizeLogArea.setEditable(false);

        setupGrid(topPanel, gbc, new JLabel("Select Directory to Organize:"), organizeDirField, browseButton, 0);
        setupGrid(topPanel, gbc, organizeButton, 1);

        browseButton.addActionListener(e -> selectDirectory(organizeDirField));
        organizeButton.addActionListener(e -> organizeFiles());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(organizeLogArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDuplicatePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dupDirField1 = new JTextField();
        dupDirField2 = new JTextField();
        JButton browseButton1 = new JButton("Browse");
        JButton browseButton2 = new JButton("Browse");
        JButton findDupButton = new JButton("Find Duplicates");
        dupLogArea = new JTextArea();
        dupLogArea.setEditable(false);

        setupGrid(topPanel, gbc, new JLabel("Select First Directory:"), dupDirField1, browseButton1, 0);
        setupGrid(topPanel, gbc, new JLabel("Select Second Directory:"), dupDirField2, browseButton2, 1);
        setupGrid(topPanel, gbc, findDupButton, 2);

        browseButton1.addActionListener(e -> selectDirectory(dupDirField1));
        browseButton2.addActionListener(e -> selectDirectory(dupDirField2));
        findDupButton.addActionListener(e -> findDuplicates());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(dupLogArea), BorderLayout.CENTER);
        return panel;
    }

    private void selectDirectory(JTextField textField) {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void organizeFiles() {
        final String dirPath = organizeDirField.getText().trim();
        if (dirPath.isEmpty()) {
            showError("Please select a directory.");
            return;
        }
        executeTask(() -> FileOrganizerUtil.organizeFiles(dirPath, organizeLogArea::append), organizeLogArea);
    }

    private void findDuplicates() {
        final String dir1 = dupDirField1.getText().trim();
        final String dir2 = dupDirField2.getText().trim();
        if (dir1.isEmpty() || dir2.isEmpty()) {
            showError("Please select both directories.");
            return;
        }
        executeTask(() -> FileOrganizerUtil.findDuplicates(dir1, dir2, dupLogArea::append), dupLogArea);
    }

    private void executeTask(Runnable task, JTextArea logArea) {
        logArea.append("Starting task...\n");
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                task.run();
                return null;
            }
            @Override
            protected void done() {
                logArea.append("Task complete.\n");
            }
        };
        worker.execute();
    }

    private void setupGrid(JPanel panel, GridBagConstraints gbc, JComponent comp1, JTextField textField, JButton button, int y) {
        gbc.gridx = 0; gbc.gridy = y; panel.add(comp1, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; panel.add(textField, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(button, gbc);
    }

    private void setupGrid(JPanel panel, GridBagConstraints gbc, JButton button, int y) {
        gbc.gridx = 1; gbc.gridy = y; panel.add(button, gbc);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new FileOrganizerGUI().setVisible(true));
    }
}
