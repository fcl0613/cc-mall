<template>
  <div class="container">
    <van-overlay
      :show="loading"
      style="display: flex; align-items: center; justify-content: center"
    >
      <van-loading type="spinner" vertical color="#44d9c5" text-color="#44d9c5">
        数据加载中……
      </van-loading>
    </van-overlay>
    <div class="no-data" v-if="shoppingCarList.length === 0">
      <img
        src="https://ts4.cn.mm.bing.net/th?id=OIP-C.dWKFV2-f2m7VbdgEAFykXgHaHa&w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2"
        @click="toSearch"
        alt=""
      />
      <div class="info">购物车竟然是空的</div>
      <div class="tip">再忙，也要买点什么犒劳自己</div>
    </div>

    <!-- 购物车内容列表 -->
    <div class="shopping-car" v-if="shoppingCarList.length > 0">
      <van-checkbox-group v-model="checkedIds" ref="checkBoxGroupRef">
        <van-swipe-cell v-for="(item, index) in shoppingCarList" :key="index">
          <van-card
            :price="formatPrice(item.price)"
            :title="item.productName"
            style="margin-top: 6px"
          >
            <template slot="thumb">
              <van-checkbox
                :name="item.id"
                checked-color="#b90505"
                icon-size="16px"
              >
                <van-image
                  width="60"
                  height="60"
                  :src="baseUrl + item.productCover"
                />
              </van-checkbox>
            </template>
            <template slot="bottom">
              <div style="float: right">
                <van-stepper
                  v-model="item.productAmount"
                  @plus="plusCount(item.id)"
                  @minus="minusCount(item.id)"
                />
              </div>
            </template>
          </van-card>
          <template #right>
            <van-button
              square
              @click="deleteProduct(item.id)"
              text="删除"
              type="danger"
              style="height: 100%"
            />
          </template>
        </van-swipe-cell>
      </van-checkbox-group>
    </div>

    <div class="footer">
      <van-submit-bar
        :price="computedTotalPrice"
        button-text="去结算"
        @submit="onSubmit"
      >
        <van-checkbox
          :disabled="shoppingCarList.length === 0 ? true : false"
          v-model="checkAllFlag"
          @click="checkAll"
          >全选</van-checkbox
        >
        <!-- <template #tip>
          你的收货地址不支持同城送,
          <span @click="onClickEditAddress">修改地址</span>
        </template> -->
      </van-submit-bar>
    </div>
  </div>
</template>

<script>
import baseImageUrl from '@/utils/imageBaseConfig'
import cartApi from '@/api/cart'
export default {
  data() {
    return {
      checkAllFlag: false,
      loading: false,
      totalPrice: 0,
      shoppingCarList: [],
      checkedIds: [],
      baseUrl: baseImageUrl.IMAGE_BASE_URL,
    }
  },
  computed: {
    computedTotalPrice() {
      let totalPrice = 0
      for (let item of this.shoppingCarList) {
        if (this.checkedIds.indexOf(item.id) !== -1) {
          let temp = item.price * item.productAmount
          totalPrice += temp
        }
      }
      return totalPrice * 100
    },
    compuetdCheckAll() {
      if (
        this.checkedIds.length > 0 &&
        this.checkedIds.length === this.shoppingCarList.length
      ) {
        this.checkAllFlag = true
        return
      }
      if (
        this.checkedIds.length > 0 &&
        this.checkedIds.length < this.shoppingCarList.length
      ) {
        this.checkAllFlag = false
        return
      }
      if (this.checkedIds.length === 0) {
        this.checkAllFlag = false
        return
      }
    },
  },
  created() {
    this.getCartList()
  },
  methods: {
    // 卡片格式化价格
    formatPrice(price) {
      let p = price * 100
      p = p.toString()
      let z = p.substring(0, p.length - 2)
      let w = p.substring(p.length - 2)
      return z + '.' + w
    },
    getCartList() {
      cartApi.getCartList().then((res) => {
        this.shoppingCarList = res.data.list
        this.totalPrice = res.data.totalPrice * 100
      })
    },
    onSubmit() {
      if (this.checkedIds.length === 0) {
        this.$dialog.alert({
          message: '请选择商品',
        })
        return
      }
      this.$router.push({
        path: '/confirmOrder',
        query: {
          carts: this.checkedIds
        }
      })
    },
    toSearch() {
      console.log('跳转到搜索页面')
      this.$router.push({
        path: '/'
      })
    },
    checkAll() {
      if (this.checkedIds.length !== 0) {
        if (
          this.checkedIds.length === this.shoppingCarList.length &&
          !this.checkAllFlag
        ) {
          this.$refs.checkBoxGroupRef.toggleAll(false)
        } else {
          this.$refs.checkBoxGroupRef.toggleAll(true)
        }
      } else {
        this.$refs.checkBoxGroupRef.toggleAll(true)
      }
    },
    deleteProduct(id) {
      this.$dialog
        .confirm({
          title: '提示',
          message: '确定要删除该商品吗',
        })
        .then(() => {
          // on confirm
          console.log('确定')
          cartApi.removeCart(id).then((res) => {
            this.$toast(res.message)
            this.getCartList()
          })
        })
        .catch(() => {
          // on cancel
        })
    },
    // 购物车商品数量增加
    plusCount(val) {
      // console.log(val)
      cartApi.plusCart(val).then((res) => {})
    },
    // 购物车商品数量减少
    minusCount(val) {
      // console.log(val)
      cartApi.minusCart(val).then((res) => {})
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  .no-data {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top: 50px;
    img {
      width: 100px;
      height: 100px;
      border-radius: 50%;
    }
    .info {
      font-size: 18px;
      margin-top: 10px;
      color: #484842;
    }
    .tip {
      font-size: 12px;
      margin-top: 6px;
      color: #909083;
    }
  }

  .shopping-car {
    .store-group {
      margin: 3vw 0;
      .goods-group {
        // padding: 2vw 4vw;
        .goods-card {
          margin: 0 10px 0 10px;
          //   padding: 0 10px 0 10px;
          background-color: #fff;
        }
      }
    }

    .delete-button {
      height: 100%;
    }
  }
  .footer {
    .van-submit-bar {
      margin-bottom: 51px;
    }
  }
}
</style>
