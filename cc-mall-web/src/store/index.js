import Vue from 'vue'
import Vuex from 'vuex'
import {setToken, getToken, removeToken} from '@/utils/token'

Vue.use(Vuex);
// 用Vuex.Store对象用来记录token
const store = new Vuex.Store({

  state: {
    // 存储token
    token:'',
    userName:'', // 可选
    avatar: ''
  },

  getters: {
	getToken(state){
		return state.token || getToken() || ''
	}
  },

  mutations: {
    // 修改token，并将token存入localStorage
    setToken(state,token) {
      state.token = token
      setToken(token)
      console.log('store、localstorage保存token成功！')
    },
    delToken(state) {
      state.token = ""
      removeToken()
    },
    // 可选
    setUserInfo(state, userName, avatar) {
      state.userName = userName
      state.avatar = avatar
    }
  },

 actions: {
   removeToken: (context) => {
     context.commit('setToken')
   },
   resetToken: (context) => {
    context.commit('delToken')
   }
 },
})
  
export default store