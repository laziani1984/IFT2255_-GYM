import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class Professionnel extends Personnel {

    /*  Attributs.  */
    /**
     *
     */
    private String domaine, emploi;
    private Map<Integer, ArrayList<Seance>> seancesParSemaine;
    private ArrayList<Seance> seances;
    private int sommeHebdo;

    /*  Constructeur.   */

    /**
     *
     * @param nom
     * @param adresse
     * @param ville
     * @param province
     * @param codePostal
     * @param telephone
     * @param courriel
     * @param employe
     * @param prof
     * @throws IOException
     * @throws WriterException
     * @throws NotFoundException
     */
    public Professionnel(String nom, String adresse, String ville, String province,
                         String codePostal, String telephone, String courriel,
                         boolean employe, boolean prof)
            throws IOException, WriterException, NotFoundException {
        super(nom, adresse, ville, province, codePostal, telephone,
                courriel, employe, prof);
        if(this.getNumero() != 0) {
            this.domaine = domaine;
            this.emploi = emploi;
            this.seances = new ArrayList<>();
            this.sommeHebdo = 0;
            BaseDonnees.addPro(this);
            this.seancesParSemaine = new HashMap<>();
        }
    }

    /*  --------------------------------    */
    /*  |   Getters, setters & adders. |    */
    /*  --------------------------------    */

    /*  Getters.    */

    /**
     *
     * @return
     */
    public String getDomaine() { return domaine; }
    public String getEmploi() { return emploi; }
    public ArrayList<Seance> getSeances() {
        return seances;
    }

    public int getSommeHebdo() {
        return sommeHebdo;
    }

    public ArrayList<Inscription> getInscriptionsSeances(int numWeek) {
        ArrayList<Inscription> totalInscriptions = new ArrayList<>();
        ArrayList<Seance> seancesSemaine = getSeancesParSemaine(numWeek);
        for(Seance seance: seancesSemaine) {
            if(seance.getDateSeance().matches(BaseDonnees
                    .getDate("dd-MM-YYYY"))) {
                for (Map.Entry<Inscription, Boolean> entry :
                        seance.getInscriptions().entrySet()) {
                        totalInscriptions.add(entry.getKey());
                }
            }
        }
        return totalInscriptions;
    }

    public ArrayList<Seance> getSeancesParSemaine(int numWeek) {
        seancesParSemaine.putIfAbsent(numWeek, new ArrayList<>());
        for(Map.Entry<Integer, ArrayList<Seance>> entry:
                this.seancesParSemaine.entrySet()) {
            if(entry.getKey() == numWeek) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void addSeancesParSemaine(Seance seance) {
        int numWeek = seance.getSemaineSeance();
        ArrayList<Seance> seances = getSeancesParSemaine(numWeek);
        seances.add(seance);
    }

    @Override
    public String getRapportHebdo(int numWeek) {
        StringBuilder rapport = new StringBuilder(buildRapport(numWeek));
        ArrayList<Seance> seances = getSeancesParSemaine(numWeek);
        this.setSommeHebdo(0);
        int i = 1;
        if(seances.size() > 0) {
            for (Seance seanceX : seances) {
                for (Map.Entry<Inscription, Boolean> entry :
                        seanceX.getInscriptions().entrySet()) {
                    Inscription inscription = entry.getKey();
                    String index = "\n" + i + ".";
                    rapport.append(index).append(entry.getKey());
                    addSommeHebdo(inscription.getTransaction().getMontant());
                    i += 1;
                }
            }
        } else {
            rapport.append("\t- Le professionnel n'a pas des " +
                    "inscriptions pour cette semaine!");
        }
        rapport.append("\n- Le montant total a verser : ")
                .append(this.sommeHebdo).append("$\n---------------------" +
                "--------------------------------------\n");
        return rapport.toString();
    }

    /*  Setters.    */

    public void setSeancesParSemaine(Map<Integer,
            ArrayList<Seance>> seancesParSemaine) {
        this.seancesParSemaine = seancesParSemaine;
    }
    public void setDomaine(String domaine) { this.domaine = domaine; }
    public void setEmploi(String emploi) { this.emploi = emploi; }
    public void setSommeHebdo(int sommeHebdo) {
        this.sommeHebdo = sommeHebdo;
    }

    /*  Adders. */
    public void addSommeHebdo(int amount) {
        this.sommeHebdo += amount;
    }

    /*  -------------------------------------------------------------------  */

    /*
        *   Option 5 :
        --------------
        5.  Consulter des inscriptions.
     */

    /**
     *
     * @param input
     * @param pro
     * @return
     */
    static boolean consulterInscriptions(Scanner input, Professionnel pro) {

        String msg = "<-- Consulter les inscriptions d";
        String msg1 = " -->\n-----------------------------------------------" +
                "------";
        /*  Partie agent au reception.  */
        if(pro == null) {
            System.out.println(msg + "'un professionnel " + msg1);
            Utilisateur utilisateur = BaseDonnees.validerAccesMenu(input);
            if (utilisateur.getEtat().matches("professionnel")) {
                Professionnel professionnel = (Professionnel) utilisateur;
                BaseDonnees.imprimerInscriptions(professionnel);
                boolean autreChoix = MenuGYM.verifierChoix(input,
                        "Voulez-vous essayer avec un nouveau professionnel? " +
                                "(oui/non) : ");
                return !autreChoix;
            } else {
                boolean reessayer = MenuGYM.verifierChoix(input,
                        "Professionnel n'est pas trouve! " +
                                "Voulez-vous reessayer? (oui/non) : ");
                return !reessayer;
            }
        } else {
            /*  Partie mobile(application). */
            System.out.println(msg + "e \"" + pro.getNom() + "\"" + msg1);
            BaseDonnees.imprimerInscriptions(pro);
            return false;
        }
    }

}
