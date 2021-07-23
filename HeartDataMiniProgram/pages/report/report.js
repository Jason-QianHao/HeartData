// pages/report/report.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    reports: {}
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    // 解析JSON对象
    /*
    {
      "2020": {
        "1": {
          "imgurl": "",
          "title": "",
          "description": "",
          "analysis": {
            "HealthIndex": "",
            "Other": ""
          },
          "dayLists":{
            "1": {
              "isUsed": "",
              "HealthIndex" : ""
            }
          }
        }
      },
      "2021": ...
    }
    */
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
                '},'+
                '{ '+
                  '"year": "2021",'+
                  '"month": ['+
                    '{ "m": "2",' +
                      '"imgurl": "/static/imgs/reports/riqi2.png",'+
                      '"title": "2月报告",'+
                      '"description": "2月报告，良好",'+
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
                      '}'+
                    ']'+
                '}'+               
              ']}';
    var obj = JSON.parse(res);
    console.log(obj);
    that.setData({
      reports: obj
    });
  }
})