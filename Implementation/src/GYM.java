import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Wael ABOU ALI.
 */
public class GYM {

    /**
     * C'est la classe main du #GYM.
     * @param args
     * @throws IOException
     * @throws NotFoundException
     * @throws WriterException  Ce sont les trois exceptions pour le
     *                          generateur des codes QR.
     */
    public static void main(String[] args)
            throws IOException, NotFoundException, WriterException {
        Scanner input = new Scanner(System.in);
        // La creation d'un objet menuGYM va initialiser la base de
        // donnees du #GYM.
        MenuGYM menu = new MenuGYM();
        menu.choisirApplication(input);
    }
}
