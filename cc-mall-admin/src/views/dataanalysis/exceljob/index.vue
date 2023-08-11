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
          <el-button type="primary" @click="getJobList">查询搜索</el-button>
        </div>
      </div>
      <div class="search-query">
        <el-form ref="queryFormRef" :model="queryForm" label-width="100px">
          <el-form-item label="任务名称">
            <el-input v-model="queryForm.jobName"></el-input>
          </el-form-item>
          <el-form-item label="任务状态">
            <el-select v-model="queryForm.jobStatus" placeholder="请选择">
              <el-option
                v-for="item in jobStatusOptions"
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
    </div>
    <div class="data-area">
      <el-table :data="jobList" border style="width: 100%">
        <el-table-column type="index" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="jobName" label="任务名称" width="200">
        </el-table-column>
        <el-table-column label="任务状态" width="120">
          <template slot-scope="scope">
            <div v-if="scope.row.jobStatus === 0">未开始</div>
            <div v-if="scope.row.jobStatus === 1">执行中</div>
            <div v-if="scope.row.jobStatus === 2">执行失败</div>
            <div v-if="scope.row.jobStatus === 3">执行成功</div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
        </el-table-column>
        <el-table-column prop="finishTime" label="完成时间" width="180">
        </el-table-column>
        <el-table-column prop="failedReason" label="失败原因" width="190">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.jobStatus == 3"
              type="text"
              @click="downLoad(scope.row.downloadUrl)"
              >下载</el-button
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
import exceljobApi from '@/api/dataanalysis/exceljob'
export default {
  data() {
    return {
      queryForm: {
        jobName: '',
        jobStatus: null,
        pageNum: 1,
        pageSize: 10,
      },
      jobStatusOptions: [
        { label: '未开始', value: 0 },
        { label: '执行中', value: 1 },
        { label: '执行失败', value: 2 },
        { label: '执行成功', value: 3 },
      ],
      jobList: [],
      total: 0,
    }
  },
  created() {
    this.getJobList()
  },
  methods: {
    getJobList() {
      exceljobApi.getJobList(this.queryForm).then((res) => {
        this.jobList = res.data.list
        this.total = res.data.total
        console.log(this.jobList)
      })
    },
    reset() {
      this.queryForm = {
        name: '',
        type: null,
        pageNum: 1,
        pageSize: 10,
      }
      this.getJobList()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getJobList()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.getJobList()
    },
    downLoad(url) {
      window.location.href = url
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