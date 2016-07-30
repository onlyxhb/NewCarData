package com.xhb.onlystar.bean;

public class SignInfo {
	public String grdm;//个人代码
	public String grmc;//个人名称
	public String lb;//签到类型 in签入 out签出
	public String zdrq ;//操作时间
	public String outDatetime;//签出时间
	public String sjID;//手机ID
	public String sjhm;//手机号码
	public double lng;//经度
	public double lat;//纬度
	
	
	public SignInfo(){
		
	}
	
	
	public SignInfo(String grdm, String grmc, String lb, String zdrq, String outDatetime, String sjID, String sjhm,
			double lng, double lat) {
		this.grdm = grdm;
		this.grmc = grmc;
		this.lb = lb;
		this.zdrq = zdrq;
		this.outDatetime = outDatetime;
		this.sjID = sjID;
		this.sjhm = sjhm;
		this.lng = lng;
		this.lat = lat;
	}
	public String getGrdm() {
		return grdm;
	}
	public void setGrdm(String grdm) {
		this.grdm = grdm;
	}
	public String getGrmc() {
		return grmc;
	}
	public void setGrmc(String grmc) {
		this.grmc = grmc;
	}
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	public String getZdrq() {
		return zdrq;
	}
	public void setZdrq(String zdrq) {
		this.zdrq = zdrq;
	}
	public String getOutDatetime() {
		return outDatetime;
	}
	public void setOutDatetime(String outDatetime) {
		this.outDatetime = outDatetime;
	}
	public String getSjID() {
		return sjID;
	}
	public void setSjID(String sjID) {
		this.sjID = sjID;
	}
	public String getSjhm() {
		return sjhm;
	}
	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
}
