<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <div class="title">
      <h1>管理我的地址</h1>
    </div>
    <van-empty v-if="list.length === 0" description="暂无地址信息" />
    <!-- <div class="address-list">
      <div class="address-card">
        <div class="up">
          <div class="name">达瓦达瓦</div>
          <div class="phone">54545454545</div>
        </div>
        <div class="down">
          <div class="address">
            等哈我的风好大爱到底到达到我的的骄傲i大的骄傲嗲大家都发会否俄双方否发
          </div>
          <div class="edit-icon"><van-icon name="edit" /></div>
        </div>
      </div>
    </div> -->
    <van-address-list
      v-if="list.length > 0"
      v-model="chosenAddressId"
      :list="list"
      default-tag-text="默认"
      @add="onAdd"
      @edit="onEdit"
    />
  </div>
</template>

<script>
import MyNavBar from '@/components/MyNavBar'
import customerApi from '@/api/customer'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      title: this.$route.meta.title,
      chosenAddressId: 1,
      list: [],
    }
  },
  created() {
    this.getAddressList()
  },
  methods: {
    onAdd() {
      this.$router.push({
        path: '/addAddress',
        query: {
          flag: true,
        },
      })
    },
    onEdit(val) {
      console.log(val)
      this.$router.push({
        path: '/addAddress',
        query: {
          flag: false,
          id: val.id,
        },
      })
    },
    getAddressList() {
      customerApi.getAddressList().then((res) => {
        this.list = res.data
        if (this.list.length > 0) {
          this.chosenAddressId = this.list[0].id
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
  .title {
    margin-top: 20px;
    margin-left: 20px;
  }
  //   .van-address-list {
  //     position: absolute;
  //     padding-bottom: 0;
  //     height: 100%;
  //     background-color: #ececec;
  //   }
  .address-list {
    margin-top: 20px;
    // padding: 0 20px;
    .address-card {
      background-color: #fff;
      padding: 20px;
      // border: 1px solid #949494;
      // border-radius: 5px;
      .up {
        display: flex;
        justify-content: baseline;
        //   align-items: center;
        .name {
          font-size: 18px;
        }
        .phone {
          margin-left: 10px;
          font-size: 16px;
          color: #949494;
          line-height: 30px;
        }
      }
      .down {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .address {
          width: 80%;
          font-size: 16px;
        }
      }
    }
  }
}
</style>