<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="content">
      <div class="comment-list-area" v-if="commentList.length > 0">
        <div class="comment-list">
          <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
            <van-list
              v-model="loading"
              :finished="finished"
              finished-text="没有更多了"
              @load="onLoad"
            >
              <div class="comment-card" v-for="(item, index) in commentList" :key="index">
                <div class="card-header">
                  <div class="card-left">
                    <div class="avatar">
                      <van-image
                        round
                        width="40"
                        height="40"
                        fit="fit"
                        :src="item.avatar"
                      />
                    </div>
                    <div class="username">{{ item.username }}</div>
                  </div>
                  <div class="card-right">{{ item.createTime }}</div>
                </div>
                <div class="card-content">
                  <div class="score"><span>{{ item.score }}</span>分</div>
                  <div class="card-text">{{ item.content }}</div>
                </div>
              </div>
            </van-list>
          </van-pull-refresh>
        </div>
      </div>
      <van-empty v-else description="暂无评论" />
    </div>
  </div>
</template>

<script>
import MyNavBar from '@/components/MyNavBar'
import commentApi from '@/api/comment'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      productId: this.$route.query.productId,
      title: this.$route.meta.title,
      commentList: [],
      total: 0,
      loading: false,
      finished: false,
      refreshing: false,
      queryObj: {
        pageNum: 1,
        pageSize: 10,
      },
    }
  },
  created() {
    this.getCommentList()
  },
  methods: {
    onRefresh() {
      this.queryObj = {
        pageNum: 1,
        pageSize: 10,
      }
      this.commentList = []
      this.total = 0
      this.refreshing = false
      this.getCommentList()
    },
    onLoad() {
      this.queryObj.pageNum = this.queryObj.pageNum + 1
      this.getCommentList()
    },
    getCommentList() {
      this.queryObj.productId = this.productId
      commentApi.getCommentList(this.queryObj).then((res) => {
        this.commentList.push(...res.data.list)
        this.total = res.data.total
        if (this.commentList.length >= this.total) {
          this.finished = true
        }
        this.refreshing = false
        this.loading = false
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
    padding: 10px;
    background-color: #ececec;
    .comment-list-area {
      .comment-list {
        .comment-card {
          padding: 10px;
          background-color: #fff;
          margin-bottom: 10px;
          .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            .card-left {
              display: flex;
              align-items: center;
              .avatar {
                margin-right: 20px;
              }
              .username {
                font-size: 18px;
              }
            }
            .card-right {
              font-size: 18px;
            }
          }
          .card-content {
            margin-top: 10px;
            .score {
              font-size: 16px;
              font-weight: bold;
              color: rgb(207, 33, 33);
            }
            .card-text {
              margin-top: 10px;
              font-size: 18px;
              overflow: hidden;
            }
          }
        }
      }
    }
  }
}
</style>