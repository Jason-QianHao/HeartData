// pages/login/login.js

// 获取应用实例
const app = getApp()

Page({
  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
  },
  loadUserInfo: function (e) {
    wx.getUserProfile({
      desc: '获取用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        // console.log(res)
        this.setData({
          userInfo: res.userInfo
        }),
        app.globalData.userInfo = res.userInfo;
        wx.switchTab({
          url: '/pages/index/index?',
        })
      }
    })
  }
})