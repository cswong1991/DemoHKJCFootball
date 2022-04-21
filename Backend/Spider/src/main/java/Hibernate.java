import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Hibernate {
    private static Hibernate instance;
    private SessionFactory session_factory;
    public Session session;
    private Transaction transaction;

    private Hibernate() {
    }

    public static Hibernate getInstance() {
        if (instance == null) {
            synchronized (Hibernate.class) {
                if (instance == null) {
                    instance = new Hibernate();
                }
            }
        }
        return instance;
    }

    public void initSession(String host, String port, String db, String user, String password) {
        Properties props = new Properties();
        String db_url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        props.setProperty("hibernate.connection.url", db_url);
        props.setProperty("hibernate.connection.username", user);
        props.setProperty("hibernate.connection.password", password);
        session_factory = new Configuration()
                .configure()
                .addAnnotatedClass(ArchiveModel.class)
                .addProperties(props)
                .buildSessionFactory();
        session = session_factory.openSession();
        transaction = session.beginTransaction();
    }

    public void endSession() {
        transaction.commit();
        session.close();
        session_factory.close();
    }
}
