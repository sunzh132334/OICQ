package com.qq.dao;

import java.util.List;

import com.qq.model.FriendApplyResp;

/**
 * ��������ָ���Ϣ ��������
 * @author yy
 *
 */
public interface FriendApplyRespDao {
	
	public int add(int srcNum,int destNum, int state, int res);
	public List<FriendApplyResp> queryLog(int jknum);
	public void changeState(int srcNum, int destNum);
	
	
	
}
