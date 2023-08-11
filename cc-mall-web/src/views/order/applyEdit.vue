<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="content">
      <van-form @submit="onSubmit">
        <van-field
          v-model="reason"
          name="内容"
          label="内容"
          placeholder="请输入退款原因"
          type="textarea"
          maxlength="50"
          show-word-limit
          :rules="[{ required: true, message: '请输入退款原因' }]"
        />
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
import MyNavBar from '@/components/MyNavBar'
import orderApplyApi from '@/api/order/orderApply'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      title: this.$route.meta.title,
      id: this.$route.query.id,
      reason: '',
    }
  },
  methods: {
    onSubmit() {
      let obj = { id: this.id, reason: this.reason }
      orderApplyApi.createApply(obj).then((res) => {
        this.$toast(res.message)
        this.$router.go(-1)
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
  }
}
</style>