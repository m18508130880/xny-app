package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import rmi.Rmi;
import rmi.RmiBean;
import util.*;

public class AlertInfoBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_ALERT;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public AlertInfoBean() 
	{
		super.className = "AlertInfoBean";
	}
	
	//GIS告警处理
	public void GISDeal(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		//currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		PrintWriter outprint = response.getWriter();
		String Resp = "9999";
		
		msgBean = pRmi.RmiExec(13, this, 0);
		if(MsgBean.STA_SUCCESS == msgBean.getStatus())
		{
			Resp = "0000";
			msgBean = pRmi.RmiExec(14, this, 0);
		}
		
		//request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		outprint.write(Resp);
	}
	
	//告警处理
	public void Deal(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		//currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		//类型
		Func_Sub_Id = currStatus.getFunc_Sub_Id()+"";
		if(Func_Sub_Id.equals("9"))
		{
			Func_Sub_Id = "";
		}
		
		//状态
		Func_Sel_Id = currStatus.getFunc_Sel_Id()+"";
		if(Func_Sel_Id.equals("9"))
		{
			Func_Sel_Id = "";
		}
		
		PrintWriter outprint = response.getWriter();
		String Resp = "9999";
		
		msgBean = pRmi.RmiExec(11, this, 0);
		if(MsgBean.STA_SUCCESS == msgBean.getStatus())
		{
			Resp = "0000";
			msgBean = pRmi.RmiExec(12, this, 0);
			
			//重新查询
			setId(Cpm_Id);
			msgBean = pRmi.RmiExec(0, this, currStatus.getCurrPage());
			//request.getSession().setAttribute("Alert_Info_" + Sid, ((Object)msgBean.getMsg()));
	    	currStatus.setTotalRecord(msgBean.getCount());
		}
		
		//request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		outprint.write(Resp);
	}
	
	//告警查询
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
	    		Json.setRst(CommUtil.IntToStringLeftFillZero(msgBean.getStatus(), 4));
				//类型
				Func_Sub_Id = "";
				//状态
				Func_Sel_Id = "";
				
				msgBean = pRmi.RmiExec(currStatus.getCmd(), this, currStatus.getCurrPage());
				if(null != msgBean.getMsg())
				{
					ArrayList<?> List = (ArrayList<?>)msgBean.getMsg();
					if(List.size() > 0)
					{
						List<Object> CData = new ArrayList<Object>();
						Iterator<?> iterator = List.iterator();
						while(iterator.hasNext())
						{
							AlertInfoBean bean = (AlertInfoBean)iterator.next();
							AlertInfoRealJson RealJson = new AlertInfoRealJson();
							RealJson.setSN(bean.getSN());
							RealJson.setCpm_Id(bean.getCpm_Id());
							RealJson.setCpm_Name(bean.getCpm_Name());
							RealJson.setId(bean.getId());
							RealJson.setCName(bean.getCName());
							RealJson.setAttr_Id(bean.getAttr_Id());
							RealJson.setAttr_Name(bean.getAttr_Name());
							RealJson.setLevel(bean.getLevel());
							RealJson.setCTime(bean.getCTime());
							RealJson.setCData(bean.getCData());
							RealJson.setLev(bean.getLev());
							RealJson.setStatus(bean.getStatus());
							RealJson.setETime(bean.getETime());
							RealJson.setOperator(bean.getOperator());
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
	    	JSONObject jsonObj = JSONObject.fromObject(Json);
	    	response.setCharacterEncoding("UTF-8");
	    	output = response.getWriter();
	    	output.write(jsonObj.toString());
	    	output.flush();
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
			case 0://查询
				switch(currStatus.getFunc_Sel_Id())
				{
					case 0:
						Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.level, t.ctime, t.cdata, t.lev, t.status, t.etime, t.operator " +
					  	  	  " from view_alert_info t " +
					  	  	  " where instr('"+ Id +"', t.cpm_id) > 0 " +
					  	  	  "   and t.level like '"+ Func_Sub_Id +"%'" +
					  	  	  "   and t.status like '"+ Func_Sel_Id +"%'" +
					  	  	  "   order by t.ctime desc ";
						break;
					default:
						Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.level, t.ctime, t.cdata, t.lev, t.status, t.etime, t.operator " +
						  	  " from view_alert_info t " +
						  	  " where instr('"+ Id +"', t.cpm_id) > 0 " +
						  	  "   and t.level like '"+ Func_Sub_Id +"%'" +
						  	  "   and t.status like '"+ Func_Sel_Id +"%'" +
						  	  "   and t.ctime >= date_format('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m-%d %H-%i-%S')" +
						  	  "   and t.ctime <= date_format('"+currStatus.getVecDate().get(1).toString()+"', '%Y-%m-%d %H-%i-%S')" +
						  	  "   order by t.ctime desc ";
						break;
				}
				break;
			case 11://处理
				Sql = " update alert_info t set t.status = '1', t.etime = date_format(NOW(), '%Y-%m-%d %H:%i:%S'), t.operator = '"+ Operator +"' where t.sn = '"+ SN +"' and t.status = '0'";
				break;
			case 12://处理
				Sql = " update alert_now  t set t.status = '1', t.etime = date_format(NOW(), '%Y-%m-%d %H:%i:%S'), t.operator = '"+ Operator +"' " +
					  " where t.cpm_id = '"+ Cpm_Id +"' " +
					  "   and t.id = '"+ Id +"' " +
					  "   and t.attr_id = '"+ Attr_Id +"'" +
					  "   and t.status = '0'";
				break;
			case 13://GIS处理
				Sql = " update alert_now  t set t.status = '1', t.etime = date_format(NOW(), '%Y-%m-%d %H:%i:%S'), t.operator = '"+ Operator +"' where t.sn = '"+ SN +"' and t.status = '0'";
				break;
			case 14://GIS处理
				Sql = " update alert_info t set t.status = '1', t.etime = date_format(NOW(), '%Y-%m-%d %H:%i:%S'), t.operator = '"+ Operator +"' " +
					  " where t.cpm_id = '"+ Cpm_Id +"' " +
					  "   and t.id = '"+ Id +"' " +
					  "   and t.attr_id = '"+ Attr_Id +"'" +
					  "   and t.status = '0'";
				break;
		}
		return Sql;
	}
	
	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setSN(pRs.getString(1));
			setCpm_Id(pRs.getString(2));
			setCpm_Name(pRs.getString(3));
			setId(pRs.getString(4));
			setCName(pRs.getString(5));
			setAttr_Id(pRs.getString(6));
			setAttr_Name(pRs.getString(7));
			setLevel(pRs.getString(8));
			setCTime(pRs.getString(9));
			setCData(pRs.getString(10));
			setLev(pRs.getString(11));
			setStatus(pRs.getString(12));
			setETime(pRs.getString(13));
			setOperator(pRs.getString(14));
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
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setCpm_Id(CommUtil.StrToGB2312(request.getParameter("Cpm_Id")));
			setCpm_Name(CommUtil.StrToGB2312(request.getParameter("Cpm_Name")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setAttr_Id(CommUtil.StrToGB2312(request.getParameter("Attr_Id")));
			setAttr_Name(CommUtil.StrToGB2312(request.getParameter("Attr_Name")));
			setLevel(CommUtil.StrToGB2312(request.getParameter("Level")));
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));
			setCData(CommUtil.StrToGB2312(request.getParameter("CData")));
			setLev(CommUtil.StrToGB2312(request.getParameter("Lev")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
			setETime(CommUtil.StrToGB2312(request.getParameter("ETime")));
			setOperator(CommUtil.StrToGB2312(request.getParameter("Operator")));
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
		}
		catch (Exception Exp) 
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String SN;
	private String Cpm_Id;
	private String Cpm_Name;
	private String Id;
	private String CName;
	private String Attr_Id;
	private String Attr_Name;
	private String Level;
	private String CTime;
	private String CData;
	private String Lev;
	private String Status;
	private String ETime;
	private String Operator;
	
	private String Func_Sub_Id;
	private String Func_Sel_Id;
	private String Token;
	
	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

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

	public String getLevel() {
		return Level;
	}

	public void setLevel(String level) {
		Level = level;
	}

	public String getCTime() {
		return CTime;
	}

	public void setCTime(String cTime) {
		CTime = cTime;
	}

	public String getCData() {
		return CData;
	}

	public void setCData(String cData) {
		CData = cData;
	}

	public String getLev() {
		return Lev;
	}

	public void setLev(String lev) {
		Lev = lev;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getETime() {
		return ETime;
	}

	public void setETime(String eTime) {
		ETime = eTime;
	}

	public String getOperator() {
		return Operator;
	}

	public void setOperator(String operator) {
		Operator = operator;
	}

	public String getFunc_Sub_Id() {
		return Func_Sub_Id;
	}

	public void setFunc_Sub_Id(String funcSubId) {
		Func_Sub_Id = funcSubId;
	}

	public String getFunc_Sel_Id() {
		return Func_Sel_Id;
	}

	public void setFunc_Sel_Id(String funcSelId) {
		Func_Sel_Id = funcSelId;
	}

	public String getToken()
	{
		return Token;
	}

	public void setToken(String token)
	{
		Token = token;
	}

}
