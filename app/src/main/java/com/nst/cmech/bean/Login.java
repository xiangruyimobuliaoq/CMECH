package com.nst.cmech.bean;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/27 下午12:04
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class Login {
    public String token;

    /**
     * company : xgg
     * createtime : 1535936060000
     * id : 18
     * password : 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
     * permission : 2
     * phone :
     * sex : 男
     * state : 1
     * username : cth
     */
    public User user;

    public class User {

        public String company;
        public long createtime;
        public int id;
        public String password;
        public int permission;
        public String phone;
        public String sex;
        public int state;
        public String username;
    }
}
