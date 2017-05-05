package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

public class AppLoginBean extends RmiBean
{
	public final static long serialVersionUID = RmiBean.RMI_APP_LOGIN;
	public AppLoginBean() 
	{
		super.className = "AppLoginBean";
	}
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public void Login(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, String Url, HashMap<String , String> TokenList) throws IOException 
	{
		PrintWriter output = null;
	    try
	    {
	    	getHtmlData(request);
			AppLoginJson Json = new AppLoginJson();
			Json.setUrl(Url);
			Json.setUId(UId);
			Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_FAILED, 4));
			
	    	//登入验证
			msgBean = pRmi.RmiExec(20, this, 0);
			Json.setRst(CommUtil.IntToStringLeftFillZero(msgBean.getStatus(), 4));
			
			//登入成功
			if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
			{
				//身份令牌
				String _Token = (CommUtil.BytesToHexString(new util.Md5().encrypt((CommUtil.SessionId()+"BESTAPP").getBytes()), 16)).toUpperCase();
				Json.setToken(_Token);
				TokenList.put(_Token, UId);
			}
			
	    	JSONObject jsonObj = (JSONObject) JSONObject.toJSON(Json);
	    	output = response.getWriter();
	    	output.write(jsonObj.toString());
	    	output.flush();
	    	
	    	System.out.println("AppLoginJson:" + jsonObj.toString());
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	output.close();
	    }
	}
    
	public void Logout(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, String Url, HashMap<String , String> TokenList) throws IOException 
	{
		PrintWriter output = null;
	    try
	    {
	    	getHtmlData(request);
	    	TokenList.remove(Token);
			AppLogoutJson Json = new AppLogoutJson();
			Json.setUrl(Url);
			Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_SUCCESS, 4));
			
			JSONObject jsonObj = (JSONObject) JSONObject.toJSON(Json);
	    	output = response.getWriter();
	    	output.write(jsonObj.toString());
	    	output.flush();
	    	
	    	System.out.println("AppLogoutJson:" + jsonObj.toString());
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	output.close();
	    }
	}
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd) 
		{
			case 20://登录验证
				Sql = "{? = call App_Login('"+ UId +"', '"+ Msg +"')}";
				break;
			case 21://获取站点
				Sql = "{? = call App_CPM_Uid('"+ UId +"')}";
				break;
			case 22://获取数据
				Sql = "{? = call App_Dat_Get('"+ IdAll +"')}";
				break;
		}
		return Sql;
	}
	
	public boolean getData(ResultSet pRs) 
	{
		boolean IsOK = true;
		try
		{
			setUId(pRs.getString(1));
			setMsg(pRs.getString(2));
			setToken(pRs.getString(3));
		}
		catch (SQLException sqlExp)
		{
			sqlExp.printStackTrace();
		}
		return IsOK;
	}
	
	public boolean getHtmlData(HttpServletRequest request)
	{
		boolean IsOK = true;
		try
		{
			setUId(CommUtil.StrToGB2312(request.getParameter("UId")));
			setMsg(CommUtil.StrToGB2312(request.getParameter("Msg")));
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String UId;
	private String Msg;
	private String Token;
	private String IdAll;
	
	public String getUId() {
		return UId;
	}
	public void setUId(String uId) {
		UId = uId;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getIdAll() {
		return IdAll;
	}
	public void setIdAll(String idAll) {
		IdAll = idAll;
	}
}