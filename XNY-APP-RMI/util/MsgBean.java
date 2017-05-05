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
	
	//====================系统状态	
	public static final int STA_SUCCESS						= 0000;	//成功
	public static final int STA_ACCOUNT_NOT_EXIST			= 1001; //用户名不存在
	public static final int STA_ACCOUNT_PWD_ERROR			= 1002;	//密码错误
	public static final int STA_ACCOUNT_NOT_LOGIN			= 1003;	//鉴权失败
	public static final int STA_FAILED						= 9999;	//系统繁忙

	
	public static String GetResult(int pStatus)
	{
		String RetVal = "";
		switch(pStatus)
		{
			//系统状态	
			case STA_SUCCESS: 
			 	RetVal = "成功";
			 	break;
			case STA_ACCOUNT_NOT_EXIST:
				RetVal = "用户名不存在";
				break;
			case STA_ACCOUNT_PWD_ERROR:
				RetVal = "密码错误";
				break;
			case STA_ACCOUNT_NOT_LOGIN:
				RetVal = "鉴权失败";
				break;
			case STA_FAILED: 
			 	RetVal = "系统繁忙";	
			 	break;
			default:
				RetVal = "系统错误";
				break;
		}
		return RetVal;
	}
	public static String GetNetResult(String pStatus)
	{
		String RetVal = "";
		switch(Integer.parseInt(pStatus))
		{
			//系统状态
			case STA_SUCCESS:
			 	RetVal = "成功";
			 	break;
			case STA_ACCOUNT_NOT_EXIST:
				RetVal = "用户名不存在";
				break;
			case STA_ACCOUNT_PWD_ERROR:
				RetVal = "密码错误";
				break;
			case STA_ACCOUNT_NOT_LOGIN:
				RetVal = "鉴权失败";
				break;
			case STA_FAILED:
			 	RetVal = "系统繁忙";
			 	break;
			default:
				RetVal = "系统错误";
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