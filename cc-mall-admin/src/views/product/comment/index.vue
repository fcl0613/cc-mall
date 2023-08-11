<template>
  <div class="container">
    <el-card>
      <el-table :data="commentList" border style="width: 100%">
        <el-table-column prop="username" label="评论用户"> </el-table-column>
        <el-table-column prop="content" label="评论内容"> </el-table-column>
        <el-table-column prop="createTime" label="评论时间"></el-table-column>
        <el-table-column prop="score" label="评论分数"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <div>
              <el-button
                type="danger"
                @click="remove(scope.row.id)"
              >删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
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
    </el-card>
  </div>
</template>

<script>
import commentApi from '@/api/product/comment'
export default {
  data() {
    return {
      commentList: [],
      total: 0,
      searchForm: {
        pageNum: 1,
        pageSize: 10,
      },
    }
  },
  created() {
    this.getCommentList()
  },
  methods: {
    getCommentList() {
      commentApi.getCommentList(this.searchForm).then((res) => {
        this.commentList = res.data.list
        this.total = res.data.total
      })
    },
    handleSizeChange(val) {
      this.searchForm.pageSize = val
      this.getCommentList()
    },
    handleCurrentChange(val) {
      this.searchForm.pageNum = val
      this.getCommentList()
    },
    remove(id) {
      this.$confirm('确定要删除该评论吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      }).then(() => {
        commentApi.remove(id).then((res) => {
          this.$message.success(res.message)
          this.getCommentList()
        })
      })
    },
  },
}
</script>

<style lang="less" scoped>
.conatiner {
  .el-card {
    margin-top: 20px;
  }
}
</style>