package com.rehoshi.docsys.util;

import com.rehoshi.simple.utils.FormatUtil;
import com.rehoshi.simple.utils.StringUtil;

/**
 * Created by hoshino on 2019/3/18.
 */

public class InputValidator {


    public static boolean validateTextLength(String name, String text, int minLength, int length, StringBuilder error) {
        boolean flag = true;
        if (name == null){
            name = "" ;
        }
        if (StringUtil.isNullOrEmpty(text) && text.length() < minLength) {
            flag = false;
            error.append(FormatUtil.formatString("请输入%s",name));
        } else if (text.length() > length) {
            flag = false;
            error.append(FormatUtil.formatString("%s长度不能超过%d位", name, length));
        } else if(text.length() < minLength){
            flag =false ;
            error.append(FormatUtil.formatString("%s长度不能小于%d位", name, minLength));
        }
        return flag;
    }

    public static boolean validateStoreName(String storeName, StringBuilder error) {
        return validateTextLength("店铺名", storeName,1, 10, error);
    }


    public static boolean validateUserName(String userName, StringBuilder error) {
        return validateTextLength("用户名", userName,1, 10, error);
    }

    public static boolean validateUserName(String nameTag, String userName, StringBuilder error) {
        return validateTextLength(nameTag, userName,1, 10, error);
    }

    public static boolean validatePassword(String password, StringBuilder error) {
        if(password.startsWith(" ") || password.endsWith(" ")){
            error.append("密码前后不能输入空格") ;
            return false ;
        }else {
            return validateTextLength("密码", password, 6, 10, error);
        }
    }

    public static boolean validatePhone(String phone, StringBuilder error) {
        return validatePhone("手机号码", phone, error) ;
    }

    public final static String PHONE_REGEX = "1[3456789][0-9]{9}" ;

    public static boolean validatePhone(String phoneTag, String phone, StringBuilder error) {
        boolean flag = true;
        if (StringUtil.isNullOrEmpty(phone)) {
            error.append(phoneTag).append("不能为空");
            flag = false;
        } else if (!phone.matches(PHONE_REGEX)) {
            error.append(phoneTag).append("格式输入错误");
            flag = false;
        }
        return flag;
    }

}
