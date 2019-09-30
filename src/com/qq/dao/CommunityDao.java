package com.qq.dao;

import java.util.List;

import com.qq.model.Community;
import com.qq.model.Jkuser;

/**
 * Ⱥʵ���������
 * @author yy
 *
 */
public interface CommunityDao {
	
	public Community getBasicInfo(int cid);
	public int getOwnerByCid(int cid);
	public int insertLog(int jknum, int cid);
	public int addCommunity(String name, int owner, String des, String path);	//����һ����Ⱥ
	public int deleteCommunity(int cid);	//ɾ��һ��Ⱥ
	public List<Integer> getAllOnLineUsers(int cid);	//�õ�һ��Ⱥ�����������û���jknum
}
