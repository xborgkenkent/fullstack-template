<template>
    <VInput type="text" placeholder="Enter first name" v-model="fname"/>
    <VInput type="text" placeholder="Enter first name" v-model="fname"/>
    <VInput type="text" placeholder="Enter first name" v-model="fname"/>
    <VInput type="text" placeholder="Enter first name" v-model="fname"/>
    <VButton type="submit">Register</VButton>
</template>

<script setup lang="ts">
import {ref} from "vue";
import VInput from "../atoms/v-input.vue"
import VButton from "../atoms/v-button.vue";

const fname = ref('')
</script>