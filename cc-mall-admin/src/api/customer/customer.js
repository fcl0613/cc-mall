import request from '@/utils/request'

export default {
    getCustomerList(pageNum, pageSize, keyword) {
        return request({
            url: '/customer/list',
            method: 'get',
            params: {
                pageNum,
                pageSize,
                keyword
            }
        })
    },
    addCustomer(obj) {
        return request({
            url: '/customer/create',
            method: 'post',
            data: obj
        })
    },
    updateCustomer(obj) {
        return request({
            url: '/customer/update',
            method: 'post',
            data: obj
        })
    },
    getCustomerDetail(id) {
        return request({
            url: `/customer/detail/${id}`,
            method: 'get'
        })
    },
    removeCustomer(obj) {
        return request({
            url: '/customer/remove',
            method: 'post',
            data: obj
        })
    }
}