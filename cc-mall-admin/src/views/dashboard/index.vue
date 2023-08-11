<template>
  <div class="dashboard-container">
    <div class="base-info">
      <el-descriptions title="数据统计" direction="vertical" :column="4" border>
        <el-descriptions-item label="今日订单数">{{
          baseInfo.todayOrderCount
        }}</el-descriptions-item>
        <el-descriptions-item label="今日总营业额">{{
          baseInfo.todayIncome
        }}</el-descriptions-item>
        <el-descriptions-item label="昨日总营业额">{{
          baseInfo.yesterdayIncome
        }}</el-descriptions-item>
        <el-descriptions-item label="待付款订单">{{
          baseInfo.obligationOrderCount
        }}</el-descriptions-item>
        <el-descriptions-item label="待发货订单">{{
          baseInfo.waitingFoDeliveryOrderCount
        }}</el-descriptions-item>
        <el-descriptions-item label="待收货订单">{{
          baseInfo.deliveredOrderCount
        }}</el-descriptions-item>
        <el-descriptions-item label="已完成订单">{{
          baseInfo.finishedOrderCount
        }}</el-descriptions-item>
        <el-descriptions-item label="售后申请订单">{{
          baseInfo.waitingOrderApplyCount
        }}</el-descriptions-item>
        <el-descriptions-item label="商品总数">{{
          baseInfo.productCount
        }}</el-descriptions-item>
        <el-descriptions-item label="上架商品数">{{
          baseInfo.productPublishCount
        }}</el-descriptions-item>
        <el-descriptions-item label="下架商品数">{{
          baseInfo.productUnPublishCount
        }}</el-descriptions-item>
        <el-descriptions-item label="用户总数">{{
          baseInfo.customerCount
        }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <div class="order-table-area">
      <div ref="orderTable" class="order-table"></div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import dashboardApi from '@/api/dashboard'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters(['name']),
  },
  data() {
    return {
      baseInfo: {},
      orderTableData: [],
      dateTime: [],
      orderCount: [],
      orderIncome: [],
      option: {
        title: {
          text: '订单统计',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['订单数量', '订单金额'],
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '订单数量',
            type: 'line',
            stack: 'Total',
            data: [120, 132, 101, 134, 90, 230, 210],
          },
          {
            name: '订单金额',
            type: 'line',
            stack: 'Total',
            data: [220, 182, 191, 234, 290, 330, 310],
          },
        ],
      },
    }
  },
  created() {
    this.getBaseInfo()
    this.getOrderTable()
  },
  methods: {
    getBaseInfo() {
      dashboardApi.getBaseInfo().then((res) => {
        // console.log(res)
        this.baseInfo = res.data
      })
    },
    getOrderTable() {
      dashboardApi.getOrderTable().then((res) => {
        this.orderTableData = res.data
        let dateTime = []
        let orderCount = []
        let orderIncome = []
        for (const item of this.orderTableData) {
          dateTime.push(item.dateTime)
          orderCount.push(item.orderCount)
          orderIncome.push(item.income)
        }
        this.option.xAxis.data = dateTime
        this.option.series[0].data = orderCount
        this.option.series[1].data = orderIncome
        this.initEcharts()
      })
    },
    initEcharts() {
      let ot = this.$echarts.init(this.$refs.orderTable)
      ot.setOption(this.option)
    },
  },
  mounted() {
    this.initEcharts()
  },
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
    .order-table-area {
      margin-top: 50px;
      .order-table {
        width: 100%;
        height: 600px;
      }
    }
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
