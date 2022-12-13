var form = document.getElementById("sign-up");
form.addEventListener("submit", function (e) {
    e.preventDefault();
    var formData = new FormData(form);
    var formBody = [];
    formData.forEach(function (val, key) {
        formBody.push("".concat(key, "=").concat(val));
    });
    fetch("/sign-up", {
        method: "post",
        body: formBody.join("&")
    }).then(function (res) { return res.text(); }).then(function (res) {
        console.log(res);
    }).catch(function (rej) {
        console.log(rej);
    });
});
