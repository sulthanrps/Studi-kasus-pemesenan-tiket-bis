import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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

        JButton hapusButton = new JButton("Reset");
        add(hapusButton, BorderLayout.SOUTH);
        hapusButton.setSize(200, 20);
        hapusButton.addActionListener(e -> {
            openConfirmationDialog();
        });

        setSize(600, 450);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void hapusData(){
        try (FileWriter writer = new FileWriter("data.txt")) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JPanel createSeatLayout(JButton[][] buttons, int rows, int cols, String prefix) {
        JPanel panel = new JPanel(new GridLayout(rows, cols, 5, 5));
        int counter = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String seatID = prefix + counter++;
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

    private void openConfirmationDialog(){
        JDialog confirmationDialog = new JDialog(this, "Confirmation Pop Up", true);
        confirmationDialog.setLayout(new GridLayout(2, 2, 5, 5));
        confirmationDialog.setSize(300, 50);
        confirmationDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(10, 10, 0, 10));

        JLabel confirmationLabel = new JLabel("Apakah anda ingin menghapus data tiket ?", SwingConstants.CENTER);

        JButton yesBtn = new JButton("Ya, saya yakin");

        contentPanel.add(confirmationLabel);
        confirmationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(yesBtn);
        yesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmationDialog.getContentPane().add(contentPanel);
        confirmationDialog.pack();

        yesBtn.setBackground(Color.RED);
        yesBtn.setOpaque(true);
        yesBtn.setBorderPainted(false);
        yesBtn.setContentAreaFilled(true);
        yesBtn.setFocusPainted(false);
        yesBtn.setForeground(Color.WHITE);


        yesBtn.addActionListener(e -> {
            hapusData();
            turnGreen();
            confirmationDialog.dispose();
        });

        confirmationDialog.setVisible(true);
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

            Penumpang penumpang = new Penumpang(nama, nik, hp);

            Trayek trayek = new Trayek(naik, turun);

            Tiket tiket = new Tiket(penumpang, trayek);

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

            loadPassengers();

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

    private void loadPassengers() {
        loadBookedSeats();
        for(String seat : selectedSeats){
            for(int i = 0; i < seatButtonsA.length; i++){
                for(int j = 0; j < seatButtonsA[i].length; j++){
                    if(seatButtonsA[i][j].getText().equals(seat)){
                        seatButtonsA[i][j].setBackground(Color.RED);
                        seatButtonsA[i][j].setEnabled(false);
                    }
                }
            }

            for(int i = 0; i < seatButtonsB.length; i++){
                for(int j = 0; j < seatButtonsB[i].length; j++){
                    if(seatButtonsB[i][j].getText().equals(seat)){
                        seatButtonsB[i][j].setBackground(Color.RED);
                        seatButtonsB[i][j].setEnabled(false);
                    }
                }
            }
        }
    }

    private void turnGreen(){
        for(int i = 0; i < seatButtonsA.length; i++){
            for(int j = 0; j < seatButtonsA[i].length; j++){
                seatButtonsA[i][j].setBackground(Color.GREEN);
                seatButtonsA[i][j].setEnabled(true);
            }
        }

        for(int i = 0; i < seatButtonsB.length; i++){
            for(int j = 0; j < seatButtonsB[i].length; j++){
                seatButtonsB[i][j].setBackground(Color.GREEN);
                seatButtonsB[i][j].setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SeatBookingGUI::new);
    }
}
