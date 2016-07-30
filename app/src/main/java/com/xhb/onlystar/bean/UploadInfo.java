package com.xhb.onlystar.bean;

public class UploadInfo {
	private int BlockCount;//总块数
	private int BlockIndex;//当前块序号，从0开始计数
	private String BlockMd5;//当前块数据的Md5
	private int BlockSize;//每块字节数
	private int Code=1;//消息代码0成功，非0失败
	private String FileUrl;//文件地址
	private String ID;//上传唯一码
	private String Mes;//信息

	public UploadInfo() {
	}

	public UploadInfo(int blockCount, int blockIndex, String blockMd5, int blockSize, int code, String fileUrl,
			String iD, String mes) {
		BlockCount = blockCount;
		BlockIndex = blockIndex;
		BlockMd5 = blockMd5;
		BlockSize = blockSize;
		Code = code;
		FileUrl = fileUrl;
		ID = iD;
		Mes = mes;
	}

	public int getBlockCount() {
		return BlockCount;
	}

	public void setBlockCount(int blockCount) {
		BlockCount = blockCount;
	}

	public int getBlockIndex() {
		return BlockIndex;
	}

	public void setBlockIndex(int blockIndex) {
		BlockIndex = blockIndex;
	}

	public String getBlockMd5() {
		return BlockMd5;
	}

	public void setBlockMd5(String blockMd5) {
		BlockMd5 = blockMd5;
	}

	public int getBlockSize() {
		return BlockSize;
	}

	public void setBlockSize(int blockSize) {
		BlockSize = blockSize;
	}

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}

	public String getFileUrl() {
		return FileUrl;
	}

	public void setFileUrl(String fileUrl) {
		FileUrl = fileUrl;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getMes() {
		return Mes;
	}

	public void setMes(String mes) {
		Mes = mes;
	}

}
