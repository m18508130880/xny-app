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

import com.alibaba.fastjson.JSONObject;

import rmi.Rmi;
import rmi.RmiBean;
import util.*;

/**
 * 告警数据
 * @author Administrator
 *
 */
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
				
				msgBean = pRmi.RmiExec(0, this, 1);
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
	    	JSONObject jsonObj = (JSONObject) JSONObject.toJSON(Json);
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
				Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.level, t.ctime, t.cdata, t.lev, t.status, t.etime, t.operator " +
				  	  " from view_alert_info t " +
				  	  " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
				  	  "   and t.ctime >= date_format('"+BDate+"', '%Y-%m-%d %H-%i-%S')" +
				  	  "   and t.ctime <= date_format('"+EDate+"', '%Y-%m-%d %H-%i-%S')" +
				  	  "   order by t.ctime desc ";
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
			setBDate(CommUtil.StrToGB2312(request.getParameter("BDate")));
			setEDate(CommUtil.StrToGB2312(request.getParameter("EDate")));
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
	
	private String Token;
	private String BDate;
	private String EDate;
	
	public String getBDate()
	{
		return BDate;
	}

	public void setBDate(String bDate)
	{
		BDate = bDate;
	}

	public String getEDate()
	{
		return EDate;
	}

	public void setEDate(String eDate)
	{
		EDate = eDate;
	}

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

	public String getToken()
	{
		return Token;
	}

	public void setToken(String token)
	{
		Token = token;
	}

}
