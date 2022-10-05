<template>
  <el-table :data="tableData" border>
    <el-table-column prop="id" label="ID" />
    <el-table-column prop="ip" label="IP" />
    <el-table-column prop="label" label="标识" />
    <el-table-column prop="hostname" label="主机名" />
    <el-table-column prop="architecture" label="架构" width="180" />
    <el-table-column prop="os" label="操作系统" />
    <el-table-column prop="role" label="角色" />
    <el-table-column prop="createdTime" label="创建时间" width="180" />
    <el-table-column prop="updateTime" label="更新时间" />
  </el-table>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import { NodeItem } from "~/axios/globalInterface";
import { getNodeList } from "~/axios/api";
export default defineComponent({
  setup() {
    const tableData = reactive<NodeItem[]>([]);
    onMounted(async () => {
      const res = await getNodeList();
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
