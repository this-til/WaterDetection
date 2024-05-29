<template>
  <el-header class="header-class">

  </el-header>
  <el-main
      class="main-class"
  >
    <div
        v-for="item in allEquipment"
        :key="item.id"
        :class=dataStyle(item)
    ></div>

  </el-main>
</template>

<script setup lang="ts">
import {defineComponent} from 'vue'
import {Equipment} from "@/api";
import {post} from "axios";

const props = defineProps<Props>();

const dataStyle = (e: Equipment) => {
  if (props.allOnlineEquipment.indexOf(e.id) < 0) {
    return "scrollbar-demo-item-bereft"
  }

}

interface Props {
  allEquipment: Equipment[];
  allOnlineEquipment: number[];
}

</script>

<style scoped>
.lowerp-left-corner {
  position: absolute;
  bottom: 0;
  left: 0;
}

.scrollbar-demo-item {
  display: flex;
  align-items: center;
  justify-content: center;

  width: 350px;
  height: 150px;

  margin: 5px;
  text-align: center;
  border-radius: 4px;
}

.scrollbar-demo-item-bereft {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;

  width: 350px;
  height: 150px;

  margin: 5px;
  text-align: center;
  border-radius: 4px;

  background: #818488;
}


.scrollbar-demo-item-normal {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;

  width: 350px;
  height: 150px;

  margin: 5px;
  text-align: center;
  border-radius: 4px;

  background: #a0cfff;
}

.scrollbar-demo-item-warn {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;

  width: 350px;
  height: 150px;

  margin: 5px;
  text-align: center;
  border-radius: 4px;

  background: #f3d19e;
}

.scrollbar-demo-item-exception {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;

  width: 350px;
  height: 150px;

  margin: 5px;
  text-align: center;
  border-radius: 4px;

  background: #f56c6c;
}


.header-class {
  width: 100%;
  height: auto;
}

.main-class {
  width: 100%;
  height: 100%;
}

</style>