<%@page import="demoapp.ConnectionTest"%>
<%@page import="org.hibernate.Session"%>
<%@page import="org.hibernate.SessionFactory"%>
<%@page import="org.hibernate.query.Query"%>
<%@page import="demoapp.stock"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.math3.stat.regression.*"%>
<%@page import="java.io.File"%>
<%@page import="org.jfree.chart.ChartUtilities"%>
<%@page import="org.jfree.chart.ChartFactory"%>
<%@page import="org.jfree.chart.JFreeChart"%>
<%@page import="org.jfree.chart.plot.PlotOrientation"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.text.ParseException"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  
</head>
<title>Stock Analyzer</title>
<body>
<%
//test

Session sess=null;
List<stock> stocks=null;

sess=ConnectionTest.GetSessionFactory().getCurrentSession();
if(!sess.getTransaction().isActive()) sess.beginTransaction();
if(stocks==null){
Query query1 = sess.createQuery("select distinct name from stock");
query1.setMaxResults(30);
stocks=query1.getResultList();
}
int j=0;
%>
<div class="container">
  <h2>Stock Analyzer</h2>
  <p>Select Your company from the list:</p>            
  <form action="<% String name=request.getParameter("selected");
%>" role="form" method='get'>
  <select name="selected">
  <%  while(j<30){ %>
            <option value="<%=stocks.get(j)%>"> <%=stocks.get(j)%></option>
        <% j++;} %>
        </select>
          <button type="submit" class="btn btn-info" >Get Current Info</button>
          
    </form>
    <h4><%=name==null?"":name %></h4>
    <%if(name!=null){ Query query2=sess.createQuery("from stock where name like"+"'"+name.substring(0,5)+"%'"+"order by ID DESC");
          Date date1;
          List<stock> currentstock=query2.getResultList();
          stock now=currentstock.get(0);
	      SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
	      DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
	      double[][] reg=new double[currentstock.size()][2];
	      double max=Integer.MIN_VALUE;
	      double min=Integer.MAX_VALUE;
	  	try {
			date1 = myFormat.parse(currentstock.get(currentstock.size()-1).getTime());
			for(int i=0;i<currentstock.size();i++) {
			reg[i][1]=Double.parseDouble(currentstock.get(i).getPrice());
			Date current=myFormat.parse(currentstock.get(i).getTime());
			long diff=current.getTime()-date1.getTime();
			reg[i][0]=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			max=max<reg[i][1]?reg[i][1]:max;
			min=min>reg[i][1]?reg[i][1]:min;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  	 SimpleRegression simpleRegression = new SimpleRegression(true);
		   simpleRegression.addData(reg);
		   for(int i=reg.length-1;i>=0;i--) {
			   dataset.addValue(reg[i][1], "", currentstock.get(i).getTime());
		   }
		   JFreeChart lineChart = ChartFactory.createLineChart(
			         name,
			         "Price","Date",
			         dataset,
			         PlotOrientation.VERTICAL,
			         true,true,false);
		   lineChart.getCategoryPlot().getRangeAxis().setRange(min,max);;
		   File Chart = new File(getServletContext().getRealPath(".") +"/"+name+".gif" ); 
		   ChartUtilities.saveChartAsJPEG(Chart ,lineChart, 1280 ,640);
		   String defaultdate=currentstock.get(0).getTime().substring(6,10)+"-"+currentstock.get(0).getTime().substring(0,2)+"-"+currentstock.get(0).getTime().substring(3,5);
		   System.out.println(defaultdate);
	      %>
	     <table class="table">
	     <h4>Last Update: <%=now.getTime() %></h4>
    <thead>
      <tr>
        <th>Name</th>
        <th>YearToDate</th>
        <th>Price</th>
        <th>Change</th>
        <th>Percent</th>
        <th>Volume</th>
        <th>PE</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><%=now.getName() %></td>
        <td><%=now.getYTD() %></td>
        <td><%=now.getPrice() %></td>
        <td><%=now.getChange() %></td>
        <td><%=now.getPercent() %></td>
        <td><%=now.getVolume() %></td>
        <td><%=now.getPE() %></td>
      </tr>      
  
    </tbody>
  </table> 
 <img border="0"  src="<%=name%>.gif" width="800" height="400"/>

  <form action="<% String currentdate=request.getParameter("number"); 
%>" role="form" method='post'> 
<label for="num">Choose the date you want to predict:</label>
<input type="date" name="number" id="num" value=<%=currentdate==null?defaultdate:currentdate%>>
 <button type="submit" class="btn btn-info" >Predict</button>
</form>
<%String predict;
if(currentdate!=null){predict=currentdate.substring(5,7)+"/"+currentdate.substring(8,10)+"/"+currentdate.substring(0,4);
Date predicteddate=myFormat.parse(predict);
Date currendd=myFormat.parse(currentstock.get(0).getTime());
Date date0=myFormat.parse(currentstock.get(currentstock.size()-1).getTime());
long origin=TimeUnit.DAYS.convert(currendd.getTime()-date0.getTime(), TimeUnit.MILLISECONDS);
long timedifference=TimeUnit.DAYS.convert(predicteddate.getTime()-date0.getTime(), TimeUnit.MILLISECONDS);
if(timedifference>origin){
%>
<h5>Your predicted date is: <%=currentdate %> and your predicted price is: <%=simpleRegression.predict(timedifference) %></h5>
<% 
}
if(timedifference<=origin){
%>
<h5>Please select a date later than today</h5>
<% 	
}
}
%>

	      <% 
	      sess.close();} %>
</div>


</body>
</html>