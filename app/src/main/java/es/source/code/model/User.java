package es.source.code.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private List<OrderItem> orderList; // 用户的订单列表

    public User(){
    }

    public User(String userName,String password){
        this.userName = userName;
        this.password = password;
        oldUser = true;
        orderList = new ArrayList<OrderItem>();
    }

    public User(String userName,String password,boolean oldUser){
        this.userName = userName;
        this.password = password;
        this.oldUser = oldUser;
        orderList = new ArrayList<OrderItem>();
    }

    public User(String userName,String password,boolean oldUser,List<OrderItem> orderList){
        this.userName = userName;
        this.password = password;
        this.oldUser = oldUser;
        this.orderList = orderList;
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

    public List<OrderItem> getOrderList(){
        return orderList;
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

    public void setOrderList(List<OrderItem> orderList){
        this.orderList = orderList;
    }
}
