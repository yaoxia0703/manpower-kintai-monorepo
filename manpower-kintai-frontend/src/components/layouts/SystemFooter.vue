<template>
  <div class="system-footer">
    <div class="footer-brand">
      <span class="footer-mark" aria-hidden="true">M</span>
      <span>Manpower</span>
    </div>
  </div>
</template>

<style scoped>
.system-footer {
  display: flex;
  height: 100%;
  align-items: center;
  justify-content: flex-end;
  background: rgba(255, 255, 255, 0.72);
  padding: 0 24px;
}

.footer-brand {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #355270;
  font-weight: 700;
}

.footer-mark {
  display: grid;
  width: 24px;
  height: 24px;
  place-items: center;
  border-radius: 6px;
  background: #0f5f9e;
  color: #ffffff;
  font-size: 0.8rem;
  font-weight: 700;
}
</style>
