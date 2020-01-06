import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseDonneesTest {

    /*  Attributs.  */
    Professionnel pro, pro1, pro2, pro3;
    Transaction transaction, transaction1, transaction2;
    Membre membre, membre1, membre2, wrongMembreNom, wrongMembreAd,
            wrongMembreVille;
    int numPro, numPro1, numPro2, numPro3, wrongProProvinceNum,
            wrongProCodeNum, wrongProTelNum;
    Employe employe, employeEmail;
    int numEmploye, numEmployeEmail;
    Gerant gerant;
    Service piscine, basket, yoga, wrongYogaDate, wrongPiscineNom,
            wrongBasketHeure, wrongPiscineRec, wrongYogaCap,
            wrongBasketMoney, rightYogaCap;

    /*  Constructeur.   */
    public BaseDonneesTest() {
        BaseDonnees bd = new BaseDonnees();
    }

    @Test
    /**
     * Une classe pour tester les inscriptions aux seances.
     */
    public void inscrireSeanceTest()
            throws IOException, NotFoundException, WriterException {

        numPro = Utilisateur.creerPro("Adebayo,Bam", "9 rue main",
                "St-Laurent", "QC", "H4N1N5", "4382300301",
                "a@ab.com" );
        numPro1 = Utilisateur.creerPro("Holiday,Justin",
                "6 rue main","Fort McMurray", "AB", "J2L1N5", "4382300302",
                "d@ab.com");
        numPro2 = Utilisateur.creerPro("Young,Trae",  "8 rue main",
                "Montreal", "QC", "H4N2N8","4382300303",
                "b@ab.com");
        numPro3 = Utilisateur.creerPro("Holiday,Jrue", "7 rue main",
                "Laval", "QC", "H4J2L1","4382300304",
                "c@ab.com");
        wrongProProvinceNum = Utilisateur.creerPro("Holiday,Justin",
                "6 rue main","Fort McMurray", "AC", "J2L1N5", "4382300305",
                "d@ab.com");
        wrongProCodeNum = Utilisateur.creerPro("Young,Trae",  "8 rue main",
                "Montreal", "QC", "H4N2N81","4382300306",
                "b@ab.com");
        wrongProTelNum = Utilisateur.creerPro("Holiday,Jrue", "7 rue main",
                "Laval", "QC", "H4J2L1","43823003084",
                "c@ab.com");
        membre = Utilisateur.creerMembre("Curry,Stephen", "10 rue main",
                "Laval", "QC", "H4J2L1","4382300311",
                "g@ab.com");
        membre1 = Utilisateur.creerMembre("James,LeBron","11 rue main",
                "Calgary", "AB", "M1L2N2","4382300312",
                "f@ab.com");
        membre2 = Utilisateur.creerMembre("Irving,Kyrie", "12 rue main",
                "Iqaluit", "NU", "J5L2N3",
                "4382300313", "e@ab.com");
        wrongMembreNom = Utilisateur.creerMembre("Curry Stephen", "10 rue main",
                "Laval", "QC", "H4J2L1","4382300314",
                "g@ab.com");
        wrongMembreAd = Utilisateur.creerMembre("James,LeBron","11 main",
                "Calgary", "AB", "M1L2N2","4382300315",
                "f@ab.com");
        wrongMembreVille = Utilisateur.creerMembre("Irving,Kyrie", "12 rue main",
                "123", "NU", "J5L2N3",
                "4382300316", "e@ab.com");
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
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        wrongBasketHeure = Service.creerSeances("Basket",  "13-11-2019",
                "12-01-2020", "13:00-12:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        wrongPiscineRec = Service.creerSeances("Piscine",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi vendredi", 22, 10, "", true);
        wrongYogaCap = Service.creerSeances("Yoga",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 0, 10, "", true);
        rightYogaCap = Service.creerSeances("Yoga",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 9, 10, "", true);
        wrongBasketMoney = Service.creerSeances("Basket",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 101, "", true);
        transaction = new Transaction(piscine.getMontant(), "cash");
        transaction1 = new Transaction(yoga.getMontant(), "cash");
        transaction2 = new Transaction(basket.getMontant(), "cash");
        assertTrue(BaseDonnees.inscrireSeance(membre, piscine, transaction));
        System.out.println("membre inscrit!");
        assertTrue(BaseDonnees.inscrireSeance(membre1, basket, transaction2));
        System.out.println("membre1 inscrit!");
        assertTrue(BaseDonnees.inscrireSeance(membre2, yoga, transaction1));
        System.out.println("membre2 inscrit!");
        assertFalse(BaseDonnees.inscrireSeance(membre2, wrongYogaCap, transaction1));

    }

    @Test
    /**
     * Une classe pour tester la consultation des rapports pour les
     * professionnels du #GYM.
     */
    public void consulterInscriptionsTest()
            throws IOException, NotFoundException, WriterException {

        numPro = Utilisateur.creerPro("Adebayo,Bam", "9 rue main",
                "St-Laurent", "QC", "H4N1N5", "4382300301",
                "a@ab.com" );
        numPro1 = Utilisateur.creerPro("Holiday,Justin",
                "6 rue main","Fort McMurray", "AB", "J2L1N5", "4382300302",
                "d@ab.com");
        numPro2 = Utilisateur.creerPro("Young,Trae",  "8 rue main",
                "Montreal", "QC", "H4N2N8","4382300303",
                "b@ab.com");
        numPro3 = Utilisateur.creerPro("Holiday,Jrue", "7 rue main",
                "Laval", "QC", "H4J2L1","4382300304",
                "c@ab.com");
        wrongProProvinceNum = Utilisateur.creerPro("Holiday,Justin",
                "6 rue main","Fort McMurray", "AC", "J2L1N5", "4382300305",
                "d@ab.com");
        wrongProCodeNum = Utilisateur.creerPro("Young,Trae",  "8 rue main",
                "Montreal", "QC", "H4N2N81","4382300306",
                "b@ab.com");
        wrongProTelNum = Utilisateur.creerPro("Holiday,Jrue", "7 rue main",
                "Laval", "QC", "H4J2L1","43823003084",
                "c@ab.com");
        membre = Utilisateur.creerMembre("Curry,Stephen", "10 rue main",
                "Laval", "QC", "H4J2L1","4382300311",
                "g@ab.com");
        membre1 = Utilisateur.creerMembre("James,LeBron","11 rue main",
                "Calgary", "AB", "M1L2N2","4382300312",
                "f@ab.com");
        membre2 = Utilisateur.creerMembre("Irving,Kyrie", "12 rue main",
                "Iqaluit", "NU", "J5L2N3",
                "4382300313", "e@ab.com");
        wrongMembreNom = Utilisateur.creerMembre("Curry Stephen", "10 rue main",
                "Laval", "QC", "H4J2L1","4382300314",
                "g@ab.com");
        wrongMembreAd = Utilisateur.creerMembre("James,LeBron","11 main",
                "Calgary", "AB", "M1L2N2","4382300315",
                "f@ab.com");
        wrongMembreVille = Utilisateur.creerMembre("Irving,Kyrie", "12 rue main",
                "123", "NU", "J5L2N3",
                "4382300316", "e@ab.com");
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
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        wrongBasketHeure = Service.creerSeances("Basket",  "13-11-2019",
                "12-01-2020", "13:00-12:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        wrongPiscineRec = Service.creerSeances("Piscine",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi vendredi", 22, 10, "", true);
        wrongYogaCap = Service.creerSeances("Yoga",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 0, 10, "", true);
        rightYogaCap = Service.creerSeances("Yoga",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 9, 10, "", true);
        wrongBasketMoney = Service.creerSeances("Basket",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 101, "", true);
        transaction = new Transaction(piscine.getMontant(), "cash");
        transaction1 = new Transaction(yoga.getMontant(), "cash");
        transaction2 = new Transaction(basket.getMontant(), "cash");
        assertTrue(BaseDonnees.inscrireSeance(membre, piscine, transaction));
        System.out.println("membre inscrit!");
        assertTrue(BaseDonnees.inscrireSeance(membre1, basket, transaction2));
        System.out.println("membre1 inscrit!");
        assertTrue(BaseDonnees.inscrireSeance(membre2, yoga, transaction1));
        System.out.println("membre2 inscrit!");
        assertFalse(BaseDonnees.inscrireSeance(membre2, wrongYogaCap, transaction1));
        System.out.println("membre2 n'est pas inscrit!");
        ArrayList<Service> services = new ArrayList<>();
        Professionnel prof;
        services.add(piscine);
        services.add(yoga);
        services.add(basket);
        for(Service service : services) {
            if(service.getNumPro() == pro.getNumero()) {
                prof = pro;
            }  else if(service.getNumPro() == pro1.getNumero()) {
                prof = pro1;
            } else { prof = pro2; }
            System.out.println(prof.getNom());
            if(service.getRecurrence().contains(BaseDonnees.getTodayStr())) {
                assertTrue(BaseDonnees.imprimerInscriptions(prof));
            } else {
                assertFalse(BaseDonnees.imprimerInscriptions(prof));
            }
        }
    }

    @Test
    /**
     * Une classe pour tester la confirmation de presence.
     */
    public void actualiserPresenceTest()
            throws NotFoundException, IOException, WriterException {

        numPro = Utilisateur.creerPro("Adebayo,Bam", "9 rue main",
                "St-Laurent", "QC", "H4N1N5", "4382300301",
                "a@ab.com" );
        numPro1 = Utilisateur.creerPro("Holiday,Justin",
                "6 rue main","Fort McMurray", "AB", "J2L1N5", "4382300302",
                "d@ab.com");
        numPro2 = Utilisateur.creerPro("Young,Trae",  "8 rue main",
                "Montreal", "QC", "H4N2N8","4382300303",
                "b@ab.com");
        membre = Utilisateur.creerMembre("Curry,Stephen", "10 rue main",
                "Laval", "QC", "H4J2L1","4382300311",
                "g@ab.com");
        membre1 = Utilisateur.creerMembre("James,LeBron","11 rue main",
                "Calgary", "AB", "M1L2N2","4382300312",
                "f@ab.com");
        membre2 = Utilisateur.creerMembre("Irving,Kyrie", "12 rue main",
                "Iqaluit", "NU", "J5L2N3",
                "4382300313", "e@ab.com");
        pro = BaseDonnees.getPro(numPro);
        pro1 = BaseDonnees.getPro(numPro1);
        pro2 = BaseDonnees.getPro(numPro2);
        piscine = Service.creerSeances("Piscine",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        basket = Service.creerSeances("Basket", "02-12-2019",
                "12-01-2020", "8:00-9:00", pro1.getNumero(), "dimanche," +
                        " mardi , jeudi, vendredi", 30, 100, "NBA juniors", true);
        yoga = Service.creerSeances("Yoga",  "20-11-2019",
                "02-02-2020", "17:00-19:00", pro2.getNumero(),
                "samedi,lundi,mercredi", 30, 20, "", true);
        transaction = new Transaction(piscine.getMontant(), "cash");
        transaction1 = new Transaction(yoga.getMontant(), "cash");
        transaction2 = new Transaction(basket.getMontant(), "cash");
        assertTrue(BaseDonnees.inscrireSeance(membre, piscine, transaction));
        assertTrue(BaseDonnees.inscrireSeance(membre1, basket, transaction2));
        assertFalse(BaseDonnees.inscrireSeance(membre2, yoga, transaction1));
        Seance seance = BaseDonnees.getSeance(1012501);
        assertFalse(BaseDonnees.actualiserPresence(seance,
                membre1.getNumero(), true));
        assertTrue(BaseDonnees.actualiserPresence(seance,
                membre.getNumero(), false));
        assertTrue(BaseDonnees.actualiserPresence(seance,
                membre.getNumero(), true));
        Seance seance1 = BaseDonnees.getSeance(3030802);
        assertFalse(BaseDonnees.actualiserPresence(seance1,
                membre2.getNumero(), true));
        assertTrue(BaseDonnees.actualiserPresence(seance1,
                membre1.getNumero(), true));
        assertTrue(BaseDonnees.actualiserPresence(seance1,
                membre1.getNumero(), true));

    }

    @Test
    /**
     * Une classe pour tester le procedure comptable.
     */
    public void envoyerVenMinuitTest()
            throws NotFoundException, IOException, WriterException {
        numPro = Utilisateur.creerPro("Adebayo,Bam", "9 rue main",
                "St-Laurent", "QC", "H4N1N5", "4382300301",
                "a@ab.com" );
        numPro1 = Utilisateur.creerPro("Holiday,Justin",
                "6 rue main","Fort McMurray", "AB", "J2L1N5", "4382300302",
                "d@ab.com");
        numPro2 = Utilisateur.creerPro("Young,Trae",  "8 rue main",
                "Montreal", "QC", "H4N2N8","4382300303",
                "b@ab.com");
        numPro3 = Utilisateur.creerPro("Holiday,Jrue", "7 rue main",
                "Laval", "QC", "H4J2L1","4382300304",
                "c@ab.com");
        membre = Utilisateur.creerMembre("Curry,Stephen", "10 rue main",
                "Laval", "QC", "H4J2L1","4382300311",
                "g@ab.com");
        membre1 = Utilisateur.creerMembre("James,LeBron","11 rue main",
                "Calgary", "AB", "M1L2N2","4382300312",
                "f@ab.com");
        membre2 = Utilisateur.creerMembre("Irving,Kyrie", "12 rue main",
                "Iqaluit", "NU", "J5L2N3",
                "4382300313", "e@ab.com");
        pro = BaseDonnees.getPro(numPro);
        pro1 = BaseDonnees.getPro(numPro1);
        pro2 = BaseDonnees.getPro(numPro2);
        pro3 = BaseDonnees.getPro(numPro3);

        gerant = Gerant.assignerGerant("Harden,James","13 rue main",
                "Quebec city", "QC", "H4M2A1", "4382300307",
                "h@ab.com", true, false);
        Gerant gerant1 = Gerant.assignerGerant("Westbrook,Russel",
                "14 rue main","York", "ON", "H3M2N1",
                "4382300308", "i@ab.com", true, false);
        numEmploye = Utilisateur.creerEmploye("Westbrook,Russel", "14 rue main",
                "Laval", "QC", "H3A2M1", "4382300309",
                "j@ab.com");
        numEmployeEmail = Utilisateur.creerEmploye("Westbrook,Russel", "14 rue main",
                "Laval", "QC", "H3A2M1", "4382300310",
                "j@ab.com");
        employe = BaseDonnees.getEmploye(numEmploye);
        employeEmail = BaseDonnees.getEmploye(numEmployeEmail);
        piscine = Service.creerSeances("Piscine",  "13-11-2019",
                "12-01-2020", "12:00-13:00", pro.getNumero(), "dimanche, " +
                        "lundi, mardi, jeudi, vendredi", 22, 10, "", true);
        basket = Service.creerSeances("Basket", "02-12-2019",
                "12-01-2020", "8:00-9:00", pro1.getNumero(), "dimanche," +
                        " mardi , jeudi, vendredi", 30, 100, "NBA juniors", true);
        yoga = Service.creerSeances("Yoga",  "20-11-2019",
                "02-02-2020", "17:00-19:00", pro2.getNumero(),
                "samedi,lundi,mercredi", 30, 20, "", true);
        transaction = new Transaction(piscine.getMontant(), "cash");
        transaction1 = new Transaction(yoga.getMontant(), "cash");
        transaction2 = new Transaction(basket.getMontant(), "cash");
        assertTrue(BaseDonnees.inscrireSeance(membre, piscine, transaction));
        assertTrue(BaseDonnees.inscrireSeance(membre1, basket, transaction2));
        assertFalse(BaseDonnees.inscrireSeance(membre2, yoga, transaction1));
        String str = BaseDonnees.envoyerVenMinuit();
        assertNotNull(str);
        System.out.println(str);
        ArrayList<Transaction> transactions = BaseDonnees.envoyerRnB
                (BaseDonnees.getDate("dd-MM-YYYY"));
        assertNotNull(transactions);
        System.out.println(transactions);
    }
}