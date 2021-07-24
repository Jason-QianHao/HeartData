// pages/dayReport/dayReport.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    year: '',
    month: '',
    day: '',
    dayReports: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var y = options.year;
    var m = options.month;
    var d = options.day;
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
                        '"HealthIndex": "8.0",'+
                        '"Other": ""'+
                      '},'+
                      '"dayLists":['+
                        '{ "d" : "1",'+
                          '"imgurl": "/static/imgs/reports/riqi1.png",'+
                          '"isUsed": "true",'+
                          '"HealthIndex" : "9.9",'+
                          '"fileLists":['+
                            '{"id": "1",'+
                              '"createdTime": "2020-01-01 15:00:00",'+
                              '"avgBeat": "75"'+
                            '},'+
                            '{"id": "2",'+
                              '"createdTime": "2020-01-01 16:00:00",'+
                              '"avgBeat": "80"'+
                            '},'+
                            '{"id": "3",'+
                              '"createdTime": "2020-01-01 17:00:00",'+
                              '"avgBeat": "60"'+
                            '}'+
                          ']'+
                        '}'+
                      ']'+
                    '}'+
                  ']'+
                '}'+
              ']}';
    var obj = JSON.parse(res);
    // console.log(obj);
    that.setData({
      year: y,
      month: m,
      day: d,
      dayReports: obj
    });
  }
})