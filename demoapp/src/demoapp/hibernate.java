package demoapp;
import java.io.IOException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import demoapp.stock;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
public class hibernate {
	public static void main(String[] args) throws IOException {
		
		
		    
			//start the session
			SessionFactory fac = new Configuration().
					configure("/demoapp/hibernate.cfg.xml").
					addAnnotatedClass(stock.class).buildSessionFactory();
			Session session=fac.getCurrentSession();
			session.beginTransaction();
			Query query = session.createQuery("from stock order by ID DESC");
		    query.setMaxResults(1);
		    stock last = (stock) query.uniqueResult();
			
	}
}
