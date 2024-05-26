<template>
  <el-header class="header-class">

    设备：{{ equipment.name }}

    <el-divider direction="vertical"/>

    <!--    <el-popover
            width="300"
            trigger="click"
            placement="bottom"
            @before-enter="clickRename"
        >
          <template #reference>
            <el-button style="margin-right: 16px">重命名</el-button>
          </template>

          <el-input v-model="newEquipmentName"/>
          <el-button @click=upEquipmentName>确定</el-button>

        </el-popover>-->

    <el-divider direction="vertical"/>

    最后更新时间：{{ new Date(equipment.upTime) }}

    <el-divider direction="vertical"/>

    坐标：({{ equipment.longitude }},{{ equipment.latitude }})

    <el-divider direction="vertical"/>

    <el-popover
        width="300"
        trigger="click"
        placement="bottom"
        @before-enter="clickFence"
    >
      <template #reference>
        <el-button style="margin-right: 16px">电子栅栏</el-button>
      </template>


      <el-checkbox v-model="newFence" label="启用" border/>

      <br>

      longitude:
      <el-input v-model="newFenceLongitude" :disabled="!equipment.electronicFence" placeholder="longitude"
                type="number"/>

      latitude:
      <el-input v-model="newFenceLatitude" :disabled="!equipment.electronicFence" placeholder="latitude"
                type="number"/>

      <el-button @click=upElectronicFence>确定</el-button>

      <!--      <el-transfer
                v-model=selectEquipmentIdList
                :data=equipmentFiltration

                filterable
                :filter-method=filterMethod
                filter-placeholder="搜索设备"
            />-->


      <!--        <template #footer>
                <div class="dialog-footer">
                  <el-button @click="displayScreeningEquipment = false">Cancel</el-button>
                  <el-button type="primary" @click="displayScreeningEquipment = false">
                    Confirm
                  </el-button>
                </div>
              </template>-->
    </el-popover>

    <el-divider direction="vertical"/>

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
import {onMounted, onUnmounted, ref, toRefs, watch} from 'vue'
import {DataTypeRunTime, Equipment, EquipmentApi, EquipmentRunTime, getResultTypeFromString, ResultType} from "@/api";
import {ElMessage} from "element-plus";
import {h} from 'vue'
import {post} from "axios";

const props = defineProps<Props>();

const equipmentRunTime = ref<EquipmentRunTime>(null)

const newEquipmentName = ref<string>()

const newFence = ref<boolean>(false)
const newFenceLongitude = ref<number>(0)
const newFenceLatitude = ref<number>(0)


const emit = defineEmits(['needUp']);

const upEquipmentName = () => {
  EquipmentApi.updateEquipmentAnotherNameById(props.equipment.id, newEquipmentName.value)
      .then(r => {
        if (r.data.resultType == 'SUCCESSFUL') {
          up()
          props.equipment.name = newEquipmentName.value
          emit('needUp');
          return
        }
      })
}

const upElectronicFence = () => {
  EquipmentApi.updateEquipmentFencePosById(props.equipment.id, newFence.value, newFenceLongitude.value, newFenceLatitude.value)
  emit('needUp');
}

const up = () => {

  EquipmentApi.getOnlineEquipment(props.equipment.id).then(r => {
    equipmentRunTime.value = r.data.data
  })

}

const clickFence = () => {
  newFence.value = props.equipment.electronicFence
  newFenceLongitude.value = props.equipment.fenceLongitude
  newFenceLatitude.value = props.equipment.fenceLatitude
}

const clickRename = () => {
  newEquipmentName.value = props.equipment.name
}


onMounted(() => {
  up();
})

let intervalId: number = 0;

onMounted(() => {
  intervalId = setInterval(updateTime, 16000);
});

onUnmounted(() => {
  clearInterval(intervalId);
});

const updateTime = () => {
  up();
}

const {equipment} = toRefs(props)
watch(equipment, (n, o) => {
  up()
})


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