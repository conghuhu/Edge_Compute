<template>
  <div class="top">
    <el-select
      v-model="selectValue"
      class="m-2"
      placeholder="Select"
      size="large"
      @change="handleChange"
    >
      <el-option
        v-for="item in options"
        :key="item.hostname"
        :label="item.hostname"
        :value="item.id"
      >
      </el-option>
    </el-select>
  </div>
  <el-table :data="tableData" border>
    <el-table-column prop="image" label="镜像名字" width="180" />
    <el-table-column prop="createTime" label="创建时间" width="180" />
    <el-table-column prop="targetPort" label="对内端口" />
    <el-table-column prop="publishedPort" label="对外端口" />
    <el-table-column prop="constraints" label="服务器名" />
    <el-table-column prop="publishMode" label="发布模式" />
    <el-table-column prop="taskId" label="任务ID" />
    <el-table-column prop="netName" label="网络名字" />
  </el-table>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import {
  ContainerInfo,
  ContainerShow,
  NodeItem,
} from "~/axios/globalInterface";
import { getNodeList, getContainerInfo } from "~/axios/api";
export default defineComponent({
  setup() {
    const selectValue = ref("");
    let options = reactive<NodeItem[]>([]);

    const tableData = reactive<ContainerShow[]>([]);
    onMounted(async () => {
      const res = await getNodeList();
      res.forEach((item) => {
        options.push(item);
      });
    });
    const handleChange = async () => {
      tableData.length=0;
      const res = await getContainerInfo(selectValue.value);
      res.forEach((item) => {
        const temp = {
          image: item.image,
          createTime: item.service ? item.service.createTime : "",
          targetPort: item.targetPort,
          publishedPort: item.service ?item.service.publishedPort : NaN,
          constraints: item.service? item.service.constraints: "",
          publishMode: item.service?item.service.publishMode:"",
          taskId: item.taskId,
          netName: item.network?item.network.netName:"",
        };
        tableData.push(temp);
      });
    };

    return {
      selectValue,
      options,
      tableData,
      handleChange,
    };
  },
});
</script>

<style lang="scss" scoped>
.top {
  width: 100%;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
</style>
