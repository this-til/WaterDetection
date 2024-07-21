<template>
  <router-view :class="['app', { landscape: isLandscape }]">

  </router-view>
</template>

<script setup lang="ts">
import {ref, onMounted, onUnmounted} from 'vue';

const isLandscape = ref(false);

const updateOrientation = () => {
  // 简化的判断逻辑，实际中可能需要更复杂的逻辑来区分手机和平板
  isLandscape.value = window.innerWidth > window.innerHeight;
};

onMounted(() => {
  window.addEventListener('resize', updateOrientation);
  updateOrientation(); // 初始化时调用一次
});

onUnmounted(() => {
  window.removeEventListener('resize', updateOrientation);
});
</script>

<style scoped>
</style>