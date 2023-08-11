import request from '@/utils/request'

export default {
    getHomeContent() {
        return request({
            url: '/home/content',
            method: 'get'
        })
    }
}