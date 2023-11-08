import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useContact = defineStore("contacts", () => {
  const contacts = ref([
    {
      id: "",
      firstname: "",
      middlename: "",
      lastname: "",
      phoneNumber: "",
      email: "",
      organization: "",
    },
  ]);

  const form = ref({
    id: "",
    firstname: "",
    middlename: "",
    lastname: "",
    phoneNumber: "",
    email: "",
    organization: "",
  });
  const getContacts = () => {
    fetch("http://localhost:9000/tables").then((response) => response.ok);

    fetch("http://localhost:9000/contacts")
      .then((response) => response.json())
      .then((data) => (contacts.value = data));
  };

  const addContact = (
    firstName: string,
    middleName: string,
    lastName: string,
    phoneNumber: string,
    email: string,
    organization: string
  ) => {
    const formData = new FormData();
    formData.append("firstName", firstName);
    formData.append("middleName", middleName);
    formData.append("lastName", lastName);
    formData.append("phoneNumber", phoneNumber);
    formData.append("email", email);
    formData.append("organization", organization);

    fetch("http://localhost:9000/contacts", {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("network error");
        }
      })
      .then((data) =>
        contacts.value.push({
          id: "",
          firstname: firstName,
          middlename: middleName,
          lastname: lastName,
          phoneNumber: phoneNumber,
          email: email,
          organization: organization,
        })
      )
      .then((error) => console.log(error));
  };

  const deleteContact = (id: string) => {
    fetch(`http://localhost:9000/contacts/${id}`, {
      method: "GET",
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("network error");
        }
      })
      .then((data) => {
        const index = contacts.value.findIndex((contact) => contact.id === id);
        if (index !== 1) {
          contacts.value.splice(index, 1);
        }
      });
  };

  const editContact = (
    id: string,
    firstName: string,
    middleName: string,
    lastName: string,
    phoneNumber: string,
    email: string,
    organization: string
  ) => {
    const formData = new FormData();
    formData.append("firstName", firstName);
    formData.append("middleName", middleName);
    formData.append("lastName", lastName);
    formData.append("phoneNumber", phoneNumber);
    formData.append("email", email);
    formData.append("organization", organization);

    fetch(`http://localhost:9000/contacts/${id}`, {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("network error");
        }
      })
      .then((data) => {
        contacts.value.map((contact) => {
          if (contact.id === id) {
            contact.firstname = firstName;
            contact.middlename = middleName;
            contact.lastname = lastName;
            contact.phoneNumber = phoneNumber;
            contact.email = email;
            contact.organization = organization;
          }
        });
      })
      .then((error) => console.log(error));
  };

  return {
    form,
    contacts,
    getContacts,
    addContact,
    deleteContact,
    editContact,
  };
});
