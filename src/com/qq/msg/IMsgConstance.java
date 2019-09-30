package com.qq.msg;

/**
 * QQ��Ŀ��һЩ��������
 * 
 * @author yy
 * 
 */
public interface IMsgConstance {

	String serverIP = "localhost";// ������IP
	int serverPort = 9090; // �������˿�
	int Server_JK_NUMBER = 10000;// ��������JK��
	
	
	// ϵͳ�õ�����Ϣ���Ͷ���
	byte command_reg = 0x01;// ע��������Ϣ
	byte command_reg_resp = 0x02;// ע��Ӧ����Ϣ
	byte command_login = 0x03;	//��½������Ϣ
	byte command_login_resp = 0x04;	//��½Ӧ����Ϣ
	byte command_headerupload = 0x05;	//ͷ���ϴ���Ϣ��
	byte command_headerupload_resp = 0x06;	//ͷ���ϴ���Ӧ��
	byte command_find = 0x07;	//����������Ϣ��
	byte command_find_resp = 0x08;	//���������Ӧ��Ϣ��
	byte command_chatText = 0x09;	//�����ı���Ϣ��
	byte command_chatFile = 0x10;	//�����ļ���Ϣ��
	byte command_commuChatFile = 0x11;	//Ⱥ���ļ���Ϣ��
	byte command_onLine = 0x12;	//��������֪ͨ��Ϣ	*����Ϣ��
	byte command_offLine = 0x13;	//����������Ϣ֪ͨ	*����Ϣ��
	byte command_addFriend_resp =  0x14;	//���������Ӧ��Ϣ
	byte command_addFriend = 0x15;	//����������Ϣ
	byte command_commuChatTxt = 0x16;	//Ⱥ���ı���Ϣ
	byte command_addCommunity = 0x17;	//��Ⱥ������Ϣ
	byte command_addCommunity_resp = 0x18;	//��Ⱥ�����Ӧ��Ϣ��
	byte command_addGroup = 0x19;	//��ӷ�����Ϣ��
	byte command_addGroup_resp = 0x20;	//��ӷ�����Ϣ��Ӧ��
	byte command_deleteFriend = 0x21;	//ɾ��������Ϣ *����Ϣ��
	byte command_deleteFriend_resp = 0x22;	//ɾ�����������Ӧ
	byte command_deleteGroup = 0x23;	//ɾ��������Ϣ
	byte command_deleteGroup_resp = 0x24;	//ɾ�������Ӧ��Ϣ
	byte command_createCommunity = 0x25;	//Ⱥ�鴴����Ϣ
	byte command_createCommunity_resp = 0x26;	//Ⱥ�鴴����Ӧ��Ϣ
	byte command_deleteCommunity = 0x27;	//ɾ��Ⱥ����Ϣ
	byte command_deleteCommunity_resp = 0x28;	//ɾ��Ⱥ���Ӧ��Ϣ
	byte command_commu_onLine = 0x29;	//����������Ⱥ��Ϣ *����Ϣ��
	byte command_commu_offLine = 0x30;	//����������Ⱥ��Ϣ *����Ϣ��
	byte command_forgetPwd = 0x31;	//����������Ϣ
	byte command_forgetPwd_resp = 0x32;	//���������Ӧ
	byte command_changePwd = 0x33;	//�޸�����
	byte command_changePwd_resp = 0x34;	//�޸������Ӧ
	
}
