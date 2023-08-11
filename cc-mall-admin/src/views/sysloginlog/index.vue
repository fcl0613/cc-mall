<template>
  <div class="container">
    <div class="header">
      <div class="search">
        <el-input v-model="username" placeholder="用户名"></el-input>
      </div>
      <div class="time-select">
        <el-date-picker
          v-model="timeRange"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        >
        </el-date-picker>
      </div>
      <div class="search-btn">
        <el-button type="primary" @click="getLoginLogList" plain
          >搜索</el-button
        >
      </div>
      <div class="reset-btn">
        <el-button type="primary" @click="reset" plain>重置</el-button>
      </div>
    </div>
    <el-table :data="logList" border style="width: 100%">
      <el-table-column prop="username" label="用户名" width="280">
      </el-table-column>
      <el-table-column prop="ipAddr" label="IP地址" width="180">
      </el-table-column>
      <el-table-column prop="userAgent" label="浏览器类型"> </el-table-column>
      <el-table-column prop="createTime" label="登录时间"> </el-table-column>
    </el-table>
    <div class="page">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20, 40, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="size"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import api from '@/api/syslog/loginLog'
export default {
  data() {
    return {
      username: '',
      timeRange: [],
      pageNum: 1,
      pageSize: 10,
      logList: [],
      size: 0,
    }
  },
  created() {
    this.getLoginLogList()
  },
  methods: {
    getLoginLogList() {
      let querydto = {}
      querydto.username = this.username
      if (this.timeRange.length > 1) {
        querydto.startTime = this.timeRange[0]
        querydto.endTime = this.timeRange[1]
      } else {
        querydto.startTime = ''
        querydto.endTime = ''
      }
      querydto.pageNum = this.pageNum
      querydto.pageSize = this.pageSize
      console.log(querydto)
      api.getLoginLogList(querydto).then((res) => {
        this.logList = res.data.list
        this.size = res.data.size
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getLoginLogList()
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.getLoginLogList()
    },
    reset() {
      this.username = ''
      this.timeRange = []
      this.pageNum = 1
      this.pageSize = 10
      this.getLoginLogList()
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  margin-top: 10px;
  margin-left: 10px;
  .header {
    height: 40px;
    margin-bottom: 20px;
    .search {
      width: 200px;
      float: left;
      display: inline;
    }
    .time-select {
      width: 350px;
      float: left;
      display: inline;
      margin: 0 0 0 8px;
    }
    .search-btn {
      float: left;
      display: inline;
      margin-left: 8px;
    }
    .reset-btn {
      float: left;
      display: inline;
      margin-left: 8px;
    }
  }
  .page {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
}
</style>
