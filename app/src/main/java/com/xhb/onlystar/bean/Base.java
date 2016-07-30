package com.xhb.onlystar.bean;

/**
 * Created by onlystar on 2016/5/6.
 */
public class Base {
    public int Code;//消息代码0成功，非0失败
    public int CountPage;//总页数
    public int CurrentPage=0;//当前页号
    public String Mes;//消息
    public int SizePerPage;//每一页最大行数

    public Base() {
    }

    public Base(int code, int countPage, int currentPage, String mes, int sizePerPage) {
        Code = code;
        CountPage = countPage;
        CurrentPage = currentPage;
        Mes = mes;
        SizePerPage = sizePerPage;
    }

    public int getCode() {
        return Code;
    }

    public int getCountPage() {
        return CountPage;
    }

    public int getCurrentPage() {
        return CurrentPage;
    }

    public String getMes() {
        return Mes;
    }

    public int getSizePerPage() {
        return SizePerPage;
    }

    public void setCode(int code) {
        Code = code;
    }

    public void setCountPage(int countPage) {
        CountPage = countPage;
    }

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }

    public void setMes(String mes) {
        Mes = mes;
    }

    public void setSizePerPage(int sizePerPage) {
        SizePerPage = sizePerPage;
    }
}
