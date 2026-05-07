public class Tremie extends Ouverture {
    
    private int idTremie;
    private double rayon;

    public Tremie(int id, double r) {
        idTremie = id;
        rayon = r;
    }

    public void setIdTremie(int id) {
        idTremie = id;
    }

    public void setRayon(double r) {
        rayon = r;
    }

    public int getIdTremie() {
        return idTremie;
    }

    public double getRayon() {
        return rayon;
    }

    @Override
    public double surface() {
        return Math.PI*rayon*rayon;
    }

}
