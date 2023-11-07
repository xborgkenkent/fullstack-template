import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const usePage = defineStore("page", () => {
  const page = ref("Platform Launch");

  return { page };
});


export const useModal = defineStore("modal", () => {
  const open = ref(false)

  return { open }
})