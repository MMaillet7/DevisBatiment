public class Carrelage extends Revetement {

    private String idCarrelage;
    private double prix;

    public Carrelage(String id, double prix) {
        idCarrelage = id;
        this.prix = prix;
    }

    public void setIdCarrelage(String id) {
        idCarrelage = id;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getIdCarrelage() {
        return idCarrelage;
    }

    public double getPrix() {
        return prix;
    }

    @Override
    public double montant() {
        return prix;
    }
}