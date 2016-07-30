package com.xhb.onlystar.bean;

public class CreateUpload {
	private Rwmxb RwmxModel;
	private String FileName;
	private long FileSize;
	private String RequestTime;
	private String TimeMd5;

	public CreateUpload(Rwmxb rwmxModel, String fileName, long fileSize, String requestTime, String timeMd5) {
		super();
		RwmxModel = rwmxModel;
		FileName = fileName;
		FileSize = fileSize;
		RequestTime = requestTime;
		TimeMd5 = timeMd5;
	}

	public Rwmxb getRwmxModel() {
		return RwmxModel;
	}

	public void setRwmxModel(Rwmxb rwmxModel) {
		RwmxModel = rwmxModel;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public long getFileSize() {
		return FileSize;
	}

	public void setFileSize(int fileSize) {
		FileSize = fileSize;
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
