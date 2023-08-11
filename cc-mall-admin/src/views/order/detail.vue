<template>
  <div class="container">
    <el-page-header @back="goBack" content="详情页面"> </el-page-header>
    <el-card shadow="never">
      <div class="option-area">
        <span>当前订单状态：{{ orderinfo.orderStatusStr }}</span>
        <div class="option-btn-area">
          <!-- <el-button size="mini">备注订单</el-button> -->
        </div>
      </div>
      <el-descriptions title="基本信息" direction="vertical" :column="4" border>
        <el-descriptions-item label="订单编号">{{
          orderinfo.orderSn
        }}</el-descriptions-item>
        <el-descriptions-item label="用户账号">{{
          orderinfo.userAccount
        }}</el-descriptions-item>
        <el-descriptions-item label="订单类型">{{
          orderinfo.orderType
        }}</el-descriptions-item>
        <el-descriptions-item label="获取积分">{{
          orderinfo.points
        }}</el-descriptions-item>
        <el-descriptions-item label="收货人">{{
          orderinfo.deliveryName
        }}</el-descriptions-item>
        <el-descriptions-item label="手机号码">{{
          orderinfo.deliveryPhone
        }}</el-descriptions-item>
        <el-descriptions-item label="收货地址">
          <span>{{ orderinfo.deliveryProvince }}</span>
          <span>{{ orderinfo.deliveryCity }}</span>
          <span>{{ orderinfo.deliveryCounty }}</span>
          <span>{{ orderinfo.deliveryAddress }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="优惠券扣减">{{
          orderinfo.couponAmount
        }}</el-descriptions-item>
      </el-descriptions>
      <div class="product-area">
        <el-table :data="productList" border style="width: 100%">
          <el-table-column label="商品图片" width="180">
            <template slot-scope="scope">
              <el-image
                style="width: 100px; height: 100px"
                :src="baseUrl + scope.row.productCover"
                fit="fit"
              ></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="productName" label="商品名" width="180">
          </el-table-column>
          <el-table-column prop="productPrice" label="商品价格">
          </el-table-column>
          <el-table-column prop="productCount" label="商品数量">
          </el-table-column>
          <el-table-column prop="subtotal" label="商品小计"> </el-table-column>
        </el-table>
        <div class="total-price">
          <span>合计：{{ orderinfo.totalPrice }}</span>
          <span>应付：{{ orderinfo.payAmount }}</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import orderApi from '@/api/order'
import imageBaseConfig from '@/utils/imageBaseConfig'
export default {
  data() {
    return {
      productList: [],
      id: this.$route.query.id,
      orderinfo: {},
      baseUrl: imageBaseConfig.IMAGE_BASE_URL,
    }
  },
  created() {
    this.getOrderDetail()
  },
  methods: {
    getOrderDetail() {
      orderApi.getOrderDetail(this.id).then((res) => {
        this.orderinfo = res.data
        this.productList = res.data.list
      })
    },
    goBack() {
      this.$router.go(-1)
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  width: 80%;
  margin: 20px auto;
  .el-page-header {
    margin-bottom: 20px;
  }
  .option-area {
    margin-bottom: 10px;
    display: flex;
    justify-content: space-between;
  }
  .product-area {
    margin-top: 20px;
    .total-price {
      margin-top: 10px;
      display: flex;
      justify-content: flex-end;
    }
  }
}
</style>