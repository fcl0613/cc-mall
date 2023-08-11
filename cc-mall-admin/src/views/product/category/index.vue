<template>
  <div class="container">
    <div class="header">
      <div class="left">
        <i class="el-icon-tickets"></i>
        商品分类数据列表
      </div>
      <div class="right">
        <el-button @click="addCategory" size="small">添加</el-button>
      </div>
    </div>
    <div class="category-table">
      <el-table :data="categoryList" border style="width: 100%">
        <el-table-column label="序号" width="70" align="center">
          <template slot-scope="scope">
            {{ (pageNum - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="分类名称" width="200">
        </el-table-column>
        <el-table-column
          prop="level"
          label="级别"
          width="180"
        ></el-table-column>
        <el-table-column prop="navStatus" label="导航栏" width="180">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.navStatus"
              :active-value="1"
              :inactive-value="0"
              @change="changeNavStatus(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="showStatus" label="是否显示" width="180">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.showStatus"
              :active-value="1"
              :inactive-value="0"
              @change="changeShowStatus(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="180"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="editCategory(scope.row.id)"
              title="修改"
            />
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="removeCategory(scope.row.id)"
              title="删除"
            />
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page-plugin">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20, 40, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import api from '@/api/product/category'
export default {
  data() {
    return {
      categoryList: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
    }
  },
  mounted() {
    this.getCategoryList()
  },
  methods: {
    getCategoryList() {
      api.getCategoryList(this.pageNum, this.pageSize).then((res) => {
        console.log(res)
        this.categoryList = res.data.list
        this.total = res.data.size
      })
    },
    addCategory() {
      this.$router.push('category/add')
    },
    editCategory(id) {
      this.$router.push({
        path: 'category/edit',
        query: {
          categoryId: id,
        },
      })
    },
    removeCategory(id) {
      this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      }).then(() => {
        let obj = {'categoryId': id}
        api.removeCategroy(obj).then((res) => {
          if (res.code === 200) {
            this.$message({
              message: '删除成功',
              type: 'success',
            })
            this.getCategoryList()
          } else {
            this.$message({
              message: res.message,
              type: 'error',
            })
          }
        })
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getCategoryList()
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.getCategoryList()
    },
    // 修改是否显示在导航栏
    changeNavStatus(obj) {
      api.updateNavStatus(obj.id, obj.navStatus).then((res) => {
        if(res.code === 200) {
          this.$message({
            message: '修改成功',
            type: 'success'
          })
        }else {
          this.$message({
            message: '修改失败',
            type: 'error'
          })
        }
      })
    },
    // 修改是否显示
    changeShowStatus(obj) {
      api.updateShowStatus(obj.id, obj.showStatus).then((res) => {
        if(res.code === 200) {
          this.$message({
            message: '修改成功',
            type: 'success'
          })
        }else {
          this.$message({
            message: '修改失败',
            type: 'error'
          })
        }
      })
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  padding: 20px;
  .header {
    border: solid 1px #ebeef5;
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
    padding: 20px;
    .left {
      font-size: 18px;
      line-height: 32px;
    }
  }
  .category-table {
    margin-top: 20px;
  }
  .page-plugin {
    margin-top: 20px;
    float: right;
    padding-right: 20px;
  }
}
</style>