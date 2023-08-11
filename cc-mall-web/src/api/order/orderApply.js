import request from '@/utils/request'

export default {
    getApplyList(pageNum, pageSize) {
        return request({
            url: '/order/return/apply/list',
            method: 'get',
            params: {
                pageNum, pageSize
            }
        })
    },
    createApply(obj) {
        return request({
            url: '/order/return/apply/create',
            method: 'post',
            data: obj
        })
    }
}