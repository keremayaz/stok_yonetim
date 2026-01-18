package tr.model;

import java.sql.Timestamp;

public class Hareket {
    private int id;
    private int urunId;
    private int musteriId;
    private int adet;
    private Timestamp tarih;
    
    // Ekran için eklenen alanlar (Veritabanında doğrudan sütun değil, JOIN ile gelecek)
    private String urunAd;
    private String musteriAdSoyad;

    public Hareket() {}

    public Hareket(int id, int urunId, int musteriId, int adet, Timestamp tarih) {
        this.id = id;
        this.urunId = urunId;
        this.musteriId = musteriId;
        this.adet = adet;
        this.tarih = tarih;
    }
    
    // Genişletilmiş Constructor
    public Hareket(int id, int urunId, String urunAd, int musteriId, String musteriAdSoyad, int adet, Timestamp tarih) {
        this.id = id;
        this.urunId = urunId;
        this.urunAd = urunAd;
        this.musteriId = musteriId;
        this.musteriAdSoyad = musteriAdSoyad;
        this.adet = adet;
        this.tarih = tarih;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUrunId() { return urunId; }
    public void setUrunId(int urunId) { this.urunId = urunId; }

    public int getMusteriId() { return musteriId; }
    public void setMusteriId(int musteriId) { this.musteriId = musteriId; }

    public int getAdet() { return adet; }
    public void setAdet(int adet) { this.adet = adet; }

    public Timestamp getTarih() { return tarih; }
    public void setTarih(Timestamp tarih) { this.tarih = tarih; }

    public String getUrunAd() { return urunAd; }
    public void setUrunAd(String urunAd) { this.urunAd = urunAd; }

    public String getMusteriAdSoyad() { return musteriAdSoyad; }
    public void setMusteriAdSoyad(String musteriAdSoyad) { this.musteriAdSoyad = musteriAdSoyad; }
}
