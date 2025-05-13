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

    private String kasir; // Nama kasir yang login

    public SeatBookingGUI() {
        this.kasir = "Kasir";
        initGUI();
    }

    public SeatBookingGUI(String kasirName) {
        this.kasir = kasirName;
        initGUI();
    }

    private void initGUI() {
        setTitle("Pemesanan Kursi Eka Bus - Kasir: " + kasir);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load booked seats
        loadBookedSeats();

        // Kursi Panel
        JPanel seatPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        seatPanel.add(createSeatLayout(seatButtonsA, 9, 2, "A"));
        seatPanel.add(createSeatLayout(seatButtonsB, 11, 2, "B"));
        add(seatPanel, BorderLayout.NORTH);

        JButton logoutButton = new JButton("Logout");
        add(logoutButton, BorderLayout.SOUTH);
        logoutButton.setPreferredSize(new Dimension(200, 20));
        logoutButton.addActionListener(e -> doLogout());

        setSize(600, 450);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void doLogout() {
        File file = new File("data.txt");

        // Ini array untuk tampilan di gui kotak kotaknya berapa
        int rowCount = 0;
        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().trim();
                    if (line.isEmpty()) continue;
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        rowCount++;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Preapare array sesuai jumlahnya
        Object[][] data = new Object[rowCount][7];
        int count = 0;
        double total = 0;

        if (file.exists()) {
            try (Scanner sc2 = new Scanner(file)) {
                int idx = 0;
                while (sc2.hasNextLine()) {
                    String line = sc2.nextLine().trim();
                    if (line.isEmpty()) continue;
                    String[] parts = line.split(",");
                    if (parts.length >= 7) {
                        String kursi  = parts[0].trim();
                        String nama   = parts[1].trim();
                        String nik    = parts[2].trim();
                        String hp     = parts[3].trim();
                        String asal   = parts[4].trim();
                        String tujuan = parts[5].trim();
                        double harga  = Double.parseDouble(parts[6].trim());

                        data[idx][0] = kursi;
                        data[idx][1] = nama;
                        data[idx][2] = nik;
                        data[idx][3] = hp;
                        data[idx][4] = asal;
                        data[idx][5] = tujuan;
                        data[idx][6] = harga;

                        total += harga;
                        count++;
                        idx++;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // header
        String headerText = String.format(
                "<html><b>Nama Kasir:</b> %s&nbsp;&nbsp; " +
                        "<b>Tiket Terjual:</b> %d&nbsp;&nbsp; " +
                        "<b>Total Penjualan:</b> Rp%.0f</html>",
                kasir, count, total
        );
        JLabel lblHeader = new JLabel(headerText);

        String[] columnNames = {"Kursi", "Nama", "NIK", "HP", "Asal", "Tujuan", "Harga"};
        JTable table = new JTable(data, columnNames);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(550, 250));

        // Panel untuk header sama tabel
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                this,
                panel,
                "Ringkasan Harian",
                JOptionPane.INFORMATION_MESSAGE
        );

        hapusData();
        System.exit(0);
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

            String message = "<html><body style='font-family:Segoe UI, sans-serif; padding:10px;'>"
                    + "<div style='text-align:center;'>"
                    + "<div style='font-size:50px; color:#27ae60;'>✔</div>"
                    + "<div style='font-size:22px; font-weight:bold; color:#27ae60; margin-top:10px;'>Pemesanan Berhasil!</div>"
                    + "<div style='font-size:14px; color:#555; margin-top:5px;'>Tiket telah berhasil dipesan.</div>"
                    + "<hr style='margin:15px 0; border: none; border-top: 1px solid #ddd;'>"
                    + "<div style='font-size:16px; color:#2c3e50;'>Harga Tiket:</div>"
                    + "<div style='font-size:24px; font-weight:bold; color:#2980b9;'>Rp" + harga + "</div>"
                    + "</div></body></html>";

            JOptionPane.showMessageDialog(null, message, "Massage", JOptionPane.PLAIN_MESSAGE);

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
