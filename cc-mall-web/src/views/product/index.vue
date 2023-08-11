<template>
  <div class="container">
    <div class="header">
      <div class="rebanck" @click="reback">
        <van-icon name="arrow-left" />
      </div>
      <div class="search-area">
        <van-field
          v-model="searchParams.keyword"
          left-icon="search"
          placeholder="请输入搜索内容"
        />
        <div class="search-btn" @click="onRefresh">搜索</div>
      </div>
    </div>
    <div class="banner">
      <!-- :style="[ defaultFlag ? 'font-weight: bold;' : '' ]" -->
      <div
        class="default"
        :style="{ fontWeight: defaultFlag ? 'bold' : '' }"
        @click="changeDefaultFlag"
      >
        默认
      </div>
      <div class="price" @click="changePriceFlag">
        <span :style="{ fontWeight: priceSortFlag !== null ? 'bold' : '' }"
          >价格</span
        >
        <div class="sort">
          <van-icon
            :style="{ fontWeight: priceSortFlag ? 'bold' : '' }"
            size="8px"
            name="arrow-up"
          />
          <van-icon
            :style="{ fontWeight: priceSortFlag === false ? 'bold' : '' }"
            size="8px"
            name="arrow-down"
          />
        </div>
      </div>
      <div
        class="has-stock"
        :style="{ fontWeight: stockFlag ? 'bold' : '' }"
        @click="changeStockFlag"
      >
        仅看有货
      </div>
    </div>
    <div class="data-area">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
        >
          <GoodsList :goodsList="productList"></GoodsList>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script>
import productApi from '@/api/product'
import GoodsList from '@/components/goodsList'
export default {
  components: {
    GoodsList,
  },
  data() {
    return {
      defaultFlag: true,
      priceSortFlag: null,
      stockFlag: false,
      searchParams: {
        keyword: '',
        categoryId: this.$route.query.categoryId,
        pageNum: 0,
        pageSize: 10,
        stockFlag: false,
      },
      loading: false,
      finished: false,
      refreshing: false,
      productList: [],
    }
  },
  methods: {
    reback() {
      this.$router.go(-1)
    },
    getProductList() {
      productApi.getProductList(this.searchParams).then((res) => {
        this.productList.push(...res.data.list)
        if (res.data.total === this.productList.length) {
          this.finished = true
        }
        this.refreshing = false
        this.loading = false
      })
    },
    changeDefaultFlag() {
      this.defaultFlag = !this.defaultFlag
      this.onRefresh()
    },
    changePriceFlag() {
      if (this.priceSortFlag === null) {
        this.priceSortFlag = true
      } else {
        this.priceSortFlag = !this.priceSortFlag
      }
      this.searchParams.priceFlag = this.priceSortFlag
      this.onRefresh()
    },
    changeStockFlag() {
      this.stockFlag = !this.stockFlag
      this.onRefresh()
    },
    onRefresh() {
      // 清空列表数据
      this.finished = false

      // 重新加载数据
      // 将 loading 设置为 true，表示处于加载状态
      this.loading = true
      this.refreshing = true
      this.searchParams.pageNum = 0
      this.searchParams.pageSize = 10
      this.onLoad()
    },
    onLoad() {
      console.log(2222)
      if (this.refreshing) {
        this.productList = []
        this.refreshing = false
      }
      this.searchParams.pageNum = this.searchParams.pageNum + 1
      this.getProductList()
    },
  },
}
</script>

<style lang="less" scoped>
.container {
  background-color: #f7f7f7;
  .header {
    padding-top: 10px;
    padding-left: 10px;
    display: flex;
    justify-content: baseline;
    background-color: #fff;
    padding-bottom: 6px;
    .rebanck {
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .search-area {
      margin-left: 6px;
      display: flex;
      width: 100%;
      .van-field {
        padding: 3px 10px;
        border: 1px solid rgb(163, 163, 163);
        border-radius: 20px;
        margin-right: 3px;
        width: 70%;
      }
      .search-btn {
        font-size: 16px;
        margin-left: 6px;
        // text-align: 16px;
        line-height: 32px;
      }
    }
  }
  .banner {
    font-size: 16px;
    display: flex;
    justify-content: space-between;
    padding: 3px 20px;
    margin-top: 1px;
    background-color: #fff;
    line-height: 32px;
    .price {
      display: flex;
      .sort {
        margin-left: 1px;
        display: flex;
        flex-flow: column;
        justify-content: center;
      }
    }
  }
  .data-area {
    margin-top: 10px;
  }
}
</style>