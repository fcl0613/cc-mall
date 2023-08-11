import request from '@/utils/request'

export default {
    getSysUserList(pageNum, pageSize, keyword) {
        return request({
            url: '/sysuser/list',
            method: 'get',
            params: {
                pageNum,
                pageSize,
                keyword
            }
        })
    },
    removeUserByIds(ids) {
        return request({
            url: '/sysuser/delete',
            method: 'post',
            data: ids
        })
    },
    addSysUser(user) {
        return request({
            url: '/sysuser/create',
            method: 'post',
            data: user
        })
    },
    getSysUserById(id) {
        return request({
            url: `/sysuser/${id}`,
            method: 'get'
        })
    },
    updateSysUser(user) {
        return request({
            url: '/sysuser/update',
            method: 'post',
            data: user
        })
    },
    getRoles(userId) {
        return request({
            url: `sysuser/getRole/${userId}`,
            method: 'get'
        })
    },
    assignRole(assignRoleObj) {
        return request({
            url: '/sysuser/assignRole',
            method: 'post',
            data: assignRoleObj
        })
    }
}