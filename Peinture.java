public class Peinture extends Revetement {

    private String idPeinture;
    private double prix;

    public Peinture(String id, double prix) {
        idPeinture = id;
        this.prix = prix;
    }

    public void setIdPeinture(String id) {
        idPeinture = id;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getIdPeinture() {
        return idPeinture;
    }

    public double getPrix() {
        return prix;
    }

    @Override
    public double montant() {
        return prix;
    }
}