<template>
  <div class="container">
    <div class="left-sidebar">
      <van-sidebar v-model="activeKey" @change="onChange">
        <van-sidebar-item v-for="(item, index) in categoryList" :key="index" :title="item.name" />
      </van-sidebar>
    </div>
    <div class="right-icon">
      <van-grid :border="false" :column-num="3">
        <van-grid-item v-for="(item, index) in iconList" :key="index" @click="goToProductPage(item.id)">
          <van-image width="60" height="60" :src="item.icon" fit="cover" />
          <span class="name">{{ item.name }}</span>
        </van-grid-item>
      </van-grid>
    </div>
  </div>
</template>

<script>
import categoryApi from '@/api/category'
export default {
  data() {
    return {
      activeKey: 0,
      categoryList: [],
      iconList: []
    }
  },
  created() {
    this.getAll()
  },
  methods: {
    onChange(index) {
      // console.log(index)
      this.iconList = this.categoryList[index].list
    },
    getAll() {
      categoryApi.getAll().then((res) => {
        this.categoryList = res.data
        this.iconList = res.data[0].list
      })
    },
    goToProductPage(id) {
      this.$router.push({
        path: '/product',
        query: {
          categoryId: id,
        },
      })
    }
  },
}
</script>

<style lang="less" scoped>
.container {
  display: flex;
  justify-content: space-between;
  .left-sidebar {
    display: inline;
    width: 25%;
    height: calc(100vh - 100px);
    overflow: scroll;
  }
  .right-icon {
    display: inline;
    // background-color: yellow;
    width: 75%;
    // height: calc(100vh - 108px);
    // overflow: scroll;
    margin-top: 10px;
    margin-left: 10px;
    margin-right: 10px;
    .name {
      margin-top: 6px;
      font-size: 14px;
      color: rgb(20, 18, 18);
    }
  }
}
</style>
