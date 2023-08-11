<template>
  <div class="container">
    <van-search placeholder="请输入搜索关键词" @click="toSearchPage"/>
    <div class="sw">
      <van-swipe :autoplay="3000" height="150">
        <van-swipe-item v-for="(item, index) in images" :key="index">
          <img v-lazy="item.adPic" @click="toAdPage(item.adLink)" />
        </van-swipe-item>
      </van-swipe>
    </div>
    <div class="cate">
      <van-grid square :border="false">
        <van-grid-item
          class="grid-item"
          v-for="(item, index) in categoryList"
          :key="index"
          :icon="item.icon"
          :text="item.name"
          @click="toProductPage(item.id)"
        />
      </van-grid>
    </div>
    <div class="goods-tab">
      <van-tabs v-model="active" sticky swipeable>
        <van-tab title="春日茶礼"> </van-tab>
        <van-tab title="美酒佳酿"> </van-tab>
        <van-tab title="休闲零食"> </van-tab>
      </van-tabs>
    </div>
    <GoodsList :goodsList="homeTeaProductList" v-if="active === 0"></GoodsList>
    <GoodsList :goodsList="homeWineProductList" v-if="active === 1"></GoodsList>
    <GoodsList :goodsList="homeSnackProductList" v-if="active === 2"></GoodsList>
  </div>
</template>

<script>
import homeApi from '@/api/homeApi'
import baseUrl from '@/utils/imageBaseConfig'
import GoodsList from '@/components/goodsList'
export default {
  components: {
    GoodsList
  },
  data() {
    return {
      images: [],
      active: 0,
      categoryList: [],
      homeTeaProductList: [],
      homeRecommendProduct: [],
      homeNewProduct: [],
      homeWineProductList: [],
      homeSnackProductList: [],
      baseUrl: baseUrl.IMAGE_BASE_URL,
    }
  },
  created() {
    this.getHomeContent()
  },
  methods: {
    getHomeContent() {
      homeApi.getHomeContent().then((res) => {
        this.images = res.data.homeAdvertiseList
        this.categoryList = res.data.homeCategory
        this.homeTeaProductList = res.data.homeTeaProductList
        this.homeRecommendProduct = res.data.homeRecommendProduct
        this.homeNewProduct = res.data.homeNewProduct
        this.homeWineProductList = res.data.homeWineProductList
        this.homeSnackProductList = res.data.homeSnackProductList
      })
    },
    toAdPage(link) {
      console.log(link)
      window.location.href = 'http://' + link
    },
    toProductPage(categoryId) {
      this.$router.push({
        path: '/product',
        query: {
          categoryId: categoryId,
        },
      })
    },
    toSearchPage() {
      this.$router.push({
        path: '/product'
      })
    }
  },
}
</script>

<style lang="less" scoped>
.container {
  background-color: #f7f7f7;
  .sw {
    padding: 10px;
    .van-swipe {
      border-radius: 8px;
      .van-swipe-item {
        img {
          display: inline-block;
          width: 100%;
          height: 200px;
        }
      }
    }
  }
  .cate {
    padding: 10px;
  }
  .goods-tab {
    padding: 10px;
    background-color: #f7f7f7;
  }
  .goods-list {
    height: 100%;
    padding: 10px;
    display: flex;
    justify-content: space-between;
    flex-flow: row wrap;
    background-color: #f7f7f7;
    .goods-card {
      padding: 6px;
      width: 160px;
      background-color: #fff;
      margin-bottom: 6px;
      .goods-image {
        display: flex;
        justify-content: center;
        align-items: center;
      }
      .title {
        padding: 0 2px;
        margin-top: 8px;
        font-size: 12px;
        overflow: hidden;
        text-overflow: 2;
        -webkit-line-clamp: 2;
        display: -webkit-box;
        -webkit-box-orient: vertical;
      }
      .price {
        margin-top: 10px;
        font-size: 14px;
        color: red;
      }
    }
  }
}
</style>
