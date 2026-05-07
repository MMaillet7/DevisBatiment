public class Appartement {
    
    private int idAppart;
    private Piece[] pieces;

    public Appartement(int id, Piece[] p) {
        idAppart = id;
        pieces = p;
    }

    public void setIdAppart(int idAppart) {
        this.idAppart = idAppart;
    }

    public int getIdAppart() {
        return idAppart;
    }

    public void setPieces(Piece[] pieces) {
        this.pieces = pieces;
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public double devisAppartement() {
        double somme = 0;
        for (Piece piece : pieces) {
            somme += piece.devisPiece();
        }
        return somme;
    }

    public double surface() {
        double surf = 0;
        for (Piece piece : pieces) {
            surf += piece.surface();
        }
        return surf;
    }

    public void dessiner() {
    }

}
