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
            <el-button type="primary" plain @click="getProductList"
              >搜索</el-button
            >
          </div>
        </div>
        <div class="search-condition">
          <div class="form-area">
            <el-form ref="form" :model="searchForm" label-width="80px">
              <el-form-item label="输入搜索">
                <el-input
                  v-model="searchForm.productName"
                  placeholder="商品名称"
                ></el-input>
              </el-form-item>
              <el-form-item label="商品分类">
                <el-cascader
                  v-model="cascaderValue"
                  :options="categoryList"
                  @change="handleChange"
                  placeholder="请选择"
                ></el-cascader>
              </el-form-item>
              <el-form-item label="上架状态">
                <el-select
                  v-model="searchForm.publishStatus"
                  placeholder="全部"
                >
                  <el-option label="上架" :value="0"></el-option>
                  <el-option label="下架" :value="1"></el-option>
                </el-select>
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
          <el-button @click="toAddPage">添加</el-button>
        </div>
      </div>
    </div>
    <div class="table-data">
      <el-table
        :data="productList"
        @selection-change="handleSelectionChange"
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="序号" width="70" align="center">
          <template slot-scope="scope">
            {{
              (searchForm.pageNum - 1) * searchForm.pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="商品名称" width="180">
        </el-table-column>
        <el-table-column prop="price" label="价格" width="180">
        </el-table-column>
        <el-table-column label="商品图片">
          <template slot-scope="scope">
            <div>
              <el-image
                style="width: 80px; height: 80px"
                :src="baseUrl + scope.row.productCover"
                fit=""
              ></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="商品库存"></el-table-column>
        <el-table-column label="上架">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.publishStatus"
              :active-value="0"
              :inactive-value="1"
              @change="changePublishStatus(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="新品">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.newStatus"
              :active-value="1"
              :inactive-value="0"
              @change="changeNewStatus(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="推荐">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.recommendStatus"
              :active-value="1"
              :inactive-value="0"
              @change="changeRecommendStatus(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-shopping-bag-1"
              size="mini"
              title="更新库存"
              @click="showUpdateDialog(scope.row.id)"
            />
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              title="编辑"
              @click="toEditPage(scope.row.id)"
            />
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              title="删除"
              @click="removeProduct(scope.row.id)"
            />
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="foot-area">
      <div class="batch-area">
        <el-select
          style="margin-right: 10px"
          v-model="batchFlag"
          placeholder="批量操作"
        >
          <el-option label="上架商品" value="1"></el-option>
          <el-option label="下架商品" value="2"></el-option>
          <el-option label="设为推荐" value="3"></el-option>
          <el-option label="取消推荐" value="4"></el-option>
          <el-option label="设为新品" value="5"></el-option>
          <el-option label="取消新品" value="6"></el-option>
          <el-option label="删除商品" value="7"></el-option>
        </el-select>
        <el-button type="primary" @click="batchOperation">确定</el-button>
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

    <el-dialog title="库存更新" :visible.sync="dialogVisible" width="30%">
      <el-form
        :model="stockForm"
        :rules="rules"
        ref="stockFormRef"
        label-width="100px"
      >
        <el-form-item label="库存" prop="stock">
          <el-input v-model.number="stockForm.stock"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="subStock">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import productApi from '@/api/product/product'
import baseUrl from '@/utils/imageBaseConfig'
import categoryApi from '@/api/product/category'
export default {
  data() {
    return {
      searchForm: {
        pageNum: 1,
        pageSize: 10,
      },
      productList: [],
      total: 0,
      baseUrl: baseUrl.IMAGE_BASE_URL,
      categoryList: [],
      cascaderValue: [],
      batchFlag: '',
      batchOptionList: [],
      dialogVisible: false,
      stockForm: {},
      rules: {
        stock: [
          { required: true, message: '请输入库存数量', trigger: 'blur' },
          { type: 'number', message: '库存必须为数字值' },
        ],
      },
    }
  },
  created() {
    this.getProductList()
    this.initCategorySelect()
  },
  methods: {
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
      this.getProductList()
    },
    handleCurrentChange(val) {
      this.searchForm.pageNum = val
      this.getProductList()
    },
    getProductList() {
      productApi.getProductList(this.searchForm).then((res) => {
        this.productList = res.data.list
        this.total = res.data.total
      })
    },
    changePublishStatus(row) {
      let obj = { id: row.id, status: row.publishStatus }
      productApi.changePublish(obj).then((res) => {
        this.$message.success(res.message)
      })
    },
    changeNewStatus(row) {
      let obj = { id: row.id, status: row.newStatus }
      productApi.changeNew(obj).then((res) => {
        this.$message.success(res.message)
      })
    },
    changeRecommendStatus(row) {
      let obj = { id: row.id, status: row.recommendStatus }
      productApi.changeRecommend(obj).then((res) => {
        this.$message.success(res.message)
      })
    },
    toEditPage(id) {
      this.$router.push({
        path: 'goods/update',
        query: {
          goodsId: id,
        },
      })
    },
    toAddPage() {
      this.$router.push('goods/add')
    },
    removeProduct(id) {
      this.$confirm('此操作将永久删除该商品, 是否继续?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      }).then(() => {
        let obj = {list: [id]}
        productApi.removeProduct(obj).then((res) => {
          this.$message.success(res.message)
        })
      })
    },
    resetSearchFrom() {
      this.searchForm = {
        pageNum: 1,
        pageSize: 10,
      }
      this.getProductList()
    },
    initCategorySelect() {
      categoryApi.getAllCategoryList().then((res) => {
        this.categoryList = res.data
      })
    },
    handleChange(val) {
      console.log(val)
      this.searchForm.categoryId = val[1]
      console.log(this.searchForm.categoryId)
    },
    showUpdateDialog(id) {
      this.stockForm.id = id
      this.dialogVisible = !this.dialogVisible
    },
    subStock() {
      this.$refs.stockFormRef.validate((valid) => {
        if (!valid) {
          this.$message({
            type: 'error',
            message: '请先填写库存信息',
          })
          return
        }
        productApi.stockAdd(this.stockForm).then((res) => {
          this.$message.success(res.message)
          this.dialogVisible = !this.dialogVisible
          this.getProductList()
        })
      })
    },
    batchOperation() {
      if(this.batchFlag === '') {
        this.$message.error('请选择操作条件')
        return
      }
      if(this.batchOptionList.length === 0) {
        this.$message.error('请选择操作对象')
        return
      }
      this.$confirm('确定执行该操作吗?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      }).then(() => {
        if(this.batchFlag === '1') {
          let obj = {list: this.batchOptionList}
          productApi.batchPublish(obj).then((res) => {
            this.$message.success(res.message)
            this.getProductList()
          })
          return
        }
        if(this.batchFlag === '2') {
          let obj = {list: this.batchOptionList}
          productApi.batchUnPublish(obj).then((res) => {
            this.$message.success(res.message)
            this.getProductList()
          })
          return
        }
        if(this.batchFlag === '3') {
          let obj = {list: this.batchOptionList}
          productApi.batchRecommed(obj).then((res) => {
            this.$message.success(res.message)
            this.getProductList()
          })
          return
        }
        if(this.batchFlag === '4') {
          let obj = {list: this.batchOptionList}
          productApi.batchUnRecommed(obj).then((res) => {
            this.$message.success(res.message)
            this.getProductList()
          })
          return
        }
        if(this.batchFlag === '5') {
          let obj = {list: this.batchOptionList}
          productApi.batchNew(obj).then((res) => {
            this.$message.success(res.message)
            this.getProductList()
          })
          return
        }
        if(this.batchFlag === '6') {
          let obj = {list: this.batchOptionList}
          productApi.batchUnNew(obj).then((res) => {
            this.$message.success(res.message)
            this.getProductList()
          })
          return
        }
        if(this.batchFlag === '7') {
          let obj = {list: this.batchOptionList}
          productApi.removeProduct(obj).then((res) => {
            this.$message.success(res.message)
            this.getProductList()
          })
          return
        }
      }).catch(() => {})
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
  .table-data {
    margin-bottom: 10px;
  }
  .foot-area {
    display: flex;
    justify-content: space-between;
  }
}
</style>