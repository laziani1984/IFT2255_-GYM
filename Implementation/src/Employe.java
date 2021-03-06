import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.IOException;

/**
 * @author Wael ABOU ALI.
 */
class Employe extends Personnel {

    /*  Constructeur.   */
    /**
     * L'employe est le Personnel du #GYM qui n'est pas un Professionnel.
     * @param nom Le nom du membre.
     * @param adresse Son adresse.
     * @param ville La ville de l'adresse.
     * @param province La province de l'adresse.
     * @param codePostal Le code postal de l'adresse.
     * @param telephone Son numero de telephone.
     * @param courriel Son courriel.
     * @param employe vrai puisque c'est un employe.
     * @param prof faux puisque ce n'est pas un Professionnel.
     * @throws IOException Les exceptions pour generer le code QR.
     * @throws WriterException
     * @throws NotFoundException
     * Ajouter l'objet cree Membre à la liste de membres dans la base de
     * donnees.
     */
    public Employe(String nom, String adresse, String ville, String province,
                   String codePostal, String telephone, String courriel,
                   boolean employe, boolean prof)
            throws IOException, WriterException, NotFoundException {
        super(nom, adresse, ville, province, codePostal, telephone,
                courriel, employe, prof);
        BaseDonnees.addEmploye(this);

    }
}
