<template>
  <div class="app-container">
    <div class="header">
      <div class="left">
        <div class="search">
          <el-input
            v-model="keyword"
            clearable
            @clear="getUserList"
            placeholder="用户名"
          ></el-input>
        </div>
        <div class="search-btn">
          <el-button type="primary" size="medium" @click="getUserList"
            >搜索</el-button
          >
        </div>
        <div class="search-btn">
          <el-button type="danger" size="medium" @click="removeSysUsers"
            >删除</el-button
          >
        </div>
      </div>
      <div class="right">
        <div class="add-btn">
          <el-button type="primary" size="medium" @click="showAddUserPage"
            >添加</el-button
          >
        </div>
      </div>
    </div>
    <el-table
      v-loading="listLoading"
      :data="userList"
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

      <el-table-column prop="userName" label="用户名" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column label="状态">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="changeUserStatus(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
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
            type="warring"
            icon="el-icon-setting"
            size="mini"
            @click="showAssignRole(scope.row)"
            title="分配角色"
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

    <el-dialog
      title="添加/修改"
      :visible.sync="dialogVisible"
      width="40%"
      @close="resetForm"
    >
      <el-form
        ref="dataForm"
        :model="sysUser"
        :rules="formRules"
        label-width="150px"
        size="small"
        style="padding-right: 40px"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input
            :readonly="sysUser.id ? true : false"
            v-model="sysUser.userName"
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="sysUser.name" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="sysUser.phone" />
        </el-form-item>
        <el-form-item label="身份证" prop="idCard">
          <el-input v-model="sysUser.idCard" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="sysUser.description" />
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

    <el-dialog title="分配角色" :visible.sync="dialogRoleVisible">
      <el-form label-width="80px">
        <el-form-item label="用户名">
          <el-input disabled :value="sysUser.userName"></el-input>
        </el-form-item>

        <el-form-item label="角色列表">
          <el-checkbox
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            @change="handleCheckAllChange"
            >全选</el-checkbox
          >
          <div style="margin: 15px 0"></div>
          <el-checkbox-group
            v-model="userRoleIds"
            @change="handleCheckedChange"
          >
            <el-checkbox
              v-for="role in allRoles"
              :key="role.id"
              :label="role.id"
              >{{ role.roleName }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="assignRole" size="small"
          >保存</el-button
        >
        <el-button @click="dialogRoleVisible = false" size="small"
          >取消</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api/sysuser/index'
export default {
  data() {
    const checkUserName = (rule, value, cb) => {
      const userNameReg = /^[a-zA-Z0-9_-]{4,16}$/
      if (userNameReg.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的用户名,数字、英文字母、下划线组成'))
    }
    const checkName = (rule, value, cb) => {
      const nameReg = /^(?:[\u4e00-\u9fa5·]{2,16})$/
      if (nameReg.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的姓名,中文组成'))
    }
    const checkPhone = (rule, value, cb) => {
      const phoneReg = /^(?:(?:\+|00)86)?1[3-9]\d{9}$/
      if (phoneReg.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的手机号'))
    }
    const checkIdCard = (rule, value, cb) => {
      const idCardReg =
        /^[1-9]\d{5}(?:18|19|20)\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\d|30|31)\d{3}[\dXx]$/
      if (idCardReg.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的身份证'))
    }
    return {
      pageNum: 1,
      pageSize: 10,
      keyword: '',
      listLoading: true,
      userList: [],
      total: 0,
      createTimes: '',
      deleteObjects: [],
      dialogVisible: false,
      sysUser: {},
      formRules: {
        userName: [
          { required: true, message: '请输入', trigger: 'blur' },
          {
            min: 4,
            max: 16,
            message: '用户名长度在4~16个字符之间',
            trigger: 'blur',
          },
          { validator: checkUserName, trigger: 'blur' },
        ],
        name: [
          { required: true, message: '请输入', trigger: 'blur' },
          { validator: checkName, trigger: 'blur' },
        ],
        phone: [
          { required: true, message: '请输入', trigger: 'blur' },
          { validator: checkPhone, trigger: 'blur' },
        ],
        idCard: [
          { required: true, message: '请输入', trigger: 'blur' },
          { validator: checkIdCard, trigger: 'blur' },
        ],
        description: [{ required: true, message: '请输入', trigger: 'blur' }],
      },
      dialogRoleVisible: false,
      allRoles: [], // 所有角色列表
      userRoleIds: [], // 用户的角色ID的列表
      isIndeterminate: false, // 是否是不确定的
      checkAll: false, // 是否全选
    }
  },
  created() {
    this.getUserList()
  },
  methods: {
    getUserList() {
      api
        .getSysUserList(this.pageNum, this.pageSize, this.keyword)
        .then((res) => {
          if (res.code === 200) {
            this.userList = res.data.records
            this.total = res.data.total
            this.listLoading = false
          } else {
            this.$message.error(res.message)
          }
        })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getUserList()
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.getUserList()
    },
    removeSysUsers() {
      if (this.deleteObjects.length === 0) {
        this.$message.warning('请先选择需要删除的用户')
        return
      }
      this.$confirm('此操作将永久删除这些用户, 是否继续?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      })
        .then(() => {
          let ids = []
          for (let i = 0; i < this.deleteObjects.length; i++) {
            ids.push(this.deleteObjects[i].id)
          }
          console.log(ids)
          api.removeUserByIds(ids).then((res) => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.getUserList()
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
    showAddUserPage() {
      this.dialogVisible = !this.dialogVisible
    },
    showEditPage(id) {
      console.log(id)
      api.getSysUserById(id).then((res) => {
        if (res.code === 200 && res.data !== null) {
          this.sysUser = res.data
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
      let ids = [id]
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning',
      })
        .then(() => {
          api.removeUserByIds(ids).then((res) => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.getUserList()
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
      this.deleteObjects = val
    },
    changeUserStatus(user) {
      console.log(user)
      api.updateSysUser(user).then((res) => {
        this.$message({
          type: 'success',
          message: '修改成功!',
        })
      })
    },
    saveOrUpdate() {
      if (!this.sysUser.id) {
        this.createSysUser()
      } else {
        this.updateSysUser()
      }
    },
    updateSysUser() {
      this.$refs.dataForm.validate((valid) => {
        if (!valid) {
          return this.$message({
            type: 'error',
            message: '请先填写正确的信息',
          })
        }
        api.updateSysUser(this.sysUser).then((res) => {
          if (res.code == 200) {
            this.$message({
              type: 'success',
              message: '修改成功!',
            })
            this.dialogVisible = !this.dialogVisible
            this.getUserList()
            this.sysUser = {}
          } else {
            this.$message({
              type: 'error',
              message: res.message,
            })
          }
        })
      })
    },
    createSysUser() {
      this.$refs.dataForm.validate((valid) => {
        if (!valid) {
          this.$message({
            type: 'error',
            message: '请先填写信息',
          })
          return
        }
        api.addSysUser(this.sysUser).then((res) => {
          if (res.code == 200) {
            this.$message({
              type: 'success',
              message: '添加成功!',
            })
            this.dialogVisible = !this.dialogVisible
            this.getUserList()
            this.sysUser = {}
          } else {
            this.$message({
              type: 'error',
              message: res.message,
            })
          }
        })
      })
    },
    resetForm() {
      this.sysUser = {}
      this.$refs.dataForm.resetFields()
    },
    assignRole() {
      if(this.userRoleIds.length === 0) {
        this.$message.error("请分配角色")
        return
      }
      let assginRoleDTO = {
        userId: this.sysUser.id,
        roleIdList: this.userRoleIds,
      }
      api.assignRole(assginRoleDTO).then((res) => {
        this.$message.success(res.message || '分配角色成功')
        this.dialogRoleVisible = false
      })
    },
    showAssignRole(row) {
      this.sysUser = row
      this.dialogRoleVisible = !this.dialogRoleVisible
      api.getRoles(row.id).then((res) => {
        this.allRoles = res.data.sysRoles
        this.userRoleIds = res.data.roleIds
        this.checkAll = this.userRoleIds.length === this.allRoles.length
        this.isIndeterminate =
          this.userRoleIds.length > 0 &&
          this.userRoleIds.length < this.allRoles.length
      })
    },
    /*
    全选勾选状态发生改变的监听
    */
    handleCheckAllChange(value) {
      // value 当前勾选状态true/false
      // 如果当前全选, userRoleIds就是所有角色id的数组, 否则是空数组
      this.userRoleIds = value ? this.allRoles.map((item) => item.id) : []
      // 如果当前不是全选也不全不选时, 指定为false
      this.isIndeterminate = false
    },

    /*
    角色列表选中项发生改变的监听
    */
    handleCheckedChange(value) {
      const { userRoleIds, allRoles } = this
      this.checkAll =
        userRoleIds.length === allRoles.length && allRoles.length > 0
      this.isIndeterminate =
        userRoleIds.length > 0 && userRoleIds.length < allRoles.length
    },
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