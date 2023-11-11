<template>
   
    <VList :tag="listTag">
        <VListItem :tag="listTagItem" v-for="p in post.posts">
            <div class="posts">
                <div class="delete">
                    <VButton @click="post.deletePost(p.id)">
                        x
                    </VButton>
                </div>
                <p>{{p.message}}</p>
                <VList :tag="listTag">
                    <div class="images">
                        <VListItem :tag="listTagItem" v-for="image in post.images">
                            <img v-if="image.postId===p.id" :src="imageData(image.image, image.extension)" class="image">
                        </VListItem>
                    </div>
                </VList>
                <div class="comments">
                    <VList :tag="listTag">
                        <VListItem :tag="listTagItem" v-for="comment in post.comments">
                            <p v-if="comment.postId===p.id">{{comment.message}}</p>
                        </VListItem>
                    </VList>
                    <form class="addForm" method="post" @submit.prevent="submitComment()" enctype="multipart/form-data">
                        <div class="input-comment">
                            <VInput
                            class="input"
                            :inputType="commentType"
                            :placeholderValue="commentPlaceHolder"
                            @change="post.formComment.message = $event.target.value"
                        />
                        </div>
                        <VButton type="submit" @click="selectedId = p.id">
                            Post
                        </VButton>
                    </form>
                </div>
            </div>
        </VListItem>
    </VList>
    
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import VList from '../atoms/v-list.vue';
import VListItem from '../atoms/v-list-item.vue';
import { usePost } from '@/stores/page';
import VInput from '../atoms/v-input.vue';
import VButton from '../atoms/v-button.vue';

const post = usePost()
const listTag="ul"
const listTagItem="li"

const commentType="text"
const commentPlaceHolder="Write a comment"

const selectedId = ref('')

const imageData = (imageByteArray: number[], extension: string) => {
    if (imageByteArray && imageByteArray.length > 0) {
      const blob = new Blob([new Uint8Array(imageByteArray)], { type: `image/${extension}` });
      return URL.createObjectURL(blob);
    }
}

const submitComment = () => {
    post.addComment(selectedId.value, post.formComment.message)
}
</script>

<style scoped>
    .posts {
        display: flex;
        flex-direction: column;
        width: fit-content;
        background-color: aqua;
    }
    .images {
        display: flex;
        flex-direction: column;
        background-color: black;
        width: fit-content;
    }

    .images img {
        width: 40vw;
    }
</style>