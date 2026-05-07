public class Porte extends Ouverture {
    
    private int idPorte;
    private double largeur;
    private double longueur;

    public Porte(int id, double lar, double lon) {
        idPorte = id;
        largeur = lar;
        longueur = lon;
    }

    public void setIdPorte(int id) {
        idPorte = id;
    }

    public void setLargeur(double lar) {
        largeur = lar;
    }

    public void setLongueur(double lon) {
        longueur = lon;
    }

    public int getIdPorte() {
        return idPorte;
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
