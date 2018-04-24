package org.yaukie.rmi.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class StringUtil extends  StringUtils {
	

	/**
	 * 驼峰转成下划线规则
	 * 必须是[a-z][A-Z]这种风格
	 * @param str
	 * @return
	 */
	public static String camel2underline(String str )
	{
		if(isEmpty(str))
		{
			return "";
		}
		StringBuilder sb = new StringBuilder(str);
		String regex = "[A-Z]";//aaaAddd
		Matcher matcher = Pattern.compile(regex).matcher(str);
		for(int i=0; matcher.find();i++)
		{
			System.out.println("start+i :"+(matcher.start()+i)+" end+i:"+(matcher.end()+i)+" group:"+matcher.group());
			sb.replace(matcher.start()+i, matcher.end()+i, "_"+matcher.group().toLowerCase());
		}
		
		//如果替换之后,首字母就是_,则删除
		if(sb.charAt(0)=='_')
		{
			sb.deleteCharAt(0);
		}
		
		return sb.toString();
	}
	
	/**
	 * 下划线规则转成驼峰
	 * 必须是[a-z]_[a-z]这种风格
	 * @param str
	 * @return
	 */
	public static String underline2camel(String str)
	{
		if(isEmpty(str))
		{
			return "";
		}
		StringBuilder sb = new StringBuilder(str);
		String regex = "_[a-z]";//aaa_dd_af_fd
		Matcher matcher = Pattern.compile(regex).matcher(str);
		for(int i=0; matcher.find();i++)
		{
			sb.replace(matcher.start()-i, matcher.end()-i, matcher.group().substring(1).toUpperCase());
		}
		
		if(Pattern.compile("[A-Z]").matcher(String.valueOf(sb.charAt(0))).find())
		{
			sb.replace(0, 1, sb.substring(0,1).toLowerCase());
		}
//		
//		if(Character.isUpperCase(sb.charAt(0)))
//		{
//			sb.replace(0, 1, String.valueOf(Character.toLowerCase(sb.charAt(0))));
//		}
		
		return sb.toString();
	}
	
	
	/**
	 * 判断该值 是否包括0
	 * @param value
	 * @return
	 */
	public static boolean isContainsZero(String value)
	{
		return "".equals(value) || "0".equals(value) || "0.00".equals(value);
	}
	
	public static boolean compareTo(String old,String news)
	{
		if(old !=null && news !=null )
		{
			if(old.equals(news))
			{
				return true;
			}
		}
		return false;
	}
	
}
