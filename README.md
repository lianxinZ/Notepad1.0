# Notepad1.0
## 一.实现功能：

### 基础功能：
1.备忘录基本功能，备忘录的添加和删除。

2.备忘录的时间戳功能

3.备忘录的查询功能，实现选择字段查询。

### 拓展功能
3.备忘录重新编辑功能。

4.备忘录更换主题功能。

5.界面美化。

## 二 主要目录说明： 注：因为太晚开始编写这个项目，没有想好就开始写了，所以写的有点混乱。敬请谅解。
### FeedReaderContract
定义架构和契约： 定义表的创建和删除语句。定义表的字段。
### DBHelper
数据库表的创建和删除。
### MainActivity 
主界面：使用listview 显示 备忘录内容 和其他界面的入口
### InsertActivity
插入界面：实现添加备忘录
### SearchActivity
搜索界面：实现搜索功能
### updateActivity
编辑界面：实现备忘录的重新编辑
### preferenceActivity
设置界面：使用preferenceFragment实现偏好设置
### Setting
设置类，加载preferences.xml配置文件
### initData
数据类，用来存放偏好设置的数据




