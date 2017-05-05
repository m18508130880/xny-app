package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
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
				
				//根据UId查找管辖的站点
				/*msgBean = pRmi.RmiExec(21, this, 0);
				if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
				{
					IdAll   = ((String)msgBean.getMsg()).substring(4);
					msgBean = pRmi.RmiExec(22, this, 0);
					if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
					{
						String Resp = ((String)msgBean.getMsg());
						if(Resp.length() > 4)
						{
							//相关数据
							HashMap<String , Object> CData = new HashMap<String , Object>();
							String[] List = Resp.substring(4).split(";");
							CData.put("GIS_Alert_Cnt", List[0]);
							CData.put("PRO_M_All", List[1]);
							CData.put("PRO_Y_All", List[2]);
							CData.put("PRO_Car_All", List[3]);
							CData.put("ALA_Alert_Cnt", List[4]);
							Json.setCData(CData);
						}
					}
				}*/
			}
	    	
			
			//登入列表
			/*Iterator<?> iter = TokenList.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry<String, String> entry = (Map.Entry)iter.next();
				String key = entry.getKey();
				
				System.out.println("Login:" + key);
			}*/
			
			
	    	JSONObject jsonObj = JSONObject.fromObject(Json);
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
			
	    	JSONObject jsonObj = JSONObject.fromObject(Json);
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