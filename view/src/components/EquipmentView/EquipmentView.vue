<template>


  <el-header class="header-class">

    设备：{{ equipment.name }} ({{ equipmentRunTime == null ? "离线" : "在线" }})

    <el-divider direction="vertical"/>

    <el-popover
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

    </el-popover>

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

      范围(m):
      <el-input v-model="newFenceRange" :disabled="!equipment.electronicFence" placeholder="range"
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

    <el-popover
        width="300"
        trigger="click"
        placement="bottom"
        @before-enter="clickFence"
    >
      <template #reference>
        <el-button style="margin-right: 16px">删除设备</el-button>
      </template>

      你确定要删除设备历史数据吗

      <br>

      <el-button @click=deleteEquipment>确定</el-button>

    </el-popover>

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
      <div
          v-for="item in equipmentRunTime.dataTypeRuntimeList"
          :key="item.dataType.id"
          :class=dataStyle(item)
      >

        {{ item.dataType.name }} : {{ item.value }}

        <div
            class="lowerp-left-corner"
        >
          <el-popover
              width="300"
              trigger="click"
              placement="bottom"
              @before-enter="clickChangeRule(item)"
          >

            <template #reference>
              <el-button style="margin-right: 16px">更改规则</el-button>
            </template>
            异常上界:
            <el-input-number v-model="newExceptionUpper" :min="newWarnUpper"/>
            <br>
            警告上界:
            <el-input-number v-model="newWarnUpper" :min="newWarnLower" :max="newExceptionUpper"/>
            <br>
            警告下界:
            <el-input-number v-model="newWarnLower" :min="newExceptionLower" :max="newWarnUpper"/>
            <br>
            异常下界:
            <el-input-number v-model="newExceptionLower" :max="newWarnLower"/>
            <br>
            <el-button @click=changeRule(item)>确定</el-button>
          </el-popover>
        </div>


      </div>
    </el-space>

    <el-row>
      执行器：
    </el-row>
    <el-space
        wrap
    >
      <div
          v-for="item in equipmentRunTime.actuatorRuntimeList"
          :key="item.actuator.id"
          class="scrollbar-demo-item-normal">

        {{ item.actuator.name }} : &nbsp;&nbsp;&nbsp;
        <el-switch
            v-model="item.activated"
            @change="switchActivated(item)"
        />

      </div>
    </el-space>
    <!--


        <el-row>
          指令：
        </el-row>
    -->

    <div
        v-if=hasScript
    >
      <el-row
          v-if=hasScript
      >
        脚本：
      </el-row>
      <el-input
          v-if=hasScript
          v-model="textarea"
          :autosize="{ minRows: 20, maxRows: 999 }"
          type="textarea"
      />
    </div>


  </el-main>
</template>

<script lang="ts" setup>
import {onMounted, onUnmounted, ref, toRefs, watch} from 'vue'
import {
  ActuatorApi,
  ActuatorRuntime,
  DataTypeRunTime,
  Equipment,
  EquipmentApi,
  EquipmentRunTime,
  getResultTypeFromString,
  ResultType, RuleApi
} from "@/api";
import {ElMessage} from "element-plus";
import {h} from 'vue'
import {post} from "axios";
import {useRouter} from "vue-router";

const router = useRouter();
const props = defineProps<Props>();

const equipmentRunTime = ref<EquipmentRunTime>(null)

const newEquipmentName = ref<string>()

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

const deleteEquipment = () => {
  EquipmentApi.removeEquipmentPosById(props.equipment.id)
  router.go(0);
}

const up = () => {
  EquipmentApi.getOnlineEquipment(props.equipment.id).then(r => {
    equipmentRunTime.value = r.data.data
  })
}

const newFence = ref<boolean>(false)
const newFenceLongitude = ref<number>(0)
const newFenceLatitude = ref<number>(0)
const newFenceRange = ref<number>(0)

const clickFence = () => {
  newFence.value = props.equipment.electronicFence
  newFenceLongitude.value = props.equipment.fenceLongitude
  newFenceLatitude.value = props.equipment.fenceLatitude
  newFenceRange.value = props.equipment.fenceRange * 111320
}

const upElectronicFence = () => {
  EquipmentApi.updateEquipmentFencePosById(props.equipment.id, newFence.value, newFenceLongitude.value, newFenceLatitude.value, newFenceRange.value / 111320)
  emit('needUp');
}

const newExceptionUpper = ref<number>(0)
const newWarnUpper = ref<number>(0)
const newWarnLower = ref<number>(0)
const newExceptionLower = ref<number>(0)

const clickChangeRule = (dataTypeRunTime: DataTypeRunTime) => {
  newExceptionUpper.value = dataTypeRunTime.rule.exceptionUpper
  newWarnUpper.value = dataTypeRunTime.rule.warnUpper
  newWarnLower.value = dataTypeRunTime.rule.warnLower
  newExceptionLower.value = dataTypeRunTime.rule.exceptionLower
}

const changeRule = (dataTypeRunTime: DataTypeRunTime) => {
  RuleApi.updateById(dataTypeRunTime.rule.id, {
    exceptionUpper: newExceptionUpper.value,
    warnUpper: newWarnUpper.value,
    warnLower: newWarnLower.value,
    exceptionLower: newExceptionLower.value,

    warnSendMessage: dataTypeRunTime.rule.warnSendMessage,
    exceptionSendMessage: dataTypeRunTime.rule.exceptionSendMessage
  })
  up()
}

const switchActivated = (actuator: ActuatorRuntime) => {
  ActuatorApi.updateActuatorByEquipmentId(props.equipment.id, actuator.embeddedId, actuator.activated)
}

const clickRename = () => {
  newEquipmentName.value = props.equipment.name
}

const textarea = ref<string>()

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
    case 3:
      return "scrollbar-demo-item-normal"
    case 2:
    case 4:
      return "scrollbar-demo-item-warn"
    case 1:
    case 5:
      return "scrollbar-demo-item-exception"
    default:
      return "scrollbar-demo-item-normal"
  }
}

interface Props {
  equipment: Equipment
  hasScript: boolean
}

</script>

<style scoped>
.lowerp-left-corner {
  position: absolute;
  bottom: 0;
  left: 0;
}

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
  position: relative;
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
  position: relative;
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
  position: relative;
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