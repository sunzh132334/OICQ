package com.qq.client;

import com.qq.msg.MsgHead;

/**
 * QQ��Ŀ
 * ͨѶģ�����Ϣ����������ӿڶ���
 * @author yy
 *
 */
public interface IClientMsgListener {
	
	/**
	 * ������յ���һ����Ϣ
	 * @param msg
	 */
	public void fireMsg(MsgHead msg);
	
}
