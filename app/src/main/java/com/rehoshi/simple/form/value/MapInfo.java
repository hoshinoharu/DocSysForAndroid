package com.rehoshi.simple.form.value;

/**
 * Created by hoshino on 2019/3/3.
 */

public class MapInfo {

    public enum Result {
        SUCCESS,
        EMPTY_INPUT,
        FORMAT_ERROR,
    }

    public Result result = Result.FORMAT_ERROR;

    //字段名称
    public String fieldNameTag;

    private String msg;

    public boolean success() {
        return result == Result.SUCCESS;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        if (msg == null) {
            msg = "" ;
            switch (this.result) {
                case FORMAT_ERROR:
                    msg = fieldNameTag + "输入格式错误";
                    break;
                case SUCCESS:
                    msg = "成功";
                    break;
                case EMPTY_INPUT:
                    msg = "请输入" + fieldNameTag;
                    break;
            }
        }
        return msg;
    }

}
