<template>

  <div class="full-screen">
    <el-container class="full">
      <el-aside width="200px">
        <el-col :span="24">
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
                  @click=displayDataView(dataType)>
                <template #title>{{ dataType.anotherName }}</template>
              </el-menu-item>


            </el-sub-menu>

            <el-sub-menu index="2">
              <template #title>
                <el-icon>
                  <Cpu/>
                </el-icon>
                <span>设备</span>
              </template>

              <el-sub-menu index="2-1">
                <template #title>传感器</template>
              </el-sub-menu>
              <el-sub-menu index="2-2">
                <template #title>执行器</template>
              </el-sub-menu>
              <el-sub-menu index="2-3">
                <template #title>
                  <!--                  <el-icon>
                                      <Connection/>
                                    </el-icon>-->
                  <span>规则</span>
                </template>
              </el-sub-menu>
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
import {ref} from 'vue';
import DataView from "@/components/DataView.vue";
import OrderView from "@/components/OrderView.vue";
import SetView from "@/components/SetView.vue";
import {DataType, Equipment, getAllDataType, getAllEquipment} from "@/api";

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}

const isDataView = ref<boolean>(false)
const useDataType = ref<DataType>(undefined)

const isOrder = ref<boolean>(false)
const isSet = ref<boolean>(false)

const allDataType = ref<DataType[]>([])
const allEquipment = ref<Equipment[]>([])


const erase = () => {
  isDataView.value = false
  useDataType.value = undefined

  isOrder.value = false
  isSet.value = false
}

const displayDataView = (dataType: DataType) => {
  erase()
  isDataView.value = true
  useDataType.value = dataType
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
  getAllDataType().then(r => {
    allDataType.value = r.data.data == null ? [] : r.data.data
  })
  getAllEquipment().then(r => {
    allEquipment.value = r.data.data == null ? [] : r.data.data
  })
}

up()
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