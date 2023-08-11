<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="content">
      <div class="order-list-area">
        <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
          <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
          >
            <div class="order-list" v-if="orderList.length > 0">
              <div class="order-area" v-for="(item, index) in orderList" :key="index">
                <div class="title">
                  <div class="order-id">订单编号：<span>{{ item.orderSn }}</span></div>
                  <div class="order-status">{{ item.statusStr }}</div>
                </div>
                <div class="product-list-area" v-if="item.orderDetails.length > 0">
                  <div class="product-list">
                    <van-card
                      v-for="(element, index) in item.orderDetails"
                      :key="index"
                      :num="element.productCnt"
                      :price="element.productPrice"
                      :title="element.productName"
                      :thumb="baseUrl + element.productCover"
                    />
                  </div>
                </div>
                <div class="area-bottom">
                  <div class="handle-note" v-if="item.handleNode">{{ item.handleNode }}</div>
                  <van-button type="info" size="mini" v-if="item.status === 3" @click="toApply(item.orderDetails[0].orderId)">继续申诉</van-button>
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
import imageBaseConfig from '@/utils/imageBaseConfig'
import orderApply from '@/api/order/orderApply'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      baseUrl: imageBaseConfig.IMAGE_BASE_URL,
      title: this.$route.meta.title,
      loading: false,
      finished: false,
      refreshing: false,
      orderList: [],
      total: 0,
      pageNum: 0,
      pageSize: 20,
    }
  },
  created() {
    // this.onRefresh()
  },
  methods: {
    onLoad() {
      this.pageNum = this.pageNum + 1
      this.getOrderList()
    },
    onRefresh() {
      this.orderList = []
      this.pageNum = 1
      this.total = 0
      this.getOrderList()
    },
    getOrderList() {
      orderApply.getApplyList(this.pageNum, this.pageSize).then((res) => {
        this.orderList.push(...res.data.list)
        this.total = res.data.total
        if (this.total === this.orderList.length) {
          this.finished = true
        }
        this.loading = false
        this.refreshing = false
      })
    },
    toApply(id) {
      // console.log(id)
      this.$router.push({
        path: '/applyEdit',
        query: {
          id
        }
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
  .content {
    padding: 10px 20px;
    background-color: #ececec;
    .order-list-area {
      .order-list {
        margin-bottom: 10px;
        background-color: #ececec;
        .order-area {
          padding: 0 10px;
          margin-bottom: 6px;
          background-color: #fff;
          .title {
            display: flex;
            justify-content: space-between;
            align-items: center;
            .order-id {
              font-size: 16px;
            }
            .order-status {
              font-size: 20px;
              color: rgb(65, 198, 250);
            }
          }
          .product-list-area {
            margin-top: 6px;
            .product-list {
              .van-card {
                margin-bottom: 8px;
              }
            }
          }
          .area-bottom {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-bottom: 8px;
            .handle-note {
              font-size: 12px;
              color: #f08c2f;
              overflow: hidden;
            }
          }
        }
      }
    }
  }
}
</style>