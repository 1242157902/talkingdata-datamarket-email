package com.talkingdata.utils;

import com.tenddata.mailbean.Mail;
import com.tenddata.mailclient.MailClient;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xu on 14-10-22.
 */
public class MailSender {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private static int retry_times = 3;
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public FreeMarkerConfigurer getFreeMarkerConfigurer() {
        return freeMarkerConfigurer;
    }

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    public String send(String sendTo, String subject, Map<String, Object> resultMap)throws Exception {
        String content = getMailText(resultMap, "report.ftl");
        Mail mail = new Mail();
        mail.setSendto(sendTo);
        mail.setSubject(subject);
        mail.setContent(content);
        MailClient mailclient = new MailClient();
        boolean isSend = true;
        int retry = 0;
        String info = null;
        String currentPath= this.getClass().getResource("/").getFile().toString();
        String url = PropertiesUtil.getProperties("emailUrl", currentPath + "/config.properties");
        if(!CommonUtils.isEmpty(url))
        {
            while (isSend) {
                info = mailclient.syncSend(url, mail);
                logger.info("mail sender result: " + info);
                System.out.println("mail sender result: " + info);
                if (!info.equals("succ")) {
                    retry ++;
                    if (retry >= retry_times) {
                        isSend = false;
                    }else {
                        logger.info("mail send fail, retry now ...");
                        System.err.println("mail send fail, retry now ...");
                    }
                }else{
                    isSend = false;
                }
            }
        }
        return info;
    }

    public String getMailText(Map<String, Object> data, String templateName)throws Exception {
        String htmlText = "";
            // 通过指定模板名获取FreeMarker模板实例
            Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
            htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl,
                    data);
        return htmlText;
    }



    public static void main(String[] args)throws Exception{
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        MailSender sender = (MailSender)context.getBean("mailSender");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("start", "20141001");
        resultMap.put("end", "20141007");
        resultMap.put("total_account",356);
        String str = sender.send("shuangliang.yang@tendcloud.com", "DataMarket 每周业务数据" + 20141001 + "~" + 20141007, resultMap);
        System.out.println(str);
    }
}
