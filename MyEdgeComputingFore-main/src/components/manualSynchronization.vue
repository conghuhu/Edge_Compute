<template>
  <div class="manual">
      <el-radio-group class="radio-group" v-model="radio" @change="handleChange">
        <el-radio class="radio" label="1" size="large" border>西药库数据同步到云端</el-radio>
        <el-radio class="radio" label="2" size="large" border> 处方数据同步到云端 </el-radio>
        <el-radio class="radio" label="3" size="large" border>云端数据同步到西药库</el-radio>
        <el-radio class="radio" label="4" size="large" border> 云端数据同步到处方 </el-radio>
        <el-radio class="radio" label="5" size="large" border> 西药库崩溃数据同步 </el-radio>
        <el-radio class="radio" label="6" size="large" border>  处方崩溃数据同步  </el-radio>
      </el-radio-group>
    <el-button type="info" @click="handleClick">
      Upload<el-icon class="el-icon--right"><Upload /></el-icon>
    </el-button>
  </div>
</template>

<script lang="ts">
import {
  westToYun,
  yunTowest,
  prescriptionToYun,
  yunToPrescription,
  hangDeadWest,
  hangDeadPrescription,
} from "~/axios/api";
import { defineComponent, ref } from "vue";
export default defineComponent({
  setup() {
    const radio = ref("1");
    const current = ref("");
    const handleChange = (value: any) => {
      current.value = value;
    };
    const handleClick = async () => {
      switch (current.value) {
        case "1":
          const west = await westToYun();
          break;
        case "2":
          const reverseWest = await yunTowest();
          break;
        case "3":
          const prescription = await prescriptionToYun();
          break;
        case "4":
          const rervsePrescription = await yunToPrescription();
          break;
        case "5":
          const deadWest = await hangDeadWest();
          break;
        case "6":
          const deadPrescription = await hangDeadPrescription();
          break;
      }
    };

    return {
      radio,
      handleChange,
      handleClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.manual{
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding-left: 20px;
.radio-group{
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  .radio{
    margin-bottom: 20px;
    width: 200px;
  }
}
}

</style>
