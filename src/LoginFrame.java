

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginFrame() {
        // Set window
        setTitle("🚌 Login Kasir Bus Eka");
        setSize(380, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main panel dengan padding dan background putih
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);

        // Header
        JLabel lblHeader = new JLabel("Silakan Masuk sebagai Kasir", SwingConstants.CENTER);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Panel form (label + field)
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(lblUser);

        usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setToolTipText("Masukkan username Anda");
        formPanel.add(usernameField);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(lblPass);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setToolTipText("Masukkan password Anda");
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Tombol Login
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBackground(new Color(46, 134, 193));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(true);

        loginButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            Kasir kasir = new Kasir(user, pass);

            // Validasi kosong
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Username & password tidak boleh kosong.",
                        "Input Kurang",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            try {
                if (kasir.login()) {
                    // Login sukses lanjut ke buka SeatBookingGUI
                    new SeatBookingGUI(user);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Username atau password salah!",
                            "Login Gagal",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Terjadi kesalahan saat login:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(loginButton);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
