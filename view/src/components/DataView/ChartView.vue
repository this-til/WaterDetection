<template>


  <div class="example-pagination-block">
    <el-pagination
        v-model:current-page=currentPage
        :page-count="pager"
    />
  </div>

  <el-table :data="tableData" border>
    <el-table-column prop="date" label="Date"/>

    <el-table-column
        v-for="(e) in equipmentList"
        :prop="String(e.id)"
        :label=e.name
    />


  </el-table>
</template>

<script lang="ts" setup>
import {DataSheet, Equipment} from "@/api";
import {markRaw, onMounted, ref, toRefs, watch} from "vue";

const tableData = ref<object[]>([])
const equipmentList = ref<Equipment[]>([])
const pager = ref<number>(0)
const currentPage = ref<number>(1)
const props = defineProps<Props>();

const max = 17;
onMounted(() => {
  update()
});

const {data} = toRefs(props);
watch(data, (newValue, oldValue) => {
  update();
});
watch(currentPage, (newValue, oldValue) => {
  update();
});


const update = () => {
  var value = data.value;
  if (value == null) {
    return
  }
  equipmentList.value = value.equipmentList
  tableData.value.length = 0
  pager.value = Math.ceil(value.timestampList.length / max)

  const s = max * (currentPage.value - 1);
  for (let i = s; i < value.timestampList.length && i < s + max; i++) {
    const obj: Employee = {}

    obj['date'] = new Date(value.timestampList[i])

    for (let j = 0; j < value.equipmentList.length; j++) {
      obj[String(value.equipmentList[j].id)] = value.value[j][i]
    }

    tableData.value.push(obj)
  }

}

interface Employee {
  [key: string]: any
}

interface Props {
  data: DataSheet
}
</script>

<style scoped>
.example-pagination-block + .example-pagination-block {
  margin-top: 10px;
}

.example-pagination-block .example-demonstration {
  margin-bottom: 16px;
}
</style>