<template>
	<HomePage class="main-container">
		<template #header>
			<VLogo >
				<h1>Contact Book</h1>
				<VButton @click="openModal()"> 
					Add Contacts
				</VButton>
			</VLogo>
		</template>
		<template #main>
			<VContacts />
			<VModalAddContact v-if="modal.openAddModal"/>
		</template>
	</HomePage>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import HomePage from "../template/HomePage.vue";
import { useModal } from "../../stores/page"
import { useContact } from "../../stores/contacts"
import VLogo from "../atoms/v-logo.vue";
import VModalAddContact from "../organisms/v-modalAddContact.vue";
import VButton from "../atoms/v-button.vue";
import VContacts from "../organisms/v-contacts.vue";

const modal = useModal()
const contact = useContact()
const openModal = () => {
	modal.openAddModal= true;
	console.log(modal.openAddModal);
};

onMounted(() => {
	contact.getContacts();
})
</script>

<style scoped>
</style>
