<template>
	<HomePage class="main-container">
		<template #main>
			<div class="main">
				<div class="left">
					<h2>Public Chats</h2>
					<ul v-for="chat in channel.channels">
						<div class="channels" @click="chats.fetchChannelInfo(chat.id, true)">
							<li>{{ chat.name }}</li>
						</div>
					</ul>
					<h2>Private Chats</h2>
					<ul v-for="chat in channel.channels">
						<div class="channels" @click="chats.fetchChannelInfo(chat.id, false)">
							<li>{{ chat.name }}</li>
						</div>
					</ul>
					<button @click="openModal()">Create Public Channel</button>
					<button @click="logout()">Logout</button>
				</div>
				<div class="right">
					<VChat />
				</div>
				<VAddChannel v-if="modal.openChannel"/>
			</div>
		</template>
	</HomePage>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import HomePage from "../template/HomePage.vue";
import { useModal } from "../../stores/page"
import VLogo from "../atoms/v-logo.vue";
import VSpan from "../atoms/v-span.vue";
import VVerticalNav from "../molecules/v-verticalNav.vue";
import VModal from "../organisms/v-modal.vue";
import { usePage } from "../../stores/page";
import router from "@/router";
import VAddChannel from "../organisms/v-addChannel.vue";
import { usePublicChannel } from "../../stores/page"
import VChat from "../organisms/v-chat.vue";
import {useChat} from "../../stores/page"

const chats = useChat()
const page = usePage();
const modal = useModal()
const channel = usePublicChannel()
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
	modal.openChannel = true;
	console.log(modal.openChannel);
};

const logout = () => {
	fetch("http://localhost:9000/logout", {
		method: "GET",
		credentials: "include",
	})
	.then((response) => {
		if (response.ok) {
			console.log('aaa')
			router.push("/login");
		}else{
			throw new Error("network response error")
		}
	})
}

onMounted(() => {
	channel.fetchChannels()
})
const listTag = "ul";
const listItemTag = "li";
</script>

<style scoped>
.main {
	display: flex;
}
</style>
