<template>
    <form action="post" @submit.prevent="login()">
        <VInput type="text" placeholder="username" id="username" v-model="username"/>
        <VInput type="text" placeholder="enter password" id="password" v-model="password"/>
        <VButton type="submit">Login</VButton>
    </form>
</template>

<script setup lang="ts">
import {ref} from "vue"
import VInput from "../atoms/v-input.vue";
import VButton from "../atoms/v-button.vue";

const username = ref('')
const password= ref('')

const login = () => {
    console.log(username.value, password.value)
}
</script>