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
            <el-button type="primary" plain @click="getApplyList"
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
              <el-form-item label="申请状态">
                <el-select v-model="searchForm.status" placeholder="全部">
                  <el-option
                    v-for="(item, index) in applyStatusOption"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="搜索日期">
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
          <!-- <el-button type="primary" @click="shiomentsBatch">批量发货</el-button> -->
        </div>
      </div>
    </div>
    <div class="data-area">
      <el-table :data="applyList" border style="width: 100%">
        <el-table-column type="index" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="id" label="服务单号" width="100">
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180"> </el-table-column>
        <el-table-column prop="returnPrice" label="回退金额" width="120"> </el-table-column>
        <el-table-column label="状态" width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.status === 0">待处理</div>
            <div v-if="scope.row.status === 1">退货中</div>
            <div v-if="scope.row.status === 2">已完成</div>
            <div v-if="scope.row.status === 3">已拒绝</div>
            <div v-if="scope.row.status === 4">删除</div>
          </template>
        </el-table-column>
        <el-table-column prop="orderSn" label="订单号" width="180"> </el-table-column>
        <el-table-column prop="handleTime" label="处理时间" width="180"> </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <div v-if="scope.row.status === 0">
              <el-button type="primary" plain @click="agreeApply(scope.row.id)"
                >同意</el-button
              >
              <el-button type="danger" plain @click="refuseApply(scope.row.id)"
                >拒绝</el-button
              >
            </div>
            <div v-if="scope.row.status === 1">
              <el-button type="primary" plain @click="finishApply(scope.row.id)"
                >退货完成</el-button
              >
            </div>
          </template>
        </el-table-column>
      </el-table>
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

    <el-dialog title="拒绝原因" @close="resetRefuseForm" :visible.sync="dialogFormVisible">
      <el-form :model="refuseForm">
        <el-form-item label="拒绝原因">
          <el-input type="textarea" v-model="refuseForm.handleNote" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="subRefuseForm"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import returnApplyApi from '@/api/order/returnApply'
export default {
  data() {
    return {
      searchForm: {
        pageNum: 1,
        pageSize: 10,
      },
      applyStatusOption: [
        { label: '待处理', value: 0 },
        { label: '退货中', value: 1 },
        { label: '已完成', value: 2 },
        { label: '已拒绝', value: 3 },
        // { label: '删除', value: 4 }
      ],
      applyList: [],
      total: 0,
      dateValus: [],
      dialogFormVisible: false,
      refuseForm: {}
    }
  },
  created() {
    this.getApplyList()
  },
  methods: {
    getApplyList() {
      if (this.dateValus.length > 0) {
        this.searchForm.startTime = this.dateValus[0]
        this.searchForm.endTime = this.dateValus[1]
      }
      returnApplyApi.getApplyList(this.searchForm).then((res) => {
        this.applyList = res.data.list
        this.total = res.data.total
      })
    },
    handleSizeChange(val) {
      this.searchForm.pageSize = val
      this.getApplyList()
    },
    handleCurrentChange(val) {
      this.searchForm.pageNum = val
      this.getApplyList()
    },
    agreeApply(id) {
      this.$confirm('确定同意该申请吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          returnApplyApi.agreeeApply(id).then((res) => {
            this.$message.success(res.message)
            this.getApplyList()
          })
        })
        .catch(() => {})
    },
    refuseApply(id) {
      this.refuseForm.id = id
      this.dialogFormVisible = !this.dialogFormVisible
    },
    finishApply(id) {
      returnApplyApi.finishApply(id).then((res) => {
        this.$message.success(res.message)
        this.getApplyList()
      })
    },
    resetSearchFrom() {
      this.searchForm = {
        pageNum: 1,
        pageSize: 10,
      }
      this.getApplyList()
    },
    resetRefuseForm() {
      this.refuseForm = {}
    },
    subRefuseForm() {
      returnApplyApi.refuseApply(this.refuseForm).then((res) => {
        this.$message.success(res.message)
        this.getApplyList()
        this.dialogFormVisible = !this.dialogFormVisible
      })
    }
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