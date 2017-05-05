package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

public class AppRoleBean extends RmiBean
{
	public final static long serialVersionUID = RmiBean.RMI_APP_ROLE;
	public AppRoleBean() 
	{
		super.className = "AppRoleBean";
	}
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public void Real(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, String Url, HashMap<String , String> TokenList) throws IOException 
	{
		PrintWriter output = null;
	    try
	    {
	    	getHtmlData(request);
	    	AppRoleJson Json = new AppRoleJson();
	    	Json.setUrl(Url);
	    	Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_FAILED, 4));
	    	if(TokenList.containsKey(Token))
	    	{
				UId = TokenList.get(Token);
				msgBean = pRmi.RmiExec(20, this, 1);
				if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
				{
					String Resp = ((String)msgBean.getMsg());
					Json.setRst(Resp.substring(0,4));
					if(Resp.length() > 4)
					{
						List<Object> CData = new ArrayList<Object>();
						String[] List = Resp.substring(4).split("\\~");
						for(int i=0; i<List.length && List[i].length()>0; i++)
						{
							String[] subList = List[i].split("\\^");
							AppRoleRealJson RealJson = new AppRoleRealJson();
							RealJson.setId(subList[0].trim());
							RealJson.setCName(subList[1].trim());
							RealJson.setPoint(subList[2].trim());
							CData.add(RealJson);
						}
						Json.setCData(CData);
					}
				}
	    	}
	    	else
	    	{
	    		//鉴权失败
	    		Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_ACCOUNT_NOT_LOGIN, 4));
	    	}
	    	
	    	JSONObject jsonObj = (JSONObject) JSONObject.toJSON(Json);
	    	output = response.getWriter();
	    	output.write(jsonObj.toString());
	    	output.flush();
	    	
	    	System.out.println("AppRoleJson:" + jsonObj.toString());
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
			case 20://权限数据
				Sql = "{? = call App_Role_Real('"+ UId +"')}";
				break;
		}
		return Sql;
	}
	
	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setToken(pRs.getString(1));
			setId(pRs.getString(2));
			setCName(pRs.getString(3));
			setPoint(pRs.getString(4));
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
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setPoint(CommUtil.StrToGB2312(request.getParameter("Point")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String Token;
	private String Id;
	private String CName;
	private String Point;
	private String UId;
	
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getCName() {
		return CName;
	}
	public void setCName(String cName) {
		CName = cName;
	}
	public String getPoint() {
		return Point;
	}
	public void setPoint(String point) {
		Point = point;
	}
	public String getUId() {
		return UId;
	}
	public void setUId(String uId) {
		UId = uId;
	}
}