const form  = document.getElementById("sign-up") as HTMLFormElement;

form.addEventListener("submit", e => {
  e.preventDefault();

  const formData = new FormData(form);
  const formBody: string[] = [];

  formData.forEach((val, key) => {
    formBody.push(`${key}=${val}`);
  });
  
  fetch("/sign-up", {
    method: "post",
    body: formBody.join("&")
  }).then(res => res.text()).then(res => {
    console.log(res);
  }).catch(rej => {
    console.log(rej);
  });
});
