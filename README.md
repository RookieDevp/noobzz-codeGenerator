# noobzz-codeGenerator
⭐欢迎star，您的支持是我更新的动力！

代码生成器
基于若依代码生成思路，抽离出一个独立的代码生成器

用处：

1、生成自定义代码（前后端、SQL等）

2、自定义代码生成模板

3、多数据源（暂支持MySQL）

4、代码通用配置
# 开始

### 准备：

1、导入noobzz-gen.sql

2、修改application-dev.yml中的数据库配置
### 启动后端
```
运行启动类GenApplication.class(端口9301)
```

### 启动前端
```
npm install

npm run dev
```
### 访问地址

http://127.0.0.1:9528

# 代码生成器TODO

- [ ] 通用模板、分类功能模板
- [x] 数据源切换
- [ ] 公共字段
- [x] 模板修改
- [x] 系统配置
- [x] 导入表可勾选模板
- [x] 生成代码到指定路径
- [ ] 实时渲染（示例值）
- [ ] 数据源加数据库字段
- [ ] 模板列表调整
- [ ] 各字段类型动态映射
- [x] 代码预览复制
- [ ] 生成信息规则采用正则表达式
- [x] 自定义导入表（区分默认导表）
- [ ] 移除多表的生成、字典类型
- [x] 显示主键字段、是否可以为null、是否是索引字段
- [ ] 接入Oracle数据库
- [ ] 创建目录（根目录无法删除）和创建模板分离
- [x] 代码生成菜单-添加数据源来源字段
- [ ] 打包doker、改单体架构
- [ ] 规范代码、删除冗余代码

# 示例图片：
![image](https://github.com/RookieDevp/noobzz-codeGenerator/assets/88661272/4a6fe1b8-5e1e-440b-bb6c-812ae36d559b)
![image](https://github.com/RookieDevp/noobzz-codeGenerator/assets/88661272/e4b65a75-43a5-437a-814c-a6196a3a9b06)
![image](https://github.com/RookieDevp/noobzz-codeGenerator/assets/88661272/b1ab9cbe-39ba-491b-83de-caca35feb046)
![image](https://github.com/RookieDevp/noobzz-codeGenerator/assets/88661272/2619ce0c-92c3-4535-8e11-e06690415585)
![image](https://github.com/RookieDevp/noobzz-codeGenerator/assets/88661272/ebe78870-a02e-4e07-8fe6-d7c9213c1469)

