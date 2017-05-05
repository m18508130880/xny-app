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

/** 
 * 销售数据处理bean(每天的销售额) 
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
	
	/** 销售 查询
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 * @throws ServletException
	 * @throws IOException
	 */
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
	
	/** 数据图表 Graph
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 */
	public void Graph(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) 
	{
		try
		{
			getHtmlData(request);
			//currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
		    
			switch(currStatus.getFunc_Sub_Id())
			{
			    case 1://时均值
			    	break;
			    case 2://日均值
			    	//request.getSession().setAttribute("Month_" + Sid, Month);
			    	//request.getSession().setAttribute("Year_" + Sid, Year);
			    	break;
			}
			
			msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
			switch(currStatus.getCmd())
			{
				case 20://数据图表
			    	//request.getSession().setAttribute("Graph_" + Sid, (Object)msgBean.getMsg());
					//currStatus.setJsp("Graph.jsp?Sid=" + Sid);
					break;
			}
			
			//request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		   	response.sendRedirect(currStatus.getJsp());
		}
		catch(Exception Ex)
		{
			Ex.printStackTrace();
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
					 " and t.ctime >= date_format(ctime, '%Y-%m-%d %H-%i-%S')" +
					 " and t.ctime <= date_format('"+CTime+"', '%Y-%m-%d %H-%i-%S')" +
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