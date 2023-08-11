import request from '@/utils/request'

export default {
    getApplyList(obj) {
        return request({
            url: '/order/returnApply/list',
            method: 'post',
            data: obj
        })
    },
    agreeeApply(id) {
        return request({
            url: `/order/returnApply/agree/${id}`,
            method: 'post',
        })
    },
    refuseApply(obj) {
        return request({
            url: '/order/returnApply/refuse',
            method: 'post',
            data: obj
        })
    },
    finishApply(id) {
        return request({
            url: `/order/returnApply/finish/${id}`,
            method: 'post'
        })
    }
}