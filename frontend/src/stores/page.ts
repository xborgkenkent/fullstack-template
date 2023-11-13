import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const usePage = defineStore("page", () => {
  const page = ref("Platform Launch");

  return { page };
});

export const useModal = defineStore("modal", () => {
  const openChannel = ref(false);

  return { openChannel };
});

export const usePublicChannel = defineStore("public-channel", () => {
  const channels = ref([
    {
      id: "",
      name: "",
      userId: "",
    },
  ]);

  const fetchChannels = () => {
    fetch("http://localhost:9000/group-channel", {
      credentials: "include",
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("network error response");
        }
      })
      .then((data) => {
        channels.value = data;
      });
  };

  return { channels, fetchChannels };
});

export const useChat = defineStore("chat", () => {
  const form = ref({
    id: "",
    name: "",
    userId: "",
  });

  const formMessage = ref([
    {
      toId: "",
      createdAt: 0,
      message: "",
      isGroup: false,
      fromId: "",
      id: "",
    },
  ]);
  const isGroup = ref(true);
  const fetchChannelInfo = (id: string, group: boolean) => {
    isGroup.value = group;
    fetch(`http://localhost:9000/group-channel/${id}`, {
      credentials: "include",
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("network error");
        }
      })
      .then((data) => {
        form.value = data[0];

        console.log("yawa" + isGroup.value);
        fetch(`http://localhost:9000/chat/${form.value.id}/${isGroup.value}`, {
          method: "GET",
          credentials: "include",
        })
          .then((response) => {
            if (response.ok) {
              return response.json();
            } else {
              throw new Error("network response error");
            }
          })
          .then((data) => {
            formMessage.value = data;
            console.log(data);
          });
      });
  };

  const addMessage = (id: string, message: string) => {
    const formData = new FormData();
    formData.append("id", id);
    formData.append("message", message);
    formData.append("isGroup", isGroup.value);
    fetch("http://localhost:9000/chat", {
      method: "POST",
      credentials: "include",
      body: formData,
    }).then((response) => {
      if (response.ok) {
        return response.ok;
      } else {
        throw new Error("network error response");
      }
    });
  };
  return { form, fetchChannelInfo, addMessage, isGroup, formMessage };
});
