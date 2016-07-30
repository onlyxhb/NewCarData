package com.xhb.onlystar.bean;

import java.util.List;

/**
 * Created by onlystar on 2016/4/29.
 */
public class AppResultOfMxb {
    public int Code;//消息代码0成功，非0失败
    public int CountPage;//总页数
    public int CurrentPage;//当前页号
    public String Mes;//消息
    public List<Rwmxb> Result;//数据结果
    public int SizePerPage;//每一页最大行数

    public AppResultOfMxb() {
    }

    public AppResultOfMxb(int code, int countPage, int currentPage, String mes, List<Rwmxb> result, int sizePerPage) {
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

    public int getCountPage() {
        return CountPage;
    }

    public int getCurrentPage() {
        return CurrentPage;
    }

    public String getMes() {
        return Mes;
    }

    public List<Rwmxb> getResult() {
        return Result;
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

    public void setResult(List<Rwmxb> result) {
        Result = result;
    }

    public void setSizePerPage(int sizePerPage) {
        SizePerPage = sizePerPage;
    }
}
