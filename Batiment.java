public class Batiment {
    
    private String idBatiment;
    private int nbrNiveaux;

    public void setIdBatiment(String idBatiment) {
        this.idBatiment = idBatiment;
    }

    public String getIdBatiment() {
        return idBatiment;
    }

    public void setNbrNiveaux(int nbrNiveaux) {
        this.nbrNiveaux = nbrNiveaux;
    }

    public int getNbrNiveaux() {
        return nbrNiveaux;
    }

    public void afficher() {
    }

    public void devisBatiment() {
    }

    public double surfaceRevetement() {
        return 1;
    }

    public double devisRevetement(int idRevetement) {
        return 1;
    }

    public void dessiner() {
    }

}
