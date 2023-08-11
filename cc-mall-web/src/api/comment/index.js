import request from '@/utils/request'

export default {
    getToBeCommentList(pageNum, pageSize) {
        return request({
            url: '/product/to/comment/list',
            method: 'get',
            params: {
                pageNum, pageSize
            }
        })
    },
    createComment(obj) {
        return request({
            url: '/product/comment/create',
            method: 'post',
            data: obj
        })
    },
    getCommentList(obj) {
        return request({
            url: '/comment/list',
            method: 'post',
            data: obj
        })
    }
}