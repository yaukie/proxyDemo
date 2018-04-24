package org.yaukie.rmi.server.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理实现
 * @author yaukie
 * 注意该实现是基于类的接口,也就是必须得有接口才能实现代理
 */
public class CatJdkImplProxy implements InvocationHandler {
	//代理目标
	private Object target ;
	
	public CatJdkImplProxy(Object target){
		this.target=target;
	}
	
	public static CatJdkImplProxy instance =null;
 
	//重构,,直接返回代理类
	@SuppressWarnings("unchecked")
	public <T> T getProxy()
	{
		return (T)Proxy.newProxyInstance(
				target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(),
				this
				);
	}
	
	
	//方法拦截
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		String methodName = method.getName();
			if(methodName.equals("run"))
			{
					for(Object obj : args){
						System.err.println(obj.toString()+" 被拦截了 惨了!");
					}
				return null;
			}
		return method.invoke(proxy, args);
	}

}
