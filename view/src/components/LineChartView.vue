<template>
  <div ref="lineChartDiv" style="width: 600px;height:400px;"></div>
</template>

<script lang="ts" setup>
import {ref, onMounted, watch, watchEffect, toRefs} from 'vue';
import * as echarts from 'echarts';
import {DataType, Equipment} from "@/api";
import {EChartsType} from "echarts";

const lineChartDiv = ref(null);

const props = defineProps<Props>();

const lineChart = ref<EChartsType>(null);

onMounted(() => {
  lineChart.value = echarts.init(lineChartDiv.value);
  update()
});

const {dataType, equipmentList, timeStep, startTime, endTime} = toRefs(props);
watch(dataType, (newValue, oldValue) => {
  update();
});
watch(equipmentList, (newValue, oldValue) => {
  update();
});
watch(timeStep, (newValue, oldValue) => {
  update();
});
watch(startTime, (newValue, oldValue) => {
  update();
});
watch(endTime, (newValue, oldValue) => {
  update();
});

const update = () => {

  const startTimestamp = props.startTime.getTime();
  const endTimestamp = props.endTime.getTime();

  const processTime = endTimestamp - startTimestamp;
  const grid = processTime / (props.timeStep * 1000)

  const datatype = []
  for (let i = 0; i < grid; i++) {
    const time = startTimestamp + i * props.timeStep;
    datatype.push(new Date(time));
  }


  const series = []
  for (let equipment of props.equipmentList) {
    series.push({
      name: equipment.anotherName,
      type: 'line',
      data: [1 + equipment.id, 2, 3, 4, 5, 6, 7, 8, 9],
    })
  }

  lineChart.value.setOption({
    tooltip: {},
    xAxis: {
      type: 'category',
      data: datatype
    },
    yAxis: {},
    series: series
  }, true, false);


  /*  const option = {
      legend: {},
      tooltip: {},
      dataset: {
        // 提供一份数据。
        source: [
          ['product', '2015', '2016', '2017'],
          ['Matcha Latte', 43.3, 85.8, 93.7],
          ['Milk Tea', 83.1, 73.4, 55.1],
          ['Cheese Cocoa', 86.4, 65.2, 82.5],
          ['Walnut Brownie', 72.4, 53.9, 39.1]
        ]
      },
      // 声明一个 X 轴，类目轴（category）。默认情况下，类目轴对应到 dataset 第一列。
      xAxis: {type: 'category'},
      // 声明一个 Y 轴，数值轴。
      yAxis: {},
      // 声明多个 bar 系列，默认情况下，每个系列会自动对应到 dataset 的每一列。
      series: [{type: 'bar'}, {type: 'bar'}, {type: 'bar'}]
    };*/

}


// 如果你需要在其他地方访问 lineChart，可以将它暴露给模板或其他 setup 函数
// 这里我选择不暴露，因为它只在 onMounted 钩子中使用

interface Props {
  dataType: DataType
  equipmentList: Equipment[]
  timeStep: number
  startTime: Date
  endTime: Date
}


</script>

<style scoped>
/* 你的样式 */
</style>