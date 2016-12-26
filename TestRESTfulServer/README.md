# 食用方法
传输均为json字符串数据，到本地后解析为UserData类
#### int head    
-> 头文件，用于检测行为，包含注册和登录的请求，默认值-1；
#### int id    
-> 为服务器内部储存数据所需编号，从客户端传输至服务器不需要填写，申请账号成功后会返回给客户端，默认值-1；
#### String username    
-> 用户昵称，默认为用户的注册邮箱，暂不提供修改接口。
#### String usermail    
-> 用户邮箱，注册和登陆时需提供。
#### String password    
-> 用户密码，注册和登陆时需提供。
#### String remarks    
-> 备注，暂无具体作用。
#### boolean issuccessful    
-> 用于验证操作是否成功，默认为false。
#### ArrayList<KUAIDI> packageList    
-> 为快递列表，为用户储存曾经查询的快递信息。


