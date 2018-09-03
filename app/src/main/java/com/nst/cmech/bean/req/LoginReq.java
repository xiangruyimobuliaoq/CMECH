package com.nst.cmech.bean.req;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/27 上午11:56
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LoginReq {
    public String username;
    public String password;

    public double latitude;
    public double longitude;

    public LoginReq(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginReq(String username, String password, double latitude, double longitude) {
        this.username = username;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;

    }

}
