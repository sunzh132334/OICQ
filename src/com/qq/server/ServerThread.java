package com.qq.server;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;

import com.qq.dao.JkuserDaoImpl;
import com.qq.model.Jkuser;
import com.qq.model.ToolsCreateMsg;
import com.qq.model.ToolsParseMsg;
import com.qq.msg.IMsgConstance;
import com.qq.msg.MsgChangePwd;
import com.qq.msg.MsgChangePwdResp;
import com.qq.msg.MsgForgetResp;
import com.qq.msg.MsgHead;
import com.qq.msg.MsgHeaderUploadResp;
import com.qq.msg.MsgLogin;
import com.qq.msg.MsgLoginResp;
import com.qq.msg.MsgReg;
import com.qq.msg.MsgRegResp;
import com.qq.util.LogTools;
import com.qq.util.MD5Util;

/**
 * �������߳� ����һ���ض��Ŀͻ���
 * 
 * @author yy
 */
public class ServerThread extends Thread {

	private Socket client; // ��ʾ���ӵĿͻ��˶���
	private DataInputStream dins;
	private DataOutputStream dous;
	private Jkuser owerUser; // ����̴߳�����û�����
	private boolean loginOk = false; // �Ƿ�ɹ���½�ı�־

	public DataInputStream getDins() {
		return dins;
	}

	public void setDins(DataInputStream dins) {
		this.dins = dins;
	}

	public DataOutputStream getDous() {
		return dous;
	}

	public void setDous(DataOutputStream dous) {
		this.dous = dous;
	}

	public ServerThread(Socket client) {
		this.client = client;
	}

	/**
	 * ���ӳɹ��� ��ȡ��һ���ͻ�����Ϣ ������ע�� ��½ ������������
	 */
	@SuppressWarnings("unused")
	private boolean readFirstMsg() {
		try {
			// ��װΪ�ɶ�дԭʼ�������͵����������
			this.dous = new DataOutputStream(client.getOutputStream());
			this.dins = new DataInputStream(client.getInputStream());

			MsgHead msg = reciveData(); // ��ȡ��һ����Ϣ:
			if (msg.getType() == IMsgConstance.command_login) {// ����ǵ�½����
				MsgLogin ml = (MsgLogin) msg;
				Jkuser jkuser = checkLogin(ml);
				MsgLoginResp loginResp = new MsgLoginResp();
				loginResp.setTotalLength(14);
				loginResp.setSrc(IMsgConstance.Server_JK_NUMBER);
				loginResp.setType(IMsgConstance.command_login_resp);
				if(jkuser == null) {
					loginResp.setDest(0);
					loginResp.setState((byte)1);
					//send to client
					this.sendMsg2Me(loginResp);
					return false;
				}else {
					this.owerUser = jkuser;
					loginResp.setState((byte)0);
					loginResp.setDest(jkuser.getJknum());
					this.sendMsg2Me(loginResp);
					ObjectOutputStream oos = new ObjectOutputStream(dous);
					oos.writeObject(jkuser);
					oos.flush();
					return true;
				}
			}
			if (msg.getType() == IMsgConstance.command_reg) {// �����ע������
				MsgReg reg = (MsgReg) msg;
				JkuserDaoImpl daoImpl = new JkuserDaoImpl();
				int jknum = daoImpl.regUser(reg.getJkuser());
				MsgRegResp msgRegResp = new MsgRegResp((byte) 0, jknum);
				msgRegResp.setTotalLength(18);
				msgRegResp.setType(IMsgConstance.command_reg_resp);
				msgRegResp.setSrc(IMsgConstance.Server_JK_NUMBER);
				msgRegResp.setDest(jknum);
				
				// ������Ϣ���ͻ���
				this.sendMsg2Me(msgRegResp);
			}
			if(msg.getType() == IMsgConstance.command_forgetPwd) {
				JkuserDaoImpl daoImpl = new JkuserDaoImpl();
				Jkuser user = daoImpl.getBasicInfo(msg.getSrc());
				MsgForgetResp forgetResp = new MsgForgetResp();
				forgetResp.setTotalLength(613);
				forgetResp.setType(IMsgConstance.command_forgetPwd_resp);
				if(user == null) {
					forgetResp.setSrc(0);
					forgetResp.setQuestion("");
					forgetResp.setAnswer("");
				}else {
					forgetResp.setSrc(IMsgConstance.Server_JK_NUMBER);
					String question = user.getQuestion().trim();
					String answer = user.getAnswer().trim();
					forgetResp.setQuestion(question);
					forgetResp.setAnswer(answer);
				}
				forgetResp.setDest(msg.getSrc());
				System.out.println(forgetResp);
				this.sendMsg2Me(forgetResp);
				
			}
			if(msg.getType() == IMsgConstance.command_changePwd) {
				System.out.println("---------------");
				int srcNum  =msg.getSrc();
				MsgChangePwd changePwd = (MsgChangePwd) msg;
				System.out.println("---"+changePwd);
				String newPwd  = changePwd.getNewPwd();
				JkuserDaoImpl daoImpl = new JkuserDaoImpl();
				int status = daoImpl.changePwd(srcNum, newPwd);
				
				
				MsgChangePwdResp changePwdResp = new MsgChangePwdResp();
				changePwdResp.setTotalLength(14);
				changePwdResp.setType(IMsgConstance.command_changePwd_resp);
				changePwdResp.setSrc(IMsgConstance.Server_JK_NUMBER);
				changePwdResp.setDest(srcNum);
				if(status == 1) {
					changePwdResp.setState((byte) 1);
				}else {
					changePwdResp.setState((byte) 0);
				}
				this.sendMsg2Me(changePwdResp);
			}
			
		} catch (Exception ef) {
			LogTools.ERROR(this.getClass(), "readFirstMsgʧ��:" + ef);
		}
		return false;
	}

	/**
	 * ����½�Ƿ�ɹ�
	 * @param ml	���ܵ�MsgLogin����
	 * @return
	 */
	private Jkuser checkLogin(MsgLogin ml) {
		JkuserDaoImpl impl = new JkuserDaoImpl();
		Jkuser user = impl.checkLogin(ml.getSrc(), MD5Util.MD5(ml.getPassword()),ml.getState());
		return user;
	}

	// �߳���ִ�н�����Ϣ�ķ���
	public void run() {
		try {
			/**
			 * ��ȡ��һ����Ϣ�����ʱ���֮Ϊ��ͬ����Ϣ���ȴ�����Ϣ���������Ϣ����������ܽ��к�������ִ�С�
			 * �����ֿ��ܣ�1 ��½ 2 ע�� 3 ��������
			 */
			while(!loginOk) {
				loginOk = readFirstMsg(); // ��ȡ��һ����Ϣ
			}

			if (loginOk) {
				// �����½�ɹ�������������̶߳�����뵽������
				ChatTools.addClient(this.owerUser, this);
			}
			while (loginOk) {
				MsgHead msg = this.reciveData();// ѭ��������Ϣ
				ChatTools.sendMsg2One(this.owerUser, msg);// �ַ�������Ϣ
			}
		} catch (Exception ef) {
			LogTools.ERROR(this.getClass(), "����������Ϣ����:" + ef);
		}
		// �û�������,�Ӷ������Ƴ�����û���Ӧ�Ĵ����̶߳���
		if(loginOk) {
			ChatTools.removeClient(this.owerUser);
		}

	}

	/**
	 * ���������϶�ȡ���ݿ�,���Ϊ��Ϣ����
	 * 
	 * @return:�����������ݿ����Ϊ��Ϣ����
	 */
	private MsgHead reciveData() throws Exception {
		int len = dins.readInt(); // ��ȡ��Ϣ����
		LogTools.INFO(this.getClass(), "����������Ϣ����:" + len);
		byte[] data = new byte[len - 4];
		dins.readFully(data);
		/**
		 * ����data�������������� ���������ǽ�data�ֽ������е����ݽ�������Ӧ����Ϣ���������
		 */
		MsgHead msg = ToolsParseMsg.parseMsg(data);// ����Ϊ��Ϣ����
		LogTools.INFO(this.getClass(), "������������Ϣ����:" + msg);
		System.out.println("type:"+msg.getType());
		return msg;
	}

	/**
	 * ����һ����Ϣ������������������Ŀͻ����û�
	 * 
	 * @param msg
	 *            :Ҫ���͵���Ϣ����
	 * @return:�Ƿ��ͳɹ�
	 */
	public boolean sendMsg2Me(MsgHead msg) {
		try {
			byte[] data = ToolsCreateMsg.packMsg(msg);// ����Ϣ������Ϊ�ֽ���
			this.dous.write(data);
			this.dous.flush();
			LogTools.INFO(this.getClass(), "������������Ϣ����:" + msg + "len:" + msg.getTotalLength());
			
			ByteArrayInputStream bins = new ByteArrayInputStream(data);
			DataInputStream dins = new DataInputStream(bins);
			return true;
		} catch (Exception ef) {
			LogTools.ERROR(this.getClass(), "������������Ϣ����:" + msg);
		}
		return false;
	}

	/**
	 * �Ͽ�������������߳���ͻ���������, �����쳣,�����߳��˳�ʱ����
	 */
	public void disConn() {
		try {
			this.client.close();
		} catch (Exception ef) {
		}
	}
}
