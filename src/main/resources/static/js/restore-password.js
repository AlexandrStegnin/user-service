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
    submitRestoreButton.on('click', function (event) {
        let phoneNumber = phoneNumberField.val().trim()
        if (phoneNumber === '') {
            console.log('Номер телефона должен быть указан')
            return false
        }
        console.log('Телефон ' + phoneNumber)
        restoreForm.modal('hide')
        messageForm.modal('show')
    })
}

