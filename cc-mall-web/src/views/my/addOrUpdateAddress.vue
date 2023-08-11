<template>
  <div class="container">
    <div class="nav-bar">
      <MyNavBar :title="title"></MyNavBar>
    </div>
    <van-address-edit
      :area-list="areaList"
      :address-info="addressInfo"
      show-postal
      show-delete
      show-set-default
      show-search-result
      :search-result="searchResult"
      :area-columns-placeholder="['请选择', '请选择', '请选择']"
      @save="onSave"
      @delete="onDelete"
      @change-detail="onChangeDetail"
    />
  </div>
</template>

<script>
import { areaList } from '@vant/area-data'
import MyNavBar from '@/components/MyNavBar'
import customerApi from '@/api/customer'
export default {
  components: {
    MyNavBar,
  },
  data() {
    return {
      title: this.$route.meta.title,
      flag: this.$route.query.flag,
      id: this.$route.query.id,
      areaList,
      searchResult: [],
      addressInfo: {},
    }
  },
  created() {
    if (this.id) {
      this.getCurrentAddress()
    }
  },
  methods: {
    onSave(val) {
      console.log(val)
      let obj = {
        province: val.province,
        city: val.city,
        county: val.county,
        fullAddress: val.addressDetail,
        defaultFlag: val.isDefault ? 0 : 1,
        deliveryName: val.name,
        phone: val.tel,
        areaCode: val.areaCode,
      }
      customerApi.addAddress(obj).then((res) => {
        this.$toast(res.message)
        this.$router.go(-1)
      })
    },
    onDelete() {
      if (!this.flag) {
        return
      }
      customerApi.removeAddress(this.id).then((res) => {
        this.$toast(res.message)
        this.$router.go(-1)
      })
    },
    onChangeDetail(val) {
      //   console.log(val)
    },
    getCurrentAddress() {
      customerApi.getAddressInfo(this.id).then((res) => {
        this.addressInfo = res.data
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
}
</style>