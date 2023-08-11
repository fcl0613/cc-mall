<template>
  <div class="conatiner">
    <div class="search">
      <div class="left">
        <div class="search-input">
          <el-input
            placeholder="请输入内容"
            v-model="keyword"
            clearable
            @clear="getCustomerList"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="getCustomerList"
            ></el-button>
          </el-input>
        </div>
        <div class="batch-remove">
          <el-button type="danger" @click="batchRemove">删除</el-button>
        </div>
      </div>
      <div class="right">
        <el-button @click="showAddDialog">添加用户</el-button>
      </div>
    </div>
    <div class="table-area">
      <el-table
        :data="customerList"
        stripe
        border
        style="width: 100%; margin-top: 10px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection"></el-table-column>
        <el-table-column label="序号" width="70" align="center">
          <template slot-scope="scope">
            {{ (pageNum - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>

        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="gender" label="性别" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="nickName" label="昵称" />
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="showEditDialog(scope.row.id)"
              title="修改"
            />
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="removeDataById(scope.row.id)"
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
        :current-page="pageNum"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%">
      <el-form
        :model="addOrUpdateCustomerDTO"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="addOrUpdateCustomerDTO.username"
            :disabled="addOrUpdateCustomerDTO.id ? true : false"
          ></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="addOrUpdateCustomerDTO.phone"></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="addOrUpdateCustomerDTO.nickName"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="addOrUpdateCustomerDTO.gender">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="resetForm">取 消</el-button>
        <el-button type="primary" @click="subFrom">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import customerApi from '@/api/customer/customer'
export default {
  data() {
    const checkPhone = (rule, value, cb) => {
      const phoneReg = /^(?:(?:\+|00)86)?1[3-9]\d{9}$/
      if (phoneReg.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的手机号'))
    }
    return {
      pageNum: 1,
      pageSize: 10,
      keyword: '',
      total: 0,
      customerList: [],
      addOrUpdateCustomerDTO: {},
      removeIdList: [],
      dialogTitle: '添加',
      dialogVisible: false,
      rules: {
        username: [
          { required: true, message: '请输入用户名称', trigger: 'blur' },
          {
            min: 3,
            max: 40,
            message: '长度在 3 到 40 个字符',
            trigger: 'blur',
          },
        ],
        phone: [
          { required: true, message: '请输入', trigger: 'blur' },
          { validator: checkPhone, trigger: 'blur' },
        ],
        nickName: [
          { required: true, message: '请输入用户名称', trigger: 'blur' },
        ],
        gender: [{ required: true, message: '请选择性别', trigger: 'blur' }],
      },
    }
  },
  created() {
    this.getCustomerList()
  },
  methods: {
    getCustomerList() {
      customerApi
        .getCustomerList(this.pageNum, this.pageSize, this.keyword)
        .then((res) => {
          this.customerList = res.data.list
          this.total = res.data.total
        })
    },
    addCustomer() {
      customerApi.addCustomer(this.addOrUpdateCustomerDTO).then((res) => {
        this.$message.success('添加成功')
        this.getCustomerList()
        this.resetForm()
      })
    },
    updateCustomer() {
      customerApi.updateCustomer(this.addOrUpdateCustomerDTO).then((res) => {
        this.$message.success('修改成功')
        this.getCustomerList()
        this.resetForm()
      })
    },
    getCustomerDetail(id) {
      customerApi.getCustomerDetail(id).then((res) => {
        this.addOrUpdateCustomerDTO = res.data
      })
    },
    removeCustomer() {
      let removeObj = new Object()
      removeObj.list = this.removeIdList
      customerApi.removeCustomer(removeObj).then((res) => {
        this.$message.success('删除成功')
        this.removeIdList = []
        this.getCustomerList()
      })
    },
    showAddDialog() {
      this.dialogTitle = '添加'
      this.dialogVisible = !this.dialogVisible
    },
    handleSelectionChange(val) {
      console.log(val)
      // 这里获取的是一整个对象
      // 在赋值之前先把之前的数据清空
      this.removeIdList = []
      val.forEach((element) => {
        this.removeIdList.push(element.id)
      })
    },
    showEditDialog(id) {
      this.dialogTitle = '修改'
      this.getCustomerDetail(id)
      this.dialogVisible = !this.dialogVisible
    },
    removeDataById(id) {
      // 先清空上次的数据
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      }).then(() => {
        this.removeIdList = []
        this.removeIdList.push(id)
        this.removeCustomer()
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getCustomerList()
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.getCustomerList()
    },
    resetForm() {
      this.addOrUpdateCustomerDTO = {}
      this.$refs.ruleForm.resetFields()
      this.dialogVisible = !this.dialogVisible
    },
    subFrom() {
      this.$refs.ruleForm.validate((valid) => {
        if (!valid) {
          return this.$message({
            type: 'error',
            message: '请先填写正确的信息',
          })
        }
        if (!this.addOrUpdateCustomerDTO.id) {
          // 走添加逻辑
          this.addCustomer()
        } else {
          // 走更新逻辑
          this.updateCustomer()
        }
      })
    },
    batchRemove() {
      console.log(this.removeIdList.length)
      if (this.removeIdList.length === 0) {
        this.$message.warning('请先选择需要删除的用户')
        return
      }
      this.$confirm('此操作将永久删除这些用户, 是否继续?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      }).then(() => {
        this.removeCustomer()
      })
    },
  },
}
</script>

<style lang="less" scoped>
.conatiner {
  padding: 20px;
  .search {
    display: flex;
    justify-content: space-between;
    .left {
      display: flex;
      .batch-remove {
        margin-left: 10px;
      }
    }
  }
  .table-area {
    margin-top: 20px;
  }
  .page-area {
    margin-top: 20px;
    float: left;
  }
}
</style>