import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class SeatBookingGUI extends JFrame {
    private final JButton[][] seatButtonsA = new JButton[9][2];
    private final JButton[][] seatButtonsB = new JButton[11][2];
    private Set<String> selectedSeats = new HashSet<>();
    private String selectedSeat = "";

    private final String[] rute = Trayek.rute;

    private String kasir; // Nama kasir yang login

    private List<String[]> dataPelanggan = new ArrayList<>();

    private DefaultTableModel model;
    private JTable table;

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

        JPanel bottom = new JPanel();
        JButton viewDataBtn = new JButton("Lihat Data Pelanggan");
        viewDataBtn.addActionListener(e -> openDataPelanggan());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> doLogout());
        bottom.add(viewDataBtn);
        bottom.add(logoutButton);
        add(bottom, BorderLayout.SOUTH);


        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openDataPelanggan(){
        getDataPelanggan();

        String[] columnNames = {"Kursi", "Nama", "NIK", "HP", "Asal", "Tujuan", "Harga", "Aksi"};

        model = new DefaultTableModel(columnNames, 0){
          public boolean isCellEditable(int row, int column) {
              return column == 7;
          }
        };

        for(String[] row : dataPelanggan) {
            model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], row[6], "Aksi"});
        }

        table = new JTable(model);
        table.setRowHeight(40);
        table.getColumn("Aksi").setMinWidth(200);
        table.getColumn("Aksi").setMaxWidth(300);
        table.getColumn("Aksi").setCellRenderer(new ButtonCellRenderer());
        table.getColumn("Aksi").setCellEditor(new ButtonCellEditor());

        refreshTable();

        JFrame frame = new JFrame("Data Pelanggan");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 300);
        frame.add(new JScrollPane(table));
        frame.setVisible(true);

        updateSeatLayout();
    };

    private void refreshTable() {
        model.setRowCount(0); // Hapus semua baris
        for (String[] row : dataPelanggan) {
            model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], row[6], "Aksi"});
        }
    }

    public class ButtonCellRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnEdit = new JButton("Edit");
        private final JButton btnDelete = new JButton("Delete");
        public ButtonCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            btnEdit.setBackground(Color.YELLOW);
            btnEdit.setMargin(new Insets(2, 8, 2, 8));
            btnEdit.setOpaque(true);
            btnEdit.setBorderPainted(false);
            btnEdit.setContentAreaFilled(true);
            btnEdit.setFocusable(true);

            btnDelete.setBackground(Color.RED);
            btnDelete.setOpaque(true);
            btnDelete.setBorderPainted(false);
            btnDelete.setContentAreaFilled(true);
            btnDelete.setFocusable(true);
            btnDelete.setMargin(new Insets(2, 8, 2, 8));

            add(btnEdit);
            add(btnDelete);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    public class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();
        private final JButton btnEdit = new JButton("Edit");
        private final JButton btnDelete = new JButton("Delete");

        private int selectedRow;

        public ButtonCellEditor() {
            panel.setLayout(new GridLayout(1, 2, 0, 0));
            btnEdit.setBackground(Color.YELLOW);
            btnEdit.setMargin(new Insets(2, 8, 2, 8));
            btnEdit.setOpaque(true);
            btnEdit.setBorderPainted(false);
            btnEdit.setContentAreaFilled(true);
            btnEdit.setFocusable(true);

            btnDelete.setBackground(Color.RED);
            btnDelete.setOpaque(true);
            btnDelete.setBorderPainted(false);
            btnDelete.setContentAreaFilled(true);
            btnDelete.setFocusable(true);
            btnDelete.setMargin(new Insets(2, 8, 2, 8));

            panel.add(btnEdit);
            panel.add(btnDelete);

            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                String[] getPenumpang = getDataPenumpang();
                editData(selectedRow);
                System.out.println(getPenumpang[0]);
            });

            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                String[] getPenumpang = getDataPenumpang();
                System.out.println("Hapus data kursi " + getPenumpang[0]);
                deleteData(selectedRow);
            });
        }

        public String[] getDataPenumpang(){
            for (int i = 0; i < dataPelanggan.size(); i++) {
                if(i == selectedRow){
                    return dataPelanggan.get(i);
                }
            }

            return null;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;
            return panel;
        }

        public Object getCellEditorValue() {
            return this;
        }
    }

    private void deleteData(int id) {
        // Tampilkan dialog konfirmasi
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data ini?",
                "Konfirmasi Hapus Data",
                JOptionPane.YES_NO_OPTION
        );

        // Hanya lanjutkan jika pengguna memilih "Yes"
        if (choice == JOptionPane.YES_OPTION) {
            dataPelanggan.remove(id);

            try (FileWriter writer = new FileWriter("data.txt", false)) {
                for (String[] row : dataPelanggan) {
                    writer.write(String.join(", ", row) + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            refreshTable();
            updateSeatLayout(); // Sinkronkan layout kursi
        }
    }

    private void editData(int rowIndex) {
        String[] data = dataPelanggan.get(rowIndex);
        String oldSeat = data[0].trim();

        JDialog dialog = new JDialog(this, "Edit Data Penumpang", true);
        dialog.setLayout(new GridLayout(7, 2, 10, 5));
        dialog.setSize(350, 350);
        dialog.setLocationRelativeTo(this);

        JTextField namaField = new JTextField(data[1].trim());
        JTextField nikField = new JTextField(data[2].trim());
        JTextField hpField = new JTextField(data[3].trim());
        JComboBox<String> naikCombo = new JComboBox<>(rute);
        JComboBox<String> turunCombo = new JComboBox<>(rute);
        naikCombo.setSelectedItem(data[4].trim());
        turunCombo.setSelectedItem(data[5].trim());

        // Cari kursi yang belum dipesan (kecuali kursi lama)
        JComboBox<String> kursiCombo = new JComboBox<>();
        for (int i = 1; i <= 18; i++) {
            String id = "A" + i;
            if (!selectedSeats.contains(id) || id.equals(oldSeat)) {
                kursiCombo.addItem(id);
            }
        }
        for (int i = 1; i <= 22; i++) {
            String id = "B" + i;
            if (!selectedSeats.contains(id) || id.equals(oldSeat)) {
                kursiCombo.addItem(id);
            }
        }
        kursiCombo.setSelectedItem(oldSeat);

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
        dialog.add(new JLabel("Kursi:"));
        dialog.add(kursiCombo);

        JButton saveBtn = new JButton("Simpan");
        dialog.add(new JLabel());
        dialog.add(saveBtn);

        saveBtn.addActionListener(e -> {
            String nama = namaField.getText();
            String nik = nikField.getText();
            String hp = hpField.getText();
            String naik = (String) naikCombo.getSelectedItem();
            String turun = (String) turunCombo.getSelectedItem();
            String newSeat = (String) kursiCombo.getSelectedItem();

            if (nama.isEmpty() || nik.isEmpty() || hp.isEmpty() || naik.equals(turun)) {
                JOptionPane.showMessageDialog(dialog, "Data tidak lengkap atau asal & tujuan sama!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Trayek trayek = new Trayek(naik, turun);
            double harga = trayek.hargaTiket();

            // Update data pelanggan
            String[] updatedData = {newSeat, nama, nik, hp, naik, turun, String.valueOf(harga)};
            dataPelanggan.set(rowIndex, updatedData);

            // Simpan kembali ke file
            try (FileWriter writer = new FileWriter("data.txt", false)) {
                for (String[] row : dataPelanggan) {
                    writer.write(String.join(", ", row) + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dialog, "Gagal menyimpan perubahan!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            refreshTable();
            updateSeatLayout();
            dialog.dispose();
        });

        dialog.setVisible(true);
    }


    private void getDataPelanggan(){
        File file = new File("data.txt");

        if(file.exists()){
            dataPelanggan.clear();
            try (Scanner scanFile = new Scanner(file)) {
                while (scanFile.hasNextLine()) {
                    dataPelanggan.add(scanFile.nextLine().split(","));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("Data Pelanggan Berhasil terambil !");
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

            updateSeatLayout();

            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void loadBookedSeats() {
        selectedSeats.clear();
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

    private void updateSeatLayout() {
        // Perbarui daftar kursi yang sudah dipesan
        selectedSeats.clear(); // Hapus isi sebelumnya
        File file = new File("data.txt");

        if (file.exists()) {
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

        // Update tampilan kursi berdasarkan selectedSeats
        for (JButton[] row : seatButtonsA) {
            for (JButton seatBtn : row) {
                String seatID = seatBtn.getText();
                if (selectedSeats.contains(seatID)) {
                    seatBtn.setBackground(Color.RED);
                    seatBtn.setEnabled(false);
                } else {
                    seatBtn.setBackground(Color.GREEN);
                    seatBtn.setEnabled(true);
                }
            }
        }

        for (JButton[] row : seatButtonsB) {
            for (JButton seatBtn : row) {
                String seatID = seatBtn.getText();
                if (selectedSeats.contains(seatID)) {
                    seatBtn.setBackground(Color.RED);
                    seatBtn.setEnabled(false);
                } else {
                    seatBtn.setBackground(Color.GREEN);
                    seatBtn.setEnabled(true);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SeatBookingGUI::new);
    }
}
