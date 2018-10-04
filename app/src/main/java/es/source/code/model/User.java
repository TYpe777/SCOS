package es.source.code.model;

import java.io.Serializable;

/**
 * @author taoye
 * @date 2018/10/2
 * @classname User.java
 * @description 用户类
 */
public class User implements Serializable{
    private String userName; // 用户名
    private String password; // 用户密码
    private boolean oldUser; // 是否是老用户

    public User(){
    }

    public User(String userName,String password){
        this.userName = userName;
        this.password = password;
        oldUser = true;
    }

    public User(String userName,String password,boolean oldUser){
        this.userName = userName;
        this.password = password;
        this.oldUser = oldUser;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public boolean isOldUser(){
        return oldUser;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setOldUser(boolean oldUser){
        this.oldUser = oldUser;
    }
}
