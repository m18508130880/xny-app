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

public class AlarmInfoBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_ALARM;
	public long getClassId()
	{
		return serialVersionUID;
	}

	public AlarmInfoBean() 
	{
		super.className = "AlarmInfoBean";
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
				Json.setRst(CommUtil.IntToStringLeftFillZero(msgBean.getStatus(), 4));
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
							AlarmInfoBean bean = (AlarmInfoBean)iterator.next();
							AlarmInfoRealJson RealJson = new AlarmInfoRealJson();
							RealJson.setSN(bean.getSN());
							RealJson.setCpm_Id(bean.getCpm_Id());
							RealJson.setCpm_Name(bean.getCpm_Name());
							RealJson.setS_Id(bean.getS_Id());
							RealJson.setS_CName(bean.getS_CName());
							RealJson.setS_Attr_Id(bean.getS_Attr_Id());
							RealJson.setS_Attr_Name(bean.getS_Attr_Name());
							RealJson.setS_Attr_Value(bean.getS_Attr_Value());
							RealJson.setD_Id(bean.getD_Id());
							RealJson.setD_CName(bean.getD_CName());
							RealJson.setD_Act_Id(bean.getD_Act_Id());
							RealJson.setD_Act_Name(bean.getD_Act_Name());
							RealJson.setCData(bean.getCData());
							RealJson.setCTime(bean.getCTime());
							RealJson.setOperator(bean.getOperator());
							RealJson.setStatus(bean.getStatus());
						}
						Json.setCData(CData);
					}
				}
	    	}
	    	else
	    	{
	    		//¼øÈ¨Ê§°Ü
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
			case 0://²éÑ¯
				Sql = " select t.sn, t.cpm_id, t.cpm_name, t.s_id, t.s_cname, t.s_attr_id, t.s_attr_name, t.s_attr_value, t.d_id, t.d_cname, t.d_act_id, t.d_act_name, t.cdata, t.ctime, t.operator, t.status " +
					  " from view_alarm_info t " +
					  " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
					  "   and t.ctime >= date_format('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m-%d %H-%i-%S')" +
					  "   and t.ctime <= date_format('"+currStatus.getVecDate().get(1).toString()+"', '%Y-%m-%d %H-%i-%S')" +
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
			setS_Id(pRs.getString(4));
			setS_CName(pRs.getString(5));
			setS_Attr_Id(pRs.getString(6));
			setS_Attr_Name(pRs.getString(7));
			setS_Attr_Value(pRs.getString(8));
			setD_Id(pRs.getString(9));
			setD_CName(pRs.getString(10));
			setD_Act_Id(pRs.getString(11));
			setD_Act_Name(pRs.getString(12));
			setCData(pRs.getString(13));
			setCTime(pRs.getString(14));
			setOperator(pRs.getString(15));
			setStatus(pRs.getString(16));
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
			setS_Id(CommUtil.StrToGB2312(request.getParameter("S_Id")));
			setS_CName(CommUtil.StrToGB2312(request.getParameter("S_CName")));
			setS_Attr_Id(CommUtil.StrToGB2312(request.getParameter("S_Attr_Id")));
			setS_Attr_Name(CommUtil.StrToGB2312(request.getParameter("S_Attr_Name")));
			setS_Attr_Value(CommUtil.StrToGB2312(request.getParameter("S_Attr_Value")));
			setD_Id(CommUtil.StrToGB2312(request.getParameter("D_Id")));
			setD_CName(CommUtil.StrToGB2312(request.getParameter("D_CName")));
			setD_Act_Id(CommUtil.StrToGB2312(request.getParameter("D_Act_Id")));
			setD_Act_Name(CommUtil.StrToGB2312(request.getParameter("D_Act_Name")));
			setCData(CommUtil.StrToGB2312(request.getParameter("CData")));
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));
			setOperator(CommUtil.StrToGB2312(request.getParameter("Operator")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
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
	private String S_Id;
	private String S_CName;
	private String S_Attr_Id;
	private String S_Attr_Name;
	private String S_Attr_Value;
	private String D_Id;
	private String D_CName;
	private String D_Act_Id;
	private String D_Act_Name;
	private String CData;
	private String CTime;
	private String Operator;
	private String Status;
	
	private String Token;
	
	public String getToken()
	{
		return Token;
	}

	public void setToken(String token)
	{
		Token = token;
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

	public String getS_Id() {
		return S_Id;
	}

	public void setS_Id(String sId) {
		S_Id = sId;
	}

	public String getS_CName() {
		return S_CName;
	}

	public void setS_CName(String sCName) {
		S_CName = sCName;
	}

	public String getS_Attr_Id() {
		return S_Attr_Id;
	}

	public void setS_Attr_Id(String sAttrId) {
		S_Attr_Id = sAttrId;
	}

	public String getS_Attr_Name() {
		return S_Attr_Name;
	}

	public void setS_Attr_Name(String sAttrName) {
		S_Attr_Name = sAttrName;
	}

	public String getS_Attr_Value() {
		return S_Attr_Value;
	}

	public void setS_Attr_Value(String sAttrValue) {
		S_Attr_Value = sAttrValue;
	}

	public String getD_Id() {
		return D_Id;
	}

	public void setD_Id(String dId) {
		D_Id = dId;
	}

	public String getD_CName() {
		return D_CName;
	}

	public void setD_CName(String dCName) {
		D_CName = dCName;
	}

	public String getD_Act_Id() {
		return D_Act_Id;
	}

	public void setD_Act_Id(String dActId) {
		D_Act_Id = dActId;
	}

	public String getD_Act_Name() {
		return D_Act_Name;
	}

	public void setD_Act_Name(String dActName) {
		D_Act_Name = dActName;
	}

	public String getCData() {
		return CData;
	}

	public void setCData(String cData) {
		CData = cData;
	}

	public String getCTime() {
		return CTime;
	}

	public void setCTime(String cTime) {
		CTime = cTime;
	}

	public String getOperator() {
		return Operator;
	}

	public void setOperator(String operator) {
		Operator = operator;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
	
}
