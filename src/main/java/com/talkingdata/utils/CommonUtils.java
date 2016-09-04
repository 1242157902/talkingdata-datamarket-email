package com.talkingdata.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 经常使用得工具类
 * User：    ysl
 * Date:   2016/7/29
 * Time:   14:19
 */
public class CommonUtils {

    private static final Logger  logger = LoggerFactory.getLogger(CommonUtils.class);


    /**
     * 将json 转换为javaBean
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            System.out.println("程序发生异常");
            logger.error("json to Bean ,error ",e);
        }
        return t;
    }
    public static List<Map<String, Object>> jsonToListMap(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString,
                    new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
        } catch (Exception e) {
            System.out.println("程序发生异常");
            logger.error("json to List<Map<String,Objcet>> ,error ",e);
        }
        return list;
    }


    /**
     * 判断该字符串是否为空，是的话，返回true，否则返回false
     * @param str       :字符串
     * @return
     */
    public static boolean isEmpty(String str)
    {
        if(str!=null&&!"".equals(str))
        {
            return false;
        }else{
            return true;
        }
    }
}
