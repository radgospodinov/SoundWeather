package model;

import javax.servlet.ServletContext;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import org.springframework.beans.factory.annotation.Autowired;

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
