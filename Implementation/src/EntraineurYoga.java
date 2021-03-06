import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.IOException;

/**
 * @author Wael ABOU ALI.
 */
public class EntraineurYoga extends ProfExerPhysique {

    /*  Attribut.  */
    String type;    // Le type de specialisation du Professionnel.

    /*  Constructeur.  */
    /**
     * EntraineurYoga est un Professionnel specialise en Exercice physique
     * Et donc il est un sous-classe de ProfExerPhysique qui est a son tour
     * un sous-classe de Professionnel.
     * @param nom Le nom du membre.
     * @param adresse Son adresse.
     * @param ville La ville de l'adresse.
     * @param province La province de l'adresse.
     * @param codePostal Le code postal de l'adresse.
     * @param telephone Son numero de telephone.
     * @param courriel Son courriel.
     * @param employe vrai puisque c'est un employe.
     * @param prof vrai puisque c'est un Professionnel.
     * @throws IOException Les exceptions pour generer le code QR.
     * @throws WriterException
     * @throws NotFoundException
     */
    public EntraineurYoga(String nom, String adresse, String ville,
                          String province, String codePostal,
                          String telephone, String courriel,
                          boolean employe, boolean prof)
            throws IOException, WriterException, NotFoundException {
        super(nom, adresse, ville, province, codePostal, telephone,
                courriel, employe, prof);
        // Si le Professionnel est deja cree on va lui assigner un type.
        if(this.getNumero() != 0) { this.type = "Entraineur Yoga"; }
    }

    /*  Getter and Setter pour changer le type du Professionnel.  */
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
