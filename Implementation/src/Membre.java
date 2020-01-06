import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * @author Wael ABOU ALI.
 */
public class Membre extends Utilisateur {

    /*  Attributs.  */
    private int montantdu;
    private int totalDepensesHebdo;
    private static int fraisMensuelles = 30;

    /*  Constructeur.   */
    /**
     *  C'est la classe pour les membres du #GYM. Elle herite la plupart de ces
     *  attributs et methodes d'Utilisateur. Seule les attributs relies au
     *  financement comme les frais d'inscriptions representent la difference
     *  entre un Utilisateur non Membre et un Membre.
     * @param nom Le nom du membre.
     * @param adresse Son adresse.
     * @param ville La ville de l'adresse.
     * @param province La province de l'adresse.
     * @param codePostal Le code postal de l'adresse.
     * @param telephone Son numero de telephone.
     * @param courriel Son courriel.
     * @param employe faux puisque c'est un membre.
     * @param prof faux aussi.
     * @throws IOException Les exceptions pour generer le code QR.
     * @throws WriterException
     * @throws NotFoundException
     * Ajouter l'objet cree Membre a la liste de membres dans la base de
     * donnees.
     */
    public Membre(String nom, String adresse, String ville, String province,
                  String codePostal, String telephone, String courriel,
                  boolean employe, boolean prof)
            throws IOException, WriterException, NotFoundException {
        super(nom, adresse, ville, province, codePostal, telephone,
                courriel, employe, prof);
        this.montantdu = 0;
        this.totalDepensesHebdo = 0;
        BaseDonnees.getMembres().add(this);
    }

    /*  Getters.    */
    /**
     * @return Retourne le montant dû sur le membre dans son compte.
     */
    public int getMontantdu() {
        return montantdu;
    }
    /**
     * @return  Retourne les montant des frais mensuelles que le membre
     *          doit payer au debut de chaque mois.
     */
    public static int getFraisMensuelles() {
        return fraisMensuelles;
    }
    /**
     * @return  Retourne le montant que le membre a depense pendant cette
     *          semaine au #GYM.
     */
    public int getTotalDepensesHebdo() { return totalDepensesHebdo; }

    /*  Setters.    */
    /**
     * @param montantdu Assigner un nouveau montant dû au lieu de l'ancien.
     */
    public void setMontantdu(int montantdu) {
        this.montantdu += montantdu;
    }
    /**
     * @param nouvellesFrais Assigner des nouvelles frais mensuelles s'il y en
     *                       une reduction des frais au futur au nom de ce membre
     *                       (reduction de prix ou loyalte).
     */
    public static void setFraisMensuelles(int nouvellesFrais) {
        fraisMensuelles = nouvellesFrais;
    }
    /**
     * @param totalDepensesHebdo Changer le montant que le membre a payer
     *                           pendant une semaine s'il y en a un ajustement
     *                           de prix pour un service ou une inscription
     *                           dont le membre a payer ses frais.
     */
    public void setTotalDepensesHebdo(int totalDepensesHebdo) {
        this.totalDepensesHebdo = totalDepensesHebdo;
    }

    /*  Adders.    */
    /**
     * @param montant   Le montant a ajouter si le membre a fait une nouvelle
     *                  inscription au cours de la semaine courante.
     */
    public void addTotalDepensesHebdo(int montant) {
        this.totalDepensesHebdo += montant;
    }

    /**
     *  Ajouter le rapport de la semaine actuelle a la liste des rapports
     *  hebdomadaire a sa position actuelle(selon le numero de la semaine
     *  actuelle au cours de l'annee. Ex : si la semaine actuelle est la 49
     *  pendant cette annee donc a l'aide du concept de pigeonnier, il va
     *  ajouter automatiquement le rapport de cette semaine a cette semaine
     *  dans le dictionnaire des rapports de ce membre).
     */
    @Override
    public boolean addRapportHebdo(int numWeek) {
        String msg = this.buildRapport(numWeek);
        for(Map.Entry<Integer, String> entry:
                this.getRapportsHebdo().entrySet()) {
            if(entry.getKey() == numWeek) {
                entry.setValue(msg);
                return true;
            }
        }
        return false;
    }

    /**
     *  a l'aide du cle de la semaine de recherche, la fonction redefinie
     *  get va extraire le rapport de cette semaine et retourner un string
     *  contenant toutes les informations a afficher pour ce membre pendant
     *  la semaine(numweek).
     */
    @Override
    public String getRapportHebdo(int numWeek) {
        StringBuilder rapport = new StringBuilder(buildRapport(numWeek));
        ArrayList<Seance> seances = this.getSeancesSemaine(numWeek);
        int i = 1;
        this.setTotalDepensesHebdo(0);
        if(seances.size() > 0) {
            for(Seance seance: seances) {
                String index = "\n" + i + ".";
                String nomPro = Objects.requireNonNull(BaseDonnees
                        .getPro(seance.getNumPro())).getNom();
                rapport.append(index).append("\t- Date du service : ")
                        .append(seance.getDateSeance())
                        .append("\n\t- Nom du professionnel : ")
                        .append(nomPro).append("\n\t- Nom du service : ")
                        .append(seance.getNom())
                        .append("\n----------------------------------------");
                this.addTotalDepensesHebdo(seance.getMontant());
                i += 1;
            }
        } else {
            rapport.append("\n\t- Le membre n'a pas de seances a suivre pour cette semaine!");
        }
        rapport.append("\nLe montant total que le ").append(this.getEtat())
                .append(" : '").append(this.getNom()).append("' a depense est ")
                .append(getTotalDepensesHebdo()).append("$");
        return rapport.toString();
    }

}