import request from '@/utils/request'

export default {
    getCategoryList(pageNum, pageSize) {
        return request({
            url: '/product/category/list',
            method: 'get',
            params: {
                pageNum,
                pageSize
            }
        })
    },
    getAllParentList() {
        return request({
            url: '/product/category/getParentList',
            method: 'get'
        })
    },
    addCategory(obj) {
        return request ({
            url: '/product/category/add',
            method: 'post',
            data: obj
        })
    },
    updateCategory(obj) {
        return request({
            url: '/product/category/update',
            method: 'post',
            data: obj
        })
    },
    removeCategroy(obj) {
        return request({
            url: '/product/category/remove',
            method: 'post',
            data: obj
        })
    },
    getCategoryDetail(id) {
        return request({
            url: `/product/category/detail/${id}`,
            method: 'get'
        })
    },
    updateNavStatus(id, status) {
        return request({
            url: '/product/category/update/navStatus',
            method: 'post',
            params: {
                id,
                status
            }
        })
    },
    updateShowStatus(id, status) {
        return request({
            url: '/product/category/update/showStatus',
            method: 'post',
            params: {
                id,
                status
            }
        })
    },
    getAllCategoryList() {
        return request({
            url: '/product/category/all',
            method: 'get'
        })
    }
}