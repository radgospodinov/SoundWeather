package model;


import java.util.concurrent.locks.Lock;

import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {

	private static SessionFactory sessionFactory;
	

	public static Session getSession() {
		if(sessionFactory==null) {
		Configuration configuration = new Configuration();
		configuration.configure("hibernate.cfg.xml");
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		sessionFactory = new Configuration().configure().buildSessionFactory(serviceRegistry);
		}
		return sessionFactory.openSession();
	}

	
}
