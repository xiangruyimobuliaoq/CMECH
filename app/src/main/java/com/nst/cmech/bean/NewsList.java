package com.nst.cmech.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/28 下午9:03
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class NewsList {


    public List<News> news = new ArrayList<>();

    public static class News {
        /**
         * createtime : 1535458923000
         * details : hosts哈佛附近拍摄&nbsp;
         * id : 39
         * picture : de1d12ac965c41f49d54d80f4e254304.jpg
         * state : 1
         * title : Sam
         */

        public long createtime;
        public String details;
        public int id;
        public String picture;
        public String state;
        public String title;
    }
}
