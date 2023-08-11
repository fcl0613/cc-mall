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
          <el-button type="primary" @click="getAdList">查询搜索</el-button>
        </div>
      </div>
      <div class="search-query">
        <el-form ref="queryFormRef" :model="queryForm" label-width="80px">
          <el-form-item label="广告名称">
            <el-input v-model="queryForm.ADName"></el-input>
          </el-form-item>
          <el-form-item label="结束时间">
            <el-date-picker
              value-format="yyyy-MM-dd HH:mm:ss"
              v-model="queryForm.endTime"
              type="date"
              placeholder="选择日期"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="上架状态">
            <el-select v-model="queryForm.publishStatus" placeholder="请选择">
              <el-option
                v-for="item in publishOptions"
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
        <el-button type="primary" @click="toAddPage">添加广告</el-button>
      </div>
    </div>
    <div class="data-list">
      <el-table :data="adList" border style="width: 100%">
        <el-table-column label="编号" width="180">
          <template slot-scope="scope">
            {{
              (queryForm.pageNum - 1) * queryForm.pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="adName" label="广告名称" width="180">
        </el-table-column>
        <el-table-column label="广告图片">
          <template slot-scope="scope">
            <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.adPic"
              fit="contain"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column label="时间">
          <template slot-scope="scope">
            <span>开始时间：{{ scope.row.beginTime }}</span
            ><br />
            <span>结束时间：{{ scope.row.endTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="上线/下线">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.publishStatus"
              :active-value="1"
              :inactive-value="0"
              @change="changePublishStatus(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="editAd(scope.row.id)"
              title="修改"
            />
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="removeAd(scope.row.id)"
              title="删除"
            />
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
import adApi from '@/api/marketing/advertis'
export default {
  data() {
    return {
      queryForm: {
        pageNum: 1,
        pageSize: 10,
      },
      publishOptions: [
        {
          value: 0,
          label: '下架',
        },
        {
          value: 1,
          label: '上架',
        },
      ],
      adList: [],
      total: 0,
    }
  },
  created() {
    this.getAdList()
  },
  methods: {
    getAdList() {
      adApi.getAdList(this.queryForm).then((res) => {
        this.total = res.data.total
        this.adList = res.data.list
      })
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getAdList()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.getAdList()
    },
    changePublishStatus(row) {
      let obj = { id: row.id, status: row.publishStatus }
      adApi.updateAdPublish(obj).then((res) => {
        this.$message.success(res.message)
        this.getAdList()
      })
    },
    editAd(id) {
      this.$router.push({
        path: 'advertise/update',
        query: {
          adId: id,
        },
      })
    },
    removeAd(id) {
      this.$confirm('此操作将永久删除该广告, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          let obj = { list: [id] }
          adApi.removeAd(obj)
          this.getAdList()
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          })
        })
    },
    reset() {
      this.queryForm =  {
        pageNum: 1,
        pageSize: 10,
      }
      this.getAdList()
    },
    toAddPage() {
      this.$router.push({
        path: 'advertise/add',
      })
    }
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
        justify-content: center;
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
  .data-list {
    margin-top: 10px;
  }
  .page-area {
    margin-top: 10px;
    float: right;
  }
}
</style>