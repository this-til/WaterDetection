import {createRouter, createWebHistory} from 'vue-router'
import NotFound from "@/components/NotFound.vue";
import Login from "@/components/Login.vue";
import Content from "@/components/Content.vue";

const routes = [
    {
        path: "/:catchAll(.*)",
        component: NotFound,
    },
    {
        path: "/login",
        component: Login,
    },
    {
        path: "/content",
        component: Content,
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router

