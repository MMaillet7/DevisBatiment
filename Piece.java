public class Piece {
    
    private int idPiece;
    private Coin[] coins;
    private Mur[] murs;

    public Piece(int id, Coin[] coins, Mur[] murs) {
        idPiece = id;
        this.coins = coins;
        this.murs = murs;
    }

    public void setIdPiece(int idPiece) {
        this.idPiece = idPiece;
    }

    public int getIdPiece() {
        return idPiece;
    }

    public void setCoins(Coin[] coins) {
        this.coins = coins;
    }

    public Coin[] getCoins() {
        return coins;
    }

    public void setMurs(Mur[] murs) {
        this.murs = murs;
    }

    public Mur[] getMurs() {
        return murs;
    }

    public double devisPiece() {
        return 1;
    }

    public double surface() {
        return 1;
    }

    public void dessiner() {
    }

}
