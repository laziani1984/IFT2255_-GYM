import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import java.io.IOException;

/**
 * @author Wael ABOU ALI.
 */
public abstract class Personnel extends Utilisateur {

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
     * @param employe vrai si c'est un employe.
     * @param pro vrai si c'est un Professionnel.
     * @throws IOException Les exceptions pour generer le code QR.
     * @throws WriterException
     * @throws NotFoundException
     */
    public Personnel(String nom, String adresse, String ville, String province,
                     String codePostal, String telephone, String courriel,
                     boolean employe, boolean pro)
            throws IOException, WriterException, NotFoundException {
        super(nom, adresse, ville, province, codePostal, telephone,
                courriel, employe, pro);
    }

}