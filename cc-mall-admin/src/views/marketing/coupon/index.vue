<template>
  <div class="container">
    <div class="search-area">
      <div class="header">
        <div class="title">
          <i class="el-icon-search"></i>
          <span style="margin-left: 3px">筛选搜索</span>
        </div>
        <div class="btn-group">
          <el-button @click="reset">重置</el-button>
          <el-button type="primary" @click="getCouponList">查询搜索</el-button>
        </div>
      </div>
      <div class="search-query">
        <el-form ref="queryFormRef" :model="queryForm" label-width="100px">
          <el-form-item label="优惠券名称">
            <el-input v-model="queryForm.name"></el-input>
          </el-form-item>
          <el-form-item label="优惠券类型">
            <el-select v-model="queryForm.type" placeholder="请选择">
              <el-option
                v-for="item in couponTypeOptions"
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
    <div class="center-banner">
      <div class="left">
        <i class="el-icon-tickets"></i>
        <span>数据列表</span>
      </div>
      <div class="right">
        <el-button type="primary" @click="toAddPage">添加优惠券</el-button>
      </div>
    </div>
    <div class="data-area">
      <el-table
        :data="couponList"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column type="index" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="name" label="优惠券名称" width="200">
        </el-table-column>
        <el-table-column prop="type" label="优惠券类型" width="100">
        </el-table-column>
        <el-table-column prop="useType" label="可使用商品" width="100"> </el-table-column>
        <el-table-column label="使用门槛" width="120">
          <template slot-scope="scope">
            <div>满{{ scope.row.minPoint }}元可用</div>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="面值单位元" width="120">
        </el-table-column>
        <el-table-column label="有效期" width="200">
          <template slot-scope="scope">
            <div>{{ scope.row.startTime }}至{{ scope.row.endTime }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="showDetail(scope.row.id)"
              >查看</el-button
            >
            <el-button type="text" @click="toEditPage(scope.row.id)"
              >编辑</el-button
            >
            <el-button type="text" @click="removeCoupon(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
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
</template>

<script>
import couponApi from '@/api/marketing/coupon'
export default {
  data() {
    return {
      queryForm: {
        name: '',
        type: null,
        pageNum: 1,
        pageSize: 10,
      },
      couponTypeOptions: [
        { label: '全场赠券', value: 0 },
        { label: '会员赠券', value: 1 },
        { label: '购物赠券', value: 2 },
        { label: '注册赠券', value: 3 },
      ],
      couponList: [],
      total: 0,
    }
  },
  created() {
    this.getCouponList()
  },
  methods: {
    getCouponList() {
      couponApi.getCouponList(this.queryForm).then((res) => {
        this.couponList = res.data.list
        this.total = res.data.total
      })
    },
    reset() {
      this.queryForm = {
        name: '',
        type: null,
        pageNum: 1,
        pageSize: 10,
      }
      this.getCouponList()
    },
    toAddPage() {
      this.$router.push({
        path: 'coupon/add'
      })
    },
    handleSelectionChange(val) {},
    showDetail(id) {
      this.$router.push({
        path: 'coupon/detail',
        query: {
          couponId: id
        }
      })
    },
    toEditPage(id) {
      this.$router.push({
        path: 'coupon/edit',
        query: {
          couponId: id
        }
      })
    },
    removeCoupon(id) {
      this.$confirm('确定要永久删除该优惠卷?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let obj = {list: [id]}
          couponApi.deleteCoupon(obj).then((res) => {
            this.$message.success(res.message)
            this.getCouponList()
          })
        }).catch(() => {
                 
        })
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getCouponList()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.getCouponList()
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  padding: 30px;
  .search-area {
    border: 1px solid rgb(235, 235, 235);
    .header {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: baseline;
      padding: 6px;
    }
    .search-query {
      margin-top: 10px;
      .el-form {
        display: flex;
        margin-left: 100px;
      }
    }
  }
  .center-banner {
    margin-top: 10px;
    padding: 6px;
    border: 1px solid rgb(235, 235, 235);
    display: flex;
    justify-content: space-between;
    align-items: baseline;
  }
  .data-area {
    margin-top: 10px;
  }
  .page-area {
    margin-top: 10px;
    float: right;
  }
}
</style>