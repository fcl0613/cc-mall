import request from '@/utils/request'

export default {
    getCouponList() {
        return request({
            url: '/coupon/all',
            method: 'get'
        })
    },
    receiveCoupon(id) {
        return request({
            url: `/coupon/receive/${id}`,
            method: 'post'
        })
    }
}