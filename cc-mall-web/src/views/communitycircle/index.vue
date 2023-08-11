<template>
  <div class="container">
    <div class="search-area">
      <van-search v-model="title" placeholder="请输入搜索关键词" @search="onSearch" />
    </div>
    <div class="post-list-area">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
        >
          <div class="post-list" v-if="postList.length > 0">
            <div class="card-area" v-for="(item, index) in postList" :key="index">
              <div class="card" @click="toInfoPage(item.id)">
                <div class="card-header">
                  <div class="avatar">
                    <van-image
                      round
                      width="40px"
                      height="40px"
                      :src="item.avatar"
                    />
                  </div>
                  <div class="username">{{ item.username }}</div>
                </div>
                <div class="card-content">
                  <div class="text-area">
                    <h4>{{ item.title }}</h4>
                    <div class="text">
                      {{ item.content }}
                    </div>
                  </div>
                  <div class="pic-area" v-if="item.pic">
                    <van-image
                      width="60px"
                      height="60px"
                      :src="baseUrl + item.pic"
                    />
                  </div>
                </div>
                <div class="card-bottom">
                  <div class="bottom-left">
                    <van-icon name="eye-o" size="20" /> {{ item.pageview }}
                  </div>
                  <div class="bottom-mid">
                    <van-icon name="chat-o" size="20" /> {{ item.commentCount }}
                  </div>
                  <div class="bottom-right">
                    <van-icon name="like-o" size="20" /> {{ item.likeCount }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
    <div class="add-btn" @click="toAddPage">
      <van-icon name="add" color="green" size="60px"/>
    </div>
  </div>
</template>

<script>
import postApi from '@/api/post'
import imageBaseConfig from '@/utils/imageBaseConfig'
export default {
  data() {
    return {
      title: '',
      pageNum: 0,
      pageSize: 10,
      postList: [],
      total: 0,
      refreshing: false,
      loading: false,
      finished: false,
      baseUrl: imageBaseConfig.IMAGE_BASE_URL
    }
  },
  created() {
    // this.onRefresh()
  },
  methods: {
    getHomePostList() {
      let obj = {
        title: this.title,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      }
      postApi.getHomePostList(obj).then((res) => {
        if(res.data.total === 0) {
          this.finished = true
          return
        }
        this.postList.push(...res.data.list)
        this.total = res.data.total
        if (this.total <= this.postList.length) {
          console.log(22)
          this.finished = true
        }
        this.loading = false
        this.refreshing = false
      })
    },
    onRefresh() {
      this.title = ''
      this.pageNum = 1
      this.postList = []
      this.total = 0
      this.finished = false
      this.getHomePostList()
    },
    onLoad() {
      if(this.total < this.pageNum * this.pageSize) {
        console.log(9999)
        return
      }
      this.pageNum = this.pageNum + 1
      this.getHomePostList()
    },
    onSearch() {
      this.pageNum = 1
      this.postList = []
      this.total = 0
      this.finished = false
      this.getHomePostList()
    },
    toInfoPage(id) {
      this.$router.push({
        path: '/postInfo',
        query: {
          id: id
        }
      })
    },
    toAddPage() {
      this.$router.push({
        path: '/postAdd'
      })
    }
  },
}
</script>

<style lang="less" scoped>
.container {
  .search-area {
    width: 100%;
    height: 54px;
    .van-search {
      width: 100%;
      position: fixed;
      z-index: 999;
    }
  }
  .post-list-area {
    padding: 10px 15px;
    background-color: #f7f7f7;
    .post-list {
      .card-area {
        margin-bottom: 10px;
        .card {
          padding: 10px;
          background-color: #fff;
          border-radius: 10px;
          .card-header {
            display: flex;
            align-items: center;
            .avatar {
              margin-right: 20px;
            }
          }
          .card-content {
            .text-area {
              h4 {
                font-size: 20px;
                font-weight: bold;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
              }
              .text {
                margin-top: 6px;
                font-size: 16px;
              }
            }
            .pic-area {
              margin-top: 10px;
            }
          }
          .card-bottom {
            margin-top: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 16px;
          }
        }
      }
    }
  }
  .add-btn {
    position: fixed;
    bottom: 80px;
    right: 20px;
  }
}
</style>
