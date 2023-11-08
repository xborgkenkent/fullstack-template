import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const usePage = defineStore("page", () => {
  const page = ref("Platform Launch");

  return { page };
});

export const useModal = defineStore("modal", () => {
  const openAddModal = ref(false);
  const openEditModal = ref(false);
  return { openAddModal, openEditModal };
});
