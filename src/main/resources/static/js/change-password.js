let changePasswordForm;
let oldPasswordField;
let newPasswordField;
let confirmPasswordField;
let changePasswordButton;
let changePasswordLink;

jQuery(document).ready(function ($) {
    changePasswordLink = $('a#change-password')
    changePasswordForm = $('#change-password-form-modal')
    oldPasswordField = $('#old-password')
    newPasswordField = $('#new-password')
    confirmPasswordField = $('#confirm-password')
    changePasswordButton = $('button#change-password')
    onChangePasswordClick()
    onChangeFormSubmit()
    onShowPasswordClick()
})

function onChangePasswordClick() {
    changePasswordLink.on('click', function (event) {
        event.preventDefault()
        changePasswordForm.modal('show')
    })
}

function onChangeFormSubmit() {
    changePasswordButton.on('click', function () {
        let oldPassword = oldPasswordField.val().trim()
        if (oldPassword === '') {
            showMessage('ВВЕДИТЕ СТАРЫЙ ПАРОЛЬ')
            return false
        }
        let newPassword = newPasswordField.val().trim()
        if (newPassword === '') {
            showMessage('ВВЕДИТЕ НОВЫЙ ПАРОЛЬ')
            return false
        }
        let confirmPassword = confirmPasswordField.val().trim()
        if (confirmPassword === '') {
            showMessage('ПОДТВЕРДИТЕ НОВЫЙ ПАРОЛЬ')
            return false
        }
        if (newPassword !== confirmPassword) {
            showMessage('ПАРОЛИ НЕ СОВПАДАЮТ')
            return false
        }
        changePasswordForm.modal('hide')
        changePassword(oldPassword, newPassword)
    })
}

/**
 *
 * @param oldPassword {String} старый пароль
 * @param newPassword {String} новый пароль
 */
function changePassword(oldPassword, newPassword) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let changePasswordDTO = {
        phone: $('b#phone').text().trim(),
        oldPassword: oldPassword,
        newPassword: newPassword
    }

    $.post({
        url: "change-password",
        data: JSON.stringify(changePasswordDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            if (data.status === 'OK') {
                showSuccessForm(data.message.toUpperCase())
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

function onShowPasswordClick() {
    $('#old-password-show').on('click', function (event) {
        event.preventDefault()
        togglePasswordVisible('#old-password')
    })
    $('#new-password-show').on('click', function (event) {
        event.preventDefault()
        togglePasswordVisible('#new-password')
    })
    $('#confirm-password-show').on('click', function (event) {
        event.preventDefault()
        togglePasswordVisible('#confirm-password')
    })
}

function togglePasswordVisible(inputId) {
    let eyeClass = 'fas fa-eye'
    let eyeSlashClass = 'fas fa-eye-slash'
    let inputType = $(inputId).attr('type')
    console.log(inputType)
    let type = inputType === 'password' ? 'text' : 'password'
    let spanClass = type === 'password' ? eyeClass : eyeSlashClass
    $(inputId).attr('type', type)
    $(inputId + '-span').removeClass(eyeClass)
    $(inputId + '-span').removeClass(eyeSlashClass)
    $(inputId + '-span').addClass(spanClass)
}
