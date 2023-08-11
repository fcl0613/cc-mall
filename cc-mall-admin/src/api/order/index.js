import request from '@/utils/request'

export default {
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
    getOrderDetail(id) {
        return request({
            url: `/order/detail/${id}`,
            method: 'get'
        })
    },
    orderShipments(obj) {
        return request({
            url: '/order/shipments',
            method: 'post',
            data: obj
        })
    },
    deleteOrder(id) {
        return request({
            url: `/order/delete/${id}`,
            method: 'post'
        })
    }
}