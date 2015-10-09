package com.xwkj.common.util;

import java.util.Random;
import java.util.regex.Pattern;

public class MathTool 
{
	public static double EPS = 1e-10;  // 精度范围

	/**
	 * @param need
	 * @param range
	 * @return
	 */
	public static int [] getRandom(int need,int range) {
		int [] rands=new int[need];
		Random random=new Random ();  
        boolean[] bool=new boolean[range];
        int randInt=0;  
        for(int i=0;i<need;i++)
        {
        	do
        		randInt=random.nextInt(range);  
        	while(bool[randInt]);
        	bool[randInt]=true;  
        	rands[i]=randInt;
        }
        return rands;
	}
	
	/**
	 * 获取随机数
	 * @param range
	 * @return
	 */
	public static int getRandom(int range) {
		return getRandom(1, range)[0];
	}
	
	public static String getRandomStr(int length) {
		int [] randoms=getRandom(length, 9);
		String str="";
		for(int i : randoms)
			str+=i;
		return str;
	}
	
	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) { 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 

	/**
	 * 从字符串中提取数字字符
	 * @param str
	 * @return
	 */
	public static String getNumStr(String str) {
		return str.replaceAll("[^\\d]","");
	}
	
	/**
	 * @param num
	 * @return
	 */
	public static boolean isInteger(double num) {
		if(num- (double)((int)num)<EPS)
			return true;
		return false;
	}
}
