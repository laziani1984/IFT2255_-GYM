
/**
 * @author Wael ABOU ALI.
 */
public class Tourniquet {

    /*  Attributs.  */
    private int tourniquetNum;
    private static int currTourniquetNum = 0;

    /*  Constructeur.   */
    /**
     *  -   Chaque tourniquet aura un numero tourniquet(currTourniquetNum).
     *  -   currTourniquetNum : C'est un nombre statique qui sera incremente et
     *                          assigne a chaque nouveau tourniquet.
     */
    public Tourniquet() {
        currTourniquetNum += 1;
        this.tourniquetNum = currTourniquetNum;
    }

    /*  Getters.    */
    public int getTourniquetNum() { return tourniquetNum; }
    public static int getCurrTourniquetNum() { return currTourniquetNum; }

    /*  Setters.    */
    public void setTourniquetNum(int tourniquetNum) {
        this.tourniquetNum = tourniquetNum;
    }

    /**
     *
     * @param QR    C'est une matrice contenant le code QR de l'utilisateur
     *              qui cherche a acceder au #GYM.
     * @return  Vrai si le code QR est dans la liste des codes QR des
     *          utilisateurs inscrits au #GYM.
     */
    public static boolean inQRCodes(String QR) {
        for(String qr : BaseDonnees.getQRcodes()) {
            if(qr.matches(QR)) { return true; }
        }
        return false;
    }

}
