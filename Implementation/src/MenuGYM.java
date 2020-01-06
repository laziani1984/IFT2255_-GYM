 import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.IOException;
import java.util.*;

/**
 * @author Wael ABOU ALI.
 *
 */
public class MenuGYM {

    /*  Constructeur.   */
    /**
     *  Creer une base de donnees pour commencer le transfert de data entre
     *  les differents classes.
     */
    public MenuGYM() { BaseDonnees.creerBaseDonnees(); }

    /**
     * @param input Scanner.
     * @throws NotFoundException    Les exceptions causees par la generation
     * @throws IOException          des codes QR.
     * @throws WriterException
     * Choisir application est responsable du choix initial de l'application
     * qu'on veut utiliser. Si oui, l'application sera l'application mobile.
     * Sinon, l'application sera celle du comptoir du #GYM.
     */
    public void choisirApplication(Scanner input)
            throws NotFoundException, IOException, WriterException {

        BaseDonnees.creerBD();
        String[] optionsIntro = {"Acceder a votre espace client", "Quitter"};
        String msg = "Etes-vous sur l'application mobile? (oui/non)";
        boolean choix = verifierChoix(input, msg);
        // Le client utilise l'appareil mobile.
        if (choix) {
            boolean fin = false;
            while (!fin) {
                String intro = "Veuillez choisir parmi les options suivantes :";
                System.out.println(intro);
                String choixIntro = afficherOptions(input, optionsIntro, 1);
                while (choixIntro == null) {
                    System.out.println(intro);
                    choixIntro = afficherOptions(input, optionsIntro, 1);
                }
                // 1. Acceder au espace client.
                if (choixIntro.matches("1")) {
                    fin = verifierClient(input);
                } else {
                    // 2. Quitter.
                    fin = menuReception(input, "8");
                }
            }
            // Le client est l'agent de reception au comptoir.
        } else {
            String message = "Bonjour et bienvenue au #GYM!\nChoisissez une" +
                    " option : ";
            String[] listeChoix = {"Gerer utilisateur.", "Gerer seance.",
                    "S'inscrire a une seance.", "Confirmer une presence a " +
                    "une seance.", "Consulter des inscriptions.",
                    "Valider l'acces au centre.", "Consulter rapports.",
                    "Quitter!"};
            chooseMenu(input, message, listeChoix, null);
        }
    }

    /**
     * @param input Scanner.
     * @return  Retourne vrai si l'adresse courriel d''utilisateur se trouve
     *          deja dans la base de donnees du #GYM. Si oui il pourra acceder
     *          a son espace client sinon il pourra reactiver son compte si
     *          c'est suspendu ou s'inscrire si invalide.
     * @throws IOException  Les exceptions causees par la generation des
     * @throws NotFoundException    codes QR.
     * @throws WriterException
     */
    public boolean verifierClient(Scanner input)
            throws IOException, NotFoundException, WriterException {

        boolean enregistre;
        while (true) {
            String courriel = BaseDonnees.verifierData(input, "Adresse courriel(Facebook) :",
                    "mob", 7, null, "", false);
            enregistre = BaseDonnees.verifierCourriels(courriel, false);
            if (!enregistre) {
                // Si compte est invalide.
                boolean inscrire = verifierChoix(input, "Votre " +
                        "compte est invalide\nVotre adresse " +
                        "courriel ne se trouve pas dans la " +
                        "base de donnees du #GYM!\nVoulez-vous" +
                        " vous inscrire au #GYM? (oui/non)");
                if (inscrire) {
                    int resultat = Utilisateur.adhererMenu(input);
                    if(resultat == 0) { return false; }
                } else {
                    boolean retry = verifierChoix(input, "voulez-vous" +
                            " reessayer une autre fois? (oui/non)");
                    if (!retry) {
                        return !continuerGestion(input);
                    }
                }
            } else {
                // Si compte est present(suspendu ou valide).
                Utilisateur utilisateur = BaseDonnees.verifierUtilisateur
                        (courriel, 0, true);
                assert utilisateur != null;
                System.out.println(utilisateur.getNom());
                // Suspendu.
                if (Objects.requireNonNull(utilisateur).estSuspendu()) {
                    boolean reactiver = verifierChoix(input,
                            "Votre compte est suspendu!\nVoulez-" +
                                    "vous le reactiver? " +
                                    "(oui/non)");
                    if (reactiver) {
                        utilisateur.actualiser(input);
                        afficherMenuMob(input, utilisateur);
                    } else {
                        boolean retry = verifierChoix(input, "Voulez-vous " +
                                "reessayer avec un nouveau compte?" +
                                " (oui/non)");
                        if (!retry) { return !continuerGestion(input); }
                    }
                    // Valide.
                } else { afficherMenuMob(input, utilisateur); }
            }
            return true;
        }
    }

    /**
     * @param input Scanner.
     * @param choixBienvenue    Le choix que l'utilisateur a fait.
     * @param utilisateur   L'utilisateur de l'application mobile.
     * @return  Retourne un booleen qui indique si l'utilisateur veut quitter
     *          ou non.
     * @throws NotFoundException    Les exceptions causees par la generation
     * @throws IOException          des codes QR.
     * @throws WriterException
     */
    private boolean menuMob(Scanner input, String choixBienvenue,
                            Utilisateur utilisateur)
            throws NotFoundException, IOException, WriterException {

        switch (choixBienvenue) {
            case "1":
                BaseDonnees.validerAccesMob(utilisateur);
                break;
            case "2":
                if (utilisateur.getEtat().matches("professionnel")) {
                    Professionnel.consulterInscriptions(input,
                            (Professionnel) utilisateur);
                } else { Seance.inscrireSeance(input, utilisateur, true); }
                break;
            case "3":
                Seance.confirmerPresence(input, utilisateur);
                break;
            case "4":
                return verifierClient(input);

            default:
        }
        return quitOrContinue(input, choixBienvenue, "5");
    }

    /**
     * @param input Scanner.
     * @param Utilisateur   L'utilisateur de l'application mobile.
     * @throws NotFoundException    Les exceptions pour le generateur des
     * @throws IOException          codes QR.
     * @throws WriterException
     */
    public void afficherMenuMob(Scanner input, Utilisateur Utilisateur)
            throws NotFoundException, IOException, WriterException {

        String message = "Votre compte est valide!\nBienvenue \"" +
                Utilisateur.getNom() + "\" a votre espace client(" +
                Utilisateur.getEtat() + ").\nChoisissez une option : ";
        // Le menu d'affichage differe selon le type du client.
        // Si c'est un professionnel.
        if (Utilisateur.getEtat().matches("professionnel")) {
            String[] choixProf = {"Acceder au #GYM", "Consulter inscriptions",
                    "Confirmer presence d'un membre", "Changer utilisateur",
                    "Quitter"};
            chooseMenu(input, message, choixProf, Utilisateur);
        } else {
            // Si c'est un membre.
            if (Utilisateur.getEtat().matches("membre")) {
                String[] choixMembre = {"Acceder au #GYM", "S'inscrire a une" +
                        " seance", "Confirmer presence a une seance",
                        "Changer utilisateur", "Quitter"};
                chooseMenu(input, message, choixMembre, Utilisateur);
            }
        }
    }

    /**
     * @param input Scanner.
     * @param choixBienvenue    Le choix que l'utilisateur a fait.
     * @return  Retourne un booleen qui indique si l'utilisateur veut quitter
     *          ou non.
     * @throws NotFoundException    Les exceptions pour le generateur des
     * @throws IOException          codes QR.
     * @throws WriterException
     */
    private boolean menuReception(Scanner input, String choixBienvenue)
            throws NotFoundException, IOException, WriterException {

        switch (choixBienvenue) {
            case "1":
                gerer(input, true, false);
                break;
            case "2":
                gerer(input, false, true);
                break;
            case "4":
                Seance.confirmerPresence(input, null);
                break;
            case "3":
            case "5":
            case "7":
                boolean done = false;
                while (!done) {
                    if (choixBienvenue.matches("5")) {
                        done = Professionnel.consulterInscriptions(input, null);
                    } else {
                        if (choixBienvenue.matches("7")) {
                            done = proComptable(input);
                        } else {
                            done = Seance.inscrireSeance(input, null, true);
                        }
                    }
                }
                break;
            case "6":
                Utilisateur.validerUtilisateur(input, true);
                break;
            default:
        }
        return quitOrContinue(input, choixBienvenue, "8");
    }

    /*  La methode est package-private et donc accessible par GYM.  */
    /**
     * @param input Scanner.
     * @param message   La message a afficher qui differe selon le type
     *                  d'application soit menu ou mobile.
     * @param listeChoix    La liste des choix a afficher pour chacun des
     *                      differents options et utilisateurs :
     *                      1.  Menu mobile :
     *                      -----------------
     *                      1.  Professionnel.
     *                      2.  Membre.
     *                      ------------------
     *                      2.  Menu au comptoir du #GYM.
     * @param utilisateur   L'utilisateur qui va utilise le menu.
     * @throws IOException  Les exceptions pour le generateur des codes QR.
     * @throws WriterException
     * @throws NotFoundException
     */
    public void chooseMenu(Scanner input, String message, String[] listeChoix,
                           Utilisateur utilisateur)
            throws IOException, WriterException, NotFoundException {

        System.out.println(message);
        boolean fin = false;
        while (!fin) {
            String choixBienvenue = afficherOptions(input, listeChoix, 0);
            while (choixBienvenue == null) {
                choixInvalide();
                choixBienvenue = afficherOptions(input, listeChoix, 0);
            }
            if (utilisateur == null) {
                fin = menuReception(input, choixBienvenue);
            } else {
                fin = menuMob(input, choixBienvenue, utilisateur);
            }
        }
        input.close();
    }

    /**
     * @param optionSelected    Scanner.
     * @param listeChoix    Une liste des choix que la methode va les afficher
     *                      sur l'ecran.
     * @param option    Un int qui represente l'option menu ou mobile.
     * @return  Retourne l'option choisi par l'utilisateur en String.
     */
    static String afficherOptions(Scanner optionSelected, String[] listeChoix,
                                  int option) {

        for(int i=0; i < listeChoix.length; i++) {
            System.out.println(i+1 + ". " + listeChoix[i]);
        }
        return validerChoix(optionSelected, listeChoix, option);

    }


    /**
     * @param input Scanner.
     * @return  Retourne un string qui contient l'option choisi par
     *          l'utilisateur en String.
     */
    public static String afficherOptionsActualisation(Scanner input) {
        String[] choixOptions = {"Changer date de debut.",
                "Changer date de fin.", "Changer heures.",
                "Changer recurrences.", "Changer capacite.",
                "Changer professionnel.", "Changer frais de service.",
                "Changer commentaires."};
        String optionChoisiStr = MenuGYM.afficherOptions(input, choixOptions, 0);
        while (optionChoisiStr == null) {
            optionChoisiStr = MenuGYM.afficherOptions(input, choixOptions, 0);
        }
        return optionChoisiStr;
    }

    /*  -------------------------------------------------------------------  */

    /*
        *   Option 1 :
        --------------
        1.  Gerer un membre(Ajouter, mise a jour, supprimer).
     */
    /**
     * @param input Scanner.
     * @param estInscrit    Booleen qui indique si l'utilisateur est inscrit
     *                      au #GYM ou non.
     * @param estService    Booleen qui indique si c'est un service ou non.
     * @throws IOException
     * @throws WriterException
     * @throws NotFoundException
     */
    private void gerer(Scanner input, boolean estInscrit, boolean estService)
            throws IOException, WriterException, NotFoundException {

        String[] choixMenu;
        if(estInscrit) {
            choixMenu = new String[]{"Ajouter utilisateur", "Actualiser " +
                    "utilisateur", "Supprimer utilisateur"};
            boolean nouveauUtilisateur = verifierChoix(input, "Etes-vous un " +
                    "nouveau utilisateur? (oui/non)");
            if(nouveauUtilisateur) { Utilisateur.adhererMenu(input); }
            else { Utilisateur.choixGererUtilisateur(input, choixMenu); }
        } else {
            choixMenu = getSeancesGerer(estService);
            getProGerer(input, choixMenu);
        }

    }

    /**
     *
     * @param estService    Si c'est un service que l'utilisateur cherche a
     *                      gerer ou juste gerer seance.
     * @return  Retourne une liste des options que l'utilisateur va choisir
     *          entre lesquelles.
     */
    static String[] getSeancesGerer(boolean estService) {

        String[] choixMenu;
        String msg = "service";
        String msg1 = "seance";
        if(estService) {
            System.out.println("<-- Gerer un " + msg + " -->");
            choixMenu = new String[]{"Ajouter " + msg + ".", "Actualiser "
                    + msg + ".", "Supprimer " + msg + "."};
        } else {
            System.out.println("<-- Gerer un " + msg1 + " -->");
            choixMenu = new String[]{"Ajouter " + msg1 + ".", "Actualiser" +
                    " " + msg1 + ".", "Supprimer " + msg1 + "."};
        }
        return choixMenu;
    }

    /**
     *
     * @param input Scanner
     * @param choixMenu Les seances que l'utilisateur va choisir entre lesquelles.
     * @throws NotFoundException    Les exceptions de generateur des codes QR.
     * @throws IOException
     * @throws WriterException
     */
    static void getProGerer(Scanner input, String[] choixMenu)
            throws NotFoundException, IOException, WriterException {

        boolean nouveauPro = verifierChoix(input, "Etes-vous un nouveau " +
                "professionnel? (oui/non)");
        if (nouveauPro) { Utilisateur.adhererMenu(input); }
        else { Utilisateur.gererAncienPro(input, choixMenu); }

    }


    /**
     * @param input Scanner.
     * @param pro   Le professionnel que le logiciel va afficher ses seances.
     * @param msg1  Le message qui indique si le logiciel va actualiser ou
     *              supprimer.
     * @param msg2  Un autre message lie au premier option choisi par
     *              utilisateur soit actualisation ou suppression.
     *
     * @return
     */
    public static Seance afficherSeancesPro(Scanner input, Professionnel pro,
                                            String msg1, String msg2) {

        System.out.println("<--- " + msg1 + " un service --->\nPour quel " +
                "service voulez-vous faire " + msg2 + "?");
        String dayOfWeekStr = BaseDonnees.getTodayStr();
        if(pro.getSeances().size() > 0) {
            ArrayList<Seance> seances = new ArrayList<>();
            for (Seance seance : pro.getSeances()) {
                if(!seance.getEtatSeance().matches("termine")) {
                    if (BaseDonnees.comparerSemaine(seance) &&
                            seance.getJourSeance().matches(dayOfWeekStr) &&
                            BaseDonnees.comparerSemaine(seance)) {
                        seances.add(seance);
                    }
                }
            }
            if (seances.size() > 0) {
                String[] seancesArr = new String[seances.size()];
                for (int j = 0; j < seances.size(); j++) {
                    seancesArr[j] = seances.get(j).getNom() + " : " +
                            seances.get(j).getSeanceID();
                }
                String optionSeance = MenuGYM.afficherOptions(input, seancesArr, 0);
                while (optionSeance == null) {
                    optionSeance = MenuGYM.afficherOptions(input, seancesArr, 0);
                }
                if(!optionSeance.matches("")) {
                    int seancePos = Integer.parseInt(optionSeance);
                    return seances.get(seancePos - 1);
                }
            }
        } else {
            System.out.println("Desole!pas des seances disponible pour " +
                    "le moment pour le " + pro.getEtat() + " : '" +
                    pro.getNom() + "'"); }
        return null;
    }


    /*  -------------------------------------------------------------------  */

    /**
     * @param input Scanner.
     * @return  Retourne la seance choisie par l'utilisateur.
     */
    static Seance choisirSeance(Scanner input) {

        ArrayList<Seance> seancesListe = BaseDonnees.chercherSeancesToday();
        ArrayList<String> choixMenuListe = new ArrayList<String>();
        if(seancesListe != null) {
            for (Seance seance : seancesListe) {
                if (!seance.getEtatSeance().matches("termine")) {
                    choixMenuListe.add("\tNom : " + seance.getNom() +
                            ".\n\tSeanceID : " + seance.getSeanceID() +
                            ".\n\tHoraire : " + seance.getHeures() +
                            ".\n\tMontant : " + seance.getMontant() + ".");
                }
            }
            String[] choixMenu = choixMenuListe.toArray(new String[0]);
            String optionChoisiStr = afficherOptions(input, choixMenu, 1);
            while (optionChoisiStr == null ||
                    Integer.parseInt(optionChoisiStr) > choixMenu.length) {
                optionChoisiStr = afficherOptions(input, choixMenu, 1);
            }
            int optionChoisi = Integer.parseInt(optionChoisiStr);
            return seancesListe.get(optionChoisi - 1);
        } else { return null; }
    }

    /**
     * @param input Scanner.
     * @param utilisateur   Le professionnel qui cherche a confirmer la
     *                      presence d'un utilisateur.
     * @return
     */
    public static Seance confirmerPresenceMobPro(Scanner input,
                                             Utilisateur utilisateur) {
        String data = MenuGYM.buildString(true);
        String msg = data + "\nVeuillez entrer un code de seance valide? ";
        System.out.println(msg);
        String seanceStr = input.nextLine();
        boolean confirmee = BaseDonnees.verifierCodeSeance(seanceStr,
                BaseDonnees.addZerosToNum(utilisateur.getNumero()).toString());
        return BaseDonnees.verifierSeance(seanceStr, confirmee);

    }

    /*  -------------------------------------------------------------------  */

    /**
     * @param input Scanner.
     * @return  Vrai si le procedure est complete et sinon la boucle retry
     *          va continuer.
     */
    boolean proComptable(Scanner input) {
        boolean retry = true;
        while (retry) {
            Utilisateur user = BaseDonnees.validerAccesMenu(input);
            if (user != null) {
                BaseDonnees.traiterProComptable(input, user);
            } else {
                System.out.println("Desole!Pas de donnees pour afficher a " +
                        "partir de ce numero de membre!");
            }
            retry = verifierChoix(input, "Voulez-vous reessayer " +
                    "avec un autre membre? (oui/non)");
        }
        return true;
    }

    /*  -------------------------------------------------------------------  */

    /**
     * @param seance    La seance que le logiciel va extraire les donnees
     *                  de laquelle.
     * @return  Retourne la confirmation de l'inscription a la seance sous
     *          forme d'un String que le logiciel va imprimer.
     */
    public static String imprimerInscriptionSeance(Seance seance) {
        StringBuilder msg = new StringBuilder();
        for(Map.Entry<Inscription, Boolean> entry :
                seance.getInscriptions().entrySet()) {
            Inscription inscription = entry.getKey();
            Utilisateur user = inscription.getMembreInscription();
            msg.append("\n[");
            assert user != null;
            msg.append("Le(").append(user.getEtat()).append(") : \n* Nom : ")
                    .append(user.getNom()).append(".\n* Numero : ").
                    append(user.getNumero()).append(".\n* Courriel : ").
                    append(user.getCourriel()).append(".\n* Adresse : ").
                    append(user.getAdresse()).append(".\n* Telephone : ").
                    append(user.getTelephone()).append("]");
        }
        return msg.toString();
    }

    /**
     * @param seance    La seance que le logiciel va extraire les donnees
     *                  de laquelle.
     * @param numMembre Le numero de membre qu'on va imprimer sa confirmation.
     * @return  Retourne la confirmation sous forme d'un String que le logiciel
     *          va l'imprimer.
     */
    static String imprimerConfirmation(Seance seance, int numMembre) {
        return "\nConfirmation:\n------------\n" +
                "Date et heure actuelles : " +
                BaseDonnees.getDate("dd-MM-yyyy HH:MM:SS") + "\nNumero de " +
                "professionnel : " + BaseDonnees.addZerosToNum(seance.getNumPro())
                + "\nNumero du membre : " + BaseDonnees.addZerosToNum(numMembre) +
                "\nCode de seance : " +
                seance.getSeanceID() + "\n" +
                "Commentaires : " +
                seance.getCommentaires();
    }

    /*  -------------------------------------------------------------------  */

    /**
     * @param optionSelected    Scanner.
     * @param listeChoix    Une liste des choix que l'utilisateur va choisir
     *                      entre eux.
     * @param optionMenu    Un int pour indiquer si c'est menu ou mob.
     * @return  Retourne un String qui represent l'option choisi par
     *          l'utilisateur si c'est une option valide.
     */
    private static String validerChoix(Scanner optionSelected, String[] listeChoix,
                                       int optionMenu) {

        String choix = ""; /* le choix en lui meme */
        String choixStr = ""; /* le numero du choix */
        int choixNum;
        boolean valide = false;
        while(!valide) {
            choixStr = optionSelected.nextLine();
            if (choixStr.matches("^[0-9]+$")) {
                choixNum = Integer.parseInt(choixStr);
                if (choixNum > 0 && choixNum <= listeChoix.length) {
                    choix = listeChoix[choixNum - 1];
                    valide = true;
                    System.out.println("Vous avez choisi <" + choix + ">");
                } else {
                    if(optionMenu != 0) { choixInvalide(); }
                    return null;
                }
            } else { return null; }
        }
        return choixStr;

    }

    /*  Methode qui verifie si c'est un membre, employe seulement ou
    employe et professionnel.  */
    /**
     * @param inscriptionScanner    Scanner.
     * @param msg   Le message a imprimer si le choix etait invalide.
     * @return  Un booleen qui indique que le choix est valide entre
     * vrai(oui ou non) et faux(autres).
     */
    static boolean verifierChoix(Scanner inscriptionScanner, String msg) {

        System.out.println(msg);
        boolean choixBool = false;
        String choixStr = "";
        /* Tant que pas oui ou non */
        boolean choixValidation = false;
        while(!choixValidation) {
            choixStr = inscriptionScanner.nextLine();
            switch(choixStr) {
                case "oui": choixBool = true;
                case "non":
                    choixValidation = true;
                    break;
                default:
                    choixInvalide();
                    System.out.println(msg);
            }
        }
        return choixBool;

    }

    /*  -------------------------------------------------------------------  */

    /**
     *  Imprime une message que le choix est incorrect.
     */
    private static void choixInvalide() {
        System.out.println("Choix incorrect ! Veuillez choisir une bonne option : ");
    }

    /**
     * @param input Scanner.
     * @throws NotFoundException
     * @throws IOException
     * @throws WriterException
     * Imprime un message que l'acces est refuse et elle appele la methode
     * choixInscription pour verifier si l'utilisateur veut s'inscrire ou
     * pas au #GYM.
     */
    public static void membreInvalide(Scanner input)
            throws NotFoundException, IOException, WriterException {
        inscrireAuGYM(input);
    }

    /**
     * @param input Scanner.
     * @throws IOException  Les exceptions pour generer les codes QR.
     * @throws NotFoundException
     * @throws WriterException
     */
    public static void inscrireAuGYM(Scanner input)
            throws IOException, NotFoundException, WriterException {
        boolean subscribe;
        subscribe = MenuGYM.verifierChoix(input, "Voulez-vous " +
                "vous inscrire a la seance? (oui/non)");
        if(subscribe) {
            Utilisateur user = BaseDonnees.validerAccesMenu(input);
            boolean inscrit = Seance.inscrireSeance(input, user, false);
            if(inscrit) { Seance.confirmerPresence(input, user); }
        } else {
            System.out.println("Vous avez decide de ne pas vous inscrire " +
                    "a la seance!");
        }
    }

    /*  -------------------------------------------------------------------  */

    /**
     * @param estSeance Booleen qui indique le string a construire sera celui
     *                  d'une seance non des utilisateurs.
     * @return  String contenant les utilisateurs, les membres,
     *          les professionnels et les employes inscrits dans la base des
     *          donnees ou les seances si estSeance est vrai.
     */
    static String buildString(boolean estSeance) {
        if (estSeance) { return buildStringSeances(); }
        else { return buildStringUtilisateurs(); }
    }

    /**
     *
     * @return Retourne un string contenant les donnees pour les seances
     *         enregistrees dans la base de donnees.
     */
    static String buildStringSeances() {

        ArrayList<Seance> listeSeances = BaseDonnees.chercherSeancesToday();
        StringBuilder liste = new StringBuilder("Les seances pour " +
                "aujourd'hui sont : ");
        assert listeSeances != null;
        for(Seance seance : listeSeances) {
            if(!seance.getEtatSeance().matches("termine")) {
                liste.append(seance.getSeanceID()).append(", ");
            }
        }
        return liste.toString();

    }

    /**
     *
     * @return  Retourne un string contenant les donnees pour les utilisateurs
     *          inscrits.
     */
    static String buildStringUtilisateurs() {

        String msg = "ayant des numeros de membre sont : ";
        StringBuilder msgUtilisateur = new StringBuilder
                ("Les utilisateurs " + msg);
        StringBuilder msgMembre = new StringBuilder
                ("Les membres " + msg);
        StringBuilder msgPro = new StringBuilder
                ("Les pros " + msg);
        StringBuilder msgEmploye = new StringBuilder
                ("Les employes " + msg);
        StringBuilder msgGerant = new StringBuilder
                ("Le gerant ayant le numero de membre : ");
        StringBuilder message = new StringBuilder();
        for (Utilisateur utilisateur : BaseDonnees.getUtilisateurs()) {
            if(utilisateur != null) {
                switch (utilisateur.getEtat()) {
                    case "membre":
                        msgMembre.append(utilisateur.getNumero())
                                .append(", ");
                        break;
                    case "professionnel":
                        msgPro.append(utilisateur.getNumero())
                                .append(", ");
                        break;
                    case "employe":
                        msgEmploye.append(utilisateur.getNumero())
                                .append(", ");
                    default:
                        if(utilisateur.getEtat().matches("gerant")) {
                            msgGerant.append(utilisateur.getNumero());
                        }
                }
            }
        }

        message.append(msgMembre).append("\n").append(msgPro).append("\n")
                .append(msgEmploye).append("\n").append(msgGerant);
        return message.toString();
    }

    /*  -------------------------------------------------------------------  */

    /* methode appeler a chaque fin des fonctions principale pour demander si l'utilsateur veut continuer ou pas, prend un scanner */
    /* qui retourne un booleen */
    /**
     * @param continuerGestion  Scanner
     * @return  Retourne false si l'utilisateur decide de quitter et vrai
     *          sinon.
     */
    static boolean continuerGestion(Scanner continuerGestion) {
        boolean continuer = verifierChoix(continuerGestion,
                "Retournez au menu principal ? (oui/non)");
        if(!continuer) {
            System.out.println("Vous avez choisi de quitter!\nSee you later!");
            return false;
        }
        return true;
    }

    /**
     * @param input Scanner.
     * @param choixBienvenue    Le choix fait par utilisateur.
     * @param choix Le choix que l'utilisateur doit taper pour quitter.
     *              '5' pour quitter en application mobile et '8' pour quitter
     *              en menu.
     * @return  Retourne vrai si l'utilisateur decide de quitter et faux sinon.
     */
    private boolean quitOrContinue(Scanner input, String choixBienvenue,
                                   String choix) {
        boolean fin;
        BaseDonnees.deletePNGs();
        if (choixBienvenue.matches(choix)) {
            System.out.println("See you later!");
            fin = true;
        }
        else { fin = !continuerGestion(input); }
        return fin;
    }

}
