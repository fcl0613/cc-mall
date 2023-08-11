import request from '@/utils/request'

export default {
    getBaseInfo() {
        return request({
            url: 'home/content',
            method: 'get'
        })
    },
    getOrderTable() {
        return request({
            url: '/home/orderTable',
            method: 'get'
        })
    }
}