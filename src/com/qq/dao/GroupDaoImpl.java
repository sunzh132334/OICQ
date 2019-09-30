package com.qq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qq.util.JdbcUtil;

public class GroupDaoImpl implements GroupDao {

	
	/**
	 * ��Ӻ���
	 */
	@Override
	public void addFriends(int gid, int jknum) {
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into ug values(?,?)";
		try {
			ps =conn.prepareStatement(sql);
			ps.setObject(1, jknum);
			ps.setObject(2, gid);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * ���һ������
	 */
	@Override
	public int addGroup(String name, int jknum) {
		int state = 0;
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int currentGid = 0;
		//�ҳ���ǰ������
		String sql = "select max(gid) from jkgroup";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				currentGid = rs.getInt(1);
				currentGid++;
			}else {
				currentGid = 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sql = "insert into jkgroup values(?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1, currentGid);
			ps.setObject(2, name);
			ps.setObject(3, jknum);
			state = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return currentGid;
	}


	/**
	 * ɾ������
	 */
	@Override
	public int deleteFriends(int jid, int gid) {
		int state = 0;
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "delete ug where jid = ? and gid = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1, jid);
			ps.setObject(2, gid);
			state = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return state;
	}


	/**
	 * �õ�src�û���dest�û��е�С��id
	 */
	@Override
	public int getGidByJknum(int srcNum, int destNum) {
		
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "select gid from jkgroup where owner = ?";
		List<Integer> gidList = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1, destNum);
			rs = ps.executeQuery();
			while(rs.next()) {
				gidList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//����ug�� ȷ������������gid
		
		int final_gid = 0;
		
		for (int i = 0; i < gidList.size(); i++) {
			int gid = gidList.get(i);
			sql = "select * from ug where jid = ? and gid = ?";
			try {
				ps = conn.prepareStatement(sql);
				ps.setObject(1, srcNum);
				ps.setObject(2, gid);
				rs = ps.executeQuery();
				if(rs.next()) {
					final_gid = gid;
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
		return final_gid;
	}


	/**
	 * ɾ��һ������
	 */
	@Override
	public int deleteGroup(int gid) {
		int  state1 = 0;
		int state2 = 0;
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		String sql = "delete from jkgroup where gid = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1, gid);
			state1 = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//ɾ��ufӳ��
		sql = "delete ug where gid = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1, gid);
			state2 = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		if(state1 == 1 ) {
			return 1;
		}else {
			return 0;
		}
	}

}
