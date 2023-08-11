<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="content">
      <van-form @submit="onSubmit">
        <van-field
          v-model="content"
          name="内容"
          label="内容"
          placeholder="请输入评价信息"
          type="textarea"
          maxlength="50"
          show-word-limit
          :rules="[{ required: true, message: '请填写评价信息' }]"
        />
        <van-field name="rate" label="评分">
          <template #input>
            <van-rate v-model="score" />
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
import MyNavBar from '@/components/MyNavBar'
import commentApi from '@/api/comment'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      tcId: this.$route.query.id,
      title: this.$route.meta.title,
      content: '',
      score: 1,
    }
  },
  methods: {
    onSubmit() {
        let obj = {id: this.tcId, content: this.content, score: this.score}
        commentApi.createComment(obj).then((res) => {
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