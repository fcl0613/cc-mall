import request from '@/utils/request'

export default {
    generateConfirmOrder(obj) {
        return request({
            url: '/order/generateConfirmOrder',
            method: 'post',
            data: obj
        })
    },
    createOrder(obj) {
        return request({
            url: '/order/create',
            method: 'post',
            data: obj
        })
    },
    getOrderList(obj) {
        return request({
            url: '/order/list',
            method: 'post',
            data: obj
        })
    },
    cancelOrder(id) {
        return request({
            url: `/order/cancel/${id}`,
            method: 'post'
        })
    },
    directConfirm(productId, count) {
        return request({
            url: '/order/direct/confirm',
            method: 'post',
            params: {
                productId, count
            }
        })
    },
    directBuy(obj) {
        return request({
            url: '/order/direct/buy',
            method: 'post',
            data: obj
        })
    },
    deleteOrder(id) {
        return request({
            url: `/order/remove/${id}`,
            method: 'post'
        })
    },
    confirmOrder(id) {
        return request({
            url: `/order/confirm/${id}`,
            method: 'post'
        })
    }
}