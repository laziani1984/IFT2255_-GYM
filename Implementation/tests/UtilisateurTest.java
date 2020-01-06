import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UtilisateurTest {

    /*  Attributs.  */
    Membre membre, membre1, membre2, wrongMembreNom, wrongMembreAd,
            wrongMembreVille;
    Professionnel pro, pro1, pro2, pro3, wrongProProvince,
            wrongProCode, wrongProTel;
    int numPro, numPro1, numPro2, numPro3, wrongProProvinceNum,
            wrongProCodeNum, wrongProTelNum;
    Employe employe, employeEmail;
    int numEmploye, numEmployeEmail;
    Gerant gerant;

    /*  Constructeur.   */
    public UtilisateurTest() {
        BaseDonnees bd = new BaseDonnees();
    }

    @Test
    /**
     * Une classe pour tester la creation d'un nouveau professionnel.
     */
    public void creerProTest()
            throws IOException, NotFoundException, WriterException {

        numPro = Utilisateur.creerPro("Adebayo,Bam", "9 rue main",
                "St-Laurent", "QC", "H4N1N5", "4382300303",
                "a@ab.com" );
        numPro1 = Utilisateur.creerPro("Holiday,Justin",
                "6 rue main","Fort McMurray", "AB", "J2L1N5", "4382300314",
                "d@ab.com");
        numPro2 = Utilisateur.creerPro("Young,Trae",  "8 rue main",
                "Montreal", "QC", "H4N2N8","4382300307",
                "b@ab.com");
        numPro3 = Utilisateur.creerPro("Holiday,Jrue", "7 rue main",
                "Laval", "QC", "H4J2L1","4382300308",
                "c@ab.com");
        wrongProProvinceNum = Utilisateur.creerPro("Holiday,Justin",
                "6 rue main","Fort McMurray", "AC", "J2L1N5", "4382300309",
                "d@ab.com");
        wrongProCodeNum = Utilisateur.creerPro("Young,Trae",  "8 rue main",
                "Montreal", "QC", "H4N2N81","4382300307",
                "b@ab.com");
        wrongProTelNum = Utilisateur.creerPro("Holiday,Jrue", "7 rue main",
                "Laval", "QC", "H4J2L1","43823003081",
                "c@ab.com");
        pro = BaseDonnees.getPro(numPro);
        pro1 = BaseDonnees.getPro(numPro1);
        pro2 = BaseDonnees.getPro(numPro2);
        pro3 = BaseDonnees.getPro(numPro3);
        wrongProProvince = BaseDonnees.getPro(wrongProProvinceNum);
        wrongProCode = BaseDonnees.getPro(wrongProCodeNum);
        wrongProTel = BaseDonnees.getPro(wrongProTelNum);
        assertNotNull(pro);
        assertNotNull(pro1);
        assertNotNull(pro2);
        assertNotNull(pro3);
        assertNull(wrongProProvince);
        assertNull(wrongProCode);
        assertNull(wrongProTel);
    }

    @Test
    /**
     * Une classe pour tester la creation d'un nouveau employe.
     */
    public void creerEmployeTest()
            throws NotFoundException, IOException, WriterException {

        Gerant gerant1 = Gerant.assignerGerant("Westbrook,Russel",
                "14 rue main","York", "ON", "H3M2N1",
                "4382300313", "i@ab.com", true, false);
        assertNull(gerant1);
        numEmploye = Utilisateur.creerEmploye("Westbrook,Russel", "14 rue main",
                "Laval", "QC", "H3A2M1", "4382300312",
                "j@ab.com");
        numEmployeEmail = Utilisateur.creerEmploye("Westbrook,Russel", "14 rue main",
                "Laval", "QC", "H3A2M1", "4382300316",
                "j@ab.com");
        employe = BaseDonnees.getEmploye(numEmploye);
        employeEmail = BaseDonnees.getEmploye(numEmployeEmail);
        assertNull(employeEmail);
        assertNotNull(employe);

    }

    @Test
    /**
     * Une classe pour tester la creation d'un nouveau membre.
     */
    public void creerMembreTest()
            throws IOException, NotFoundException, WriterException {
        membre = Utilisateur.creerMembre("Curry,Stephen", "10 rue main",
                "Laval", "QC", "H4J2L1","4382300310",
                "g@ab.com");
        membre1 = Utilisateur.creerMembre("James,LeBron","11 rue main",
                "Calgary", "AB", "M1L2N2","4382300314",
                "f@ab.com");
        membre2 = Utilisateur.creerMembre("Irving,Kyrie", "12 rue main",
                "Iqaluit", "NU", "J5L2N3",
                "4382300303", "e@ab.com");
        wrongMembreNom = Utilisateur.creerMembre("Curry Stephen", "10 rue main",
                "Laval", "QC", "H4J2L1","4382300310",
                "g@ab.com");
        wrongMembreAd = Utilisateur.creerMembre("James,LeBron","11 main",
                "Calgary", "AB", "M1L2N2","4382300314",
                "f@ab.com");
        wrongMembreVille = Utilisateur.creerMembre("Irving,Kyrie", "12 rue main",
                "123", "NU", "J5L2N3",
                "4382300303", "e@ab.com");
        assertNotNull(membre);
        assertNotNull(membre1);
        assertNotNull(membre2);
        assertNull(wrongMembreNom);
        assertNull(wrongMembreAd);
        assertNull(wrongMembreVille);
        BaseDonnees.deletePNGs();
        
    }

}