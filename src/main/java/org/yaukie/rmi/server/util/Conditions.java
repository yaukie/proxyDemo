package org.yaukie.rmi.server.util;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 封装查询条件对象
 * 通过该对象生成查询条件
 * @author yaukie
 *
 */
public class Conditions {
	
	public  List<Condition> conditionList = new ArrayList<Condition>();

	public Conditions condition (String name,String operation ,String value)
	{
		  return append(name,operation,value);
	}
	
	 private Conditions and() {
	        return append("and");
	    }

	 private Conditions or() {
	        return append("or");
	    }

	 private Conditions left() {
	        return append("(");
	    }

	 private Conditions right() {
	        return append(")");
	    }
	
	
	private Conditions append(String name,String operation ,String value)
	{
		Condition condition = new Condition(name,operation,value);
		this.conditionList.add(condition);
		return this;
	}
	
	private Conditions append(String operation){
		return append("",operation,"");
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(this.conditionList.size() > 0 )
		{
			for(Condition condition : conditionList )
			{
				String name = condition.getName();
				String operation = condition.getOperation();
				String value = condition.getValue();
				if(!StringUtil.isEmpty(name))
				{
					sb.append("  ").append(StringUtil.camel2underline(name));
				}
				
				if(!StringUtil.isEmpty(operation))
				{
					sb.append("  ").append(operation.trim());
				}
				
				if(!StringUtil.isEmpty(value))
				{
					 if(value.contains("?"))
					 {
						 sb.append("  ").append(value);
					 }else
					 {
						 sb.append("  ").append("'").append(value).append("'");
					 }
				} 
				
			}
		}
		sb.toString().trim().replaceAll("\\s{2}", "  ");
		return super.toString();
	}
	
	private class Condition {
		private String name;
		private String operation;
		private String value;
		
		public Condition(String name,String operation,String value)
		{
			this.name=name;
			this.operation=operation;
			this.value=value;
		}
		
		public String getName() {
			return name;
		}
		
		public String getOperation() {
			return operation;
		}
		
		public String getValue() {
			return value;
		}
		
	}
	
	public static void main(String[] args) {
		String condition = new Conditions()
        .condition("date", "=", "1")
        .and()
        .left()
        .condition("userName", "=", "2")
        .or()
        .condition("birthDay", "=", "3")
        .right()
        .toString();
			System.out.println(condition);
	}
}
