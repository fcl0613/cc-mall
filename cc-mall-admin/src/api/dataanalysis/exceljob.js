import request from '@/utils/request'

export default {
    getJobList(dto) {
        return request({
            url: '/excel/output/job/list',
            method: 'post',
            data: dto
        })
    }
}