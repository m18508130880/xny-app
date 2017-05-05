package servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rmi.*;
import util.*;
import bean.*;

////0全部查询 2插入 3修改 4删除 10～19单个查询
public class MainServlet extends HttpServlet
{
	public final static long serialVersionUID = 1000;
	private Rmi m_Rmi = null;
	private String rmiUrl = null;
	private Connect connect = null;
	public ServletConfig Config;
	public HashMap<String , String> TokenList = new HashMap<String , String>();
	
	public final ServletConfig getServletConfig() 
	{
		return Config;
	}
	
	public void init(ServletConfig pConfig) throws ServletException
	{	
		Config = pConfig;
		connect = new Connect();
		connect.config = pConfig;
		connect.ReConnect();
	}		
    protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    protected void doPut(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    protected void doTrace(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    

    protected void processRequest(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
    	if(connect.Test()== false)
    	{
    		request.getSession().setAttribute("ErrMsg", CommUtil.StrToGB2312("RMI服务端未正常运行，无法登陆！"));
    		response.sendRedirect(getUrl(request) + "error.jsp");
    		return;
    	}
    	
        response.setContentType("text/html;charset=gb2312;");
        String strUrl = request.getRequestURI();
        String[] str = strUrl.split("/");
        strUrl = str[str.length - 1];
        System.out.println("********************" + strUrl + "[" + request.getRemoteAddr() + "]");
        
        if(strUrl.equals("AppLogin.do"))
        	new AppLoginBean().Login(request, response, m_Rmi, strUrl, TokenList);     	//登入接口
        if(strUrl.equals("AppLogout.do"))
        	new AppLoginBean().Logout(request, response, m_Rmi, strUrl, TokenList);    	//登出接口
        if(strUrl.equals("AppGisReal.do"))
        	new AppGisBean().Real(request, response, m_Rmi, strUrl, TokenList);        	//GIS展示接口
        if(strUrl.equals("DataReal.do"))
        	new AppDataBean().Real(request, response, m_Rmi, strUrl, TokenList);       	//实时数据接口
        if(strUrl.equals("DataPast.do"))
        	new AppDataBean().Past(request, response, m_Rmi, strUrl, TokenList);       	//历史数据接口
        if(strUrl.equals("Alert_Info.do"))	                 	 						
        	new AlertInfoBean().Real(request, response, m_Rmi, strUrl, TokenList);		//告警日志
        if(strUrl.equals("Acc_Data.do"))				         						
            new AccDataBean().Real(request, response, m_Rmi, strUrl, TokenList);  		//累积流量数据
        if(strUrl.equals("Acc_Sale.do"))				         						
        	new AccSaleBean().Real(request, response, m_Rmi, strUrl, TokenList);  		//销售数据
        
        
    }
    private class Connect extends Thread
	{
    	private ServletConfig config = null;
    	public boolean Test()
    	{
    		int i = 0;
        	boolean ok = false;
        	while(3 > i)
    		{        		
    	    	try
    			{   
    	    		if(i != 0) sleep(500);
    	    		ok = m_Rmi.Test();
    	    		i = 3;
    	    		ok = true;
    			}
    	    	catch(Exception e)
    			{    	    		
    	    		ReConnect();
    	    		i++;
    			}
    		}
    		return ok;
    	}
    	private void ReConnect()
    	{
    		try
    		{
    			rmiUrl = config.getInitParameter("rmiUrl");
    			Context context = new InitialContext();
    			m_Rmi = (Rmi) context.lookup(rmiUrl);
    		}
    		catch(Exception e)
    		{	
    			e.printStackTrace();
    		}
    	}
    }
	public final static String getUrl(HttpServletRequest request)
	{
		String url = "http://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath() + "/";
		return url;
	}
	
} 