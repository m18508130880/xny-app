<%@ page contentType="text/html; charset=gb2312" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>LNG-APP</title>
<script type='text/javascript' src='js/md5.js'></script>
<script>
var Token = '';
function doLOG()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      Token = Info.token;
	      alert('[Rst:' + Info.rst + '][Token:' + Token + ']');
	      //alert(Info.CData.GIS_Alert_Cnt);
	      //alert(Info.CData.PRO_M_All);
	      //alert(Info.CData.PRO_Y_All);
	      //alert(Info.CData.PRO_Car_All);
	      //alert(Info.CData.ALA_Alert_Cnt);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppLogin.do?UId=cf&Msg='+hex_md5('cf111111').toUpperCase()+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doOUT()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppLogout.do?Token='+Token+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doGIS()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	      alert(Info.CData[0].id);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppGisReal.do?Token='+Token+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doSTA()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	      alert(Info.CData[0].id);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppStatReal.do?Token='+Token+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doROE()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	      //alert(Info.CData[0].id);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppRoleReal.do?Token='+Token+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doDAT()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	      alert(Info.CData[0].id);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppDataReal.do?Token='+Token+'&Level=5001'+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doALA()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	      alert(Info.CData[0].id);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppAlaReal.do?Token='+Token+'&Level=5001'+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doPRO1()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      //alert(Info.rst);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppProReal.do?Token='+Token+'&Level=5001&OilCode=9999&RunSta=9&AlaSta=9'+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doPRO2()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppProYLedger.do?Token='+Token+'&Level=500101&OilCode=3001&Year=2013'+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doPRO3()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppProMLedger.do?Token='+Token+'&Level=500101&OilCode=3001&Year=2013&Month=12'+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doPRO4()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppProWLedger.do?Token='+Token+'&Level=500101&OilCode=3001&Year=2013&Month=12&Week=2'+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doPRO5()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppProDLedger.do?Token='+Token+'&Level=500101&OilCode=3001&Date=2013-12-03'+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}

function doPRO6()
{
	if(window.XMLHttpRequest)
	{
	  reqInfo = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
	  reqInfo = new ActiveXObject('Microsoft.XMLHTTP');
	}
	reqInfo.onreadystatechange = function()
	{
	  var state = reqInfo.readyState;
	  if(state == 4)
	  {
	    if(reqInfo.status == 200)
	    {
	      var Resp = reqInfo.responseText;
	      var Info = eval("(" + Resp + ")");
	      alert(Info.rst);
	    }
	  }
	};
	var url = 'http://lng.591ip.net/lng-app/AppProGraph.do?Token='+Token+'&Level=500101&OilCode=3001&Mode=0&Year=2013'+'&currtime='+new Date();
	//var url = 'http://lng.591ip.net/lng-app/AppProGraph.do?Token='+Token+'&Level=500101&OilCode=3001&Mode=1&Year=2013&Month=11'+'&currtime='+new Date();
	//var url = 'http://lng.591ip.net/lng-app/AppProGraph.do?Token='+Token+'&Level=500101&OilCode=3001&Mode=2&Year=2013&Month=12&Week=2'+'&currtime='+new Date();
	//var url = 'http://lng.591ip.net/lng-app/AppProGraph.do?Token='+Token+'&Level=500101&OilCode=3001&Mode=3&Date=2013-12-09'+'&currtime='+new Date();
	reqInfo.open('post',url,false);
	reqInfo.send(null);
}
</script>
</head>
<body>
	<input type="button" value="登入接口(已完善)" onclick="doLOG()">
	<input type="button" value="登出接口(已完善)" onclick="doOUT()">
	<input type="button" value="GIS 展示(已完善)" onclick="doGIS()">
	<input type="button" value="站点布局(已完善)" onclick="doSTA()">
	<br><br>
	<input type="button" value="权限查询(已完善)" onclick="doROE()">
	<input type="button" value="生产数据(已完善)" onclick="doDAT()">
	<input type="button" value="告警数据(已完善)" onclick="doALA()">
	<br><br>
	<input type="button" value="销售统计-实时库存(已完善)" onclick="doPRO1()">
	<input type="button" value="销售统计-年报表(已完善)"   onclick="doPRO2()">
	<input type="button" value="销售统计-月报表(已完善)"   onclick="doPRO3()">
	<input type="button" value="销售统计-周报表(已完善)"   onclick="doPRO4()">
	<br><br>
	<input type="button" value="销售统计-日报表(已完善)"   onclick="doPRO5()">
	<input type="button" value="销售统计-图表分析(已完善)" onclick="doPRO6()">
</body>
</html>