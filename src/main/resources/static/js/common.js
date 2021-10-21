let messageForm
let confirmForm

jQuery(document).ready(function ($) {
    messageForm = $('#message-form-modal')
    confirmForm = $('#confirm-form-modal')
    onRetrySendClick()
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

function onRetrySendClick() {
    $(document).on('click', 'a.retry-send', function (event) {
        event.preventDefault()
        confirmModal.find('#response-state').removeClass('error')
        confirmModal.find('#confirm-code').val('')
        retrySendConfirmMessage()
    })
}

function retrySendConfirmMessage() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let userDTO = getUserDTO(null, null)

    $.post({
        url: "retry-send",
        data: JSON.stringify(userDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
        })
        .fail(function (jqXHR) {
            console.log(jqXHR.responseText);
        })
        .always(function () {
            console.log("FINISHED")
        })
}

function showSuccessForm(title) {
    confirmForm.find('#success-text').text(title)
    confirmForm.find('#response-state').addClass('success')
    confirmForm.modal('show')
}

function showFailForm(title) {
    confirmForm.find('#fail-text').text(title)
    confirmForm.find('#response-state').addClass('error')
    confirmForm.modal('show')
}
