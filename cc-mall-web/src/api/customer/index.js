import request from '@/utils/request'

export default {
    login(obj) {
        return request({
            url: '/customer/login',
            method: 'post',
            data: obj
        })
    },
    regist(obj) {
        return request({
            url: '/customer/register',
            method: 'post',
            data: obj
        })
    },
    sendMessage(obj) {
        return request({
            url: '/customer/send/message',
            method: 'post',
            data: obj
        })
    },
    getMyPageInfo() {
        return request({
            url: '/customer/my/content',
            method: 'get'
        })
    },
    getInfo() {
        return request({
            url: '/customer/info',
            method: 'get'
        })
    },
    updateAvatar(ul) {
        return request({
            url: '/customer/update/avatar',
            method: 'post',
            params: {
                ul
            }
        })
    },
    updateGender(gender) {
        return request({
            url: '/customer/update/gender',
            method: 'post',
            params: {
                gender
            }
        })
    },
    updateNickName(name) {
        return request({
            url: '/customer/update/nickName',
            method: 'post',
            params: {
                name
            }
        })
    },
    getAddressList() {
        return request({
            url: '/address/list',
            method: 'get'
        })
    },
    addAddress(obj) {
        return request({
            url: '/address/add',
            method: 'post',
            data: obj
        })
    },
    updateAddress(obj) {
        return request({
            url: '/address/update',
            method: 'post',
            data: obj
        })
    },
    removeAddress(id) {
        return request({
            url: `/address/remove/${id}`,
            method: 'post'
        })
    },
    setDefaultAddress(id) {
        return request({
            url: `/address/setDefault/${id}`,
            method: 'post'
        })
    },
    getAddressInfo(id) {
        return request({
            url: `/address/info/${id}`,
            method: 'get'
        })
    }
}