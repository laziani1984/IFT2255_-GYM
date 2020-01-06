import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.*;

/**
 * @author Wael ABOU ALI.
 */
public class BaseDonnees {

    /*  Attributs.  */
    private static String[] joursSemaine = new String[]{"dimanche", "lundi",
            "mardi", "mercredi", "jeudi", "vendredi", "samedi"};
    private static ArrayList<Utilisateur> utilisateurs;
    private static ArrayList<Employe> employes;
    private static ArrayList<Membre> membres;
    private static ArrayList<Professionnel> professionnels;
    private static Map<String, ArrayList<Transaction>> transactions;
    private static Map<Integer, ArrayList<Seance>> seances;
    private static ArrayList<String> courriels;
    private static ArrayList<String> QRcodes;
    private static boolean baseCree = false;

    /*  Constructeur.   */

    /**
     *  -   Puisque le systeme ne pourra pas creer deux bases de donnees
     *      simultanement alors le systeme va verifier si une base de
     *      etait creee ou pas. Sinon, baseCree sera assignee en true
     *      et la base de donnees est creee.
     *  -   Lors de la creation de la base de donnees le logiciel va creer
     *      des listes pour les utilisateurs, employes et ainsi de suite.
     */
    public BaseDonnees() {

        if(!baseCree) {
            utilisateurs = new ArrayList<>();
            employes = new ArrayList<>();
            membres = new ArrayList<>();
            professionnels = new ArrayList<>();
            transactions = new HashMap<>();
            seances = new HashMap<>();
            courriels = new ArrayList<>();
            QRcodes = new ArrayList<>();
        }
    }

    /**
     * @return
     */
    public static BaseDonnees creerBaseDonnees() {
        if(!baseCree) {
            BaseDonnees baseDonnees = new BaseDonnees();
            baseCree = true;
            return baseDonnees;
        } else { return null; }
    }

    /*  -------------------------------------------------------------------  */

    /*  --------------------------------    */
    /*  |   Getters, setters & adders. |    */
    /*  --------------------------------    */

    /*  Getters.    */
    /**
     * @return  Retourne la liste des utilisateurs inscrits au #GYM.
     */
    public static ArrayList<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }
    /**
     * @return  Retourne la liste des employes inscrits au #GYM.
     */
    public static ArrayList<Employe> getEmployes() { return employes; }
    /**
     * @return  Retourne la liste des membres inscrits au #GYM.
     */
    public static ArrayList<Membre> getMembres() { return membres; }
    /**
     * @return  Retourne la liste des professionnels inscrits au #GYM.
     */
    public static ArrayList<Professionnel> getProfessionnels() {
        return professionnels;
    }
    /**
     * @return  Retourne la liste des transactions faites au #GYM.
     */
    public static Map<String, ArrayList<Transaction>> getTransactions() {
        return transactions;
    }
    /**
     * @param numWeek   Le numero de la semaine que le logiciel va chercher.
     * @return  Une listes des seances de la semaine numWeek si la cle(semaine)
     *          est presente dans le dictionnaire des seances.
     */
    public static ArrayList<Seance> getSemaineSeances(int numWeek) {

        for(Map.Entry<Integer, ArrayList<Seance>> entry:
                seances.entrySet()) {
            if (entry.getKey() == numWeek) {
                return entry.getValue();
            }
        }
        return null;
    }
    /**
     * @return  Retourne la liste des courriels inscrits dans la base de
     *          donnees du #GYM.
     */
    public static ArrayList<String> getCourriels() { return courriels; }
    /**
     * @return  Retournes la liste des codes QR des utilisateurs inscrits
     *          au #GYM.
     */
    public static ArrayList<String> getQRcodes() { return QRcodes; }

    /**
     * @param numUser   C'est le numero d'utilisateur que le logiciel va lui
     *                  ajouter des '0's complementaires afin d'avoir la forme
     *                  suivante(0000xxxxx).
     * @return  Retourne un StringBuilder avec le numero modifie avec les
     *          zeros complementaires.
     */
    public static StringBuilder addZerosToNum(int numUser) {
        StringBuilder addedZerosNum = new StringBuilder("" + numUser);
        while (addedZerosNum.length() < 9) {
            addedZerosNum.insert(0, "0");
        }
        return addedZerosNum;
    }

    /**
     * @return  Retourne un dictionnaire contenant toutes les seances creees
     *          dans #GYM
     */
    public static Map<Integer, ArrayList<Seance>> getSeances() { return seances; }

    /**
     *
     * @param numWeek   La semaine que le logiciel veut obtenir ses seances.
     * @return  Une liste des seances de service offerts par #GYM pendant
     *          cette semaine(numWeek).
     */
    public static ArrayList<Seance> getSeancesSemaine(int numWeek) {
        seances.putIfAbsent(numWeek, new ArrayList<>());
        for(Map.Entry<Integer, ArrayList<Seance>> entry : seances.entrySet()) {
            if (entry.getKey() == numWeek) {
                return entry.getValue();
            }
        }
        return null;
    }

    /*  Getters pour les objets avec ID.    */
    /**
     *
     * @param numSeance  Numero de la seance.
     * @return  Retourne la seance si trouvee et nulle sinon.
     */
    public static Seance getSeance(int numSeance) {
        for(Map.Entry<Integer, ArrayList<Seance>> entry : seances.entrySet()) {
            ArrayList<Seance> arrSeance = entry.getValue();
            for(Seance seance : arrSeance) {
                if(seance.getSeanceID() == numSeance) {
                    return seance;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param transactionID Le numero de la transaction faite.
     * @return  Retourne la transaction si trouvee et nulle sinon.
     */
    public static Transaction getTransaction(int transactionID) {
        for(Map.Entry<String, ArrayList<Transaction>> entry:
                transactions.entrySet()) {
            for(Transaction transaction : entry.getValue()) {
                if(transaction.getTransactionID() == transactionID) {
                    return transaction;
                }
            }
        }
        return null;
    }

    /**
     * @param today Un string avec la date d'aujourd'hui.
     * @return  Retoune la liste des transactions faite au #GYM pendant la
     *          journee courante.
     */
    public static ArrayList<Transaction> envoyerRnB(String today) {
        for (Map.Entry<String, ArrayList<Transaction>> entry:
                transactions.entrySet()) {
            if(entry.getKey().matches(today)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /* Getters pour les utilisateurs avec les numeros.  */

    /**
     *
     * @param num   le numero de membre.
     * @return  Retourne un utilisateur si trouve et nulle sinon.
     */
    public static Utilisateur getUser(int num) {
        for(Utilisateur utilisateur : utilisateurs) {
            if((utilisateur.getNumero() == num) && (!utilisateur.getEtat()
                    .matches("termine"))) { return utilisateur; }
        }
        return null;
    }

    /**
     *
     * @param numPro    le numero de membre du professionnel a chercher.
     * @return  Retourne le professionnel si trouve et nulle sinon.
     */
    public static Professionnel getPro(int numPro) {
        for(Professionnel professionnel: BaseDonnees.getProfessionnels()) {
            if(professionnel.getNumero() == numPro) {
                return professionnel;
            }
        }
        return null;
    }

    /**
     *
     * @param numMembre    le numero de membre du membre a chercher.
     * @return  Retourne le membre si trouve et nulle sinon.
     */
    public static Membre getMembre(int numMembre) {
        for(Membre membre: membres) {
            if(membre.getNumero() == numMembre) {
                return membre;
            }
        }
        return null;
    }

    /**
     *
     * @param numEmploye    le numero de membre du employe a chercher.
     * @return  Retourne le employe si trouve et nulle sinon.
     */
    public static Employe getEmploye(int numEmploye) {
        for(Employe employe: BaseDonnees.getEmployes()) {
            if(employe.getNumero() == numEmploye) {
                return employe;
            }
        }
        return null;
    }

    /**
     * @param input Scanner.
     * @param seance    La seance que le logiciel va chercher dans sa liste
     *                  des numeros de membres inscrits
     * @return  Cette fonction va composer un string de tous les utilisateurs
     *          inscrits dans la seance et retoune la valeur recu par
     *          validerUtilisateur.
     *          En d'autre mots, elle affiche tous les numeros de membres inscrits
     *          dans la seance et si l'utilisateur a valide son numero, elle va le
     *          retourner sinon elle retourne '0'.
     * @throws NotFoundException    Les exceptions des generateurs du code QR.
     * @throws IOException
     * @throws WriterException
     */
    public static int getNumMembre(Scanner input, Seance seance)
            throws NotFoundException, IOException, WriterException {
        StringBuilder msg = new StringBuilder("Les membres inscrit " +
                "dans la seance de (" + seance.getNom() + ") sont : ");
        for(int user : seance.getNumMembres()) {
            msg.append(user).append(", ");
        }
        System.out.println(msg);
        return Utilisateur.validerUtilisateur(input, false);
    }

    /*  Setters.    */
    /**
     * @param employes  Changer la liste des employes avec une nouvelle liste.
     */
    public static void setEmployes(ArrayList<Employe> employes) {
        BaseDonnees.employes = employes;
    }
    /**
     * @param membres   Changer la liste des membres avec une nouvelle liste.
     */
    public static void setMembres(ArrayList<Membre> membres) {
        BaseDonnees.membres = membres;
    }
    /**
     * @param professionnels    Changer la liste des professionnels avec une
     *                          nouvelle liste.
     */
    public static void setProfessionnels(ArrayList<Professionnel> professionnels) {
        BaseDonnees.professionnels = professionnels;
    }
    /**
     * @param seances   Changer la liste des seances avec une nouvelle liste.
     */
    public static void setSeances(Map<Integer, ArrayList<Seance>> seances) {
        BaseDonnees.seances = seances;
    }
    /**
     * @param utilisateurs Changer la liste des utilisateurs avec une
     *                     nouvelle liste.
     */
    public static void setNumUtilisateurs(ArrayList<Utilisateur> utilisateurs) {
        utilisateurs = utilisateurs;
    }

    /*  Adders.  */
    /**
     * @param utilisateur   Le utilisateur a ajouter a la liste des
     *                      utilisateurs.
     * @return  Retourne la position d'utilisateur ajoute dans la liste qui
     *          sera son id specifique.
     */
    public static void addUtilisateur(Utilisateur utilisateur) {
        utilisateurs.add(utilisateur);
    }

    /**
     * @param membre   Le membre a ajouter a la liste des membres.
     * @return  Retourne la position du membre ajoute dans la liste qui
     *          sera son id specifique.
     */
    public static int addMembre(Membre membre) {
        membres.add(membre);
        return membres.indexOf(membre);
    }

    /**
     * @param pro   Le professionnel a ajouter a la liste des professionnels.
     * @return  Retourne la position du professionnel ajoute dans la liste qui
     *          sera son id specifique.
     */
    public static int addPro(Professionnel pro) {
        professionnels.add(pro);
        return professionnels.indexOf(pro);
    }

    /**
     * @param employe   L'employe a ajouter a la liste des employes.
     * @return  Retourne la position de l'employe ajoute dans la liste qui
     *          sera son id specifique.
     */
    public static int addEmploye(Employe employe) {
        employes.add(employe);
        return employes.indexOf(employe);
    }

    /**
     * @param date  La date de la transaction.
     * @param transaction   La transaction elle meme qui sera ajoute a la
     *                      liste des transactions faites au #GYM dont la
     *                      cle est la date en parametre 'date'.
     */
    public static void addTransaction(String date, Transaction transaction) {
        transactions.putIfAbsent(date, new ArrayList<>());
        for(Map.Entry<String, ArrayList<Transaction>> entry :
                transactions.entrySet()) {
            if(entry.getKey().matches(date)) {
                entry.getValue().add(transaction);
            }
        }
    }

    /**
     * @param utilisateur   L'utilisateur qui va s'inscrire a la seance.
     * @param seance    La seance que l'utilisateur veut s'inscrire
     *                  dans laquelle.
     * @param transaction   La transaction que l'utilisateur a fait pour
     *                      completer son inscription a la seance.
     * @return  Retourne un booleen qui indique si l'inscription est
     *          faite ou pas.
     */
    public static boolean addInscription(Utilisateur utilisateur, Seance seance,
                                         Transaction transaction) {
        if(seance.getInscriptions().size() <= seance.getCapacite()) {
            Inscription inscription = new Inscription(utilisateur, seance,
                    transaction);
            Professionnel pro = getPro(seance.getNumPro());
            assert pro != null;
            seance.addInscription(inscription);
            utilisateur.addSeancesPris(seance);
            return true;
        }
        System.out.println("La capacite maximale est atteinte!Impossible " +
                "de creer votre inscription!");
        return false;
    }

    /*  Adders. */
    /**
     * @param numWeek   La semaine dont le numero est numWeek.
     * @param seance    La seance a ajouter dans la liste dont la cle est
     *                  numWeek.
     */
    public static void addSeance(int numWeek, Seance seance) {
        Objects.requireNonNull(getSeancesSemaine(numWeek)).add(seance);
    }

    /**
     * @param courriel  Ajouter le courriel courriel a la liste des courriels
     *                  des utilisateurs.
     */
    public static void addCourriel(String courriel) {
        courriels.add(courriel);
    }

    /*  -------------------------------------------------------------------  */

    /**
     *
     * @param input Scanner.
     * @param optionChoisiStr   L'option choisi pour actualiser dans la seance.
     * @param seance    La seance que le logiciel va actualiser un des ses
     *                  attributs.
     * @param professionnel Le professionnel qui cherche a actualiser un de(ses) seances.
     * @param seule Un booleen qui indique si le professionnel cherche a
     *              actualiser une seule seance ou plusieurs.
     */
    public static void actualiserAttribut(Scanner input, String optionChoisiStr,
                                          Seance seance,
                                          Professionnel professionnel,
                                          boolean seule) {

        String choix = "";
        String msg = "";
        int choixInt = 0;
        boolean found = false;
        switch (optionChoisiStr) {
            case "1":
                choix = verifierData(input,
                        "Date de debut(JJ-MM-YYYY) :", "", 8,null, "", false);
                msg = "Date de debut";
                break;
            case "2":
                choix = verifierData(input, "Date de fin(JJ-MM-YYYY)" +
                                "[2 semaines - 3 mois] apres date debut(" +
                                seance.getDateDebut() +") :",
                        "", 8, seance.getDateDebut(), "", false);
                msg = "Date de fin";
                break;
            case "3":
                choix = verifierData(input, "Heure(HH:MM-HH:MM) :",
                        "", 9, null, "", false);
                msg = "Heure";
                break;
            case "4":
                choix = verifierData(input, "Recurrence((J),..)",
                        "", 10, null, "", false);
                msg = "Recurrence";
                break;
            case "5":
                choix = verifierData(input, "Capacite maximale : ",
                        "", 11, null, "", false);
                choixInt = Integer.parseInt(choix);
                msg = "Capacite maximale";
                break;
            case "6":
            case "7":
                while(!found) {
                    if (optionChoisiStr.matches("6")) {
                        choix = verifierData(input, "Code de professionnel" +
                                        "(9 chiffres) : ", "professionnel", 12,
                                null, "", false);
                        choixInt = Integer.parseInt(choix);
                        Utilisateur user = getUser(choixInt);
                        if(user != null && user.getEtat().matches("professionnel")) {
                            found = true;
                        } else {
                            System.out.println("Code du professionnel " +
                                    "invalide! Veuillez reessayer avec " +
                                    "un nouveau code!");
                        }
                        msg = "Code de professionnel";
                    } else {
                        choix = verifierData(input,
                                "Frais de service :", "", 14, null,
                                "", false);
                        choixInt = Integer.parseInt(choix);
                        if(choixInt >= 0 && choixInt <= 100) {
                            found = true;
                        } else {
                            System.out.println("Le montant est invalide!" +
                                    " Veuillez reessayer avec un " +
                                    "nouveau montant!");
                        }
                        msg = "Frais de service";
                    }
                }
                break;
            case "8":
                choix = verifierData(input, "Commentaires : ", "",
                        19, null, "", false);
                msg = "Commentaires";
        }
        appliquerActualisations(choix, optionChoisiStr, choixInt, seance,
                msg, professionnel, seule);
    }

    /**
     * @param nouveauChoix  Le nouveau choix que le professionnel a decide de
     *                     le prendre pour faire l'actualisation.
     * @param optionChoisiStr   L'otion choisi a partir de la liste de options
     *                          affiches sur l'ecran.
     * @param choixInt  La forme int de optionChoisiStr.
     * @param seance    La seance que le professionnel cherche a actualiser.
     * @param msg
     * @param pro   Le professionnel qui cherche a actualiser ses(une de ses)
     *              seances.
     * @param seule Si c'est une seule seance a modifier ou non.
     */
    private static void appliquerActualisations(String nouveauChoix,
                                                String optionChoisiStr,
                                                int choixInt, Seance seance,
                                                String msg, Professionnel pro,
                                                boolean seule) {

        String choix = "";
        if (choixInt != 0) { choix += choixInt; }
        else { choix = nouveauChoix; }
        if(optionChoisiStr.matches("6") || optionChoisiStr.matches("7")) {
            if(optionChoisiStr.matches("6")) {
                if(seance != null && seance.getNumPro() == choixInt) {
                    System.out.println("Desole! Le code de professionnel est " +
                            "similaire au precedent!");
                    return;
                }
            } else {

                if(seance.getMontant() == choixInt) {
                    System.out.println("Desole! Le montant e payer est" +
                            " similaire au precedent!");
                    return;
                }
            }
        }
        String notification = "Notification de seance : Le choix : " + msg +
                " est modifie sur le systeme!";

        for(Seance seance1 : pro.getSeances()) {
            assert seance1 != null;
            if(!seance1.getEtatSeance().matches("termine")) {
                if(seule) {
                    if(comparerSemaine(seance1) &&
                            getTodayStr().matches(seance1.getJourSeance())) {
                        switchActualisation(optionChoisiStr, seance1, choix,
                                choixInt, msg);
                        System.out.println(notification);
                    }
                } else {
                    switchActualisation(optionChoisiStr, seance1, choix,
                            choixInt, msg);
                    System.out.println(notification);
                }
            }
            if(choixInt == 6) {
                assert seance != null;
                seance.setEtatSeance("termine");
            }
        }
    }

    /**
     * @param optionChoisiStr   L'option choisi qui sera utilise dans le switch.
     * @param seance    La seance a modifier.
     * @param choix Le nouveau choix a faire.
     * @param choixInt  Le nouveau choix en int si c'est un int qu'on cherche
     *                  a modifier.
     * @param msg   Un string qui indique le type de modification que le
     *              professionnel a fait pour la seance(Ex : modifier le nom,
     *              la capacite, etc.).
     */
    private static void switchActualisation(String optionChoisiStr,
                                            Seance seance, String choix,
                                            int choixInt, String msg) {

        switch (optionChoisiStr) {
            case "1":
                seance.setDateDebut(choix);
                break;
            case "2":
                seance.setDateFin(choix);
                break;
            case "3":
                seance.setHeures(choix);
                break;
            case "4":
                seance.setRecurrence(choix);
                break;
            case "5":
                seance.setCapacite(choixInt);
                break;
            case "6":
                seance.setNumPro(choixInt);
                Professionnel pro = getPro(choixInt);
                assert pro != null;
                addSeance(getCurrentWeek(), seance);
                break;
            case "7":
                seance.setMontant(choixInt);
                break;
            case "8":
                seance.setCommentaires(choix);
                break;
        }
        String notification = "le choix : " +
                msg + " est modifie dans le service : "
                + seance.getNom();
        notifierMembres(seance, notification);
    }

    /**
     * @param seance    La seance destinee pour l'actualisation a faire.
     * @param msg   Le type de changement que le logiciel a fait pour la seance
     *              et qui sera envoye en message aux utilisateurs inscrits
     *              dans la seance.
     */
    private static void notifierMembres(Seance seance, String msg) {
        for (Map.Entry<Inscription, Boolean> entry :
                seance.getInscriptions().entrySet()) {
            Inscription inscription = entry.getKey();
            Utilisateur user = inscription.getMembreInscription();
            assert user != null;
            if (user.getEtat().matches("membre")) {
                Membre membre = (Membre) user;
                System.out.println("Notification au membre(" +
                        membre.getNom() + ") : Le membre est notifie que " + msg);
            }
        }
    }

    /**
     * @param seance    La seance a supprimer(la seance sera marquee comme
     *                  termine pour avoir toujours des records a toutes les
     *                  seances creees au #GYM).
     */
    public static void supprimerSeance(Seance seance) {
        seance.setEtatSeance("termine");
        System.out.println("seance supprime!");
        notifierMembres(seance, "la seance est supprimee!");
    }

    /**
     * @param pro   Le professionnel qui cherche a imprimer les inscriptions
     *              d'une de ses seances.
     */
    public static boolean imprimerInscriptions(Professionnel pro) {

        ArrayList<Inscription> membresInscrits =
                pro.getInscriptionsSeances(getCurrentWeek());
        int totalInscription = membresInscrits.size();
        if(totalInscription > 0) {
            for (Inscription inscription : membresInscrits) {
                System.out.println(inscription);
            }
        }
        System.out.println("Le total des inscriptions = " +
                totalInscription + " inscriptions.");
        return totalInscription > 0;
    }

    /**
     * @param membre    Le membre qui cherche a inscire dans la seance.
     * @param service   Les seances de service que le membre sera inscrit a
     *                  lesquelles des que la transaction est termine.
     * @param transaction   La transaction que le membre va faire pour
     *                      s'inscrire a la seance.
     * @return  Retourne vrai ssi la seance est dans la liste des seances
     *          et que le nom de service est similaire au nom de service de la
     *          seance.
     */
    static boolean inscrireSeance(Membre membre, Service service, Transaction transaction) {
        ArrayList<Seance> seanceArrayList = chercherSeancesToday();
        if(service != null && membre != null && transaction != null) {
            if (seanceArrayList != null) {
                for (Seance seance : seanceArrayList) {
                    if (seance.getNom().matches(service.getNom())) {
                        Inscription inscription = new Inscription(membre, seance, transaction);
                        seance.getInscriptions().put(inscription, false);
                        return true;
                    }
                }
            } else { System.out.println("Pas de seances pour aujourd'hui!"); }
        }
        return false;
    }

    /**
     *  Au debut de chaque mois, chaque client sera facture les frais
     *  mensuels du #GYM.
     */
    public static void appliquerRenouvellement() {
        for(Membre membre : membres) {
            if(membre.getEtat().matches("membre")) {
                membre.setMontantdu(Membre.getFraisMensuelles());
            }
        }
    }

    /**
     *
     *
     * @param seance    La seance dont l'utilisateur voulait actualiser sa
     *                  presence dans laquelle.
     * @param numMembre C'est le numero de membre d'utilisateur qui voulait
     *                  confirmer sa presence.
     * @param mob   Booleen qui indique si l'utilisateur est sur
     *              l'application mobile ou il est present au comptoir.
     * @return  Retourne un booleen qui indique l'utilisateur a soit deja
     *          confirmer sa presence ou que sa confirmation etait deja
     *          confirmee un peu plus tot.
     * @throws IOException  Les exceptions liees au code QR.
     * @throws NotFoundException
     * @throws WriterException
     */
    public static boolean actualiserPresence(Seance seance, int numMembre,
                                             boolean mob)
            throws IOException, NotFoundException, WriterException {

        for(Map.Entry<Inscription, Boolean> entry:
                seance.getInscriptions().entrySet()) {
            Inscription inscription = entry.getKey();
            Utilisateur user = inscription.getMembreInscription();
            if(inscription.getMembreInscription().getNumero() == numMembre) {
                if(!entry.getValue()) {
                    entry.setValue(true);
                    if(mob) { System.out.println("Valide!\n" + user.getQR()); }
                    else { System.out.println("Valide!\n" + MenuGYM.
                            imprimerConfirmation(seance, numMembre)); }
                } else {
                    System.out.println("Le(" + user.getEtat() + ") : '" +
                            user.getNom() + "' a deja confirme sa " +
                            "presence a la seance!");
                }
                return true;
            }
        }
        System.out.println("Acces refuse!le membre '" + getMembre(numMembre)
                .getNom() + "' n'est pas inscrit!");
        if(!mob) {
            Scanner input = new Scanner(System.in);
            MenuGYM.membreInvalide(input);
        }
        return false;
    }



    /**
     * @param seanceStr
     * @param confirmee
     * @return
     */
    public static Seance verifierSeance(String seanceStr, boolean confirmee) {
        if(confirmee) {
            int numSeance = Integer.parseInt(seanceStr);
            for (Map.Entry<Integer, ArrayList<Seance>> entry : BaseDonnees.
                    getSeances().entrySet()) {
                for(Seance seance : entry.getValue()) {
                    if (!seance.getEtatSeance().matches("termine")) {
                        if (seance.getSeanceID() == numSeance) {
                            return seance;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return
     */
    public static ArrayList<Seance> chercherSeancesToday() {

        String today = getDate("dd-MM-yyyy");
        String[] todayDate = today.split("-");
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekStr = joursSemaine[dayOfWeek - 1];
        ArrayList<Seance> seancesJour = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Seance>> entry : BaseDonnees.
                getSeances().entrySet()) {
            for(Seance seance : entry.getValue()) {
                if (!seance.getEtatSeance().matches("termine")) {
                    String[] startDate = seance.getDateDebut().split("-");
                    String[] endDate = seance.getDateFin().split("-");
                    if (calculerDiff(endDate, todayDate) >= 0 &&
                            calculerDiff(todayDate, startDate) >= 0 &&
                            seance.getJourSeance().matches(dayOfWeekStr) &&
                            BaseDonnees.comparerSemaine(seance)) {
                        seancesJour.add(seance);
                    }
                }
            }
        }
        if(seancesJour.isEmpty()) { return null; }
        else { return seancesJour; }

    }

    /*  -----------------------------------------------------------------    */

    /*
        -   Valider acces d'utilisateur :
            1.  Menu.
            2.  Mobile.
     */
    /**
     * @param input Scanner.
     * @return  Retourne un utilisateur si le numero de membre est valide et
     *          se trouve dans la base de donnees et nulle sinon.
     */
    static Utilisateur validerAccesMenu(Scanner input) {

        String data = MenuGYM.buildString(false);
        String msg = data + "\nVeuillez entrer votre numero de membre?";
        String numeroStr = BaseDonnees.verifierData(input, msg, "", 12,
                null, "", false);
        int numero = Integer.parseInt(numeroStr);
        return BaseDonnees.getUser(numero);
    }

    /**
     * @param utilisateur   L'utilisateur qui veut valider son acces.
     * @return Retourne vrai si l'utilisateur est valide et faux sinon.
     *
     */
    static boolean validerAccesMob(Utilisateur utilisateur) {

        if(Tourniquet.inQRCodes(utilisateur.getQR())) {
            System.out.println(utilisateur.getConfirmation());
            System.out.println("Tourniquet ouvert!\nLe (" + utilisateur.getEtat()
                    + ") : " + utilisateur.getNom() +
                    " pourrait acceder au #GYM!");
            return true;
        } else {
            System.out.println("Acces invalide!\nTourniquet ferme!");
            return false;
        }
    }

    /*  -----------------------------------------------------------------    */

    /*  Une partie du code qui sert a verifier si les donnees passees
        en parametres sont compatibles au format demande.

        verifierData options:
        ---------------------
        Case 1:     Nom.
        Case 2:     Adresse.
        Case 3:     Ville.
        Case 4:     Province.
        Case 5:     Code postal.
        Case 6:     Telephone.
        Case 7:     Courriel.
        Case 8:     Date de debut valide ou Date de fin valide.
        Case 9:     Heures valides.
        Case 10:    Les jours de recurrence hebdomadaire.
        Case 11:    La capacite d'une seance.
        Case 12:    Numero de pro.
        Case 13:    Code de seance.
        Case 14:    Montant pour les frais.
        Case 15:    Numero de la carte credit.
        Case 16:    Expiration carte.
        Case 17:    CVV.
        default:    La longueur des commentaires.  */
    /**
     * @param inscriptionScanner    Scanner
     * @param msg   Le message affcihe pour indiquer laquelle des cases
     *             a remplir.
     * @param personnel indique l'etat du personnel.
     * @param cndtn_idx L'index a visiter dans le switch.
     * @param dateCompare   Si la date est la date de fin alors la valeur sera
     *                      non-nulle. Sinon, la valeur sera toujours nulle.
     * @param numProStr Le numero de professionnel pour l'utiliser a la
     *                  codeSeance. Sinon il sera toujours vide.
     * @param preCreated    Si c'est appele a travers la methode creerBD().
     *                      Sinon il sera toujours faux.
     * @return  Retourne la valeur de input si elle respecte la syntaxe. Sinon,
     *          une boucle while sera presente pour demander une information
     *          valide.
     */
    public static String verifierData(Scanner inscriptionScanner, String msg,
                                      String personnel, int cndtn_idx,
                                      String dateCompare, String numProStr,
                                      boolean preCreated) {

        System.out.println(msg);
        String input = inscriptionScanner.nextLine();
        String format = "doit correspondre au bon format";
        switch (cndtn_idx) {
            case 1:
                while (!verifierNom(input)){
                    input = getInput(inscriptionScanner, msg,
                            "Le nom" + format + "(x,x)!");
                }
                break;
            case 2:
                while (!verifierAdresse(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "L'adresse " + format + "(# rue #)!");
                }
                break;
            case 3:
                while (!verifierVille(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "Le nom de la ville " + format +
                                    "(*|*-*|*(_)*|* *)(sans chiffres)!");
                }
                break;
            case 4:
                while (!verifierProvince(input)) {
                    input = getInput(inscriptionScanner, msg, "Le code de la" +
                            " province " + format + "Canadien(xx)!");
                }
                break;
            case 5:
                while (!verifierCodePostale(input)) {
                    input = getInput(inscriptionScanner, msg, "Le code " +
                            "postal " + format + "Canadien(xxx xxx|xxxxxx)!");
                }
                break;
            case 6:
                while (!verifierTelepohne(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "Le numero de telephone " + format +
                                    "(#(10 chiffres))!");
                }
                break;
            case 7:
                while (!verifierCourriel(input, personnel)) {
                    input = getInput(inscriptionScanner, msg, "Le courriel "
                            + format + "*(2+)@*(2+).*!");
                }
                break;
            case 8:
                if(dateCompare == null) {
                    while (!verifierDate(input,  "", preCreated)) {
                        input = getInput(inscriptionScanner, msg,
                                "La date de debut " + format + "(JJ-MM-AAAA)!");
                    }
                } else {
                    while (!verifierDate(input, dateCompare, false)) {
                        input = getInput(inscriptionScanner, msg,
                                "La date de fin " + format + "(JJ-MM-AAAA) !");
                    }
                }
                break;
            case 9:
                while (!verifierHeureService(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "L'heure entree n'est pas valide(HH:MM-HH:MM)!");
                }
                break;
            case 10:
                while (!verifierRecurrence(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "La recurrence entree n'est pas valide!");
                }
                break;
            case 11:
                while (!verifierLimite(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "L'entree ne correspond pas a la bonne limite(1,30)!");
                }
                break;
            case 12:
                while (!verifierNombre(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "Le numero d'utilisateur ne correspond " +
                                    "pas au bon format!");
                }
                break;
            case 13:
                while (!verifierCodeSeance(input, numProStr)) {
                    input = getInput(inscriptionScanner, msg,
                            "Le code de seance ne correspond pas au bon " +
                                    "format!!");
                }
                break;
            case 14:
                while (!verifierMontant(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "Le montant entre ne correspond pas au limites!");
                }
                break;
            case 15:
                while (!verifierCarte(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "Le numero de carte ne correspond pas au bon " +
                                    "format(45 ou 5(1-4))xx xxxx xxxx xxxx)!");
                }
                break;
            case 16:
                while (!verifierExpiration(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "La date d'expiration ne correspond pas au bon " +
                                    "format(MM/AA)!");
                }
                break;
            case 17:
                while (!verifierCVV(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "La CVV ne correspond pas au bon format" +
                                    "(xxx) ou (xxxx)!");
                }
                break;
            case 18:
                while (!verifierNomService(input)) {
                    input = getInput(inscriptionScanner, msg, "Le nom de " +
                            "service ne correspond pas a la liste des " +
                            "services au #GYM!(Soccer, Basket, Yoga, ...!)");
                }
            default:
                while (!verifierCommentaires(input)) {
                    input = getInput(inscriptionScanner, msg,
                            "La longueur du commentaire n'est pas respectee!");
                }
                break;
        }
        return input;
    }

    /**
     * @param inscriptionScanner    Scanner.
     * @param case_tofill   le message qui indique quelle case l'utilisateur
     *                      doit remplir pour l'instant(nom, adresse, etc.).
     * @param msg   Le message d'erreur a imprimer en cas de faute.
     * @return  Retourne un string qui contient les informations que
     *          l'utilisateur a entre au system en utilisant .nextLine().
     */
    private static String getInput(Scanner inscriptionScanner,
                                   String case_tofill, String msg) {

        System.out.println(msg);
        System.out.println(case_tofill);
        return inscriptionScanner.nextLine();
    }

    /**
     * @param nom   Nom d'utilisateur.
     * @param adresse   Son adresse.
     * @param ville Sa ville.
     * @param province  Son province.
     * @param codePostal    Son code postal.
     * @param telephone Son numero de telephone.
     * @param courriel  Son courriel.
     * @return  Retourne vrai ssi tous les verificateurs sont vrai.
     */
    static boolean verifierDonneesUsers(String nom, String adresse,
                                        String ville, String province,
                                        String codePostal, String telephone,
                                        String courriel) {
        return verifierNom(nom) && verifierAdresse(adresse) &&
                verifierVille(ville) && verifierProvince(province) &&
                verifierCodePostale(codePostal) && verifierTelepohne(telephone)
                && verifierCourriel(courriel, "");
    }

    /**
     * @param nom   Nom de service.
     * @param dateDebut Son date de debut.
     * @param dateFin   Son date de fin.
     * @param heures    Les heures des seances.
     * @param numPro    Le numero de professionnel assigne au cours.
     * @param recurrence    La recurrence hebdomadaire des seances.
     * @param capacite  La limite des inscriptions.
     * @param montant   Le montant a payer pour s'inscrire.
     * @param commentaires  Les commentaires s'il y en a.
     * @param preCreated    Si c'est deja cree par la methode creerBD() ou non.
     *                      Parce que sinon, il est impossible deja de creer
     *                      des seances au passe. Mais pour le testing c'est mis
     *                      comme exception pour cette methode afin de creer un
     *                      logiciel qui contient deja des donnees a tester.
     * @return  Retourne vrai ssi tous les verificateurs sont vrai.
     */
    static boolean verifierDonneesService(String nom, String dateDebut,
                                          String dateFin, String heures,
                                          int numPro, String recurrence,
                                          int capacite, int montant,
                                          String commentaires, boolean preCreated) {
        return verifierNomService(nom) && verifierDate(dateDebut, "",
                preCreated) && verifierDate(dateFin, dateDebut, preCreated) &&
                verifierHeureService(heures) &&
                verifierNombre(addZerosToNum(numPro).toString()) &&
                verifierRecurrence(recurrence) &&
                verifierLimite(Integer.toString(capacite)) &&
                verifierMontant(Integer.toString(montant)) &&
                verifierCommentaires(commentaires);
    }

    /**
     * @param tocompareCourriel Courriel a comparer
     * @param estMob   Booleen qui indique c'est pour
     *                              l'application mobile ou non.
     * @return  Retourne vrai selon estMob est vrai ou non. Si le logiciel
     *          n'est pas au mode 'mobile' donc la valeur de retour sera
     *          faux si le courriel a comparer est deja dans la base de
     *          donnees.
     *          Si c'est estMob alors on chercher a trouver le courriel dans
     *          la base de donnees est donc vrai si trouve.
     *          NB : la valeur de retour est inversee puisque c'est une boucle
     *          while(true) donc il suffit de retourner faux pour sortir de
     *          la boucle while.
     */
    public static boolean verifierCourriels(String tocompareCourriel,
                                            boolean estMob) {
        for(String courriel: courriels) {
            if(courriel.matches(tocompareCourriel)) {
                if(!estMob) {
                    System.out.println("Courriel est deja dans la base de donnees!");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param courriel  Le courriel que le logiciel va confirmer.
     * @param numero    le numero d'utilisateur si ce n'est pas l'application
     *                  mobile.
     * @param estMob    Un booleen pour separer entre les processus de
     *                  validation d'utilisateur si c'est estMob(vrai) donc
     *                  il suffit d'utiliser le courriel en parametre. Sinon,
     *                  on utilise le numero pour avoir l'utilisateur.
     * @return  Retourne l'utilisateur si trouve et nulle sinon.
     */
    public static Utilisateur verifierUtilisateur(String courriel, int numero,
                                                  boolean estMob) {
        for(Utilisateur utilisateur : utilisateurs) {
            if(estMob) {
                if (utilisateur.getCourriel().matches(courriel)) {
                    return utilisateur;
                }
            } else {
                if(utilisateur.getNumero() == numero) { return utilisateur; }
            }
        }
        return null;
    }

    /**
     * @param input Scanner.
     * @return  Un booleen pour indiquer que le nom respecte la syntaxe ou pas.
     */
    private static boolean verifierNom(String input) {
        if(input.matches("^([a-zA-Z_.\\s-]+,[a-zA-Z]" +
                "[a-zA-Z_.\\s-]*)$") && input.length() <= 25) {
            return true;
        } else {
            System.out.println("Nom invalide!");
            return false;
        }
    }

    /**
     * @param input Scanner.
     * @return  Un booleen pour indiquer que le nom de service respecte la
     *          syntaxe ou pas.
     */
    private static boolean verifierNomService(String input) {
        if(Service.getServices().containsKey(input) &&
                input.matches("(^[a-zA-Z_.\\s-]{1,20})$")) {
            return true;
        }
        System.out.println("Nom de service invalide!");
        return false;
    }


    /**
     * @param input Scanner.
     * @return  Un booleen pour indiquer que l'adresse respecte la
     *          syntaxe ou pas.
     */
    private static boolean verifierAdresse(String input) {
        if(input.matches("\\d+ rue \\w+")) {
            return true;
        } else {
            System.out.println("Adresse invalide!");
            return false;
        }
    }

    /**
     * @param input Scanner.
     * @return  Un booleen pour indiquer que le numero de telephone respecte
     *          la syntaxe ou pas.
     */
    private static boolean verifierTelepohne(String input) {
        if(input.length() == 10 &&
                input.chars().allMatch(Character::isDigit)) {
            for(Utilisateur utilisateur: getUtilisateurs()) {
                if(utilisateur != null) {
                    if (utilisateur.getTelephone().matches(input)) {
                        System.out.println("Numero de telephone est deja present!");
                        return false;
                    }
                }
            }
            return true;
        }
        System.out.println("Numero de telephone est invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Un booleen pour indiquer que le code postal respecte la syntaxe ou pas.
     */
    private static boolean verifierCodePostale(String input) {
        if(input.matches("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] " +
                "?[0-9][A-Z][0-9]$")) {
            return true;
        }
        System.out.println("Code postal invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Un booleen pour indiquer que le nom de la ville respecte la
     *          syntaxe ou pas.
     */
    private static boolean verifierVille(String input) {
        if(input.length() <= 25 && !input.matches(".*\\d.*")) {
            return true;
        }
        System.out.println("La ville est invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @param personnel Si c'est pour un acces mobile donc mob est vrai.
     * @return  Un booleen pour indiquer que l'adresse courriel respecte la
     *          syntaxe ou pas.
     */
    private static boolean verifierCourriel(String input, String personnel) {
        boolean verifierInscription = false;
        if(personnel.matches("mob")) { verifierInscription = true; }
        if(!verifierCourriels(input, verifierInscription)) {
            if(input.matches("^([\\p{L}-_.[0-9]]+){1,64}@([\\p{L}-_.[0-9]]+)" +
                    "{2,255}.[a-z]{2,}$")) {
                return true;
            }
            System.out.println("Le format du courriel est invalide!");
            return false;
        }
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Un booleen pour indiquer que les heures de service respecte la
     *          syntaxe ou pas.
     */
    private static boolean verifierHeureService(String input) {
        if(input.matches("^(([01]?[0-9]|2[0-3]):[0-5][0-9])-" +
                "(([01]?[0-9]|2[0-3]):[0-5][0-9])$")) {
            if(verifierTemps(input)) { return true; }
        }
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Un booleen pour indiquer que le temps de la seance respecte la
     *          syntaxe ou pas.
     */
    private static boolean verifierTemps(String input) {
        String[] hours = input.split("-");
        String[] starTime = hours[0].split(":");
        String[] endTime = hours[1].split(":");
        String startHourStr = starTime[0];
        String endHourStr = endTime[0];
        String startMinStr = starTime[1];
        String endMinStr = endTime[1];
        int startHour = Integer.parseInt(startHourStr);
        int endHour = Integer.parseInt(endHourStr);
        int startMin = Integer.parseInt(startMinStr);
        int endMin = Integer.parseInt(endMinStr);
        String msg = "Le temps en ";
        String msg1 = " de depart est plus grand que le temps de fin!";
        if(startHour <= endHour) {
            if(startMin <= endMin) { return true; }
            System.out.println(msg + "minute" + msg1);
        } else { System.out.println(msg + "heure" + msg1); }
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Retourne un booleen qui indique que la limite 1 et 30 ou non.
     */
    private static boolean verifierLimite(String input) {
        if(input.matches("^[1-9]|[0][1-9]|[1-2][0-9]|[3][0]$")) {
            return true;
        }
        System.out.println("La capacite est invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @param numProStr Le numero du professionnel qui est assigne a la seance.
     * @return  Retourne un booleen qui indique que le code de la seance
     *          respecte la syntaxe demandee ou non.
     */
    public static boolean verifierCodeSeance(String input, String numProStr) {
        if(input.matches("^[0-9]{7}$")) {
            String[] codeSeance = input.split("");
            String[] proStrArr = numProStr.split("");
            String codeServiceStr = codeSeance[0] + codeSeance[1] +
                    codeSeance[2];
            String codeSeanceProStr = codeSeance[5] + codeSeance[6];
            int codeService = Integer.parseInt(codeServiceStr);
            String proStr = proStrArr[7] + proStrArr[8];
            int pro = Integer.parseInt(proStr);
            int codeSeancePro = Integer.parseInt(codeSeanceProStr);
            if (Service.getServices().containsValue(codeService)) {
                if (codeSeancePro == pro) { return true; }
            }
            System.out.println("Code invalide!"); }
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Retourne un booleen qui indique que le numero de membre
     *          respecte la syntaxe ou non.
     */
    private static boolean verifierNombre(String input) {
        if (input.matches("^[0-9]{0,9}$")) {
            if (!input.matches("0")) { return true; }
        }
        System.out.println("Nombre invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Retourne un booleen qui indique que le montant est au plus
     *          100$ ou pas.
     */
    private static boolean verifierMontant(String input) {
        if(input.matches("^[0]|[1-9][0-9]|[1][0][0]$")) {
            return true;
        }
        System.out.println("Montant invalide!");
        return false;
    }

    /**
     * @param commentaires  Un string pour les commentaires a ajouter a la
     *                      seance creee.
     * @return  Retourne un booleen qui indique que les commentaires sont au
     *          plus 100 caracteres ou pas.
     */
    private static boolean verifierCommentaires(String commentaires) {
        if(commentaires.length() <= 101) {
            return true;
        }
        System.out.println("La longueur des commentaires est invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Retourne un booleen qui indique que le numero de la carte
     *          respecte la syntaxe ou non.
     */
    private static boolean verifierCarte(String input) {
        if(input.matches("^[4][5][0-9]{14}|[5][1-4][0-9]{14}$")) {
            return true;
        }
        System.out.println("La carte est invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Retourne un booleen qui indique que la date d'expiration de la
     *          carte de credit respecte la syntaxe ou non.
     */
    private static boolean verifierExpiration(String input) {
        if(input.matches("^[1][2]/[1][9]|[0][1-9]/[2-9][0-9]|" +
                "[1][0-2]/[2-9][0-9]$")) {
            return true;
        }
        System.out.println("Date d'expiration invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Retourne un booleen qui indique que le cvv respecte la syntaxe
     *          ou non.
     */
    private static boolean verifierCVV(String input) {
        if(input.matches("^[0-9]{3,4}$")) {
            return true;
        }
        System.out.println("Code CVV invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Retourne un booleen qui indique que le code de province
     *          respecte la syntaxe ou non.
     */
    private static boolean verifierProvince(String input) {
        String[] provinces = {"AB", "BC", "MB", "NB", "NL", "NS", "ON",
                "PE", "QC", "SK", "NT", "NU", "YT"};
        for (String province : provinces) {
            if(input.matches(province)) { return true; }
        }
        System.out.println("Code province est invalide!");
        return false;
    }

    /**
     * @param input Scanner.
     * @return  Retourne un booleen qui indique que les recurrence respectent
     *          la syntaxe ou non.
     */
    public static boolean verifierRecurrence(String input) {

        List<String> list = Arrays.asList(joursSemaine);
        String[] vals = input.split("\\s*,\\s*");
        List<String> saved_vals = new ArrayList<>();
        for (String val : vals) {
            if (!list.contains(val) || saved_vals.contains(val)) { return false; }
            saved_vals.add(val);
        }
        return true;
    }

    /*  -------------------------------------------------------------------  */

    /*  Verificateurs des dates.    */
    /*  ========================    */

    /**
     * @param input Scanner.
     * @param dateCompare   La date de debut a comparer avec pour s'assurer que
     *                      la difference entre les deux dates est toujours
     *                      entre les 14 - 90 jours.
     * @param preCreated    Si c'est la methode creerBD() a appele cette
     *                      methode et donc le systeme pourra accepter
     *                      les dates plus anciennes que la date actuelle
     *                      d'aujourd'hui ou pas(faux).
     * @return  Retourne un booleen qui indique que la date est valide.
     */
    static boolean verifierDate(String input, String dateCompare,
                                 boolean preCreated) {

        if (input.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])\\-" +
                "(1[012]|0?[1-9])\\-((?:19|20)\\d{2})\\s*$")) {
            String startDate = getDate("dd-MM-yyyy");
            String[] endVals = input.split("-");
            String[] startVals;
            String[] todayVals = startDate.split("-");
            int diffTotal;
            boolean firstDate = dateCompare.matches("");

            if (firstDate) { startVals = todayVals; }
            else { startVals = dateCompare.split("-"); }

            int todayYears = Integer.parseInt(todayVals[2]);
            int todayMonths = Integer.parseInt(todayVals[1]);
            int todayDays = Integer.parseInt(todayVals[0]);
            int endYears = Integer.parseInt(endVals[2]);
            int endMonths = Integer.parseInt(endVals[1]);
            int endDays = Integer.parseInt(endVals[0]);

            if (!firstDate) {
                diffTotal = calculerDiff(endVals, startVals);
                if(diffTotal >= 14 && diffTotal <= 90) {
                    if (endYears >= todayYears) {
                        if (endYears == todayYears) {
                            if (endMonths >= todayMonths) {
                                if (endMonths == todayMonths)
                                    if (endDays >= todayDays) { return true; }
                                return true;
                            }
                            return true;
                        }
                        return true;
                    }
                }
                System.out.println("La periode est invalide!");
                return false;
            } else {
                if(!preCreated) {
                    diffTotal = calculerDiff(endVals, startVals);
                    return diffTotal >= 0;
                }
                return true;
            }

        } else { System.out.println("Date invalide!"); }
        return false;
    }

    /**
     * @param week  le numero de la semaine a avoir la date de laquelle.
     * @param day   Le numero de la journee dans la semaine pour avoir la
     *              date a partit de ces deux donnees en parametres.
     * @return  Retourne un string qui indique la date.
     */
    public static String getDateFromWeekNum(int week, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYY");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, day);
        return sdf.format(cal.getTime());
    }


    /*  Methode pour avoir la date et heure actuelle.   */
    /**
     * @param pattern   C'est la facon que le logiciel va utiliser pour avoir
     *                 la date.
     * @return  Retourne la date actuelle d'aujourd'hui.
     */
    static String getDate(String pattern) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }


    /**
     * @return  Retourne la journee actuelle en int.
     */
    static int getToday() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @return  Retourne la journee avec son nom en String.
     */
    static String getTodayStr() {
        return joursSemaine[getToday() - 1];
    }

    /**
     * @param day   le nom de la journee en String.
     * @return  Retourne la journee sous forme d'un int.
     */
    static int getDayInt(String day) {
        int i = 1;
        for(String jour : joursSemaine) {
            if(day.matches(jour)) {
                return i;
            }
            i += 1;
        }
        return 0;
    }

    /**
     * @return  Retourne le numero de la semaine courante en int.
     */
    static int getCurrentWeek() {
        return Integer.parseInt(getDate("w"));
    }

    /**
     * @param input Scanner.
     * @return  Retoune le numero de la semaine dans l'annee en int.
     */
    static int specificWeek(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ldspec = LocalDate.parse(input, formatter);
        return ldspec.get( IsoFields.WEEK_OF_WEEK_BASED_YEAR );
    }

    /**
     * @param seance    La seance dont la methode cherche a comparer son numero
     *                  de la semaine avec la semaine courante.
     * @return  Retourne vrai si les deux semaines a comparer sont les memes.
     */
    static boolean comparerSemaine(Seance seance) {
        return seance.getSemaineSeance() == getCurrentWeek();
    }

    /*  ------------------------------------------------------------------   */

    /*
        Methode pour s'assurer que la difference entre la date de debut et
        celle de la fin est bien respectee.
     */
    /**
     * @param endVals   C'est la date qu'on veut la comparer avec startVals.
     * @param startVals C'est la date de debut de la comparaison.
     *                  La deux date seront transferees en jours afin de
     *                  calculer la difference entre eux.
     * @return  Un int qui indique la difference entre les deux date en jours.
     */
    private static int calculerDiff(String[] endVals, String[] startVals) {

        int endYears = Integer.parseInt(endVals[2]) * 365;
        int endMonths = Integer.parseInt(endVals[1]) * 30;
        int endDays = Integer.parseInt(endVals[0]);
        int startYears = Integer.parseInt(startVals[2]) * 365;
        int startMonths = Integer.parseInt(startVals[1]) * 30;
        int startDays = Integer.parseInt(startVals[0]);
        return endYears + endMonths + endDays - startYears -
                startMonths - startDays;
    }

    /**
     * @param dateDebut Date de debut a comparer
     * @param dateFin   Date de fin a comparer
     * @return  Retourne un int en millisecondes qui sera transforme en
     *          jours a l'aide du calcul (1000*60*60*24)
     */
    static int calculerJours(String dateDebut, String dateFin){

        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = formatter1.parse(dateDebut);
            Date date2 = formatter1.parse(dateFin);
            long diff = date2.getTime() - date1.getTime();
            return (int) (diff / (1000*60*60*24));

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*  ------------------------------------------------------------------   */

    /*
       Creer une nouvelle Base de Donnees.
    */
    /**
     * Une base de donnees experimentale qui sert a tester la qualite du code.
     * @throws IOException
     * @throws WriterException
     * @throws NotFoundException
     */
    static void creerBD() throws IOException, WriterException, NotFoundException {

        int subscription = 60;
        Professionnel pro1 = new Professionnel("Adebayo,Bam", "9 rue main",
                "St-Laurent", "QC", "H4N1N5", "4382300306",
                "a@ab.com" ,true, true);
        EntraineurYoga pro2 = new EntraineurYoga("Young,Trae",  "8 rue main",
                "Montreal", "QC", "H4N2N8","4382300307",
                "b@ab.com", true, true);
        Professionnel pro3 = new Professionnel("Holiday,Jrue", "7 rue main",
                "Laval", "QC", "H4J2L1","4382300308",
                "c@ab.com", true, true);
        EntraineurYoga pro4 = new EntraineurYoga("Holiday,Justin",
                "6 rue main","Fort McMurray", "AB", "J2L1N5", "4382300309",
                "d@ab.com", true, true);
        Service piscine = Service.creerSeances("Piscine",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro1.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        Service basket = Service.creerSeances("Basket", "02-12-2019",
                "12-01-2020", "8:00-9:00", pro3.getNumero(), "dimanche," +
                        " mardi , jeudi, vendredi", 30, 100, "NBA juniors", true);
        Service yoga = Service.creerSeances("Yoga",  "20-11-2019",
                "02-02-2020", "17:00-19:00", pro4.getNumero(),
                "samedi,lundi,mercredi", 30, 20, "", true);
        Membre membre1 = new Membre("Irving,Kyrie", "12 rue main",
                "Iqaluit", "NU", "J5L2N3",
                "4382300303", "e@ab.com", false, false);
        Membre membre2 = new Membre("James,LeBron","11 rue main",
                "Calgary", "AB", "M1L2N2","4382300314",
                "f@ab.com", false, false);
        Membre membre3 = new Membre("Curry,Stephen", "10 rue main",
                "Laval", "QC", "H4J2L1","4382300310",
                "g@ab.com", false, false);
        membre2.setSuspendu(true);
        membre2.setMontantdu(Membre.getFraisMensuelles());
        assert yoga != null;
        Transaction transaction = new Transaction(yoga.getMontant(), "cash");
        assert piscine != null;
        Transaction transaction1 = new Transaction(piscine.getMontant(), "cash");
        assert basket != null;
        Transaction transaction2 = new Transaction(basket.getMontant(), "cash");
        inscrireSeance(membre1, yoga, transaction);
        inscrireSeance(membre2, piscine, transaction1);
        inscrireSeance(membre3, basket, transaction2);
        Gerant employe1 = Gerant.assignerGerant("Harden,James","13 rue main",
                "Quebec city", "QC", "H4M2A1", "4382300311",
                "h@ab.com", true, false);
        Gerant employe2 = Gerant.assignerGerant("Westbrook,Russel",
                "14 rue main","York", "ON", "H3M2N1",
                "4382300313", "i@ab.com", true, false);
        if(employe2 == null) {
            Employe employe3 = new Employe("Westbrook,Russel", "14 rue main",
                    "Laval", "QC", "H3A2M1", "4382300312",
                    "j@ab.com", true, false);
        }
        BaseDonnees.proceduresComptable();

    }

    /*  -------------------------------------------------------------------  */

    /*
        *   Option 7 :
        --------------
        7.  Procedure comptable..
     */

    /**
     *  La procedure comptable c'est celle responsable a envoyer les rapports
     *  aux membres, professionnels, gerant et RnB.
     */
    public static void proceduresComptable() {

        int dayOfWeek = BaseDonnees.getToday();
        String dateStr = BaseDonnees.getDate("dd-MM-yyyy HH:MM:SS");
        String[] date = dateStr.split("-");
        String[] date1 = date[2].split(" ");
        String[] date2 = date1[1].split(":");
        String todayStr = date[0];
        String heure = date2[0];
        String minute = date2[1];

        /*  Si c'est vendredi soir a minuit.    */
        if((dayOfWeek == 6) && heure.matches("00") && minute.matches("00")) {
            envoyerVenMinuit();
        }
        /*  Envoyer rapport financier a RnB a 21:00 heure chaque jour.   */
        if(heure.matches("21") && minute.matches("00")) {
            envoyerRnB(getDate("dd-MM-YYYY"));
        }

        /*  Faire un renouvellement automatique et mettre la somme au compte
            des membres au debut de chaque mois.   */
        if(todayStr.matches("01")) { appliquerRenouvellement(); }

    }

    /**
     * @param input Scanner.
     * @param user  l'utilisateur(membre, professionnel ou gerant) qui veut
     *              avoir une copie de son rapport soit par le recevoir a
     *              son compte ou en l'imprimant.
     */
    public static void traiterProComptable(Scanner input, Utilisateur user) {

        String msg = "Desole!Pas de donnees pour ";
        String msg1 = "a partir de ce numero de membre!";
        String[] choixListe = {"Envoyer les rapports " +
                "a votre compte.", "Imprimer les rapports."};
        String choix = MenuGYM.afficherOptions(input, choixListe, 1);
        while (choix == null) {
            choix = MenuGYM.afficherOptions(input, choixListe, 1);
        }
        String etat = user.getEtat();
        switch (etat) {
            case "gerant":
            case "membre":
            case "professionnel":
                if(!user.estSuspendu()) {
                    if (etat.matches("gerant")) {
                        Gerant gerant = Gerant.getGerant();
                        if (choix.matches("1")) {
                            gerant.addRapportHebdo(getCurrentWeek());
                        } else {
                            System.out.println(gerant
                                    .getRapportHebdo(getCurrentWeek()));
                        }
                    } else {
                        if (etat.matches("membre")) {
                            Membre membre = getMembre(user.getNumero());
                            assert membre != null;
                            if (choix.matches("1")) {
                                membre.addRapportHebdo(getCurrentWeek());
                            } else {
                                System.out.println(membre
                                        .getRapportHebdo(getCurrentWeek()));
                            }
                        } else {
                            if (etat.matches("professionnel")) {
                                Professionnel pro = getPro(user.getNumero());
                                assert pro != null;
                                if (choix.matches("1")) {
                                    pro.addRapportHebdo(getCurrentWeek());
                                } else {
                                    System.out.println(pro
                                            .getRapportHebdo(getCurrentWeek()));
                                }
                            } else {
                                System.out.println("Le membre : '" +
                                        user.getNom() + "' n'a pas pris des seances " +
                                        "pour cette semaine!");
                                break;
                            }
                        }
                    }
                    if (choix.matches("1")) {
                        System.out.println("Rapport envoye!");
                    }
                    break;
                }  else { System.out.println("Le " + user.getEtat() + " : '"
                        + user.getNom() + "' est deja suspendu!"); }
            default:
                if (choix.matches("1")) {
                    System.out.println(msg + "afficher " + msg1);
                } else {
                    System.out.println(msg + "imprimer " + msg1);
                }
        }
    }

    /**
     *  Une methode pour envoyer les rapports aux professionnels.
     */
    private static String envoyerRapportsPro() {
        StringBuilder str = new StringBuilder();
        for(Professionnel pro : professionnels) {
            if(pro.getInscriptionsSeances(getCurrentWeek()).size() > 0) {
                pro.addRapportHebdo(getCurrentWeek());
                str.append(pro.getRapportHebdo(getCurrentWeek()));
                System.out.println("Rapport envoye a : '" + pro.getNom() + "'");
            }
        }
        return str.toString();
    }

    /**
     *  Une methode pour envoyer les rapports aux membres.
     */
    private static String envoyerRapportsMembre() {
        StringBuilder str = new StringBuilder();
        for(Membre member : membres) {
            if(member.getSeancesSemaine(getCurrentWeek()).size() > 0) {
                member.addRapportHebdo(getCurrentWeek());
                str.append(member.getRapportHebdo(getCurrentWeek()));
                System.out.println("Rapport envoye a : '" + member.getNom() + "'");
            }
        }
        return str.toString();
    }

    /**
     *  Une methode pour envoyer les rapports aux professionnels.
     */
    private static String envoyerRapportsGerant(Gerant gerant) {
        gerant.addRapportHebdo(getCurrentWeek());
        System.out.println("Rapport envoye a : '" + gerant.getNom() + "'");
        return gerant.getRapportHebdo(getCurrentWeek());

    }

    public static String envoyerVenMinuit() {
        StringBuilder str = new StringBuilder();
        str.append(envoyerRapportsGerant(Gerant.getGerant()));
        str.append(envoyerRapportsMembre());
        str.append(envoyerRapportsPro());
        return str.toString();
    }

    /*  -------------------------------------------------------------------  */

    /*
        Option 8 (Menu) ou 5 (Mob) :
        ----------------------------
        8.  Quitter.
     */
    /**
     *  Methode utilisee pour supprimer les PNGs creees par le logiciel avant
     *  de quitter(a l'aide d'un quit du systeme). Avant le input.close.
     */
    public static void deletePNGs() {

        ArrayList<String> files = Utilisateur.getFilenames();
        for(String filename : files) {
            File file = new File(filename);
            file.delete();
        }

    }

}
