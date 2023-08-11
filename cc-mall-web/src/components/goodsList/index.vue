<template>
  <div class="container">
    <div class="goods-list" v-if="goodsList.length !== 0">
      <div
        v-for="(item, index) in goodsList"
        :key="index"
        class="goods-card"
        @click="goToDetail(item.id)"
      >
        <div class="goods-image">
          <van-image
            width="150"
            height="150"
            :src="baseUrl + item.productCover"
            fit="cover"
          />
        </div>
        <div class="title">{{ item.productName }}</div>
        <div class="price">￥{{ item.price }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import baseUrl from '@/utils/imageBaseConfig'
export default {
  name: 'GoodsList',
  props: ['goodsList'],
  data() {
    return {
      baseUrl: baseUrl.IMAGE_BASE_URL,
    }
  },
  methods: {
    goToDetail(id) {
      this.$router.push({
        path: '/productDetail',
        query: {
          productId: id,
        },
      })
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  display: flex;
  justify-content: center;
  .goods-list {
    height: 100%;
    width: 90%;
    padding: 10px;
    display: flex;
    justify-content: space-between;
    flex-flow: row wrap;
    background-color: #f7f7f7;
    .goods-card {
      padding: 6px;
      width: 45%;
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