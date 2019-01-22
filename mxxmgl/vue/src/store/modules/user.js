import {getInfo, login, logout} from '@/api/login'
import {getToken, removeToken, setToken} from '@/utils/auth'
import {default as api} from '../../utils/api'
import store from '../../store'
import router from '../../router'
import md5 from 'md5'

const user = {
  state: {
    username:'',
    // nickname: "",
    userId: "",
    // avatar: 'https://www.gravatar.com/avatar/6560ed55e62396e40b34aac1e5041028',
    avatar:'http://pozitiv-news.ru/wp-content/uploads/2010/12/0715.jpg',
    // avatar:'',
    role: '',
    menus: [],
    permissions: [],
    token:''
  },
  mutations: {
    SET_USER: (state, userInfo) => {
      state.username = userInfo.username;
      // state.nickname = userInfo.nickname;
      state.userId = userInfo.userId;
      state.role = userInfo.roleName;
      state.menus = userInfo.menuList;
      state.permissions = userInfo.permissionList;
    },
    SET_TOKEN: (state,token)=>{
      state.token = token;
    },
    SET_AVATAR:(state,avatar)=>{
      state.avatar = avatar;
    },
    RESET_USER: (state) => {
      // state.nickname = "";
      state.username = "";
      state.userId = "";
      state.role = '';
      state.menus = [];
      state.permissions = [];
      state.token = '';
    }
  },
  actions: {
    // 登录
    Login({commit, state}, loginForm) {
      loginForm.password = md5(loginForm.password);
      return new Promise((resolve, reject) => {
        api({
          url: "/user/login",
          method: "post",
          data: loginForm
        }).then(data => {
          if (data.status === 0) {
            //cookie中保存前端登录状态
            // setToken();

            //登陆成功之后，将token保存到store里面
            commit('SET_TOKEN',data.data);
            // commit('SET_AVATAR')
          }
          resolve(data);
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 获取用户信息
    GetInfo({commit, state}) {
      return new Promise((resolve, reject) => {
        api({
          url: '/user/getInfoByUsername',
          method: 'get',
        }).then(data => {
          //储存用户信息
          commit('SET_USER', data.data);

          //cookie保存登录状态,仅靠vuex保存的话,页面刷新就会丢失登录状态
          // setToken();
          commit('SET_TOKEN',store.getters.token);

          //生成路由
          let userPermission = data.data;
          store.dispatch('GenerateRoutes', userPermission).then(() => {
            //生成该用户的新路由json操作完毕之后,调用vue-router的动态新增路由方法,将新路由添加
            router.addRoutes(store.getters.addRouters)
          })
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    LogOut({commit}) {
      return new Promise((resolve) => {
        api({
          url: "/user/logout",
          method: "post"
        }).then(data => {
          commit('RESET_USER')
          // removeToken()
          resolve(data);
        }).catch(() => {
          commit('RESET_USER')
          // removeToken()
        })
      })
    },
    // 前端 登出
    FedLogOut({commit}) {
      return new Promise(resolve => {
        commit('RESET_USER')
        // removeToken()
        resolve()
      })
    }
  }
}
export default user
