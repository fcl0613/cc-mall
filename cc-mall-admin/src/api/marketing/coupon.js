import request from '@/utils/request'

export default {
    getCouponList(obj) {
        return request({
            url: '/coupon/list',
            method: 'post',
            data: obj
        })
    },
    addCoupon(obj) {
        return request({
            url: '/coupon/create',
            method: 'post',
            data: obj
        })
    },
    updateCoupon(obj) {
        return request({
            url: '/coupon/update',
            method: 'post',
            data: obj
        })
    },
    deleteCoupon(obj) {
        return request({
            url: '/coupon/remove',
            method: 'post',
            data: obj
        })
    },
    getCouponInfo(id) {
        return request({
            url: `/coupon/info/${id}`,
            method: 'get'
        })
    },
    getCouponDetail(id) {
        return request({
            url: `/coupon/detail/${id}`,
            method: 'get'
        })
    },
    getCouponHistoryList(obj) {
        return request({
            url: '/coupon/history/list',
            method: 'post',
            data: obj
        })
    },
}