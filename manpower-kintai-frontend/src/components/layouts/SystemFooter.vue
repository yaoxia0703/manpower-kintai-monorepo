<template>
  <div class="system-footer">
    <div class="footer-brand">
      <el-avatar :size="24" class="footer-mark">M</el-avatar>
      <el-text type="primary" tag="span">Manpower</el-text>
    </div>
  </div>
</template>

<style scoped>
.system-footer {
  display: flex;
  height: 100%;
  align-items: center;
  justify-content: flex-end;
  background: var(--el-bg-color-overlay);
  padding: 0 24px;
}

.footer-brand {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-weight: 700;
}

.footer-mark {
  background: var(--el-color-primary);
  color: var(--el-color-white);
  font-weight: 700;
}
</style>
