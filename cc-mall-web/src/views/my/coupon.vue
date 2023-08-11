<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="content">
      <van-tabs v-model="active">
        <van-tab title="未领取">
          <div class="coupon-list-area">
            <div class="coupon-list" v-if="unReceived && unReceived.length > 0">
              <div
                class="coupon-card"
                v-for="(item, index) in unReceived"
                :key="index"
              >
                <div class="card-up">
                  <div class="left">
                    <div class="price">{{ item.amount }}元</div>
                  </div>
                  <div class="right">
                    <div class="name">{{ item.name }}</div>
                    <div class="date">{{ item.effectTime }}</div>
                  </div>
                </div>
                <div class="card-down">
                  <div class="to-use-btn" @click="received(item)">立即领取</div>
                </div>
              </div>
            </div>
            <van-empty v-else description="暂无数据" />
          </div>
        </van-tab>
        <van-tab title="未使用">
          <div class="coupon-list-area">
            <div class="coupon-list" v-if="unUsed && unUsed.length > 0">
              <div
                class="coupon-card"
                v-for="(item, index) in unUsed"
                :key="index"
              >
                <div class="card-up">
                  <div class="left">
                    <div class="price">{{ item.amount }}元</div>
                  </div>
                  <div class="right">
                    <div class="name">{{ item.name }}</div>
                    <div class="date">{{ item.effectTime }}</div>
                  </div>
                </div>
                <div class="card-down">
                  <div class="to-use-btn" @click="toUse(item)">立即使用</div>
                </div>
              </div>
            </div>
            <van-empty v-else description="暂无数据" />
          </div>
        </van-tab>
        <van-tab title="已过期">
          <div class="coupon-list-area">
            <div class="coupon-list" v-if="exported && exported.length > 0">
              <div
                class="coupon-card"
                v-for="(item, index) in exported"
                :key="index"
              >
                <div class="card-up">
                  <div class="left">
                    <div class="price">{{ item.amount }}元</div>
                  </div>
                  <div class="right">
                    <div class="name">{{ item.name }}</div>
                    <div class="date">{{ item.effectTime }}</div>
                  </div>
                </div>
                <div class="card-down">
                  <div class="to-use-btn">已过期</div>
                </div>
              </div>
            </div>
            <van-empty v-else description="暂无数据" />
          </div>
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script>
import MyNavBar from '@/components/MyNavBar'
import couponApi from '@/api/coupon'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      title: this.$route.meta.title,
      active: 0,
      unReceived: [],
      unUsed: [],
      exported: [],
    }
  },
  created() {
    this.getCoupon()
  },
  methods: {
    getCoupon() {
      couponApi.getCouponList().then((res) => {
        this.unReceived = res.data.unReceivedList
        this.unUsed = res.data.unUsedList
        this.exported = res.data.exportedList
      })
    },
    received(item) {
    //   console.log(item)
      couponApi.receiveCoupon(item.id).then((res) => {
        this.$toast(res.message)
        this.getCoupon()
      })
    },
    toUse(item) {
      console.log(item)
      if (item.useType === 0) {
        // 通用优惠券跳转到首页
        this.$router.push({
          path: '/',
        })
      }
      if (item.useType === 1) {
        this.$router.push({
          path: '/product',
          query: {
            categoryId: item.categoryId,
          },
        })
      }
      if (item.useType === 2) {
        this.$router.push({
          path: '/productDetail',
          query: {
            productId: item.productId,
          },
        })
      }
    },
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
  .content {
    .coupon-list-area {
      padding: 10px 20px;
      background-color: #ececec;
      .coupon-list {
        .coupon-card {
          background-color: #fff;
          border: 1px solid #ddd;
          border-radius: 10px;
          padding: 6px;
          margin-top: 10px;
          // position: absolute;
          .card-up {
            display: flex;
            justify-content: space-between;
            // justify-content: baseline;
            align-items: center;
            .left {
              width: 40%;
              .price {
                color: red;
                font-size: 40px;
              }
            }
            .right {
              // width: 70%;
              margin-right: 30px;
              .name {
                font-size: 18px;
              }
              .date {
                margin-top: 6px;
                font-size: 12px;
              }
            }
          }
          .card-down {
            // float: right;
            margin-top: 8px;
            display: flex;
            justify-content: flex-end;
            .to-use-btn {
              cursor: pointer;
              font-size: 14px;
              margin-right: 30px;
            }
          }
        }
      }
    }
  }
}
</style>