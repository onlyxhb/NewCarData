package com.xhb.onlystar.bean;

import java.util.List;

public class LoginReback {
	public int Code;// 消息代码0成功，非0失败
	public int CountPage;// 总页数
	public int CurrentPage;// 当前页号
	public String Mes;// 消息
	public List<User> Result;
	public int SizePerPage;// 每一页最大行数

	public LoginReback(int code, int countPage, int currentPage, String mes, List<User> Result, int sizePerPage) {
		super();
		Code = code;
		CountPage = countPage;
		CurrentPage = currentPage;
		Mes = mes;
		this.Result = Result;
		SizePerPage = sizePerPage;
	}

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}

	public int getCountPage() {
		return CountPage;
	}

	public void setCountPage(int countPage) {
		CountPage = countPage;
	}

	public int getCurrentPage() {
		return CurrentPage;
	}

	public void setCurrentPage(int currentPage) {
		CurrentPage = currentPage;
	}

	public String getMes() {
		return Mes;
	}

	public void setMes(String mes) {
		Mes = mes;
	}

	public List<User> getResult() {
		return Result;
	}

	public void setResult(List<User> Result) {
		this.Result = Result;
	}

	public int getSizePerPage() {
		return SizePerPage;
	}

	public void setSizePerPage(int sizePerPage) {
		SizePerPage = sizePerPage;
	}

}
