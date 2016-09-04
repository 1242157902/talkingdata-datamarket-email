package com.talkingdata.java;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;
import com.talkingdata.domain.Shrio;
import com.talkingdata.domain.ShrioView;
import com.talkingdata.domain.WeekReport;
import com.talkingdata.service.WeekReportService;
import com.talkingdata.utils.*;
import oam.weixin.sendsessage.SendMessage;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * User：    ysl
 * Date:   2016/7/28
 * Time:   12:00
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final WeekReportService wrs = new WeekReportService();
    static String phones = "15650725379";

    static{
            ScheduledExecutorService excutor = Executors.newSingleThreadScheduledExecutor();
            long oneDay = 24 * 60 * 60 * 1000;
             long initDelay  =  DateUtils.getTimeMillis("12:00:00") - System.currentTimeMillis();
             logger.info(" initDelay:"+initDelay);
            initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
        excutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    try{
                        Main mainClazz = new Main();
                        mainClazz.sendDayEmail();
                    }catch (Exception e){
                        logger.error("RuntimeException catched,can run next");
                    }
                }
            }, initDelay, oneDay, TimeUnit.MILLISECONDS);
    }
    /**
     * 对应gatewaylog:
     *          发送gateway对应的每天的各个服务调用情况
     */
    public   void  sendDayEmail()
    {
        //获得当前的时间
        String endTime = DateUtils.dateToLongStr(new Date());
        String startTime = DateUtils.millionsToStrDate(DateUtils.getBeforeDay(endTime, 1));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("start",startTime);
        resultMap.put("end", endTime);
        try {
            List<WeekReport> weekReportList = wrs.getResult("");
            System.out.println(weekReportList.size());
            if(weekReportList!=null&&weekReportList.size()>0)
            {
                resultMap.put("weekReportList",weekReportList);
            }else{
                resultMap.put("weekReportList",null);
            }
            //使用freemarker
            ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
            MailSender sender = (MailSender) ac.getBean("mailSender");
            String[] emails = getEmails("emails");
            if (emails!=null&&emails.length>0)
            {
                for(String email:emails)
                {
                   sender.send(email, "DataMarket 每天业务数据" + startTime + "~" + endTime, resultMap); // send mail
                    logger.info("DataMarket every day  data send to :"+email+" time:"+ startTime + "~" + endTime+" send success ！");
                }
            }else
            {
                logger.info(" email is null ");
            }
        } catch (Exception e) {
            System.out.print("Exception");
            SendMessage.Send(phones, "analytics weekly report error: " + e.getMessage());
            logger.error("Exception caught while sending email ", e);
        }
    }

    /**
     * 发送一个鉴权访问的失败率
     */
    public  void sendShrioEmail()
    {
        //获得当前的时间
        String endTime = DateUtils.dateToLongStr(new Date());
        String startTime = DateUtils.millionsToStrDate(DateUtils.getBeforeDay(endTime, 1));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("start",startTime);
        resultMap.put("end", endTime);
        Shrio shrio = null;
        try {
            String currentPath= this.getClass().getResource("/").getFile().toString();
            String url = PropertiesUtil.getProperties("shrioReportUrl", currentPath + "/config.properties");
            if(!CommonUtils.isEmpty(url))
            {
                String s= HttpRequest.sendGet(url, "startTime=" +"");
                logger.info(" the getting result is :"+s);
                Gson gson = new Gson();
                ShrioView shrioView = gson.fromJson(s, new TypeToken<ShrioView>() {}.getType());
                shrio = shrioView.getData();
            }
            resultMap.put("shrio",shrio);
            if(shrio!=null&&shrio.getAccessRequest()!=0)
            {
                double faileRates = (double)shrio.getSuccessRequest()/shrio.getAccessRequest();
                String faileRate = (1-faileRates)*100+"";
                logger.info(" shrio  faileRate is :"+faileRate);
                resultMap.put("failRate",faileRate.substring(0,4)+"%");
            }else{
                resultMap.put("failRate",0);
            }
            //使用freemarker
            ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
            MailSenderGeneral sender = (MailSenderGeneral) ac.getBean("mailGeneralSender");
            String[] emails = getEmails("shrioEmails");
            logger.info(" emails:"+emails);
            if (emails!=null&&emails.length>0)
            {
                for(String email:emails)
                {
                    sender.send(email, "DataMarket 每天业务数据" + startTime + "~" + endTime, resultMap,"shrioreport.ftl"); // send mail
                    logger.info("DataMarket every day  data send to :"+email+" time:"+ startTime + "~" + endTime+" send success ！");
                }
            }else
            {
                logger.info(" email is null ");
            }
        } catch (Exception e) {
            SendMessage.Send(phones, "analytics weekly report error: " + e.getMessage());
            logger.error("Exception caught while sending shrioemail ", e);
        }
    }

    /**
     * 获取所有的emials地址
     * @return
     */
    public   String[] getEmails(String value)
    {
        logger.info(" emailsParameter:" + value);
        if(!StringUtils.isNullOrEmpty(value))
        {
            String currentPath= this.getClass().getResource("/").getFile().toString();
            try {
                String emails = PropertiesUtil.getProperties(value, currentPath + "/config.properties");
                logger.info(" emails:"+emails);
                if(!CommonUtils.isEmpty(emails))
                {
                    return emails.split(",");
                }else
                {
                    return null;
                }
            } catch (IOException e) {
                logger.error("Exception caught while geting emails from config.properties ", e);
                return  null;
            }
        }else
        {
            return null;
        }
    }

    public static void main(String[] args)
    {
        Main mainClazz = new Main();
        mainClazz.sendShrioEmail();
    }

}
