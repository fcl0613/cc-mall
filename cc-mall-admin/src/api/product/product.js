import request from '@/utils/request'

export default {
    getProductList(obj) {
        return request({
            url: '/product/list',
            method: 'post',
            data: obj
        })
    },
    addProduct(obj) {
        return request({
            url: '/product/add',
            method: 'post',
            data: obj
        })
    },
    updateProduct(obj) {
        return request({
            url: '/product/update',
            method: 'post',
            data: obj
        })
    },
    removeProduct(obj) {
        return request({
            url: '/product/remove',
            method: 'post',
            data: obj
        })
    },
    getProductDetail(id) {
        return request({
            url: `/product/detail/${id}`,
            method: 'get'
        })
    },
    changePublish(obj) {
        return request({
            url: '/product/change/publish',
            method: 'post',
            data: obj
        })
    },
    changeRecommend(obj) {
        return request({
            url: '/product/change/recommend',
            method: 'post',
            data: obj
        })
    },
    changeNew(obj) {
        return request({
            url: '/product/change/new',
            method: 'post',
            data: obj
        })
    },
    stockAdd(obj) {
        return request({
            url: '/product/stock/add',
            method: 'post',
            data: obj
        })
    },
    batchPublish(obj) {
        return request({
            url: '/product/batch/publish',
            method: 'post',
            data: obj
        })
    },
    batchUnPublish(obj) {
        return request({
            url: '/product/batch/unPublish',
            method: 'post',
            data: obj
        })
    },
    batchRecommed(obj) {
        return request({
            url: '/product/batch/recommend',
            method: 'post',
            data: obj
        })
    },
    batchUnRecommed(obj) {
        return request({
            url: '/product/batch/unRecommend',
            method: 'post',
            data: obj
        })
    },
    batchNew(obj) {
        return request({
            url: '/product/batch/new',
            method: 'post',
            data: obj
        })
    },
    batchUnNew(obj) {
        return request({
            url: '/product/batch/unNew',
            method: 'post',
            data: obj
        })
    },
    findAll(name) {
        return request({
            url: '/product/all',
            method: 'get',
            params: {
                name
            }
        })
    }
}