import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NoteApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}

class LoginScreen extends JFrame {
    public LoginScreen() {
        setTitle("Creador de Notas - Login");
        setSize(600, 450); // Increased size of the login window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(150, 25)); // Reduced size

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 25)); // Reduced size

        JButton loginButton = new JButton("Iniciar Sesión");
        JButton createAccountButton = new JButton("Crear Cuenta");

        JLabel emailLabel = new JLabel("Correo Electrónico:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);

        JPanel photoPanel = new JPanel();
        photoPanel.setPreferredSize(new Dimension(200, 200)); 

        try {
            ImageIcon originalIcon = new ImageIcon("image.jpg");
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Updated size to 200x200
            JLabel photoLabel = new JLabel(new ImageIcon(scaledImage));
            photoPanel.add(photoLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("No se pudo cargar la imagen", SwingConstants.CENTER);
            photoPanel.add(errorLabel);
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(photoPanel, BorderLayout.EAST);

        add(mainPanel);

        createAccountButton.addActionListener(e -> {
            new CreateAccountScreen();
            dispose();
        });

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (GestionUsuarios.iniciarSesion(email, password)) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                new NotesScreen(email);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas o usuario no registrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}

class CreateAccountScreen extends JFrame {
    public CreateAccountScreen() {
        setTitle("Creador de Notas - Crear Cuenta");
        setSize(600, 450); // Increased size to match LoginScreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(150, 25)); // Reduced size

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 25)); // Reduced size

        JButton registerButton = new JButton("Registrar");

        JLabel emailLabel = new JLabel("Correo Electrónico:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);

        JPanel photoPanel = new JPanel();
        photoPanel.setPreferredSize(new Dimension(200, 200)); // Adjusted size for the photo panel

        try {
            ImageIcon originalIcon = new ImageIcon("image.jpg");
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Updated size to 200x200
            JLabel photoLabel = new JLabel(new ImageIcon(scaledImage));
            photoPanel.add(photoLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("No se pudo cargar la imagen", SwingConstants.CENTER);
            photoPanel.add(errorLabel);
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(photoPanel, BorderLayout.EAST);

        add(mainPanel);

        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String result = GestionUsuarios.registrarUsuario(email, password);
                if ("success".equals(result)) {
                    JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    new NotesScreen(email);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}

class NotesScreen extends JFrame {
    private final File userFolder;

    public NotesScreen(String email) {
        setTitle("Creador de Notas - " + email);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userFolder = new File("Usuarios", email);
        if (!userFolder.exists()) {
            userFolder.mkdir();
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultListModel<String> notesModel = new DefaultListModel<>();
        JList<String> notesList = new JList<>(notesModel);
        JScrollPane scrollPane = new JScrollPane(notesList);

        JTextField noteField = new JTextField();
        JButton addButton = new JButton("Añadir Nota");
        JButton editButton = new JButton("Editar Nota");
        JButton deleteButton = new JButton("Borrar Nota");
        JButton logoutButton = new JButton("Cerrar Sesión");
        JButton exitButton = new JButton("Salir");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(noteField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(logoutButton);
        bottomPanel.add(exitButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        loadNotes(notesModel);

        addButton.addActionListener(e -> {
            String note = noteField.getText().trim();
            if (!note.isEmpty()) {
                notesModel.addElement(note);
                saveNoteToFile(note);
                noteField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "La nota no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int selectedIndex = notesList.getSelectedIndex();
            if (selectedIndex != -1) {
                String oldNote = notesModel.getElementAt(selectedIndex);
                String newNote = JOptionPane.showInputDialog(this, "Edita tu nota:", oldNote);
                if (newNote != null && !newNote.trim().isEmpty()) {
                    notesModel.set(selectedIndex, newNote);
                    deleteNoteFromFile(oldNote);
                    saveNoteToFile(newNote);
                } else {
                    JOptionPane.showMessageDialog(this, "La nota no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una nota para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedIndex = notesList.getSelectedIndex();
            if (selectedIndex != -1) {
                String note = notesModel.getElementAt(selectedIndex);
                notesModel.remove(selectedIndex);
                deleteNoteFromFile(note);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una nota para borrar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        logoutButton.addActionListener(e -> {
            new LoginScreen();
            dispose();
        });

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void loadNotes(DefaultListModel<String> notesModel) {
        File[] noteFiles = userFolder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (noteFiles != null) {
            for (File noteFile : noteFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(noteFile))) {
                    String note = reader.readLine();
                    if (note != null) {
                        notesModel.addElement(note);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveNoteToFile(String note) {
        File noteFile = new File(userFolder, note.hashCode() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(noteFile))) {
            writer.write(note);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteNoteFromFile(String note) {
        File noteFile = new File(userFolder, note.hashCode() + ".txt");
        if (noteFile.exists()) {
            noteFile.delete();
        }
    }
}