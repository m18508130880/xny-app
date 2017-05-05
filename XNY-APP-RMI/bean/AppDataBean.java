package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

public class AppDataBean extends RmiBean
{
	public final static long serialVersionUID = RmiBean.RMI_APP_DATA;
	public AppDataBean() 
	{
		super.className = "AppDataBean";
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
	    	AppDataJson Json = new AppDataJson();
	    	Json.setUrl(Url);
	    	Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_FAILED, 4));
	    	if(TokenList.containsKey(Token))
	    	{
	    		//根据Level查找管辖的站点
//				msgBean = pRmi.RmiExec(20, this, 0);
//				if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
//				{
					Json.setRst(CommUtil.IntToStringLeftFillZero(msgBean.getStatus(), 4));
					IdAll   = Level;
					msgBean = pRmi.RmiExec(0, this, 0);
					if(null != msgBean.getMsg())
					{
						ArrayList<?> List = (ArrayList<?>)msgBean.getMsg();
						if(List.size() > 0)
						{
							List<Object> CData = new ArrayList<Object>();
							Iterator<?> iterator = List.iterator();
							while(iterator.hasNext())
							{
								AppDataBean dataBean = (AppDataBean)iterator.next();
								AppDataRealJson RealJson = new AppDataRealJson();
								RealJson.setCpm_Id(dataBean.getCpm_Id());
								RealJson.setCpm_Name(dataBean.getCpm_Name());
								RealJson.setId(dataBean.getId());
								RealJson.setCName(dataBean.getCName());
								RealJson.setAttr_Id(dataBean.getAttr_Id());
								RealJson.setAttr_Name(dataBean.getAttr_Name());
								RealJson.setCTime(dataBean.getCTime());
								RealJson.setValue(dataBean.getValue());
								RealJson.setUnit(dataBean.getUnit());
								CData.add(RealJson);
							}
							Json.setCData(CData);
						}
					}
				//}
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
	    	
	    	System.out.println("AppDataJson:" + jsonObj.toString());
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
			case 0://生产数据
				Sql = " select t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.value, t.unit " +
			  	  	  " from view_data_now t " +
			  	  	  " where instr('"+ IdAll +"', t.cpm_id) > 0 " +
			  	  	  " and t.ctime in (select max(a.ctime) from view_data_now a where a.cpm_id = t.cpm_id and a.id = t.id and a.attr_id = t.attr_id group by a.cpm_id, a.id, a.attr_id) " +
			  	  	  " order by t.cpm_id, t.id, t.attr_id asc, t.ctime desc ";
				break;
			case 20://获取站点
				Sql = "{? = call App_CPM_Get('"+ Level +"')}";
				break;
		}
		return Sql;
	}
	
	public boolean getData(ResultSet pRs) 
	{
		boolean IsOK = true;
		try
		{
			setCpm_Id(pRs.getString(1));
			setCpm_Name(pRs.getString(2));			
			setId(pRs.getString(3));
			setCName(pRs.getString(4));		
			setAttr_Id(pRs.getString(5));
			setAttr_Name(pRs.getString(6));			
			setCTime(pRs.getString(7));
			setValue(pRs.getString(8));
			setUnit(pRs.getString(9));
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
			setCpm_Id(CommUtil.StrToGB2312(request.getParameter("Cpm_Id")));
			setCpm_Name(CommUtil.StrToGB2312(request.getParameter("Cpm_Name")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setAttr_Id(CommUtil.StrToGB2312(request.getParameter("Attr_Id")));
			setAttr_Name(CommUtil.StrToGB2312(request.getParameter("Attr_Name")));
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));
			setValue(CommUtil.StrToGB2312(request.getParameter("Value")));
			setUnit(CommUtil.StrToGB2312(request.getParameter("Unit")));
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
			setLevel(CommUtil.StrToGB2312(request.getParameter("Level")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String Cpm_Id;
	private String Cpm_Name;
	private String Id;
	private String CName;
	private String Attr_Id;
	private String Attr_Name;
	private String CTime;
	private String Value;
	private String Unit;
	private String Token;
	private String Level;
	private String IdAll;
	
	public String getCpm_Id() {
		return Cpm_Id;
	}
	public void setCpm_Id(String cpmId) {
		Cpm_Id = cpmId;
	}
	public String getCpm_Name() {
		return Cpm_Name;
	}
	public void setCpm_Name(String cpmName) {
		Cpm_Name = cpmName;
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
	public String getAttr_Id() {
		return Attr_Id;
	}
	public void setAttr_Id(String attrId) {
		Attr_Id = attrId;
	}
	public String getAttr_Name() {
		return Attr_Name;
	}
	public void setAttr_Name(String attrName) {
		Attr_Name = attrName;
	}
	public String getCTime() {
		return CTime;
	}
	public void setCTime(String cTime) {
		CTime = cTime;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getLevel() {
		return Level;
	}
	public void setLevel(String level) {
		Level = level;
	}
	public String getIdAll() {
		return IdAll;
	}
	public void setIdAll(String idAll) {
		IdAll = idAll;
	}
}