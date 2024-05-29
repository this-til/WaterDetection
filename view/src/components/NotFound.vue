<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "NotFound.vue"
})
</script>

<template>
  <div class="loogin-container ">
    啊，404 了，没有找到你想要的页面，快去怒艹大伟哥吧
  </div>
</template>

<style scoped>
.loogin-container {
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
}

</style>