package com.xhb.onlystar.bean;

import java.util.List;

public class GetUploadFileUrlReback {
	    public int Code;//消息代码0成功，非0失败
	    public int CountPage;//总页数
	    public int CurrentPage;//当前页号
	    public String Mes;//消息
	    public List<FileUrl> Result;//数据结果
	    public int SizePerPage;//每一页最大行数
	    
		public GetUploadFileUrlReback(int code, int countPage, int currentPage, String mes, List<FileUrl> result,
				int sizePerPage) {			
			Code = code;
			CountPage = countPage;
			CurrentPage = currentPage;
			Mes = mes;
			Result = result;
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

		public List<FileUrl> getResult() {
			return Result;
		}

		public void setResult(List<FileUrl> result) {
			Result = result;
		}

		public int getSizePerPage() {
			return SizePerPage;
		}

		public void setSizePerPage(int sizePerPage) {
			SizePerPage = sizePerPage;
		}
		
		
	    
	    
}
