public class Niveau {
    
    private int idNiveau;
    private Appartement[] apparts;

    public Niveau(int id, Appartement[] apparts) {
        idNiveau = id;
        this.apparts = apparts;
    }

    public void setIdNiveau(int idNiveau) {
        this.idNiveau = idNiveau;
    }

    public int getIdNiveau() {
        return idNiveau;
    }

    public void setApparts(Appartement[] apparts) {
        this.apparts = apparts;
    }

    public Appartement[] getApparts() {
        return apparts;
    }

    public double devisNiveau() {
        double somme = 0;
        for (Appartement appart : apparts) {
            somme += appart.devisAppartement();
        }
        // il faut rajouter le revetement exterieur
        return somme;
    }

}
