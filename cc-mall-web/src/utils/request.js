import axios from 'axios'
import { Toast, Dialog } from 'vant';
import store from '@/store'
import { getToken } from './token'

const BASE_URL = '/api'

const service = axios.create({
  // baseURL 将自动加在 url`前面，除非 url 是一个绝对 URL。
  // 它可以通过设置一个 baseURL 便于为 axios 实例的方法传递相对 URL
  baseURL: BASE_URL,
  // timeout设置一个请求超时时间，如果请求时间超过了timeout，请求将被中断，单位为毫秒（ms）
  timeout: 100000,
  // headers是被发送的自定义请求头，请求头内容需要根据后端要求去设置，这里我们使用本项目请求头。
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  }
})

service.interceptors.request.use(
  config => {
    if (getToken()) {
      config.headers['token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)


service.interceptors.response.use(
  response => {
    const res = response.data

    // if the custom code is not 20000, it is judged as an error.
    if (res.code !== 200) {
      Toast.fail(res.message);
      console.log(res)
      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 401 || res.code === 5000 || res.code === 50012 || res.code === 50014) {
        // to re-login
        Dialog.confirm({
          title: 'Confirm logout',
          message: 'You have been logged out, you can cancel to stay on this page, or log in again',
        })
          .then(() => {
            // on confirm
            store.dispatch('resetToken').then(() => {
              location.reload()
            })
          })
          .catch(() => {
            // on cancel
          })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Toast.fail(error.message);
    return Promise.reject(error)
  }
)

export default service