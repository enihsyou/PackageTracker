#快递收发和跟踪系统

##项目介绍
**辣鸡快递**——能够获取和跟踪快递最新进展情况的Android应用.

##参与人员
- [@enihsyou](https://github.com/enihsyou)
- [@Sleaf](https://github.com/Sleaf)
- [@rainbow-0926](https://github.com/rainbow-0926)
- [@cvb628](https://github.com/cvb628)

##详细信息
应用采用 *MVC* 模型，分工合作完成视图、控制器和模型的构建。
进行版本更迭记录，从`v0.0.1`开始。各自在自己的`dev`分支编写，完成后经讨论合并到`master`分支上。

- 包工头 @enihsyou
  * 设计实现主体架构
  * 定义接口，分配任务
  * API提供数据
  * 实现控制器
- 设计狮 @Sleaf
  * 设计UI
  * 设计功能
  * 设计数据模型
- @rainbow-0926 & @cvb628
  * 解析API数据
  * 解析获取快递公司信息
  * 实现多语言功能，UI文字定义和翻译
  * 绘制图标
- 共同完成
  * 主题设计
  * 介绍页面
  * 应用测试

##TODO
* [x] 快递信息API
* [ ] 在主页上添加 添加快递单按钮
* [ ] 用户登录功能
* [ ] 后台跟踪快递信息
* [ ] 绘制应用图标
* [ ] *GitHub Pages* 介绍页面
* [ ] 扫码功能
* [ ] 呼叫快递员功能
* [ ] 解析内容生成界面(类似Flyme短信解析)
* [ ] 每项项目一张卡片，背景为对应快递公司主题
* [ ] 利用API识别快递公司，自动完成
* [ ] ~~待添加~~

##第三方库
* [OkHttp](https://github.com/square/okhttp) 发送HTTP请求
* [Gson](https://github.com/google/gson) JSON解析
* [jsoup](https://github.com/jhy/jsoup) HTML解析
* [ZXing](https://github.com/zxing/zxing) 扫描二维码
* [fab-speed-dial](https://github.com/yavski/fab-speed-dial) FAB with speed dial
