<template>
  <div class="container">
    <div class="header">
      <van-nav-bar title="注册" left-arrow @click-left="onClickLeft" />
    </div>
    <div class="form-data">
      <van-form @submit="onSubmit">
        <van-field
          v-model="formData.phone"
          name="手机号"
          label="手机号"
          placeholder="手机号"
          :rules="[
            { required: true, message: '请填写手机号' },
            { validator: checkPhone, message: '请填写正确的手机号' },
          ]"
        />
        <van-field
          v-model="formData.password"
          type="password"
          name="密码"
          label="密码"
          placeholder="密码"
          :rules="[
            { required: true, message: '请填写密码' },
            {
              validator: checkPassword,
              message: '至少包含字母，数字，特殊符号中任意3项',
            },
          ]"
        />
        <van-field
          v-model="formData.captcha"
          label="短信验证码"
          placeholder="请输入短信验证码"
          :rules="[{ required: true, message: '请填写验证码' }]"
        >
          <template #button>
            <van-button
              size="small"
              type="primary"
              @click="sendMessage"
              native-type="button"
              :disabled="isSmsSend"
              >{{ sendBtnText }}</van-button
            >
          </template>
        </van-field>
        <div style="margin: 16px">
          <van-button round block type="info" native-type="submit"
            >提交</van-button
          >
        </div>
      </van-form>
    </div>
  </div>
</template>

<script>
import customerApi from '@/api/customer'
export default {
  data() {
    return {
      formData: {},
      isSmsSend: false,
      sendBtnText: '点击发送验证码',
      // 计时器对象
      timer: null,
      // 倒数60秒
      counter: 60
    }
  },
  methods: {
    onSubmit(values) {
      // console.log('submit', values)
      customerApi.regist(this.formData).then((res) => {
        this.$toast.success('注册成功')
        this.$router.push({
          path: '/login'
        })
      })
    },
    checkPhone(val) {
      return /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[189]))\d{8}$/.test(
        val
      )
    },
    checkPassword(val) {
      return /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\W_!@#$%^&*`~()-+=]+$)(?![0-9\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\W_!@#$%^&*`~()-+=]/.test(
        val
      )
    },
    sendMessage() {
      if (!this.formData.phone) {
        this.$toast.fail('请输入手机号')
        return
      }
      let obj = { phone: this.formData.phone }
      customerApi.sendMessage(obj).then((res) => {
        this.$toast.success(res.message)
        this.isSmsSend = true
        this.countDown()
      })
    },
    onClickLeft() {
      this.$router.go(-1)
    },
    countDown () {
        // 将setInterval()方法赋值给前面定义的timer计时器对象，等用clearInterval()方法时方便清空这个计时器对象
        this.timer = setInterval(() => {
          // 替换文本，用es6里面的``这个来创建字符串模板，让秒实时改变
          this.sendBtnText = `(${this.counter}秒)后重新发送`
          this.counter--
          if (this.counter < 0) {
            // 当计时小于零时，取消该计时器
            clearInterval(this.timer)
          }
        }, 1000)
      }
  },
}
</script>

<style lang="less" scoped>
.container {
  // padding: 20px;
  .form-data {
    padding: 20px;
    // margin-top: 10px;
  }
}
</style>