<template>
  <el-row class="login-page">
    <el-col :xs="24" :md="12" class="login-side login-brand">
      <el-space direction="vertical" alignment="flex-start" :size="20" class="login-content">
        <el-avatar :size="72" class="brand-avatar">M</el-avatar>
        <div>
          <h1 class="brand-title">勤怠管理システム</h1>
          <el-text class="brand-subtitle">ManpowerGroup</el-text>
        </div>
      </el-space>
    </el-col>

    <el-col :xs="24" :md="12" class="login-side login-form-side">
      <div class="login-content form-content">
        <h2 class="form-title">ログイン</h2>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-position="top"
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

          <el-button type="primary" size="large" :loading="loading" class="login-button" @click="handleLogin">
            ログイン
          </el-button>
        </el-form>
      </div>
    </el-col>
  </el-row>
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
  width: 100%;
  height: 100dvh;
  overflow: hidden;
  background: var(--el-bg-color-page);
}

.login-side {
  display: flex;
  min-height: 100%;
  align-items: center;
  justify-content: center;
  padding: 48px;
}

.login-brand {
  background: var(--app-brand-color);
  color: var(--el-color-white);
}

.login-content {
  width: min(100%, 420px);
}

.brand-avatar {
  border: 2px solid rgba(255, 255, 255, 0.72);
  background: transparent;
  color: var(--el-color-white);
  font-size: 32px;
  font-weight: 700;
}

.brand-title,
.form-title {
  margin: 0;
  font-weight: 700;
  line-height: 1.25;
}

.brand-title {
  color: var(--el-color-white);
  font-size: 32px;
}

.brand-subtitle {
  color: rgba(255, 255, 255, 0.78);
}

.login-form-side {
  background: var(--el-bg-color-page);
}

.form-title {
  margin-bottom: 32px;
  color: var(--el-text-color-primary);
  font-size: 28px;
}

.login-button {
  width: 100%;
  margin-top: 8px;
}

@media (max-width: 767px) {
  .login-page {
    height: auto;
    min-height: 100dvh;
    overflow: auto;
  }

  .login-side {
    min-height: 50dvh;
    padding: 32px 24px;
  }

  .brand-title {
    font-size: 26px;
  }
}
</style>
