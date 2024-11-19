# blank-frame-parent

#### 介绍
后端快速开发脚手架，多模块maven构建，方便按功能划分多个模块快速开发。集成用户、角色、权限、权限点（按钮），请求级别权限拦截。

增删改查等常用业务深度封装，代码简洁，关注点可放到具体业务上。

支持基于注解的Excel文件读写，支持切面级的数据字典转换，支持敏感数据隐藏，支持接口级的参数自动加密、解密。自带基于数据替换的混淆加密算法。

全局异常拦截处理，可配置不同异常返回不同数据，可根据项目需要进行调整。

多平台响应格式，可根据需求自己定制接口返回格式。

#### 软件架构
基于Spring Boot、Mybatis、FreeMaker、Layui构建，已集成Swagger2、knife4j、MySQL、Log4j2、HttpClient4.5、Java Mail、Junit。

前端部分集成了多级联动下拉框、多选下拉框、树状表格等等常用组件。

支持下拉、多级下拉、单选框、复选框等组件数据字典自动翻译功能，支持数据字典和数据库表两种翻译模式，支持缓存翻译和数据库查询翻译，多级数据支持异步翻译。

#### 安装教程

1.  clone代码到本地。
2.  执行blank-frame/resources/init-data.sql
3.  启动项目，二次开发。

#### 使用说明

1.  common模块已集成权限管理，在com/teamsoft/framework/sys包下
2.  使用时配合code-generation使用（同在本组织下开源），支持Controller到Mapper层Java代码、SQL xml、前端页面一键生成。

#### 联系我

有更全面，更nice的想法，请告知我：dominealex@163.com