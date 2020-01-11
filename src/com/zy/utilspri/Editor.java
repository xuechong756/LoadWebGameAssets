package com.zy.utilspri;

public class Editor {

	private static final String t = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	private static final int[] r = new int[128];

	static {
		for (int i = 0; i < 64; ++i) {
			r[t.codePointAt(i)] = i;
		}
	}
	
	public static class Utils{
		
		public static class UuidUtils{
			
			public static String decompressUuid(String hash){
				 if (23 == hash.length()) {
		             String t = "";
		             for (int i = 5; i < 23; i += 2) {
		                 int n = r[hash.codePointAt(i)];
		                 int s = r[hash.codePointAt(i + 1)];
		                 t += Integer.toHexString(n >> 2);
		                 t += Integer.toHexString((3 & n) << 2 | s >> 4);
		                 t += Integer.toHexString(15 & s);
		             }
		             hash = hash.substring(0, 5) + t;
				 } else {
		             if (22 != hash.length()) {
		            	 return hash;
		             }
		             String t = "";
		             for (int i = 2; i < 22; i += 2) {
		                 int n = r[hash.codePointAt(i)];
		                 int s = r[hash.codePointAt(i + 1)];
		                 t += Integer.toHexString((n >> 2));
		                 t += Integer.toHexString(((3 & n) << 2 | s >> 4));
		                 t += Integer.toHexString((15 & s));
		             }
		             hash = hash.substring(0, 2) + t;
		         }
		        return hash.substring(0, 8) + "-" + hash.substring(8, 12) + "-" +hash.substring(12, 16) + "-" + hash.substring(16, 20) + "-" + hash.substring(20);
			} 
		}
	}

	public static void main(String[] a) {
		String str = Editor.Utils.UuidUtils.decompressUuid("");
		System.out.println(str);
	}
}
