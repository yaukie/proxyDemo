package org.rmi;

import java.lang.reflect.Proxy;

import org.yaukie.rmi.server.impl.CatDoesImpl;
import org.yaukie.rmi.server.inter.ICat;
import org.yaukie.rmi.server.proxy.CatCglibImplProxy;
import org.yaukie.rmi.server.proxy.CatDoesImplProxy;
import org.yaukie.rmi.server.proxy.CatJdkImplProxy;
/**
 * 静态代理类的测试实现
 * @author yaukie
 *
 */
public class Test {
	public static void main(String[] args) {
		//静态代理
//		CatDoesImplProxy proxy = new CatDoesImplProxy();
//		proxy.run("小黑");
		CatDoesImpl impl = new CatDoesImpl();
		CatJdkImplProxy p = new CatJdkImplProxy(impl);
		ICat cat = (ICat)p.getProxy();
		cat.run("小黑猫");
//		ICat cat = (ICat)CatCglibImplProxy.getInstance().getProxy(impl.getClass());
//		cat.run("小黑毛d");
	}
}
