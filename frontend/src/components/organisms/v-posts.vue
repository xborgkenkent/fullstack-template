<template>
   
    <VList :tag="listTag">
        <VListItem :tag="listTagItem" v-for="posta in post.posts.image">
            <p>{{posta.id}}</p>
            <img :src="`http://localhost/image/${posta.id}`" alt="aa">
        </VListItem>
    </VList>
    
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import VList from '../atoms/v-list.vue';
import VListItem from '../atoms/v-list-item.vue';
import { usePost } from '@/stores/page';

const post = usePost()
const listTag="ul"
const listTagItem="li"

onMounted(() => {
	post.getPosts()
})

const imageData = computed(() => {
    
})
</script>