<template>


  <el-header class="header-class">

    数据类型：{{ dataType.anotherName }} (id:{{ dataType.id }})

    <el-divider direction="vertical"/>

    时间：

    <el-date-picker
        :model-value=time
        type="datetimerange"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        format="YYYY-MM-DD HH:mm:ss"
        date-format="YYYY/MM/DD ddd"
        time-format="A hh:mm:ss"
    />

    <el-divider direction="vertical"/>

    <el-button
        plain @click="displayScreeningEquipment = true">
      筛选设备
    </el-button>

    <el-dialog
        v-model="displayScreeningEquipment"
        title="筛选设备"
        width="600"
        :before-close="handleClose"
    >

      <el-transfer
          v-model=selectEquipmentIdList
          :data=equipmentFiltration

          filterable
          :filter-method=filterMethod
          filter-placeholder="搜索设备"
      />
      <!--        <template #footer>
                <div class="dialog-footer">
                  <el-button @click="displayScreeningEquipment = false">Cancel</el-button>
                  <el-button type="primary" @click="displayScreeningEquipment = false">
                    Confirm
                  </el-button>
                </div>
              </template>-->
    </el-dialog>

    <el-divider direction="vertical"/>

    呈现方式：

    <el-select
        v-model="presentationMode"
        placeholder="Select"
        style="width: 240px"
    >
      <el-option
          v-for="item in presentationModeList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
      />
    </el-select>

    <el-divider direction="vertical"/>

    时间步进（秒）：

    <el-input
        v-model=_timeStep
        style="max-width: 100px"
        @blur=handleInput
        class="input-with-select">
    </el-input>


  </el-header>
  <el-main class="main-class">

    <LineChartView
        id="lineChart"

        v-if="presentationMode == 'lineChart' && data != null"

        :data=data
    >

    </LineChartView>

    <ChartView
        id="chart"
        v-else-if="presentationMode == 'chart' && data != null">

    </ChartView>

  </el-main>

</template>

<script lang="ts" setup>
import {markRaw, onMounted, ref, toRefs, warn, watch} from 'vue'
import {DataSheet, DataType, Equipment, getDataToDataSheet} from "@/api";
import {ElMessageBox} from 'element-plus'
import LineChartView from "@/components/DataView/LineChartView.vue";
import ChartView from "@/components/DataView/ChartView.vue";
import {c} from "vite/dist/node/types.d-aGj9QkWt";
import * as echarts from "echarts";

const props = defineProps<Props>();

const data = ref<DataSheet>(null)


const time = ref<[Date,Date]>([
  new Date(new Date().getTime() - 1000 * 60 * 60),
  new Date()
])

const displayScreeningEquipment = ref<boolean>(false)

const equipmentFiltration = ref<EquipmentPack[]>([])
const selectEquipmentIdList = ref<[]>([])
const selectEquipmentList = ref<Equipment[]>([])


const _timeStep = ref('10')
const timeStep = ref<number>(10)


for (let equipment of props.equipmentList) {
  const items = {
    key: equipment.id,
    label: equipment.anotherName,
    disabled: false,
    equipment: equipment,
  };
  equipmentFiltration.value.push(items)
  selectEquipmentList.value.push(equipment)
  selectEquipmentIdList.value.push(equipment.id)
}


const filterMethod = (query, item) => {
  return item.label.toLowerCase().includes(query.toLowerCase())
}

const handleClose = (done: () => void) => {
  displayScreeningEquipment.value = false;
  //TODO
}

const presentationMode = ref('lineChart')

const presentationModeList = ref([
  {
    value: 'lineChart',
    label: '折线图',
  },
  {
    value: 'chart',
    label: '图表',
  },
])


watch(selectEquipmentIdList, (n, o) => {
  selectEquipmentList.value = []
  for (let never of n) {
    for (let equipment of props.equipmentList) {
      if (equipment.id == never) {
        selectEquipmentList.value.push(equipment)
        break
      }
    }
  }
})

watch(selectEquipmentList, (n, o) => {
  up()
})

watch(timeStep, (n, o) => {
  up()
})


watch(time, (n, o) => {
  up()
}, { immediate: false, deep: false })

watch(timeStep, (n, o) => {
  up()
})

const {dataType} = toRefs(props)
watch(dataType, (n, o) => {
  up()
})

onMounted(() => {
  up()
});

const up = () => {
  getDataToDataSheet({
    dataTypeId: props.dataType.id,
    equipmentIdArray: selectEquipmentIdList.value,
    timeStep: timeStep.value,
    startTime: time.value[0],
    endTime: time.value[1],
  }).then(r => {
    data.value = r.data.data
  })
}

const handleInput = (v) => {
  let replace = v.target.value.replace(/\D/g, '');
  replace = replace < 8 ? 8 : (replace > 12800 ? 12800 : replace)
  _timeStep.value = replace
  timeStep.value = parseInt(replace)
}

interface EquipmentPack {
  key: number
  label: string
  disabled: boolean
  equipment: Equipment
}

interface Props {
  dataType: DataType
  equipmentList: Equipment[]
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