package com.xhb.onlystar.bean;

public class FileUrl {
	
	String PicPath;
	String clsbm;
	String rwdh;
	int xh;
	
	public FileUrl(String picPath, String clsbm, String rwdh, int xh) {
		PicPath = picPath;
		this.clsbm = clsbm;
		this.rwdh = rwdh;
		this.xh = xh;
	}

	public String getPicPath() {
		return PicPath;
	}

	public void setPicPath(String picPath) {
		PicPath = picPath;
	}

	public String getClsbm() {
		return clsbm;
	}

	public void setClsbm(String clsbm) {
		this.clsbm = clsbm;
	}

	public String getRwdh() {
		return rwdh;
	}

	public void setRwdh(String rwdh) {
		this.rwdh = rwdh;
	}

	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}
	
	


}
