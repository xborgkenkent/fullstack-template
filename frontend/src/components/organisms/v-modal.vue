<template>
	<Teleport to="body">
		<div v-if="(modal.open = true)" class="modal">
			<form class="addForm" method="post" @submit.prevent="upload()" enctype="multipart/form-data">
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderTitle"
					@change="post.form.message = $event.target.value"
				/>
				<v-input
					class="input"
					:inputType="inputType"
					:placeholderValue="placeHolderTitle"
					@change="post.form.password = $event.target.value"
				/>
				<v-input type="file" multiple name="image" @change="handleFileChange"/>
				<!-- <v-select
					v-model="kanban.status"
					:items="kanban.statuses"
					class="select"
				/> -->
				<v-button class="submitButton" type="submit">Post</v-button>
			</form>
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
import { usePost } from "../../stores/page";
import VButton from "../atoms/v-button.vue";

const page = usePage();
const modal = useModal();
const post = usePost();
const inputType = "text";
const placeHolderTitle = "Enter message...";

const handleFileChange = (event) => {
      post.form.image = event.target.files;
}

const upload = () => {
	console.log("ASdassaas")
    const url = "http://localhost:9000/";
    const formData = new FormData()
    formData.append("message", post.form.message)
	formData.append("password", post.form.password)
    for (let i = 0; i < post.form.image.length; i++) {
    	formData.append("images[]", post.form.image[i]);
  	}
    console.log(post.form.image)
    fetch(url, {
        method: "POST",
        body: formData
    })
        .then((response) => {
            if(response.ok) {
				console.log(response)
                return response.ok
            }else{
                console.log("response status", response.status)
                console.log("response status", response.statusText)
                throw new Error("Network response was not ok")
            }
    })
        .then((data) => {
            //photo.fetchPhoto()
            console.log("success", data)
        })
        .catch((error) => {
            console.log(error)
        })
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
	background-color: var(--white);
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
