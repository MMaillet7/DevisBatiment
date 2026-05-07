public class Fenetre extends Ouverture {
    
    private int idFenetre;
    private double largeur;
    private double longueur;

    public Fenetre(int id, double lar, double lon) {
        idFenetre = id;
        largeur = lar;
        longueur = lon;
    }

    public void setIdFenetre(int id) {
        idFenetre = id;
    }

    public void setLargeur(double lar) {
        largeur = lar;
    }

    public void setLongueur(double lon) {
        longueur = lon;
    }

    public int getIdFenetre() {
        return idFenetre;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getLongueur() {
        return longueur;
    }

    @Override
    public double surface() {
        return largeur*longueur;
    }

}
