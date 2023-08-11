<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="content">
      <div class="product-list-area">
        <div class="product-list">
          <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
            <van-list
              v-model="loading"
              :finished="finished"
              finished-text="没有更多了"
              @load="onLoad"
            >
              <van-card
                v-for="(item, index) in goodsList"
                :key="index"
                :num="item.productCount"
                :price="item.productPrice"
                :title="item.productName"
                :thumb="baseUrl + item.productCover"
              >
                <template #footer>
                  <van-button size="mini" @click="toComment(item.id)">去评价</van-button>
                </template>
              </van-card>
            </van-list>
          </van-pull-refresh>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MyNavBar from '@/components/MyNavBar'
import imageBaseConfig from '@/utils/imageBaseConfig'
import commentApi from '@/api/comment'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      baseUrl: imageBaseConfig.IMAGE_BASE_URL,
      title: this.$route.meta.title,
      refreshing: false,
      loading: false,
      finished: false,
      pageSize: 20,
      pageNum: 0,
      goodsList: [],
      total: 0,
    }
  },
  created() {},
  methods: {
    onLoad() {
      this.pageNum = this.pageNum + 1
      this.getGoodsList()
    },
    onRefresh() {
      this.goodsList = []
      this.total = 0
      this.pageNum = 1
      this.getGoodsList()
    },
    getGoodsList() {
      commentApi.getToBeCommentList(this.pageNum, this.pageSize).then((res) => {
        this.goodsList.push(...res.data.list)
        this.total = res.data.total
        if (this.total <= this.goodsList.length) {
          this.finished = true
        }
        this.loading = false
        this.refreshing = false
      })
    },
    toComment(id) {
      this.$router.push({
        path: '/editComment',
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
    margin-top: 10px;
    padding: 0 10px 20px 10px;
    background-color: #ececec;
  }
}
</style>