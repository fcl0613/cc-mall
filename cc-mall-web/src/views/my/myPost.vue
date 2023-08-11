<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="search-area">
      <van-search
        v-model="postTitle"
        @search="onSearch"
        placeholder="请输入搜索关键词"
      />
    </div>
    <div class="content">
      <div class="post-list-area">
        <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
          <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
          >
            <div class="post-list" v-if="postList.length > 0">
              <div
                class="card-area"
                v-for="(item, index) in postList"
                :key="index"
              >
                <div class="post-area">
                  <h3>{{ item.title }}</h3>
                  <div class="text-area">{{ item.content }}</div>
                  <div class="pic-area" v-if="item.pic">
                    <van-image
                      width="60"
                      height="60"
                      :src="baseUrl + item.pic"
                    />
                  </div>
                </div>
                <div class="operation-area">
                  <div class="edit-btn" @click="toEditPage(item.id)">编辑</div>
                  <div class="delete-btn" @click="deletePost(item.id)">
                    删除
                  </div>
                </div>
              </div>
            </div>
          </van-list>
        </van-pull-refresh>
      </div>
    </div>
    <div class="add-btn" @click="toAddPage">
      <van-icon name="add" color="green" size="60px" />
    </div>
  </div>
</template>

<script>
import MyNavBar from '@/components/MyNavBar'
import postApi from '@/api/post'
import imageBaseConfig from '@/utils/imageBaseConfig'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      title: this.$route.meta.title,
      postTitle: '',
      postList: [],
      total: 0,
      pageNum: 0,
      pageSize: 10,
      baseUrl: imageBaseConfig.IMAGE_BASE_URL,
      refreshing: false,
      loading: false,
      finished: false,
    }
  },
  created() {
    // this.getPostList()
  },
  methods: {
    getPostList() {
      let obj = {
        title: this.postTitle,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      }
      postApi.getMyPostList(obj).then((res) => {
        if (res.data.total === 0) {
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
    toEditPage(id) {
      this.$router.push({
        path: '/postEdit',
        query: {
          id: id,
        },
      })
    },
    onSearch() {
      this.pageNum = 1
      this.postList = []
      this.total = 0
      this.finished = false
      this.getPostList()
    },
    onRefresh() {
      this.title = ''
      this.pageNum = 1
      this.postList = []
      this.total = 0
      this.finished = false
      this.getPostList()
    },
    onLoad() {
      if (this.total < this.pageNum * this.pageSize) {
        // console.log(9999)
        return
      }
      this.pageNum = this.pageNum + 1
      this.getPostList()
    },
    deletePost(id) {
      this.$dialog
        .confirm({
          // title: '标题',
          message: '确定要删除该文章吗?',
        })
        .then(() => {
          // on confirm
          postApi.removePost(id).then((res) => {
            this.$toast.success(res.message)
            this.onRefresh()
          })
        })
        .catch(() => {
          // on cancel
        })
    },
    toAddPage() {
      this.$router.push({
        path: '/postAdd',
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
  .search-area {
    margin-top: 3px;
  }
  .content {
    padding: 10px 15px;
    background-color: #ececec;
    .post-list-area {
      .post-list {
        .card-area {
          background-color: #fff;
          border-radius: 10px;
          padding: 5px 10px;
          margin-bottom: 10px;
          .post-area {
            h3 {
              font-size: 20px;
              font-weight: bold;
            }
            .text-area {
              margin-top: 6px;
              font-size: 18px;
            }
            .pic-area {
              margin-top: 6px;
              .van-image {
                margin-right: 3px;
              }
            }
          }
          .operation-area {
            display: flex;
            justify-content: flex-end;
            font-size: 14px;
            .edit-btn {
              color: rgb(31, 29, 27);
            }
            .delete-btn {
              margin-left: 8px;
              color: rgb(245, 73, 21);
            }
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