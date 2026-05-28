<template>
  <RouterLink v-if="to" :to="to" class="menu-item" active-class="active">
    <span>{{ label }}</span>
    <span v-if="chevron" class="chevron" aria-hidden="true"></span>
  </RouterLink>

  <button v-else class="menu-item" type="button">
    <span>{{ label }}</span>
    <span v-if="chevron" class="chevron" aria-hidden="true"></span>
  </button>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'

defineProps<{
  label: string
  to?: string
  chevron?: boolean
}>()
</script>

<style scoped>
.menu-item {
  display: inline-flex;
  min-width: 74px;
  height: 42px;
  align-items: center;
  justify-content: center;
  border: 0;
  border-bottom: 3px solid transparent;
  background: transparent;
  color: #344054;
  cursor: pointer;
  font-size: 0.92rem;
  line-height: 1;
  padding: 0 14px;
  text-decoration: none;
  white-space: nowrap;
}

.menu-item:hover {
  background: #f6f8fb;
  color: #0f5f9e;
}

.menu-item.active {
  border-bottom-color: #0f8fcf;
  background: #eaf6fd;
  color: #0b5f8f;
}

.chevron {
  width: 7px;
  height: 7px;
  margin-left: 8px;
  border-right: 1.5px solid currentColor;
  border-bottom: 1.5px solid currentColor;
  transform: rotate(45deg) translateY(-2px);
}

@media (max-width: 640px) {
  .menu-item {
    min-width: 68px;
    padding: 0 12px;
    font-size: 0.86rem;
  }
}
</style>
