package com.qq.dao;

/**
 * Ⱥ�ļ�¼ʵ�� ��������
 * @author yy
 *
 */
public interface CommuChatLogDao {
	
	public int insertLog(int cid, int srcNum, String chatTxt,String sendTime);
	public int addMapping(int lid, int jid);

}
