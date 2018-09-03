package com.nst.cmech.util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/20 下午2:51
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ConsUtil {
    public static final String USERNAME = "username";
    public static final String IMG = "img";
    private static final String ISLOGIN = "islogin";
    private static final String STYLE = "style";
    public static final String DATACLASS = "dataclass";
    public static final String COMPANY = "company";
    public static String USERINFO = "userinfo";
    public static String CLIENTID = "clientid";


    public static String getID() {
        return SpUtil.getString(ConsUtil.USERINFO, ConsUtil.CLIENTID, null);
    }
    public static String getCompany() {
        return SpUtil.getString(ConsUtil.USERINFO, ConsUtil.COMPANY, null);
    }

    public static void cleanID() {
        SpUtil.putString(ConsUtil.USERINFO, ConsUtil.CLIENTID, null);
    }

    public static void setLogin(boolean islogin) {
        SpUtil.putBoolean(ConsUtil.USERINFO, ConsUtil.ISLOGIN, islogin);
    }

    public static boolean getLogin() {
        return SpUtil.getBoolean(ConsUtil.USERINFO, ConsUtil.ISLOGIN, false);
    }

    public static void setStyle(int s) {
        SpUtil.putInt(ConsUtil.USERINFO, ConsUtil.STYLE, s);
    }

    public static int getStyle() {
        return SpUtil.getInt(ConsUtil.USERINFO, ConsUtil.STYLE, 1);
    }

    public static int getIMG() {
        return SpUtil.getInt(ConsUtil.USERINFO, ConsUtil.IMG, 0);
    }

    public static String getUsername() {
        return SpUtil.getString(ConsUtil.USERINFO, ConsUtil.USERNAME, null);
    }

    public static void cleanUsername() {
        SpUtil.getString(ConsUtil.USERINFO, ConsUtil.USERNAME, null);
    }

    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    private static final String[][] MIME_MapTable = {
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public static String getMIMEType(String fName) {

        String type = "*/*";
//获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
//在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    public static Locale getLanguage() {
        int i = SpUtil.getInt("language", "language", 0);
        if (i == 1 || i == 0) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (i == 2) {
            return Locale.ENGLISH;
        }
        return Locale.SIMPLIFIED_CHINESE;
    }

    public static void setLanguage(int i) {
        SpUtil.putInt("language", "language", i);
    }
}
