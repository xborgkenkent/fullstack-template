<template>
    <div class="chat">
        <p>{{ chat.form.name }}</p>
        <div>
            <ul v-for="m in chat.formMessage">
                <li >
                    {{ m.message }}
                </li>
            </ul>
        </div>
        <div>
            <textarea placeholder="Enter message..." v-model="message"></textarea>
            <button @click="chat.addMessage(chat.form.id, message)">Send</button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from "vue"
import {useChat} from "../../stores/page"
import {useRoute} from "vue-router"

const routes = useRoute()


const chat = useChat()

const message = ref('')

</script>

<style scoped>
    .chat {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        min-height: 100vh;
    }
</style>