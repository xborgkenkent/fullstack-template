<template>
	<HomePage >
		<template #header>
			<v-header/>
			<VLogin/>
			<VModal v-if="modal.open" @click="openModal()"/>
		</template>
		<template #main>
			<VPosts />
			<p>kkkk</p>
		</template>
	</HomePage>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import HomePage from "../template/HomePage.vue";
import { useModal } from "../../stores/page"
import VModal from "../organisms/v-modal.vue";
import vHeader from "../organisms/v-header.vue";
import { usePage } from "../../stores/page";
import { usePost } from "../../stores/page";
import VPosts from "../organisms/v-posts.vue";
import VLogin from "../organisms/v-login.vue";
const page = usePage();
const modal = useModal()
const post = usePost();
const openModal = () => {
	modal.open = true;
	console.log(modal.open);
};

onMounted(() => {
	//post.getPosts()
})

const socket = new WebSocket("ws://10.11.0.230:9000/socket");

socket.onopen = () => {
	console.log("connected");
}

socket.onmessage = (event) => {
	const data = JSON.parse(event.data)

	post.posts.p.push(data.post)
	post.posts.i.push(data.image)
	post.posts.c.push(data.comment)
	
	console.log(post.posts);
}

socket.onclose = () => {
	console.log("disconnected");
}
</script>

<style scoped>
</style>
