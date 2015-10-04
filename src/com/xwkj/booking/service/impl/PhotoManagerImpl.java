package com.xwkj.booking.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.WebContextFactory;

import com.xwkj.booking.bean.PhotoBean;
import com.xwkj.booking.domain.Photo;
import com.xwkj.booking.domain.Room;
import com.xwkj.booking.service.PhotoManager;
import com.xwkj.booking.service.util.ManagerTemplate;
import com.xwkj.booking.servlet.PhotoServlet;

public class PhotoManagerImpl extends ManagerTemplate implements PhotoManager {

	@Override
	public List<PhotoBean> getPhotosByRid(String rid) {
		Room room=roomDao.get(rid);
		List<PhotoBean> photos=new ArrayList<>();
		for(Photo photo: photoDao.findByRoom(room))
			photos.add(new PhotoBean(photo));
		return photos;
	}

	@Override
	public void deletePhoto(String pid) {
		Photo photo=photoDao.get(pid);
		String rootPath=WebContextFactory.get().getServletContext().getRealPath("/");
		String pathname=rootPath+PhotoServlet.PHOTO_FOLDER+"/"+photo.getRoom().getRid()+"/"+photo.getFilename();
		File file=new File(pathname);
		file.delete();
		Room room=photo.getRoom();
		if(room.getCover()!=null) {
			if(room.getCover().getPid().equals(photo.getPid())) {
				room.setCover(null);
				roomDao.update(room);
			}
		}
		photoDao.delete(photo);
	}

	@Override
	public void setAsRoomCover(String pid) {
		Photo photo=photoDao.get(pid);
		Room room=photo.getRoom();
		room.setCover(photo);
		roomDao.update(room);
	}

}
