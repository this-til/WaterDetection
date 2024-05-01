<template>


  <el-container>
    <el-header class="header-class">

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

      &nbsp;&nbsp;&nbsp;

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
            v-model=selectEquipmentList
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

      &nbsp;&nbsp;&nbsp;



    </el-header>
    <el-divider/>
    <el-main>Main</el-main>
  </el-container>

</template>

<script lang="ts" setup>
import {ref, warn, watch} from 'vue'
import {DataType, Equipment} from "@/api";
import {ElMessageBox} from 'element-plus'

const time = ref<[Date, Date]>([
  new Date(new Date().getTime() - 1000 * 60 * 60 * 24),
  new Date()
])

const displayScreeningEquipment = ref<boolean>(false)

const equipmentFiltration = ref<EquipmentPack[]>([])
const selectEquipmentList = ref<[]>([])


const props = defineProps<Props>();

typeConversion(props.equipmentList, equipmentFiltration.value)

function typeConversion(list: Equipment[], outList: EquipmentPack[]) {
  for (let equipment of list) {
    outList.push({
      key: equipment.id,
      label: equipment.anotherName,
      disabled: false,
      equipment: equipment,
    })
  }
}

const filterMethod = (query, item) => {
  return item.label.toLowerCase().includes(query.toLowerCase())
}

const handleClose = (done: () => void) => {
  displayScreeningEquipment.value = false;
  //TODO
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