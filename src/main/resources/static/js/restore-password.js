let restoreForm;
let phoneNumberField;
let submitRestoreButton;
let restorePasswordButton;
let messageForm;

jQuery(document).ready(function ($) {
    init()
    onRestorePasswordClick()
    onRestoreFormSubmit()
})

function init() {
    restorePasswordButton = $('#restore-password')
    restoreForm = $('#restore-password-form-modal')
    phoneNumberField = $('#phone-number')
    submitRestoreButton = $('#restore')
    messageForm = $('#message-form-modal')
}

function onRestorePasswordClick() {
    restorePasswordButton.on('click', function (event) {
        event.preventDefault()
        restoreForm.modal('show')
    })
}

function onRestoreFormSubmit() {
    submitRestoreButton.on('click', function () {
        let phoneNumber = phoneNumberField.val().trim()
        if (phoneNumber === '') {
            console.log('Номер телефона должен быть указан')
            return false
        }
        if (validPhoneNumber(phoneNumber)) {
            console.log('Телефон ' + phoneNumber)
            restoreForm.modal('hide')
            restorePassword(phoneNumber)
        }
    })
}

function validPhoneNumber(phone) {
    let pattern = /^\+?([0-9]{11})$/;
    if(phone.match(pattern)) {
        return true;
    }
    else {
        alert("Введите номер телефона в формате +79999999999");
        return false;
    }
}

/**
 *
 * @param phoneNumber {String} номер телефона
 */
function restorePassword(phoneNumber) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let userDTO = {
        phone: phoneNumber
    }

    $.post({
        url: "restore-password",
        data: JSON.stringify(userDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            if (data.status === 200) {
                showMessage('ВАМ ОТПРАВЛЕНО СМС С ВРЕМЕННЫМ ПАРОЛЕМ. ИСПОЛЬЗУЙТЕ ЕГО ДЛЯ АВТОРИЗАЦИИ.')
            }
        })
        .fail(function (jqXHR) {
            let errorMessage = (jqXHR.responseJSON.message).toUpperCase()
            console.log(errorMessage);
            showMessage(errorMessage)
        })
        .always(function () {
            console.log("FINISHED")
        })
}

/**
 *
 * @param message {String}
 */
function showMessage(message) {
    let divMessage = messageForm.find('#message')
    divMessage.text(message)
    messageForm.modal('show')
}
