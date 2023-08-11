import request from '@/utils/request'

export default {
    createComment(obj) {
        return request({
            url: '/post/comment/create',
            method: 'post',
            data: obj
        })
    },
    getComment(obj) {
        return request({
            url: '/post/comment/list',
            method: 'post',
            data: obj
        })
    }
}