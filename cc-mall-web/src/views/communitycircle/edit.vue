<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="content">
      <van-form>
        <van-field
          v-model="post.title"
          name="标题"
          label="标题"
          placeholder="标题"
          :rules="[{ required: true, message: '请填写标题' }]"
        />
        <van-field
          v-model="post.content"
          name="内容"
          label="内容"
          type="textarea"
          maxlength="500"
          show-word-limit
          placeholder="内容"
          :rules="[{ required: true, message: '请输入内容' }]"
        />
        <van-field name="uploader" label="图片上传">
          <template #input>
            <van-uploader
              v-model="uploader"
              :after-read="afterRead"
              @delete="deleteFile"
              :max-count="3"
            />
          </template>
        </van-field>
      </van-form>
    </div>
    <div class="botton-area">
      <van-button type="info" @click="onSubmit">提交</van-button>
    </div>
  </div>
</template>

<script>
import MyNavBar from '@/components/MyNavBar'
import aliyunOSSApi from '@/api/aliyunOSS'
import axios from 'axios'
import baseImageUrl from '@/utils/imageBaseConfig'
import postApi from '@/api/post'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      title: this.$route.meta.title,
      id: this.$route.query.id,
      post: {},
      uploader: [],
      uploaderIndex: 0,
      aliyunOssInfo: {},
      baseUrl: baseImageUrl.IMAGE_BASE_URL,
      baseUrlPre: baseImageUrl.IMAGE_BASE_URL_PRE,
    }
  },
  created() {
    this.getAliyunOssPolicy()
    this.getPistInfo()
  },
  methods: {
    afterRead(e) {
      this.uploader.pop()
      let file = e.file
      let fileName = new Date().getTime() + file.name.substr(-6)
      let accessUrl = this.aliyunOssInfo.dir + '/' + fileName
      let sendData = new FormData()
      console.log(accessUrl)
      sendData.append('key', accessUrl)
      sendData.append('ossaccessKeyId', this.aliyunOssInfo.accessKeyId)
      sendData.append('policy', this.aliyunOssInfo.policy)
      sendData.append('signature', this.aliyunOssInfo.signature)
      sendData.append('success_action_status', 200)
      sendData.append('Content-Type', 'image/png')
      sendData.append('file', file)
      axios.post(this.aliyunOssInfo.host, sendData).then((res) => {
        if (res.status === 200) {
          this.$toast('图片上传成功')
          this.uploader.push({
            id: this.uploaderIndex,
            url: this.baseUrlPre + accessUrl,
            fileName: accessUrl.split('/')[2] + '/' + fileName,
          })
          this.uploaderIndex = this.uploaderIndex + 1
        } else {
          this.$toast('图片上传失败')
        }
      })
    },
    deleteFile(e) {
      console.log(e)
    },
    // 获取阿里云OSS签名
    getAliyunOssPolicy() {
      aliyunOSSApi.getAliyunOssPolicy().then((res) => {
        this.aliyunOssInfo = res.data
      })
    },
    onSubmit() {
      if (this.post.title === '') {
        this.$toast('文章标题不能为空')
        return
      }
      if (this.post.content === '') {
        this.$toast('文章内容不能为空')
        return
      }
      let pics = []
      for (const item of this.uploader) {
        pics.push(item.fileName)
      }
      this.post.pics = pics
      postApi.updatePost(this.post).then((res) => {
        this.$toast(res.message)
        this.$router.go(-1)
      })
    },
    getPistInfo() {
      postApi.getPostDetail(this.id).then((res) => {
        this.post = res.data
        if (res.data.pics && res.data.pics.length > 0) {
          for (const item of res.data.pics) {
            this.uploader.push({
              id: this.uploaderIndex,
              url: this.baseUrl + item,
              fileName: item,
            })
            this.uploaderIndex = this.uploaderIndex + 1
          }
        }
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
  .botton-area {
    position: fixed;
    bottom: 0;
    width: 100%;
    height: 50px;
    z-index: 999;
    .van-button {
      width: 100%;
    }
  }
}
</style>