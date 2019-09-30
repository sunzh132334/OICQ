package com.qq.server;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import com.qq.dao.BaseJdbcDao;
import com.qq.dao.CommuApplyDapImpl;
import com.qq.dao.CommuApplyRespDaoImpl;
import com.qq.dao.CommuChatLogDaoImpl;
import com.qq.dao.CommunityDaoImpl;
import com.qq.dao.FriendApplyDaoImpl;
import com.qq.dao.FriendApplyRespDaoImpl;
import com.qq.dao.GroupDaoImpl;
import com.qq.dao.JkfileDaoImpl;
import com.qq.dao.JkuserDaoImpl;
import com.qq.model.ChatLog;
import com.qq.model.CommuApply;
import com.qq.model.CommuApplyResp;
import com.qq.model.Community;
import com.qq.model.Jkgroup;
import com.qq.model.Jkuser;
import com.qq.msg.IMsgConstance;
import com.qq.msg.MsgAddCommunity;
import com.qq.msg.MsgAddCommunityResp;
import com.qq.msg.MsgAddFriendResp;
import com.qq.msg.MsgAddGroup;
import com.qq.msg.MsgAddGroupResp;
import com.qq.msg.MsgChatFile;
import com.qq.msg.MsgChatText;
import com.qq.msg.MsgCommuChatFile;
import com.qq.msg.MsgCommuChatText;
import com.qq.msg.MsgCreateCommunity;
import com.qq.msg.MsgCreateCommunityResp;
import com.qq.msg.MsgDeleteCommunity;
import com.qq.msg.MsgDeleteCommunityResp;
import com.qq.msg.MsgDeleteFriendResp;
import com.qq.msg.MsgDeleteGroup;
import com.qq.msg.MsgDeleteGroupResp;
import com.qq.msg.MsgFind;
import com.qq.msg.MsgFindResp;
import com.qq.msg.MsgHead;
import com.qq.msg.MsgHeaderUploadResp;
import com.qq.util.ImageUtil;

/**
 * �������ܹ���ͻ������߳�,ת����Ϣ���� ����ֻ��Ҫ�ṩ��������,���Խ�Ϊ��̬����
 */
public class ChatTools {
	// ���洦���̵߳Ķ��ж���
	private static Map<Jkuser, ServerThread> stList = new HashMap();

	private ChatTools() {
	}// ����Ҫ�����������,��������˽��

	/**
	 * ���û���½�ɹ��󽫶�Ӧ�Ĵ����̶߳�����뵽������ ��������ѷ���������Ϣ
	 * 
	 * @param ct
	 *            :�����̶߳���
	 */
	public static void addClient(Jkuser user, ServerThread ct) {
		stList.put(user, ct);
		// ���������ߵ���Ϣ
		if(user.getState() == 1) {
			sendOnOffLineMsg(user, true);
		}
	}

	/**
	 * �û��˳�ϵͳ 1.�Ƴ���������еĴ����̶߳��� 2.������ѷ���������Ϣ
	 * 
	 * @param user
	 *            :�˳��û�����
	 */
	public static void removeClient(Jkuser user) {
		stList.remove(user);
		//ͬʱ����ѷ�������֪ͨ
		if(user.getState() == 1) {
			sendOnOffLineMsg(user, false);
		}
	}

	/**
	 * ������û��ĺ��ѷ��� ����/������Ϣ
	 * 
	 * @param user
	 *            :����/���ߵ��û�
	 */
	public static void sendOnOffLineMsg(Jkuser user, boolean onLine) {
		// ������û��ĺ��ѷ��ͣ��Ҽ����ߵ���Ϣ
		ArrayList<Jkgroup> gList = (ArrayList<Jkgroup>) user.getGroupList();
		for (int i = 0; i < gList.size(); i++) {
			Jkgroup jkgroup = gList.get(i);
			ArrayList<Jkuser> uList = (ArrayList<Jkuser>) jkgroup.getUserList();
			for (int j = 0; j < uList.size(); j++) {
				Jkuser jkuser = uList.get(j);
				if(stList.get(getUserByNum(jkuser.getJknum())) != null) {
					MsgHead head = new MsgHead();
					head.setTotalLength(13);
					head.setSrc(user.getJknum());
					head.setDest(jkuser.getJknum());
					if(onLine) {
						head.setType(IMsgConstance.command_onLine);
					} else {
						head.setType(IMsgConstance.command_offLine);
					}
					stList.get(getUserByNum(jkuser.getJknum())).sendMsg2Me(head);
				}
			}
			
		}
		
		JkuserDaoImpl daoImpl = new JkuserDaoImpl();
		List<Integer> cidList = daoImpl.getAllCids(user.getJknum());
		if(cidList == null || cidList.size() == 0) return;
		for (int i = 0; i < cidList.size(); i++) {
			int cid = cidList.get(i);
			CommunityDaoImpl communityDaoImpl = new CommunityDaoImpl();
			List<Integer> uList = communityDaoImpl.getAllOnLineUsers(cid);
			for (int j = 0; j < uList.size(); j++) {
				int uid = uList.get(j);
				if(uid == user.getJknum()) continue;
					MsgHead msgHead = new MsgHead();
					msgHead.setTotalLength(13);
					msgHead.setDest(cid);
					msgHead.setSrc(user.getJknum());
					if(onLine) {
						msgHead.setType(IMsgConstance.command_commu_onLine);
					}else {
						msgHead.setType(IMsgConstance.command_commu_offLine);
					}
					stList.get(getUserByNum(uid)).sendMsg2Me(msgHead);
				}
			}
		}
		
		

	/**
	 * ����jknum��stList�����еõ�user����
	 * 
	 * @param jknum
	 * @return
	 */
	private static Jkuser getUserByNum(int jknum) {
		Jkuser jkuser = null;
		Set<Jkuser> set = stList.keySet();
		Iterator<Jkuser> iterator = set.iterator();
		while (iterator.hasNext()) {
			Jkuser jkuser2 = iterator.next();
			if (jkuser2.getJknum() == jknum) {
				jkuser = jkuser2;
				break;
			}
		}

		return jkuser;
	}

	/**
	 * �������е�ĳһ���û�������Ϣ
	 * 
	 * @param srcUser
	 *            ��������
	 * @param msg
	 *            :��Ϣ����
	 * @throws SQLException
	 */
	public static synchronized void sendMsg2One(Jkuser srcUser, MsgHead msg)
			throws SQLException {

		// ͷ���ϴ�����
		if (msg.getType() == IMsgConstance.command_headerupload) {
			try {
				ObjectInputStream oins = new ObjectInputStream(stList.get(
						srcUser).getDins());
				File file = (File) oins.readObject();

				// ���Ȱ��ļ����浽����
				// �ļ�������60*60��С
				String path = "F:/QQimg/u" + srcUser.getJknum() + System.currentTimeMillis() +  ".jpg";
				BufferedImage bi = ImageUtil.compressImage(file, 60, 60);
				ImageIO.write(bi, "jpg", new FileOutputStream(path));
				// �޸����ݿ��¼
				JkuserDaoImpl userImpl = new JkuserDaoImpl();
				int state = userImpl.updateIcon(srcUser.getJknum(), path);

				// ������Ϣ���ͻ���
				MsgHeaderUploadResp headerUploadResp = new MsgHeaderUploadResp();
				headerUploadResp.setTotalLength(14);
				headerUploadResp.setSrc(IMsgConstance.Server_JK_NUMBER);
				headerUploadResp.setDest(srcUser.getJknum());
				headerUploadResp
						.setType(IMsgConstance.command_headerupload_resp);
				headerUploadResp.setState((byte) state);

				stList.get(srcUser).sendMsg2Me(headerUploadResp);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		} else if (msg.getType() == IMsgConstance.command_find) {
			try {
				// ��������
				MsgFind find = (MsgFind) msg;
				int findId = find.getFindId();
				byte classify = find.getClassify();
				// �����ݿ��в�ѯ����
				Object object = BaseJdbcDao.findById(classify, findId);
				MsgFindResp findResp = new MsgFindResp();
				findResp.setTotalLength(14);
				findResp.setSrc(IMsgConstance.Server_JK_NUMBER);
				findResp.setDest(srcUser.getJknum());
				findResp.setType(IMsgConstance.command_find_resp);
				if (object == null) {
					findResp.setState((byte) 0);
					stList.get(srcUser).sendMsg2Me(findResp);
				} else {
					findResp.setState((byte) 1);
					stList.get(srcUser).sendMsg2Me(findResp);
					// ֱ�ӰѶ�����Ϣ���л����ͻ���
					ObjectOutputStream oos = new ObjectOutputStream(stList.get(
							srcUser).getDous());
					oos.writeObject(object);
					oos.flush();
				}
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (msg.getType() == IMsgConstance.command_chatText) {
			MsgChatText chatText = (MsgChatText) msg;
			// ���ݿ�����Ӽ�¼
			ChatLog chatLog = new ChatLog();
			chatLog.setContent(chatText.getCharTxt());
			chatLog.setSrcid(chatText.getSrc());
			chatLog.setDestid(chatText.getDest());
			chatLog.setSendtime(chatText.getSendTime());
			// �ж���Ϣ�������Ƿ����� ������� ״̬����Ϊ1��ʾ�ѽ��� ����0��ʾδ����
			boolean flag = false;
			Set<Jkuser> uSet = stList.keySet();
			Iterator<Jkuser> iterator = uSet.iterator();
			Jkuser dest = null;
			while (iterator.hasNext()) {
				Jkuser user = iterator.next();
				if (user.getJknum() == chatText.getDest()) {
					flag = true;
					dest = user;
					break;
				}
			}

			if (flag) {
				chatLog.setState(1);
			} else {
				chatLog.setState(0);
			}
			ChatLogDaoImpl chatLogDaoImpl = new ChatLogDaoImpl();
			int state = chatLogDaoImpl.save(chatLog);
			// ����Է����� �Ͱ���Ϣֱ�ӷ��͵��Է�����
			if (flag) {
				stList.get(dest).sendMsg2Me(chatText);
			}
			return;
		} else if(msg.getType() == IMsgConstance.command_addFriend) {
			int destNum = msg.getDest();
			//��Ŀ���û�����   ֱ�Ӱ���Ϣת����Ŀ���û� ��������ݴ洢�����ݿ�
			if(stList.get(getUserByNum(destNum)) == null) {
				FriendApplyDaoImpl applyDaoImpl = new FriendApplyDaoImpl();
				applyDaoImpl.addLog(msg.getSrc(), msg.getDest(), 0);
			}else {
				stList.get(getUserByNum(destNum)).sendMsg2Me(msg);	//ֱ�Ӱ���Ӻ��������͵���Ӧ�Ŀͻ���
			}
		}else if(msg.getType() == IMsgConstance.command_addFriend_resp) {
			MsgAddFriendResp addFriendResp = (MsgAddFriendResp) msg;
			int destNum = msg.getDest();
			//��Ŀ���û����� ��ôֱ��ת����Ŀ���û�  ����浽���ݿ�
			if(stList.get(getUserByNum(destNum)) == null) {
				FriendApplyRespDaoImpl applyRespDaoImpl = new FriendApplyRespDaoImpl();
				applyRespDaoImpl.add(msg.getSrc(), destNum, 0, addFriendResp.getRes());
			}else {
				stList.get(getUserByNum(destNum)).sendMsg2Me(msg);	//ֱ�Ӱ���Ӻ��������͵���Ӧ�Ŀͻ���
			}
		}else if(msg.getType() == IMsgConstance.command_commuChatTxt) {
			MsgCommuChatText chatText = (MsgCommuChatText) msg;
			int cid = chatText.getDestCid();
			CommunityDaoImpl communityDaoImpl = new CommunityDaoImpl();
			Community community = communityDaoImpl.getBasicInfo(cid);
			ArrayList<Jkuser> uList = (ArrayList<Jkuser>) community.getUserList();
			//���� �����������¼���뵽���ݿ���
			CommuChatLogDaoImpl chatLogDaoImpl = new CommuChatLogDaoImpl();
			int lid = chatLogDaoImpl.insertLog(cid, chatText.getSrc(), chatText.getchatTxt(), chatText.getSendTime());
			for (int i = 0; i < uList.size(); i++) {
				int jknum = uList.get(i).getJknum();
				if(jknum == chatText.getSrc()) continue;
				if(stList.get(getUserByNum(jknum)) == null) {
					//�����û�������  ��ô���mapping
					chatLogDaoImpl.addMapping(lid, jknum);
				}else {
					stList.get(getUserByNum(jknum)).sendMsg2Me(chatText);
				}
			}
		}else if(msg.getType() == IMsgConstance.command_addCommunity) {
			MsgAddCommunity addCommunity = (MsgAddCommunity) msg;
			int cid = addCommunity.getDestCid();
			CommunityDaoImpl communityDaoImpl = new CommunityDaoImpl();
			int owner = communityDaoImpl.getOwnerByCid(cid);
			addCommunity.setDest(owner);
			if (stList.get(getUserByNum(owner)) == null) {
				CommuApply apply = new CommuApply();
				apply.setCid(addCommunity.getDestCid());
				apply.setDestid(owner);
				apply.setSrcid(addCommunity.getSrc());
				apply.setState(0);
				CommuApplyDapImpl applyDapImpl = new CommuApplyDapImpl();
				applyDapImpl.save(apply);
			}else {
				stList.get(getUserByNum(owner)).sendMsg2Me(addCommunity);
				//˳���������Ⱥ���û����л����ͻ���
				OutputStream os = stList.get(getUserByNum(owner)).getDous();
				try {
					ObjectOutputStream oos = new ObjectOutputStream(os);
					JkuserDaoImpl daoImpl = new JkuserDaoImpl();
					Jkuser jkuser = daoImpl.getBasicInfo(addCommunity.getSrc());
					oos.writeObject(jkuser);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else if(msg.getType() == IMsgConstance.command_addCommunity_resp) {
			MsgAddCommunityResp addCommunityResp = (MsgAddCommunityResp) msg;
			CommunityDaoImpl communityDaoImpl = new CommunityDaoImpl();
			if(addCommunityResp.getRes() == 1) {
				/**
				 * ���Ⱥ��-����ӳ��
				 */
				communityDaoImpl.insertLog(addCommunityResp.getDest(), addCommunityResp.getDestcid());
			}
			if(stList.get(getUserByNum(addCommunityResp.getDest())) == null) {
				CommuApplyResp applyResp = new CommuApplyResp();
				applyResp.setCid(addCommunityResp.getDestcid());
				applyResp.setSrcid(addCommunityResp.getSrc());
				applyResp.setDestid(addCommunityResp.getDest());
				applyResp.setState(0);
				applyResp.setRes(addCommunityResp.getRes());
				CommuApplyRespDaoImpl applyRespDaoImpl = new CommuApplyRespDaoImpl();
				applyRespDaoImpl.save(applyResp);
			}else {
				stList.get(getUserByNum(addCommunityResp.getDest())).sendMsg2Me(addCommunityResp);
				if(addCommunityResp.getRes() == 1) {
					//Ⱥ�Ļ�����Ϣ���л�������
					try {
						ObjectOutputStream oos = new ObjectOutputStream(stList.get(getUserByNum(addCommunityResp.getDest())).getDous());
						oos.writeObject(communityDaoImpl.getBasicInfo(addCommunityResp.getDestcid()));
						oos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else if(msg.getType() == IMsgConstance.command_chatFile) {
			MsgChatFile chatFile = (MsgChatFile)msg;
			int destJknum =  chatFile.getDest();
			int srcNum = chatFile.getSrc();
			String fileName = chatFile.getFileName();
			String sendTime = chatFile.getSendTime();
			//���ߵĻ�ֱ��ת��  ���򴫵����ݿ�
			if(stList.get(getUserByNum(destJknum)) == null) {
				String path = "F:/QQServer/"+fileName;
				try {
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
					bos.write(chatFile.getFileData());
					bos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				JkfileDaoImpl fileDaoImpl = new JkfileDaoImpl();
				//����ļ�
				int fid = fileDaoImpl.addFile(path, fileName, srcNum, sendTime);
				//���ӳ��
				fileDaoImpl.addUFMapping(destJknum, fid, sendTime,0);
			}else {
				stList.get(getUserByNum(destJknum)).sendMsg2Me(chatFile);
			}
		}else if(msg.getType() == IMsgConstance.command_commuChatFile) {
			MsgCommuChatFile chatFile = (MsgCommuChatFile) msg;
			int cid = chatFile.getDestCid();
			CommunityDaoImpl communityDaoImpl = new CommunityDaoImpl();
			Community community = communityDaoImpl.getBasicInfo(cid);
			List<Jkuser> uList = community.getUserList();
			//���Ⱥ��-�ļ�ӳ��
			String path = "F:/QQServer/"+chatFile.getSrc()+chatFile.getFileName();
			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
				bos.write(chatFile.getFileData());
				bos.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JkfileDaoImpl daoImpl = new JkfileDaoImpl();
			int fid = daoImpl.addFile(path, chatFile.getFileName(), chatFile.getSrc(), chatFile.getSendTime());
			
			//�ļ������ɺ� ���ӳ��
			daoImpl.addCfMapping(chatFile.getDestCid(), fid);
			
			for (int i = 0; i < uList.size(); i++) {
				if(uList.get(i).getJknum() == chatFile.getSrc()) continue;
				//����ֱ��ת�� �����߰Ѽ�¼�浽���ݿ���
				if(stList.get(getUserByNum(uList.get(i).getJknum())) == null) {
					
					daoImpl.addUcfMapping(uList.get(i).getJknum(), cid, fid);
					
				}else {
					stList.get(getUserByNum(uList.get(i).getJknum())).sendMsg2Me(chatFile);
				}
				
			}
			
			
			
			
		}else if(msg.getType() == IMsgConstance.command_addGroup) {
			MsgAddGroup addGroup = (MsgAddGroup) msg;
			GroupDaoImpl daoImpl = new GroupDaoImpl();
			int uid = addGroup.getSrc();
			String groupName = addGroup.getGroupName();
			int gid = daoImpl.addGroup(groupName, uid);
			
			//��ͻ��˷�����ӷ����Ӧ��Ϣ
			MsgAddGroupResp addGroupResp = new MsgAddGroupResp();
			addGroupResp.setTotalLength(14);
			addGroupResp.setType(IMsgConstance.command_addGroup_resp);
			addGroupResp.setSrc(gid);
			addGroupResp.setDest(addGroup.getSrc());
			
			addGroupResp.setState((byte)1);
			
			stList.get(getUserByNum(addGroup.getSrc())).sendMsg2Me(addGroupResp);
			
		}else if(msg.getType() == IMsgConstance.command_deleteFriend) {
			int srcNum = msg.getSrc();
			int destNum = msg.getDest();
			GroupDaoImpl daoImpl = new GroupDaoImpl();
			int gid1 = daoImpl.getGidByJknum(srcNum, destNum);
			int gid2 = daoImpl.getGidByJknum(destNum, srcNum);
			int state1 = daoImpl.deleteFriends(srcNum, gid1);
			int state2 = daoImpl.deleteFriends(destNum, gid2);
			
			//��ͻ��˷���ɾ�����ѻ�Ӧ��Ϣ
			MsgDeleteFriendResp deleteFriendResp = new MsgDeleteFriendResp();
			deleteFriendResp.setTotalLength(18);
			deleteFriendResp.setType(IMsgConstance.command_deleteFriend_resp);
			deleteFriendResp.setSrc(msg.getSrc());
			deleteFriendResp.setDest(msg.getDest());
			if(state1 == 1 && state2 == 1) {
				deleteFriendResp.setState((byte)1);
			}else {
				deleteFriendResp.setState((byte)0);
			}
			deleteFriendResp.setGid(gid2);
			stList.get(getUserByNum(srcNum)).sendMsg2Me(deleteFriendResp);
			
			//�ж϶Է��ڲ�����  ���ߵĻ�ͬ��Ҫ����ɾ��������Ϣ��Ӧ
			if(stList.get(getUserByNum(destNum)) != null) {
				if(state1 == 1 && state2 == 1) {
					deleteFriendResp.setSrc(destNum);
					deleteFriendResp.setDest(srcNum);
					deleteFriendResp.setGid(gid1);
					stList.get(getUserByNum(destNum)).sendMsg2Me(deleteFriendResp);
					
				}
			}
			
			
		}else if(msg.getType() == IMsgConstance.command_deleteGroup) {
			MsgDeleteGroup deleteGroup = (MsgDeleteGroup) msg;
			int gid = deleteGroup.getGid();
			int srcNum = deleteGroup.getSrc();
			
			
			GroupDaoImpl daoImpl = new GroupDaoImpl();
			int state = daoImpl.deleteGroup(gid);
			
			//��ͻ��˷��ͷ���ɾ����Ӧ��Ϣ
			MsgDeleteGroupResp deleteGroupResp = new MsgDeleteGroupResp();
			deleteGroupResp.setTotalLength(18);
			deleteGroupResp.setType(IMsgConstance.command_deleteGroup_resp);
			deleteGroupResp.setSrc(IMsgConstance.Server_JK_NUMBER);
			deleteGroupResp.setDest(srcNum);
			deleteGroupResp.setGid(gid);
			if(state == 1) {
				deleteGroupResp.setState((byte)1);
			}else {
				deleteGroupResp.setState((byte)0);
			}
			
			stList.get(getUserByNum(srcNum)).sendMsg2Me(deleteGroupResp);
			
		}else if(msg.getType() == IMsgConstance.command_createCommunity) {
			MsgCreateCommunity community = (MsgCreateCommunity) msg;
			int owner = community.getSrc();
			String name = community.getcName();
			String des = community.getcDes();
			byte[] data = community.getIcon();
			
			//���Ȱ�ͷ�񱣴浽����
			String path = "F:/QQimg/"+owner+community.getFileName();
			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
				bos.write(data);
				bos.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//���ݿ������Ⱥ��¼
			CommunityDaoImpl communityDaoImpl = new CommunityDaoImpl();
			int cid = communityDaoImpl.addCommunity(name, owner, des, path);
			
			//��ͻ��˷��ͻ�Ӧ��Ϣ
			MsgCreateCommunityResp communityResp = new MsgCreateCommunityResp();
			communityResp.setTotalLength(18);
			communityResp.setType(IMsgConstance.command_createCommunity_resp);
			communityResp.setSrc(IMsgConstance.Server_JK_NUMBER);
			communityResp.setDest(owner);
			communityResp.setCid(cid);
			if(cid > 0) {				
				communityResp.setState((byte)1);
			}else {
				communityResp.setState((byte)0);
			}
			
			stList.get(getUserByNum(owner)).sendMsg2Me(communityResp);
			
		}else if(msg.getType() == IMsgConstance.command_deleteCommunity) {
			MsgDeleteCommunity community = (MsgDeleteCommunity) msg;
			int cid = community.getCid();
			CommunityDaoImpl communityDaoImpl = new CommunityDaoImpl();
			int state = communityDaoImpl.deleteCommunity(cid);
			
			MsgDeleteCommunityResp communityResp = new MsgDeleteCommunityResp();
			communityResp.setTotalLength(18);
			communityResp.setType(IMsgConstance.command_deleteCommunity_resp);
			communityResp.setSrc(IMsgConstance.Server_JK_NUMBER);
			communityResp.setDest(community.getSrc());
			communityResp.setCid(cid);
			if(state == 1) {
				communityResp.setState((byte) 1);
			}else {
				communityResp.setState((byte) 0);
			}
			
			System.out.println(communityResp.getState()+"------------");
			
			stList.get(getUserByNum(community.getSrc())).sendMsg2Me(communityResp);
			
		}
	}
}
