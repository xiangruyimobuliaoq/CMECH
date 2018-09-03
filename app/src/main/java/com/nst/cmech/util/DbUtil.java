package com.nst.cmech.util;

import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/27 上午10:51
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class DbUtil {

    private static LiteOrm liteOrmSingle;
    private static LiteOrm liteOrmCascade;

    public static void init(Context context) {
        if (liteOrmSingle == null) {
            liteOrmSingle = LiteOrm.newSingleInstance(context, "cmech.db");
        }
        if (liteOrmCascade == null) {
            liteOrmCascade = LiteOrm.newCascadeInstance(context, "cmech.db");
        }
        liteOrmSingle.setDebugged(true); // open the log
        liteOrmCascade.setDebugged(true); // open the log
    }

    public static LiteOrm single() {
        return liteOrmSingle;
    }

    public static LiteOrm cascade() {
        return liteOrmCascade;
    }

}
