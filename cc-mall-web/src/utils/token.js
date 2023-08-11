
const TOKEN_KEY = 'PORTAL_TOKEN'

export function setToken(token) { // 将token存入localStorage
    return localStorage.setItem(TOKEN_KEY, token)
}

export function getToken() {	//拿到localStorage中的token
    return localStorage.getItem(TOKEN_KEY)
}

export function removeToken() {  //退出登录时调用销毁token
    return localStorage.removeItem(TOKEN_KEY)
}