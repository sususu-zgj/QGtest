<template>
    <form @submit.prevent="submit">
        <div>
            <input type="text" v-model="username" placeholder="用户名" required>
        </div>
        <div>
            <input type="text" v-model="password" placeholder="密码" required>
        </div>
        <div>
            <input type="text" v-model="checkPassword" placeholder="确认密码" required>
        </div>
        <div>
            <input type="text" v-model="phone" placeholder="手机号" required>
        </div>
        <div>
            <button type="submit">注册</button>
        </div>
    </form>
</template>

<script lang="ts">
    export default {
        name: "Register"
    }
</script>

<script lang="ts" setup>
import { ref } from 'vue';
    let username = ref("")
    let password = ref("")
    let checkPassword = ref("")
    let phone = ref("")

    function submit() {
        if (password.value != checkPassword.value) {
            alert("前后密码不一致！")
            return;
        }
        alert("注册成功!")
    }
</script>

<style scoped>
    button {
        padding: 8px;
        background-color: #bfd3e0;
        border-color: transparent;
        border-radius: 10px;
        margin-left: 100px;
        margin-top: 30px;
        width: 192px;
        transition: background-color 0.5s;
    }
    button:hover {
        cursor: pointer;
        background-color: rgb(121, 196, 234);
    }
    input {
        width: 300px;
        height: 32px;
        margin-left: 50px;
        margin-top: 40px;
        padding: 0.5rem;
        border: 1px solid #ccc;
        border-radius: 4px;
    }
</style>
