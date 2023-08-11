<template>
  <div class="app-container">
    <div class="header">
      <div class="left">
        <div class="search">
          <el-input
            v-model="keyword"
            clearable
            @clear="getRoleList"
            placeholder="请输入搜索内容"
          ></el-input>
        </div>
        <div class="search-btn">
          <el-button type="primary" size="medium" @click="getRoleList"
            >搜索</el-button
          >
        </div>
        <div class="search-btn">
          <el-button type="danger" size="medium" @click="removeSysRoles"
            >删除</el-button
          >
        </div>
      </div>
      <div class="right">
        <div class="add-btn">
          <el-button type="primary" size="medium" @click="showAddRolePage"
            >添加</el-button
          >
        </div>
      </div>
    </div>

    <el-table
      v-loading="listLoading"
      :data="roleList"
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

      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="showEditPage(scope.row.id)"
            title="修改"
          />
          <el-button 
          type="warning" 
          icon="el-icon-baseball" 
          size="mini" 
          @click="showConferredAuth(scope.row)" 
          title="分配权限"/>
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
    <!-- 分页组件 -->
    <div class="page">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%" @close="resetForm">
      <el-form
        ref="dataForm"
        :model="sysRole"
        :rules="formRules"
        label-width="150px"
        size="small"
        style="padding-right: 40px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="sysRole.roleName" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="sysRole.roleCode" />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="sysRole.description" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          @click="dialogVisible = false"
          size="small"
          icon="el-icon-refresh-right"
          >取 消</el-button
        >
        <el-button
          type="primary"
          icon="el-icon-check"
          @click="saveOrUpdate"
          size="small"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api/sysrole/index'
export default {
  data() {
    const checkroleCode = (rule, value, cb) => {
      const roleCode = /^[a-zA-Z]+$/
      if (roleCode.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的角色编码,英文字母组成'))
    }
    return {
      roleList: [],
      pageNum: 1,
      pageSize: 10,
      keyword: '',
      total: 0,
      listLoading: true,
      sysRole: {},
      dialogVisible: false,
      deleteObjects: [],
      formRules: {
        roleName: [
          { required: true, message: '请输入', trigger: 'blur' },
          {
            min: 2,
            max: 10,
            message: '角色名长度在2~10个字符之间',
            trigger: 'blur',
          },
        ],
        roleCode: [
          { required: true, message: '请输入', trigger: 'blur' },
          { validator: checkroleCode, trigger: 'blur' },
        ],
      },
    }
  },
  created() {
    this.getRoleList()
  },
  methods: {
    getRoleList() {
      api.getRoleList(this.pageNum, this.pageSize, this.keyword).then((res) => {
        console.log(res)
        this.roleList = res.data.records
        this.total = res.data.total
        this.listLoading = false
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getRoleList()
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.getRoleList()
    },
    showEditPage(id) {
      api.getSysRoleById(id).then((res) => {
        if (res.code === 200 && res.data !== null) {
          this.sysRole = res.data
          this.dialogVisible = !this.dialogVisible
        } else {
          this.$message({
            type: 'error',
            message: res.message,
          })
        }
      })
    },
    removeDataById(id) {
      console.log(id)
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      })
        .then(() => {
          let ids = [id]
          console.log(ids[0])
          api.removeRoleByIds(ids).then((res) => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.getRoleList()
            } else {
              this.$message({
                type: 'error',
                message: res.message,
              })
            }
          })
        })
        .catch(() => {})
    },
    showAddRolePage() {
      this.dialogVisible = !this.dialogVisible
      // 清空弹框数据
      this.sysRole = {}
    },
    saveOrUpdate() {
      if (!this.sysRole.id) {
        this.createSysRole()
      } else {
        this.updateSysRole()
      }
    },
    createSysRole() {
      this.$refs.dataForm.validate((valid) => {
        if (!valid) {
          this.$message({
            type: 'error',
            message: '请先填写信息',
          })
          return
        }
        api.addSysRole(this.sysRole).then((res) => {
          if (res.code == 200) {
            this.$message({
              type: 'success',
              message: '添加成功!',
            })
            this.dialogVisible = !this.dialogVisible
            this.getRoleList()
            this.sysRole = {}
          } else {
            this.$message({
              type: 'error',
              message: res.message,
            })
          }
        })
      })
    },
    updateSysRole() {
      this.$refs.dataForm.validate((valid) => {
        if (!valid) {
          this.$message({
            type: 'error',
            message: '请先填写信息',
          })
          return
        }
        // console.log(this.sysRole)
        api.updateSysRole(this.sysRole).then((res) => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '修改成功!',
            })
            this.dialogVisible = !this.dialogVisible
            this.getRoleList()
            this.sysRole = {}
          } else {
            this.$message({
              type: 'error',
              message: res.message,
            })
          }
        })
      })
    },
    removeSysRoles() {
      if (this.deleteObjects.length === 0) {
        this.$message({
          type: 'warning',
          message: '请先选择需要删除的角色',
        })
        return
      }
      this.$confirm('此操作将永久删除这些角色, 是否继续?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      })
        .then(() => {
          let ids = []
          for (let i = 0; i < this.deleteObjects.length; i++) {
            ids.push(this.deleteObjects[i].id)
          }
          api.removeRoleByIds(ids).then((res) => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.getRoleList()
            } else {
              this.$message({
                type: 'error',
                message: res.message,
              })
            }
          })
        })
        .catch(() => {})
    },
    handleSelectionChange(val) {
      // 这玩意儿接收的是一整个数组对象
      this.deleteObjects = val
      // console.log(this.deleteObjects)
    },
    resetForm() {
      this.sysRole = {}
      this.$refs.dataForm.resetFields()
    },
    showConferredAuth(row) {
      this.$router.push('/system/conferredAuth?id='+row.id+'&roleName='+row.roleName);
    }
  },
}
</script>

<style lang="less" scoped>
.header {
  display: flex;
  justify-content: space-between;
  .left {
    div {
      display: inline-block;
    }
    .search {
      margin-right: 20px;
    }
    .search-btn {
      margin-right: 20px;
    }
  }
}
.page {
  display: flex;
  // align-items: center;
  justify-content: center;
  margin-top: 20px;
}
</style>