public class Isolation extends Revetement {

    private String idIsolation;
    private double prix;

    public Isolation(String id, double prix) {
        idIsolation = id;
        this.prix = prix;
    }

    public void setIdIsolation(String id) {
        idIsolation = id;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getIdIsolation() {
        return idIsolation;
    }

    public double getPrix() {
        return prix;
    }

    @Override
    public double montant() {
        return prix;
    }
}