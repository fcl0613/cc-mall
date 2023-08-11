import request from '@/utils/request'

export default {
    getRoleList(pageNum, pageSize, keyword) {
        return request({
            url: '/sysrole/list',
            method: 'get',
            params: {
                pageNum,
                pageSize,
                keyword
            }
        })
    },
    removeRoleByIds(ids) {
        return request({
            url: '/sysrole/delete',
            method: 'post',
            data: ids
        })
    },
    addSysRole(role) {
        return request({
            url: '/sysrole/create',
            method: 'post',
            data: role
        })
    },
    getSysRoleById(id) {
        return request({
            url: `/sysrole/${id}`,
            method: 'get'
        })
    },
    updateSysRole(role) {
        return request({
            url: '/sysrole/update',
            method: 'post',
            data: role
        })
    },
    getConferredMenu(roleId) {
        return request({
            url: `/sysrole/getConferredMenu/${roleId}`,
            method: 'get'
        })
    },
    conferredMenu(obj) {
        return request({
            url: 'sysrole/conferredMenu',
            method: 'post',
            data: obj
        })
    }
}