<template>

  <div class="full-screen">
    <el-container class="full">
      <el-aside width="200px">
        <el-col>
          <el-menu class="el-menu-vertical-demo">
            <el-sub-menu index="1">
              <template #title>
                <el-icon>
                  <Odometer/>
                </el-icon>
                <span>监视值</span>
              </template>

              <el-menu-item
                  v-for="(dataType) in allDataType"
                  :key=dataType.id
                  :index="'1-' + dataType.id"
                  @click=displayDataView(dataType)>
                <template #title>{{ dataType.name }}</template>
              </el-menu-item>


            </el-sub-menu>

            <el-sub-menu index="2">
              <template #title>
                <el-icon>
                  <Cpu/>
                </el-icon>
                <span>设备</span>
              </template>

              <el-menu-item
                  @click="displayAllEquipmentView"
                  index="2-all"

              >

                <template #title>-总览-</template>

              </el-menu-item>

              <el-menu-item
                  v-for="e in allEquipment"
                  :index="'2-' + e.id"
                  @click=displayEquipmentView(e)
              >
                <template #title>{{ e.name }} ({{
                    allOnlineEquipment.indexOf(e.id) >= 0 ? ("在线") : ("离线")
                                 }})
                </template>


              </el-menu-item>

            </el-sub-menu>


            <el-menu-item
                index="3"
                @click=displayOrder>
              <el-icon>
                <Finished/>
              </el-icon>
              <span>指令</span>
            </el-menu-item>

            <el-menu-item
                index="4"
                @click=displaySet>
              <el-icon>
                <Operation/>
              </el-icon>
              <span>设置</span>
            </el-menu-item>
          </el-menu>
        </el-col>
      </el-aside>
      <el-main class="full">
        <DataView
            v-if=isDataView
            :equipmentList=allEquipment
            :dataType=useDataType>
        </DataView>

        <AllEquipmentView
            v-else-if=isAllEquipmentView
            :allEquipment="allEquipment"
            :allOnlineEquipment="allOnlineEquipment"
        >

        </AllEquipmentView>

        <EquipmentView
            v-else-if=isEquipmentView
            :equipment=useEquipment
            :hasScript="true"
            @needUp="needUp"
        >

        </EquipmentView>

        <OrderView
            v-else-if=isOrder>
        </OrderView>

        <SetView
            v-else-if=isSet>
        </SetView>


      </el-main>
    </el-container>
  </div>

  <!--  <el-row class="tac">

    </el-row>

    <el-col :span="48" >
      <DataView class="def-view">
      </DataView>
    </el-col>-->

</template>

<script lang="ts" setup>
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
  Odometer, Connection, Cpu, Finished, Operation,
} from '@element-plus/icons-vue'
import {onMounted, onUnmounted, ref} from 'vue';
import DataView from "@/components/DataView/DataView.vue";
import OrderView from "@/components/OrderView.vue";
import SetView from "@/components/SetView.vue";
import EquipmentView from "@/components/EquipmentView/EquipmentView.vue"
import {
  DataType,
  Equipment,
  DataApi,
  EquipmentApi,
  DataTypeApi,
  _token,
  AlarmMessageApi,
  AlarmMessage,
  DataTypeRunTime
} from "@/api";
import {useRouter} from "vue-router";
import AllEquipmentView from "@/components/EquipmentView/AllEquipmentView.vue";
import {ElMessage} from 'element-plus'
import { h } from 'vue'
import { ElNotification } from 'element-plus'


const router = useRouter();

if (_token == null || _token == "") {
  router.replace({path: "/login"});
}

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}

const isDataView = ref<boolean>(false)
const useDataType = ref<DataType>(undefined)

const isEquipmentView = ref<boolean>(false)
const useEquipment = ref<Equipment>(undefined)

const isAllEquipmentView = ref<boolean>(false)

const isOrder = ref<boolean>(false)
const isSet = ref<boolean>(false)

const allDataType = ref<DataType[]>([])
const allEquipment = ref<Equipment[]>([])
const allOnlineEquipment = ref<number[]>([])

const erase = () => {
  isDataView.value = false
  useDataType.value = undefined

  isAllEquipmentView.value = false
  isEquipmentView.value = false
  useEquipment.value = undefined

  isOrder.value = false
  isSet.value = false
}

const displayDataView = (dataType: DataType) => {
  erase()
  isDataView.value = true
  useDataType.value = dataType
}

const displayAllEquipmentView = () => {
  erase()
  isAllEquipmentView.value = true
}

const displayEquipmentView = (equipment: Equipment) => {
  erase()
  isEquipmentView.value = true
  useEquipment.value = equipment
}

const displayOrder = () => {
  erase()
  isOrder.value = true
}

const displaySet = () => {
  erase()
  isSet.value = true
}

const up = () => {
  DataTypeApi.getAllDataType().then(r => {
    allDataType.value = r.data.data == null ? [] : r.data.data
  })
  EquipmentApi.getAllEquipment().then(r => {
    allEquipment.value = r.data.data == null ? [] : r.data.data
  })
  EquipmentApi.getAllOnlineEquipmentId().then(r => {
    allOnlineEquipment.value = r.data.data == null ? [] : r.data.data
  })
  AlarmMessageApi.getAlarmMessageList().then(r => {
    for (let a in r.data.data) {
      const alarmMessage = r.data.data[a]
      let error: string = ""
      switch (alarmMessage.dataState) {
        case 2:
        case 4:
          error = "warning"
          break
        case 1:
        case 5:
          error = "error"
          break
      }
      ElNotification({
        showClose: true,
        message: alarmMessage.message,
        type: error,
        offset: 50 * a,
        duration: 14000,
      })
    }
  })
}

const needUp = () => {
  up()
}

onMounted(() => {
  up()
});

let intervalId: number = 0;

onMounted(() => {
  intervalId = setInterval(updateTime, 16000);
});

onUnmounted(() => {
  clearInterval(intervalId);
});

const updateTime = () => {
  up()
}

</script>

<style>
.el-menu-vertical-demo {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  overflow-y: auto;
  padding: 20px;
  box-sizing: border-box;
}

.full-screen {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
}

.full {
  width: 100%;
  height: 100%;
  padding: 20px;
}
</style>