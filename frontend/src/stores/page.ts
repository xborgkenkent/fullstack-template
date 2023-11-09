import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const usePage = defineStore("page", () => {
  const page = ref("Platform Launch");

  return { page };
});

export const useModal = defineStore("modal", () => {
  const open = ref(false);

  return { open };
});

export const usePost = defineStore("posts", () => {
  const form = ref([
    {
      message: "",
      password: "",
      image: "",
    },
  ]);

  const post = {
    id: "",
    message: "",
    password: "",
    createdAt: "",
  };

  const image = {
    id: "",
    postId: "",
    image: "",
    extension: "",
  };
  const posts = ref({
    p: post,
    i: image,
  });
  const getPosts = () => {
    fetch("http://localhost:9000/posts")
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("network error");
        }
      })
      .then((data) => {
        console.log(data);
        posts.value = data;
        //posts.value = data
      });
  };
  return { form, getPosts, posts };
});
