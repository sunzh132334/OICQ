package com.qq.dao;

/**
 * �ļ�ʵ���������
 * @author yy
 *
 */
public interface JkfileDao {
	public int addFile(String path, String name, int jid, String sendTime);	//���һ���ļ�
	public int addUFMapping(int destid, int fid, String sendTime, int curState);	//����û����ļ�ӳ��
	public int addCfMapping(int cid, int fid);	//���Ⱥ���ļ�ӳ��
	public int addUcfMapping(int jknum, int cid, int fid);	//����û�Ⱥ���ļ�ӳ��
}
