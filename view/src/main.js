import './assets/main.css'

import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import axios from "axios";
import {Vue} from "vitepress/vue-demi";

const app = createApp(App);
app.use(ElementPlus)


//const url = (await axios.get("./config.json")).data.url

axios.defaults.withCredentials = true

app.mount('#app')
