<template>
  <div>
    <van-nav-bar :title="activeTitle" fixed />
    <div class="main">
      <router-view></router-view>
    </div>
    <van-tabbar route>
      <van-tabbar-item replace to="/layout/home" icon="home-o"
        >精选</van-tabbar-item
      >
      <van-tabbar-item replace to="/layout/categoary" icon="qr"
        >分类</van-tabbar-item
      >
      <van-tabbar-item replace to="/layout/discuss" icon="smile-comment-o"
        >圈子</van-tabbar-item
      >
      <van-tabbar-item replace to="/layout/shoppingCart" icon="shopping-cart-o"
        >购物车</van-tabbar-item
      >
      <van-tabbar-item replace to="/layout/my" icon="user-o"
        >我的</van-tabbar-item
      >
    </van-tabbar>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeTitle: this.$route.meta.title
    }
  },
  watch: {
    $route() {
      this.activeTitle = this.$route.meta.title
    }
  }
}
</script>

<style lang="less" scoped>
.main {
  padding-top: 46px;
  padding-bottom: 50px;
}
</style>
