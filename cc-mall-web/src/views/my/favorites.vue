<template>
  <div class="container">
    <van-nav-bar
      :title="title"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
    />
    <van-overlay
      :show="loading"
      style="display: flex; align-items: center; justify-content: center"
    >
      <van-loading type="spinner" vertical color="#44d9c5" text-color="#44d9c5">
        数据加载中……
      </van-loading>
    </van-overlay>
    <van-empty v-if="favoriteList.length === 0" description="暂无收藏" />
    <div class="shopping-car" v-if="favoriteList.length > 0">
      <van-swipe-cell v-for="(item, index) in favoriteList" :key="index">
        <van-card
          :price="formatPrice(item.productPrice)"
          :title="item.productName"
          style="margin-top: 6px"
          @click="toProductdetail(item.productId)"
        >
          <template slot="thumb">
            <van-image
              width="60"
              height="60"
              :src="baseUrl + item.productCover"
            />
          </template>
        </van-card>
        <template #right>
          <van-button
            square
            @click="removeFavorite(item.id)"
            text="删除"
            type="danger"
            style="height: 100%"
          />
        </template>
      </van-swipe-cell>
    </div>
  </div>
</template>

<script>
import productFavoriteApi from '@/api/product/favorite'
import imageBaseConfig from '@/utils/imageBaseConfig'
export default {
  data() {
    return {
      title: this.$route.meta.title,
      loading: false,
      favoriteList: [],
      total: 0,
      baseUrl: imageBaseConfig.IMAGE_BASE_URL,
    }
  },
  created() {
    this.loading = true
    this.getFavoriteList()
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1)
    },
    getFavoriteList() {
      productFavoriteApi.getFavoriteList().then((res) => {
        this.favoriteList = res.data.list
        this.total = res.data.total
        this.loading = false
      })
    },
    removeFavorite(id) {
      this.$dialog
        .confirm({
          title: '提示',
          message: '确定要删除该收藏吗',
        })
        .then(() => {
          // on confirm
          console.log('确定')
          productFavoriteApi.deleteFavorite(id).then((res) => {
            this.$toast(res.message)
            this.getFavoriteList()
          })
        })
        .catch(() => {
          // on cancel
        })
    },
    // 卡片格式化价格
    formatPrice(price) {
      let p = price * 100
      p = p.toString()
      let z = p.substring(0, p.length - 2)
      let w = p.substring(p.length - 2)
      return z + '.' + w
    },
    toProductdetail(productId) {
      this.$router.push({
        path: '/productDetail',
        query: {
          productId
        }
      })
    }
  },
}
</script>

<style lang="less" scoped>
.conatiner {
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
}
</style>