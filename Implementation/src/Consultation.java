
/**
 * @author Wael ABOU ALI.
 */
public class Consultation extends Service {

    /*  Constructeur.   */
    /**
     * Consultation est un service offert par les professionnels a #GYM et
     * donc il possede le meme constructeur Service.
     * @param nom   Nom de service.
     * @param date1 Date de debut de la seance.
     * @param date2 Date fin.
     * @param heures Heures de la seance.
     * @param numPro Numero de professionnel assigne à la seance.
     * @param recurrence Les recurrences hebdomadaires.
     * @param capacite La capacite maximale des membres à inscrire.
     * @param montant Le montant à payer pour s'inscrire à la seance.
     * @param commentaires Des commentaires s'il y en a.
     * @param preCreated Booleen qui indique que la seance sera creee
     *                   à travers la simulation(pendant le deroulement
     *                   de programme ou creee à l'aide de la methode
     *                   creerBD()).
     */
    Consultation(String nom, String date1, String date2, String heures,
                 int numPro, String recurrence, int capacite, int montant,
                 String commentaires, boolean preCreated) {
        super(nom, date1, date2, heures, numPro, recurrence, capacite,
                montant, commentaires, false, preCreated);
    }
}
