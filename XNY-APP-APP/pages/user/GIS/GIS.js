var app = getApp()
var token = "";
var iconPath = "../../../image/marke.gif";
Page({
  data: {
    center: {lng:117.592666,lat:36.670606},
    id: [],
    brief: [],
    markers: []
  },
  onLoad: function (str) {
    var that = this;
    var uid = str.UId;
    token = str.token;
    wx.request({
      url: 'https://beb.com.cn/xny-app/AppGisReal.do',
      data: {
        UId:uid,
        Token:token
      },
      method: 'GET', 
      success: function(res){
        console.log(res.data)
        if(res.data.rst = "0000") {
          var dataObj = res.data.cData;
          var idArray = new Array();
          var briefArray = new Array();
          var markerArray = new Array();
          for(var i = 0; i < dataObj.length; i++) {
            idArray[i] = dataObj[i].id
            briefArray[i] = dataObj[i].brief
            markerArray[i] = {
              id: idArray[i],
              latitude: dataObj[i].latitude,
              longitude: dataObj[i].longitude,
              iconPath: iconPath
            }
          }
          console.log(idArray)
          console.log(briefArray)
          console.log(markerArray)
          that.setData({
            id: idArray,
            brief: briefArray,
            markers: markerArray
          })
        }
      },
      fail: function(res) {
        console.log("fail")
        console.log(res)
      },
      complete: function() {
      }
    })
  },
  gotoData:function(e){
    var Id = e.target.id;
    wx.navigateTo({
      url: '../data/data?token='+token+'&Id='+Id
    })
  },
  markertap:function(e){
    var Id = e.markerId;
    wx.navigateTo({
      url: '../data/data?token='+token+'&Id='+Id
    })
  }
})