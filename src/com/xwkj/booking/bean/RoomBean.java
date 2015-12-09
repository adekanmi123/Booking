package com.xwkj.booking.bean;

import java.util.Date;

import com.xwkj.booking.domain.Room;

public class RoomBean {
	private String rid;
	private String rname;
	private int number;
	private String location;
	private String transportation;
	private double latitude;
	private double longitude;
	private int level;
	private double area;
	private double price;
	private String descriptor;
	private Date createDate;
	private PhotoBean cover;
	private int sold;
	private int available;
	private boolean enable;
	
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public PhotoBean getCover() {
		return cover;
	}
	public void setCover(PhotoBean cover) {
		this.cover = cover;
	}	
	public int getSold() {
		return sold;
	}
	public void setSold(int sold) {
		this.sold = sold;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getTransportation() {
		return transportation;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public int getLevel() {
		return level;
	}
	public void setTransportation(String transportation) {
		this.transportation = transportation;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public RoomBean(Room room) {
		super();
		this.rid = room.getRid();
		this.rname = room.getRname();
		this.number = room.getNumber();
		this.location = room.getLocation();
		this.transportation= room.getTransportation();
		this.latitude= room.getLatitude();
		this.longitude=room.getLongitude();
		this.level=room.getLevel();
		this.area = room.getArea();
		this.price = room.getPrice();
		this.descriptor = room.getDescriptor();
		this.createDate = room.getCreateDate();
		if(room.getCover()==null)
			this.cover=null;
		else
			this.cover=new PhotoBean(room.getCover());
		this.sold=room.getSold();
		this.available=room.getAvailable();
		this.enable=room.isEnable();
	}

}
