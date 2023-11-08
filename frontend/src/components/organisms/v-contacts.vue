<template>

    <VList :tag="listTag" class="list-contacts">
        <VListItem :tag="listItemTag" v-for="contact in contact.contacts" :key="contact.id" >
            <div class="contact">
                <h3>Name</h3>
                <p>{{  `${contact.firstname ?? ""} ${contact.middlename ?? ""} ${contact.lastname ?? ""}` }}</p>
                <h4>Contact Info</h4>
                <p>{{ contact.phoneNumber }}</p>
                <p>{{ contact.email }}</p>
                <div class="actions">
                    <VButton @click="editContact(contact)">Edit</VButton>
                    <VButton @click="deleteContact(contact.id)">Delete</VButton>
                </div>
            </div>
        </VListItem>
    </VList>

    <VModalEditContact v-if="modal.openEditModal"/>
</template>

<script setup lang="ts">
import VList from "../atoms/v-list.vue";
import VListItem from "../atoms/v-list-item.vue";
import { useContact } from "@/stores/contacts";
import VButton from "../atoms/v-button.vue";
import VModalEditContact from "./v-modalEditContact.vue";
import { useModal } from "@/stores/page";

const modal = useModal()
const contact = useContact();
const listTag="ul"
const listItemTag="li"

const editContact = (selectedContact: contact.form) => {

    modal.openEditModal = true
    contact.form.id = selectedContact.id
     contact.form.firstname = selectedContact.firstname
     contact.form.middlename = selectedContact.middlename
     contact.form.lastname = selectedContact.lastname
     contact.form.phoneNumber = selectedContact.phoneNumber
     contact.form.email = selectedContact.email
     contact.form.organization = selectedContact.organization
}

const deleteContact = (id: string) => {
    contact.deleteContact(id)
}
</script>

<style scoped>

dl {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    border: 1px solid black;
}

dt, dd {
    border: 1px solid black;
}

.list-contacts {
    display: flex;
    flex-direction: row;
    gap: 1rem;
    list-style-type: none;
}

.contact {
    display: flex;
    flex-direction: column;
    width: 5rem;
    background-color: antiquewhite;
    padding: 2rem;
    border-radius: 10px;
}

.actions {
    display: flex;
    gap: .5rem;
}

.actions button{
    padding: 4%;
}
</style>