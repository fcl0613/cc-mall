import request from '@/utils/request'

export default{
    getWebLogList(queryDto) {
        return request({
            url: '/sysweblog/list',
            method: 'post',
            data: queryDto
        })
    }
}