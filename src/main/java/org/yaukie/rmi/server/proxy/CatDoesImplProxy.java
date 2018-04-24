package org.yaukie.rmi.server.proxy;

import org.yaukie.rmi.server.impl.CatDoesImpl;
import org.yaukie.rmi.server.inter.ICat;

/**
 * 搞一个静态代理类
 * @author yaukie
 *用来代理ICat的接口实现
 *静态代理缺点是,一个实现对应一个代理
 *接口变化了,那么实现也跟着变化,代理也要跟着变
 */
public class CatDoesImplProxy implements ICat{
	
	//代理的目标对象,其实是ICat的某个实现类
	private CatDoesImpl catDoesImpl;

	private void before()
	{
		System.out.println("静态代理 CatDoesImplProxy before ");
	}
	
	public void run(String name) {
		before();
		System.out.println("cat "+ name +"run so faster !");
		after();
	}
	
	private void after()
	{
		System.out.println("静态代理 CatDoesImplProxy after  ");
	}
	
}
