package org.yaukie.rmi.server.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaukie.rmi.server.User;

/**
 * 通过java 反射 
 * 来修改bean的更新属性值
 * @author yaukie
 */
public class BeanUtil {

	private static final  Logger log = LoggerFactory.getLogger(BeanUtil.class);
	
	/**
	 * 更新比较实体类
	 * @param <T>
	 * @param source
	 * @param target
	 */
	public static <T> T getBeanEntity(Object source ,Object target ,boolean ignoreZero)
	throws  Exception
	{
		Map<String,String> valueMap = beanToMap(source);
		return setBeanValue(target.getClass(), valueMap, ignoreZero);
	}
	
	/**
	 * 更新比较实体类
	 * @param source
	 * @param target
	 */
	public static void updateEntityBean(Object source ,Object target )
	throws  Exception
	{
		Map<String,String> valueMap = beanToMap(source);
		updateNullFieldValue(target, valueMap);
	}
	
	private static <T> T getBean(Object target ,Class<?> cls ,boolean isIngoreZero)
	throws Exception
	{
		Map<String,String> valueMap = beanToMap(target);
		return setBeanValue(cls, valueMap, isIngoreZero);
	}
	
	private static boolean updateBean(Object target ,Object obj )
	throws Exception
	{
		Map<String,String> valueMap =  beanToMap(obj);
		return updateFieldValue(target, valueMap);
	}
	
	/**
	 * 更新实体属性值,
	 * 将未有更新的属性设置为null
	 * @param target
	 * @param valueMap
	 * @return
	 */
	private static void updateNullFieldValue(Object target ,Map  valueMap)
	throws Exception
	{
		Class<?> cls = target.getClass();
		Field[] fields = cls.getDeclaredFields();
		for(Field field : fields )
		{
			try
			{
				String oldValue = getFieldValue(target, field);
				oldValue = oldValue==null?"":oldValue;
				String newValue = (String) valueMap.get(field.getName());
				newValue=newValue==null?"":newValue;
				//两个值想等,则不进行更新
				if(StringUtil.compareTo(oldValue, newValue))
				{
					setFieldValue(target, field, null);
					log.debug("正在更新属性"+field.getName()+":"+"值一样不更新!");
				}
			} catch(Exception e)
			{
				continue;
			}
		}
	}
	
	/**
	 * 更新实体属性值,
	 * 新老实体比较,将老的实体数据更新到新的实体
	 * @param target
	 * @param valueMap
	 * @return
	 */
	private static boolean updateFieldValue(Object target ,Map  valueMap)
	throws Exception
	{
		boolean flag = false;
		Class<?> cls = target.getClass();
		Field[] fields = cls.getDeclaredFields();
		for(Field field : fields )
		{
			try
			{
				String oldValue = getFieldValue(target, field);
				oldValue = oldValue==null?"":oldValue;
				String newValue = (String) valueMap.get(field.getName());
				newValue=newValue==null?"":newValue;
				//两个值不想等,则进行更新
				if(!StringUtil.compareTo(oldValue, newValue))
				{
					setFieldValue(target, field, newValue);
					flag = true;
				}
			}catch (Exception e)
			{
				continue;
			}
		}
		return flag;
	}
	
	/**
	 * 给某个实体设置属性值
	 * 并且有是否更新zero开关
	 * true 空值也更新
	 * false 空值跳过
	 * @param cls
	 * @param valueMap
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unused")
	private static <T>T setBeanValue(Class<?> cls ,Map valueMap,boolean ignoreZero) 
			throws Exception
	{
		T obj = (T) cls.newInstance();
		Field[] fields = cls.getDeclaredFields();
		for(Field field : fields )
		{
				Object value = valueMap.get(field.getName());
				try
				{
					if( ignoreZero && !StringUtil.isContainsZero(String.valueOf(value)))
					{
						setFieldValue(obj, field, String.valueOf(value));
					}
				}catch(Exception e)
				{
					//全部更新
					continue;
				}
				
		}
		return obj;
	}
	
	
	/**
	 * 将实体的所有属性全部封装成map对象
	 * 以后调用直接通过属性名 获取属性值
	 * @param cls
	 * @param field
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private static Map<String,String> beanToMap(Object target)
	throws Exception
	{
		Map  valueMap = new HashMap();
		Class<?> cls = target.getClass();
		Field[] fields = cls.getDeclaredFields();
		for(Field field :fields )
		{
			String value = getFieldValue(target, field);
			valueMap.put(field.getName(), value);
		}
		return  valueMap;
	}
	
	/**
	 * 获取该属性值
	 * @param target
	 * @param field
	 * @return
	 */
	private static String getFieldValue(Object target,Field field) 
	throws Exception
	{
		String result = null;
		Class<?> cls = target.getClass();
		Method[] methods = cls.getDeclaredMethods();
		String type = field.getType().getSimpleName();
		 if(isContainsGetterMethod(methods, field))
			{
					Method method = cls.getMethod(getterMethodName(field),new Class[]{});
					Object returnVal = method.invoke(target, new Object[]{});
					if("Date".equals(type))
					{
						result = DateUtil.formatDate((Date)returnVal);
					}else
					{
						if(returnVal !=null )
						{
							result = String.valueOf(returnVal);
						}
					}
		 
			} 
		
		return result ;
	}
	
	/**
	 * 为某个属性赋值
	 * @param target
	 * @param field
	 * @throws  
	 * @throws SecurityException 
	 */
	private static void setFieldValue(Object target , Field field ,String value) throws Exception
	{
		Class<?> cls = target.getClass();
		Method[] methods = cls.getDeclaredMethods();
		String type = field.getType().getSimpleName();
		//通过set方法对某个属性赋值
		if(isContainsSetterMethod(methods, field))
		{
				Method method =cls.getMethod(setterMethodName(field), field.getType());
				if(!StringUtil.isEmpty(value))
				{
					
				if("String".equalsIgnoreCase(type))
				{
					method.invoke(target,value);
				}else if(type.equals("Integer") || type.equals("int"))
				{
					int v = Integer.valueOf(value);
					method.invoke(target,v);
				}else if("Float".equalsIgnoreCase(type))
				{
					float v = Float.valueOf(value);
					method.invoke(target,v);
				}else if("Double".equalsIgnoreCase(type))
				{
					double b = Double.valueOf(value);
					method.invoke(target, b);
				}
			}else
			{
				method.invoke(target, value);
			}
		}
		
	}
	
	/**
	 * 判断某个属性是否被指定注解
	 * @param <T>
	 * @param target
	 * @param annotation
	 * @return
	 */
	private static   boolean isAnnotationByWhat(Object target,Field field,Class annotation)
	{
			if(StringUtil.isEmpty(annotation))
			{
				return false;
			}
			Class<?> cls = target.getClass();
			Field[] fields = cls.getDeclaredFields();
			for(Field f : fields)
			{
				if( !f.getName().equals(field.getName()))
				{
					System.out.println(target+"does not contains this field !");
					return false;
				}
			}
			
			Annotation obj =	field.getAnnotation(annotation);
			if( !StringUtil.isEmpty(obj))
			{
				return true;
			}
			return false;
	}
	
	/**
	 * 判断某个属性的get方法是否被指定注解
	 * @param <T>
	 * @param target
	 * @param annotation
	 * @return
	 */
	private static   boolean isGetterAnnotationByWhat(Object target,Field field,Class annotation)
	throws Exception
	{
			if(StringUtil.isEmpty(annotation))
			{
				return false;
			}
			Class<?> cls = target.getClass();
			Method[] methods = cls.getDeclaredMethods();
			//是否包括该getter方法
			if(isContainsGetterMethod(methods, field))
			{
				Method method = cls.getMethod(getterMethodName(field), new Class[]{});
				Annotation obj =	method.getAnnotation(annotation);
				if( !StringUtil.isEmpty(obj))
				{
					return true;
				}
			}
		
			return false;
	}
	
	
	/**
	 * getter 方法
	 * @param field
	 * @return
	 */
	private static String getterMethodName(Field field )
	{
		String fieldName = field.getName();
		return "get"+fieldName.substring(0,1).toUpperCase()
				+fieldName.substring(1);
	}
	/**
	 * setter 方法
	 * @param field
	 * @return
	 */
	private static String setterMethodName(Field field )
	{
		String fieldName = field.getName();
		return "set"+fieldName.substring(0,1).toUpperCase()
				+fieldName.substring(1);
	}
	/**
	 * 判断该属性值是否带有getter方法
	 * @param obj
	 * @param field
	 * @return
	 */
	private static boolean isContainsGetterMethod(Method[] methods , Field field )
	{
		boolean flag = false;
		for(Method method :methods)
		{
			String methodName = method.getName();
			if(methodName.equals(getterMethodName(field)))
			{
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	
	/**
	 * 判断该属性值是否带有setter方法
	 * @param obj
	 * @param field
	 * @return
	 */
	private static boolean isContainsSetterMethod(Method[] methods , Field field )
	{
		boolean flag = false;
		for(Method method :methods)
		{
			String methodName = method.getName();
			if(methodName.equals(setterMethodName(field)))
			{
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	public static void main(String[] args) throws Exception {
		User user = new User();
		user.setAge(99);
		User user2 = new User();
		user2.setAge(99);
	    BeanUtil.updateEntityBean(user2, user);
	    System.out.println(user2.getAge());
	}
	
}
