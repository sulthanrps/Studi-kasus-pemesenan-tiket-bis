import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrintTicketFrame extends JFrame {
    public PrintTicketFrame(Penumpang penumpang, Trayek trayek, double harga) {
        super("Pratinjau Tiket");
        // format harga & waktu
        String hargaStr = String.format(Locale.forLanguageTag("id"), "Rp%,.0f", harga);
        String waktu    = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("id"))
                .format(new Date());

        // PANEL TIKET
        JPanel ticketPanel = new JPanel(new BorderLayout());
        ticketPanel.setBackground(Color.WHITE);
        ticketPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // HEADER
        String hdr = "<html><div style='text-align:center; font-family:Segoe UI; padding:10px;'>"
                + "<span style='font-size:18px; font-weight:bold; color:#2980b9;'>TIKET BUS EKA</span><br>"
                + "<span style='font-size:12px; color:#7f8c8d;'>" + waktu + "</span>"
                + "</div></html>";
        ticketPanel.add(new JLabel(hdr, SwingConstants.CENTER), BorderLayout.NORTH);

        // BODY
        String body = "<html><div style='font-family:Segoe UI; padding:10px;'>"
                + "<b>Nama:</b> "   + penumpang.getNama()   + "<br>"
                + "<b>NIK:</b> "    + penumpang.getNik()    + "<br>"
                + "<b>No HP:</b> "  + penumpang.getHp()     + "<br>"
                + "<b>Asal:</b> "   + trayek.getAsal()      + "<br>"
                + "<b>Tujuan:</b> " + trayek.getTujuan()    + "<br>"
                + "<b>Harga:</b> <span style='color:#27ae60;'>" + hargaStr + "</span>"
                + "</div></html>";
        JLabel lblBody = new JLabel(body);
        lblBody.setBorder(new EmptyBorder(10,10,10,10));
        ticketPanel.add(lblBody, BorderLayout.CENTER);

        // susun frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(ticketPanel, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new PrintTicketFrame(new Penumpang("Dinda", "23112", "323"), new Trayek("wila", "sad"), 34);
    }
}
