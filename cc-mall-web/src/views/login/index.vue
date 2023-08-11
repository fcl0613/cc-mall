<template>
  <div class="container">
    <div class="image-area">
      <van-image
        round
        fit="cover"
        width="100"
        height="100"
        src="https://img01.yzcdn.cn/vant/cat.jpeg"
      ></van-image>
    </div>
    <div class="login-area">
      <van-form @submit="onSubmit" ref="loginFormRef">
        <van-field
          v-model="formObj.loginAccount"
          name="用户名"
          label="用户名"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
          v-model="formObj.password"
          type="password"
          name="密码"
          label="密码"
          placeholder="密码"
          :rules="[{ required: true, message: '请填写密码' }]"
        />
        <div style="margin: 16px">
          <van-button round block type="info" native-type="submit"
            >提交</van-button
          >
        </div>
      </van-form>
    </div>
    <div class="tips" @click="toRegisterPage">
      没有账号？立即注册
    </div>
  </div>
</template>

<script>
import customerApi from '@/api/customer'
import { setToken } from '@/utils/token'
export default {
  data() {
    return {
      formObj: {}
    }
  },
  methods: {
    onSubmit() {
      this.$refs.loginFormRef.validate().then(() => {
        console.log(2233)
        customerApi.login(this.formObj).then((res) => {
          setToken(res.data.token)
          this.$toast('登录成功')
          this.$router.push({
            path: '/layout/home'
          })
        })
      }).catch(() => {
      })
    },
    toRegisterPage() {
      this.$router.push({
        path: '/register'
      })
    }
  }
}
</script>

<style lang="less" scoped>
.container {
  padding: 50px 20px;
  height: 100%;
  .image-area {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
  .login-area {
    margin-top: 20px;
  }
  .tips {
    font-size: 12px;
    height: 20px;
    line-height: 20px;
    text-align: center;
    
  }
}
</style>