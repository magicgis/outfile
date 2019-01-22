### 梅县项目管理系统(mxxmgl) 
### 功能实现
1. 后台api返回json 采用restful风格设计
1. 用户权限认证
2. 附件上传下载（地址windows系统）
3. excel批量导入
4. md5进行密码加密匹配
5. 分页查询 
### 待实现功能
1. 动态生成路由
2. 在线访问人数
3. 密码加盐并判断访问次数
4. 返回同意状态码

### 统一返回格式

```
{
    status:200
    msg:"登陆成功"
    data:{
       "username":"admin"
       "password":"admin" 
    }
}

```



 