let messageForm;

jQuery(document).ready(function ($) {
    messageForm = $('#message-form-modal')
})

/**
 * Показать сообщение
 *
 * @param message {String}
 */
function showMessage(message) {
    let divMessage = messageForm.find('#message')
    divMessage.text(message)
    messageForm.modal('show')
}


function onEnterKeyPressed() {
    $(document).keypress(function(e) {
        if ($('#confirm-form-modal').hasClass('show')
            && (e.keycode === 13 || e.which === 13)) {
            e.preventDefault()
            confirmButton.click()
        }
    });
}
