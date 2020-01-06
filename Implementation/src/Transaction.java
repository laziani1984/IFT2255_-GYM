import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Wael ABOU ALI.
 */
public class Transaction {

    /*  Attributs.  */
    private int montant;
    private static int numTransaction = 0;
    private int transactionID = 0;
    private String methode, dateTransaction;

    /* Constructeur.    */

    /**
     * transactionID c'est le numero specifique de la transaction faite par #GYM.
     * @param montant   Le montant a payer.
     * @param methode   La methode de paie.
     * La transaction sera ajoutee au dictionnaire(journee, transactions
     * faites par #GYM pendant la journee) dans la base de donnees du #GYM.
     */
    public Transaction(int montant, String methode) {
        numTransaction += 1;
        this.setTransactionID(numTransaction);
        this.montant = montant;
        this.methode = methode;
        this.dateTransaction = BaseDonnees.getDate("dd-MM-YYYY");
        BaseDonnees.addTransaction(dateTransaction, this);

    }

    /*  Getters.    */
    /**
     *
     * @return  Retourne le numero de transaction.
     */
    public int getTransactionID() { return this.transactionID; }

    /**
     *
     * @return  Retourne la transaction elle meme selon besoin.
     */
    public Transaction getTransaction() {
        return BaseDonnees.getTransaction(this.getTransactionID());
    }

    /**
     *
     * @return  Retourne le montant que l'utilisateur a payer pendant la
     *          transaction.
     */
    public int getMontant() { return montant; }

    /**
     *
     * @return  Retourne la methode de paiment.
     */
    public String getMethode() { return methode; }

    /**
     *
     * @return  Retourne la date de transaction.
     */
    public String getDateTransaction() { return dateTransaction; }

    /*  Setters.    */

    /**
     *
     */
    public void setTransactionID(int numTransaction) {
        transactionID += numTransaction;
    }

    /**
     *
     * @param montant   Changer le montant a payer d'une transaction.
     */
    public void setMontant(int montant) { this.montant = montant; }

    /**
     *
     * @param methode   Changer la methode de paiment.
     */
    public void setMethode(String methode) { this.methode = methode; }

    /**
     *
     * @param dateTransaction   Changer la date de transaction faite.
     */
    public void setDateTransaction(String dateTransaction) {
        this.dateTransaction = dateTransaction;
    }


    /*  ------------------------------------------------------------------   */

    /*
        Methode pour payer les frais ou les ajoutes sur le compte du membre.
     */

    /**
     *
     * @param input Scanner.
     * @param montant   Montant a payer pour accomplir la transaction.
     * @return  Retourne un booleen qui indique si la reception du paiment
     *          ou non.
     */
    static boolean payerFrais(Scanner input, int montant) {

        System.out.println("<-- Payer les frais dus -->");
        System.out.println("Veuillez choisir un parmi ces options : ");
        String[] methodesPaiment = { "Cash", "Debit", "Credit"};
        String choixPaimentStr = MenuGYM.afficherOptions(input, methodesPaiment, 1);
        while (choixPaimentStr == null) {
            choixPaimentStr = MenuGYM.afficherOptions(input, methodesPaiment, 1);
        }
        return choixPaiment(input, choixPaimentStr, montant);
    }

    /**
     *
     * @param input Scanner.
     * @param choixPaimentStr   Le choix que l'utilisateur a fait pour la
     *                          methode avec laquelle il va payer les
     *                          frais demandees.
     * @param montant   Le montant a payer pour avoir le service desire.
     * @return
     */
    static boolean choixPaiment(Scanner input, String choixPaimentStr, int montant) {

        boolean hasPaid = false;

        switch (choixPaimentStr) {
            case "1":
                boolean payerCash = MenuGYM.verifierChoix(input, "Voulez-vous payer " +
                        "en cash? (oui/non) : ");
                if(payerCash) { hasPaid = methodePaie(input, montant, 1); }
                break;
            case "2":
                boolean payerDebit = MenuGYM.verifierChoix(input, "Voulez-vous payer " +
                        "en debit? (oui/non) : ");
                if(payerDebit) { hasPaid = methodePaie(input, montant, 2); }
                break;
            default:
                boolean payerCredit = MenuGYM.verifierChoix(input, "Voulez-vous payer " +
                        "en credit? (oui/non) : ");
                if(payerCredit) { hasPaid = methodePaie(input, montant, 3); }
                break;
        }

        if(!hasPaid) {
            boolean changerMethode = MenuGYM.verifierChoix(input, "Voulez-vous changer la " +
                    "methode de paiment? (oui/non) ");
            if (changerMethode) { return payerFrais(input, montant); }
            else {
                System.out.println("Le membre a choisi de ne pas payer pour le " +
                        "moment!");
                return false;
            }
        }

        return true;

    }

    /**
     *
     * @param input Scanner
     * @param montant   Montant a payer par utilisateur.
     * @param option    L'option de la methode de paie soit :
     *                  1.  Cash.
     *                  2.  Debit.
     *                  3.  Credit.
     * @return  Retourne vrai si l'utilisateur a paye le montant indique et
     *          faux sinon.
     */
    private static boolean methodePaie(Scanner input, int montant, int option) {

        StringBuilder paiment = new StringBuilder();
        String str = "";
        String carte;
        boolean hasPaid = true;
        switch (option) {
            case 1:
                str = "Le membre a paye un montant de " + montant +
                        "$ en cash.";
                paiment.append("cash");
                break;
            case 2:
                carte = BaseDonnees.verifierData(input, "Veuillez entrer le " +
                        "numero de la carte debit?\n(16 chiffres)(commence " +
                        "avec 45 ou (5(1-4))", "", 15, null, "", false);
                str = "Le membre a paye un montant de " + montant +
                        "$ avec sa carte debit : " + carte;
                paiment.append(montant).append(carte);
                break;
            case 3:
                carte = BaseDonnees.verifierData(input, "Veuillez entrer le " +
                        "numero de la carte credit?\n(16 chiffres)(commence" +
                        " avec 45 ou (5(1-4))", "", 15, null, "", false);
                String expiration = BaseDonnees.verifierData(input, "Veuillez entrer " +
                                "la date d'expiration de la carte credit?\n(date " +
                                "commence en 11/19 jusqu'a 12/99)", "", 16,
                        null, "", false);
                String cvv = BaseDonnees.verifierData(input, "Veuillez entrer le " +
                                "CVV de la carte credit?(3 ou 4 chiffres)", "",
                        17, null, "", false);
                str = "Le membre a paye un montant de : " + montant +
                        "$ avec la carte : " + carte + ", ayant une date " +
                        "d'expiration : " + expiration +
                        " dont le cvv est : " + cvv;
                paiment.append(montant).append(carte).append(expiration)
                        .append(cvv);
                break;
            default:
                hasPaid = false;
                System.out.println("Methode paiment invalide!");
        }
        if(hasPaid) {
            new Transaction(montant, paiment.toString());
            envoyerRnB(str);
            return true;
        }
        return false;
    }

    /**
     *
     * @param str   C'est les details de la methode de paiment(soit cash, debit
     *              avec le numero de la carte ou credit avec numero,
     *              expiration et cvv) qui sera envoye a RnB.
     */
    static void envoyerRnB(String str) {
        System.out.println("paiment envoye au RnB!");
        System.out.println(str);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "montant =" + montant +
                ", transactionID =" + transactionID +
                ", methode ='" + methode + '\'' +
                ", dateTransaction ='" + dateTransaction + '\'' +
                '}';
    }
}
