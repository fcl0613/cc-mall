import request from '@/utils/request'

export default {
    collectProduct(productId) {
        return request({
            url: `/product/favorite/add/${productId}`,
            method: 'post'
        })
    },
    getFavoriteList(pageNum, pageSize) {
        return request({
            url: '/product/favorite/list',
            method: 'get',
            params: {
                pageNum, pageSize
            }
        })
    },
    cancelCollect(productId) {
        return request({
            url: `/product/favorite/cancel/${productId}`,
            method: 'post'
        })
    },
    deleteFavorite(id) {
        return request({
            url: `/product/favorite/delete/${id}`,
            method: 'post'
        })
    }
}