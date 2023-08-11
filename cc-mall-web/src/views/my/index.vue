<template>
  <div class="container">
    <div class="header">
      <div class="my-info">
        <div class="base-info" @click="toMyInfoPage">
          <div class="avatar">
            <van-image
              round
              width="60px"
              height="60px"
              :src="pageContent.avatar"
            />
          </div>
          <div class="nick-name">{{ pageContent.nickName }}</div>
        </div>
        <div class="point-info">
          <div class="point">{{ pageContent.point }}</div>
          <div class="point-des">积分</div>
        </div>
      </div>
      <div class="order-area">
        <div class="order-banner">
          <div class="banner-left">我的订单</div>
          <div class="banner-right" @click="toOrderPage(0)">全部订单></div>
        </div>
        <div class="order-icon-list">
          <van-badge :content="pageContent.pendingPayment > 0 ? pageContent.pendingPayment : ''">
            <div class="icon-box" @click="toOrderPage(1)">
              <van-icon name="pending-payment" />
              <span>待付款</span>
            </div>
          </van-badge>
          <van-badge :content="pageContent.waitingForDelivery > 0 ? pageContent.waitingForDelivery : ''">
            <div class="icon-box" @click="toOrderPage(2)">
              <van-icon name="send-gift-o" />
              <span>待发货</span>
            </div>
          </van-badge>
          <van-badge :content="pageContent.shipped > 0 ? pageContent.shipped : ''">
            <div class="icon-box" @click="toOrderPage(3)">
              <van-icon name="logistics" />
              <span>待收货</span>
            </div>
          </van-badge>
          <van-badge :content="pageContent.waitingComments > 0 ? pageContent.waitingComments : ''">
            <div class="icon-box" @click="toBeCommentPage">
              <van-icon name="comment-o" />
              <span>待评价</span>
            </div>
          </van-badge>
          <van-badge :content="pageContent.afterSales > 0 ? pageContent.afterSales : ''">
            <div class="icon-box" @click="toOrderApplyPage">
              <van-icon name="after-sale" />
              <span>售后订单</span>
            </div>
          </van-badge>
        </div>
      </div>
    </div>
    <div class="option-list">
      <van-cell-group>
        <van-cell title="我的文章" to="/myPost" is-link />
        <van-cell title="我的优惠券" to="/coupon" is-link />
        <van-cell title="我的收藏" to="/myFavorites" is-link />
        <van-cell title="地址管理" to="/address" is-link />
        <van-cell title="意见反馈" is-link />
        <van-cell title="设置" is-link />
      </van-cell-group>
    </div>
  </div>
</template>

<script>
import customerApi from '@/api/customer'
import baseUrl from '@/utils/imageBaseConfig'
export default {
  data() {
    return {
      pageContent: {},
      baseUrl: baseUrl.IMAGE_BASE_URL
    }
  },
  created() {
    this.getPageContent()
  },
  methods: {
    getPageContent() {
      customerApi.getMyPageInfo().then((res) => {
        this.pageContent = res.data
      })
    },
    toMyInfoPage() {
      this.$router.push({
        path: '/customerInfo'
      })
    },
    toOrderPage(status) {
      this.$router.push({
        path: '/orderList',
        query: {
          status
        }
      })
    },
    toOrderApplyPage() {
      this.$router.push({
        path: '/orderApplyList',
      })
    },
    toBeCommentPage() {
      this.$router.push({
        path: '/productToComment',
      })
    }
  }
}
</script>

<style lang="less" scoped>
.container {
  background-color: #e7e7e7;
  .header {
    background-color: #fff;
    .my-info {
      .base-info {
        margin: 20px 0 0 20px;
        display: flex;
        align-items: center;
        .nick-name {
          font-size: 14px;
          margin-left: 10px;
        }
      }
      .point-info {
        margin-top: 10px;
        margin-left: 20px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        width: 60px;
        .point {
          font-size: 16px;
        }
        .point-des {
          font-size: 10px;
          color: rgb(179, 179, 179);
        }
      }
    }
    .order-area {
      margin-top: 10px;
      padding: 0 20px 10px;
      .order-banner {
        display: flex;
        justify-content: space-between;
        .banner-left {
          font-size: 16px;
        }
        .banner-right {
          font-size: 12px;
          color: #c2c0c0e7;
        }
      }
      .order-icon-list {
        margin-top: 10px;
        display: flex;
        justify-content: space-between;
        .icon-box {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          span {
            margin-top: 8px;
            font-size: 12px;
          }
        }
      }
    }
  }
  .option-list {
    margin-top: 10px;
  }
}
</style>
