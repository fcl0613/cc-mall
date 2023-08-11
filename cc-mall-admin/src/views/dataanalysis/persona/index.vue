<template>
  <div class="container">
    <div class="header">
      <el-button type="primary" @click="downLoadTemplate">下载模板</el-button>
      <el-upload
        style="margin: 0 6px"
        action=""
        :http-request="excelUpload"
        :before-upload="handlePreview"
        :show-file-list="false"
      >
        <el-button type="primary">点击上传</el-button>
        <!-- <div slot="tip" class="el-upload__tip">只能上传excel文件</div> -->
      </el-upload>
      <el-button type="primary" @click="showDialog">创建下载任务</el-button>
    </div>
    <div class="content">
      <el-card class="box-card">
        <div class="title">性别</div>
        <div class="pic-area" ref="genderArea"></div>
        <!-- <div class="no-data" v-show="hasData === false">暂无数据</div> -->
      </el-card>
      <el-card class="box-card">
        <div class="title">年龄</div>
        <div class="pic-area" ref="ageArea"></div>
        <!-- <div class="no-data" v-show="hasData === false">暂无数据</div> -->
      </el-card>
      <el-card class="box-card">
        <div class="title">职业</div>
        <div class="pic-area" ref="careerArea"></div>
        <!-- <div class="no-data" v-show="hasData === false">暂无数据</div> -->
      </el-card>
      <el-card class="box-card">
        <div class="title">是否有车</div>
        <div class="pic-area" ref="hasCarArea"></div>
        <!-- <div class="no-data" v-show="hasData === false">暂无数据</div> -->
      </el-card>
      <el-card class="box-card">
        <div class="title">是否有房</div>
        <div class="pic-area" ref="hasHomeArea"></div>
        <!-- <div class="no-data" v-show="hasData === false">暂无数据</div> -->
      </el-card>
      <el-card class="box-card">
        <div class="title">学历</div>
        <div class="pic-area" ref="educationLevelArea"></div>
        <!-- <div class="no-data" v-show="hasData === false">暂无数据</div> -->
      </el-card>
      <el-card class="box-card">
        <div class="title">来源地</div>
        <div class="pic-area" ref="countyOfOriginArea"></div>
        <!-- <div class="no-data" v-show="hasData === false">暂无数据</div> -->
      </el-card>
      <el-card class="box-card">
        <div class="title">收入</div>
        <div class="pic-area" ref="IncomeArea"></div>
        <!-- <div class="no-data" v-show="hasData === false">暂无数据</div> -->
      </el-card>
    </div>

    <el-dialog title="添加下载任务" :visible.sync="dialogVisible" width="30%">
      <el-form
        ref="stockFormRef"
        label-width="100px"
      >
        <el-form-item label="任务名" prop="jobName">
          <el-input v-model="jobName"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="subJob">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import personaApi from '@/api/dataanalysis/persona'
import { getToken } from '@/utils/auth'
import axios from 'axios'
export default {
  data() {
    return {
      hasData: false,
      dialogVisible: false,
      jobName: '',
      genderOption: {
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: 'Search Engine' },
              { value: 735, name: 'Direct' },
              { value: 580, name: 'Email' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      },
      ageOption: {
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      },
      careerOption: {
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      },
      hasCarOption: {
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: 'Search Engine' },
              { value: 735, name: 'Direct' },
              { value: 580, name: 'Email' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      },
      hasHomeOption: {
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: 'Search Engine' },
              { value: 735, name: 'Direct' },
              { value: 580, name: 'Email' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      },
      eduLevelOption: {
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      },
      originOption: {
        tooltip: {
          trigger: 'item',
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: 'Search Engine' },
              { value: 735, name: 'Direct' },
              { value: 580, name: 'Email' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      },
      incomeOption: {
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
          },
        ],
      },
    }
  },
  created() {
    this.getData()
  },
  methods: {
    initEcharts() {
      if (!this.hasData) return
      // console.log(222)
      let ot = this.$echarts.init(this.$refs.genderArea)
      ot.setOption(this.genderOption)
      let ageOt = this.$echarts.init(this.$refs.ageArea)
      ageOt.setOption(this.ageOption)
      let careerOt = this.$echarts.init(this.$refs.careerArea)
      careerOt.setOption(this.careerOption)
      let hasCarOt = this.$echarts.init(this.$refs.hasCarArea)
      hasCarOt.setOption(this.hasCarOption)
      let hasHomeOt = this.$echarts.init(this.$refs.hasHomeArea)
      hasHomeOt.setOption(this.hasHomeOption)
      let eduLevelOt = this.$echarts.init(this.$refs.educationLevelArea)
      eduLevelOt.setOption(this.eduLevelOption)
      let originOt = this.$echarts.init(this.$refs.countyOfOriginArea)
      originOt.setOption(this.originOption)
      let incomeOt = this.$echarts.init(this.$refs.IncomeArea)
      incomeOt.setOption(this.incomeOption)
    },
    getData() {
      personaApi.getPersonaData().then((res) => {
        if (res.data === null) return
        else {
          // console.log(res)
          // 性别
          let genderData = []
          for (const item of res.data.genderDimension) {
            genderData.push({ value: item.count, name: item.tag })
          }
          this.genderOption.series[0].data = genderData
          // 年龄
          let agex = []
          let agey = []
          for (const item of res.data.ageDimension) {
            agex.push(item.tag)
            agey.push(item.count)
          }
          this.ageOption.xAxis.data = agex
          this.ageOption.series[0].data = agey
          // 职业
          let careerx = []
          let careery = []
          for (const item of res.data.careerDimension) {
            careerx.push(item.tag)
            careery.push(item.count)
          }
          this.careerOption.xAxis.data = careerx
          this.careerOption.series[0].data = careery
          // 是否有车
          let carData = []
          for (const item of res.data.hasCarDimension) {
            carData.push({ value: item.count, name: item.tag })
          }
          this.hasCarOption.series[0].data = carData
          // 是否有房
          let homeData = []
          for (const item of res.data.hasHomeDimension) {
            homeData.push({ value: item.count, name: item.tag })
          }
          this.hasHomeOption.series[0].data = homeData
          // 教育等级
          let edux = []
          let eduy = []
          for (const item of res.data.educationalLevelDimension) {
            edux.push(item.tag)
            eduy.push(item.count)
          }
          this.eduLevelOption.xAxis.data = edux
          this.eduLevelOption.series[0].data = eduy
          // 来源地
          let originData = []
          for (const item of res.data.countryOfOriginDimension) {
            originData.push({ value: item.count, name: item.tag })
          }
          this.originOption.series[0].data = originData
          // 收入情况
          let incomex = []
          let incomey = []
          for (const item of res.data.incomeDimension) {
            incomex.push(item.tag)
            incomey.push(item.count)
          }
          this.incomeOption.xAxis.data = incomex
          this.incomeOption.series[0].data = incomey

          this.hasData = true
          this.initEcharts()
        }
      })
    },
    downLoadTemplate() {
      axios({
        method: 'GET',
        url: process.env.VUE_APP_BASE_API + '/excel/template/download',
        responseType: 'blob',
        headers: { token: getToken() },
      })
        .then((res) => {
          console.log(res)
          const { data, headers } = res
          const blob = new Blob([data], { type: headers['content-type'] })
          let dom = document.createElement('a')
          let url = window.URL.createObjectURL(blob)
          dom.href = url
          dom.style.display = 'none'
          document.body.appendChild(dom)
          dom.click()
          dom.parentNode.removeChild(dom)
          window.URL.revokeObjectURL(url)
        })
        .catch((err) => {
          this.$message.error(err)
        })
    },
    handlePreview(file) {
      const isXlS =
        file.type ===
          'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
        file.type === 'application/vnd.ms-excel'
      if (!isXlS) {
        this.$message.error('仅支持xls、xlsx格式的文件!')
      }
      return isXlS
    },
    async excelUpload(options) {
      try {
        let file = options.file
        let sendData = new FormData()
        sendData.append('Content-Type', 'multipart/form-data;charset=utf-8')
        sendData.append('file', file)
        axios
          .post(process.env.VUE_APP_BASE_API + '/excel/upload', sendData, {
            headers: {
              token: getToken()
            }
          })
          .then((res) => {
            console.log(res)
            if (res.status === 200) {
              this.$message.success('上传成功')
            } else {
              this.$message({
                message: '上传失败',
                type: 'error',
              })
            }
          })
      } catch (e) {
        this.$message({
          message: '上传失败',
          type: 'error',
        })
      }
    },
    showDialog() {
      this.dialogVisible = !this.dialogVisible
      this.jobName = ''
    },
    subJob() {
      personaApi.createJob(this.jobName).then((res) => {
        this.$message.success(res.message)
        this.dialogVisible = !this.dialogVisible
      })
    }
  },
  mounted() {
    this.initEcharts()
  },
}
</script>

<style lang="less" scoped>
.container {
  padding: 5px;
  .header {
    display: flex;
    justify-content: flex-end;
  }
  .content {
    margin-top: 10px;
    margin-left: 30px;
    display: flex;
    flex-flow: wrap;
    justify-content: flex-start;
    .box-card {
      margin-right: 5px;
      margin-bottom: 20px;
      width: 32%;
      height: 350px;
      .pic-area {
        width: 100%;
        height: 320px;
      }
      .no-data {
        margin-top: 100px;
        display: flex;
        justify-content: center;
      }
    }
  }
}
</style>