import request from '@/utils/request'

export default {
    getLoginLogList(queryDto) {
        return request({
            url: '/sysloginlog/list',
            method: 'post',
            data: queryDto
        })
    }
}