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

const {dataType} = toRefs(props);
watch(dataType, (newValue, oldValue) => {
  update();
});
const {equipmentList} = toRefs(props);
watch(equipmentList, (newValue, oldValue) => {
  update();
});
const {timeStep} = toRefs(props);
watch(timeStep, (newValue, oldValue) => {
  update();
});


const update = () => {

  //time
  const datatype = ['1', '2', '3', '4', '5', '6', '7', '8', '9'];

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


  /*    lineChart.setOption({
        title: {
          text: 'ECharts 入门示例'
        },
        tooltip: {},
        xAxis: {
          data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
        },
        yAxis: {},
        series: [{
          name: '销量',
          type: 'bar', // 注意这里应该是 'bar' 而不是 'line'，除非你想画折线图
          data: [5, 20, 36, 10, 10, 20]
        }]
      });*/


}


// 如果你需要在其他地方访问 lineChart，可以将它暴露给模板或其他 setup 函数
// 这里我选择不暴露，因为它只在 onMounted 钩子中使用

interface Props {
  dataType: DataType
  equipmentList: Equipment[]
  timeStep: number
}


</script>

<style scoped>
/* 你的样式 */
</style>