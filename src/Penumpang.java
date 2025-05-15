public class Penumpang {
    private String nama;
    private String nik;
    private String noHp;

    public Penumpang(String nama, String nik, String noHp) {
        this.nama = nama;
        this.nik = nik;
        this.noHp = noHp;
    }

    public String getNama() {
        return nama;
    }

    public String getNik() {
        return nik;
    }

    public String getHp() {
        return noHp;
    }
}