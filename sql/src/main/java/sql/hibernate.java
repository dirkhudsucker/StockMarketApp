package sql;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.apache.commons.math3.stat.regression.*;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
public class hibernate {

	public static void main(String[] args) throws IOException {
    
		//start the session
		SessionFactory fac = new Configuration().
				configure("/sql/hibernate.cfg.xml").
				addAnnotatedClass(stock.class).buildSessionFactory();
		Session session=fac.getCurrentSession();
		session.beginTransaction();
	
  
		//fetching data from the website
		for(int j=1;j<3;j++) {
		org.jsoup.nodes.Document doc;
		doc=Jsoup.connect("http://money.cnn.com/data/markets/dow/?page="+j).get();
		String time = doc.getElementById("wsod_quoteRight").select("span").get(1).text();
		//parsing time format
		int n=0;
		for(int i=0;i<time.length();i++) {
	    	if(time.charAt(i)==',') {
	    		n=i;
	    		break;
	    	}	
		}
		time=time.substring(n+2);
		//querying if today's record has been recorded
	    Query query = session.createQuery("from stock order by ID DESC");
	    query.setMaxResults(1);
	    stock last = (stock) query.uniqueResult();
	    if(last.getTime().substring(0, 10).equals(time)&&j==1) {
	    System.out.println("Today's data already recorded");	
	    session.close();
	    return;
	    }
	    
		//getting the stock market table and save in database
		Element div = doc.getElementById("wsod_indexConstituents");
		Elements table=div.select("tbody");
		Elements rows = table.select("tr");
		for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
		    Element row = rows.get(i);
		    Elements cols = row.select("td");
		    stock temp=new stock(cols.get(0).text(),cols.get(1).text(),cols.get(2).text(),cols.get(3).text(),cols.get(4).text(),cols.get(5).text(),cols.get(6).text(), time);
		    session.save(temp);
		    }  
	}
		//submit the data saved in the session	
		session.getTransaction().commit();
		fac.close();
	}

}
