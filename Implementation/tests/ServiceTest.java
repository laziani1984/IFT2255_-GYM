import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ServiceTest {

    /*  Attributs.  */
    Service piscine, basket, yoga, wrongYogaDate, wrongPiscineNom,
            wrongBasketHeure, wrongPiscineRec, wrongYogaCap,
            wrongBasketMoney, rightYogaCap;
    Professionnel pro, pro1, pro2, pro3;
    int numPro, numPro1, numPro2, numPro3;

    /*  Constructeur.   */
    public ServiceTest() {
        BaseDonnees bd = new BaseDonnees();
    }

    @Test
    /**
     * Une classe pour tester la creation d'un ensemble des seances de service.
     */
    public void creerSeancesTest()
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
        pro = BaseDonnees.getPro(numPro);
        pro1 = BaseDonnees.getPro(numPro1);
        pro2 = BaseDonnees.getPro(numPro2);
        pro3 = BaseDonnees.getPro(numPro3);
        piscine = Service.creerSeances("Piscine",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        basket = Service.creerSeances("Basket", "02-12-2019",
                "12-01-2020", "8:00-9:00", pro1.getNumero(), "dimanche," +
                        " mardi , jeudi, vendredi", 30, 100, "NBA juniors", true);
        yoga = Service.creerSeances("Yoga",  "20-11-2019",
                "02-02-2020", "17:00-19:00", pro2.getNumero(),
                "samedi,lundi,mercredi", 30, 20, "", true);
        wrongPiscineNom = Service.creerSeances("Piscin",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        wrongYogaDate = Service.creerSeances("Yoga",  "13-112019",
                "12-01-2020", "12:00-13:00", pro1.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        wrongBasketHeure = Service.creerSeances("Basket",  "13-11-2019",
                "12-01-2020", "13:00-12:00", pro2.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        wrongPiscineRec = Service.creerSeances("Piscine",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro3.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi vendredi", 22, 10, "", true);
        wrongYogaCap = Service.creerSeances("Yoga",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro1.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 0, 10, "", true);
        rightYogaCap = Service.creerSeances("Yoga",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 9, 10, "", true);
        wrongBasketMoney = Service.creerSeances("Basket",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro2.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 101, "", true);

        assertNotNull(piscine);
        assertNotNull(basket);
        assertNotNull(yoga);
        assertNull(wrongPiscineNom);
        assertNull(wrongYogaDate);
        assertNull(wrongBasketHeure);
        assertNull(wrongPiscineRec);
        assertNotNull(rightYogaCap);
        assertNull(wrongYogaCap);
        assertNull(wrongBasketMoney);

    }
}