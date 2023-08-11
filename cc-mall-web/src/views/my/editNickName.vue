<template>
  <div class="container">
    <van-nav-bar title="编辑昵称" left-arrow @click-left="onClickLeft" />
    <div class="form-area">
      <van-form @submit="onSubmit">
        <van-field
          v-model="nickName"
          name="用户昵称"
          label="用户昵称"
          placeholder="用户昵称"
          :rules="[{ required: true, message: '请填写用户昵称' }]"
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
import customerApi from '@/api/customer'
export default {
  data() {
    return {
      nickName: this.$route.query.nickName,
    }
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1)
    },
    onSubmit() {
        customerApi.updateNickName(this.nickName).then((res) => {
            this.$router.go(-1)
        })
    }
  },
}
</script>

<style lang="less" scoped>
.container {
    .form-area {
        margin-top: 60px;
    }
}
</style>