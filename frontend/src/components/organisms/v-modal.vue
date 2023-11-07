<template>
	<Teleport to="body">
		<div v-if="(modal.open = true)" class="modal">
			<div class="addForm">
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderTitle"
					v-model="title"
				/>
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderDesc"
					v-model="description"
				/>
				<!-- <v-select
					v-model="kanban.status"
					:items="kanban.statuses"
					class="select"
				/> -->
				<v-button class="submitButton">Add Task</v-button>
			</div>
			<div class="close">
				<v-button class="closeButton" @click="modal.open = false">x</v-button>
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

const page = usePage();
const modal = useModal()
const inputType = "text";
const placeHolderTitle = "Enter task name";
const placeHolderDesc = "Enter description";

const description = ref("");
const title = ref("");

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
