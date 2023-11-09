<template>
	<div class="header">
		<VLogo>
			<h2>BBS</h2>
		</VLogo>
		<VButton @click="modal.open = true">
			Add Post
		</VButton>
	</div>
</template>

<script setup lang="ts">
import VLogo from "../atoms/v-logo.vue";
import VButton from "../atoms/v-button.vue";
import { useModal } from "../../stores/page"

const modal = useModal()
</script>

<style scoped>

.header {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
}

</style>
