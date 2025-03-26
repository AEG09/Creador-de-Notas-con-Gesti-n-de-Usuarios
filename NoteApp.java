import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NoteApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}

class LoginScreen extends JFrame {
    public LoginScreen() {
        setTitle("Creador de Notas - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Iniciar Sesión");
        JButton createAccountButton = new JButton("Crear Cuenta");

        panel.add(new JLabel("Correo Electrónico:"));
        panel.add(emailField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton registerButton = new JButton("Registrar");

        panel.add(new JLabel("Correo Electrónico:"));
        panel.add(emailField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);

        add(panel, BorderLayout.CENTER);
        add(registerButton, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("true".equals(GestionUsuarios.registrarUsuario(email, password))) {
                JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                new LoginScreen();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "El usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}