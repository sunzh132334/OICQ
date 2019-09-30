package com.qq.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ϵͳ��־��¼������
 */
public class LogTools {
	private LogTools() {
	};// ����Ҫ��������,������private

	private static boolean isDebug = true;// �Ƿ���Ա�־

	// ��ֹ���һ����־��Ϣ
	public static void disDebug() {
		isDebug = false;
	}

	/**
	 * ��¼һ����־��Ϣ
	 * 
	 * @param c
	 *            :��Ϣ���ڵ���
	 * @param msg
	 *            :��Ϣ�Ķ���
	 */
	public static void INFO(Class c, Object msg) {
		if (!isDebug) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
		String t = sdf.format(new Date());// ��ʽ��ʱ��
		System.out.println("INFO:" + t + ":" + c.getSimpleName() + ":" + msg);
	}

	/**
	 * ��¼������־��Ϣ
	 * 
	 * @param c
	 *            :��Ϣ���ڵ���
	 * @param msg
	 *            :��Ϣ�Ķ���
	 */
	public static void ERROR(Class c, Object msg) {
		if (!isDebug) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("hh-FF-ss");
		String t = sdf.format(new Date());
		System.out.println("ERROR:" + t + ":" + c.getSimpleName() + ":" + msg);
	}

}
