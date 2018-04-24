package org.yaukie.rmi.server.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * cglib实现动态代理
 * @author yaukie
 */
public class CatCglibImplProxy implements MethodInterceptor {

	//代理对象
	private Object target;
	
	public static   CatCglibImplProxy instance = null;
	
	public static CatCglibImplProxy getInstance()
	{
		if(instance == null)
		{
			instance = new CatCglibImplProxy();
		}
		return instance;
	}
	
	private void doSth()
	{
		System.out.println("CGLIB 动态代理开始起作用..");
	}
	
	/**
	 * 创建代理对象target
	 * @param <T>
	 * @param target
	 * @return
	 */
	public <T> T getProxy(Object target)
	{
		this.target=target;
		Enhancer hancer = new Enhancer();
		 hancer.setSuperclass(target.getClass().getSuperclass());
		 hancer.setClassLoader(target.getClass().getClassLoader());
		 hancer.setCallback(this);
		return  (T) hancer.create();
	}
	
	/**
	 * 创建代理对象
	 * 另外一种写法
	 * @param cls
	 * @return
	 */
	public <T>T getProxy(Class<T> cls)
	{
		return ((T) Enhancer.create(cls, this));
	}
	/**
	 * 拦截器实现
	 */
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy arg) throws Throwable {
		doSth();
		String methodName = method.getName();
		if(methodName.equals("run"))
		{
				for(Object o : args){
					System.err.println(o.toString()+" 被拦截了 惨了!");
				}
			return null;
		}
		return arg.invokeSuper(obj, args);
	}

}
