import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalTime;

public class OnBoardingFrame extends JFrame {

    public OnBoardingFrame() {
        setTitle("Selamat Datang di Sistem Pemesanan Bus Eka");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(550, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Ini untuk ucapan berdasarkan waktu
        String ucapan;
        int hour = LocalTime.now().getHour();
        if (hour >= 4 && hour < 12) ucapan = "Selamat Pagi";
        else if (hour < 15)           ucapan = "Selamat Siang";
        else if (hour < 18)           ucapan = "Selamat Sore";
        else                          ucapan = "Selamat Malam";

        // Teks semangat
        String html = "<html>"
                + "<div style='text-align:center; font-family:SansSerif; color:#333;'>"
                +   "<h1 style='margin:10px 0; color:#2E86C1;'>"
                +     ucapan + ", Kasir Eka! 🚌"
                +   "</h1>"
                +   "<p style='font-size:14px; margin:8px 0;'>"
                +     "Selamat datang di Sistem Pemesanan Tiket!<br>"
                +     "Silakan pilih opsi berikut untuk melanjutkan:<br>"
                +     "<ul style='text-align:left; display:inline-block; margin-top:5px;'>"
                +       "<li><span style='font-weight:bold; color:#28A745;'>Daftar</span> – untuk kasir baru tanpa akun</li>"
                +       "<li><span style='font-weight:bold; color:#2E86C1;'>Login</span> – untuk kasir lama</li>"
                +     "</ul>"
                +   "</p>"
                +   "<p style='font-style:italic; color:#555; margin-top:15px;'>"
                +     "Klik opsi di atas untuk masuk ke dashboard akun Anda."
                +   "</p>"
                + "</div>"
                + "</html>";

        JLabel teksLabel = new JLabel(html);
        teksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        teksLabel.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Button
        JButton btnDaftar = new JButton("Daftar");
        btnDaftar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnDaftar.setPreferredSize(new Dimension(120, 40));
        btnDaftar.setBackground(new Color(39, 174, 96));
        btnDaftar.setForeground(Color.WHITE);
        btnDaftar.setFocusPainted(false);
        btnDaftar.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });

        btnDaftar.setOpaque(true);
        btnDaftar.setBorderPainted(false);
        btnDaftar.setContentAreaFilled(true);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(120, 40));
        btnLogin.setBackground(new Color(46, 134, 193));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(true);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        southPanel.setBorder(new EmptyBorder(15, 0, 20, 0));
        southPanel.add(btnDaftar);
        southPanel.add(btnLogin);

        // Assamble
        add(teksLabel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OnBoardingFrame::new);
    }
}
