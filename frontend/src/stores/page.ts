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

  const formComment = ref({
    message: "",
  });

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

  const comment = ref({
    id: "",
    postId: "",
    message: "",
    createdAt: "",
  });

  const posts = ref({
    p: Array<typeof post>(),
    i: Array<typeof image>(),
    c: Array<typeof comment>(),
  });
  const getPosts = () => {
    fetch("http://10.11.0.230:9000/posts")
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

  const addComment = (id: string, message: string) => {
    const formData = new FormData();
    formData.append("comment", message);
    fetch(`http://10.11.0.230:9000/comment/${id}`, {
      method: "POST",
      body: formData,
    }).then((response) => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error("network error");
      }
    });
  };

  const deletePost = (id: string) => {
    fetch(`http://10.11.0.230:9000/posts/${id}`, {
      method: "GET",
    }).then((response) => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error("network error");
      }
    });
  };
  return { form, getPosts, posts, addComment, formComment, deletePost };
});
