<template>
  <el-button type="primary" @click="handleShow">新增</el-button>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="id" label="Id" width="auto" />
    <el-table-column prop="path" label="路径" width="auto" />
    <el-table-column prop="url" label="网关路由" width="auto" />
    <el-table-column prop="enabled" label="是否可用" width="auto" />
    <el-table-column fixed="right" label="Operations" width="auto">
      <template #default="scope">
        <el-button
          type="text"
          size="small"
          @click="handleClick(scope.row, scope.column, scope.$index)"
          >修改</el-button
        >
        <el-button
          type="text"
          size="small"
          @click="handleDlete(scope.row, scope.column, scope.$index)"
          >删除</el-button
        >
      </template>
    </el-table-column>
  </el-table>
  <el-dialog
    v-model="dialogFormVisible"
    :title="currentState === 'update' ? '更新网关' : '新增网关'"
  >
    <el-form style="max-width: 560px">
      <el-form-item label="路径ID">
        <el-select
          v-model="selectID"
          class="m-2"
          placeholder="Select"
          size="large"
          @focus="handleChange"
          @change="showImage"
        >
          <el-option
            v-for="item in hostName"
            :key="item.hostname"
            :label="item.hostname"
            :value="item.id"
          >
          </el-option>
        </el-select>
        --
        <el-select
          v-model="selectImage"
          :disabled="isImage"
          placeholder="Select"
          size="large"
          @focus="handleImage"
          @change="handleStoreImage"
        >
          <el-option
            v-for="item in dataImage"
            :key="item.image"
            :label="item.image"
            :value="item.image"
          >
          </el-option>
        </el-select>
      </el-form-item >
      <el-form-item label="路径  " >
        <el-input v-model="form.path" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="是否可用">
        <el-switch v-model="form.enabled"></el-switch>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button
          v-if="currentState === 'update'"
          type="primary"
          @click="handleUpdate"
          >更新</el-button
        >
        <el-button v-else type="primary" @click="handleCreate">新增</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import { Gateway, ContainerInfo, NodeItem } from "~/axios/globalInterface";
import {
  getGateWay,
  deleteeGateWay,
  updateGateWay,
  createGateWay,
  getContainerInfo,
  getNodeList,
} from "~/axios/api";
export default defineComponent({
  setup() {
    const tableData = reactive<Gateway[]>([]);
    onMounted(async () => {
      await refresh();
    });
    let selectID = ref("");
    const currentState = ref("update");
    const dialogFormVisible = ref(false);
    let form = reactive<Gateway>({
      id: "",
      path: "",
      url: "",
      enabled: true,
    });
    const handleClick = (row: Gateway, column: any, index: number) => {
      currentState.value = "update";
      dialogFormVisible.value = true;
      Object.assign(form, row);
    };
    const handleDlete = async (row: Gateway, column: any, index: number) => {
      const pathId = row.id;
      await deleteeGateWay(pathId);
      tableData.length = 0;
      await refresh();
    };
    const handleUpdate = async () => {
      await updateGateWay(form);
      tableData.length = 0;
      await refresh();
    };
    const result = reactive<Gateway>({
      id: "",
      path: "",
      url: "",
      enabled: true,
    });
    const handleCreate = async () => {
      result.id = tempHostName.value + "-" + selectImage.value;
      result.path = form.path;
      result.url = "http://" + tempIP.value + ":" + tempTargetport.value + "/";
      result.enabled = form.enabled;
      console.log(result);
      tableData.length = 0;
      await refresh();
    };
    const handleShow = async () => {
      dialogFormVisible.value = true;
      currentState.value = "create;";
      form.id = "";
      form.path = "";
      form.url = "";
      form.enabled = true;
    };
    const refresh = async () => {
      const res = await getGateWay();
      res.forEach((item: Gateway) => {
        tableData.push(item);
      });
    };
    const hostName = reactive<NodeItem[]>([]);
    const nodeMap = reactive<Map<string, NodeItem>>(new Map());
    const handleChange = async () => {
      hostName.length = 0;
      const res = await getNodeList();
      res.forEach((item) => {
        hostName.push(item);
        nodeMap.set(item.id, item);
      });
    };
    let selectImage = ref("");
    const isImage = ref(true);
    const tempHostName = ref("");
    const tempIP = ref("");
    const showImage = () => {
      isImage.value = false;
      tempHostName.value = nodeMap.get(selectID.value)?.hostname || "";
      tempIP.value = nodeMap.get(selectID.value)?.ip || "";
    };
    const dataImage = reactive<ContainerInfo[]>([]);
    const nodeImage = reactive<Map<string, ContainerInfo>>(new Map());
    const handleImage = async () => {
      dataImage.length = 0;
      const res = await getContainerInfo(selectID.value);
      res.forEach((item) => {
        dataImage.push(item);
        nodeImage.set(item.image, item);
      });
    };
    const tempTargetport = ref(NaN);
    const handleStoreImage = () => {
      tempTargetport.value =
        nodeImage.get(selectImage.value)?.targetPort || NaN;
    };

    return {
      tableData,
      handleClick,
      handleDlete,
      dialogFormVisible,
      form,
      //formLabelWidth,
      handleUpdate,
      handleCreate,
      currentState,
      handleShow,
      selectID,
      hostName,
      handleChange,
      selectImage,
      isImage,
      showImage,
      dataImage,
      handleImage,
      handleStoreImage,
    };
  },
});
</script>

<style lang="scss" scoped></style>
