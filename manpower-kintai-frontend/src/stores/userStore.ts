import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { CurrentUser } from '@/types/employee'

export const useUserStore = defineStore('user', () => {
  const profile = ref<CurrentUser | null>(null)

  const isLoaded = computed(() => profile.value !== null)
  const displayName = computed(() => profile.value?.displayName ?? '')
  const email = computed(() => profile.value?.email ?? '')

  function setProfile(user: CurrentUser) {
    profile.value = user
  }

  function clearProfile() {
    profile.value = null
  }

  return {
    profile,
    isLoaded,
    displayName,
    email,
    setProfile,
    clearProfile,
  }
})
