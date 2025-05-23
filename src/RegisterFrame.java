import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class RegisterFrame extends JFrame implements ActionListener {

    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JButton btnKembali;


    public RegisterFrame() {
        // Ini buat set window
        setTitle("Registrasi Kasir Bus Eka");
        setSize(500, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Main panel dengan padding, ini aku diajarin ytb gatau bener gak guyssy
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        mainPanel.setLayout(new BorderLayout(0, 15));
        setContentPane(mainPanel);

        // Judul
        JLabel lblTitle = new JLabel("Daftar Akun Kasir Baru", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form panel (label + field)
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        JLabel lblUser = new JLabel("Username (contoh: dindaaw):");
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.setToolTipText("Masukkan username unik, misal: eka123");
        formPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Password (min.6 karakter):");
        lblPass.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setToolTipText("Minimal 6 karakter");
        formPanel.add(txtPassword);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Tombol Daftar di bawah
        JButton btnDaftar = new JButton("Daftar");
        btnDaftar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnDaftar.setBackground(new Color(46, 134, 193));
        btnDaftar.setForeground(Color.WHITE);
        btnDaftar.setFocusPainted(false);
        btnDaftar.addActionListener(this);
        btnDaftar.setOpaque(true);
        btnDaftar.setBorderPainted(false);
        btnDaftar.setContentAreaFilled(true);

        btnKembali = new JButton("Kembali");
        btnKembali.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnKembali.setBackground(Color.LIGHT_GRAY);
        btnKembali.setFocusPainted(false);
        btnKembali.addActionListener(this);
        btnKembali.setOpaque(true);
        btnKembali.setBorderPainted(false);
        btnKembali.setContentAreaFilled(true);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnKembali);
        btnPanel.add(btnDaftar);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnKembali) {
            // Balik ke halaman onboarding
            new OnBoardingFrame().setVisible(true);
            dispose();
            return;
        }

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Validasi yang di input bener atau enggak
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Mohon isi semua field.",
                    "Input Kurang", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Password minimal 6 karakter.",
                    "Password Terlalu Pendek", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            JOptionPane.showMessageDialog(this,
                    "Username hanya boleh huruf, angka, atau underscore (tanpa spasi).",
                    "Input Salah", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean adaHuruf = false;
        boolean adaAngka = false;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isLetter(ch)) adaHuruf = true;
            if (Character.isDigit(ch)) adaAngka = true;
        }

        if (!adaHuruf || !adaAngka) {
            JOptionPane.showMessageDialog(this, "Password harus mengandung huruf dan angka.");
            return;
        }

        // Simpan ke kasir.txt
        File file = new File("kasir.txt");
        try {
            if (!file.exists()) file.createNewFile();

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equalsIgnoreCase(username)) {
                    JOptionPane.showMessageDialog(this,
                            "Username sudah terpakai.",
                            "Duplikat", JOptionPane.ERROR_MESSAGE);
                    br.close();
                    return;
                }
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(username + "," + password);
            bw.newLine();
            bw.close();

            JOptionPane.showMessageDialog(this,
                    "Selamat! Akun '" + username + "' berhasil dibuat.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Lanjut ke Onboarding keren
            new OnBoardingFrame().setVisible(true);
            dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new);
    }
}
