package com.xhb.onlystar.bean;

import java.util.List;

public class GetRwList {
    private List<String> queryCondition;
    private String RequestTime;
    private String TimeMd5;

   public  GetRwList(List<String> queryCondition, String RequestTime, String TimeMd5) {
        this.queryCondition = queryCondition;
        this.RequestTime = RequestTime;
        this.TimeMd5 = TimeMd5;
    }

    public List<String> getQueryCondition() {
        return queryCondition;
    }

    public String getRequestTime() {
        return RequestTime;
    }

    public String getTimeMd5() {
        return TimeMd5;
    }

    public void setQueryCondition(List<String> queryCondition) {
        this.queryCondition = queryCondition;
    }

    public void setRequestTime(String requestTime) {
        RequestTime = requestTime;
    }

    public void setTimeMd5(String timeMd5) {
        TimeMd5 = timeMd5;
    }
}
