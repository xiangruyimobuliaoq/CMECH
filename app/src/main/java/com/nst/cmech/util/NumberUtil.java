package com.nst.cmech.util;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

/**
 * 创建者     彭龙
 * 创建时间   2018/7/21 上午9:18
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class NumberUtil {
    public static Random random = new Random();

    public static void splitRedPacket(double total, int count) {
        ArrayList<Double> list = new ArrayList<>();
        BigDecimal btotal = new BigDecimal(total);
        BigDecimal hongbao;
        do {
            hongbao = new BigDecimal(random.nextDouble()).multiply(btotal).setScale(2, RoundingMode.HALF_UP);
        }
        while (hongbao.setScale(2, RoundingMode.HALF_UP).compareTo(new BigDecimal(0)) == -1 || hongbao.compareTo(new BigDecimal(0)) == 0 || hongbao.setScale(2, RoundingMode.HALF_UP).compareTo(btotal.divide(new BigDecimal(3), 2, RoundingMode.HALF_UP)) == 1);
        btotal = btotal.subtract(hongbao);
        list.add(hongbao.doubleValue());
        for (int i = 0; i < count - 1; i++) {
            if (list.size() < count - 1) {
                do {
                    hongbao = new BigDecimal(random.nextDouble()).multiply(btotal).setScale(2, RoundingMode.HALF_UP);
                }
                while (hongbao.compareTo(new BigDecimal(0)) == -1 || hongbao.compareTo(new BigDecimal(0)) == 0 || hongbao.compareTo(btotal.divide(new BigDecimal(3), 2, RoundingMode.HALF_UP)) == 1);
                btotal = btotal.subtract(hongbao);
                list.add(hongbao.doubleValue());
            } else {
                list.add(btotal.doubleValue());
            }
        }
        Log.e("list", list.toString());
        BigDecimal sum = new BigDecimal(0);
        for (int j = 0; j < list.size(); j++) {
            sum = sum.add(new BigDecimal(list.get(j)));
        }
        Log.e("sum", sum.doubleValue() + "");
    }
}
