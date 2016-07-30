package com.xhb.onlystar.bean;

public class BeginUpload {
	private UploadInfo Fileinfo;
	private String FileContextStr;
	private String Compression;
	private String StrMd5;
	private String RequestTime;
	private String TimeMd5;

	public BeginUpload(UploadInfo fileinfo, String fileContextStr, String compression, String strMd5,
					   String requestTime, String timeMd5) {
		super();
		Fileinfo = fileinfo;
		FileContextStr = fileContextStr;
		Compression = compression;
		StrMd5 = strMd5;
		RequestTime = requestTime;
		TimeMd5 = timeMd5;
	}

	public UploadInfo getFileinfo() {
		return Fileinfo;
	}

	public void setFileinfo(UploadInfo fileinfo) {
		Fileinfo = fileinfo;
	}

	public String getFileContextStr() {
		return FileContextStr;
	}

	public void setFileContextStr(String fileContextStr) {
		FileContextStr = fileContextStr;
	}

	public String getCompression() {
		return Compression;
	}

	public void setCompression(String compression) {
		Compression = compression;
	}

	public String getStrMd5() {
		return StrMd5;
	}

	public void setStrMd5(String strMd5) {
		StrMd5 = strMd5;
	}

	public String getRequestTime() {
		return RequestTime;
	}

	public void setRequestTime(String requestTime) {
		RequestTime = requestTime;
	}

	public String getTimeMd5() {
		return TimeMd5;
	}

	public void setTimeMd5(String timeMd5) {
		TimeMd5 = timeMd5;
	}

}
