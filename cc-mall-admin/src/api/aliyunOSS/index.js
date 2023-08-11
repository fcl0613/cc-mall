import request from '@/utils/request'

export default {
    getAliyunOssPolicy() {
        return request({
            url: 'aliyun/oss/policy',
            method: 'get'
        })
    }
}