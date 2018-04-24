package org.yaukie.rmi.server.impl;

import org.yaukie.rmi.server.inter.ICat;
/**
 * 静态代理演示
 * @author yaukie
 *静态代理含义:直接认为写死代理类
 */
public class CatDoesImpl implements ICat {
	public void run(String name ) {
		System.out.println("cat "+name+"run so fast !");
	}
}
