<template>
  <div class="container">
    <el-card class="card">
      <el-form
        :model="couponForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
      >
        <el-form-item label="优惠券名称" prop="name">
          <el-input v-model="couponForm.name"></el-input>
        </el-form-item>
        <el-form-item label="优惠券类型" prop="type">
          <el-select v-model="couponForm.type" placeholder="请选择">
            <el-option
              v-for="item in couponTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="总发行量" prop="couponCount">
          <el-input v-model.number="couponForm.couponCount"></el-input>
        </el-form-item>
        <el-form-item label="面额" prop="amount">
          <el-input v-model="couponForm.amount"></el-input>
        </el-form-item>
        <el-form-item label="每人限领" prop="perLimit">
          <el-input v-model.number="couponForm.perLimit"></el-input>
        </el-form-item>
        <el-form-item label="使用门槛" prop="minPoint">
          <el-input v-model="couponForm.minPoint"></el-input>
        </el-form-item>
        <el-form-item label="有效期">
          <el-date-picker
            v-model="couponForm.startTime"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="mini"
          >
          </el-date-picker>
          至
          <el-date-picker
            v-model="couponForm.endTime"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd HH:mm:ss"
            size="mini"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="可使用商品">
          <el-radio-group @change="useTypeChange" v-model="couponForm.useType">
            <el-radio-button :label="0">全场通用</el-radio-button>
            <el-radio-button :label="1">指定分类</el-radio-button>
            <el-radio-button :label="2">指定商品</el-radio-button>
          </el-radio-group>
          <div class="usetype-area" v-if="couponForm.useType === 1">
            <div class="usetype-header">
              <el-cascader
                v-model="cascaderValue"
                :options="categoryList"
                @change="handleChange"
                placeholder="请选择"
                size="mini"
                ref="cascaderCategoryRef"
              ></el-cascader>
              <el-button size="mini" @click="addToCategoryTable"
                >添加</el-button
              >
            </div>
            <div class="usetype-table">
              <el-table
                :data="categoryTableDataList"
                border
                style="width: 100%"
              >
                <el-table-column prop="name" label="分类名称" width="350">
                </el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button type="text" @click="removeCategory(scope.row)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
          <div class="usetype-area" v-if="couponForm.useType === 2">
            <div class="usetype-header">
              <el-autocomplete
                v-model="state"
                :fetch-suggestions="querySearchAsync"
                placeholder="请输入内容"
                @select="handleSelect"
              ></el-autocomplete>
              <el-button size="mini" @click="addToProductTable">添加</el-button>
            </div>
            <div class="usetype-table">
              <el-table :data="productRelationList" border style="width: 100%">
                <el-table-column
                  prop="productName"
                  label="商品名称"
                  width="350"
                >
                </el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button type="text" @click="removeProduct(scope.row)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="couponForm.note"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">立即创建</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import categoryApi from '@/api/product/category'
import productApi from '@/api/product/product'
import couponApi from '@/api/marketing/coupon'
export default {
  data() {
    const checkPrice = (rule, value, cb) => {
      const priceReg = /^-?\d+(,\d{3})*(\.\d{1,2})?$/
      if (priceReg.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的价格'))
    }
    const checkNumber = (rule, value, cb) => {
      const numberReg = /^\+?[1-9]\d*$/
      if (numberReg.test(value)) {
        return cb()
      }
      cb(new Error('请输入正确的数字'))
    }
    return {
      couponForm: {
        type: 0,
        perLimit: 1,
        useType: 0,
      },
      rules: {
        name: [
          { required: true, message: '请输入优惠券名称', trigger: 'blur' },
        ],
        type: [
          { required: true, message: '请选择优惠券类型', trigger: 'blur' },
        ],
        couponCount: [
          { required: true, message: '请输入总发行量', trigger: 'blur' },
          { validator: checkNumber, trigger: 'blur' },
        ],
        amount: [
          { required: true, message: '请输入面额', trigger: 'blur' },
          { validator: checkPrice, trigger: 'blur' },
        ],
        perLimit: [
          { required: true, message: '请输入每人限领', trigger: 'blur' },
          { validator: checkNumber, trigger: 'blur' },
        ],
        minPoint: [
          { required: true, message: '请输入使用门槛', trigger: 'blur' },
          { validator: checkPrice, trigger: 'blur' },
        ]
      },
      couponTypeOptions: [
        { label: '全场赠券', value: 0 },
        { label: '会员赠券', value: 1 },
        { label: '购物赠券', value: 2 },
        { label: '注册赠券', value: 3 },
      ],
      cascaderValue: [],
      categoryList: [],
      categoryRelationList: [], // 后端对接的数据模型
      categoryTableDataList: [], // 页面展示数据模型
      productRelationList: [], // 后端对接数据模型 也可以用作前端展示
      state: '',
      restaurants: [],
      timeout: 3000,
      currentSelectProduct: {},
    }
  },
  created() {
    this.initCategorySelect()
  },
  methods: {
    submitForm() {
      this.$refs.ruleForm.validate((valid) => {
        if (!valid) {
          return this.$message({
            type: 'error',
            message: '请先填写正确的信息',
          })
        }
        if(!this.couponForm.startTime || !this.couponForm.endTime) {
            return this.$message.warning('请指定起始时间')
        }
        if (this.couponForm.useType === 1 && this.categoryRelationList.length === 0) {
          return this.$message.warning('请选择指定分类')
        }
        if (this.couponForm.useType === 2 && this.productRelationList.length === 0) {
          return this.$message.warning('请选择指定商品')
        }
        if (this.couponForm.useType === 1) {
          this.couponForm.categoryRelationList = this.categoryRelationList
        }
        if (this.couponForm.useType === 2) {
          this.couponForm.productRelationList = this.productRelationList
        }
        couponApi.addCoupon(this.couponForm).then((res) => {
          this.$message.success(res.message)
          this.$router.push({
              path: '/marketing/coupon'
          })
        })
      })
    },
    resetForm() {
      this.couponForm = {
        type: 0,
        perLimit: 1,
        useType: 0,
      }
      this.categoryRelationList = []
      this.categoryTableDataList = []
      this.productRelationList = []
    },
    initCategorySelect() {
      categoryApi.getAllCategoryList().then((res) => {
        this.categoryList = res.data
      })
    },
    removeCategory(row) {
      // console.log(row)
      this.categoryRelationList = this.categoryRelationList.filter(
        (val) => val.productCategoryId !== row.id
      )
      this.categoryTableDataList = this.categoryTableDataList.filter(
        (val) => val.id !== row.id
      )
    },
    handleChange(val) {
      console.log(val)
    },
    addToCategoryTable() {
      let temp = this.$refs['cascaderCategoryRef'].getCheckedNodes()
      if (temp.length === 0) {
        this.$message.warning('请先选择分类')
        return
      }
      // console.log(temp)
      let cname = temp[0].pathLabels[1]
      let pname = temp[0].pathLabels[0]
      let id = temp[0].path[1]
      this.categoryRelationList.push({
        productCategoryId: id,
        productCategoryName: cname,
        parentCategoryName: pname,
      })
      this.categoryTableDataList.push({ id: id, name: pname + '/' + cname })
    },
    useTypeChange(val) {
      //   console.log(val)
      // 这里只要用户选择了那么就置空数据
      this.categoryRelationList = []
      this.categoryTableDataList = []
      this.productRelationList = []
    },
    handleSelect(val) {
      console.log('handleSelect', val)
      this.currentSelectProduct = val
    },
    querySearchAsync(queryString, cb) {
      clearTimeout(this.timeout)
      if (queryString === '') {
        cb([])
        return
      }
      productApi.findAll(queryString).then((res) => {
        let rest = []
        if (res.data.length > 0) {
          for (const item of res.data) {
            rest.push({ value: item.productName, id: item.id })
          }
        }
        this.timeout = setTimeout(() => {
          cb(rest)
        }, 3000 * Math.random())
      })
    },
    addToProductTable() {
      this.productRelationList.push({
        productId: this.currentSelectProduct.id,
        productName: this.currentSelectProduct.value,
      })
    },
    removeProduct(row) {
      this.productRelationList = this.productRelationList.filter(
        (val) => val.productId !== row.productId
      )
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  display: flex;
  justify-content: center;
  .card {
    padding: 10px 90px;
    margin-top: 20px;
    width: 800px;
    .usetype-area {
      margin-top: 10px;
      .usetype-header {
        .el-button {
          margin-left: 6px;
        }
      }
      .usetype-table {
        margin-top: 10px;
      }
    }
  }
}
</style>