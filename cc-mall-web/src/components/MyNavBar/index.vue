<template>
  <div class="container">
    <van-nav-bar
      :title="title"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
    />
  </div>
</template>

<script>
export default {
  name: 'MyNavBar',
  props: ['title'],
  data() {
    return {}
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1)
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  background-color: #fff;
  .van-nav-bar {
    position: fixed;
    width: 100%;
    top: 0;
    z-index: 999;
  }
}
</style>