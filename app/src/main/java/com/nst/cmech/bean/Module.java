package com.nst.cmech.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.annotation.Unique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/22 下午2:34
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class Module {

    public List<DataClass> dataClass = new ArrayList<>();

    @Table("Module")
    public static class DataClass implements Serializable, MultiItemEntity {
        /**
         * ename : 0
         * flag : 2
         * id : 30
         * imageUrl : 1
         * name : 0
         */

        @Ignore
        public String ename;
        @Ignore
        public int itemType;
        @Ignore
        public int flag;

        @Unique
        public int id;
        public String filepath;
        public int version;

        @Ignore
        public int dataType;
        @Ignore
        public String imageUrl;
        @Ignore
        public String name;
        /**
         * dataName :
         * dataType : 39
         * file :
         * fileImage : 331be0d6c5a8413aaca1f07410562363.jpg
         * state : 1
         */

        @Ignore
        public String dataName;
        @Ignore
        public String file;
        @Ignore
        public String fileImage;
        @Ignore
        public int state;

        @Override
        public int getItemType() {
            return itemType;
        }
    }
}
