package com.rehoshi.simple.business.auth;

/**
 * Created by hoshino on 2019/2/14.
 * 权限管理器
 */

public abstract class AuthManager<User, Model> {

    /**
     * 添加权限
     */
    public abstract boolean hasAddAuth(User user, Model model);

    /**
     * 修改权限
     */
    public abstract boolean hasDeleteAuth(User user, Model model);

    /**
     * 删除权限
     */
    public abstract boolean hasUpdateAuth(User user, Model model);

    public boolean hasQueryAuth(User user) {
        return true;
    }
}
