<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="order-content">
      <van-tabs v-model="active" @click="changeStaus">
        <van-tab title="全部订单"></van-tab>
        <van-tab title="待付款"></van-tab>
        <van-tab title="待发货"></van-tab>
        <van-tab title="待收货"></van-tab>
        <van-tab title="已完成"></van-tab>
        <van-tab title="已关闭"></van-tab>
      </van-tabs>
      <div class="list-area">
        <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
          <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
          >
            <div class="order-list" v-if="orderList.length > 0">
              <div
                class="order-card"
                v-for="(item, index) in orderList"
                :key="index"
              >
                <div class="order-card-header">
                  <div class="order-id">
                    订单号：<span>{{ item.orderSn }}</span>
                  </div>
                  <div class="order-status" v-if="item.orderStatus === 0">
                    待付款
                  </div>
                  <div class="order-status" v-if="item.orderStatus === 1">
                    待发货
                  </div>
                  <div class="order-status" v-if="item.orderStatus === 2">
                    已发货
                  </div>
                  <div class="order-status" v-if="item.orderStatus === 3">
                    已完成
                  </div>
                  <div class="order-status" v-if="item.orderStatus === 4">
                    已关闭
                  </div>
                </div>
                <div class="pro-list">
                  <div
                    class="pro-card"
                    v-for="(element, index) in item.orderDetailList"
                    :key="index"
                  >
                    <div class="left">
                      <van-image
                        width="80"
                        height="80"
                        :src="baseUrl + element.productCover"
                      />
                    </div>
                    <div class="right">
                      <div class="right-up">{{ element.productName }}</div>
                      <div class="right-down">
                        <div class="price">￥{{ element.productPrice }}</div>
                        <div class="count">{{ element.productCnt }}</div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="order-card-bottom">
                  <div class="coupon">
                    优惠 <span>{{ item.couponAmount }}</span> 元
                  </div>
                  <div class="total-price">合计{{ item.totalPrice }}</div>
                  <div
                    class="handle-btn"
                    v-if="item.orderStatus === 0 || item.orderStatus === 1"
                  >
                    <van-button size="mini" v-if="item.orderStatus === 0" 
                      @click="payNotify(item.orderId)">去付款</van-button
                    >
                    <van-button size="mini" @click="cancelOrder(item.orderId)"
                      >取消订单</van-button
                    >
                  </div>
                  <div
                    class="handle-btn"
                    v-if="item.orderStatus === 2 || item.orderStatus === 3"
                  >
                    <van-button size="mini" v-if="item.orderStatus === 2" @click="confirm(item.orderId)">确认收货</van-button>
                    <van-button size="mini" @click="removeOrder(item.orderId)">删除订单</van-button>
                    <van-button size="mini" @click="toApplyPage(item.orderId)">申请退款</van-button>
                  </div>
                  <div
                    class="handle-btn"
                    v-if="item.orderStatus === 4"
                  >
                    <van-button size="mini" @click="removeOrder(item.orderId)">删除订单</van-button>
                  </div>
                </div>
              </div>
            </div>
          </van-list>
        </van-pull-refresh>
      </div>
    </div>
  </div>
</template>

<script>
import MyNavBar from '@/components/MyNavBar'
import orderApi from '@/api/order'
import imageBaseConfig from '@/utils/imageBaseConfig'
import alipayApi from '@/api/alipay'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      active: this.$route.query.status,
      //   orderStatus: this.$route.query.status,
      title: this.$route.meta.title,
      pageNum: 0,
      pageSize: 20,
      refreshing: false,
      loading: false,
      finished: false,
      orderList: [],
      total: 0,
      baseUrl: imageBaseConfig.IMAGE_BASE_URL,
    }
  },
  methods: {
    getOrderList() {
      let obj = { pageNum: this.pageNum, pageSize: this.pageSize }
      if (this.active === 1) {
        obj.orderStatus = 0
      }
      if (this.active === 2) {
        obj.orderStatus = 1
      }
      if (this.active === 3) {
        obj.orderStatus = 2
      }
      if (this.active === 4) {
        obj.orderStatus = 3
      }
      if (this.active === 5) {
        obj.orderStatus = 4
      }
      orderApi.getOrderList(obj).then((res) => {
        // console.log(res.data.orderList)
        this.orderList.push(...res.data.orderList)
        this.total = res.data.total
        if (this.total == this.orderList.length) {
          this.finished = true
        }
        this.loading = false
        this.refreshing = false
      })
    },
    onRefresh() {
      this.orderList = []
      this.pageNum = 1
      this.getOrderList()
    },
    onLoad() {
      this.pageNum = this.pageNum + 1
      this.getOrderList()
    },
    changeStaus(val) {
      console.log(val)
      this.onRefresh()
    },
    cancelOrder(id) {
      this.$dialog
        .confirm({
          title: '取消订单',
          message: '确定取消该订单吗？',
        })
        .then(() => {
          orderApi.cancelOrder(id).then((res) => {
            this.onRefresh()
          })
        })
        .catch(() => {
          // on cancel
        })
    },
    removeOrder(id) {
      orderApi.deleteOrder(id).then((res) => {
        this.$toast(res.message)
        this.onRefresh()
      })
    },
    confirm(id) {
      orderApi.confirmOrder(id).then((res) => {
        this.$toast(res.message)
        this.onRefresh()
      })
    },
    toApplyPage(id) {
      this.$router.push({
        path: '/applyEdit',
        query: {
          id
        }
      })
    },
    payNotify(id) {
      alipayApi.payNotify(id).then((res) => {
        this.$toast.success(res.message)
        this.onRefresh()
      })
    }
  },
}
</script>

<style lang="less" scoped>
.container {
  height: 100%;
  background-color: #ececec;
  .nav-bar {
    height: 46px;
  }
  .order-content {
    .list-area {
      padding: 10px;
      background-color: #ececec;
      .order-list {
        .order-card {
          background-color: #fff;
          border: 1px solid #ddd;
          padding: 10px 20px;
          margin-bottom: 10px;
          .order-card-header {
            display: flex;
            justify-content: space-between;
            .order-id {
              font-size: 12px;
            }
            .order-status {
              display: flex;
              justify-content: flex-end;
              font-size: 16px;
              color: rgb(65, 198, 250);
            }
          }
          .pro-list {
            margin-top: 6px;
            .pro-card {
              display: flex;
              align-items: center;
              //   justify-content: space-between;
              //   .left {

              //   }
              .right {
                margin-left: 60px;
                width: 100%;
                .right-up {
                  font-size: 18px;
                  overflow: hidden;
                }
                .right-down {
                  margin-top: 20px;
                  display: flex;
                  justify-content: space-between;
                  font-size: 16px;
                  align-items: center;
                }
              }
            }
          }
          .order-card-bottom {
            margin-top: 10px;
            display: flex;
            justify-content: flex-end;
            align-items: center;
            .coupon {
              font-size: 12px;
              margin-right: 10px;
            }
            .total-price {
              font-size: 12px;
              margin-right: 10px;
            }
            .handle-btn {
              display: flex;
              justify-items: center;
            }
          }
        }
      }
    }
  }
}
</style>