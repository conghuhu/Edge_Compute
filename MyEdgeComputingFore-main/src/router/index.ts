import { createRouter, createWebHashHistory } from "vue-router";

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: "/",
      name: "Home",
      component: () => import("~/views/Home.vue"),
      children:[
        {
          path:"getwaySet",
          name:"getwaySet",
          component:()=>import("~/components/gatewaySettings.vue")
        },
        {
          path:"getwayChange",
          name:"getwayChange",
          component:()=>import("~/components/gatewayChange.vue")
        },
        {
          path:"containerConfig",
          name:"containerConfig",
          component:()=>import("~/components/containerConfig.vue")
        },
        {
          path:"containerInfo",
          name:"containerInfo",
          component:()=>import("~/components/containerInfo.vue")
        },
        {
          path:"containerMonitor",
          name:"containerMonitor",
          component:()=>import("~/components/containerMonitor.vue")
        },
        {
          path:"containerChange",
          name:"containerChange",
          component:()=>import("~/components/containerChange.vue")
        },
        {
          path:"synchronousDataFlow",
          name:"synchronousDataFlow",
          component:()=>import("~/components/synchronousDataFlow.vue")
        },
        {
          path:"synchronousDataJournal",
          name:"synchronousDataJournal",
          component:()=>import("~/components/synchronousDataJournal.vue")
        },
        {
          path:"manualSynchronization",
          name:"manualSynchronization",
          component:()=>import("~/components/manualSynchronization.vue")
        },
        {
          path:"nodeList",
          name:"nodeList",
          component:()=>import("~/components/nodeList.vue")
        },
      ]
    },
   
  ],
});
export default router;