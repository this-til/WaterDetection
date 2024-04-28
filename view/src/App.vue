<template>
  <el-row class="tac">
    <el-col :span="24">
      <!--      <h5 class="mb-2">Default colors</h5>-->
      <el-menu
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
      >
        <el-sub-menu index="1">
          <template #title>
            <el-icon>
              <Odometer/>
            </el-icon>
            <span>监视值</span>
          </template>


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
            <!--            <el-menu-item>item one</el-menu-item>-->
          </el-sub-menu>
          <el-sub-menu index="2-2">
            <template #title>执行器</template>
            <!--            <el-menu-item>item one</el-menu-item>-->
          </el-sub-menu>
        </el-sub-menu>


        <el-sub-menu index="3">
          <template #title>
            <el-icon>
              <Connection/>
            </el-icon>
            <span>规则</span>
          </template>
        </el-sub-menu>
        <!--        <el-sub-menu index="1">
                  <template #title>
                    <el-icon><location /></el-icon>
                    <span>Navigator One</span>
                  </template>
                  <el-menu-item-group title="Group One">
                    <el-menu-item index="1-1">item one</el-menu-item>
                    <el-menu-item index="1-2">item two</el-menu-item>
                  </el-menu-item-group>
                  <el-menu-item-group title="Group Two">
                    <el-menu-item index="1-3">item three</el-menu-item>
                  </el-menu-item-group>
                  <el-sub-menu index="1-4">
                    <template #title>item four</template>
                    <el-menu-item index="1-4-1">item one</el-menu-item>
                  </el-sub-menu>
                </el-sub-menu>-->

        <!--        <el-menu-item index="2">
                  <el-icon><icon-menu /></el-icon>
                  <span>Navigator Two</span>
                </el-menu-item>
                <el-menu-item index="3" disabled>
                  <el-icon><document /></el-icon>
                  <span>Navigator Three</span>
                </el-menu-item>
                <el-menu-item index="4">
                  <el-icon><setting /></el-icon>
                  <span>Navigator Four</span>
                </el-menu-item>-->
      </el-menu>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
  Odometer, Connection, Cpu,
} from '@element-plus/icons-vue'

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
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
</style>