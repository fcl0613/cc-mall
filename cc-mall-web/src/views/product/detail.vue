<template>
  <div class="container">
    <div class="nav-bar">
      <van-nav-bar
        title="商品详情"
        left-arrow
        @click-left="reback"
        @click-right="goToCartPage"
      >
        <template #right>
          <van-icon
            style="margin-right: 60px"
            name="shopping-cart-o"
            size="20px"
          /> </template
      ></van-nav-bar>
    </div>
    <div class="goods-base-info">
      <div class="goods-show-pic">
        <van-swipe indicator-color="white" height="300">
          <van-swipe-item
            v-for="(image, index) in productDetail.productCover"
            :key="index"
          >
            <img object-fit="cover" width="100%" :src="baseUrl + image" />
          </van-swipe-item>
        </van-swipe>
      </div>
      <div class="price">￥{{ productDetail.price }}</div>
      <div class="text-area">
        <div class="name-area">
          <div class="goods-name">{{ productDetail.productName }}</div>
          <div class="collect" @click="hasCollect">
            <van-icon v-if="!isFavorite" name="star-o" />
            <van-icon v-if="isFavorite" color="red" name="star" />
          </div>
        </div>
        <div class="goods-description">
          {{ productDetail.subtitle }}
        </div>
      </div>
    </div>
    <div class="comment-area">
      <div class="comment-header">
        <div class="left">评价({{ productDetail.commentCount }})</div>
        <div class="right" @click="toCommentsPage">查看全部></div>
      </div>
      <div class="comment-info">
        <van-button type="default" size="small" color="#ddd" round
          >好评({{ productDetail.positiveCount }})</van-button
        >
        <van-button type="default" size="small" color="#ddd" round
          >中评({{ productDetail.neutralCount }})</van-button
        >
        <van-button type="default" size="small" color="#ddd" round
          >差评({{ productDetail.negativeCount }})</van-button
        >
      </div>
    </div>
    <div class="goods-detail">
      <div class="title">商品详情</div>
      <div class="contant">{{ productDetail.description }}</div>
      <div
        class="pic-list"
        v-if="productDetail.productPics && productDetail.productPics.length > 0"
      >
        <img
          v-for="(image, index) in productDetail.productPics"
          :key="index"
          object-fit="cover"
          width="100%"
          :src="baseUrl + image"
        />
      </div>
    </div>
    <div class="footer">
      <van-goods-action>
        <van-goods-action-icon icon="chat-o" text="客服" @click="showPhone" />
        <van-goods-action-icon
          icon="cart-o"
          text="购物车"
          @click="goToCartPage"
        />
        <van-goods-action-button
          type="warning"
          text="加入购物车"
          @click="cartClick"
        />
        <van-goods-action-button
          type="danger"
          text="立即购买"
          @click="buyClick"
        />
      </van-goods-action>
    </div>

    <van-popup v-model="showActionSheet" @closed="popupCloseEvent" closeable round position="bottom">
      <div class="sheet-container">
        <div class="product-info">
          <div class="info">
            <div class="info-left">
              <img
                v-if="productDetail.productCover"
                :src="baseUrl + productDetail.productCover[0]"
                alt=""
              />
            </div>
            <div class="info-right">
              <div class="product-price">￥{{ productDetail.price }}</div>
              <div class="product-stock">库存{{ productDetail.stock }}</div>
            </div>
          </div>
          <div class="count">
            <div class="count-left">数量</div>
            <div class="count-right"><van-stepper v-model="goodsCount" /></div>
          </div>
        </div>

        <van-button style="width: 100%" color="rgb(255, 59, 59)" type="primary" @click="confirmSub"
          >确定</van-button
        >
      </div>
    </van-popup>
  </div>
</template>

<script>
import productApi from '@/api/product'
import baseImageUrl from '@/utils/imageBaseConfig'
import favoriteApi from '@/api/product/favorite'
import cartApi from '@/api/cart'
export default {
  data() {
    return {
      productId: this.$route.query.productId,
      productDetail: {},
      baseUrl: baseImageUrl.IMAGE_BASE_URL,
      showActionSheet: false,
      goodsCount: 1,
      isFavorite: false,
      buyFlag: false    // 购买购物车标志 true直接购买 false 加入购物车
    }
  },
  created() {
    this.getProductDetail()
  },
  methods: {
    reback() {
      this.$router.go(-1)
    },
    goToCartPage() {
      this.$router.push({
        path: '/layout/shoppingCart'
      })
    },
    getProductDetail() {
      productApi.getProductDetail(this.productId).then((res) => {
        this.productDetail = res.data
        this.isFavorite = this.productDetail.favoriteFlag
      })
    },
    // 立即购买
    buyClick() {
      this.buyFlag = true
      this.showActionSheet = !this.showActionSheet
    },
    // 加入购物车
    cartClick() {
      this.buyFlag = false
      this.showActionSheet = !this.showActionSheet
    },
    // 收藏
    hasCollect() {
      this.isFavorite = !this.isFavorite
      if (this.isFavorite) {
        // 加入收藏
        favoriteApi.collectProduct(this.productId)
      } else {
        // 取消收藏
        favoriteApi.cancelCollect(this.productId)
      }
    },
    toCommentsPage() {
      this.$router.push({
        path: '/commentList',
        query: {
          productId: this.productId
        }
      })
    },
    // 客服展示电话
    showPhone() {
      this.$dialog.alert({
        message: '客服电话：400-5565-4452',
      })
    },
    // 购物车或者直接购买
    confirmSub() {
      if(this.buyFlag) {
        // 走直接购买商品逻辑
        this.$router.push({
          path: '/directConfirm',
          query: {
            productId: this.productId,
            count: this.goodsCount
          }
        })
      }else {
        // 走加入购物车逻辑
        let obj = {productId: this.productId, productCount: this.goodsCount}
        cartApi.addCart(obj).then((res) => {
          this.$toast(res.message)
          this.showActionSheet = !this.showActionSheet
        })
      }
    },
    popupCloseEvent() {
      this.goodsCount = 0
    }
  },
}
</script>

<style lang="less" scoped>
.container {
  background-color: #f7f7f7;
  .nav-bar {
    height: 46px;
    background-color: #fff;
    .van-nav-bar {
      position: fixed;
      width: 100%;
      top: 0;
      z-index: 999;
    }
  }
  .goods-base-info {
    background-color: #fff;
    width: 100%;
    .price {
      background-color: rgb(250, 68, 68);
      color: #fff;
      font-size: 20px;
      padding: 6px 0 6px 10px;
    }
    .text-area {
      margin-top: 8px;
      padding: 0 10px;
      .name-area {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .goods-name {
          width: 70%;
          font-size: 16px;
          font-weight: bold;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
      }
      .goods-description {
        width: 90%;
        margin-top: 3px;
        font-size: 10px;
        padding-bottom: 8px;
        color: #a3a3a3;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }
    }
  }
  .comment-area {
    margin-top: 10px;
    background-color: #fff;
    padding: 5px 10px;
    .comment-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .left {
        font-size: 20px;
        font-weight: bold;
      }
      .right {
        font-size: 10px;
        color: #a3a3a3;
      }
    }
    .comment-info {
      margin-top: 3px;
      display: flex;
      // justify-content: space-between;
      .van-button {
        margin-right: 10px;
      }
    }
  }
  .goods-detail {
    padding: 0 10px;
    background-color: #fff;
    margin-top: 10px;
    .title {
      font-size: 18px;
      padding-top: 10px;
    }
    .contant {
      margin-top: 8px;
      font-size: 16px;
      margin-bottom: 8px;
    }
  }
  .footer {
    height: 1.5rem;
  }
}

.sheet-container {
  //   padding: 20px 0 10px 10px;
  .product-info {
    padding: 10px;
    .info {
      display: flex;
      .info-left {
        img {
          width: 100px;
          height: 100px;
        }
      }
      .info-right {
        margin-left: 10px;
        .product-price {
          font-size: 24px;
          // font-weight: bold;
          color: rgb(255, 59, 59);
        }
        .product-stock {
          margin-top: 8px;
          font-size: 16px;
          color: #a3a3a3;
        }
      }
    }
    .count {
      display: flex;
      justify-content: space-between;
      margin-right: 10px;
    }
  }
}
</style>