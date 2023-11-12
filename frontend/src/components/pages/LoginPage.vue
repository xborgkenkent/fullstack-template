<template>
    <VInput type="text" placeholder="Enter username..." v-model="fname"/>
    <VInput type="text" placeholder="Enter password..." v-model="fname"/>
</template>

<script setup lang="ts">
import {ref} from "vue";
import VInput from "../atoms/v-input.vue"


const fname = ref('firsntma')
</script>