// index.js
/*
  用户的注册、登陆
*/
// 获取应用实例
const app = getApp()
// 引入图表插件
var wxCharts = require('../../utils/wxcharts.js');
var lineChart = null;
var flag = false;
var cnt = 2;

Page({
  data: {
    userInfo: {},
    x_data: [],
    heartdata: [],
    blueTooth: {},
    showBluetoothDevices: false,
    bleLists: null
  },

  // 页面初始化加载
  onLoad: function () {
    var that = this;
    var arr1 = new Array(250);
    var arr2 = new Array(250);
    for (var i = 0; i < 250; i++) {
      arr1[i] = i + 1;
    }
    for (var i = 0; i < 250; i++) {
      arr2[i] = 1;
    }
    that.setData({
      userInfo: app.globalData.userInfo,
      x_data: arr1,
      heartdata: arr2
    });
    // this.OnWxChart([], [], '心电信号');
    // console.log(this.data.userInfo);
  },

  //测试文件传输服务
  testTransport: function () {
    // var data = new Array(); 
    // data = this.heartdata;
    // for(var i = 0; i < 250 - 1; i++){
    //   data[i] = data[i + 1];
    // }
    // data[250 - 1] = cnt;
    // this.setData({
    //   heartdata: data
    // });
    // 向服务器发送数据
    wx.request({
      url: app.globalData.domain + '/recieveData',
      header: {
        'Connection': 'keep-alive'
      },
      data: {
        onedata: cnt,
        pepoleid: app.globalData.pepoleid,
        filepath: app.globalData.filepath
      },
      success(res) {
        if (res.data == '500') {
          // 关闭蓝牙连接
          // ....
          // 提示错误
          wx.showToast({
            title: '数据传输服务器失败',
            icon: 'fail',
            duration: 2000
          });
        } else {
          // 存储当前服务器文件路径
          app.globalData.filepath = res.data;
        }
      }
    });
    // test
    if (cnt > 5) {
      flag = true;
    } else if (cnt < 1) {
      flag = false
    }
    if (flag) {
      cnt--;
    } else {
      cnt++;
    }
  },

  // 运行
  action: function () {
    // 监听蓝牙和数据传输
    this.ble();
    // // 绘图
    // this.OnWxChart(this.data.x_data,this.data.heartdata,'心电信号');
  },

  ble: function () {
    // 显示蓝牙列表
    this.setData({
      showBluetoothDevices: true
      // bleLists: [{"deviceId":1, "name":2}]
    });
    // 搜索蓝牙
      // 初始化蓝牙模块
    wx.openBluetoothAdapter({
      success: function (res) {
        // 开始搜寻附近的蓝牙外围设备
        wx.startBluetoothDevicesDiscovery({
          services: [] // 要搜索的蓝牙设备主服务的 UUID 列表（支持 16/32/128 位 UUID）
        })
      }
    });
    // 获取在蓝牙模块生效期间所有搜索到的蓝牙设备。
    // 这里不用that会报错 why???
    var that = this;
    wx.getBluetoothDevices({
      success: function (res) {
        // success
        //{devices: Array[11], errMsg: "getBluetoothDevices:ok"}
        // console.log("getBluetoothDevices");
        // console.log(res);
        that.setData({
          bleLists: res.devices
        });
        // console.log(that.data.bleLists);
      }
    })
  },

  // 连接蓝牙
  connectBle: function(e){
    var deviceId =  e.currentTarget.dataset.deviceId;
    var name = e.currentTarget.dataset.name;
  },

  // 取消连接蓝牙
  cancelConnectBle: function () {
    this.setData({
      showBluetoothDevices: false
    });
  },

  //折线图绘制方法
  OnWxChart: function (x_data, y_data, name) {
    var windowWidth = '',
      windowHeight = ''; //定义宽高
    try {
      var res = wx.getSystemInfoSync(); //试图获取屏幕宽高数据
      windowWidth = res.windowWidth / 750 * 690; //以设计图750为主进行比例算换
      windowHeight = res.windowWidth / 750 * 550 //以设计图750为主进行比例算换
    } catch (e) {
      console.error('getSystemInfoSync failed!'); //如果获取失败
    }
    lineChart = new wxCharts({
      canvasId: 'heart', //输入wxml中canvas的id
      type: 'line',
      categories: x_data, //模拟的x轴横坐标参数
      animation: false, //是否开启动画

      series: [{
        name: name,
        data: y_data,
        format: function (val, name) {
          return val;
        }
      }],
      xAxis: { //是否隐藏x轴分割线
        disableGrid: true,
      },
      yAxis: { //y轴数据
        title: '电压(V)', //标题
        format: function (val) { //返回数值
          return val.toFixed(2);
        },
        min: 0, //最小值
        max: 3.3, // 最大值
        gridColor: '#D8D8D8',
      },
      width: windowWidth * 1.1, //图表展示内容宽度
      height: windowHeight, //图表展示内容高度
      dataLabel: false, //是否在图表上直接显示数据
      dataPointShape: false, //是否在图标上显示数据点标志
      extra: {
        lineStyle: 'Broken' //曲线
      },
    });
  }
})