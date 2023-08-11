import request from '@/utils/request'

export default {
    getAdList(obj) {
        return request({
            url: '/advertising/list',
            method: 'post',
            data: obj
        })
    },
    addAdvertis(obj) {
        return request({
            url: '/advertising/create',
            method: 'post',
            data: obj
        })
    },
    updateAdvertis(obj) {
        return request({
            url: '/advertising/update',
            method: 'post',
            data: obj
        })
    },
    getAdDetail(id) {
        return request({
            url: `/advertising/detail/${id}`,
            method: 'get'
        })
    },
    updateAdPublish(obj) {
        return request({
            url: '/advertising/update/publish',
            method: 'post',
            data: obj
        })
    },
    removeAd(obj) {
        return request({
            url: '/advertising/remove',
            method: 'post',
            data: obj
        })
    }
}