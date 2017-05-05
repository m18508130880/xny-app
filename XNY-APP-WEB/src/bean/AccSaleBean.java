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

import com.alibaba.fastjson.JSONObject;

import rmi.Rmi;
import rmi.RmiBean;
import util.*;

/** 
 * 销售数据处理bean(销售统计) 
 * AccDataBean数据处理bean
 * @author cui
 * 
 */
public class AccSaleBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_ACC_SALE;
	
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public AccSaleBean()
	{
		super.className = "AccSaleBean";
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
							AccSaleBean bean = (AccSaleBean)iterator.next();
							AccSaleRealJson RealJson = new AccSaleRealJson();
							RealJson.setSN(bean.getSN());
							RealJson.setCpm_Id(bean.getCpm_Id());
							RealJson.setCpm_Name(bean.getCpm_Name());
							RealJson.setCTime(bean.getCTime());
							RealJson.setUnit_Price(bean.getUnit_Price());
							RealJson.setValue(bean.getValue());
							RealJson.setSalary(bean.getSalary());
							RealJson.setDes(bean.getDes()); 
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
			case 0://累积銷售 (历史) 站点销售表
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.ctime, t.unit_price, t.value, t.salary, t.des " +
					 " from view_Acc_Sale_day t " +
					 " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
					 " and t.ctime >= date_format('"+ BDate +"', '%Y-%m-%d %H-%i-%S')" +
					 " and t.ctime <= date_format('"+ EDate +"', '%Y-%m-%d %H-%i-%S')" +
					 " order by t.cpm_id, t.ctime desc ";
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
			setCTime(pRs.getString(4));
			setUnit_Price(pRs.getString(5));
			setValue(pRs.getString(6));
			setSalary(pRs.getString(7));
			setDes(pRs.getString(8));
		} 
		catch (SQLException sqlExp) 
		{
			sqlExp.printStackTrace();
		}		
		return IsOK;
	}
	
	/** 获取request页面 SN、ID、Cpm_Id、Level 等数据
	 * @param request
	 * @return
	 */
	public boolean getHtmlData(HttpServletRequest request) 
	{
		boolean IsOK = true;
		try 
		{	
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setCpm_Id(CommUtil.StrToGB2312(request.getParameter("Cpm_Id")));
			setCpm_Name(CommUtil.StrToGB2312(request.getParameter("Cpm_Name")));
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));		
			setUnit_Price(CommUtil.StrToGB2312(request.getParameter("Unit_Price")));
			setValue(CommUtil.StrToGB2312(request.getParameter("Value")));
			setSalary(CommUtil.StrToGB2312(request.getParameter("Salary")));
			setDes(CommUtil.StrToGB2312(request.getParameter("Des")));
			
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
	private String CTime;
	private String Unit_Price;
	private String Value;
	private String Salary;
	private String Des;
	
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

	public String getToken()
	{
		return Token;
	}

	public void setToken(String token)
	{
		Token = token;
	}

	public String getUnit_Price() {
		return Unit_Price;
	}

	public void setUnit_Price(String unit_Price) {
		Unit_Price = unit_Price;
	}

	public String getSalary() {
		return Salary;
	}

	public void setSalary(String salary) {
		Salary = salary;
	}

	public String getSN()
	{
		return SN;
	}

	public void setSN(String sN)
	{
		SN = sN;
	}

	public String getCpm_Id()
	{
		return Cpm_Id;
	}

	public void setCpm_Id(String cpm_Id)
	{
		Cpm_Id = cpm_Id;
	}
	
	public String getCpm_Name()
	{
		return Cpm_Name;
	}

	public void setCpm_Name(String cpm_Name)
	{
		Cpm_Name = cpm_Name;
	}

	

	public String getCTime()
	{
		return CTime;
	}

	public void setCTime(String cTime)
	{
		CTime = cTime;
	}

	
	public String getValue()
	{
		return Value;
	}

	public void setValue(String value)
	{
		Value = value;
	}

	public String getDes()
	{
		return Des;
	}

	public void setDes(String des)
	{
		Des = des;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	
	
	
}