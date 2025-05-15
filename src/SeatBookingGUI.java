import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class SeatBookingGUI extends JFrame {
    private final JButton[][] seatButtonsA = new JButton[9][2];
    private final JButton[][] seatButtonsB = new JButton[11][2];
    private final Set<String> selectedSeats = new HashSet<>();
    private String selectedSeat = "";

    private final String[] rute = Trayek.rute;

    private final String kasir; // Nama kasir yang login

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
                "<html><div style='font-family:Segoe UI; padding:8px; background:#cceeff;'>" +
                        "<b>Kasir:</b> %s &nbsp; <b>Tiket:</b> %d &nbsp; <b>Total:</b> Rp%,.0f" +
                        "</div></html>",
                kasir, count, total
        );
        JLabel lblHeader = new JLabel(headerText);

        String[] columnNames = {"Kursi", "Nama", "NIK", "HP", "Asal", "Tujuan", "Harga"};
        JTable table = new JTable(data, columnNames);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(24);
        table.setShowGrid(true);
        table.setGridColor(Color.GRAY);
        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(176, 196, 222));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(700, 450));

        // Panel untuk header sama tabel
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                this,
                panel,
                "Ringkasan Penjualan",
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

//    private void openConfirmationDialog(){
//        JDialog confirmationDialog = new JDialog(this, "Confirmation Pop Up", true);
//        confirmationDialog.setLayout(new GridLayout(2, 2, 5, 5));
//        confirmationDialog.setSize(300, 50);
//        confirmationDialog.setLocationRelativeTo(this);
//
//        JPanel contentPanel = new JPanel();
//        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
//        contentPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
//
//        JLabel confirmationLabel = new JLabel("Apakah anda ingin menghapus data tiket ?", SwingConstants.CENTER);
//
//        JButton yesBtn = new JButton("Ya, saya yakin");
//
//        contentPanel.add(confirmationLabel);
//        confirmationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//        contentPanel.add(yesBtn);
//        yesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        confirmationDialog.getContentPane().add(contentPanel);
//        confirmationDialog.pack();
//
//        yesBtn.setBackground(Color.RED);
//        yesBtn.setOpaque(true);
//        yesBtn.setBorderPainted(false);
//        yesBtn.setContentAreaFilled(true);
//        yesBtn.setFocusPainted(false);
//        yesBtn.setForeground(Color.WHITE);
//
//
//        yesBtn.addActionListener(e -> {
//            hapusData();
//            turnGreen();
//            confirmationDialog.dispose();
//        });
//
//        confirmationDialog.setVisible(true);
//    }

    private void openPassengerDialog() {
        JDialog dialog = new JDialog(this, "Data Penumpang", true);
        dialog.setLayout(new GridLayout(7, 2, 10, 5));
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(this);

        JTextField namaField = new JTextField();
        JTextField nikField = new JTextField();
        JTextField hpField = new JTextField();
        JComboBox<String> naikCombo = new JComboBox<>(rute);
        JComboBox<String> turunCombo = new JComboBox<>(rute);
        JTextField hargaField = new JTextField();
        hargaField.setEditable(false); //agar tidak bisa diubah user makanya false

        //auto update harga
        ActionListener updateHarga = e -> {
            String asal   = (String) naikCombo.getSelectedItem();
            String tujuan = (String) turunCombo.getSelectedItem();
            Trayek trayek = new Trayek(asal, tujuan);
            double h      = trayek.hargaTiket();
            String hStr   = String.format(
                    Locale.forLanguageTag("id"),
                    "Rp%,.0f",
                    h
            );
            hargaField.setText(hStr);
        };
        naikCombo.addActionListener(updateHarga);
        turunCombo.addActionListener(updateHarga);
        updateHarga.actionPerformed(null);

        dialog.add(new JLabel("Nama:"));
        dialog.add(namaField);
        dialog.add(new JLabel("NIK:"));
        dialog.add(nikField);
        dialog.add(new JLabel("No HP:"));
        dialog.add(hpField);
        dialog.add(new JLabel("Keberangkatan:"));
        dialog.add(naikCombo);
        dialog.add(new JLabel("Tujuan:"));
        dialog.add(turunCombo);
        dialog.add(new JLabel("Harga:"));
        dialog.add(hargaField);

        JButton submitBtn = new JButton("Konfirmasi");
        dialog.add(new JLabel()); // spacer
        dialog.add(submitBtn);

        submitBtn.addActionListener(e -> {
            String nama = namaField.getText();
            String nik = nikField.getText();
            String hp = hpField.getText();
            String naik = (String) naikCombo.getSelectedItem();
            String turun = (String) turunCombo.getSelectedItem();

            // Validasi input kosong
            if (nama.isEmpty() || nik.isEmpty() || hp.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Data tidak terisi penuh!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi hanya bisa huruf tanpa angka
            if (!nama.matches("[A-Za-z\\s]+")) {
                JOptionPane.showMessageDialog(dialog, "Nama hanya boleh berisi huruf dan spasi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi nik 16 digit
            if (!nik.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(dialog, "NIK harus terdiri dari 16 digit angka!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi no hp
            if (!hp.matches("\\d+")) {
                JOptionPane.showMessageDialog(dialog, "Nomor HP hanya boleh berisi angka!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi naik dan turun sama
            if (naik.equals(turun)) {
                JOptionPane.showMessageDialog(dialog, "Titik keberangkatan tidak boleh sama dengan tujuan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Penumpang penumpang = new Penumpang(nama, nik, hp);

            Trayek trayek = new Trayek(naik, turun);

            Tiket tiket = new Tiket(penumpang, trayek);

            double harga = trayek.hargaTiket();

            // setelah menyimpan data, tampilkan dialog dengan opsi Cetak Tiket & OK
            String message = "<html><body style='font-family:Segoe UI; padding:10px; background:#fff;'>"
                    + "<div style='text-align:center;'>"
                    +   "<div style='font-size:40px; color:#27ae60;'>✔</div>"
                    +   "<div style='font-size:20px; font-weight:bold; color:#34495e; margin-top:5px;'>Pembayaran Berhasil</div>"
                    +   "<div style='font-size:14px; color:#7f8c8d; margin:10px 0;'>Terima kasih! Silahkan cetak tiket Anda.</div>"
                    +   "<div style='font-size:16px; font-weight:bold; color:#2980b9;'>Rp" + harga + "</div>"
                    + "</div></body></html>";

            // tombol cetak tiket dan oke
            Object[] options = {"Cetak Tiket", "Kembali"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    message,
                    "Sukses",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            // jika user pilih Cetak Tiket, panggil method cetak
            if (choice == JOptionPane.YES_OPTION) {
                System.out.println("YESfdkshkasjhksd");
              PrintTicketFrame sda =  new PrintTicketFrame(penumpang, trayek, harga);
              sda.setVisible(true);

            }

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
