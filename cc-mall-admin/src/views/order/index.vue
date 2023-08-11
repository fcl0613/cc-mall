<template>
  <div class="container">
    <div class="header">
      <div class="search-area">
        <div class="top">
          <div class="title">
            <i class="el-icon-search"></i>
            <span>搜索筛选</span>
          </div>
          <div class="btn-group">
            <el-button @click="resetSearchFrom">重置</el-button>
            <el-button type="primary" plain @click="getOrderList"
              >搜索</el-button
            >
          </div>
        </div>
        <div class="search-condition">
          <div class="form-area">
            <el-form ref="form" :model="searchForm" label-width="80px">
              <el-form-item label="订单号">
                <el-input
                  v-model="searchForm.orderId"
                  placeholder="订单号"
                ></el-input>
              </el-form-item>
              <el-form-item label="订单状态">
                <el-select v-model="searchForm.orderStatus" placeholder="全部">
                  <el-option
                    v-for="(item, index) in orderStatusOption"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="订单号">
                <el-date-picker
                  v-model="dateValus"
                  type="daterange"
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                >
                </el-date-picker>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>

      <div class="banner">
        <div class="left">
          <i class="el-icon-tickets"></i>
          <h3>数据列表</h3>
        </div>
        <div class="right">
          <el-button type="primary" @click="shiomentsBatch">批量发货</el-button>
        </div>
      </div>
    </div>
    <div class="data-area">
      <el-card>
        <el-table
          :data="orderList"
          @selection-change="handleSelectionChange"
          border
          style="width: 100%"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column
            label="序号"
            type="index"
            width="70"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="orderId"
            label="订单编号"
            width="178"
          ></el-table-column>
          <el-table-column
            prop="orderTime"
            label="下单时间"
            width="157"
          ></el-table-column>
          <el-table-column
            prop="customerUsername"
            label="用户账号"
            width="88"
          ></el-table-column>
          <el-table-column label="订单类型" width="88">
            <template slot-scope="scope">
              <div v-if="scope.row.orderType === 0">配送</div>
              <div v-if="scope.row.orderType === 1">自提</div>
            </template>
          </el-table-column>
          <el-table-column label="订单状态" width="100">
            <template slot-scope="scope">
              <div v-if="scope.row.orderStatus === 0">待付款</div>
              <div v-if="scope.row.orderStatus === 1">待发货</div>
              <div v-if="scope.row.orderStatus === 2">已发货</div>
              <div v-if="scope.row.orderStatus === 3">已完成</div>
              <div v-if="scope.row.orderStatus === 4">已关闭</div>
              <div v-if="scope.row.orderStatus === 5">无效订单</div>
            </template>
          </el-table-column>
          <el-table-column
            prop="totalAmount"
            label="订单总额"
            width="88"
          ></el-table-column>
          <el-table-column
            prop="payAmount"
            label="实付金额"
            width="88"
          ></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button size="small" plain @click="toDetailPage(scope.row.id)"
                >查看订单</el-button
              >
              <el-button
                size="small"
                v-if="scope.row.orderStatus === 1"
                type="primary"
                plain
                @click="shipmentsOne(scope.row.id)"
                >订单发货</el-button
              >
              <el-button
                size="small"
                v-if="scope.row.orderStatus === 0"
                type="danger"
                plain
                @click="cancelOrder(scope.row.id)"
                >关闭订单</el-button
              >
              <el-button
                size="small"
                v-if="scope.row.orderStatus === 0"
                type="danger"
                @click="deleteOrder(scope.row.id)"
                >删除订单</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    <div class="page-area">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="searchForm.pageNum"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="searchForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import orderApi from '@/api/order'
export default {
  data() {
    return {
      searchForm: {
        pageNum: 1,
        pageSize: 10,
      },
      orderStatusOption: [
        { label: '待付款', value: 0 },
        { label: '待发货', value: 1 },
        { label: '已发货', value: 2 },
        { label: '已完成', value: 3 },
        { label: '已关闭', value: 4 },
      ],
      dateValus: [],
      orderList: [],
      batchOptionList: [],
      total: 0,
    }
  },
  created() {
    this.getOrderList()
  },
  methods: {
    resetSearchFrom() {
      this.searchForm = {
        pageNum: 1,
        pageSize: 10,
      }
      this.dateValus = []
      this.getOrderList()
    },
    getOrderList() {
      if (this.dateValus.length > 0) {
        this.searchForm.startTime = this.dateValus[0]
        this.searchForm.endTime = this.dateValus[1]
      }
      orderApi.getOrderList(this.searchForm).then((res) => {
        this.orderList = res.data.list
        this.total = res.data.total
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      let list = []
      for (const item of val) {
        list.push(item.id)
      }
      this.batchOptionList = list
    },
    handleSizeChange(val) {
      this.searchForm.pageSize = val
      this.getOrderList()
    },
    handleCurrentChange(val) {
      this.searchForm.pageNum = val
      this.getOrderList()
    },
    toDetailPage(id) {
      this.$router.push({
        path: '/order/orderDetail',
        query: {
          id: id,
        },
      })
    },
    shipmentsOne(id) {
      this.$confirm('货品确定发出了吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          let obj = { orderIds: [id] }
          orderApi.orderShipments(obj).then((res) => {
            this.$message.success(res.message)
            this.getOrderList()
          })
        })
        .catch(() => {})
    },
    cancelOrder(id) {
      this.$confirm('确定取消该订单吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          orderApi.cancelOrder(id).then((res) => {
            this.$message.success(res.message)
            this.getOrderList()
          })
        })
        .catch(() => {})
    },
    deleteOrder(id) {
      this.$confirm('确定删除该订单吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          orderApi.deleteOrder(id).then((res) => {
            this.$message.success(res.message)
            this.getOrderList()
          })
        })
        .catch(() => {})
    },
    shiomentsBatch() {
      if (this.batchOptionList.length === 0) {
        this.$message.warning('请选择需要操作的订单')
        return
      }
      let obj = { ordderIds: this.batchOptionList }
      orderApi.orderShipments(obj).then((res) => {
        this.$message.success(res.message)
        this.getOrderList()
      })
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  padding: 30px;
  .header {
    margin-bottom: 10px;
    .search-area {
      border: 1px solid rgb(233, 231, 231);
      padding: 10px;
      margin-bottom: 10px;
      .top {
        display: flex;
        justify-content: space-between;
        align-items: baseline;
        margin-bottom: 10px;
        .title {
          span {
            margin-left: 6px;
          }
        }
      }
      .search-condition {
        display: flex;
        align-items: center;
        justify-content: center;
        .form-area {
          form {
            display: flex;
            width: 100%;
            .el-form-item {
              margin-right: 50px;
            }
          }
        }
      }
    }
    .banner {
      border: 1px solid rgb(233, 231, 231);
      padding: 10px;
      display: flex;
      justify-content: space-between;
      align-items: baseline;
      .left {
        display: flex;
        h3 {
          margin: 0 0 0 10px;
        }
      }
    }
  }
  .page-area {
    margin-top: 10px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>