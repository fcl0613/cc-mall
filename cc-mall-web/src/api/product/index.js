import request from '@/utils/request'

export default {
    getProductList(obj) {
        return request({
            url: '/product/list',
            method: 'post',
            data: obj
        })
    },
    getProductDetail(id) {
        return request({
            url: `/product/detail/${id}`,
            method: 'get'
        })
    }
}