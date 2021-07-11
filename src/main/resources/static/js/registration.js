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
        confirmPhone(confirmCode)
    })
}

function confirmPhone(confirmCode) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let confirmPhoneDTO = {
        confirmCode: confirmCode
    }

    $.post({
        url: "/confirm",
        data: JSON.stringify(confirmPhoneDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            confirmModal.modal('hide')
            console.log("SUCCESS " + data.message)
        })
        .fail(function (jqXHR) {
            console.log(jqXHR.responseText);
        })
        .always(function () {
            console.log("FINISHED")
        })

}
