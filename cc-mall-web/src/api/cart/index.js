import request from '@/utils/request'

export default {
    addCart(obj) {
        return request({
            url: '/cart/add',
            method: 'post',
            data: obj
        })
    },
    removeCart(id) {
        return request({
            url: `/cart/remove/${id}`,
            method: 'post'
        })
    },
    plusCart(id) {
        return request({
            url: `/cart/plus/${id}`,
            method: 'post'
        })
    },
    minusCart(id) {
        return request({
            url: `/cart/minus/${id}`,
            method: 'post'
        })
    },
    getCartList(pageNum, pageSize) {
        return request({
            url: `/cart/list`,
            method: 'get',
            params: {
                pageNum, pageSize
            }
        })
    }
}