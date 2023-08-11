import request from '@/utils/request'

export default {
    payNotify(id) {
        return request({
            url: '/alipay/notify',
            method: 'post',
            params: {
                orderId: id,
                price: 20
            }
        })
    }
}