// index.js
/*
  用户的注册、登陆
*/
// 获取应用实例
const app = getApp()
// 引入图表插件
var wxCharts = require('../../utils/wxcharts.js');
var lineChart = null;

Page({
  data: {
    userInfo: {},
    x_data: [],
    heartdata: [],
    blueTooth: {},
  },
  onLoad: function() {
    var that = this;
    var arr1 = new Array(250);
    var arr2 = new Array(250);
    for(var i = 0;i < 250; i++){
      arr1[i] = i + 1;
    }
    for(var i = 0;i < 250; i++){
      arr2[i] = 1;
    }
    that.setData({
      userInfo: app.globalData.userInfo,
      x_data: arr1,
      heartdata: arr2
    });
    this.OnWxChart([],[],'心电信号');
    // console.log(this.data.userInfo);
  },
  // 监听蓝牙和数据

  // 绘图
  action: function () {
    this.OnWxChart(this.data.x_data,this.data.heartdata,'心电信号');
  },

  //折线图绘制方法
  OnWxChart:function(x_data,y_data,name){
    var windowWidth = '', windowHeight='';    //定义宽高
    try {
      var res = wx.getSystemInfoSync();    //试图获取屏幕宽高数据
      windowWidth = res.windowWidth / 750 * 690;   //以设计图750为主进行比例算换
      windowHeight = res.windowWidth / 750 * 550    //以设计图750为主进行比例算换
    } catch (e) {
      console.error('getSystemInfoSync failed!');   //如果获取失败
    }
    lineChart = new wxCharts({
      canvasId: 'heart',     //输入wxml中canvas的id
      type: 'line',     
      categories:x_data,    //模拟的x轴横坐标参数
      animation: false,  //是否开启动画
     
      series: [{
        name: name,
        data: y_data,
        format: function (val, name) {
          return val;
        }
      }
      ],
      xAxis: {   //是否隐藏x轴分割线
        disableGrid: true,
      },
      yAxis: {      //y轴数据
        title: '电压(V)',  //标题
        format: function (val) {  //返回数值
          return val.toFixed(2);
        },
        min: 0,   //最小值
        max: 3.3, // 最大值
        gridColor: '#D8D8D8',
      },
      width: windowWidth*1.1,  //图表展示内容宽度
      height: windowHeight,  //图表展示内容高度
      dataLabel: false,  //是否在图表上直接显示数据
      dataPointShape: false, //是否在图标上显示数据点标志
      extra: {
        lineStyle: 'Broken'  //曲线
      },
    });
  }
})
