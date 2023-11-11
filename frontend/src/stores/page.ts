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

  const posts = ref([{
    id: "",
    message: "",
    password: "",
    createdAt: "",
  }]);

  const images = ref([{
    id: "",
    postId: "",
    image: "",
    extension: "",
  }]);

  const comments = ref([{
    id: "",
    postId: "",
    message: "",
    createdAt: "",
  }]);


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
     
        //posts.value = data
      });
  };

  const addComment = (id: string, message: string) => {
    const formData = new FormData();
    formData.append("comment", message);
    fetch(`http://localhost:9000/comment/${id}`, {
      method: "POST",
      body: formData,
    }).then((response) => {
      if (response.ok) {
        return response.ok;
      } else {
        throw new Error("network error");
      }
    });
  };

  const deletePost = (id: string) => {
    fetch(`http://localhost:9000/posts/${id}`, {
      method: "GET",
    }).then((response) => {
      if (response.ok) {
        return response.ok;
      } else {
        throw new Error("network error");
      }
    });
  };
  return { form, getPosts, posts, images, comments, addComment, formComment, deletePost };
});
