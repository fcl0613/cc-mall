<template>
  <div class="container">
    <el-card class="box-card">
      <el-form
        ref="dataForm"
        :model="category"
        :rules="rules"
        label-width="180px"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="category.name" ></el-input>
        </el-form-item>
        <el-form-item label="父级分类">
          <el-select v-model="category.parentId" placeholder="">
            <el-option label="无父级分类" :value="0"></el-option>
            <el-option
              v-for="(item, index) in parentList"
              :key="index"
              :label="item.categoryName"
              :value="item.categoryId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model.number="category.sort"></el-input>
        </el-form-item>
        <el-form-item label="是否显示">
          <el-radio v-model="category.showStatus" :label="1">是</el-radio>
          <el-radio v-model="category.showStatus" :label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="是否显示在导航栏">
          <el-radio v-model="category.navStatus" :label="1">是</el-radio>
          <el-radio v-model="category.navStatus" :label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="分类图标">
          <el-upload
            action=""
            list-type="picture"
            :show-file-list="false"
            :http-request="fileUpload"
            :on-success="handleUploadSuccess"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过500kb
            </div>
          </el-upload>
        </el-form-item>
        <div class="pre-image" v-if="resUrl">
          <img :src="resUrl" alt="" />
        </div>
        <el-form-item label="分类描述" prop="description">
          <el-input v-model="category.description"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import aliyunOSSApi from '@/api/aliyunOSS/index'
import categoryApi from '@/api/product/category'
export default {
  data() {
    return {
      categoryId: this.$route.query.categoryId,
      category: {
        id: null,
        parentId: 0,
        name: '',
        navStatus: 0,
        showStatus: 0,
        sort: null,
        icon: '',
        description: '',
      },
      parentList: [],
      aliyunOssInfo: {},
      resUrl: '',
      rules: {
        name: [
          { required: true, message: '请输入分类名', trigger: 'blur' },
        ],
        sort: [
          { required: true, message: '请输入排序编号', trigger: 'blur' },
          { type: 'number', message: '排序编号必须为数字', trigger: 'blur' },
        ],
        description: [
          { required: true, message: '请输入分类描述', trigger: 'blur' },
        ],
      }
    }
  },
  mounted() {
    this.getCategory()
    this.getAliyunOssPolicy()
    this.getAllParentList()
  },
  methods: {
    // 获取所有的父级分类
    getAllParentList() {
      categoryApi.getAllParentList().then((res) => {
        this.parentList = res.data
      })
    },
    // 当是更新分类的时候传入id 会根据编号找到对应分类的信息
    getCategory() {
      if (this.categoryId) {
        categoryApi.getCategoryDetail(this.categoryId).then((res) => {
          this.category = res.data
        })
      }
    },
    // 获取阿里云OSS签名
    getAliyunOssPolicy() {
      aliyunOSSApi.getAliyunOssPolicy().then((res) => {
        this.aliyunOssInfo = res.data
      })
    },
    // 提交表单
    onSubmit() {
      this.$refs.dataForm.validate((valid) => {
        if (!valid) {
          this.$message({
            type: 'error',
            message: '请先填写信息',
          })
          return
        }
        if (!this.categoryId && this.resUrl === '' && this.category.parentId !== 0) {
          this.$message({
            type: 'error',
            message: '请先上传图片',
          })
          return
        }
        // 判断是不是分类的编辑操作
        if (this.categoryId) {
          // 更新
          categoryApi.updateCategory(this.category).then((res) => {
            if (res.code === 200) {
              let that = this
              this.$message({
                message: '更新成功',
                type: 'success',
                onClose: () => {
                  that.$router.push('/product/category')
                },
              })
            } else {
              this.$message({
                message: '更新失败',
                type: 'error',
              })
            }
          })
        } else {
          // 添加
          categoryApi.addCategory(this.category).then((res) => {
            if (res.code === 200) {
              let that = this
              this.$message({
                message: '添加成功',
                type: 'success',
                onClose: () => {
                  that.$router.push('/product/category')
                },
              })
            } else {
              this.$message({
                message: '添加失败',
                type: 'error',
              })
            }
          })
        }
      })
    },
    cancel() {
      this.$router.push('/product/category')
    },
    // 处理图片上传成功后的代码
    handleUploadSuccess() {},
    // 自定义文件上传策略
    async fileUpload(options) {
      try {
        let file = options.file
        let accessUrl =
          this.aliyunOssInfo.dir + '/' + new Date().getTime() + file.name
        let sendData = new FormData()
        console.log(accessUrl)
        sendData.append('key', accessUrl)
        sendData.append('ossaccessKeyId', this.aliyunOssInfo.accessKeyId)
        sendData.append('policy', this.aliyunOssInfo.policy)
        sendData.append('signature', this.aliyunOssInfo.signature)
        sendData.append('success_action_status', 200)
        sendData.append('Content-Type', 'image/png')
        sendData.append('file', file)
        this.$http.post(this.aliyunOssInfo.host, sendData).then((res) => {
          if (res.status === 200) {
            this.$message({
              message: '图片上传成功',
              type: 'success',
            })
            this.resUrl = this.aliyunOssInfo.host + '/' + accessUrl
            this.category.icon = this.resUrl
          } else {
            this.$message({
              message: '图片上传失败',
              type: 'success',
            })
          }
        })
      } catch (e) {
        options.onError('上传失败')
        this.$message({
          message: '图片上传失败',
          type: 'success',
        })
      }
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  .box-card {
    margin-top: 50px;
    width: 800px;
    padding: 50px;
    .pre-image {
      margin-bottom: 22px;
      display: flex;
      justify-content: center;
      img {
        width: 100px;
        height: 100px;
        border: solid 1px #383838;
      }
    }
  }
}
</style>