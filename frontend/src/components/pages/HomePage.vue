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



const socket = new WebSocket("ws://localhost:9000/socket");

socket.onopen = () => {
	console.log("connected");
}

socket.onmessage = (event) => {
	const data = JSON.parse((event.data))
	
	post.posts = [...data.post, ...post.posts]
	post.images = [...data.image, ...post.images]
	post.comment = [...data.comment, ...post.comments]
	console.log(post.posts)



	//console.log(JSON.parse(JSON.stringify(post.posts.post)));
}

socket.onclose = () => {
	console.log("disconnected");
}
</script>

<style scoped>
</style>
