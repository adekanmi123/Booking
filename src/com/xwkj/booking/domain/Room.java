package com.xwkj.booking.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Room implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String rid;
	private String rname;
	private Integer number;
	private String location;
	private String transportation;
	private Double latitude;
	private Double longitude;
	private Integer level;
	private Double area;
	private Double price;
	private String descriptor;
	private Date createDate;
	private Photo cover;
	private Set<Photo> photos;
	private Integer sold;
	private Integer available;
	private boolean enable;
	
	public String getRid() {
		return rid;
	}
	public String getRname() {
		return rname;
	}
	public Integer getNumber() {
		return number;
	}
	public String getLocation() {
		return location;
	}
	public String getTransportation() {
		return transportation;
	}
	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public Integer getLevel() {
		return level;
	}
	public Double getArea() {
		return area;
	}
	public Double getPrice() {
		return price;
	}
	public String getDescriptor() {
		return descriptor;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Photo getCover() {
		return cover;
	}
	public Set<Photo> getPhotos() {
		return photos;
	}
	public Integer getSold() {
		return sold;
	}
	public Integer getAvailable() {
		return available;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setTransportation(String transportation) {
		this.transportation = transportation;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public void setArea(Double area) {
		this.area = area;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setCover(Photo cover) {
		this.cover = cover;
	}
	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}
	public void setSold(Integer sold) {
		this.sold = sold;
	}
	public void setAvailable(Integer available) {
		this.available = available;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
}
