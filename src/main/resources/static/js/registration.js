let confirmModal;
let registration;
let submitButton;
let confirmButton;

jQuery(document).ready(function ($) {
    confirmModal = $('#confirm-form-modal');
    registration = $('#registration')
    submitButton = $('#submit')
    confirmButton = $('#confirm')
    onSubmitRegistration()
    onConfirmPhone()
})

function onSubmitRegistration() {
    registration.on('submit', function (event) {
        event.preventDefault()
        let agreementPersonalData = $('#agreement-personal-data').prop('checked')
        let agreementRules = $('#agreement-rules').prop('checked')
        if (!agreementPersonalData || !agreementRules) {
            console.log("Нужно принять правила и согласиться с обработкой и хранением ПД")
            return false
        }
        confirmModal.modal('show')
    })
}

function onConfirmPhone() {
    confirmButton.on('click', function (event) {
        event.preventDefault()
        let confirmCode = $('#confirm-code').val()
        if (confirmCode.length === 0) {
            console.log("Код не должен быть пустым")
            return false
        }
        updateUser(confirmCode)
    })
}

function updateUser(confirmCode) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let userDTO = {
        name: $('#name').val(),
        secondName: $('#secondName').val(),
        lastName: $('#lastName').val(),
        email: $('#email').val(),
        phone: $('#phone').val(),
        confirmCode: confirmCode,
        password: $('#password').val(),
        confirmPassword: $('#confirmPassword').val(),
        individual: $('#individual').prop('checked'),
        selfEmployed: $('#self-employed').prop('checked'),
        agreementPersonalData: $('#agreement-personal-data').prop('checked'),
        agreementRules: $('#agreement-rules').prop('checked')
    }

    $.post({
        url: "/confirm",
        data: JSON.stringify(userDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            confirmModal.modal('hide')
            window.location.href="/profile"
        })
        .fail(function (jqXHR) {
            console.log(jqXHR.responseText);
        })
        .always(function () {
            console.log("FINISHED")
        })
}
