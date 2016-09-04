package com.talkingdata.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.talkingdata.domain.WeekReport;
import com.talkingdata.utils.CommonUtils;
import com.talkingdata.utils.HttpRequest;
import com.talkingdata.utils.PropertiesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User：    ysl
 * Date:   2016/7/29
 * Time:   14:40
 */
public class WeekReportService {

    public List<WeekReport> getResult(String endTime)throws Exception
    {
        //发送 GET 请求
        /*String s= HttpRequest.sendGet("http://localhost:8080/api/report/getDayReport",
                "startTime="+endTime);*/
        String currentPath= this.getClass().getResource("/").getFile().toString();
        String url = PropertiesUtil.getProperties("reportUrl", currentPath + "/config.properties");
        if(!CommonUtils.isEmpty(url))
        {
            String s= HttpRequest.sendGet(url,"startTime="+endTime);
            Gson gson = new Gson();
            List<WeekReport> weekReportList = gson.fromJson(s, new TypeToken<List<WeekReport>>() {}.getType());
            return weekReportList;
        }else
        {

            return null;
        }
    }

}
