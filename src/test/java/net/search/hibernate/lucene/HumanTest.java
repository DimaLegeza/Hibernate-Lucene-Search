package net.search.hibernate.lucene;

import net.search.hibernate.lucene.human.Human;
import org.apache.log4j.BasicConfigurator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author: Dima Legeza
 * @since: 06.04.14
 */
public class HumanTest {

    public static final Version LUCENE_VERSION = Version.LUCENE_36;
    private static Logger logger = Logger.getLogger(HumanTest.class);

    private static Human[] testHumans = {new Human("Dmytro", "Legeza", (short) 1989, "male"),
            new Human("Alexandra", "Bowkun", (short) 1989, "female")};

    private static SessionFactory hibernateSessionFactory;

    private static Session hibernateSession;

    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        Configuration configuration = new Configuration();
        configuration.configure("hibernate-test-cfg.xml");
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
                .buildServiceRegistry();
        hibernateSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        hibernateSession = hibernateSessionFactory.openSession();
        populateDBWithTestData();
    }

    @Test
    public void testSynonymsMale() throws Exception {
        Analyzer analyzer = Search.getFullTextSession(hibernateSession).getSearchFactory().getAnalyzer("synonymsAnalyzer");
        FullTextSession fullTestSession = Search.getFullTextSession(hibernateSession);
        QueryParser parser = new QueryParser(LUCENE_VERSION, "name", analyzer);
        String searchString = "Dima";
        Query luceneQuery = parser.parse(searchString);
        org.hibernate.search.FullTextQuery fullTextQuery = fullTestSession.createFullTextQuery(luceneQuery);
        fullTextQuery.setProjection("id", "name");
        List<Object[]> searchResults = fullTextQuery.list();

        boolean foundName = false;
        for (Object[] result : searchResults) {
            logger.debug("Result found: " + result[0] + ", " + result[1]);
            if (result[1].equals("Dmytro")) {
                foundName = true;
            }
        }
        Assert.assertEquals(1, searchResults.size());
        Assert.assertTrue(foundName);
    }

    @Test
    public void testSynonymsFemale() throws Exception {
        Analyzer analyzer = Search.getFullTextSession(hibernateSession).getSearchFactory().getAnalyzer("synonymsAnalyzer");
        FullTextSession fullTestSession = Search.getFullTextSession(hibernateSession);
        QueryParser parser = new QueryParser(LUCENE_VERSION, "name", analyzer);
        String searchString = "Sasha";
        Query luceneQuery = parser.parse(searchString);
        org.hibernate.search.FullTextQuery fullTextQuery = fullTestSession.createFullTextQuery(luceneQuery);
        fullTextQuery.setProjection("id", "name");
        List<Object[]> searchResults = fullTextQuery.list();

        boolean foundName = false;
        for (Object[] result : searchResults) {
            logger.debug("Result found: " + result[0] + ", " + result[1]);
            if (result[1].equals("Alexandra")) {
                foundName = true;
            }
        }
        Assert.assertEquals(1, searchResults.size());
        Assert.assertTrue(foundName);
    }

    private void populateDBWithTestData() {
        Transaction tx = hibernateSession.beginTransaction();

        hibernateSession.save(testHumans[0]);
        hibernateSession.save(testHumans[1]);

        tx.commit();
    }

}
