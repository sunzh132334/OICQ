package com.qq.msg;

/**
 * �������ǻ�Ӧ��Ϣ
 * @author yy
 *
 */
public class MsgForgetResp extends MsgHead {
	
	@Override
	public String toString() {
		return "MsgForgetResp [question=" + question + ", answer=" + answer
				+ "]";
	}
	private String question;	//�ܱ�����
	private String answer;	//�ܱ���
	
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
