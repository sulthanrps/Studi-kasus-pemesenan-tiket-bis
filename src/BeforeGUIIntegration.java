import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class BeforeGUIIntegration {
    static void seeLayout(String[][] seat, boolean[][] status){
        int counter = 0;
        for (int i = 0; i < seat.length; i++) {
            for (int j = 0; j < seat[i].length; j++) {
                System.out.print(seat[i][j] + " : " + status[i][j] + ", ");
                counter++;
                if(counter == 3) {
                    System.out.println();
                    counter = 0;
                };
            }
        }
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String[][] seatLayout = {
                {"A1", "A2", "A3"},
                {"A4", "A5", "A6"},
                {"A7", "A8", "A9"},
                {"A10", "A11", "A12"},
                {"A13", "A14", "A15"},
                {"A16", "A17", "A18"}
        };

        String[][] secondarySeatLayout = {
                {"B1", "B2", "B3", "B4"},
                {"B5", "B6", "B7", "B8"},
                {"B9", "B10", "B11", "B12"},
                {"B13", "B14", "B15", "B16"},
                {"B17", "B18", "B19", "B20"},
                {"B21", "B22"}
        };

        boolean[][] seatLayoutStatus = {
                {false, false, false},
                {false, false, false},
                {false, false, false},
                {false, false, false},
                {false, false, false},
                {false, false, false}
        };

        var filename = "data.txt";
        var file = new File(filename);

        if(file.exists() && file.canRead()) {
            try (var reader = new BufferedReader(new FileReader(file))) {
                var line = reader.readLine();

                while(line != null) {
                    String[] splitted = line.split(",");

                    String seat = splitted[0];

                    for(int i = 0; i < seatLayout.length; i++) {
                        for(int j = 0; j < seatLayout[i].length; j++) {
                            if(seatLayout[i][j].equalsIgnoreCase(seat)) {
                                seatLayoutStatus[i][j] = true;
                            }
                        }
                    }

                    line = reader.readLine();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        seeLayout(seatLayout, seatLayoutStatus);

        System.out.print("Pilih seat : "); String seatChoosen = input.nextLine();
        System.out.print("Masukkan Nama : "); String nama = input.nextLine();
        System.out.print("Masukkan NIK : "); String nik = input.nextLine();
        System.out.print("Masukkan No HP : "); String hp = input.nextLine();
        System.out.println();
        String[] rute = Trayek.rute;
        for(String s : rute) {
            System.out.println(s);
        }
        System.out.print("Masukkan stasiun tempat anda naik : "); String naik = input.nextLine();
        System.out.println();
        for(String s : rute){
            if(!s.equalsIgnoreCase(naik)) {
                System.out.println(s);
            }
        }
        System.out.print("Masukkan stasiun tempat anda turun : "); String turun = input.nextLine();

        Penumpang penumpang = new Penumpang(nama, nik, hp);

        Trayek trayek = new Trayek(naik, turun);

        System.out.println("Harga Tiket : " + trayek.hargaTiket());

        Tiket tiket = new Tiket(penumpang, trayek);

        try (var writer = new FileWriter("data.txt", true)) {
            writer.write(seatChoosen + ", " + nama + ", " + nik + ", " + hp + ", " + naik + ", " + turun + "\n");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
