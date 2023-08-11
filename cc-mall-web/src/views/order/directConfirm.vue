<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="content">
      <div class="product-list-area" v-if="productList.length > 0">
        <div class="product-list">
          <van-card
            v-for="(item, index) in productList"
            :key="index"
            :num="productCount"
            :price="item.price"
            :title="item.productName"
            :thumb="baseUrl + item.productCover"
          />
        </div>
      </div>
      <div class="buttom-area">
        <van-cell
          is-link
          title="优惠券"
          :value="couponAmount"
          @click="couponShow = true"
        />
        <van-cell title="方式">
          <van-radio-group v-model="typeRadio" direction="horizontal">
            <van-radio :name="0">配送</van-radio>
            <van-radio :name="1">自提</van-radio>
          </van-radio-group></van-cell
        >
        <van-cell
          v-if="typeRadio === 0"
          is-link
          title="收货地址"
          @click="addressShow = true"
        />
        <div class="address-area" v-if="typeRadio === 0 && addressIndex >= 0">
          <div class="address-pre">
            <span>{{ addressList[addressIndex].name }}</span
            ><span>{{ addressList[addressIndex].tel }}</span>
          </div>
          <div class="address-detail">
            {{ addressList[addressIndex].address }}
          </div>
        </div>
        <van-submit-bar
          :price="totalPrice"
          button-text="提交订单"
          @submit="onSubmit"
        />
      </div>

      <van-action-sheet
        v-model="couponShow"
        cancel-text="不使用优惠券"
        title="优惠券"
        @cancel="onCancel"
        :closeable="false"
      >
        <div class="coupon-content">
          <van-tabs v-model="couponActive">
            <van-tab title="可使用">
              <div
                class="coupon-list-area"
                v-if="hasCoupon && hasCoupon.length > 0"
              >
                <van-radio-group v-model="couponId">
                  <van-cell-group>
                    <van-cell
                      v-for="(item, index) in hasCoupon"
                      :key="index"
                      clickable
                      @click="changeCouponId(item)"
                    >
                      <template #right-icon>
                        <van-radio :name="item.id" />
                      </template>
                      <div class="coupon-card">
                        <div class="card-up">
                          <div class="left">
                            <div class="price">{{ item.amount }}元</div>
                          </div>
                          <div class="right">
                            <div class="name">{{ item.name }}</div>
                            <div class="date">
                              <span
                                >{{ item.startTime }}~{{ item.endTime }}</span
                              >
                            </div>
                          </div>
                        </div>
                        <div class="card-down"></div>
                      </div>
                    </van-cell>
                  </van-cell-group>
                </van-radio-group>
                <!-- <div class="coupon-card">
                        
                    </div> -->
              </div>
            </van-tab>
            <van-tab title="不可用">
              <div class="coupon-list-area">
                <van-cell-group v-if="noCoupon && noCoupon.length > 0">
                  <van-cell v-for="(item, index) in noCoupon" :key="index">
                    <div class="coupon-card">
                      <div class="card-up">
                        <div class="left">
                          <div class="price">{{ item.amount }}元</div>
                        </div>
                        <div class="right">
                          <div class="name">{{ item.name }}</div>
                          <div class="date">
                            <span>{{ item.startTime }}~{{ item.endTime }}</span>
                          </div>
                        </div>
                      </div>
                      <div class="card-down">{{ item.noUsedReason }}</div>
                    </div>
                  </van-cell>
                </van-cell-group>
              </div>
            </van-tab>
          </van-tabs>
        </div>
      </van-action-sheet>

      <van-action-sheet v-model="addressShow" title="收货地址">
        <div class="address-list-area">
          <div class="address-list" v-if="addressList.length > 0">
            <van-radio-group
              v-for="(item, index) in addressList"
              :key="index"
              v-model="addressIndex"
            >
              <van-radio :name="index">
                <div class="address-card">
                  <div class="card-up">
                    <span>{{ item.name }} </span><span> {{ item.tel }}</span
                    ><van-tag v-if="item.isDefault" type="danger">默认</van-tag>
                  </div>
                  <div class="card-down">{{ item.address }}</div>
                </div>
              </van-radio>
            </van-radio-group>
          </div>
        </div>
      </van-action-sheet>
    </div>
  </div>
</template>

<script>
import MyNavBar from '@/components/MyNavBar'
import imageBaseConfig from '@/utils/imageBaseConfig'
import orderApi from '@/api/order'
import customerApi from '@/api/customer'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      baseUrl: imageBaseConfig.IMAGE_BASE_URL,
      productId: this.$route.query.productId,
      productCount: this.$route.query.count,
      title: this.$route.meta.title,
      productList: [],
      couponAmount: '',
      couponShow: false,
      typeRadio: 0,
      addressShow: false,
      addressIndex: -1,
      addressList: [],
      totalPrice: 0,
      couponActive: 0,
      hasCoupon: [],
      noCoupon: [],
      couponId: null,
      totalPriceBak: 0,
    }
  },
  created() {
    this.getAddress()
    this.getDirectConfirm()
  },
  methods: {
    onSubmit() {
      if (this.typeRadio === 0 && this.addressIndex === -1) {
        this.$dialog.alert({
          message: '请选择收货地址',
        })
        return
      }
      let couponId = this.couponId
      let obj = { couponId: couponId }
      if (this.addressIndex !== -1) {
        console.log(222)
        let addressId = this.addressList[this.addressIndex].id
        obj.addressId = addressId
      }
      obj.orderType = this.typeRadio
      obj.payType = 1 // 这里就默认支付宝
      obj.productId = this.productId
      obj.count = this.productCount
      orderApi.directBuy(obj).then((res) => {
        console.log(res)
        this.$dialog
          .alert({
            message: '订单提交成功，请前往付款',
          })
          .then(() => {
            this.$router.go(-1)
          })
      })
    },
    onCancel() {
      for (const item of this.hasCoupon) {
        if (item.id === this.couponId) {
          this.totalPrice = JSON.parse(JSON.stringify(this.totalPriceBak))
          this.couponId = 0
          return
        }
      }
    },
    changeCouponId(item) {
      if (this.couponId === item.id) return
      this.couponId = item.id
      let temp = this.totalPriceBak
      this.totalPrice = temp - item.amount * 100
    },
    getAddress() {
      customerApi.getAddressList().then((res) => {
        this.addressList = res.data
      })
    },
    getDirectConfirm() {
      orderApi.directConfirm(this.productId, this.productCount).then((res) => {
        this.productList = res.data.productList
        this.count = res.data.count
        this.hasCoupon = res.data.hasCoupon
        this.noCoupon = res.data.noCoupon
        this.totalPrice = res.data.totalPrice * 100
        this.totalPriceBak = JSON.parse(JSON.stringify(this.totalPrice))
        if (!this.hasCoupon || this.hasCoupon.length === 0) {
          this.couponAmount = '无可用优惠券'
        } else {
          this.couponAmount = this.hasCoupon.length + '张可用'
        }
      })
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
    margin-top: 10px;
    .buttom-area {
      margin-top: 10px;
      .address-area {
        margin: 10px 0;
        background-color: #fff;
        .address-pre {
          font-size: 16px;
          padding-top: 10px;
          margin-left: 20px;
        }
        .address-detail {
          font-size: 16px;
          margin-top: 6px;
          margin-left: 20px;
          padding-bottom: 10px;
        }
      }
    }
  }
  .coupon-content {
    padding: 10px 20px;
    .coupon-list-area {
      .van-cell {
        margin-top: 10px;
        border: 1px solid #ddd;
        border-radius: 10px;
      }
      .coupon-card {
        background-color: #fff;
        // border: 1px solid #ddd;

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
  .address-list-area {
    margin-top: 10px;
    padding: 0 20px 10px 20px;
    .address-list {
      .van-radio {
        margin-top: 8px;
      }
      .address-card {
        margin-left: 20px;
        font-size: 20px;
        .card-up {
          .van-tag {
            margin-left: 5px;
          }
        }
      }
    }
  }
}
</style>