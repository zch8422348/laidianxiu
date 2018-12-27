package com.yuncai.call.tdvrcall.bean;

/**
 * Function:一键拨号 实体类
 * Created by TianMing.Xiong on 18-12-20.
 */

public class QuickContact{
    private int _id;
    private String qGroupId;
    private String qName;
    private String qPhoneNumber;

    public String getqGroupId() {
        return qGroupId;
    }

    public void setqGroupId(String qGroupId) {
        this.qGroupId = qGroupId;
    }

    public String getqName() {
        return qName;
    }

    public void setqName(String qName) {
        this.qName = qName;
    }

    public String getqPhoneNumber() {
        return qPhoneNumber;
    }

    public void setqPhoneNumber(String qPhoneNumber) {
        this.qPhoneNumber = qPhoneNumber;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "QuickContact{" +
                "qGroupId='" + qGroupId + '\'' +
                ", qName='" + qName + '\'' +
                ", qPhoneNumber='" + qPhoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        if(this==obj){
            return true;
        }
        if(obj instanceof QuickContact){
            QuickContact quickContact = (QuickContact) obj;
            if(quickContact.getqName().equals(this.getqName()) && quickContact.getqPhoneNumber().equals(this.getqPhoneNumber())){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}
