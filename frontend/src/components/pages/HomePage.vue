<template>
	<HomePage class="main-container">
		<template #left>
			<p>left</p>
		</template>

		<template #right>
			<p>right</p>
		</template>
	</HomePage>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import HomePage from "../template/HomePage.vue";
import { useModal } from "../../stores/page"
import VLogo from "../atoms/v-logo.vue";
import VSpan from "../atoms/v-span.vue";
import VVerticalNav from "../molecules/v-verticalNav.vue";
import VModal from "../organisms/v-modal.vue";
import { usePage } from "../../stores/page";

const page = usePage();
const modal = useModal()
const spanValue = ref("Platform Launch");
const withImg = true;
const source = "../../public/task.png";
const links: { to: string; name: string }[] = [
	{
		to: "/",
		name: "Platform Launch",
	},
	{
		to: "/",
		name: "Marketing Plan",
	},
	{
		to: "/",
		name: "Roadmap",
	},
];

const openModal = () => {
	modal.open = true;
	console.log(modal.open);
};

const listTag = "ul";
const listItemTag = "li";
</script>

<style scoped>
.addButton {
	background-color: var(--violet);
	padding: 0.5rem;
	border-radius: 20px;
	cursor: default;
}

.header-title {
	font-weight: bolder;
}
</style>
