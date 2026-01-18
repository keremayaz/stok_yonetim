package tr.model;

public class Urun {
    private int id;
    private String ad;
    private int kategoriId;
    private int stok;
    private double fiyat;
    
    // Ekran için eklenen alan
    private String kategoriAd;

    public Urun() {}

    public Urun(int id, String ad, int kategoriId, int stok, double fiyat) {
        this.id = id;
        this.ad = ad;
        this.kategoriId = kategoriId;
        this.stok = stok;
        this.fiyat = fiyat;
    }
    
    // Genişletilmiş Constructor
    public Urun(int id, String ad, int kategoriId, String kategoriAd, int stok, double fiyat) {
        this.id = id;
        this.ad = ad;
        this.kategoriId = kategoriId;
        this.kategoriAd = kategoriAd;
        this.stok = stok;
        this.fiyat = fiyat;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public int getKategoriId() { return kategoriId; }
    public void setKategoriId(int kategoriId) { this.kategoriId = kategoriId; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public double getFiyat() { return fiyat; }
    public void setFiyat(double fiyat) { this.fiyat = fiyat; }

    public String getKategoriAd() { return kategoriAd; }
    public void setKategoriAd(String kategoriAd) { this.kategoriAd = kategoriAd; }

    @Override
    public String toString() {
        return ad + " (Stok: " + stok + ")";
    }
}
