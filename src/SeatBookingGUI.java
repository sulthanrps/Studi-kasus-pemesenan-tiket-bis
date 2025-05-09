import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class SeatBookingGUI extends JFrame {
    private final JButton[][] seatButtonsA = new JButton[9][2];
    private final JButton[][] seatButtonsB = new JButton[11][2];
    private final Set<String> selectedSeats = new HashSet<>();
    private String selectedSeat = "";

    private final String[] rute = Trayek.rute;

    public SeatBookingGUI() {
        setTitle("Pemesanan Kursi Eka Bus");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load booked seats
        loadBookedSeats();

        // Kursi Panel
        JPanel seatPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        seatPanel.add(createSeatLayout(seatButtonsA, 9, 2, "A"));
        seatPanel.add(createSeatLayout(seatButtonsB, 11, 2, "B"));
        add(seatPanel, BorderLayout.NORTH);

        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createSeatLayout(JButton[][] buttons, int rows, int cols, String prefix) {
        JPanel panel = new JPanel(new GridLayout(rows, cols, 5, 5));
        int counter = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
//                if (prefix.equals("B") && i == 5 && j > 1) continue; // B21-B22 only

                String seatID = prefix + counter++;
                System.out.println(seatID);
                JButton seatBtn = new JButton(seatID);

                seatBtn.setBackground(Color.GREEN);
                seatBtn.setOpaque(true);
                seatBtn.setBorderPainted(false);
                seatBtn.setContentAreaFilled(true);

                if (selectedSeats.contains(seatID)) {
                    seatBtn.setBackground(Color.RED);
                    seatBtn.setEnabled(false);
                } else {
                    seatBtn.addActionListener(e -> {
                        selectedSeat = seatBtn.getText();
                        openPassengerDialog();
                    });
                }
                seatBtn.setFocusPainted(false);
                buttons[i][j] = seatBtn;
                panel.add(seatBtn);
            }
        }
        panel.setBorder(BorderFactory.createTitledBorder("Kursi " + prefix));
        return panel;
    }

    private void openPassengerDialog() {
        JDialog dialog = new JDialog(this, "Data Penumpang", true);
        dialog.setLayout(new GridLayout(6, 2, 10, 5));
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(this);

        JTextField namaField = new JTextField();
        JTextField nikField = new JTextField();
        JTextField hpField = new JTextField();
        JComboBox<String> naikCombo = new JComboBox<>(rute);
        JComboBox<String> turunCombo = new JComboBox<>(rute);

        dialog.add(new JLabel("Nama:"));
        dialog.add(namaField);
        dialog.add(new JLabel("NIK:"));
        dialog.add(nikField);
        dialog.add(new JLabel("No HP:"));
        dialog.add(hpField);
        dialog.add(new JLabel("Naik di:"));
        dialog.add(naikCombo);
        dialog.add(new JLabel("Turun di:"));
        dialog.add(turunCombo);

        JButton submitBtn = new JButton("Konfirmasi");
        dialog.add(new JLabel()); // spacer
        dialog.add(submitBtn);

        submitBtn.addActionListener(e -> {
            String nama = namaField.getText();
            String nik = nikField.getText();
            String hp = hpField.getText();
            String naik = (String) naikCombo.getSelectedItem();
            String turun = (String) turunCombo.getSelectedItem();

            if (nama.isEmpty() || nik.isEmpty() || hp.isEmpty() || naik.equals(turun)) {
                JOptionPane.showMessageDialog(dialog, "Data tidak lengkap atau naik & turun sama!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Trayek trayek = new Trayek(naik, turun);

            double harga = trayek.hargaTiket();
            JOptionPane.showMessageDialog(dialog, "Pemesanan berhasil!\nHarga Tiket: Rp" + harga);


            try (FileWriter writer = new FileWriter("data.txt", true)) {
                writer.write(selectedSeat + ", " + nama + ", " + nik + ", " + hp + ", " + naik + ", " + turun + ", " + harga + "\n");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dialog, "Gagal menyimpan data!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            namaField.setText("");
            nikField.setText("");
            hpField.setText("");
            naikCombo.setSelectedIndex(0);
            turunCombo.setSelectedIndex(0);

            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void loadBookedSeats() {
        File file = new File("data.txt");
        if (!file.exists()) return;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length > 0) {
                    selectedSeats.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca file data.txt");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SeatBookingGUI::new);
    }
}
