package demoapp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import demoapp.stock;

public class ConnectionTest {
	
private static SessionFactory factory=null;
private static Configuration config=null;


private static SessionFactory BuildSessionFactory(){

try {
config=new Configuration();	
config.configure("/demoapp/hibernate.cfg.xml").
addAnnotatedClass(stock.class);
System.out.println("Configuration success");
factory= config.buildSessionFactory();
return factory;

}
catch(Throwable ex) {
System.err.println("SessionFactory creation failure");
ex.printStackTrace();
throw new ExceptionInInitializerError(ex);
}
}
public static SessionFactory GetSessionFactory() {
if(factory==null) factory=BuildSessionFactory();
return factory;
}
public static List<stock> Dropdown(){
if(factory==null) factory=GetSessionFactory();
Session sess=ConnectionTest.GetSessionFactory().getCurrentSession();
sess.beginTransaction();
Query query1 = sess.createQuery("select distinct name from stock");
query1.setMaxResults(30);
List<stock> stocks=query1.getResultList();
sess.close();
return stocks;
}

public static List<stock> CurrentInfo(){
if(factory==null) factory=GetSessionFactory();
Session sess=ConnectionTest.GetSessionFactory().getCurrentSession();
sess.beginTransaction();
Query query1 = sess.createQuery("select distinct name from stock");
query1.setMaxResults(30);
List<stock> stocks=query1.getResultList();
sess.close();
return stocks;
}
}

