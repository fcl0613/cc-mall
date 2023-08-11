import request from '@/utils/request'

export default {
    addSysmenu(sysMenu) {
        return request({
            url: '/sysmenu/create',
            method: 'post',
            data: sysMenu
        })
    },
    getAll() {
        return request({
            url: '/sysmenu/all',
            method: 'get'
        })
    },
    updateSysmenu(sysMenu) {
        return request({
            url: '/sysmenu/update',
            method: 'post',
            data: sysMenu
        })
    },
    deleteSysMenuById(id) {
        return request({
            url: `/sysmenu/delete/${id}`,
            method: 'post'
        })
    },
    getSysMenuById(id) {
        return request({
            url: `sysmenu/${id}`,
            method: 'get'
        })
    }
}