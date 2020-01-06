
/**
 * @author Wael ABOU ALI.
 */
public class Inscription {

    /*  Attributs.  */
    private Utilisateur membre;
    private Seance seance;
    private Transaction transaction;
    private String dateInscription;

    /*  Constructeur.   */
    /**
     * @param utilisateur   C'est un Utilisateur pas Membre puisque tous les
     *                      utilisateurs du #GYM peuvent s'inscrire aux seances.
     * @param seance        C'est la seance destinee que les utilisateurs
     *                      vont s'inscrire a laquelle.
     * @param transaction   La transaction faite afin de s'inscrire dans la
     *                      seance en indiquant le montant paye par
     *                      l'utilisateur et la methode de paiment.
     *  Aussi a chaque inscription le systeme va ajouter automatiquement
     *  1.  Le montant total que les utilisateurs ont paye pour s'inscrire
     *      a la seance.
     *  2.  Ajouter l'utilisateur a la liste des membres dans la seance.
     *  3.  Ajouter la seance a la liste des seances que l'utilisateur a pris.
     *  4.  Ajouter la transaction a la liste des transactions que #GYM a fait
     *      pour la journee actuelle. a la fin de chaque journee, cette liste
     *      sera envoyee a RnB(a 9:00 PM).
     */
    public Inscription(Utilisateur utilisateur, Seance seance, Transaction transaction) {

        int numPro = seance.getNumPro();
        Professionnel pro = BaseDonnees.getPro(numPro);
        assert pro != null;
        this.seance = seance;
        this.dateInscription = transaction.getDateTransaction();
        this.membre = utilisateur;
        this.transaction = transaction;
        seance.addMontantSeance(transaction.getMontant());
        seance.getNumMembres().add(utilisateur.getNumero());
        utilisateur.addSeancesPris(this.seance);
        seance.addMontantSeance(transaction.getMontant());
    }

    /*  Getters.    */
    /**
     * @return  Cette methode retourne un le membre qui est inscrit dans cette
     *          inscription.
     */
    public Utilisateur getMembreInscription() { return membre; }
    /**
     * @return  Retourne la seance de l'inscription.
     */
    public Seance getSeanceInscription() { return seance; }
    /**
     * @return  Retourne la date de l'inscription.
     */
    public String getDateInscription() { return dateInscription; }
    /**
     * @return  Retourne la transaction faite pendant l'inscription. Cette
     *          transaction contiendra le montant que le membre a paye
     *          pour s'inscrire et la methode de paiment.
     */
    public Transaction getTransaction() { return transaction; }

    /*  Setters.    */
    /**
     * @param membre Changer le membre de l'inscription s'il y en a une
     *               faute quelques part lors de processus de l'inscription.
     */
    public void setMembre(Utilisateur membre) { this.membre = membre; }
    /**
     * @param seance Changer la seance d'inscription pour une nouvelle.
     */
    public void setSeance(Seance seance) { this.seance = seance; }

    /**
     * Une redefinition de la methode toString pour imprimer la confirmation
     * directement a chaque fois qu'on appele la fonction system.out.println.
     */
    @Override
    public String toString() {
        return "\t- Date du service : " + seance.getDateSeance() +
                "\n\t- Date et heure a laquelle les donnees etaient recues" +
                " par l'ordinateur : " + seance.getDateCreation() + "\n\t- " +
                "Nom du membre : " + membre.getNom() + "\n\t- Numero du " +
                "membre : " + BaseDonnees.addZerosToNum(membre.getNumero()) +
                "\n\t- Code de la seance : " + seance.getSeanceID() +
                "\n\t- Transaction(Montant) : " + transaction.getMontant() +
                "$\n\t- Methode de paiment : " + transaction.getMethode()
                + "\n----------------------------------------";
    }
}
