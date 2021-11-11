let restoreForm;
let phoneNumberField;
let submitRestoreButton;

jQuery(document).ready(function ($) {
    init()
    onRestoreFormSubmit()
})

function init() {
    restoreForm = $('#restore-password-form-modal')
    phoneNumberField = $('#phone-number')
    submitRestoreButton = $('#restore')
}

function onRestoreFormSubmit() {
    submitRestoreButton.on('click', function () {
        let phoneNumber = phoneNumberField.val().trim()
        if (phoneNumber === '') {
            showMessage('НОМЕР ТЕЛЕФОНА ДОЛЖЕН БЫТЬ УКАЗАН')
            return false
        }
        restoreForm.modal('hide')
        restorePassword(phoneNumber)
    })
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
        url: "/restore-password",
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
            showMessage(errorMessage)
        })
        .always(function () {
            console.log("FINISHED")
        })
}
