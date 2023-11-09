<template>
	<HomePage >
		<template #header>
			<v-header/>
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
const page = usePage();
const modal = useModal()
const post = usePost();
const openModal = () => {
	modal.open = true;
	console.log(modal.open);
};

onMounted(() => {
	post.getPosts()
})
</script>

<style scoped>
</style>
