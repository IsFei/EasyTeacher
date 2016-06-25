package com.ace.easyteacher.DataBase;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "class_info")
public class ClassInfo {
	@Column(name = "class_name")
	private String class_name;
	@Column(name = "room_name")
	private String room_name;
	@Column(name = "subject")
	private String subject;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

}
