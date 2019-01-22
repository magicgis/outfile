import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
export const constantRouterMap = [
  {path: '/login', component: _import('login/index'), hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '首页',
    hidden: true,
    children: [{
      path: 'dashboard', component: _import('dashboard/index')
    }]
  }
]
export default new Router({
  // mode: 'history', //后端支持可开
  // base:'mxxmgl',
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export const asyncRouterMap = [
  // {
  //   path: '/system',
  //   component: Layout,
  //   redirect: '/system/article',
  //   name: '功能模块',
  //   meta: {title: '功能模块', icon: 'tree'},
  //   children: [
  //     {
  //       path: 'article',
  //       name: '文章',
  //       component: _import('article/article'),
  //       meta: {title: '文章', icon: 'example'},
  //       menu: 'article'
  //     },
  //   ]
  // },
  // {
  //   path:'/project',
  //   component:Layout,
  //   redirect:'/project/',
  //   name: '项目管理',
  //   meta:{title:'项目管理',icon:'example'},
  //   children:[
  //     {
  //       path: '',
  //       name: '项目列表',
  //       component: _import('project/project'),
  //       meta: {title: '项目列表', icon: 'project'},
  //       menu: 'project',
  //       // children:[{
  //       //   path: 'detail',
  //       //   name:'项目详细信息',
  //       //   component:_import('project/project_detail')
  //       // }
  //       // ]
  //     },
  //     {
  //       path: 'plan',
  //       name: '计划安排',
  //       component: _import('project/plan'),
  //       meta: {title: '计划安排', icon: 'plan'},
  //       menu: 'plan'
  //     },
  //     {
  //       path: 'task',
  //       name: '任务执行',
  //       component: _import('project/task'),
  //       meta: {title: '任务执行', icon: 'task'},
  //       menu: 'task'
  //     },
  //     {
  //       path: 'show',
  //       name: '汇总展示',
  //       component: _import('project/show'),
  //       meta: {title: '汇总展示', icon: 'show'},
  //       menu: 'show'
  //     },
  //     {
  //       path: 'file',
  //       name: '文件管理',
  //       component: _import('project/file'),
  //       meta: {title: '文件管理', icon: 'file'},
  //       menu: 'file'
  //     }
  //
  //   ]
  // },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/',
    name: '用户管理',
    meta: {title: '用户权限', icon: 'table'},
    children: [
      {
        path: '',
        name: '用户列表',
        component: _import('user/user'),
        meta: {title: '用户列表', icon: 'user'},
        menu: 'user'
      },
      {
        path: 'role',
        name: '权限管理',
        component: _import('user/role'),
        meta: {title: '权限管理', icon: 'password'},
        menu: 'role'
      },
    ]
  },

  {path: '*', redirect: '/404', hidden: true}
]
