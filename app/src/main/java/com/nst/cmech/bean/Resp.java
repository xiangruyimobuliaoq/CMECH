package com.nst.cmech.bean;

/**
 * 创建者     彭龙
 * 创建时间   2018/8/27 下午2:24
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class Resp<T> {
    public String message;
    public T data;
    public int status;
    public String emessage;
}
