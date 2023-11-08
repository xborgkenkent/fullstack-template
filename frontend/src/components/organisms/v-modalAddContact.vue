<template>
	<Teleport to="body">
		<div v-if="(modal.openAddModal = true)" class="modal">
			<div class="addForm">
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderFname"
					v-model="contact.form.firstname"
				/>
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderMname"
					v-model="contact.form.middlename"
				/>
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderLname"
					v-model="contact.form.lastname"
				/>
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderPhoneNumber"
					v-model="contact.form.phoneNumber"
				/>
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderEmail"
					v-model="contact.form.email"
				/>
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderOrganization"
					v-model="contact.form.organization"
				/>

				<!-- <v-select
					v-model="kanban.status"
					:items="kanban.statuses"
					class="select"
				/> -->
				<v-button class="submitButton" @click="addContact()">Add Contact</v-button>
			</div>
			<div class="close">
				<v-button class="closeButton" @click="modal.openAddModal = false">x</v-button>
			</div>
		</div>
	</Teleport>
</template>

<script setup lang="ts">
import { watch, ref } from "vue";
import { usePage } from "../../stores/page";
import VInput from "../atoms/v-input.vue";
import VSelect from "../atoms/v-select.vue";
import { useModal } from "../../stores/page";
import { useContact } from "@/stores/contacts";

const page = usePage();
const modal = useModal()
const contact = useContact()
const inputType = "text";
const placeHolderFname = "Enter first name";
const placeHolderLname = "Enter last name";
const placeHolderMname = "Enter middle name";
const placeHolderPhoneNumber = "Enter phone number";
const placeHolderEmail = "Enter email";
const placeHolderOrganization = "Enter organization name";

const addContact = () => {
	if(contact.form.firstname === "" && contact.form.lastname === "" && contact.form.middlename==="") {
		alert("Please provide atleast one info of the name")
	}else if(contact.form.phoneNumber === "" && contact.form.email === "") {
		alert("Please provide atleast one contact information")
	}else{
		contact.addContact(contact.form.firstname, contact.form.middlename, contact.form.lastname, contact.form.phoneNumber, contact.form.email, contact.form.organization);
	}
}
</script>

<style scoped>
.modal {
	display: flex;
	justify-content: space-between;
	position: fixed;
	z-index: 999;
	top: 40%;
	left: 40%;
	width: 30vw;
	background-color: var(--jaguar);
	padding: 3rem;
	border-radius: 10px;
}

.addForm {
	display: flex;
	justify-content: center;
	align-items: center;
	flex-direction: column;
	gap: 1rem;
}
.input {
	padding: 0.5rem;
	width: 25vw;
}

.select {
	padding: 0.5rem;
	width: 26vw;
}
.submitButton {
	background-color: var(--violet);
	width: 25vw;
	padding: 0.5rem;
	text-align: center;
	border-radius: 10px;
}
.closeButton {
	background-color: var(--red);
	font-weight: bolder;
	padding: 0.2rem;
	border-radius: 10px;
	width: 5vw;
	cursor: default;
}
</style>
