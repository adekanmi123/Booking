package com.xwkj.booking.bean;

import java.util.Date;

import com.xwkj.booking.domain.Photo;

public class PhotoBean {
	private String pid;
	private String filename;
	private Date upload;
	private String rid;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Date getUpload() {
		return upload;
	}
	public void setUpload(Date upload) {
		this.upload = upload;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	
	public PhotoBean(Photo photo) {
		super();
		this.pid = photo.getPid();
		this.filename = photo.getFilename();
		this.upload = photo.getUpload();
		this.rid = photo.getRoom().getRid();
	}
	
}
