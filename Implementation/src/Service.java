import java.util.*;

/**
 * @author Wael ABOU ALI.
 */
public class Service {

    /*  Attributs.  */
    private static Map<String, Integer> services = fillHashMap();
    private int numPro, capacite, montant, numRecurrence, codeService,
            codeSeance;
    private String nom, dateDebut, dateFin, heures, recurrence, commentaires,
            dateCreation;

    /*  Construteur.    */
    /**
     * Seance est un service offert par les professionnels a #GYM et
     * donc il possede le même constructeur Service(super).
     * @param nom   Nom de service.
     * @param dateDebut Date de debut de la seance.
     * @param dateFin Date fin.
     * @param heures Heures de la seance.
     * @param numPro Numero de professionnel assigne a la seance.
     * @param recurrence Les recurrences hebdomadaires.
     * @param capacite La capacite maximale des membres a inscrire.
     * @param montant Le montant a payer pour s'inscrire a la seance.
     * @param commentaires Des commentaires s'il y en a.
     * @param first C'est un booleen qui sert a appeler la fonction
     *              creerSeance pour la premiere fois pour eviter la
     *              boucle infinie creee par l'appel du constructeur
     *              super a travers seance qui va boucler a l'infini. (patron)
     * @param preCreated Booleen qui indique que la seance sera creee
     *                   a travers la simulation(pendant le deroulement
     *                   de programme ou creee a l'aide de la methode
     *                   creerBD()).
     */
    Service(String nom, String dateDebut, String dateFin,
            String heures, int numPro, String recurrence, int capacite,
            int montant, String commentaires, boolean first,
            boolean preCreated) {
        if((BaseDonnees.verifierDonneesService(nom, dateDebut, dateFin, heures,
                numPro, recurrence, capacite, montant, commentaires, preCreated))) {
            this.nom = nom;
            if (services.containsKey(nom)) {
                this.codeService = services.get(nom);
            } else {
                System.out.println("Le code de service est inconnu!");
            }
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
            this.heures = heures;
            this.recurrence = recurrence;
            this.numRecurrence = recurrence.split("\\s*,\\s*").length;
            this.capacite = capacite;
            this.numPro = numPro;
            this.montant = montant;
            this.commentaires = commentaires;
            this.dateCreation = BaseDonnees.getDate("dd-MM-YYYY");
            if (first) {
                codeSeance = this.creerSeance(preCreated, true);
            }
        }
    }

    /*  Getters.    */
    /**
     * @return  Retourne le nom d'un service.
     */
    public String getNom() { return nom; }
    /**
     * @return  Retourne la capacite d'un service.
     */
    public int getCapacite() { return capacite; }
    /**
     * @return  Retourne la date de debut d'un service.
     */
    public String getDateDebut() { return dateDebut; }
    /**
     * @return  Retourne la date de fin d'un service.
     */
    public String getDateFin() { return dateFin; }
    /**
     * @return  Retourne les heures d'un service.
     */
    public String getHeures() { return heures; }
    /**
     * @return  Retourne les recurrences hebdomadaire en String.
     */
    public String getRecurrence() { return recurrence; }
    /**
     * @return  Retourne le nombre des recurrence hebdomadaire d'un service.
     */
    public int getNumRecurrence() { return numRecurrence; }
    /**
     * @param numPro    Numero de professionnel qu'on cherche.
     * @return  Retourne le professionnel si c'est le professionnel charge a
     *          ce service et nulle si le professionnel n'est pas present dans
     *          la base de donnees du #GYM.
     */
    public Professionnel getPro(int numPro) {
        for(Professionnel professionnel: BaseDonnees.getProfessionnels()) {
            if(professionnel.getNumero() == numPro) {
                return professionnel;
            }
        }
        return null;
    }
    /**
     * @return  Retourne le numero de professionnel charge a ce service.
     */
    public int getNumPro() { return numPro; }
    /**
     * @return  Retourne le montant assigne a ce service.
     */
    public int getMontant() { return montant; }
    /**
     * @return  Retourne le code de la seance.
     */
    public int getCodeSeance() { return codeSeance; }
    /**
     * @return  Retourne le code de service.
     */
    public int getCodeService() { return codeService; }
    /**
     * @return  Retourne les commentaires lies a ce service.
     */
    public String getCommentaires() { return commentaires; }
    /**
     * @return  Retourne le dictionnaire des services offerts par #GYM.
     */
    public static Map<String, Integer> getServices() { return services; }
    /**
     * @return  Retourne le temps dans lequel le service etait cree.
     */
    public String getDateCreation() { return dateCreation; }


    /*  Setters.    */
    /**
     * @param nom   Changer le nom de service.
     */
    public void setNom(String nom) { this.nom = nom; }
    /**
     * @param capacite  Changer la capacite
     */
    public void setCapacite(int capacite) { this.capacite = capacite; }
    /**
     * @param dateDebut Changer la date de debut.
     */
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
    /**
     * @param dateFin   Changer la date de fin.
     */
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }
    /**
     * @param heures    Changer les heures.
     */
    public void setHeures(String heures) { this.heures = heures; }
    /**
     * @param recurrence    Changer les recurrences d'un service.
     */
    public void setRecurrence(String recurrence) { this.recurrence = recurrence; }
    /**
     * @param numPro    Changer le numero de professionnel charge au service.
     */
    public void setNumPro(int numPro) {
        if (this.numPro != numPro) {
            this.numPro = numPro;
        } else {
            System.out.println("Le numero de professionnel est similaire au " +
                    "numero de professionnel initial!");
        }
    }
    /**
     * @param commentaires  Changer les commentaires dans un service s'il y
     *                      en a.
     */
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    /**
     * @param montant   Changer le montant d'un service avec un nouveau si
     *                  l'ancien n'est pas similaire au nouveau.
     */
    public void setMontant(int montant) {
        if (this.montant != montant) {
            this.montant = montant;
        } else {
            System.out.println("Le nouveau montant est similaire au " +
                    "montant initial!");
        }
    }
    /**
     * @param services  Changer les services offerts au #GYM au complet.
     */
    public void setServices(Map<String, Integer> services) {
        this.services = services;
    }
    /**
     * @param numRecurrence Changer le nombre de recurrence par semaine par un
     *                      nouveau.
     */
    public void setNumRecurrence(int numRecurrence) {
        this.numRecurrence = numRecurrence;
    }
    /**
     * @param codeService   Changer le codeService par un nouveau s'il y avait
     *                      une faute en creant le premier.
     */
    public void setCodeService(int codeService) {
        this.codeService = codeService;
    }


    /**
     * @return  Un dictionnaire qui contient les noms et les codes des services
     *          offerts par #GYM.
     */
    private static Map<String, Integer> fillHashMap() {

        Map<String, Integer> servs = new HashMap<>();
        servs.put("Nutrition", 598);
        servs.put("Zumba", 883);
        servs.put("Piscine", 101);
        servs.put("Soccer", 202);
        servs.put("Basket", 303);
        servs.put("Yoga", 404);
        return servs;
    }

    /*  -------------------------------------------------------------------  */

    /*
        *   Option 2 :
        --------------
        2.  Gerer un service(Ajouter, mise a  jour, supprimer).
     */

    /**
     * @param input Scanner
     * @param numPro   Le numero de professionnel qui cherche a creer
     *                 la(les) nouvelles seances.
     * @return  Retourne vrai si les seances sont creees et faux sinon.
     */
    static boolean creerSeancesMenu(Scanner input, int numPro) {

        System.out.println("<-- Creer une seance -->\nVeuillez remplir les" +
                " champs suivants : ");
        return verifierDataSeance(input, numPro);
    }

    /**
     * Cette methode est responsable de faire la collection des donnees
     * des seances a creer et les verifier afin de creer ses seances.
     * @param input Scanner.
     * @param numPro    Le numero de professionnel qui cherche a creer
     *                  la(les) nouvelles seances.
     * @return  Retourne vrai si les seances de service sont creees et
     *          faux sinon.
     */
    public static boolean verifierDataSeance(Scanner input, int numPro) {

        String dateDebut = BaseDonnees.verifierData(input,
                "Date de debut(JJ-MM-YYYY) :", "", 8,null, "", false);
        String dateFin = BaseDonnees.verifierData(input,
                "Date de fin(JJ-MM-YYYY) :", "", 8, dateDebut, "", false);
        String heure = BaseDonnees.verifierData(input,
                "Heure(HH:MM-HH:MM) :", "", 9, null, "", false);
        String recurrence = BaseDonnees.verifierData(input,
                "Recurrence((J),..)", "", 10, null, "", false);
        String capaciteStr = BaseDonnees.verifierData(input,
                "Capacite maximale :", "", 11, null, "", false);
        String numProStr = BaseDonnees.addZerosToNum(numPro).toString();
        String fraisService = BaseDonnees.verifierData(input,
                "Frais de service :", "", 14, null, "", false);
        String nomService = BaseDonnees.verifierData(input,
                "nom de service(20 caracteres) :", "", 18,null, "", false);
        String commentaires = BaseDonnees.verifierData(input,
                "Commentaires :", "", 19, null, "", false);
        int montant = Integer.parseInt(fraisService);
        int capacite = Integer.parseInt(capaciteStr);
        Utilisateur user = BaseDonnees.getUser(numPro);
        assert user != null;
        if(user.getEtat().matches("professionnel")) {
            Service service = creerSeances(nomService, dateDebut, dateFin, heure,
                    numPro, recurrence, capacite, montant, commentaires,
                    false);
            return service != null;
        } else {
            System.out.println("Impossible de creer la seance!Code du professinonel invalide!");
            return false;
        }
    }

    /**
     * @param nomService
     * @param dateDebut
     * @param dateFin
     * @param heure
     * @param numPro
     * @param recurrence
     * @param capacite
     * @param montant
     * @param commentaires
     * @param preCreated
     * @return
     */
    static Service creerSeances(String nomService, String dateDebut,
                                String dateFin, String heure,
                                int numPro, String recurrence, int capacite,
                                int montant, String commentaires, boolean preCreated) {


        Service service = new Service(nomService, dateDebut, dateFin, heure,
                numPro, recurrence, capacite, montant, commentaires, true, preCreated);
        if(service.getCodeSeance() != 0) {
            if(!preCreated) {
                String dateStr = BaseDonnees.getDate("dd-MM-YYYY HH:MM:SS");
                System.out.println("Service cree!");
                System.out.println(
                        "Date et heure actuelles(JJ-MM-AAAA HH:MM:SS) : " + dateStr +
                                ".\n- Date de debut du service (JJ-MM-AAAA) : " + dateDebut +
                                ".\n- Date de fin du service (JJ-MM-AAAA) : " + dateFin +
                                ".\n- Heure du service (HH:MM) : " + heure +
                                ".\n- Recurrence hebdomadaire du service (quels jours " +
                                "il est offert a la meme heure) : " + recurrence +
                                ".\n- Capacite maximale (maximum 30 inscriptions) : " + capacite +
                                ".\n- Numero du professionnel (9 chiffres) : "
                                + BaseDonnees.addZerosToNum(numPro) +
                                ".\n- Code du seance (7 chiffres) : " + service.getCodeSeance() +
                                ".\n- Frais du service (jusqu'a  100.00$) : " + montant +
                                "$.\n- Commentaires (100 caracteres) : " + commentaires + ".");
            }
            return service;
        } else {
            System.out.println("Impossible de creer la seance!Les donnees sont invalides!");
            return null;
        }

    }

    /**
     * @param input Scanner
     * @param professionnel Le professionnel dont le systeme va actualiser une
     *                      ou plusieurs seances de son compte.
     * @return  Ca retourne vrai si le professionnel ne veut pas actualiser des
     *          autres seances de son compte et faux sinon.
     */
    public static boolean actualiserSeances(Scanner input, Professionnel professionnel) {
        Seance seance = MenuGYM.afficherSeancesPro(input, professionnel,
                "actualiser", "l'actualisation");
        if(seance != null) {
            String optionChoisi = MenuGYM.afficherOptionsActualisation(input);
            boolean seule = MenuGYM.verifierChoix(input, "voulez-vous " +
                    "actualiser une seule seance? (oui/non)");
            if(seule) {
                BaseDonnees.actualiserAttribut(input, optionChoisi, seance,
                        professionnel, true);
            } else {
                BaseDonnees.actualiserAttribut(input, optionChoisi, seance,
                        professionnel, false);
            }
            return !MenuGYM.verifierChoix(input, "voulez-vous choisir une autre option " +
                    "service? (oui/non) ");
        } else {
            System.out.println("Le professionnel n'a pas des seances disponbiles!");
            return true;
        }
    }

    /**
     * @param input Scanner
     * @param professionnel Le professionnel dont le systeme va enlever la(les)
     *                      seances de son compte.
     * @return  Ca retourne vrai si le professionnel ne veut pas supprimer des
     *          autres seances de son compte et faux sinon.
     */
    public static boolean supprimerSeances(Scanner input, Professionnel professionnel) {

        Seance seance = MenuGYM.afficherSeancesPro(input, professionnel,
                "Supprimer", "la suppression");
        if(seance != null) {
            boolean supprimerService = MenuGYM.verifierChoix(input,
                    "Etes-vous sur de supprimer cette seance? " +
                            "(oui/non) : ");
            if (supprimerService) {
                BaseDonnees.supprimerSeance(seance);
            } else { System.out.println("Vous avez decide de ne pas " +
                    "supprimer la(les) seances!"); }
        }
        return !MenuGYM.verifierChoix(input, "voulez-vous choisir une " +
                "autre option service? (oui/non) ");
    }

    /*  -------------------------------------------------------------------  */

    /**
     * @param preCreated    Un booleen qui indique que la seance sera creee
     *                      a travers un simulateur ou creee a travers la
     *                      methode creerBD().
     * @param first Si c'est la premiere seance a creer alors le codeSeance
     *              sera le code de la premiere seance a initialiser.
     * @return  Retourne le codeSeance.
     */
    public int creerSeance(boolean preCreated, boolean first) {

        int codeSeance = 0;
        int timeWeek = BaseDonnees.calculerJours
                (dateDebut, dateFin) / 7;
        int startingWeek = BaseDonnees.specificWeek(dateDebut);
        int totalseances =  timeWeek * numRecurrence;
        Professionnel pro = getPro(numPro);
        if(pro != null && !pro.getEtat().matches("termine")) {
            for(int i = 0; i < totalseances; i++) {
                int seanceID = generateID(pro, i + 1, nom);
                if(first) {
                    codeSeance = seanceID;
                    first = false;
                }
                int semaine = ((i / numRecurrence) + startingWeek) % 52;
                int jour = (i % numRecurrence);
                String jourStr = recurrence.split("\\s*,\\s*")[jour];
                int calDay = BaseDonnees.getDayInt(jourStr);
                String dateSeance = BaseDonnees.getDateFromWeekNum(semaine, calDay);
                new Seance(nom, dateDebut, dateFin, heures, numPro, recurrence,
                        capacite, montant, commentaires, seanceID, semaine,
                        jourStr, dateSeance, preCreated);
            }
        } else {
            System.out.println("Numero de professionnel invalide!Impossible de creer le service!");
        }
        return codeSeance;
    }

    /**
     * @param pro   C'est le code du professionnel que le systeme va utiliser
     *              pour avoir ses deux derniers chiffres en produisant les
     *              numeros de la seance.
     * @param i     C'est le nombre de la seance(i dans la boucle des seances
     *              a creer a partir du nombre total des seances a creer
     *              entre la date de debut et fin).
     * @param nom   C'est le nom de service que la seance sera liee a laquelle.
     * @return  La valeur de retour sera le numero de la seance (Ex: 4040101)
     *          qui represente 404(code service), 01(i numero de la seance) et
     *          01(deux derniers chiffres du numero membre du professionnel).
     */
    public static Integer generateID(Professionnel pro, int i, String nom) {
        String num, seanceIDStr;
        int codeService;
        if (services.containsKey(nom)) {
            codeService = services.get(nom);
        } else { return 0; }
        String numProStr = BaseDonnees.addZerosToNum(pro.getNumero()).toString();
        String[] proStr = numProStr.split("(?<=.)");
        if(i < 10) { num = "0" + i; }
        else { num = "" + i; }
        seanceIDStr = codeService + num + proStr[7] + proStr[8];
        return Integer.parseInt(seanceIDStr);
    }

}
