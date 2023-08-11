import request from '@/utils/request'

export default {
    getPersonaData() {
        return request({
            url: '/dataAnalysis/persona',
            method: 'get'
        })
    },
    createJob(jobName) {
        return request({
            url: '/excel/output/job/create',
            method: 'post',
            params: {
                jobName
            }
        })
    }
}