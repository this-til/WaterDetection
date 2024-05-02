<template>


  <el-container>
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
            v-model=selectEquipmentIdName
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
          @input="handleInput"
          class="input-with-select">
      </el-input>


    </el-header>
    <el-main>

      <LineChartView
          id="lineChart"
          v-if="presentationMode == 'lineChart'"

          :dataType=dataType
          :equipmentList=selectEquipmentList
          :timeStep=timeStep
      >

      </LineChartView>

      <ChartView
          id="chart"
          v-else-if="presentationMode == 'chart'">

      </ChartView>

    </el-main>
  </el-container>

</template>

<script lang="ts" setup>
import {ref, warn, watch} from 'vue'
import {DataType, Equipment} from "@/api";
import {ElMessageBox} from 'element-plus'
import LineChartView from "@/components/LineChartView.vue";
import ChartView from "@/components/ChartView.vue";

const props = defineProps<Props>();

const time = ref<[Date, Date]>([
  new Date(new Date().getTime() - 1000 * 60 * 60 * 24),
  new Date()
])

const displayScreeningEquipment = ref<boolean>(false)

const equipmentFiltration = ref<EquipmentPack[]>([])
const selectEquipmentIdName = ref<[]>([])
const selectEquipmentList = ref<Equipment[]>([])

watch(selectEquipmentIdName, (n, o) => {
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
  selectEquipmentIdName.value.push(equipment.id)
}


const filterMethod = (query, item) => {
  return item.label.toLowerCase().includes(query.toLowerCase())
}

const handleClose = (done: () => void) => {
  displayScreeningEquipment.value = false;
  //TODO
}

const presentationMode = ref('lineChart')

const presentationModeList = [
  {
    value: 'lineChart',
    label: '折线图',
  },
  {
    value: 'chart',
    label: '图表',
  },
]

const handleInput = (event) => {
  const replace = event.target.value.replace(/\D/g, '');
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
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 0;
}
</style>