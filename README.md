# HeartData
心电小程序客户端数据的服务器存储和分析

注意工程中，配置文件密码相关敏感信息已屏蔽，clone项目时需自行配置。

# 小程序系统架构

​	小程序主要分为登陆页面、主页、心电报告页面。其中主页和心电报告页面整合为带tabbar的页面。

<img src="/./img/image-20210723183849092.png" alt="image-20210723183849092" style="zoom:30%;" />

## 登陆页面

​	用来登陆小程序。

### 页面展示

<img src="/./img/image-20210723183205703.png" alt="image-20210723183205703" height="2" style="zoom:40%" />

### 介绍

​	登陆页面由背景图片和登录按钮组成。

- 背景图片，设置铺满全屏。

- 登陆可以设置为"用户名 + 密码"形式登陆，也可以设置为微信登陆。

  目前采用微信方式登陆，点击按钮后，弹出弹窗，授权后可以获取到微信相关的信息：微信昵称、性别、地区。

  <img src="/./img/image-20210723183725257.png" alt="image-20210723183725257" style="zoom:50%;" />

### 代码框架

- js

  <img src="/./img/image-20210723184021316.png" alt="image-20210723184021316" style="zoom:30%;" />

  js部分后续还要加上将登陆信息发送到服务器部分。

  如果是微信登陆，需要在第一次登陆时发送信息。

  如果时"用户名+密码"方式登陆，则在注册时发送信息到服务器。

- wxml

  <img src="/./img/image-20210723184046722.png" alt="image-20210723184046722" style="zoom:40%;" />

## 主页

​	展示用户信息、通过蓝牙与硬件连接、实时显示波形。

### 页面展示

<img src="/./img/image-20210723184408143.png" alt="image-20210723184408143" style="zoom:40%;" />

### 介绍

​	主页由个人信息、波形区域、操作按钮和底部tabbar组成。

- 个人信息。由登陆页面传过来的参数渲染而成。这里通过微信登陆传过来的昵称、性别、城市，进行展示。
- 波形区域，用来显示心电波形。
- 操作按钮。登陆小程序进入主页后，点击按钮会弹出对话框选择蓝牙进行连接，连接完成后，开始接受数据进行波形展示。
- 底部tabbar。有两个选项卡，主页和心电报告。

### 代码框架

- js

  <img src="/./img/image-20210723203742528.png" alt="image-20210723203742528" style="zoom:40%;" />

  userInfo为用户信息；heartdata初始化为1，后面实时显示时，进行数组移动即可，更新最后一个数。

  目前缺少蓝牙部分的连接逻辑。

- wxml

  <img src="/./img/image-20210723203924092.png" alt="image-20210723203924092" style="zoom:40%;" />

## 心电报告

​	用来查询用户的心电和健康的年度报告、月报告、日报告等信息。

### 报告主页

​	用来简述用户各年的情况。

#### 页面展示

<img src="/./img/image-20210728123454880.png" alt="image-20210728123454880" style="zoom:50%;" />

#### 介绍

- 报告主页由多个简略版年度报告组成
- 每个年度报告简略版包含最多两个月的月报告，一个超链接可以查看所有年度报告。
- 每个简略版月报告包含名称、简介月健康状况。

#### 报告格式

​	报告由服务端生成，并以JSON字符串格式发送到小程序。

```json
{ "data" : [
    {
      "year": "2020",
      "month": [
                 { "m": "1",
                   "imgurl": "/static/imgs/reports/riqi1.png",
                   "title": "1月报告",
                   "description": "1月报告，良好",
                   "analysis": {
                     "HealthIndex": "",
                     "Other": ""
                    },
                   "dayLists":[
                                { "d" : "31",
                                  "isUsed": "",
                                  "HealthIndex" : ""
                                }
                              ]
                  }
               ]
     }             
   ]
}
```

### 详细年度报告

​	详细展示该年所有使用设备的每个月的大概情况。

#### 页面展示

<img src="/./img/image-20210728124527330.png" alt="image-20210728124527330" style="zoom:50%;" />

#### 介绍

	- 包含概念使用设备的，每个月的简略版月报告。

### 详细月报告

​	展示每个月的健康状况，和显示当月每天的大致情况。

#### 页面展示

<img src="/./img/image-20210728124857069.png" alt="image-20210728124857069" style="zoom:50%;" />

#### 介绍

- 包含当月的月健康指数和其他信息。
- 包含当月每天的是否使用过设备的简略信息。

#### 示例月报告

```json
{ "data" : [
             { 
               "year": "2020",
               "month": [
                    			{ "m": "1",
                      			"imgurl": "/static/imgs/reports/riqi1.png",
                      			"title": "1月报告",
                      			"description": "1月报告，良好",
                      			"analysis": {
                        									"HealthIndex": "8.0",
                        									"Other": ""
                      			},
                      			"dayLists":[
                        								{ "d" : "1",'+
                          								"imgurl": "/static/imgs/reports/riqi1.png",'+
                          								"isUsed": "true",
                          								"HealthIndex" : "9.9"
                        								},
                        								{ "d" : "2",
                          								"imgurl": "/static/imgs/reports/riqi2.png",
                          								"isUsed": "false",
                          								"HealthIndex" : ""
                        								},
                        								{ "d" : "3",
                          								"imgurl": "/static/imgs/reports/riqi3.png",
                          								"isUsed": "true",
                          								"HealthIndex" : "1.5"
                        								}
                      			]
                   				}
                 ]
               }
             	]
};
```

### 详细日报告

​	包含每日的大致健康状况，和每次使用设备的记录。

#### 页面展示

<img src="/./img/image-20210728125739532.png" alt="image-20210728125739532" style="zoom:50%;" />

#### 介绍

- 包含当日健康状况描述和每次使用记录
- 每个使用记录包含名称、使用时间、健康状况。

#### 示例日报告

```json
{ "data" : [
            { 
              "year": "2020",
               "month": [
                        	{ "m": "1",
                      			"imgurl": "/static/imgs/reports/riqi1.png",
                      			"title": "1月报告",
                     				"description": "1月报告，良好",
                      			"analysis": {
                        									"HealthIndex": "8.0",
                        									"Other": ""
                      			},
                      			"dayLists":[
                        								{ "d" : "1",
                          								"imgurl": "/static/imgs/reports/riqi1.png",
                          								"isUsed": "true",
                          								"HealthIndex" : "9.9",
                          								"fileLists":[
                            														{
  																												"id": "1",
                              														"createdTime": "2020-01-01 15:00:00",
                              														"avgBeat": "75"
                            														},
                            														{
                                                          "id": "2",
                              														"createdTime": "2020-01-01 16:00:00",
                              														"avgBeat": "80"
                            														},
                            														{
                                                          "id": "3",
                              														"createdTime": "2020-01-01 17:00:00",
                              														"avgBeat": "60"
                            														}
                          														]
                        									}
                      			]
                    			}
                 ]
             }
          ]
};
```

# 开发笔记

1. 数据读取或者其他参数读取时，判断是否为null或者为""时，注意，null在前，""在后。

   ```java
   // 注意这里null和""的判读不能写反了。
   		if(openId == null || openId.equals("")) {
   			return Constants.SUCCESSCODE;
   		}
   ```

   
