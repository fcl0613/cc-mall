import request from '@/utils/request'

export default {
    getAll() {
        return request({
            url: '/category/all',
            method: 'get'
        })
    }
}