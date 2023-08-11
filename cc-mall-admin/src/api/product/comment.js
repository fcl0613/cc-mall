import request from '@/utils/request'

export default {
    getCommentList(obj) {
        return request({
            url: '/comment/list',
            method: 'post',
            data: obj
        })
    },
    remove(id) {
        return request({
            url: `/comment/remove/${id}`,
            method: 'post'
        })
    }
}