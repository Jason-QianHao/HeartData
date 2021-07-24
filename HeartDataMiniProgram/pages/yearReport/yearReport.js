// pages/report-detail/report-detail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    year: '',
    yearReports: {}
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // console.log(options.year);
    var y = options.year;
    var that = this;
    // 通过http请求，加载每年前两个月的报告
    
    // 解析JSON对象
    var res = '{ "data" : ['+
                '{ '+
                  '"year": "2020",'+
                  '"month": ['+
                    '{ "m": "1",' +
                      '"imgurl": "/static/imgs/reports/riqi1.png",'+
                      '"title": "1月报告",'+
                      '"description": "1月报告，良好",'+
                      '"analysis": {'+
                        '"HealthIndex": "",'+
                        '"Other": ""'+
                      '},'+
                      '"dayLists":['+
                        '{ "d" : "31",'+
                          '"isUsed": "",'+
                          '"HealthIndex" : ""'+
                        '}'+
                      ']'+
                    '},'+
                    '{ "m": "2",' +
                      '"imgurl": "/static/imgs/reports/riqi2.png",'+
                      '"title": "2月报告",'+
                      '"description": "2月报告，良好",'+
                      '"analysis": {'+
                        '"HealthIndex": "",'+
                        '"Other": ""'+
                      '},'+
                      '"dayLists":['+
                        '{ "d" : "28",'+
                          '"isUsed": "",'+
                          '"HealthIndex" : ""'+
                        '}'+
                      ']'+
                    '}'+
                  ']'+
                '}'+
              ']}';
    var obj = JSON.parse(res);
    // console.log(obj);
    that.setData({
      yearReports: obj,
      year: y
    });
  },

  /**
   * 月报告查看详情
   */
  monthReport: function(e){
    // console.log(e);
    wx.navigateTo({
      url: '/pages/monthReport/monthReport?year=' + e.currentTarget.dataset.year + '&month=' + e.currentTarget.dataset.month
    })
  }
})