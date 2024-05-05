<template>
  <div ref="lineChartDiv" class=def-view></div>
</template>

<script lang="ts" setup>
import {ref, onMounted, watch, watchEffect, toRefs, markRaw} from 'vue';
import * as echarts from 'echarts';
import {Data, DataSheet, DataType, Equipment} from "@/api";
import {EChartsType} from "echarts";

const lineChartDiv = ref(null);

const props = defineProps<Props>();

let lineChart: EChartsType | undefined = undefined;

onMounted(() => {
  lineChart = markRaw(echarts.init(lineChartDiv.value));
  update()
});

const {data} = toRefs(props);
watch(data, (newValue, oldValue) => {
  update();
});

window.addEventListener('resize', function () {
  if (lineChart == undefined) {
    return
  }
  lineChart.resize()
});


const update = () => {

  const data: DataSheet = props.data;

  if (lineChart == undefined) {
    return
  }

  const timestampList: Date [] = []
  for (let string of data.timestampList) {
    timestampList.push(new Date(string));
  }


  const series = []

  for (let i = 0; i < data.equipmentList.length; i++) {
    series.push({
      name: data.equipmentList[i].name,
      type: 'line',
      data: data.value[i],
    })
  }

  lineChart.setOption(markRaw({
    tooltip: {},
    xAxis: {
      type: 'category',
      data: timestampList
    },
    yAxis: {
      type: 'value',
      name: '值'
    },
    series: series
  }), true, false);
}


interface Props {
  data: DataSheet
}


</script>

<style scoped>
.def-view {
  width: 100%;
  height: 100%;
  padding: 0;
  box-sizing: border-box;
}

</style>