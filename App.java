import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class App extends JFrame {
    private DefaultListModel<String> noteListModel;
    private JList<String> noteList;
    private JTextArea noteContent;
    private String userEmail;

    public App(String userEmail) {
        this.userEmail = userEmail;
        setTitle("Creador de Notas - Gestión de Notas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        noteListModel = new DefaultListModel<>();
        noteList = new JList<>(noteListModel);
        noteContent = new JTextArea();
        JButton createButton = new JButton("Crear Nota");
        JButton saveButton = new JButton("Guardar Nota");
        JButton deleteButton = new JButton("Borrar Nota");

        // Layout setup
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(noteList), BorderLayout.CENTER);
        leftPanel.add(createButton, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(noteContent), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(saveButton);
        bottomPanel.add(deleteButton);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load notes
        loadNotes();

        // Event listeners
        createButton.addActionListener(e -> createNote());
        saveButton.addActionListener(e -> saveNote());
        deleteButton.addActionListener(e -> deleteNote());
        noteList.addListSelectionListener(e -> loadNoteContent());

        setVisible(true);
    }

    private void loadNotes() {
        noteListModel.clear();
        File userFolder = getUserFolder();
        if (userFolder.exists() && userFolder.isDirectory()) {
            for (File file : userFolder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    noteListModel.addElement(file.getName().replace(".txt", ""));
                }
            }
        }
    }

    private void loadNoteContent() {
        String selectedNote = noteList.getSelectedValue();
        if (selectedNote != null) {
            File noteFile = new File(getUserFolder(), selectedNote + ".txt");
            try {
                noteContent.setText(Files.readString(noteFile.toPath()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar la nota.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createNote() {
        String noteName = JOptionPane.showInputDialog(this, "Nombre de la nueva nota:");
        if (noteName != null && !noteName.trim().isEmpty()) {
            File noteFile = new File(getUserFolder(), noteName + ".txt");
            if (noteFile.exists()) {
                JOptionPane.showMessageDialog(this, "Ya existe una nota con este nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    noteFile.createNewFile();
                    noteListModel.addElement(noteName);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al crear la nota.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void saveNote() {
        String selectedNote = noteList.getSelectedValue();
        if (selectedNote != null) {
            File noteFile = new File(getUserFolder(), selectedNote + ".txt");
            try {
                Files.writeString(noteFile.toPath(), noteContent.getText());
                JOptionPane.showMessageDialog(this, "Nota guardada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar la nota.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una nota para guardar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteNote() {
        String selectedNote = noteList.getSelectedValue();
        if (selectedNote != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de borrar esta nota?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                File noteFile = new File(getUserFolder(), selectedNote + ".txt");
                if (noteFile.delete()) {
                    noteListModel.removeElement(selectedNote);
                    noteContent.setText("");
                    JOptionPane.showMessageDialog(this, "Nota borrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al borrar la nota.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una nota para borrar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private File getUserFolder() {
        File userFolder = new File("data/" + userEmail);
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }
        return userFolder;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App("test@example.com")); // Replace with actual user email
    }
}
