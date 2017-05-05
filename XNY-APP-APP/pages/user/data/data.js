// pages/user/data/data.js
var app = getApp()
var token = "";
var Cpm_Id = "";

Page({
  data: {
    dataUrl: "",
    dataReal: {},
    dataPast: {},
    dataAlert: {},
    dataAccSale: {},
    dataAcc: {},
    BDate: '2017-05-05',
    EDate: '2017-05-05'
  },
  onLoad: function (str) {  //初始化
    Cpm_Id = str.Id;
    token = str.token;
    var that = this;
    var url = 'https://beb.com.cn/xny-app/DataReal.do';
    var pData = {
      Cpm_Id: Cpm_Id,
      Token: token,
      newDate: new Date()
    }
    request("dataReal", this, url, pData)
  },
  gotoDataReal: function(){ //实时数据
    var url = 'https://beb.com.cn/xny-app/DataReal.do';
    var pData = {
      Cpm_Id:Cpm_Id,
      Token:token,
      newDate: new Date()
    }
    request("dataReal", this, url, pData)
  },
  gotoDataPast: function(){ //历史数据
    var that = this;
    var url = 'https://beb.com.cn/xny-app/DataPast.do';
    var pData = {
      Cpm_Id: Cpm_Id,
      Token: token,
      BDate: that.data.BDate + " 00:00:00",
      EDate: that.data.EDate + " 23:59:59",
      newDate: new Date()
    }
    request("dataPast", this, url, pData)
  },
  gotoDataAlert: function(){  //告警数据
    var that = this;
    var url = 'https://beb.com.cn/xny-app/Alert_Info.do';
    var pData = {
      Cpm_Id: Cpm_Id,
      Token: token,
      BDate: that.data.BDate + " 00:00:00",
      EDate: that.data.EDate + " 23:59:59",
      newDate: new Date()
    }
    request("dataAlert", this, url, pData)
  },
  gotoDataAccSale: function(){  //销售统计
    var that = this;
    var url = 'https://beb.com.cn/xny-app/Acc_Sale.do';
    var pData = {
      Cpm_Id: Cpm_Id,
      Token: token,
      BDate: that.data.BDate + " 00:00:00",
      EDate: that.data.EDate + " 23:59:59",
      newDate: new Date()
    }
    request("dataAccSale", this, url, pData)
  },
  gotoDataAcc: function(){  //用量统计
    var that = this;
    var url = 'https://beb.com.cn/xny-app/Acc_Data.do';
    var pData = {
      Cpm_Id: Cpm_Id,
      Token: token,
      BDate: that.data.BDate + " 00:00:00",
      EDate: that.data.EDate + " 23:59:59",
      newDate: new Date()
    }
    request("dataAcc", this, url, pData)
  },
  BDateChange: function(e) { //时间选择器
    this.setData({
      BDate: e.detail.value
    })
  },
  EDateChange: function(e) { //时间选择器
    this.setData({
      EDate: e.detail.value
    })
  },
  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  }
})

/**
 * pDataName:数据名称
 * pthat：that=this
 * pUrl：url
 * pData：数据对象
 */
function request(pDataName, pthat, pUrl, pData) {
  wx.request({
    url: pUrl,
    data: pData,
    method: 'GET', 
    success: function(res){ // 成功
      console.log(res.data)
      var cData = res.data.cData;
      if(res.data.rst == "0000")
      {
        if(pDataName == "dataReal"){
          pthat.setData({
            dataReal: cData,
            dataUrl: pDataName + ".wxml"
          })
        }
        if(pDataName == "dataPast"){
          pthat.setData({
            dataPast: cData,
            dataUrl: pDataName + ".wxml"
          })
        }
        if(pDataName == "dataAlert"){
          pthat.setData({
            dataAlert: cData,
            dataUrl: pDataName + ".wxml"
          })
        }
        if(pDataName == "dataAccSale"){
          pthat.setData({
            dataAccSale: cData,
            dataUrl: pDataName + ".wxml"
          })
        }
        if(pDataName == "dataAcc"){
          pthat.setData({
            dataAcc: cData,
            dataUrl: pDataName + ".wxml"
          })
        }
      }
    }
  })
}