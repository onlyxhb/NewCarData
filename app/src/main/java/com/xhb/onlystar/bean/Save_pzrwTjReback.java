package com.xhb.onlystar.bean;

public class Save_pzrwTjReback {
	private String Code;
	private String CountPage;
	private String CurrentPage;
	private String Mes;
	private String Result;
	private String SizePerPage;

	public Save_pzrwTjReback(String code, String countPage, String currentPage, String mes, String result,
			String sizePerPage) {
		super();
		Code = code;
		CountPage = countPage;
		CurrentPage = currentPage;
		Mes = mes;
		Result = result;
		SizePerPage = sizePerPage;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getCountPage() {
		return CountPage;
	}

	public void setCountPage(String countPage) {
		CountPage = countPage;
	}

	public String getCurrentPage() {
		return CurrentPage;
	}

	public void setCurrentPage(String currentPage) {
		CurrentPage = currentPage;
	}

	public String getMes() {
		return Mes;
	}

	public void setMes(String mes) {
		Mes = mes;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getSizePerPage() {
		return SizePerPage;
	}

	public void setSizePerPage(String sizePerPage) {
		SizePerPage = sizePerPage;
	}

}
