package com.yuncai.call.tdvrcall.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class RecordBean implements Serializable{
	

	private int record_id;
	private String record_name;
	private String record_num;
	private String record_address;
	private int record_type;
	private String record_date;
	private long record_contactid;
	private long record_photoid;
	private int start=-1;
	private int end=-1;
	private int type=-1;
	private boolean showflag;
	public  RecordBean(){};//
	boolean isBlackContact ;
	//通过号码查询联系人列表是否存在此号码
	int  isContacts;
	// 通过电话号获取联系人姓名
	String contactsName;
	// 通过联系人号码获取联系人头像
	Bitmap contactIcon;
	String contactIconUri ;
	private String record_duration ;


	public String getContactIconUri() {
		return contactIconUri;
	}

	public void setContactIconUri(String contactIconUri) {
		this.contactIconUri = contactIconUri;
	}

	public int getIsContacts() {
		return isContacts;
	}
	public void setIsContacts(int isContacts) {
		this.isContacts = isContacts;
	}
	public String getContactsName() {
		return contactsName;
	}
	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	public Bitmap getContactIcon() {
		return contactIcon;
	}
	public void setContactIcon(Bitmap contactIcon) {
		this.contactIcon = contactIcon;
	}
	public boolean getIsBlackContact() {
		return isBlackContact;
	}
	public void setIsBlackContact(boolean isBlackContact) {
		this.isBlackContact = isBlackContact;
	}

	public String getRecord_duration() {
		return record_duration;
	}

	public void setRecord_duration(String record_duration) {
		this.record_duration = record_duration;
	}

	/**
	 * @return the showflag
	 */
	public boolean isShowflag() {
		return showflag;
	}
	/**
	 * @param showflag the showflag to set
	 */
	public void setShowflag(boolean showflag) {
		this.showflag = showflag;
	}
	/**
	 * @return the record_address
	 */
	public String getRecord_address() {
		return record_address;
	}
	/**
	 * @param record_address the record_address to set
	 */
	public void setRecord_address(String record_address) {
		this.record_address = record_address;
	}
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the record_contactid
	 */
	public long getRecord_contactid() {
		return record_contactid;
	}
	/**
	 * @param record_contactid the record_contactid to set
	 */
	public void setRecord_contactid(long record_contactid) {
		this.record_contactid = record_contactid;
	}
	/**
	 * @return the record_photoid
	 */
	public long getRecord_photoid() {
		return record_photoid;
	}
	/**
	 * @param record_photoid the record_photoid to set
	 */
	public void setRecord_photoid(long record_photoid) {
		this.record_photoid = record_photoid;
	}
	public int getRecord_id() {
		return record_id;
	}
	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}
	public String getRecord_name() {
		return record_name;
	}
	public void setRecord_name(String record_name) {
		this.record_name = record_name;
	}
	public String getRecord_num() {
		return record_num;
	}
	public void setRecord_num(String record_num) {
		this.record_num = record_num;
	}
	public int getRecord_type() {
		return record_type;
	}
	public void setRecord_type(int record_type) {
		this.record_type = record_type;
	}
	public String getRecord_date() {
		return record_date;
	}
	public void setRecord_date(String record_date) {
		this.record_date = record_date;
	}
	
}