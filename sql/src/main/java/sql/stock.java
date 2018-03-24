package sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="stockmarket")
public class stock {
@Id
@GenericGenerator(name="kaugen" , strategy="increment")
@GeneratedValue(generator="kaugen")
@Column(name="ID")
private int ID;

@Column(name="name")
private String name;

@Column(name="YTD")
private String YTD;

@Column(name="price")
private String price;

@Column(name="change")
private String change;

@Column(name="per")
private String percent;

@Column(name="volume")
private String volume;

@Column(name="time")
private String time;

@Column(name="PE")
private String PE;

public stock() {}
public stock(String name,String price,String change,String percent,String PE,String volume,String YTD,String time) {

this.name=name;	
this.percent=percent;	
this.change=change;	
this.price=price;	
this.PE=PE;	
this.time=time;	
this.YTD=YTD;	
this.volume=volume;	
}
public int getID() {
	return ID;
}
public void setID(int iD) {
	ID = iD;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getYTD() {
	return YTD;
}
public void setYTD(String yTD) {
	YTD = yTD;
}
public String getPrice() {
	return price;
}
public void setPrice(String price) {
	this.price = price;
}
public String getChange() {
	return change;
}
public void setChange(String change) {
	this.change = change;
}
public String getPercent() {
	return percent;
}
public void setPercent(String percent) {
	this.percent = percent;
}
public String getVolume() {
	return volume;
}
public void setVolume(String volume) {
	this.volume = volume;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getPE() {
	return PE;
}
public void setPE(String pE) {
	PE = pE;
}
}
