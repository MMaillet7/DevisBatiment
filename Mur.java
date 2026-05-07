public class Mur {
    
    private int idMur;
    private Coin debut;
    private Coin fin;

    public Mur(int id, Coin d, Coin f) {
        idMur = id;
        debut = d;
        fin = f;
    }

    public void setIdMur(int id) {
        idMur = id;
    }

    public void setDebut(Coin d) {
        debut = d;
    }

    public void setFin(Coin f) {
        fin = f;
    }

    public int getIdMur() {
        return idMur;
    }

    public Coin getDebut() {
        return debut;
    }

    public Coin getFin() {
        return fin;
    }

    @Override
    public String toString() {
        return "["+debut+";"+fin+"]";
    }

    public double longueur() {
        return Math.sqrt(Math.pow(debut.getCx()-fin.getCx(),2)+Math.pow(debut.getCy()-fin.getCy(),2));
    }

    public double surface(double hauteur) {
        return this.longueur()*hauteur;
    }

    public void dessiner() {}

}
