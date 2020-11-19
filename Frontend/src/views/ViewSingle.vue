<template>
  <v-container class="pa-11">
    <v-img
        v-bind:src="`/api/i/${imageId}.png`"
    ></v-img>
    <h1>Title</h1>
    <v-btn
        to="/"
        color="primary"
    >
      Return Home
    </v-btn>
  </v-container>
</template>

<script>
  // @ is an alias to /src
  import axios from 'axios';

  export default {
    name: 'Home',
    data() {
      return {
        name: "",
        imageId: this.$route.params.imageId
      }
    },
    mounted() {
      let _this = this;
      axios.get(`/api/i/${_this.imageId}`).then((body) => {
        _this.name = body.data.name;
      });
    },

  }
</script>