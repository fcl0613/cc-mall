<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="content">
      <div class="title">{{ postInfo.title }}</div>
      <div class="text-area">{{ postInfo.content }}</div>
      <div class="pic-area" v-if="postInfo.pics">
        <van-image
          width="100"
          height="100"
          v-for="(item, index) in postInfo.pics"
          :key="index"
          :src="baseUrl + item"
        />
      </div>
      <div class="banner-area">
        <van-icon
          @click="likeOperation(postInfo.likeStatus)"
          name="good-job"
          :color="postInfo.likeStatus ? 'red' : ''"
        />
      </div>
    </div>
    <div class="comment-area">
      <h4>评论</h4>
      <div class="comment-list-area" v-if="commentList.length > 0">
        <div class="comment-list">
          <div class="card-area">
            <div class="card" v-for="(item, index) in commentList" :key="index">
              <div class="card-header">
                <div class="avatar">
                  <van-image
                    round
                    width="30px"
                    height="30px"
                    :src="item.avatar"
                  />
                </div>
                <div class="username">{{ item.username }}</div>
              </div>
              <div class="card-content">
                {{ item.content }}
              </div>
              <div class="card-bottom">{{ item.createTime }}</div>
              <van-divider />
            </div>
          </div>
        </div>
        <van-pagination
          v-if="total > 10"
          v-model="pageNum"
          :total-items="total"
          :items-per-page="pageSize"
        />
      </div>
      <div v-else>
        <van-empty description="暂无评论内容" />
      </div>
    </div>
    <div class="input-area">
      <van-field v-model="commentContent" placeholder="请输入评论内容" />
      <van-button type="primary" @click="subComment">提交</van-button>
    </div>
  </div>
</template>

<script>
import MyNavBar from '@/components/MyNavBar'
import postAPi from '@/api/post'
import imageBaseConfig from '@/utils/imageBaseConfig'
import postCommentApi from '@/api/post/comment'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      title: this.$route.meta.title,
      id: this.$route.query.id,
      postInfo: {},
      pageNum: 1,
      pageSize: 10,
      total: 0,
      baseUrl: imageBaseConfig.IMAGE_BASE_URL,
      commentContent: '',
      commentList: [],
    }
  },
  created() {
    this.getPostInfo()
    this.getCommentList()
  },
  methods: {
    getPostInfo() {
      postAPi.getPostInfo(this.id).then((res) => {
        this.postInfo = res.data
      })
    },
    likeOperation(val) {
      if (val) {
        // 取消点赞
        postAPi.postUnLike(this.id).then((res) => {
          this.postInfo.likeStatus = !this.postInfo.likeStatus
        })
      } else {
        // 点赞
        postAPi.postLike(this.id).then((res) => {
          this.postInfo.likeStatus = !this.postInfo.likeStatus
        })
      }
    },
    subComment() {
      let obj = { postId: this.id, content: this.commentContent }
      postCommentApi.createComment(obj).then((res) => {
        this.$toast('评论成功')
        this.commentContent = ''
        this.pageNum = 1
        this.getCommentList()
      })
    },
    getCommentList() {
      let obj = {
        postId: this.id,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      }
      postCommentApi.getComment(obj).then((res) => {
        this.commentList = res.data.list
        this.total = res.data.total
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
    margin-top: 3px;
    padding: 10px;
    background-color: #fff;
    .title {
      font-size: 18px;
      font-weight: bold;
    }
    .text-area {
      margin-top: 10px;
      font-size: 16px;
    }
    .pic-area {
      margin-top: 10px;
      .van-image {
        margin-right: 10px;
      }
    }
    .banner-area {
      display: flex;
      justify-content: flex-end;
    }
  }
  .comment-area {
    margin-top: 3px;
    padding: 3px 10px 50px 10px;
    background-color: #fff;
    h4 {
      font-size: 20px;
    }
    .comment-list-area {
      margin-top: 6px;
      .comment-list {
        .card-area {
          .card {
            .card-header {
              display: flex;
              align-items: center;
              font-size: 16px;
              font-weight: bold;
              .avatar {
                margin-right: 8px;
              }
            }
            .card-content {
              font-size: 16px;
            }
            .card-bottom {
              margin-top: 10px;
              font-size: 14px;
            }
          }
        }
      }
    }
  }
  .input-area {
    position: fixed;
    bottom: 0;
    background-color: #fff;
    width: 100%;
    display: flex;
    z-index: 999;
    .van-field {
      width: 84%;
    }
  }
}
</style>