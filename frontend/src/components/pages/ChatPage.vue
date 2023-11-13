<template>
    <p>{{ routes.params.id }}</p>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue"
import {useRoute} from "vue-router"
import {useChat} from "../../stores/page"
const pathName = ref(window.location.pathname)

const routes = useRoute()
const chat = useChat()

const message = ref({
    toId: "",
    createdAt: 0,
    message: "",
    isGroup: false,
    fromId: "",
    id: ""

})

onMounted(() => {
    fetch(`http://localhost/:9000/${routes.params.id}/${chat.isGroup}`, {
        method: "GET",
        credentials: 'include'
    })
    .then((response) => {
        if(response.ok) {
            return response.ok
        }else{
            throw new Error("network response error")
        }
    })
    .then((data) => {
        console.log(data)
    })
})
</script>

/*

[
    {
        "toId": "d3b45926-5e78-4fc0-9b57-db99cd87bef8",
        "createdAt": 1699854104000,
        "message": "kent cute",
        "isGroup": true,
        "fromId": "86506981-2ca1-4443-bc74-829b6639d159",
        "id": "282a02f7-fd6e-4322-aed6-d2e8564cdc13"
    }
]

*/