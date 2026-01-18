package tr.model;

public class Rol {
    private int id;
    private String ad;

    public Rol() {}

    public Rol(int id, String ad) {
        this.id = id;
        this.ad = ad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    @Override
    public String toString() {
        return ad;
    }
}
