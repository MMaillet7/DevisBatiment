public class Coin {
    
    private int idCoin;
    private double cx;
    private double cy;

    public Coin(int id, double x, double y) {
        idCoin =id;
        cx = x;
        cy = y;
    }

    public void setIdCoin(int id) {
        idCoin = id;
    }

    public void setCx(double x) {
        cx = x;
    }

    public void setCy(double y) {
        cx = y;
    }

    public int getIdCoin() {
        return idCoin;
    }

    public double getCx() {
        return cx;
    }

    public double getCy() {
        return cy;
    }

    @Override
    public String toString() {
        return "("+cx+";"+cy+")";
    }

    public void dessiner() {}
}
