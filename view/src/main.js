import './assets/main.css'

import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './components/App.vue'
import axios from "axios";
import {Vue} from "vitepress/vue-demi";
import router from './router/index.js'

const app = createApp(App);
app.use(ElementPlus)
app.use(router)

//const url = (await axios.get("./config.json")).data.url

axios.defaults.withCredentials = true

app.mount('#app')
