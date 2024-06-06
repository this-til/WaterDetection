<template>
  <el-header class="header-class">

  </el-header>
  <el-main
      class="main-class"

  >

    <div
        v-for="item in allEquipment"
    >

      <EquipmentView
          :equipment=item
          :hasScript="false"
      >

      </EquipmentView>

      <br>

    </div>


    <!--    <el-space
            wrap
        >

          <div
              v-for="item in allEquipmentRunTime"
              :key="item.equipment.id"
              class=scrollbar-demo-item-normal
          >
            {{ item.equipment.name }}
            <br>
            <div
                v-for="i in item.dataTypeRuntimeList"
                :key="item.equipment.id * i.dataType.id"
                style="color: #f56c6c"
                class="txt"
            >
              {{ i.dataType.name }}:{{ i.value }}

            </div>


          </div>

          <div
              v-for="item in allEquipment"
              :key="item.id"
              :class=dataStyle(item)
          >
            {{ item.name }} ({{ allOnlineEquipment.indexOf(item.id) >= 0 ? ("在线") : ("离线") }})


          </div>
        </el-space>-->


  </el-main>
</template>

<script setup lang="ts">
import {ref} from 'vue'

import {Equipment} from "@/api";
import EquipmentView from "@/components/EquipmentView/EquipmentView.vue";

const props = defineProps<Props>();

const dataStyle = (e: Equipment) => {
  if (props.allOnlineEquipment.indexOf(e.id) < 0) {
    return "scrollbar-demo-item-bereft"
  }
  return "scrollbar-demo-item-normal"
}


interface Props {
  allEquipment: Equipment[];
  allOnlineEquipment: number[];
}

</script>

<style scoped>
.header-class {
  width: 100%;
  height: auto;
}

.main-class {
  width: 100%;
  height: 100%;
}

</style>