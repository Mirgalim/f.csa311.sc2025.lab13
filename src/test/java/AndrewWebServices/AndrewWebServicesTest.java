package AndrewWebServices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AndrewWebServicesTest {
    Database database;
    RecSys recommender;
    PromoService promoService;
    AndrewWebServices andrewWebService;

    boolean mailSent;

    class StubRecSys extends RecSys {
        @Override
        public String getRecommendation(String accountName) {
            return "The Matrix";
        }
    }

    class MockPromoService extends PromoService {
        @Override
        public void mailTo(String email) {
            mailSent = true;
        }
    }

    @Before
    public void setUp() {
        database = new InMemoryDatabase();
        ((InMemoryDatabase) database).addAccount("Scotty", 17214);

        recommender = new StubRecSys();
        promoService = new MockPromoService();
        mailSent = false;

        andrewWebService = new AndrewWebServices(database, recommender, promoService);
    }

    @Test
    public void testLogIn() {
        assertTrue(andrewWebService.logIn("Scotty", 17214));
    }

    @Test
    public void testGetRecommendation() {
        assertEquals("The Matrix", andrewWebService.getRecommendation("Scotty"));
    }

    @Test
    public void testSendEmail() {
        andrewWebService.sendPromoEmail("test@example.com");
        assertTrue(mailSent);
    }

    @Test
    public void testNoSendEmail() {
        andrewWebService.logIn("Scotty", 17214);
        assertTrue(!mailSent);
    }
}
