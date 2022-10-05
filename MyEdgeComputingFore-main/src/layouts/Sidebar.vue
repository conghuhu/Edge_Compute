<template>
  <div class="sidebar">
    <el-menu
      default-active="1"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      background-color="rgb(40,52,70)"
      text-color="#fff"
      :router="true"
    >
      <el-sub-menu index="1">
        <template #title>
          <el-icon><expand /></el-icon>
          <span>请求管理模块</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="getwaySet">网关设置</el-menu-item>
          <el-menu-item index="getwayChange">网关变化记录</el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
      <el-sub-menu index="2">
        <template #title>
          <el-icon><setting /></el-icon>
          <span>容器管理模块</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="nodeList">节点列表</el-menu-item>
          <el-menu-item index="containerConfig">容器配置</el-menu-item>
          <el-menu-item index="containerInfo">容器信息</el-menu-item>
          <el-menu-item index="containerMonitor">容器监控</el-menu-item>
          <el-menu-item index="containerChange">容器变更记录</el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
      <el-sub-menu index="3">
        <template #title>
          <el-icon><grid /></el-icon>
          <span>云边同步模块</span>
        </template>
        <el-menu-item-group>
          <el-menu-item index="synchronousDataFlow">同步数据流向</el-menu-item>
          <el-menu-item index="synchronousDataJournal"
            >同步故障日志</el-menu-item
          >
          <el-menu-item index="manualSynchronization">手动同步</el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script lang="ts">
import { computed, onMounted, defineComponent } from "vue";
import { useStore } from "vuex";
import { useRoute, useRouter } from "vue-router";
export default defineComponent({
  setup() {
    const handleOpen = (key: string, keyPath: string[]) => {
      console.log(key, keyPath);
    };
    const handleClose = (key: string, keyPath: string[]) => {
      console.log(key, keyPath);
    };
    const store = useStore();
    const router = useRouter;
    return {
      router,
      handleOpen,
      handleClose,
    };
  },
});
</script>

<style lang="scss" scoped>
.sidebar {
  // display: block;
  // position: absolute;
  // left: 0;
  // top: 70px;
  // bottom: 0;
  width: 100%;
  height: 100%;
  overflow-y: scroll;
  background: rgb(40, 52, 70);
}
.sidebar::-webkit-scrollbar {
  width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
  width: 250px;
}
.sidebar > ul {
  height: 100%;
}
</style>
