import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

/**
 * @author Wael ABOU ALI.
 */
public abstract class Utilisateur {

    /*  Attributs.  */
    private int numero;
    private String QR;
    private static int currNum = 0;
    private String nom, adresse, ville, province, codePostal,
            telephone, courriel, etat;
    private boolean employe, prof, suspendu = false;
    private Map<Integer, ArrayList<Seance>> seancesPris;
    private Map<Integer, String> rapportsHebdo;
    private static ArrayList<String> filenames =
            new ArrayList<>();

    /*  Constructeur.   */
    /**
     * L'utilisateur c'est toute personnel qui utilise les facilites du #GYM
     * (membres, professionnels, employes et gerant).
     * @param nom Le nom du membre.
     * @param adresse Son adresse.
     * @param ville La ville de l'adresse.
     * @param province La province de l'adresse.
     * @param codePostal Le code postal de l'adresse.
     * @param telephone Son numero de telephone.
     * @param courriel Son courriel.
     * @param employe vrai si c'est un employe.
     * @param prof vrai si c'est un professionnel.
     * @throws IOException Les exceptions pour generer le code QR.
     * @throws WriterException
     * @throws NotFoundException
     * Ajouter l'objet cree utilisateur à la liste de utilisateurs dans la base
     * de donnees.
     * Ajouter son courriel a la liste des courriels dans la base de donnees.
     */
    public Utilisateur(String nom, String adresse, String ville, String province,
                       String codePostal, String telephone, String courriel,
                       boolean employe, boolean prof)
            throws IOException, WriterException, NotFoundException {
        if(BaseDonnees.verifierDonneesUsers(nom, adresse, ville, province,
                codePostal, telephone, courriel)) {
            this.nom = nom;
            this.adresse = adresse;
            this.ville = ville;
            this.province = province;
            this.codePostal = codePostal;
            this.telephone = telephone;
            this.employe = employe;
            this.prof = prof;
            if(employe) {
                if(prof) { this.etat = "professionnel"; }
                else { this.etat = "employe"; }
            } else { this.etat = "membre"; }
            currNum += 1;
            this.numero = currNum;
            this.courriel = courriel;
            BaseDonnees.addCourriel(courriel);
            this.QR = generateQRCode(this.numero);
            this.seancesPris = new HashMap<>();
            this.rapportsHebdo = new HashMap<>();
            BaseDonnees.addUtilisateur(this);
            BaseDonnees.getQRcodes().add(this.QR);
        } else {
            System.out.println("Impossible de creer le nouveau utilisateur " +
                    "puisque les donnees ne sont pas au bon format!");
            this.numero = 0;
        }
    }

    /*  Getters.    */

    /**
     *
     * @return  Retourne le numero de membre d'utilisateur.
     */
    public int getNumero() { return this.numero; }

    /**
     *
     * @return  Retourne le nom d'utilisateur.
     */
    public String getNom() { return this.nom; }

    /**
     *
     * @return  Retourne l'adresse d'utilisateur.
     */
    public String getAdresse() { return this.adresse; }

    /**
     *
     * @return  Retourne le numero de telephone d'utilisateur.
     */
    public String getTelephone() { return this.telephone; }

    /**
     *
     * @return  Retourne le courriel d'utilisateur.
     */
    public String getCourriel() { return this.courriel; }

    /**
     *
     * @return Retourne un booleen qui indique si l'utilisateur est un
     *         employe ou pas.
     */
    public boolean estEmploye() { return this.employe; }

    /**
     *
     * @return Retourne un booleen qui indique si l'utilisateur est un
     *         professionnel ou pas.
     */
    public boolean estProf() { return this.prof; }

    /**
     *
     * @return  Retourne un booleen qui indique si l'utilisateur est suspendu
     *          ou pas.
     */
    public boolean estSuspendu() { return suspendu; }

    /**
     *
     * @return  Retourne le code QR d'utilisateur.
     */
    public String getQR() { return QR; }

    /**
     *
     * @return  Retourne l'etat d'utilisateur.
     */
    public String getEtat() { return etat; }

    /**
     *
     * @return  Retourne le nom de la ville d'utilisateur.
     */
    public String getVille() { return ville; }

    /**
     *
     * @return  Retourne le code de la province d'utilisateur.
     */
    public String getProvince() { return province; }

    /**
     *
     * @return  Retourne le code postal d'utilisateur.
     */
    public String getCodePostal() { return codePostal; }

    /**
     *
     * @return  Retourne tous les rapports hebdomadaire sauvegardes au
     *          compte d'utilisateur.
     */
    public Map<Integer, String> getRapportsHebdo() {
        return rapportsHebdo;
    }

    /**
     *
     * @return  Retourne les seances que l'utilisateur a pris.
     */
    public ArrayList<Seance> getSeancesPris() {
        for(Map.Entry<Integer, ArrayList<Seance>> entry:
                this.seancesPris.entrySet()) {
            return entry.getValue();
        }
        return null;
    }

    /**
     *
     * @return  Une liste des fichiers que le systeme a creer pour produire
     *          les PNGs afin de les lire pour produire les matrices des
     *          codes QR.
     */
    public static ArrayList<String> getFilenames() {
        return filenames;
    }

    /**
     *
     * @return  Retourne les seances que l'utilisateur a pris pendant la
     *          semaine de numWeek
     */
    public ArrayList<Seance> getSeancesSemaine(int numWeek) {
        seancesPris.putIfAbsent(numWeek, new ArrayList<>());
        for(Map.Entry<Integer, ArrayList<Seance>> entry:
                this.seancesPris.entrySet()) {
            if(entry.getKey() == numWeek) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     *
     * @return  Methode original qui sera redefinit plus tard par les
     *          differents sous-classes.
     */
    public String getRapportHebdo(int numWeek) { return null; }

    /*  Setters.    */
    /**
     *
     * @return  Changer le nom par un nouveau.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     *
     * @return  Changer l'adresse par un nouveau.
     */
    public void setAdresse(String adresse) { this.adresse = adresse; }

    /**
     *
     * @return  Changer le numero de telephone par un nouveau.
     */
    public void setTelephone(String telephone) { this.telephone = telephone; }

    /**
     *
     * @return  Changer l'etat d'utilisateur en employe.
     */
    public void setEmploye(boolean employe) { this.employe = employe; }

    /**
     *
     * @return  Changer l'etat de suspension par un nouveau soit vrai pour
     *          une suspension ou faux pour reactivation.
     */
    public void setSuspendu(boolean suspendu) { this.suspendu = suspendu; }

    /**
     *
     * @return  Changer l'etat d'utilisateur en professionnel.
     */
    public void setProf(boolean prof) { this.prof = prof; }

    /**
     *
     * @return  Changer le courriel par un nouveau.
     */
    public void setCourriel(String courriel) { this.courriel = courriel; }

    /**
     *
     * @return  Changer l'etat d'utilisateur par un nouveau(membre,
     *          professionnel, meploye ou termine).
     */
    public void setEtat(String newEtat) { this.etat = newEtat; }

    /**
     *
     * @return  Changer le code postal par un nouveau.
     */
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    /**
     *
     * @return  Changer le code de la province par un nouveau.
     */
    public void setProvince(String province) { this.province = province; }

    /**
     *
     * @return
     */
    public void setVille(String ville) { this.ville = ville; }

    /*  Adders. */
    /**
     *
     * @param seance    La seance a ajouter au compte d'utilisateur.
     */
    public void addSeancesPris(Seance seance) {
        // numWeek est le numero de la semaine.
        int numWeek = seance.getSemaineSeance();
        // seances est une liste des seances de la semaine du numWeek pour
        // l'utilisateur.
        ArrayList<Seance> seances = this.getSeancesSemaine(numWeek);
        // Ajoute la seance a la liste des seances qu'il a pris pendant
        // la semaine numWeek.
        seances.add(seance);
    }

    /**
     *
     * @param numWeek   C'est un int qui represente le numero de la semaine
     *                  destinee
     * @return  Retourne un booleen qui indique que le rapport de la semaine
     *          du numWeek est ajoute au dictionnaire contenant les rapports
     *          hebdomadaire de l'utilisateur.
     */
    public boolean addRapportHebdo(int numWeek) {
        String msg = buildRapport(numWeek);
        for(Map.Entry<Integer, String> entry:
                this.getRapportsHebdo().entrySet()) {
            if(entry.getKey() == numWeek) {
                entry.setValue(msg);
                return true;
            }
        }
        return false;
    }


    /*  -----------------------------------------------------------------   */

    /*
        *   Option 2 :
        --------------
        6.  Gerer utilisateur(adherer, actualiser et supprimer).
     */

    /*
        1.  Adherer au #GYM.
     */

    /**
     *
     * @param input
     * @return
     * @throws IOException
     * @throws WriterException
     * @throws NotFoundException
     */
    static int adhererMenu(Scanner input)
            throws IOException, WriterException, NotFoundException {

        System.out.println("<-- Inscription au centre sportif -->");
        System.out.println("Veuillez inscrire toutes les informations " +
                "necessaires.");
        String nom = BaseDonnees.verifierData(input, "nom(25 caracteres) :",
                "", 1, null, "", false);
        String adresse = BaseDonnees.verifierData(input, "adresse(# rue *) :", "",
                2, null, "", false);
        String ville = BaseDonnees.verifierData(input, "Ville(25 caracteres) :",
                "", 3, null, "", false);
        String province = BaseDonnees.verifierData(input, "Province(xx Canada) :",
                "", 4, null, "", false);
        String code = BaseDonnees.verifierData(input, "Code postal Canadien(xxx xxx) :",
                "", 5, null, "", false);
        String telephone = BaseDonnees.verifierData(input, "telephone(10 chiffres) :", "",
                6, null, "", false);
        String courriel = BaseDonnees.verifierData(input, "Courriel Facebook(*@*.*) :",
                "", 7, null, "", false);
        boolean employe = MenuGYM.verifierChoix(input, "Êtes-vous un employe? (oui/non) :");
        boolean pro = false;
        if(employe) { pro = MenuGYM.verifierChoix(input, "Êtes-vous un professionnel? (oui/non) :"); }
        boolean confirmer = MenuGYM.verifierChoix(input,
                "Procedez a l'inscription (oui/non) : ");
        if(confirmer) {
            if (pro || employe) {
                if(pro) { return creerPro(nom, adresse, ville, province,
                        code, telephone, courriel); }
                else { return creerEmploye(nom, adresse, ville, province,
                        code, telephone, courriel); }
            } else {
                Membre membre = creerMembre(nom, adresse, ville, province,
                    code, telephone, courriel);
                System.out.println("Les frais d'inscription sont : " +
                        Membre.getFraisMensuelles() + "$");
                return payerFraisMembre(input, membre);
            }
        } else {
            System.out.println("Inscription annulee!");
            return 0;
        }
    }

    /**
     *
     * @param nom   Nom du nouveau professionnel.
     * @param adresse Son adresse.
     * @param ville Le nom de sa ville.
     * @param province  Le nom de sa province.
     * @param code  Son code postal.
     * @param telephone Son numero de telephone.
     * @param courriel  Son adresse courriel.
     * @return  Retourne son numero de membre s'il y en a. Sinon, il retourne 0.
     * @throws NotFoundException    Les exceptions pour generer le code QR.
     * @throws IOException
     * @throws WriterException
     */
    public static int creerPro(String nom, String adresse, String ville,
                               String province, String code, String telephone,
                               String courriel)
            throws NotFoundException, IOException, WriterException {

        if(BaseDonnees.verifierDonneesUsers(nom,
                adresse, ville, province, code, telephone,
                courriel)) {
            Professionnel professionnel = new Professionnel(nom,
                    adresse, ville, province, code, telephone,
                    courriel, true, true);
            System.out.println("Numero du professionel : " +
                    professionnel.getNumero());
            System.out.println(professionnel.getQR());
            return professionnel.getNumero();
        } else {
            System.out.println("Desole!impossible de creer le nouveau" +
                    " professionnel!");
            return 0;
        }

    }

    /**
     *
     * @param nom   Nom du nouveau employe.
     * @param adresse Son adresse.
     * @param ville Le nom de sa ville.
     * @param province  Le nom de sa province.
     * @param code  Son code postal.
     * @param telephone Son numero de telephone.
     * @param courriel  Son adresse courriel.
     * @return  Retourne son numero de membre s'il y en a. Sinon, il retourne 0.
     * @throws NotFoundException    Les exceptions pour generer le code QR.
     * @throws IOException
     * @throws WriterException
     */
    public static int creerEmploye(String nom, String adresse, String ville,
                                   String province, String code, String telephone,
                                   String courriel)
            throws NotFoundException, IOException, WriterException {

        if(BaseDonnees.verifierDonneesUsers(nom,
                adresse, ville, province, code, telephone,
                courriel)) {
            Employe employe = new Employe(nom, adresse, ville,
                    province, code, telephone, courriel, true, false);
            System.out.println("Numero d'employe : " +
                    employe.getNumero());
            System.out.println(employe.getQR());
            return employe.getNumero();
        } else {
            System.out.println("Desole!impossible de creer le nouveau" +
                    " professionnel!");
            return 0;
        }

    }

    /**
     *
     * @param nom   Nom du nouveau membre.
     * @param adresse Son adresse.
     * @param ville Le nom de sa ville.
     * @param province  Le nom de sa province.
     * @param code  Son code postal.
     * @param telephone Son numero de telephone.
     * @param courriel  Son adresse courriel.
     * @return  Retourne son numero de membre s'il y en a. Sinon, il retourne 0.
     * @throws NotFoundException    Les exceptions pour generer le code QR.
     * @throws IOException
     * @throws WriterException
     */
    public static Membre creerMembre(String nom, String adresse,
                                  String ville, String province, String code,
                                  String telephone, String courriel)
            throws NotFoundException, IOException, WriterException {

        if(BaseDonnees.verifierDonneesUsers(nom,
                adresse, ville, province, code, telephone,
                courriel)) {
            return new Membre(nom, adresse, ville, province,
                    code, telephone, courriel, false, false);
        } else {
            System.out.println("Desole!impossible de creer le nouveau" +
                    " professionnel!");
            return null;
        }

    }

    static int payerFraisMembre(Scanner input, Membre membre) {

        boolean proceder = MenuGYM.verifierChoix(input,
                "Voulez vous proceder avec le paiment des frais? (oui/non) ");
        if (proceder) {
            boolean fraisPaye = Transaction.payerFrais(input,
                    Membre.getFraisMensuelles());
            if (fraisPaye) {
                System.out.println("Numero du membre : " +
                        membre.getNumero());
                System.out.println(membre.getQR());
                BaseDonnees.addMembre(membre);
                return membre.getNumero();
            }
        }
        membre.setSuspendu(true);
        System.out.println("Les frais d'inscription ne sont pas" +
                " payes!\nMembre suspendu!");
        return 0;

    }

    /**
     *
     * @param input Scanner.
     * @param choixMenu La liste des choix que l'utilisateur peut choisir
     *                  entre lesquelles.
     * @throws NotFoundException
     * @throws IOException
     * @throws WriterException
     */
    static void choixGererUtilisateur(Scanner input, String[] choixMenu)
            throws NotFoundException, IOException, WriterException {

        Utilisateur utilisateur = BaseDonnees.validerAccesMenu(input);
        if (utilisateur != null) {
            String choixGerer = MenuGYM.afficherOptions(input, choixMenu, 1);
            while (choixGerer == null) {
                choixGerer = MenuGYM.afficherOptions(input, choixMenu, 1);
            }
            switch (choixGerer) {
                case "1":
                    Utilisateur.adhererMenu(input);
                    break;
                case "2":
                    utilisateur.actualiser(input);
                    break;
                default:
                    utilisateur.supprimer(input);
                    break;
            }
        } else {
            boolean inscrire = MenuGYM.verifierChoix(input, "Membre n'est pas trouve! " +
                    "Voulez-vous s'inscrire? (oui/non) :");
            if (inscrire) { Utilisateur.adhererMenu(input); }
            else {
                System.out.println("Vous avez choisi de ne pas inscrire au #GYM!");
            }
        }

    }

    static void gererAncienPro(Scanner input, String[] choixMenu)
            throws NotFoundException, IOException, WriterException {

        int numUser = Utilisateur.validerUtilisateur(input, false);
        Professionnel pro = BaseDonnees.getPro(numUser);
        if (pro != null) {
            boolean done = false;
            while (!done) {
                System.out.println("<-- Gerer seance -->");
                String choixGerer = MenuGYM.afficherOptions(input, choixMenu, 1);
                while (choixGerer == null) {
                    choixGerer = MenuGYM.afficherOptions(input, choixMenu, 1);
                }
                switch (choixGerer) {
                    case "1":
                        done = Service.creerSeancesMenu(input, pro.getNumero());
                        break;
                    case "2":
                        done = Service.actualiserSeances(input, pro);
                        break;
                    default:
                        done = Service.supprimerSeances(input, pro);
                        break;
                }
            }
        } else {
            boolean inscrire = MenuGYM.verifierChoix(input, "Professionnel " +
                    "n'est pas trouve! Voulez-vous s'inscrire? (oui/non) :");
            if(inscrire) { Utilisateur.adhererMenu(input);}
        }

    }

    /*  -----------------------------------------------------------------   */

    /*
        2.  Actualiser utilisateur soit en reactivant son suspension ou le
            faire suspendre au #GYM.
     */

    /**
     *  La methode initiale pour actualiser l'etat courant d'un utilisateur
     *  soit en le suspendant ou en reactivant son compte.
     * @param input Scanner.
     */
    void actualiser(Scanner input) {

        String msg = "Le " + this.getEtat() + " avec le numero '" +
                this.getNumero() + "' est maintenant : ";
        if(this.estSuspendu()) {
            if (this.getEtat().matches("membre")) {
                Membre membre = (Membre) this;
                int montant = membre.getMontantdu();
                if (montant > 0) {
                    gererSuspension(input, montant, membre, this.estSuspendu(),
                            msg + "actif!", "Les frais ne sont pas payes" +
                                    " et le compte est toujours suspendu!");
                } else {
                    gererSuspension(input, montant, membre,
                            this.estSuspendu(), msg + "actif!",
                            "Le processus de reactivation est annule!");
                }
            } else {
                gererSuspension(input, 0, this, this.estSuspendu(), msg +
                        "actif!", "La suspension est annulee!");
            }
        } else {
            gererSuspension(input, 0, this, this.estSuspendu(), msg +
                    "suspendu!", "La suspension est annulee!");
        }
    }

    /**
     *
     * @param input Scanner.
     * @param montant   Montant a payer pour reactiver le compte suspendu.
     * @param utilisateur   L'utilisateur qui cherche a activer son compte.
     * @param suspension    Le cas de suspension si l'utilisateur est suspendu
     *                     ou pas.
     * @param message1  Les messages a afficher dans les situations differentes.
     * @param message2  Soit en activant une inscription suspendu ou en
     *                  suspendant une inscription valide.
     * Cette methode est utilise pour suspendre ou activer un utilisateur.
     */
    private void gererSuspension(Scanner input, int montant, Utilisateur utilisateur,
                                 boolean suspension, String message1, String message2) {

        boolean fraisPaye = true;
        if(montant != 0) {
            System.out.println("Le montant dû sur le membre est : "
                    + montant + "$.");
            boolean changerEtat = MenuGYM.verifierChoix(input, "Voulez-vous " +
                    "payer le montant dû sur le compte? (oui/non) ");
            if (changerEtat) {
                fraisPaye = Transaction.payerFrais(input, montant);
            } else { fraisPaye = false; }
        }
        else {
            if(!suspension) {
                System.out.println("Le " + utilisateur.getEtat() + " : " +
                        utilisateur.getNumero() + " est deja actif!");
                boolean suspendre = MenuGYM.verifierChoix(input, "Voulez-vous suspendre" +
                        " le " + utilisateur.getEtat() + " avec le numero '"
                        + utilisateur.getNumero() + "' maintenant? (oui/non)");
                if (!suspendre) { fraisPaye = false; }
            } else {
                boolean activer = MenuGYM.verifierChoix(input, "Voulez-vous " +
                        "actualiser l'etat du membre en actif? (oui/non) ");
                if(!activer) { fraisPaye = false; }
            }
        }
        if (fraisPaye) {
            utilisateur.setSuspendu(!suspension);
            System.out.println(message1);
        } else { System.out.println(message2); }
    }

    /*  -----------------------------------------------------------------   */

    /*
        3.  Supprimer utilisateur.
     */

    /**
     * Processus inital pour supprimer un utilisateur de la base de donnees.
     * @param input Scanner.
     */
    void supprimer(Scanner input) {

        boolean supprimerUtilisateur = MenuGYM.verifierChoix(input,
                "Êtes-vous sûr de supprimer votre inscription? " +
                        "(oui/non) : ");
        if(supprimerUtilisateur) { this.chercherPourSupprimer(); }
        else {
            System.out.println("La suppression de votre inscription est " +
                    "annulee.\nVous êtes toujours dans notre base de " +
                    "donnees!");
        }

    }

    /**
     *  C'est un processus qui sert a chercher la presence d'un utilisateur
     *  dans la base de donnees.
     */
    private void chercherPourSupprimer() {

        boolean found = false;
        for (Utilisateur utilisateur : BaseDonnees.getUtilisateurs()) {
            if (utilisateur.getNumero() == this.getNumero()) {
                found = true;
                this.deleteUser();
                break;
            }
        }
        if(!found) {
            System.out.println("Desole!l'utilisateur n'est pas inscrit dans " +
                    "la base de donnees!");
        }
    }

    /**
     *  Retirer l'utilisateur de la base de donnees selon son type soit un
     *  Membre, Professionnel ou Employe mais pas termine.
     */
    private void deleteUser() {

        System.out.println(this.getEtat() + " supprime!");
        if(this.getEtat().matches("membre")) {
            BaseDonnees.getMembres().remove(this);
        } else {
            if(this.getEtat().matches("professionnel")) {
                BaseDonnees.getProfessionnels().remove(this);
            } else { BaseDonnees.getEmployes().remove(this); }
        }
        Objects.requireNonNull(BaseDonnees.getUser(this.numero)).setEtat("termine");
        System.out.println(MenuGYM.buildString(false));

    }

    /*  -------------------------------------------------------------------  */

    /*
        *   Option 6 :
        --------------
        6.  Valider l'acces au centre.
     */
    /**
     *
     * @param input Scanner.
     * @param estValidation Si c'est un processus de validation d'acces pour
     *                      acceder au #GYM ou non.
     * @return  Si c'est un utilisateur valide, le systeme va retourner le
     *          numero d'utilisateur. Sinon, il retourne 0.
     * @throws IOException  Les exceptions pour les codes QR.
     * @throws WriterException
     * @throws NotFoundException
     */
    static int validerUtilisateur(Scanner input, Boolean estValidation)
            throws IOException, WriterException, NotFoundException {

        Utilisateur utilisateur = BaseDonnees.validerAccesMenu(input);
        if (utilisateur != null && !utilisateur.getEtat().matches("termine")) {
            if (utilisateur.estSuspendu()) {
                System.out.println(utilisateur.getEtat() + " suspendu!");
                boolean changerEtat = MenuGYM.verifierChoix(input,
                        "Voulez-vous reactiver votre compte? " +
                                "(oui/non) :");
                if (changerEtat) {
                    if (utilisateur.getEtat().matches("membre")) {
                        utilisateur.actualiser(input);
                    } else { utilisateur.setSuspendu(false); }
                } else { return 0; }
            } else {
                if (estValidation) {
                    System.out.println(utilisateur.getEtat() + " valide!\n" +
                            "Tourniquet ouvert!");
                }
            }
            return utilisateur.getNumero();
        } else {
            System.out.println("Utilisateur invalide!");
            boolean inscrire = MenuGYM.verifierChoix(input,
                    "Voulez-vous s'inscrire au #GYM? (oui/non) :");
            if (inscrire) { return adhererMenu(input); }
            else { return 0; }
        }
    }

    /*  ------------------------------------------------------------------   */

    /**
     *
     * @param numWeek   Le numero de la semaine qu'on veut creer le rapport
     *                  pour la quelle.
     * @return  Retourne un String contenat toutes les informations liees au
     *          Utilisateur pendant la semaine de numWeek.
     */
    public String buildRapport(int numWeek) {
        StringBuilder msgBld = new StringBuilder("\nLe rapport de la " +
                "semaine(" + numWeek + ") pour le " + this.getEtat() +  " : '" +
                this.getNom() + "'\n----------------------------------------" +
                "---------------------\n");
        msgBld.append("- Nom du membre : ").append(this.getNom())
                .append("\n- Numero du membre : ").append(this.getNumero())
                .append("\n- Adresse du membre : ").append(this.getAdresse())
                .append("\n- Ville du membre : ").append(this.getVille())
                .append("\n- Province du membre : ").append(this.getProvince())
                .append("\n- Code postal du membre : ").append(this.getCodePostal())
                .append("\n");
        if(!this.getEtat().matches("gerant")) {
            msgBld.append("- Pour chaque service fourni, les details " +
                    "suivants sont requis : \n");
        }
        return msgBld.toString();
    }

    /**
     *
     * @return  Une redefinition de la methode toString pour imprimer ces
     *          donnees directement quand on essaye d'imprimer un objet
     *          Utilisateur.
     */
    @Override
    public String toString() {
        return "Personnel :" +
                "\n* nom : " + nom + "\n* numero : " + BaseDonnees
                .addZerosToNum(numero) + "\n* adresse : " + adresse +
                "\n* telephone : " + telephone + "\n* employe : " +
                employe + "\n* professionnel : " + prof +
                "\n* suspendu :" + suspendu + "\n";
    }

    /**
     *
     * @return  Retourne un String qui represente la confirmation
     *          necessaire pour acceder soit a une seance de service
     *          ou au #GYM.
     */
    public String getConfirmation() {
        return "* nom : " + nom + "\n* numero : " + BaseDonnees
                .addZerosToNum(numero) + "\n* Code QR : " + this.getQR();
    }

    /*  -----------------------------------------------------------------   */

    /**
     *
     * @param memNum    C'est le numero de membre qu'on cherche a creer un QR
     *                  code pour lequel.
     * @return  Retourne une matrice sous forme d'un string contenant le code
     *          QR requis.
     * @throws WriterException  Les exceptions requises pour traiter et creer
     *                          des fichiers a partir du code JAVA.
     * @throws IOException
     * @throws NotFoundException
     */
    public static String generateQRCode(int memNum)
            throws WriterException, IOException, NotFoundException {
        String qrCodeData = "QR code generator!";
        String filePath = "membre" + memNum + ".png";
        filenames.add(filePath);
        String charset = "UTF-8";
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        createQRCode(qrCodeData, filePath, charset, hintMap, 20, 20);
        return readQRCode(filePath, charset, hintMap);
    }

    public static void createQRCode(String qrCodeData, String filePath,
                                    String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
                .lastIndexOf('.') + 1), new File(filePath));
    }

    /**
     *
     * @param filePath
     * @param charset
     * @param hintMap
     * @return  Cette methode va lire un fichier PNG qui reprsente l'image
     *          de barcode forme par la methode createQRCode en matrice des
     *          String qui sera retournee a son part a generateQRCode pour
     *          l'enregister dans la base de donnees.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NotFoundException
     */
    public static String readQRCode(String filePath, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        InputStream stream = new FileInputStream(filePath);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(stream))));
        String imageStr = binaryBitmap.toString();
        stream.close();
        return imageStr;
    }

}
