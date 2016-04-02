package model;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateTest {

	public static void main(String[] args) {
		
		
		User u = new User();
		u.setAlbums(new ArrayList<Album>());
		
		
		 Configuration configuration = new Configuration();
         configuration.configure("hibernate.cfg.xml");
         System.out.println("Hibernate Configuration loaded");
          
         //apply configuration property settings to StandardServiceRegistryBuilder
         ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
         System.out.println("Hibernate serviceRegistry created");
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory(serviceRegistry);
		
		Session session = sf.openSession();
		session.beginTransaction();
		
		session.save(u);
		session.getTransaction().commit();
		
		session.close();
	}

	
	
	
}
