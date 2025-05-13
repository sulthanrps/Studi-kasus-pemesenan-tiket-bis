import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Kasir {
    private String username;
    private String password;

    public Kasir(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Fungsi login untuk cek ke file "kasir.txt"
    public boolean login() {
        File file = new File("kasir.txt");

        if (!file.exists()) {
            // Kalau file belum ada, langsung gagal login
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Ini buat baca setiap baris. Format file: username,password
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 2) {
                    String user = parts[0].trim();
                    String pass = parts[1].trim();

                    // Cocokkin
                    if (user.equals(username) && pass.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Bisa juga kasih pesan error di UI
        }

        return false; // Kalau tidak ada yang cocok
    }
}
