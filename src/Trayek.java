public class Trayek {
    private String naik;
    private String turun;

    static String[] rute = {"Wilangan", "Ngawi", "Gendingan", "Solo", "Kartosuro", "Jogja", "Magelang"};

    public Trayek(String naik, String turun) {
        this.naik = naik;
        this.turun = turun;
    }

    public double hargaTiket(){
        if(naik.equalsIgnoreCase("Wilangan") && turun.equalsIgnoreCase("Ngawi") || naik.equalsIgnoreCase("Ngawi") && turun.equalsIgnoreCase("Wilangan")){
            return 35000;
        }

        else if(naik.equalsIgnoreCase("Wilangan") && turun.equalsIgnoreCase("Gendingan") || naik.equalsIgnoreCase("Gendingan") && turun.equalsIgnoreCase("Wilangan")){
            return 45000;
        }

        else if(naik.equalsIgnoreCase("Wilangan") && turun.equalsIgnoreCase("Solo") || naik.equalsIgnoreCase("Solo") && turun.equalsIgnoreCase("Wilangan")){
            return 55000;
        }

        else if(naik.equalsIgnoreCase("Wilangan") && turun.equalsIgnoreCase("Kartosuro") || naik.equalsIgnoreCase("Kartosuro") && turun.equalsIgnoreCase("Wilangan")){
            return 60000;
        }

        else if(naik.equalsIgnoreCase("Wilangan") && turun.equalsIgnoreCase("Jogja") || naik.equalsIgnoreCase("Jogja") && turun.equalsIgnoreCase("Wilangan")){
            return 70000;
        }

        else if(naik.equalsIgnoreCase("Wilangan") && turun.equalsIgnoreCase("Magelang") || naik.equalsIgnoreCase("Magelang") && turun.equalsIgnoreCase("Wilangan")){
            return 80000;
        }

        else if(naik.equalsIgnoreCase("Ngawi") && turun.equalsIgnoreCase("Gendingan") || naik.equalsIgnoreCase("Gendingan") && turun.equalsIgnoreCase("Ngawi")){
            return 25000;
        }

        else if(naik.equalsIgnoreCase("Ngawi") && turun.equalsIgnoreCase("Solo") || naik.equalsIgnoreCase("Solo") && turun.equalsIgnoreCase("Ngawi")){
            return 30000;
        }

        else if(naik.equalsIgnoreCase("Ngawi") && turun.equalsIgnoreCase("Kartosuro") || naik.equalsIgnoreCase("Kartosuro") && turun.equalsIgnoreCase("Ngawi")){
            return 40000;
        }

        else if(naik.equalsIgnoreCase("Ngawi") && turun.equalsIgnoreCase("Jogja") || naik.equalsIgnoreCase("Jogja") && turun.equalsIgnoreCase("Ngawi")){
            return 45000;
        }

        else if(naik.equalsIgnoreCase("Ngawi") && turun.equalsIgnoreCase("Magelang") || naik.equalsIgnoreCase("Magelang") && turun.equalsIgnoreCase("Ngawi")){
            return 60000;
        }

        else if(naik.equalsIgnoreCase("Gendingan") && turun.equalsIgnoreCase("Solo") || naik.equalsIgnoreCase("Solo") && turun.equalsIgnoreCase("Gendingan")){
            return 25000;
        }

        else if(naik.equalsIgnoreCase("Gendingan") && turun.equalsIgnoreCase("Kartosuro") || naik.equalsIgnoreCase("Kartosuro") && turun.equalsIgnoreCase("Gendingan")){
            return 35000;
        }

        else if(naik.equalsIgnoreCase("Gendingan") && turun.equalsIgnoreCase("Jogja") || naik.equalsIgnoreCase("Jogja") && turun.equalsIgnoreCase("Gendingan")){
            return 40000;
        }

        else if(naik.equalsIgnoreCase("Gendingan") && turun.equalsIgnoreCase("Magelang") || naik.equalsIgnoreCase("Magelang") && turun.equalsIgnoreCase("Gendingan")){
            return 55000;
        }

        else if(naik.equalsIgnoreCase("Solo") && turun.equalsIgnoreCase("Kartosuro") || naik.equalsIgnoreCase("Kartosuro") && turun.equalsIgnoreCase("Solo")){
            return 15000;
        }

        else if(naik.equalsIgnoreCase("Solo") && turun.equalsIgnoreCase("Jogja") || naik.equalsIgnoreCase("Jogja") && turun.equalsIgnoreCase("Solo")){
            return 15000;
        }

        else if(naik.equalsIgnoreCase("Solo") && turun.equalsIgnoreCase("Magelang") || naik.equalsIgnoreCase("Magelang") && turun.equalsIgnoreCase("Solo")){
            return 30000;
        }

        else if(naik.equalsIgnoreCase("Kartosuro") && turun.equalsIgnoreCase("Jogja") || naik.equalsIgnoreCase("Jogja") && turun.equalsIgnoreCase("Kartosuro")){
            return 15000;
        }

        else if(naik.equalsIgnoreCase("Kartosuro") && turun.equalsIgnoreCase("Magelang") || naik.equalsIgnoreCase("Magelang") && turun.equalsIgnoreCase("Kartosuro")){
            return 30000;
        }

        else if(naik.equalsIgnoreCase("Jogja") && turun.equalsIgnoreCase("Magelang") || naik.equalsIgnoreCase("Magelang") && turun.equalsIgnoreCase("Jogja")){
            return 15000;
        }

        else {
            return 0;
        }
    }


}
