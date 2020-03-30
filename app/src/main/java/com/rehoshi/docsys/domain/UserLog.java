package com.rehoshi.docsys.domain;

import java.util.Date;

public class UserLog {
    /**
     * 操作ID
     */
    private String id ;
    /**
     * 用户ID
     */
    private String userId ;
    private User user ;

    /**
     * 查询内容
     */
    private String searchContent ;

    /**
     * 查询时间
     */
    private Date searchTime ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public Date getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(Date searchTime) {
        this.searchTime = searchTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
