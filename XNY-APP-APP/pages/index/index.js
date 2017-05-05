var app = getApp()
var md5 = require("../../utils/md5.js");
//md5.hex_md5("");
function writeStatus(status, icon){ 
  // 弹出提示框
    wx.showToast({
      title: status,
      icon: icon
    })
}
Page({
  data: {
    userInfo: {}
  },
  // 初始化
  onLoad: function () {
    var that = this
    //调用应用实例的方法获取全局数据
    app.getUserInfo(function(userInfo){
      //更新数据
      that.setData({
        userInfo:userInfo
      })
    })
  },
  // 登录处理函数
  Login: function() {
    var that = this;
    var status = "";
    var icon = "";
    wx.request({
      url: 'https://beb.com.cn/xny-app/AppLogin.do',
      data: {
        UId: "cui",
        Msg: md5.hex_md5("cui111111"),
        newDate: new Date()
      },
      header:{'Content-Type': 'application/json'},
      method: 'GET',
      success: function(res){
        console.log(res.data)
        if(res.data.rst == "0000") {
          status = "成功";
          icon = "success";
          wx.navigateTo({
            url: '../user/GIS/GIS?token='+res.data.token+'&UId='+res.data.UId
          })
        }
      }, 
      fail: function(res) {
        console.log("fail")
        console.log(res.data)
        status = "失败";
        icon = "loading";
      },
      complete: function() {
        writeStatus(status, icon);
      }
    })
  }
})