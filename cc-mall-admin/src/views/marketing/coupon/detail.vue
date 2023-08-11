<template>
  <div class="container">
    <div class="content">
      <div class="header">
        <el-descriptions
          title="优惠券详情"
          direction="vertical"
          :column="6"
          border
          v-if="couponDetail"
        >
          <el-descriptions-item label="名称">{{
            couponDetail.name
          }}</el-descriptions-item>
          <el-descriptions-item label="优惠券类型">{{
            couponDetail.type
          }}</el-descriptions-item>
          <el-descriptions-item label="可使用商品">{{
            couponDetail.useType
          }}</el-descriptions-item>
          <el-descriptions-item label="使用门槛">{{
            couponDetail.minPoint
          }}</el-descriptions-item>
          <el-descriptions-item label="面值">{{
            couponDetail.amount
          }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{
            couponDetail.status
          }}</el-descriptions-item>
          <el-descriptions-item label="有效期">{{
            couponDetail.effectiveTime
          }}</el-descriptions-item>
          <el-descriptions-item label="总发行量">{{
            couponDetail.couponCount
          }}</el-descriptions-item>
          <el-descriptions-item label="已领取">{{
            couponDetail.receiveCount
          }}</el-descriptions-item>
          <el-descriptions-item label="待领取">{{
            couponDetail.unReceiveCount
          }}</el-descriptions-item>
          <el-descriptions-item label="已使用">{{
            couponDetail.useCount
          }}</el-descriptions-item>
          <el-descriptions-item label="未使用">{{
            couponDetail.unUseCount
          }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="nav-bar">
        <div class="nav-bar-header">
          <div class="title">
            <i class="el-icon-search"></i>
            <span style="margin-left: 3px">筛选搜索</span>
          </div>
          <div class="btn-group">
            <el-button @click="reset">重置</el-button>
            <el-button type="primary" @click="getCouponHistoryList"
              >查询搜索</el-button
            >
          </div>
        </div>
        <div class="search-query">
          <el-form ref="queryFormRef" :model="queryForm" label-width="100px">
            <el-form-item label="订单编号">
              <el-input v-model="queryForm.orderId"></el-input>
            </el-form-item>
            <el-form-item label="使用状态">
              <el-select v-model="queryForm.status" placeholder="请选择">
                <el-option
                  v-for="item in useStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="table-area">
        <el-table :data="couponHistoryList" border style="width: 100%">
          <el-table-column prop="username" label="领取会员" width="180">
          </el-table-column>
          <el-table-column prop="getType" label="领取方式" width="180">
          </el-table-column>
          <el-table-column prop="getTime" label="领取时间"></el-table-column>
          <el-table-column prop="curStatus" label="当前状态"></el-table-column>
          <el-table-column prop="useTime" label="使用时间"></el-table-column>
          <el-table-column prop="orderId" label="订单编号"></el-table-column>
        </el-table>
      </div>
      <div class="page-area">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryForm.pageNum"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="queryForm.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import couponApi from '@/api/marketing/coupon'
export default {
  data() {
    return {
      couponId: this.$route.query.couponId,
      useStatusOptions: [
        { label: '未使用', value: 0 },
        { label: '已使用', value: 1 },
        { label: '已过期', value: 2 },
      ],
      queryForm: {
        couponId: this.$route.query.couponId,
        pageNum: 1,
        pageSize: 10,
      },
      couponHistoryList: [],
      total: 0,
      couponDetail: {},
    }
  },
  created() {
    this.getCouponDetail()
    this.getCouponHistoryList()
  },
  methods: {
    getCouponDetail() {
      couponApi.getCouponDetail(this.couponId).then((res) => {
        this.couponDetail = res.data
      })
    },
    getCouponHistoryList() {
      couponApi.getCouponHistoryList(this.queryForm).then((res) => {
        this.couponHistoryList = res.data.list
        this.total = res.data.total
      })
    },
    reset() {
      this.queryForm = {
        couponId: this.couponId,
        pageNum: 1,
        pageSize: 10,
      }
      this.getCouponHistoryList()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getCouponHistoryList()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.getCouponHistoryList()
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  padding: 20px 0;
  display: flex;
  justify-content: center;
  .content {
    width: 1200px;
  }
  .nav-bar {
    margin-top: 10px;
    border: 1px solid #ddd;
    padding: 20px;
    .nav-bar-header {
      display: flex;
      justify-content: space-between;
    }
    .search-query {
      margin-top: 10px;
      display: flex;
      .el-form {
        display: flex;
      }
    }
  }
  .table-area {
    margin-top: 10px;
  }
  .page-area {
    margin-top: 10px;
    float: right;
  }
}
</style>