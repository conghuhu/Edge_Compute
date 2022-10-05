<template>
  <el-form
    ref="ruleFormRef"
    :model="ruleForm"
    :rules="rules"
    label-width="120px"
    class="demo-ruleForm"
    :size="'large'"
  >
    <el-form-item label="服务名字" prop="serviceName">
      <el-input
        style="width: 600px"
        v-model="ruleForm.serviceName"
        width="100px"
      ></el-input>
    </el-form-item>
    <el-form-item label="镜像名字" prop="image">
      <el-input style="width: 600px" v-model="ruleForm.image"></el-input>
    </el-form-item>
    <el-form-item label="对外端口" prop="publishedPort">
      <el-input
        style="width: 600px"
        v-model="ruleForm.publishedPort"
      ></el-input>
    </el-form-item>
    <el-form-item label="对内端口" prop="targetPort">
      <el-input style="width: 600px" v-model="ruleForm.targetPort"></el-input>
    </el-form-item>
    <el-form-item label="网络名称" prop="netWork">
      <el-select v-model="selectNetwork" >
        <el-option  v-for="item in network"
        :key="item.netName"
        :label="item.netName"
        :value="item.netName"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="服务器选择">
      <el-select v-model="selectNode">
        <el-option v-for="item in node"
        :key="item.label"
        :label="item.label"
        :value="item.label"></el-option>
        
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm">创建服务</el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { defineComponent, onMounted } from "vue";
import { reactive, ref } from "vue";
import {
  ContainerForm,
  ShowContainerForm,
  NodeItem,
  NetWorkItem,
} from "~/axios/globalInterface";
import { getNodeList, getContainerInfo, getNetWork,createContainer} from "~/axios/api";
import { ElMessage } from 'element-plus'
export default defineComponent({
  setup() {
    const ruleForm = reactive<ContainerForm>({
      serviceName: "",
      image: "",
      publishedPort: "",
      targetPort: "",
      netWork: "",
      constraints: [],
    });

    const rules = reactive({
      serviceName: [
        {
          required: true,
          message: "请正确输入服务名字",
          trigger: "blur",
        },
        {
          min: 1,
          max: 15,
          message: "输入长度是1-15",
          trigger: "blur",
        },
      ],
      image: [
        {
          required: true,
          message: "请正确输入镜像名字",
          trigger: "blur",
        },
        {
          min: 1,
          max: 15,
          message: "输入长度是1-15",
          trigger: "blur",
        },
      ],
      publishedPort: [
        {
          required: true,
          message: "请正确输入对外端口号",
          trigger: "blur",
        },
        {
          min: 1,
          max: 15,
          message: "输入长度是1-15",
          trigger: "blur",
        },
      ],
      targetPort: [
        {
          required: true,
          message: "请正确输入对内端口号",
          trigger: "blur",
        },
        {
          min: 1,
          max: 15,
          message: "输入长度是1-15",
          trigger: "blur",
        },
      ],
    });
    const selectNode =ref("");
    const selectNetwork =ref("");
    const node = reactive<NodeItem[]>([]);
    const network =reactive<NetWorkItem[]>([]);
    onMounted(async () => {
      const res = await getNodeList();
      res.forEach((item) => {
        node.push(item);
      });
      const temp = await getNetWork();
      temp.forEach((item)=>{
        network.push(item);
      })
    });
    const submitForm = async () => {
      ruleForm.netWork=selectNetwork.value;
      ruleForm.constraints=["node.labels.func == "+selectNode.value]
      console.log(ruleForm);
      try {
        await createContainer(ruleForm);
        ElMessage({
          message:'创建服务成功',
          type:'success'
        })
        resetForm();
      } catch (error) {
        ElMessage({
          message:'创建服务失败,请联系管理员',
          type:'error'
        })
      }
      

    };

    const resetForm = () => {
      if (!ruleForm) return;
      ruleForm.serviceName = "";
      ruleForm.image = "";
      ruleForm.publishedPort = "";
      ruleForm.targetPort = "";
      selectNetwork.value="";
      selectNode.value="";
    };

    return {
      rules,
      ruleForm,
      submitForm,
      resetForm,
      node,
      network,
      selectNetwork,
      selectNode
    };
  },
});
</script>

<style lang="scss" scoped></style>
