import request from '@/utils/request'

export default {
    getHomePostList(obj) {
        return request({
            url: '/post/home/list',
            method: 'post',
            data: obj
        })
    },
    getPostInfo(id) {
        return request({
            url: `/post/info/${id}`,
            method: 'get'
        })
    },
    postLike(id) {
        return request({
            url: `/post/like/${id}`,
            method: 'post'
        })
    },
    postUnLike(id) {
        return request({
            url: `/post/unLike/${id}`,
            method: 'post'
        })
    },
    createPost(obj) {
        return request({
            url: '/post/create',
            method: 'post',
            data: obj
        })
    },
    getPostDetail(id) {
        return request({
            url: `/post/my/detail/${id}`,
            method: 'get'
        })
    },
    getMyPostList(obj) {
        return request({
            url: '/post/my/list',
            method: 'post',
            data: obj
        })
    },
    updatePost(obj) {
        return request({
            url: '/post/update',
            method: 'post',
            data: obj
        })
    },
    removePost(id) {
        return request({
            url: `/post/remove/${id}`,
            method: 'post'
        })
    }
}