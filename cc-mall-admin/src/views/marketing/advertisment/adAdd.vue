<template>
  <div class="container">
    <el-card>
      <el-form
        :model="adForm"
        :rules="rules"
        ref="adFormRef"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="广告名称" prop="adName">
          <el-input v-model="adForm.adName"></el-input>
        </el-form-item>
        <el-form-item label="开始时间" prop="beginTime">
          <el-date-picker
            value-format="yyyy-MM-dd HH:mm:ss"
            v-model="adForm.beginTime"
            type="date"
            placeholder="选择日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            value-format="yyyy-MM-dd HH:mm:ss"
            v-model="adForm.endTime"
            type="date"
            placeholder="选择日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="上线/下线">
          <el-radio v-model="adForm.publishStatus" :label="0">下线</el-radio>
          <el-radio v-model="adForm.publishStatus" :label="1">上线</el-radio>
        </el-form-item>
        <el-form-item label="广告图片">
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
          <el-image
            style="width: 100px; height: 100px"
            v-if="imageUrl"
            :src="imageUrl"
            fit="contain"
          ></el-image>
        </el-form-item>
        <el-form-item label="广告链接" prop="adLink">
          <el-input v-model="adForm.adLink"></el-input>
        </el-form-item>
        <el-form-item label="广告备注">
          <el-input type="textarea" v-model="adForm.adNote"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import aliyunOSSApi from '@/api/aliyunOSS/index'
import adApi from '@/api/marketing/advertis'
export default {
  data() {
    const checkLink = (rule, value, cb) => {
      const LinkReg =
        /^(((ht|f)tps?):\/\/)?([^!@#$%^&*?.\s-]([^!@#$%^&*?.\s]{0,63}[^!@#$%^&*?.\s])?\.)+[a-z]{2,6}\/?/
      if (LinkReg.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的网址'))
    }
    return {
      adForm: {
        publishStatus: 0,
      },
      rules: {
        adName: [{ required: true, message: '请输入', trigger: 'blur' }],
        beginTime: [
          { required: true, message: '请选择开始时间', trigger: 'blur' },
        ],
        endTime: [
          { required: true, message: '请选择结束时间', trigger: 'blur' },
        ],
        adLink: [
          { required: true, message: '请输入广告链接', trigger: 'blur' },
          { validator: checkLink, trigger: 'blur' },
        ],
      },
      aliyunOssInfo: {},
      imageUrl: '',
    }
  },
  created() {
    this.getAliyunOssPolicy()
  },
  methods: {
    handleUploadSuccess() {},
    getAliyunOssPolicy() {
      aliyunOSSApi.getAliyunOssPolicy().then((res) => {
        this.aliyunOssInfo = res.data
      })
    },
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
            this.imageUrl = this.aliyunOssInfo.host + '/' + accessUrl
            this.adForm.adPic = this.imageUrl
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
    onSubmit() {
      this.$refs.adFormRef.validate((valid) => {
        if (!valid) {
          this.$message({
            type: 'error',
            message: '请先填写信息',
          })
          return
        }
        adApi.addAdvertis(this.adForm).then((res) => {
            this.$message.success(res.message)
            this.cancel()
        })
      })
    },
    cancel() {
      this.$router.push('/marketing/advertise')
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  padding: 15px;
  display: flex;
  justify-content: center;
  .el-card {
    width: 50%;
    padding-right: 100px;
  }
}
</style>