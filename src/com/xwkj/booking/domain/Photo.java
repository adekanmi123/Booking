package com.xwkj.booking.domain;

import java.io.Serializable;
import java.util.Date;

public class Photo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pid;
	private String filename;
	private Date upload;
	private Room room;
	
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
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	
}
