<template>
  <el-header class="header-class">

    设备：{{ equipment.name }}

    <el-divider direction="vertical"/>

    最后更新时间：


  </el-header>
  <el-main
      class="main-class"
      v-if="equipmentRunTime != null"
  >

    <el-row>
      数据：
    </el-row>

    <el-space
        wrap
    >
      <p
          v-for="item in equipmentRunTime.dataTypeRuntimeList"
          :key="item.dataType.id"
          :class=dataStyle(item)
      >

        {{ item.dataType.name }} : {{ item.value }}


      </p>

    </el-space>

    <el-row>
      执行器：
    </el-row>
    <el-space
        wrap
    >
      <p

          v-for="item in 20" :key="item"
          class="scrollbar-demo-item-normal">
      </p>
    </el-space>


    <el-row>
      指令：
    </el-row>


  </el-main>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {DataSheet, DataTypeRunTime, Equipment, EquipmentApi, EquipmentRunTime} from "@/api";
import ChartView from "@/components/DataView/ChartView.vue";
import LineChartView from "@/components/DataView/LineChartView.vue";
import {Color} from "echarts";

const props = defineProps<Props>();

const equipmentRunTime = ref<EquipmentRunTime>(null)

const up = () => {

  EquipmentApi.getOnlineEquipment(props.equipment.id).then(r => {
    equipmentRunTime.value = r.data.data
  })

}

up();

const dataStyle = (dataRunTime: DataTypeRunTime): string => {
  switch (dataRunTime.dataState) {
    case 2:
      return "scrollbar-demo-item-normal"
    case 1:
    case 3:
      return "scrollbar-demo-item-warn"
    case 0:
    case 4:
      return "scrollbar-demo-item-exception"
    default:
      return "scrollbar-demo-item-normal"
  }
}

interface Props {
  equipment: Equipment
}

</script>

<style scoped>
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

.scrollbar-demo-item-normal {

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