<template>
  <el-container class="layout-container-demo" style="height: 100vh">
    <el-header style="padding: 0"><vHeader /></el-header>
    <el-container>
      <el-aside width="200px"><vSidebar /></el-aside>
      <el-main><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script lang="ts">
import { useStore } from "vuex";
import vHeader from "~/layouts/Header.vue";
import vSidebar from "~/layouts/Sidebar.vue";
import { reactive, ref, defineComponent, onMounted, computed } from "vue";
export default defineComponent({
  components: {
    vHeader,
    vSidebar,
  },
  setup() {
    const store = useStore();
    const collapse = computed(() => store.state.collapse);
    return {
      collapse,
    };
  },
});
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  width: 100%;
}
.element-plus-logo {
  width: 50%;
}
</style>
