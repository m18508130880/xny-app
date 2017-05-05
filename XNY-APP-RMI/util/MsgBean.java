package util;
import java.io.Serializable;

public class MsgBean implements Serializable
{
	public final static long serialVersionUID = 10;
	public static String ClassName = "MsgBean";
	public long getClassId()
	{
		return serialVersionUID;
	}
	public String getClassName()
	{
		return ClassName;
	}
	public static final int CONST_PAGE_SIZE				= 20; 
	
	//====================ϵͳ״̬	
	public static final int STA_SUCCESS						= 0000;	//�ɹ�
	public static final int STA_ACCOUNT_NOT_EXIST			= 1001; //�û���������
	public static final int STA_ACCOUNT_PWD_ERROR			= 1002;	//�������
	public static final int STA_ACCOUNT_NOT_LOGIN			= 1003;	//��Ȩʧ��
	public static final int STA_FAILED						= 9999;	//ϵͳ��æ

	
	public static String GetResult(int pStatus)
	{
		String RetVal = "";
		switch(pStatus)
		{
			//ϵͳ״̬	
			case STA_SUCCESS: 
			 	RetVal = "�ɹ�";
			 	break;
			case STA_ACCOUNT_NOT_EXIST:
				RetVal = "�û���������";
				break;
			case STA_ACCOUNT_PWD_ERROR:
				RetVal = "�������";
				break;
			case STA_ACCOUNT_NOT_LOGIN:
				RetVal = "��Ȩʧ��";
				break;
			case STA_FAILED: 
			 	RetVal = "ϵͳ��æ";	
			 	break;
			default:
				RetVal = "ϵͳ����";
				break;
		}
		return RetVal;
	}
	public static String GetNetResult(String pStatus)
	{
		String RetVal = "";
		switch(Integer.parseInt(pStatus))
		{
			//ϵͳ״̬
			case STA_SUCCESS:
			 	RetVal = "�ɹ�";
			 	break;
			case STA_ACCOUNT_NOT_EXIST:
				RetVal = "�û���������";
				break;
			case STA_ACCOUNT_PWD_ERROR:
				RetVal = "�������";
				break;
			case STA_ACCOUNT_NOT_LOGIN:
				RetVal = "��Ȩʧ��";
				break;
			case STA_FAILED:
			 	RetVal = "ϵͳ��æ";
			 	break;
			default:
				RetVal = "ϵͳ����";
				break;
		}
		return RetVal;
	}
	private int status = STA_SUCCESS;
	private Object msg;
	private int count = 0;	
	
	public MsgBean(int pStatus, Object pMsg, int pCount) 
	{
		status = pStatus;
		msg = pMsg;
		count = pCount;
	}
	public MsgBean() 
	{
	}
	public Object getMsg() 
	{
		return msg;
	}
	public int getStatus() {
		return status;
	}
	public int getCount() {
		return count;
	}
}