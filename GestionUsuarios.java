import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GestionUsuarios {
    private static final String DATOS_FOLDER = "DATOS";
    private static final String USUARIOS_FOLDER = "Usuarios";
    private static final String FILE_PATH = DATOS_FOLDER + File.separator + "usuarios.txt";
    private static Map<String, String> usuarios = new HashMap<>();

    static {
        inicializarDirectorios();
        cargarUsuarios();
    }

    public static String registrarUsuario(String email, String password) {
        if (!email.contains("@")) {
            return "formato incorrecto, introduce tu correo electronico";
        }
        if (usuarios.containsKey(email)) {
            return "el nombre de usuario ya existe";
        }
        String hashedPassword = hashPassword(password);
        if (hashedPassword != null) {
            usuarios.put(email, hashedPassword);
            guardarUsuarios();
            crearCarpetaUsuario(email);
            return "success";
        }
        return "error desconocido";
    }

    public static boolean iniciarSesion(String email, String password) {
        if (!usuarios.containsKey(email)) {
            return false;
        }
        String hashedPassword = hashPassword(password);
        return hashedPassword != null && hashedPassword.equals(usuarios.get(email));
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void cargarUsuarios() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    usuarios.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
        }
    }

    private static void guardarUsuarios() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, String> entry : usuarios.entrySet()) {
                bw.write(entry.getKey() + ":" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void inicializarDirectorios() {
        File datosFolder = new File(DATOS_FOLDER);
        File usuariosFolder = new File(USUARIOS_FOLDER);
        if (!datosFolder.exists()) {
            datosFolder.mkdir();
        }
        if (!usuariosFolder.exists()) {
            usuariosFolder.mkdir();
        }
    }

    private static void crearCarpetaUsuario(String email) {
        File usuariosFolder = new File(USUARIOS_FOLDER);
        int contador = 1;
        File userFolder;
        do {
            userFolder = new File(usuariosFolder, "nombreconelqueseregistran" + contador);
            contador++;
        } while (userFolder.exists());
        userFolder.mkdir();
    }
}
