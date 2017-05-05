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
 * 累积流量数据处理bean(用量统计) 
 * AccDataBean数据处理bean
 * @author cui
 * 
 */
public class AccDataBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_ACC_DATA;
	
	/* 获得DataBean的 serialVersionUID (non-Javadoc)
	 * @see rmi.RmiBean#getClassId()
	 */
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public AccDataBean()
	{
		super.className = "AccDataBean";
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
							AccDataBean bean = (AccDataBean)iterator.next();
							AccDataRealJson RealJson = new AccDataRealJson();
							RealJson.setSN(bean.getSN());
							RealJson.setCpm_Id(bean.getCpm_Id());
							RealJson.setCpm_Name(bean.getCpm_Name());
							RealJson.setId(bean.getId());
							RealJson.setCName(bean.getCName());
							RealJson.setAttr_Id(bean.getAttr_Id());
							RealJson.setAttr_Name(bean.getAttr_Name());
							RealJson.setCTime(bean.getCTime());
							RealJson.setB_Value(bean.getB_Value());
							RealJson.setE_Value(bean.getE_Value());
							RealJson.setValue(bean.getValue());
							RealJson.setUnit(bean.getUnit());
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
			case 0://累积流量 (历史) 站点用气表
			   Sql = " SELECT t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.b_value, t.e_value, t.e_value - t.b_value AS VALUE, t.unit, t.des  " +
					 " from view_acc_data_day t " +
					 " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
					 " and t.ctime >= date_format('"+ BDate +"', '%Y-%m-%d %H-%i-%S')" +
					 " and t.ctime <= date_format('"+ EDate +"', '%Y-%m-%d %H-%i-%S')" +
					 " order by t.cpm_id, t.ctime desc ";
			   break;
			case 20://数据图表
			   Sql = " {? = call rmi_graph('"+ Id +"', '"+ currStatus.getFunc_Sub_Id() +"', '"+ currStatus.getVecDate().get(0).toString().substring(0,10) +"')}";
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
			setCTime(pRs.getString(8));
			setB_Value(pRs.getString(9));
			setE_Value(pRs.getString(10));
			setValue(pRs.getString(11));
			setUnit(pRs.getString(12));
			setDes(pRs.getString(13));
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
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setAttr_Id(CommUtil.StrToGB2312(request.getParameter("Attr_Id")));
			setAttr_Name(CommUtil.StrToGB2312(request.getParameter("Attr_Name")));		
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));		
			setB_Value(CommUtil.StrToGB2312(request.getParameter("B_Value")));
			setE_Value(CommUtil.StrToGB2312(request.getParameter("E_Value")));
			setValue(CommUtil.StrToGB2312(request.getParameter("Value")));
			setUnit(CommUtil.StrToGB2312(request.getParameter("Unit")));
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
	private String Id;
	private String CName;
	private String Attr_Id;
	private String Attr_Name;
	private String CTime;
	private String B_Value;
	private String E_Value;
	private String Value;
	private String Unit;
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

	public String getId()
	{
		return Id;
	}

	public void setId(String id)
	{
		Id = id;
	}

	public String getCName()
	{
		return CName;
	}

	public void setCName(String cName)
	{
		CName = cName;
	}

	public String getAttr_Id()
	{
		return Attr_Id;
	}

	public void setAttr_Id(String attr_Id)
	{
		Attr_Id = attr_Id;
	}

	public String getAttr_Name()
	{
		return Attr_Name;
	}

	public void setAttr_Name(String attr_Name)
	{
		Attr_Name = attr_Name;
	}

	public String getCTime()
	{
		return CTime;
	}

	public void setCTime(String cTime)
	{
		CTime = cTime;
	}

	public String getB_Value()
	{
		return B_Value;
	}

	public void setB_Value(String b_Value)
	{
		B_Value = b_Value;
	}

	public String getE_Value()
	{
		return E_Value;
	}

	public void setE_Value(String e_Value)
	{
		E_Value = e_Value;
	}

	public String getValue()
	{
		return Value;
	}

	public void setValue(String value)
	{
		Value = value;
	}

	public String getUnit()
	{
		return Unit;
	}

	public void setUnit(String unit)
	{
		Unit = unit;
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