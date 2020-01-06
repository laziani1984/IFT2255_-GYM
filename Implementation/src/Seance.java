
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.IOException;
import java.util.*;

/**
 * @author Wael ABOU ALI.
 */
public class Seance extends Service {

    /*  Attributs.  */

    private Map<Inscription, Boolean> inscriptions;
    private ArrayList<Integer> numMembres;
    private int seanceID, semaineSeance, totalMontantSeance;
    private String jourSeance, etatSeance;
    private String dateSeance;

    /*  Constructeur.   */
    /**
     * Seance est un service offert par les professionnels a #GYM et
     * donc il possede le même constructeur Service(super).
     * @param nom   Nom de service.
     * @param date1 Date de début de la séance.
     * @param date2 Date fin.
     * @param heures Heures de la séance.
     * @param numPro Numéro de professionnel assigné a la séance.
     * @param recurrence Les recurrences hebdomadaires.
     * @param capacite La capacité maximale des membres a inscrire.
     * @param montant Le montant a payer pour s'inscrire a la séance.
     * @param commentaires Des commentaires s'il y en a.
     * @param preCreated Booléen qui indique que la séance sera créée
     *                   a travers la simulation(pendant le déroulement
     *                   de programme ou créée a l'aide de la méthode
     *                   creerBD()).
     */
    public Seance(String nom, String date1, String date2, String heures,
                  int numPro, String recurrence, int capacite, int montant,
                  String commentaires, int seanceID, int semaineSeance,
                  String jourSeance, String dateSeance, boolean preCreated) {
        super(nom, date1, date2, heures, numPro, recurrence, capacite,
                montant, commentaires, false, preCreated);
        this.inscriptions = new HashMap<>();
        this.seanceID = seanceID;
        this.semaineSeance = semaineSeance;
        this.jourSeance = jourSeance;
        numMembres = new ArrayList<>();
        this.dateSeance = dateSeance;
        totalMontantSeance = 0;
        etatSeance = "Active";
        Professionnel pro = BaseDonnees.getPro(numPro);
        assert pro != null;
        pro.addSeancesParSemaine(this);
        BaseDonnees.addSeance(semaineSeance,this);

    }

    /*  -------------------------------------------------------------------  */

    /*  --------------------------------    */
    /*  |   Getters, setters & adders. |    */
    /*  --------------------------------    */

    /*  Getters.    */
    /**
     * @return  Retourne le numero de la seance qui sera dans ce cas le code
     *          de la premiere seance de l'ensemble des seances creees.
     */
    public int getSeanceID() { return seanceID; }
    /**
     * @return  Retourne le dictionnaire contenant les inscriptions de la
     *          seance.
     */
    public Map<Inscription, Boolean> getInscriptions() {
        return this.inscriptions;
    }
    /**
     * @return  Retourne un int qui reprsente le code de la semaine de la
     *          seance.
     */
    public int getSemaineSeance() { return semaineSeance; }
    /**
     * @return  Retourne le nom de la journee de la seance.
     */
    public String getJourSeance() { return jourSeance; }
    /**
     * @return  Retourne une liste des nombres des membres inscrit dans la
     *          seance.
     */
    public ArrayList<Integer> getNumMembres() { return numMembres; }
    /**
     * @return  Retourne le montant total que les membres ont paye pour
     *          s'inscrire a la seance
     */
    public int getTotalMontantSeance() { return this.totalMontantSeance; }
    /**
     * @return  Retourne l'etat de la seance soit "actif" ou "termine".
     */
    public String getEtatSeance() { return etatSeance; }
    /**
     * @return  Retourne la date de la seance(JJ-MM-AAAA).
     */
    public String getDateSeance() { return dateSeance; }

    /*  Setters.    */

    /**
     * @param inscriptions  Assigner un nouveau dictionnaire des inscriptions
     *                      au lieu de l'ancien.
     */
    public void setInscriptions(Map<Inscription, Boolean> inscriptions) {
        this.inscriptions = inscriptions;
    }
    /**
     * @param seanceID  Changer le code de la seance pour un nouveau.
     */
    public void setSeanceID(int seanceID) { this.seanceID = seanceID; }
    /**
     * @param semaineSeance Changer la semaine de la seance.
     */
    public void setSemaineSeance(int semaineSeance) {
        this.semaineSeance = semaineSeance;
    }
    /**
     * @param etatSeance    Changer l'etat de la seance soit en "actif" ou
     *                      "termine".
     */
    public void setEtatSeance(String etatSeance) { this.etatSeance = etatSeance; }
    /**
     * @param dateSeance    Changer la date de la seance s'il y avait une
     *                      erreur en entrant la date.
     */
    public void setDateSeance(String dateSeance) {
        this.dateSeance = dateSeance;
    }
    /**
     * @param totalMontantSeance    Changer la valeur totale accumulee.
     */
    public void setTotalMontantSeance(int totalMontantSeance) {
        this.totalMontantSeance = totalMontantSeance;
    }

    /*  Adders. */
    /**
     * @param montant   Ajouter montant au totalMontantSeance.
     */
    public void addMontantSeance(int montant) {
        this.totalMontantSeance += montant;
    }
    /**
     * @param inscription   Ajouter la nouvelle inscription a la liste des
     *                      inscriptions de la seance.
     */
    public void addInscription(Inscription inscription) {
        this.inscriptions.put(inscription, false);
    }

    /*  -------------------------------------------------------------------  */

    /*
        *   Option 3 :
        --------------
        3.  S'inscrire   une seance.
    */
    /**
     * @param input Scanner.
     * @param utilisateur   L'utilisateur qui veut s'inscrire a la seance.
     * @param estMob  La valeur de retour si c'est sur l'application
     *                mobile ou pas.
     * @return  La valeur de retour sera vrai si c'est une application mobile
     *          et faux sinon.
     * @throws NotFoundException
     * @throws IOException
     * @throws WriterException
     */
    static boolean inscrireSeance(Scanner input, Utilisateur utilisateur,
                                  boolean estMob)
            throws NotFoundException, IOException, WriterException {

        String today = BaseDonnees.getDate("dd-MM-YYYY");
        System.out.println("<-- S'inscrire a  une seance -->\nVoici une liste" +
                " des seances de service que #GYM offre pour aujourd'hui le" +
                " : " + BaseDonnees.getTodayStr() + "(" + today + ")");
        Seance seance = MenuGYM.choisirSeance(input);

        if (seance != null) {
            int numUser;
            if (utilisateur == null) {
                numUser = Utilisateur.validerUtilisateur(input, false);
                utilisateur = BaseDonnees.getUser(numUser);
            } else {
                numUser = utilisateur.getNumero();
            }
            if (utilisateur != null) {
                if (!utilisateur.estSuspendu() || !utilisateur.getEtat()
                        .matches("termine")) {
                    if (!utilisateur.getSeancesSemaine(BaseDonnees.getCurrentWeek())
                            .contains(seance)) {
                        if (numUser != seance.getNumPro()) {
                            boolean confirmer = MenuGYM.verifierChoix(input,
                                    "Voulez-vous confirmer votre inscription? (oui/non)");
                            if (confirmer) {
                                boolean payerMontant = MenuGYM.verifierChoix(input, "Le " +
                                        "montant   payer sera : " + seance.getMontant() +
                                        "$\nVoulez-vous continuer avec votre inscription?" +
                                        " (oui/non)");
                                if (payerMontant) {
                                    boolean payerBool = Transaction.payerFrais(input,
                                            seance.getMontant());
                                    if (payerBool) {
                                        Transaction transaction = new Transaction
                                                (seance.getMontant(), "cash");
                                        BaseDonnees.addInscription(utilisateur, seance,
                                                transaction);
                                        System.out.println(MenuGYM.
                                                imprimerConfirmation(seance, utilisateur.getNumero()));
                                        return true;
                                    } else {
                                        System.out.println("La transaction est annulee!" +
                                                "Votre inscription n'est pas enregistree!");
                                    }
                                } else {
                                    System.out.println("La transaction est annulee!" +
                                            "Votre inscription n'est pas enregistree!");
                                }
                            } else {
                                System.out.println("Vous avez choisi de ne pas continuer " +
                                        "le processus d'inscription!");
                            }
                        } else {
                            System.out.println("Impossible d'inscrire le charge de " +
                                    "la seance au seance!");
                        }
                    } else {
                        System.out.println("Le membre est deja inscrit dans la seance!");
                    }
                } else {
                    System.out.println("Le (" + utilisateur.getEtat() + ") : '" +
                            utilisateur.getNom() + "' ne pourra pas confirmer sa " +
                            "presence ou inscrire a la seance sans changer son etat!");
                }
            } else {
                System.out.println("Desole!Donnees invalides!");
                return false;
            }
        }  else { System.out.println("Seance introuvable!"); }
        return estMob;

    }

    //  -------------------------------------------------------------------- //

    /*
        Option 4 :
        ----------
        Confirmer presence   une seance.
     */
    /**
     * @param input Scanner
     * @param utilisateur   Ca pourrait etre :
     *                      1.  Application mobile :
     *                          alors l'utilisateur pourrait etre soit :
     *                          1.  le professionnel qui cherche a confirmer
     *                              la presence d'un membre a une des ses seances.
     *                              Et dans ce cas le professionnel aura besoin
     *                              d'avoir le numero du membre pour lui confirmer
     *                              sa presence.    (confirmerPresence)
     *                          2.  Le membre qui veut confirmer sa presence a la
     *                              seance et ca affichera un code QR si sa
     *                              presence est confirmee.
     *                              (BaseDonnees.actualiserPresence)
     *                      2.  Menu principale au #GYM :
     *                          l'utilisateur vient pour confirmer sa presence a la
     *                          seance.
     * @throws IOException
     * @throws NotFoundException
     * @throws WriterException
     */
    public static void confirmerPresence(Scanner input,
                                         Utilisateur utilisateur)
            throws IOException, NotFoundException, WriterException {
        Seance seance = null;
        boolean fini = false;
        boolean inscrit;
        if(utilisateur != null) {
            while (!fini) {
                System.out.println("\n<-- Confirmer presence -->\n");
                if(utilisateur.getEtat().matches("professionnel")) {
                    seance = MenuGYM.confirmerPresenceMobPro(input, utilisateur);
                } else {
                    if(utilisateur.getEtat().matches("membre")) {
                        seance = MenuGYM.choisirSeance(input);
                    } else {
                        System.out.println("Utilisateur invalide!");
                        fini = true;
                    }
                }
                if (seance != null) {
                    if (utilisateur.getEtat().matches("membre") ||
                            utilisateur.getEtat().matches("professionnel")) {
                        if (utilisateur.getEtat().matches("professionnel")) {
                            confirmerSeance(input, seance);
                            fini = !MenuGYM.verifierChoix(input, "Voulez-vous essayer" +
                                    " avec un nouveau membre? (oui/non)");
                        } else {
                            inscrit = BaseDonnees.actualiserPresence(seance,
                                    utilisateur.getNumero(), true);
                            if(!inscrit) { fini = nonInscrit(input, utilisateur); }
                            else { fini = true; }
                        }
                    }
                } else {
                    if (utilisateur.getEtat().matches("professionnel")) {
                        System.out.println("Pas de seances au #GYM pour : '"
                                + utilisateur.getNom() + "' pour cette semaine!");
                    } else { nonInscrit(input, utilisateur); }
                }
            }
        } else {
            seance = MenuGYM.choisirSeance(input);
            if (seance != null) { confirmerSeance(input, seance); }
            else {
                System.out.println("Pas de seances au #GYM pour aujourd'hui!");
            }
        }
    }

    /**
     * @param input Scanner.
     * @param utilisateur L'utilisateur qui cherche a confirmer sa presence a
     *                    la seance.
     * @return  Retourne un booleen :
     *          1.  vrai si l'utilisateur veut quitter sans inscrire a la
     *              seance.
     *          2.  S'il veut s'inscrire a la seance, la valeur de retour
     *              de son inscription sera retournee en negation.
     *              (s'il est inscrit la valeur de retour sera faux pour
     *              continuer a la confirmation de la presence de la seance).
     * @throws IOException
     * @throws NotFoundException
     * @throws WriterException
     * Les exceptions sont pour la generation des codes QR.
     */
    private static boolean nonInscrit(Scanner input,
                                            Utilisateur utilisateur)
            throws IOException, NotFoundException, WriterException {
        boolean fini;
        fini = !MenuGYM.verifierChoix(input,
                "Voulez-vous vous inscrire a la " +
                        "seance? (oui/non)");
        if (!fini) {
            return !inscrireSeance(input, utilisateur, false);
        } else {
            System.out.println("Vous avez choisi de ne pas " +
                    "continuer le processus d'inscription!");
        }
        return true;
    }

    /**
     * @param input  Scanner.
     * @param seance C'est la seance que l'utilisateur cherche a confirmer
     *               sa presence a laquelle.
     * @return  Retourne un booleen qui indique si la presence est confirmee
     *          ou pas.
     * @throws NotFoundException
     * @throws IOException
     * @throws WriterException
     */
    public static boolean confirmerSeance(Scanner input, Seance seance)
            throws NotFoundException, IOException, WriterException {
        int numMembre = BaseDonnees.getNumMembre(input, seance);
        Utilisateur user = BaseDonnees.getUser(numMembre);
        if(user != null) {
            if (!user.estSuspendu() || !user.getEtat().matches("termine")) {
                return BaseDonnees.actualiserPresence(seance, numMembre, false);
            }
            System.out.println("Le (" + user.getEtat() + ") : '" + user.getNom()
                    + "' ne pourra pas confirmer sa presence ou inscrire a la " +
                    "seance sans changer son etat!");
        }
        System.out.println("Desole!Donnees invalides!");
        return false;
    }

    //  -------------------------------------------------------------------- //

    @Override
    /**
     *  C'est une redefinition de la methode toString pour imprimer a chaque
     *  fois que le system appele la fonction system.out.println la forme qui
     *  se trouve en dessous.
     */
    public String toString() {
        return "Service(" + this.getNom() + ") :-" +
                "\n* dateDebut : " + this.getDateDebut() +
                "\n* dateFin : " + this.getDateFin() +
                "\n* heures : " + this.getHeures() +
                "\n* recurrence : " + this.getRecurrence() +
                "\n* numero Professionnel : " + this.getNumPro() +
                "\n* codeSeance : " + this.getSeanceID() +
                "\n* capacite : " + this.getCapacite() +
                "\n* montant : " + this.getMontant() +
                "\n* commentaires : " + this.getCommentaires() +
                "\n* inscriptions : " + MenuGYM.imprimerInscriptionSeance(this)
                + "}\n";
    }
}