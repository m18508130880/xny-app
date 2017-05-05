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
import net.sf.json.JSONObject;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

public class AppGisBean extends RmiBean
{
	public final static long serialVersionUID = RmiBean.RMI_APP_GIS;
	public AppGisBean() 
	{
		super.className = "AppGisBean";
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
	    	AppGisJson Json = new AppGisJson();
	    	Json.setUrl(Url);
	    	Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_FAILED, 4));
	    	if(TokenList.containsKey(Token))
	    	{
	    		//根据UId查找管辖的站点
	    		UId = TokenList.get(Token);
				msgBean = pRmi.RmiExec(21, this, 0);
				if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
				{
					IdAll   = ((String)msgBean.getMsg()).substring(4);
					msgBean = pRmi.RmiExec(20, this, 0);
					if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
					{
						String Resp = ((String)msgBean.getMsg());
						Json.setRst(Resp.substring(0,4));
						if(Resp.length() > 4)
						{
							List<Object> CData = new ArrayList<Object>();
							String[] List = Resp.substring(4).split(";");
							for(int i=0; i<List.length && List[i].length()>0; i++)
							{
								AppGisRealJson RealJson = new AppGisRealJson();
								RealJson.setId(List[i].split(",")[0]);
								RealJson.setBrief(List[i].split(",")[1]);
								RealJson.setOnOff(List[i].split(",")[2]);
								RealJson.setLongitude(List[i].split(",")[3]);
								RealJson.setLatitude(List[i].split(",")[4]);
								CData.add(RealJson);
							}
							Json.setCData(CData);
						}
					}
				}
	    	}
	    	else
	    	{
	    		//鉴权失败
	    		Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_ACCOUNT_NOT_LOGIN, 4));
	    	}
	    	
	    	JSONObject jsonObj = JSONObject.fromObject(Json);
	    	response.setCharacterEncoding("UTF-8");
	    	output = response.getWriter();
	    	output.write(jsonObj.toString());
	    	output.flush();
	    	
	    	System.out.println("AppGisJson:" + jsonObj.toString());
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
			case 20://GIS数据
				Sql = "{? = call App_Gis_Real('"+ IdAll +"')}";
				break;
			case 21://获取站点
				Sql = "{? = call App_CPM_Uid('"+ UId +"')}";
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
			setBrief(pRs.getString(3));
			setOnOff(pRs.getString(4));
			setLongitude(pRs.getString(5));
			setLatitude(pRs.getString(6));
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
			setBrief(CommUtil.StrToGB2312(request.getParameter("Brief")));
			setOnOff(CommUtil.StrToGB2312(request.getParameter("OnOff")));
			setLongitude(CommUtil.StrToGB2312(request.getParameter("Longitude")));
			setLatitude(CommUtil.StrToGB2312(request.getParameter("Latitude")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String Token;
	private String Id;
	private String Brief;
	private String OnOff;
	private String Longitude;
	private String Latitude;
	private String IdAll;
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
	public String getBrief() {
		return Brief;
	}
	public void setBrief(String brief) {
		Brief = brief;
	}
	public String getOnOff() {
		return OnOff;
	}
	public void setOnOff(String onOff) {
		OnOff = onOff;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getIdAll() {
		return IdAll;
	}
	public void setIdAll(String idAll) {
		IdAll = idAll;
	}
	public String getUId() {
		return UId;
	}
	public void setUId(String uId) {
		UId = uId;
	}
}