<template>
  <div class="container">
    <van-nav-bar title="个人资料" left-arrow @click-left="onClickLeft" />
    <h1>个人资料</h1>
    <div class="info-area">
      <van-cell-group>
        <van-cell class="avatar" title="头像" is-link>
          <van-uploader :after-read="uploadAvatar">
            <van-image
              width="40"
              height="40"
              round
              :src="customerInfo.avatar"
            />
          </van-uploader>
        </van-cell>
        <van-cell
          title="昵称"
          :value="customerInfo.nickName"
          @click="goToEditNickNamePage"
          is-link
        />
        <van-cell
          title="性别"
          :value="customerInfo.gender"
          @click="showGenderPop"
          is-link
        />
      </van-cell-group>
    </div>
    <van-action-sheet
      v-model="show"
      :actions="genderActions"
      @select="onSelect"
    />

    <van-button type="primary" block @click="logout">退出登录</van-button>
  </div>
</template>

<script>
import aliyunOSSApi from '@/api/aliyunOSS/index'
import customerApi from '@/api/customer'
import axios from 'axios'
import baseImageUrl from '@/utils/imageBaseConfig'
import { removeToken } from '@/utils/token'
export default {
  data() {
    return {
      customerInfo: {},
      show: false,
      genderActions: [
        { name: '男', flag: 0 },
        { name: '女', flag: 1 },
        { name: '未知', flag: 2 },
      ],
      aliyunOssInfo: {},
      baseImageUrlPre: baseImageUrl.IMAGE_BASE_URL_PRE,
    }
  },
  created() {
    this.getInfo()
    this.getAliyunOssPolicy()
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1)
    },
    getInfo() {
      customerApi.getInfo().then((res) => {
        this.customerInfo = res.data
      })
    },
    showGenderPop() {
      this.show = !this.show
    },
    onSelect(item) {
      // console.log(item)
      customerApi.updateGender(item.flag).then((res) => {
        this.customerInfo.gender = item.name
        this.show = !this.show
      })
    },
    goToEditNickNamePage() {
      this.$router.push({
        path: '/editNickName',
        query: {
          nickName: this.customerInfo.nickName,
        },
      })
    },
    uploadAvatar(e) {
      // console.log(e)
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
          customerApi
            .updateAvatar(this.baseImageUrlPre + accessUrl)
            .then((res) => {
              this.$toast('图片上传成功')
              this.customerInfo.avatar = this.baseImageUrlPre + accessUrl
            })
        } else {
          this.$toast('图片上传失败')
        }
      })
    },
    // 获取阿里云OSS签名
    getAliyunOssPolicy() {
      aliyunOSSApi.getAliyunOssPolicy().then((res) => {
        this.aliyunOssInfo = res.data
      })
    },
    logout() {
      removeToken()
      this.$router.push({
        path: '/login'
      })
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  h1 {
    margin: 30px 0 0 30px;
  }
  .info-area {
    margin-top: 20px;
    .avatar {
      display: flex;
      align-items: center;
    }
  }
}
</style>