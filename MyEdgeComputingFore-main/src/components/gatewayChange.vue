<template>
  <el-table :data="tableData" border >
    <el-table-column prop="fromservice" label="服务来源" />
    <el-table-column prop="toservice" label="服务去向"  />
    <el-table-column prop="insertdate" label="创建服务" />
  </el-table>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive } from "vue";
import { getUpdateInfo } from "~/axios/api";
import { NetWorkChange } from "~/axios/globalInterface";
export default defineComponent({
  setup() {
    const tableData = reactive<NetWorkChange[]>([]);
    onMounted(async()=>{
        const res = await getUpdateInfo();
        res.forEach((item)=>{
            tableData.push(item);
        })
    })
    return{
        tableData,
    }
  },
});
</script>

<style lang="scss" scoped></style>
