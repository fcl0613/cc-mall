import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/views/layout'
import Home from '@/views/home'
import Categoary from '@/views/categoary'
import CommunityCircle from '@/views/communitycircle'
import My from '@/views/my'
import ShoppingCart from '@/views/shoppingcart'
import Login from '@/views/login'
import Register from '@/views/register'
import {getToken} from '@/utils/token'
import Product from '@/views/product'
import Detail from '@/views/product/detail'
import CustomerInfo from '@/views/my/myInfo'
import EditNickName from '@/views/my/editNickName'
import Favorites from '@/views/my/favorites'
import Address from '@/views/my/address'
import AddOrUpdateAddress from '@/views/my/addOrUpdateAddress'
import ConfirmOrder from '@/views/order/confirmOrder'
import coupon from '@/views/my/coupon'
import orderList from '@/views/order/orderList'
import directConfirm from '@/views/order/directConfirm'
import orderApplyList from '@/views/order/orderApplyList'
import toBeComment from "@/views/product/comment/toBeComment"
import editComment from '@/views/product/comment/editComment'
import applyEdit from '@/views/order/applyEdit'
import commentList from '@/views/comment'
import postInfo from '@/views/communitycircle/info'
import postAdd from '@/views/communitycircle/add'
import myPost from '@/views/my/myPost'
import postEdit from '@/views/communitycircle/edit'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        redirect: '/layout'
    },
    {
        path: '/layout',
        component: Layout,
        redirect: '/layout/home',
        children: [
            {
                path: 'home',
                component: Home,
                meta: {
                    title: '精选'
                }
            },
            {
                path: 'categoary',
                component: Categoary,
                meta: {
                    title: '发现'
                }
            },
            {
                path: 'discuss',
                component: CommunityCircle,
                meta: {
                    title: '圈子'
                }
            },
            {
                path: 'shoppingCart',
                component: ShoppingCart,
                meta: {
                    title: '购物车'
                }
            },
            {
                path: 'my',
                component: My,
                meta: {
                    title: '我的'
                }
            }
        ]
    },
    {
        path: '/login',
        component: Login
    },
    {
        path: '/register',
        component: Register
    },
    {
        path: '/product',
        component: Product,
        meta: {
            title: '商品列表'
        }
    },
    {
        path: '/productDetail',
        component: Detail,
        meta: {
            title: '商品详情'
        }
    },
    {
        path: '/customerInfo',
        component: CustomerInfo,
        meta: {
            title: '个人资料'
        }
    },
    {
        path: '/editNickName',
        component: EditNickName,
        meta: {
            title: '编辑昵称'
        }
    },
    {
        path: '/myFavorites',
        component: Favorites,
        meta: {
            title: '我的收藏'
        }
    },
    {
        path: '/address',
        component: Address,
        meta: {
            title: '地址管理'
        }
    },
    {
        path: '/addAddress',
        component: AddOrUpdateAddress,
        meta: {
            title: '添加地址'
        }
    },
    {
        path: '/confirmOrder',
        component: ConfirmOrder,
        meta: {
            title: '订单确认'
        }
    },
    {
        path: '/directConfirm',
        component: directConfirm,
        meta: {
            title: '订单确认'
        }
    },
    {
        path: '/coupon',
        component: coupon,
        meta: {
            title: '优惠券'
        }
    },
    {
        path: '/orderList',
        component: orderList,
        meta: {
            title: '订单列表'
        }
    },
    {
        path: '/orderApplyList',
        component: orderApplyList,
        meta: {
            title: '售后订单'
        }
    },
    {
        path: '/productToComment',
        component: toBeComment,
        meta: {
            title: '待评价商品'
        }
    },
    {
        path: '/editComment',
        component: editComment,
        meta: {
            title: '评论'
        }
    },
    {
        path: '/applyEdit',
        component: applyEdit,
        meta: {
            title: '退款申请'
        }
    },
    {
        path: '/commentList',
        component: commentList,
        meta: {
            title: '评论列表'
        }
    },
    {
        path: '/postInfo',
        component: postInfo,
        meta: {
            title: 'cc动态'
        }
    },
    {
        path: '/postAdd',
        component: postAdd,
        meta: {
            title: '文章添加'
        }
    },
    {
        path: '/myPost',
        component: myPost,
        meta: {
            title: '我的文章'
        }
    },
    {
        path: '/postEdit',
        component: postEdit,
        meta: {
            title: '文章修改'
        }
    }
]

const router = new VueRouter({
    routes
})

router.beforeEach((to, from, next) => {
    // console.log(to.path)
    if (to.path === '/login' || to.path === '/register' || to.path === '/layout/home') return next()
    const token = getToken()
    // console.log(token)
    if (!token) {
        return next('/login')
    }
    next()
})

export default router
