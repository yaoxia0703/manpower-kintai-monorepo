<template>
  <main class="login-page">
    <section class="brand-panel" aria-label="ManpowerGroup Kintai">
      <div class="brand-mark" aria-hidden="true">M</div>
      <h1>勤怠管理システム</h1>
      <p>ManpowerGroup</p>
    </section>

    <section class="form-panel">
      <div class="form-wrapper">
        <h2>ログイン</h2>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-position="top"
          class="login-form"
          @keyup.enter="handleLogin"
        >
          <el-form-item label="メールアドレス" prop="email">
            <el-input
              v-model="form.email"
              type="email"
              placeholder="example@company.com"
              size="large"
              autocomplete="username"
            />
          </el-form-item>

          <el-form-item label="パスワード" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="パスワード"
              size="large"
              show-password
              autocomplete="current-password"
            />
          </el-form-item>

          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            ログイン
          </el-button>
        </el-form>
      </div>
    </section>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  email: '',
  password: '',
})

const rules: FormRules<typeof form> = {
  email: [
    { required: true, message: 'メールアドレスを入力してください', trigger: 'blur' },
    { type: 'email', message: '有効なメールアドレスを入力してください', trigger: 'blur' },
  ],
  password: [{ required: true, message: 'パスワードを入力してください', trigger: 'blur' }],
}

async function handleLogin() {
  if (!formRef.value) return

  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await authStore.login(form.email, form.password)
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/admin'
    router.push(redirect)
  } catch (err: unknown) {
    const message = err instanceof Error ? err.message : 'ログインに失敗しました'
    ElMessage.error(message)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: grid;
  min-height: 100vh;
  grid-template-columns: minmax(320px, 0.9fr) minmax(360px, 1.1fr);
  background: #f6f8fb;
}

.brand-panel {
  display: flex;
  min-height: 100%;
  flex-direction: column;
  justify-content: center;
  padding: 64px;
  background: #123c69;
  color: #ffffff;
}

.brand-mark {
  display: grid;
  width: 72px;
  height: 72px;
  margin-bottom: 32px;
  place-items: center;
  border: 2px solid rgba(255, 255, 255, 0.72);
  border-radius: 8px;
  font-size: 2rem;
  font-weight: 700;
}

.brand-panel h1 {
  margin: 0 0 12px;
  font-size: 2rem;
  font-weight: 700;
  line-height: 1.25;
}

.brand-panel p {
  margin: 0;
  color: rgba(255, 255, 255, 0.78);
  font-size: 1rem;
}

.form-panel {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
}

.form-wrapper {
  width: min(100%, 400px);
}

.form-wrapper h2 {
  margin: 0 0 28px;
  color: #172033;
  font-size: 1.7rem;
  font-weight: 700;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 22px;
}

.login-form :deep(.el-form-item__label) {
  color: #344054;
  font-weight: 600;
}

.login-button {
  width: 100%;
  margin-top: 4px;
}

@media (max-width: 760px) {
  .login-page {
    grid-template-columns: 1fr;
  }

  .brand-panel {
    min-height: 220px;
    padding: 32px 24px;
  }

  .brand-mark {
    width: 56px;
    height: 56px;
    margin-bottom: 20px;
    font-size: 1.5rem;
  }
}
</style>
