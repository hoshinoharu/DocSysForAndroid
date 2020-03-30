package com.rehoshi.docsys.domain;

import android.content.Context;

import com.rehoshi.docsys.control.Launcher;
import com.rehoshi.simple.business.vo.SPAble;

import java.util.Date;

public class User extends SPAble {
    /**
     * 用户角色
     */
    public interface Role{
        String ADMIN = "admin" ;
        String USER = "user" ;
    }

    private String id;//id
    private String account;//account
    private String name;//nick_name
    private String password;//password
    private String token ;
    private String description = "这个人很懒什么都没留下";
    private String role ;
    private Date createTime ;

    public static User getCurUser() {
        return get(User.class, Launcher.getInstance().getTopActivity());
    }

    public static void saveChange(User data) {
        change(data, Launcher.getInstance().getTopActivity());
    }

    public static User getCurUser(Context context) {
        return getCurUser();
    }


    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getAccount(){
        return account;
    }
    public void setAccount(String account){
        this.account = account;
    }

    public String getName(){
        if(name == null){
            name = getAccount();
        }
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void logout(Context context) {
        change(new User(), Launcher.getInstance().getTopActivity());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isAdmin(){
        return Role.ADMIN.equals(this.getRole()) ;
    }
}

