package com.zhan.app.sl.util;

import java.util.Random;

public class RandomCodeUtil {
   public static String randomCode(int len){
	   Random ramRandom=new Random();
	   StringBuilder sb=new StringBuilder();
	  for(int i=0;i<len;i++){
		  sb.append(ramRandom.nextInt(10));
	  }
	  return sb.toString();
   }
}
