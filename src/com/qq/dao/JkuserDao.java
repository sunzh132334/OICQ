package com.qq.dao;

import java.util.List;

import com.qq.model.Jkuser;

/**
 * QQ�û�ʵ���������
 * @author yy
 *
 */
public interface JkuserDao {
	
	
	public int regUser(Jkuser jkuser);	//ע���û�  ����Ϊ������jkuser
	public Jkuser checkLogin(int jknum, String password,int state);	//�û��ĵ�½ �ɹ������û���ȫ����Ϣ
	public Jkuser findPwd(int jknum);	//�һ�����  �����û���jknum ��  �ܱ����� �ܱ���
	public int changePwd(int jknum, String newPwd);	//�޸����� �ɹ�����1
	public void offOnline(int jknum);	//����qq����
	public int updateIcon(int jknum,String iconpath);	//�޸�ͷ��
	public int updateUserInfo(Jkuser jkuser);	//�����û��Ļ�������  
	public Jkuser getBasicInfo(int jknum);
	public List<Integer> getAllCids(int jid);
}
