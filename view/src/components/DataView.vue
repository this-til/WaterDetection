<template>


  <el-header class="header-class">

    数据类型：{{ dataType.name }} (id:{{ dataType.id }})

    <el-divider direction="vertical"/>

    时间：

    <el-date-picker
        v-model=time
        :default-time="defaultTime"
        :shortcuts=shortcuts
        :clearable=false
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
        v-model=presentationMode
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
        v-if="presentationMode == 'lineChart' && data != null"
        :data=data
    >

    </LineChartView>

    <ChartView
        v-else-if="presentationMode == 'chart' && data != null"
        :data=data
    >

    </ChartView>

  </el-main>

</template>

<script lang="ts" setup>
import {markRaw, onMounted, onUnmounted, ref, toRefs, warn, watch} from 'vue'
import {DataSheet, DataType, Equipment, getDataToDataSheet} from "@/api";
import {ElMessageBox} from 'element-plus'
import LineChartView from "@/components/DataView/LineChartView.vue";
import ChartView from "@/components/DataView/ChartView.vue";
import {c} from "vite/dist/node/types.d-aGj9QkWt";
import * as echarts from "echarts";

const props = defineProps<Props>();

const data = ref<DataSheet | null>(null)


const time = ref<[Date, Date]>([
  new Date(new Date().getTime() - 1000 * 60 * 60),
  new Date()
])

const defaultTime: [Date, Date] = [
  new Date(new Date().getTime() - 1000 * 60 * 60),
  new Date()
] // '12:00:00', '08:00:00'

const shortcuts = ref([
  {
    text: '过去一小时',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setHours(start.getHours() - 1)
      return [start, end]
    },
  },
  {
    text: '过去两小时',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setHours(start.getHours() - 2)
      return [start, end]
    },
  },
  {
    text: '过去三小时',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setHours(start.getHours() - 3)
      return [start, end]
    },
  }, {
    text: '过去六小时',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setHours(start.getHours() - 6)
      return [start, end]
    },
  },
  {
    text: '过去半天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setHours(start.getDate() - 12)
      return [start, end]
    },
  },
  {
    text: '过去一天',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setDate(start.getDate() - 1)
      return [start, end]
    },
  },

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
    label: equipment.name,
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

watch(time, (n, o) => {
  if (time.value[1].getTime() > new Date().getTime()) {
    time.value[1] = new Date();
  }
  up()
})

const {dataType} = toRefs(props)
watch(dataType, (n, o) => {
  up()
})

/*
watch(_timeStep, (n, o) => {
  let replace: number = parseInt(_timeStep.value.replace(/\D/g, ''));
  replace = replace < 8 ? 8 : (replace > 12800 ? 12800 : replace)
  _timeStep.value = String(replace)
  timeStep.value = replace
  up()
})
*/


onMounted(() => {
  up()
});

const lastUpdatedTime = ref(new Date(0))
const updateTime = () => {
  if (lastUpdatedTime.value.getTime() < new Date().getTime() - 1000 * 16) {
    time.value[0] = new Date(time.value[0].getTime() + 1000 * 16);
    time.value[1] = new Date(time.value[1].getTime() + 1000 * 16);
    lastUpdatedTime.value = new Date();
    up()
  }
};

let intervalId: number = 0;

onMounted(() => {
  intervalId = setInterval(updateTime, 1000);
});

onUnmounted(() => {
  clearInterval(intervalId);
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
    timeStep.value = data.value.timeStep
    _timeStep.value = String(data.value.timeStep)
  })
}

const handleInput = (v) => {
  let replace = v.target.value.replace(/\D/g, '');
  replace = replace < 8 ? 8 : (replace > 12800 ? 12800 : replace)
  _timeStep.value = replace
  timeStep.value = parseInt(replace)
  up()
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