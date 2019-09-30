package com.qq.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.qq.model.ChatLog;
import com.qq.model.Community;
import com.qq.model.Jkuser;
import com.qq.model.ToolsCreateMsg;
import com.qq.model.ToolsParseMsg;
import com.qq.msg.IMsgConstance;
import com.qq.msg.MsgChangePwd;
import com.qq.msg.MsgChangePwdResp;
import com.qq.msg.MsgChatText;
import com.qq.msg.MsgCommuChatText;
import com.qq.msg.MsgFind;
import com.qq.msg.MsgFindResp;
import com.qq.msg.MsgForgetResp;
import com.qq.msg.MsgHead;
import com.qq.msg.MsgHeaderUpload;
import com.qq.msg.MsgHeaderUploadResp;
import com.qq.msg.MsgLogin;
import com.qq.msg.MsgLoginResp;
import com.qq.msg.MsgReg;
import com.qq.msg.MsgRegResp;
import com.qq.util.LogTools;
import com.qq.util.MD5Util;

/**
 * QQ�ͻ��˵�ͨ��ģ��,�ṩ�� 1.��½��ע��ӿڵ��ã� 2.�ڶ����߳��н��շ�������Ϣ 3.�����յ�����Ϣ�ַ�������������
 */
public class ClientConnection extends Thread {

	private static final ClientConnection ins = new ClientConnection();// ���൥ʵ������
	private Socket client; // ����������������
	private DataOutputStream dous;// ���������
	private DataInputStream dins;// ����������
	public DataInputStream getDins() {
		return dins;
	}

	public void setDins(DataInputStream dins) {
		this.dins = dins;
	}



	private List<IClientMsgListener> listeners = new ArrayList<IClientMsgListener>(); // װ�����еļ���������
	
	/** ����Ҫ��������,���Թ�����˽�� */
	private ClientConnection() {

	}

	// ��ʵ��������ʷ���
	public static ClientConnection getIns() {
		return ins;
	}

	/** �����Ϸ�����,�Ƿ�����ɹ� */
	public boolean conn2Server() {
		try {
			// 1.����һ�����������˵�Socket����
			client = new Socket(IMsgConstance.serverIP,
					IMsgConstance.serverPort);
			// 2.�õ��������������
			// 3.��װΪ�ɶ�дԭʼ�������͵����������
			this.dous = new DataOutputStream(client.getOutputStream());
			this.dins = new DataInputStream(client.getInputStream());
			return true;
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return false;
	}

	public DataOutputStream getDous() {
		return dous;
	}

	public void setDous(DataOutputStream dous) {
		this.dous = dous;
	}

	/**
	 * ��ע����û���Ϣ�ύ�������� �ȴ�����������Ӧ ����jknum
	 * 
	 * @param jkuser
	 * @return
	 * @throws Exception
	 */
	public int regServer(Jkuser jkuser) throws Exception {
		int state = 0;
		MsgReg msgReg = new MsgReg();
		msgReg.setTotalLength(250);
		msgReg.setType(IMsgConstance.command_reg);
		msgReg.setDest(IMsgConstance.Server_JK_NUMBER);
		msgReg.setSrc(0);
		msgReg.setJkuser(jkuser);
		this.sendMsg(msgReg);
		// �����˵�½����֮��,�������һ��Ӧ�����Ϣ
		MsgHead regResp = readFromServer();
		MsgRegResp resp = (MsgRegResp) regResp;

		if (resp.getState() == 0) {
			return resp.getDest();
		}

		return -1;

	}

	/**
	 * ���Ͻ��ܷ�������������Ϣ ���ҽ�������������
	 */
	public void run() {
		while (true) {
			try {
				// ����һ����Ϣ
				MsgHead m = readFromServer();
				// ����Ϣ�ַ���������ȥ����
				for (IClientMsgListener lis : listeners) {
					lis.fireMsg(m);
				}
			} catch (Exception ef) {
				ef.printStackTrace();
				break; // �����ȡ����,���˳�
			}
		}
		LogTools.INFO(this.getClass(), "�ͻ��˽����̼߳��˳�!");

	}

	
	
	/**
	 * ���������϶�ȡһ���������˷�������Ϣ ��������������������ڶ����߳���
	 * 
	 * @return:��ȡ������Ϣ����
	 */
	public  MsgHead readFromServer() throws Exception {
		int totalLen = dins.readInt();
		LogTools.INFO(this.getClass(), "�ͻ��˶�����Ϣ�ܳ�Ϊ:" + totalLen);
		byte[] data = new byte[totalLen - 4];
		dins.readFully(data); // ��ȡ���ݿ�
		MsgHead msg = ToolsParseMsg.parseMsg(data);// ���Ϊ��Ϣ����
		LogTools.INFO(this.getClass(), "�ͻ����յ���Ϣ:" + msg);
		return msg;
	}

	/** ����һ����Ϣ���������ķ��� */
	public void sendMsg(MsgHead msg) throws Exception {
		LogTools.INFO(this.getClass(), "�ͻ��˷�����Ϣ:" + msg);
		byte[] data = ToolsCreateMsg.packMsg(msg);// �������Ϊ���ݿ�
		this.dous.write(data);// ����
		this.dous.flush();
	}

	
	/**
	 * ��������˷�����������������Ϣ ���õ��������˵Ļ�Ӧ
	 * @param jknum
	 * @return
	 */
	public String[] forgetPwd(int jknum) {
		String[] str = new String[2];
		//��������˷�������
		MsgHead forgetPwd = new MsgHead();
		forgetPwd.setTotalLength(13);
		forgetPwd.setSrc(jknum);
		forgetPwd.setDest(IMsgConstance.Server_JK_NUMBER);
		forgetPwd.setType(IMsgConstance.command_forgetPwd);
		try {
			this.sendMsg(forgetPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			MsgHead head = readFromServer();
			MsgForgetResp forgetResp = (MsgForgetResp) head;
			if(forgetResp.getSrc() == 0) return null;
			str[0] = forgetResp.getQuestion().trim();
			str[1] = forgetResp.getAnswer().trim();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return str;
		
	}
	
	
	/**
	 * �޸����� �ɹ�����1 ʧ�ܵĻ�����0
	 * @param srcNum
	 * @param newPwd
	 */
	public byte changePwd(int srcNum, String newPwd) {
		MsgChangePwd changePwd = new MsgChangePwd();
		changePwd.setTotalLength(45);
		changePwd.setType(IMsgConstance.command_changePwd);
		changePwd.setSrc(srcNum);
		changePwd.setDest(IMsgConstance.Server_JK_NUMBER);
		changePwd.setNewPwd(MD5Util.MD5(newPwd));
		try {
			this.sendMsg(changePwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MsgChangePwdResp changePwdResp = null;
		
		try {
			MsgHead head = readFromServer();
			changePwdResp = (MsgChangePwdResp) head;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return changePwdResp.getState();
	}
	
	
	/**
	 * Ϊ����������һ����Ϣ�������������
	 * 
	 * @param l
	 *            :��Ϣ�������������
	 */
	public void addMsgListener(IClientMsgListener l) {
		this.listeners.add(l);
	}

	// �ر���һ���ͻ���������
	public void closeMe() {
		try {
			this.client.close();
		} catch (Exception ef) {
		}
	}

	/**
	 * ��ĳ�����ѷ�����Ϣ
	 * 
	 * @param srcJknum
	 * @param destJknum
	 * @param content
	 * @return
	 */
	public int sendMsg2One(int srcJknum, int destJknum, String content,
			String sendTime) {
		MsgChatText chatText = new MsgChatText();
		chatText.setTotalLength(98);
		chatText.setSrc(srcJknum);
		chatText.setDest(destJknum);
		chatText.setCharTxt(content);
		chatText.setSendTime(sendTime);
		chatText.setType(IMsgConstance.command_chatText);
		try {
			this.sendMsg(chatText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * �����������Ⱥ����Ϣ
	 * @param srcNum
	 * @param destCid
	 * @param chatTxt
	 * @param sendTime
	 */
	public void sendCommuTxt(int srcNum, int destCid, String chatTxt, String sendTime) {
		MsgCommuChatText chatText = new MsgCommuChatText();
		chatText.setTotalLength(102);
		chatText.setSrc(srcNum);
		chatText.setDest(IMsgConstance.Server_JK_NUMBER);
		chatText.setchatTxt(chatTxt);
		chatText.setDestCid(destCid);
		chatText.setType(IMsgConstance.command_commuChatTxt);
		chatText.setSendTime(sendTime);
		try {
			this.sendMsg(chatText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	/**
	 * ����������͵�½����
	 * 
	 * @param jkNum
	 * @param pwd
	 * @return
	 */
	public Jkuser loginServer(int jkNum, String pwd, int state) {
		try {
			MsgLogin msgLogin = new MsgLogin();
			msgLogin.setTotalLength(49);
			msgLogin.setType(IMsgConstance.command_login);
			msgLogin.setSrc(jkNum);
			msgLogin.setState(state);
			msgLogin.setDest(IMsgConstance.Server_JK_NUMBER);
			msgLogin.setPassword(pwd);
			this.sendMsg(msgLogin);
			// ����ȵ�һ��Ӧ����Ϣ
			MsgHead loginResp = readFromServer();
			MsgLoginResp mlr = (MsgLoginResp) loginResp;
			if (mlr.getState() == 1) {
				return null;
			} else {
				ObjectInputStream oins = new ObjectInputStream(dins);
				Jkuser jkuser = new Jkuser();
				jkuser = (Jkuser) oins.readObject();
				System.out.println("���յ�:" + jkuser);
				return jkuser;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
