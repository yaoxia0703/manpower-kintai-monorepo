import { createRouter, createWebHistory } from 'vue-router'
import SystemLayout from '@/components/layouts/SystemLayout.vue'
import { useAuthStore } from '@/stores/auth'
import { useUserStore } from '@/stores/userStore'
import LoginView from '@/views/login/LoginView.vue'
import DashboardView from '@/views/DashboardView.vue'
import TimesheetView from '@/views/timesheet/TimesheetView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/admin',
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      alias: ['/admin/login'],
      meta: { guestOnly: true },
    },
    {
      path: '/admin',
      name: 'admin',
      component: SystemLayout,
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'admin-home',
          component: DashboardView,
        },
        {
          path: 'timesheet',
          name: 'timesheet',
          component: TimesheetView,
        },
      ],
    },
    {
      path: '/dashboard',
      redirect: '/admin',
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/admin',
    },
  ],
})

router.beforeEach(async (to) => {
  const authStore = useAuthStore()
  const userStore = useUserStore()
  const token = authStore.token

  if (to.meta.requiresAuth && !token) {
    return {
      path: '/login',
      query: { redirect: to.fullPath },
    }
  }

  if (to.meta.requiresAuth && token && !userStore.isLoaded) {
    try {
      await authStore.loadCurrentUser()
    } catch {
      authStore.clearSession()
      return {
        path: '/login',
        query: { redirect: to.fullPath },
      }
    }
  }

  if (to.meta.guestOnly && token) {
    return '/admin'
  }
})

export default router
