package rmi;

import java.io.Serializable;
import java.sql.ResultSet;
import util.*;

public abstract class RmiBean implements Serializable
{
	public static final int RMI_APP_LOGIN		= 1;
	public static final int RMI_APP_GIS			= 2;
	public static final int RMI_APP_PRO			= 3;
	public static final int RMI_APP_DATA		= 4;
	public static final int RMI_APP_ALA			= 5;
	public static final int RMI_APP_STAT		= 6;
	public static final int RMI_APP_ROLE		= 7;
	public static final int RMI_APP_PRO_L		= 8;
	public static final int RMI_ACC_DATA		= 9;
	public static final int RMI_ACC_SALE		= 10;
	public static final int RMI_ALARM			= 11;
	public static final int RMI_ALERT			= 12;
	
	
	public MsgBean msgBean = null;
	public String className;
	public CurrStatus currStatus = null;
	
	public RmiBean()
	{
		msgBean = new MsgBean(); 		
	}
	public String getClassName()
	{
		return className;
	}
	
	public abstract long getClassId();
	public abstract String getSql(int pCmd);
	public abstract boolean getData(ResultSet pRs);
}
