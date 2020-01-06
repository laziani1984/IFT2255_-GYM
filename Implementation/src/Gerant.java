import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wael ABOU ALI.
 */
public class Gerant extends Employe {

    /*  Attributs :
    * - rapports :  un dictionnaire qui contiendra tous les rapports du #GYM
    *               que le gerant va recevoir soit a chaque vendredi minuit
    *               ou a sa demande.
    * - gerant :    Puisqu'on ne peut pas avoir plus qu'un gerant en même
    *               temps. Alors un objet static de type gerant va servir
    *               comme un containeur de l'objet gerant cree.
    * - hasGerant : Un booleen qui sert a identifier si le #GYM a deja un
    *               gerant ou pas. Sinon, un objet Gerant sera cree.
    * */
    private static Map<Integer, ArrayList<String>> rapports;
    private static Gerant gerant;
    private static boolean hasGerant = false;

    /*  Constructeur.  */
    /**
     * Gerant est un sous-classe d'Employe puisque le gerant est un employe
     * avec un acces special. La presence du booleen hasGerant sert a s'assurer
     * qu'une seule instance du Gerant sera creee. (Singleton)
     * @param nom le nom du gerant(String).
     * @param adresse l'adresse du gerant(String).
     * @param ville Le nom de la ville(String).
     * @param province Le nom de la province(String).
     * @param codePostal Le code postal de l'adresse du gerant(String).
     * @param telephone Le numero de telephone du gerant(String).
     * @param courriel L'adresse courriel du gerant(String).
     * @param employe Un booleen pour identifier si c'est un enploye ou pas.
     * @param prof Un booleen pour identifier si c'est un professionnel ou pas.
     * @throws IOException
     * @throws WriterException
     * @throws NotFoundException    Ce sont les trois exceptions pour le
     *                              generateur des codes QR.
     */
    private Gerant(String nom, String adresse, String ville, String province,
                   String codePostal, String telephone, String courriel,
                   boolean employe, boolean prof)
            throws IOException, WriterException, NotFoundException {
        super(nom, adresse, ville, province, codePostal, telephone,
                courriel, employe, prof);
        if(this.getNumero() != 0) {
            gerant = this;
            this.setEtat("gerant");
            rapports = new HashMap<>();
        }
    }

    /**
     * @return  Puisque Gerant est une classe(Singleton) donc il suffit
     *          d'appeler getGerant sans savoir son numero pour recevoir
     *          le gerant du #GYM.
     */
    public static Gerant getGerant() {
        return gerant;
    }

    /**
     * @return  getRapports va retourner le dictionnaire contenant tous
     *          les rapports enregistres au nom du gerant X.
     */
    public static Map<Integer, ArrayList<String>> getRapports() {
        return rapports;
    }

    /**
     * Cette fonction sert a verifier que le #GYM n'a pas un gerant
     * assigne pour le moment. Si hasGerant est vrai, le constructeur
     * de type Gerant sera appele pour creer le nouveau gerant.
     * Si ce n'est pas le cas un objet de type Employe sera cree.
     * @param nom
     * @param adresse
     * @param ville
     * @param province
     * @param codePostal
     * @param telephone
     * @param courriel
     * @param employe
     * @param prof
     * @return
     * @throws IOException
     * @throws WriterException
     * @throws NotFoundException
     */
    public static Gerant assignerGerant(String nom, String adresse,
                                        String ville, String province,
                                        String codePostal, String telephone,
                                        String courriel, boolean employe,
                                        boolean prof)
            throws IOException, WriterException, NotFoundException {
        if(!hasGerant) {
            hasGerant = true;
            return new Gerant(nom, adresse, ville, province, codePostal,
                    telephone, courriel, employe, prof);
        } else { return null; }
    }

    /**
     *  La methode getRapportHebdo est une methode redefinie heritee de la
     *  classe parent Utilisateur. Cette methode va retourner un rapport de
     *  la semaine courante pour tous les professionnels du #GYM et le
     *  montant total a verser pour eux.
     */
    @Override
    public String getRapportHebdo(int numWeek) {
        StringBuilder rapport = new StringBuilder(buildRapport(numWeek));
        int totalSommeGYM = 0;
        for(Professionnel professionnel : BaseDonnees.getProfessionnels()) {
            String str = professionnel.getRapportHebdo(numWeek);
            rapport.append(str);
            totalSommeGYM += professionnel.getSommeHebdo();
        }
        rapport.append("- Le montant total a verser au professionnels du #GYM : ")
                .append(totalSommeGYM).append("$\n------------------------" +
                "------------------\n");
        return rapport.toString();
    }
}
