let restoreForm;
let phoneNumberField;
let submitRestoreButton;
let restorePasswordButton;

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
            showMessage('НОМЕР ТЕЛЕФОНА ДОЛЖЕН БЫТЬ УКАЗАН')
            return false
        }
        if (validPhoneNumber(phoneNumber)) {
            restoreForm.modal('hide')
            restorePassword(phoneNumber)
        } else {
            showMessage('ВВЕДИТЕ НОМЕР ТЕЛЕФОНА В ФОРМАТЕ +79999999999')
        }
    })
}

function validPhoneNumber(phone) {
    let pattern = /^\+?([0-9]{11})$/;
    return !!phone.match(pattern);
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
        }
    })
        .done(function (data) {
            if (data.status === 'OK') {
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
