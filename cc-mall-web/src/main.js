import Vue from 'vue'
import App from './App.vue'
import router from '@/router'
import '@/style/reset.css'
import '@/mobile/flexible'
import store from './store'
import './plugins/vant'
import '@/assets/style/global.css'
// 引入全部样式
import 'vant/lib/index.less'


Vue.config.productionTip = false

new Vue({
    router,
    store,
    render: h => h(App),
}).$mount('#app')
