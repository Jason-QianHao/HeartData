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
    // 用户信息
    userInfo: {},
    // 画图
    x_data: [],
    heartdata: [],
    // 蓝牙
    showBluetoothDevices: false,
    bleLists: null,
    deviceId: '',
    name: '',
    serviceId: '',
    services: [],
    cd04: '',
    characteristics04: null,
    result: null,
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
    /**
     * 初始化蓝牙模块
     * 其他蓝牙相关 API 必须在 wx.openBluetoothAdapter 调用之后使用。否则 API 会返回错误（errCode=10000）
     * 如果设备已经打开蓝牙会报错：openBluetoothAdapter:fail already opened，但是不影响逻辑进success
     */
    wx.openBluetoothAdapter({
      success: function (res) {
        /**
         * 开始搜寻附近的蓝牙外围设备
         */ 
        wx.startBluetoothDevicesDiscovery({
          services: [] // 要搜索的蓝牙设备主服务的 UUID 列表（支持 16/32/128 位 UUID）
        })
      },
      fail: function (res) {
        console.log(res);
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
        console.log(res);
        that.setData({
          bleLists: res.devices
        });
        // console.log(that.data.bleLists);
      }
    })
  },

  // 连接蓝牙
  connectBle: function (e) {
    // console.log(e);
    var that = this;
    that.setData({
      deviceId: e.currentTarget.dataset.id,
    });
    console.log(this.data.deviceId);
    /**
     * 监听蓝牙低功耗连接状态的改变事件。包括开发者主动连接或断开连接，设备丢失，连接异常断开等等
     */
    wx.onBLEConnectionStateChange(function (res) {
      console.log(`device ${res.deviceId} state has changed, connected: ${res.connected}`)
    });
    /** 
     * 连接蓝牙低功耗设备。若小程序在之前已有搜索过某个蓝牙设备，并成功建立连接，可直接传入之前搜索获取的 deviceId 直接尝试连接该设备，无需再次进行搜索操作。
     */
    wx.createBLEConnection({
      deviceId: that.data.deviceId,
      success: function () {
        /**
         * 连接成功后开始获取设备的服务列表(service)。
         */
        wx.getBLEDeviceServices({
          deviceId: that.data.deviceId,
          success: function (res) {
            // 回调结果中读取服务列表
            that.setData({
              services: res.services
            });
            console.log('device services:', that.data.services);
            // 选定一个服务UUID
            that.setData({
              serviceId: that.data.services[1].uuid
            });
            console.log('marster services:', that.data.services[1].uuid);
            /**
             * 设定一个定时器。在定时到期以后执行注册的回调函数
             * 延迟3秒，根据服务获取特征 
             */
            setTimeout(
              function () {
                /**
                 * 获取蓝牙低功耗设备某个服务中所有特征 (characteristic)。
                 */
                wx.getBLEDeviceCharacteristics({
                  deviceId: that.data.deviceId,
                  serviceId: that.data.serviceId,
                  success: function (res) {
                    /** 
                     * 获取设备特征列表
                     * 属性	       类型	   说明
                     * uuid	      string	蓝牙设备特征的 UUID
                     * properties	Object	该特征支持的操作类型
                     */
                    console.log('device getBLEDeviceCharacteristics:', res.characteristics);
                    for (var i = 0; i < res.characteristics.length; i++) {
                      if (res.characteristics[i].uuid.indexOf("cd04") != -1) {
                        that.setData({
                          cd04: res.characteristics[i].uuid,
                          characteristics04: res.characteristics[i]
                        });
                      }
                    }
                    /**
                     * 启用蓝牙低功耗设备特征值变化时的 notify 功能，订阅特征。注意：必须设备的特征支持 notify 或者 indicate 才可以成功调用。
                     * 顺序开发设备特征notifiy
                     */
                    wx.notifyBLECharacteristicValueChange({
                      // 启用 notify 功能
                      // 这里的 deviceId 需要在上面的 getBluetoothDevices 或 onBluetoothDeviceFound 接口中获取
                      deviceId: that.data.deviceId,
                      serviceId: that.data.serviceId,
                      characteristicId: that.data.cd04,
                      state: true,
                      success: function (res) {
                        console.log('notifyBLECharacteristicValueChanged success', res)
                      }
                    });
                    /**
                     * 监听蓝牙低功耗设备的特征值变化事件。必须先调用 wx.notifyBLECharacteristicValueChange 接口才能接收到设备推送的 notification。
                     * 回调获取 设备发过来的数据
                     */
                    wx.onBLECharacteristicValueChange(function (characteristic) {
                      console.log('characteristic value comed:', characteristic)
                      /**
                       * 监听cd04中的结果
                       */
                      if (characteristic.characteristicId.indexOf("cd04") != -1) {
                        const result = characteristic.value;
                        const hex = that.buf2hex(result);
                        console.log(hex);
                        that.setData({
                          result: hex
                        });
                      }
                    });
                  }
                });
              },
              1500);
          }
        })
      }
    });
    // 隐藏蓝牙列表
    this.setData({
      showBluetoothDevices: false
    });
  },

  /**
   * 发送 数据到设备中
   */
  // bindViewTap: function () {
  //   var that = this;
  //   var hex = 'AA5504B10000B5'
  //   var typedArray = new Uint8Array(hex.match(/[\da-f]{2}/gi).map(function (h) {
  //     return parseInt(h, 16)
  //   }))
  //   console.log(typedArray)
  //   var buffer1 = typedArray.buffer
  //   console.log(buffer1)
  //   /**
  //    * 向蓝牙低功耗设备特征值中写入二进制数据。
  //    */
  //   wx.writeBLECharacteristicValue({
  //     deviceId: that.data.deviceId,
  //     serviceId: that.data.serviceId,
  //     characteristicId: that.data.cd04,
  //     value: buffer1,
  //     success: function (res) {
  //       console.log("success  指令发送成功");
  //     }
  //   });
  // },

  /**
   * 数据转hex
   */ 
  buf2hex: function (buffer) { // buffer is an ArrayBuffer
    return Array.prototype.map.call(new Uint8Array(buffer), x => ('00' + x.toString(16)).slice(-2)).join('');
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