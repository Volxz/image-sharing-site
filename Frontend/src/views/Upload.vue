<template>
  <v-container class="upload pa-11">
    <v-form
        ref="form"
        v-model="valid"
        lazy-validation
    >
      <v-text-field
          v-model="title"
          :rules="[rules.required, rules.min, rules.max]"
          label="Name"
          required
      ></v-text-field>


      <v-file-input
          accept="image/png"
          v-model="file"
          :rules="[rules.required]"
          label="File input"
      ></v-file-input>

      <v-btn
          :disabled="!valid"
          color="success"
          class="mr-4"
          @click="submit"
      >
        Submit
      </v-btn>
    </v-form>
  </v-container>
</template>

<script>
// @ is an alias to /src
import axios from 'axios';

export default {
  name: 'Home',
  data: () => ({
    valid: true,
    title: '',
    file: '',
    rules: {
      required: value => !!value || 'Required.',
      min: v => v.length >= 3 || 'Min 3 characters',
      max: v => v.length <= 30 || 'Max 30 characters',
    },
  }),

  methods: {
    submit() {
      this.$refs.form.validate();
      let formData = new FormData();
      formData.append("file", this.file);
      formData.append("title", this.title);
      let _this = this;
      axios.post('/api/i', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then((data)=>{
        _this.$router.push(data.headers.location.split('/api')[1]);
        console.log(data.headers);
      })
    },
  },
}
</script>