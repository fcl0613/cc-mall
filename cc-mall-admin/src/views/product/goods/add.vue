<template>
  <div class="container">
    <el-card class="box-card">
      <el-button @click="retback">返回</el-button>
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="填写商品信息"></el-step>
        <el-step title="填写商品促销"></el-step>
        <el-step title="填写商品属性"></el-step>
      </el-steps>
      <div v-if="active === 0">
        <div class="form-area">
          <el-form
            :model="productForm"
            :rules="rules"
            ref="ruleForm"
            label-width="100px"
          >
            <el-form-item label="商品名称" prop="productName">
              <el-input v-model="productForm.productName"></el-input>
            </el-form-item>
            <el-form-item label="商品分类" prop="categoryId">
              <el-cascader
                v-model="cascaderValue"
                :options="categoryList"
                @change="handleChange"
                placeholder="请选择"
              ></el-cascader>
            </el-form-item>
            <el-form-item label="副标题" prop="subtitle">
              <el-input v-model="productForm.subtitle"></el-input>
            </el-form-item>
            <el-form-item label="商品介绍" prop="description">
              <el-input
                type="textarea"
                :rows="2"
                v-model="productForm.description"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
            <el-form-item label="商品库存">
              <el-input v-model.number="productForm.stock"></el-input>
            </el-form-item>
            <el-form-item label="商品售价" prop="price">
              <el-input v-model="productForm.price"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div v-if="active === 1">
        <div class="form-area">
          <el-form
            :model="productForm"
            :rules="rules"
            ref="ruleForm"
            label-width="100px"
          >
            <el-form-item label="赠送积分">
              <el-input v-model.number="productForm.giftPoint"></el-input>
            </el-form-item>
            <el-form-item label="商品上架">
              <el-switch
                v-model="productForm.publishStatus"
                :active-value="0"
                :inactive-value="1"
              >
              </el-switch>
            </el-form-item>
            <el-form-item label="商品推荐">
              <span style="margin-right: 6px">新品</span>
              <el-switch
                v-model="productForm.newStatus"
                :active-value="1"
                :inactive-value="0"
              >
              </el-switch>
              <span style="margin-right: 6px; margin-left: 16px">推荐</span>
              <el-switch
                v-model="productForm.recommendStatus"
                :active-value="1"
                :inactive-value="0"
              >
              </el-switch>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div v-if="active === 2">
        <div class="form-area">
          <el-form
            :model="productForm"
            :rules="rules"
            ref="ruleForm"
            label-width="100px"
          >
            <el-form-item label="商品封面">
              <el-upload
                action=""
                :http-request="fileUpload"
                :on-success="handleCoverUploadSuccess"
                :file-list="productCover"
                :on-remove="handleCoverRemove"
                :limit="4"
                list-type="picture"
              >
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png文件，且不超过2MB
                </div>
              </el-upload>
            </el-form-item>
            <el-form-item label="商品详情">
              <el-upload
                action=""
                :http-request="fileUpload1"
                :on-success="handlePicUploadSuccess"
                :file-list="productPics"
                :on-remove="handlePicRemove"
                list-type="picture"
              >
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png文件，且不超过2MB
                </div>
              </el-upload>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="btn-group">
        <el-button v-if="active !== 0" @click="pre">上一步</el-button>
        <el-button v-if="active !== 2" @click="next">下一步</el-button>
        <el-button v-if="active === 2" @click="subFrom">提交</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import categoryApi from '@/api/product/category'
import aliyunOSSApi from '@/api/aliyunOSS/index'
import productApi from '@/api/product/product'
export default {
  data() {
    return {
      active: 0,
      productForm: {
        stock: 0,
        price: 0,
        giftPoint: 0,
        publishStatus: 1,
        newStatus: 0,
        recommendStatus: 0,
      },
      rules: {
        productName: [
          { required: true, message: '请输入商品名称', trigger: 'blur' },
        ],
        categoryId: [
          { required: true, message: '请选择商品分类', trigger: 'blur' },
        ],
        subtitle: [
          { required: true, message: '请输入副标题', trigger: 'blur' },
        ],
        description: [
          { required: true, message: '请输入商品介绍', trigger: 'blur' },
        ],
      },
      cascaderValue: [],
      categoryList: [],
      productCover: [],
      productPics: [],
      aliyunOssInfo: {},
      coverMap: new Map(),
      picsMap: new Map(),
      fileName: '',
    }
  },
  created() {
    this.initCategorySelect()
    this.getAliyunOssPolicy()
  },
  methods: {
    next() {
      this.$refs.ruleForm.validate((valid) => {
        if (!valid) {
          this.$message({
            type: 'error',
            message: '请先填写信息',
          })
          return
        } else {
          if (this.active++ > 2) this.active = 0
        }
      })
    },
    pre() {
      if (this.active - 1 >= 0) this.active = this.active - 1
    },
    handleChange(val) {
      console.log(val)
      this.productForm.categoryIdParent = val[0]
      this.productForm.categoryId = val[1]
      console.log(this.productForm.categoryId)
    },
    initCategorySelect() {
      categoryApi.getAllCategoryList().then((res) => {
        this.categoryList = res.data
      })
    },
    handleRemove(file, productCover) {
      console.log(file, productCover)
    },
    // 获取阿里云OSS签名
    getAliyunOssPolicy() {
      aliyunOSSApi.getAliyunOssPolicy().then((res) => {
        this.aliyunOssInfo = res.data
      })
    },
    // 自定义文件上传策略
    async fileUpload(options) {
      try {
        let file = options.file
        let fileName = new Date().getTime() + file.name
        let accessUrl = this.aliyunOssInfo.dir + '/' + fileName
        let dirList = this.aliyunOssInfo.dir.split('/')
        this.fileName = dirList[dirList.length - 1] + '/' + fileName
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
          } else {
            this.$message({
              message: '图片上传失败',
              type: 'error',
            })
          }
        })
      } catch (e) {
        options.onError('上传失败')
        this.$message({
          message: '图片上传失败',
          type: 'error',
        })
      }
    },
    async fileUpload1(options) {
      try {
        let file = options.file
        let fileName = new Date().getTime() + file.name
        let accessUrl = this.aliyunOssInfo.dir + '/' + fileName
        let dirList = this.aliyunOssInfo.dir.split('/')
        this.fileName = dirList[dirList.length - 1] + '/' + fileName
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
            let productPics = this.productPics
            for (const item of productPics) {
              if (!this.picsMap.has(item.uid)) {
                this.picsMap.set(item.uid, fileName)
              }
            }
          } else {
            this.$message({
              message: '图片上传失败',
              type: 'error',
            })
          }
        })
      } catch (e) {
        options.onError('上传失败')
        this.$message({
          message: '图片上传失败',
          type: 'error',
        })
      }
    },
    handleCoverUploadSuccess(response, file, fileList) {
      //   console.log(response, file, fileList)
      for (const item of fileList) {
        if (!this.coverMap.has(item.uid)) {
          this.coverMap.set(item.uid, this.fileName)
        }
      }
    },
    handlePicUploadSuccess(response, file, fileList) {
      for (const item of fileList) {
        if (!this.picsMap.has(item.uid)) {
          this.picsMap.set(item.uid, this.fileName)
        }
      }
    },
    handleCoverRemove(file, fileList) {
      console.log(file, fileList)
      this.coverMap.delete(file.uid)
    },
    handlePicRemove(file, fileList) {
      this.picsMap.delete(file.uid)
    },
    retback() {
      this.$router.push('/product/goods')
    },
    subFrom() {
      let coverList = new Array()
      for (let [key, value] of this.coverMap) {
        console.log(key + ' = ' + value)
        coverList.push(value)
      }
      let picList = new Array()
      for (let [key, value] of this.picsMap) {
        console.log(key + ' = ' + value)
        picList.push(value)
      }
      this.productForm.productCover = coverList
      this.productForm.productPics = picList
      this.$refs.ruleForm.validate((valid) => {
        if (!valid) {
          this.$message({
            type: 'error',
            message: '请先填写信息',
          })
          return
        }
        productApi.addProduct(this.productForm).then((res) => {
            this.$message.success(res.message)
            this.retback()
        })
      })
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
    width: 1200px;
    padding: 50px;
    .form-area {
      width: 100%;
      padding: 20px 250px 20px 100px;
    }
    .btn-group {
      display: flex;
      justify-items: center;
      align-items: center;
      padding-left: 160px;
    }
  }
}
</style>