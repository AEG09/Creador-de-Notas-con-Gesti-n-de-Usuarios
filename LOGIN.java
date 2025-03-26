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

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        // Campos de texto
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // Botones
        JButton loginButton = new JButton("Iniciar Sesión");
        JButton createAccountButton = new JButton("Crear Cuenta");

        // Añadir componentes al panel
        panel.add(new JLabel("Correo Electrónico:"));
        panel.add(emailField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);

        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);

        // Añadir paneles al frame
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Acción para el botón de crear cuenta
        createAccountButton.addActionListener(e -> {
            new CreateAccountScreen();
            dispose();
        });

        // Acción para el botón de iniciar sesión
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                // Aquí puedes redirigir al usuario a la pantalla principal del creador de notas
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

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        // Campos de texto
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // Botón de registro
        JButton registerButton = new JButton("Registrar");

        // Añadir componentes al panel
        panel.add(new JLabel("Correo Electrónico:"));
        panel.add(emailField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);

        // Añadir paneles al frame
        add(panel, BorderLayout.CENTER);
        add(registerButton, BorderLayout.SOUTH);

        // Acción para el botón de registro
        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                new LoginScreen();
                dispose();
            }
        });

        setVisible(true);
    }
}