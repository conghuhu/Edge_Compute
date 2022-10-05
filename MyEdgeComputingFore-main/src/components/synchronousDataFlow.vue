<template>
  <div style="">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <el-icon><menu /></el-icon> 基础表格
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      
      <el-table
        style="padding:0%"
        :data="tableData"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="executionTime"
          label="执行时间"
          width="280"
        ></el-table-column>
        <el-table-column prop="fromData" label="数据来源" width="280"></el-table-column>
        <el-table-column prop="toData" label="数据去向" width="280"></el-table-column>
        <el-table-column prop="status" label="状态" align="center">
        </el-table-column>
        <!-- <el-table-column label="操作" width="180" align="center">
                    <template #default="scope">
                        <el-button type="text" icon="el-icon-delete" class="red"
                            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column> -->
      </el-table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import { dataFlow } from "~/axios/globalInterface";
import { getUserInfo } from "~/axios/api";
export default defineComponent({
  setup() {
    const tableData = reactive<dataFlow[]>([]);
    onMounted(async () => {
      const res = await getUserInfo();
      res.forEach((item) => {
        tableData.push(item);
      });
    });
    return {
      tableData,
    };
  },
});
</script>

<style lang="scss" scoped></style>
