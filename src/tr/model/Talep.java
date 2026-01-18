package tr.model;

public class Talep {
    private int id;
    private int kullaniciId;
    private String aciklama;
    private String durum;
    
    // Ekran için eklenen alan
    private String kullaniciAdSoyad;

    public Talep() {}

    public Talep(int id, int kullaniciId, String aciklama, String durum) {
        this.id = id;
        this.kullaniciId = kullaniciId;
        this.aciklama = aciklama;
        this.durum = durum;
    }
    
    // Genişletilmiş Constructor
    public Talep(int id, int kullaniciId, String kullaniciAdSoyad, String aciklama, String durum) {
        this.id = id;
        this.kullaniciId = kullaniciId;
        this.kullaniciAdSoyad = kullaniciAdSoyad;
        this.aciklama = aciklama;
        this.durum = durum;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(int kullaniciId) { this.kullaniciId = kullaniciId; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }

    public String getKullaniciAdSoyad() { return kullaniciAdSoyad; }
    public void setKullaniciAdSoyad(String kullaniciAdSoyad) { this.kullaniciAdSoyad = kullaniciAdSoyad; }
}
