let confirmModal;
let registration;
let submitButton;
let confirmButton;

jQuery(document).ready(function ($) {
    confirmModal = $('#confirm-form-modal');
    registration = $('#registration')
    submitButton = $('#submit')
    confirmButton = $('#confirm')
    // confirmButton.removeClass('disabled')
    // confirmButton.prop('disabled', false)
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
        create()
    })
}

function onConfirmPhone() {
    confirmButton.on('click', function (event) {
        event.preventDefault()
        let confirmCode = $('#confirm-code').val()
        let clientBitrixId = $('#client-id').val()
        let clientSecret = $('#client-secret').val()
        if (confirmCode.length === 0) {
            console.log("Код не должен быть пустым")
            return false
        }
        // confirmButton.addClass('disabled')
        // confirmButton.prop('disabled', true)
        confirm(confirmCode, clientBitrixId, clientSecret)
    })
}

function create() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let userDTO = getUserDTO(null, null, null)

    $.post({
        url: "create",
        data: JSON.stringify(userDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            $('#client-id').val(data.message)
            $('#client-secret').val(data.additionalInfo)
            confirmModal.modal('show')
        })
        .fail(function (jqXHR) {
            console.log(jqXHR.responseText);
        })
        .always(function () {
            console.log("FINISHED")
        })
}
function confirm(confirmCode, clientBitrixId, additionalInfo) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let userDTO = getUserDTO(confirmCode, clientBitrixId, additionalInfo)

    $.post({
        url: "confirm",
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

function getUserDTO(confirmCode, clientBitrixId, additionalInfo) {
    return {
        name: $('#name').val(),
        secondName: $('#secondName').val(),
        lastName: $('#lastName').val(),
        email: $('#email').val(),
        phone: $('#phone').val(),
        confirmCode: confirmCode,
        individual: $('#individual').prop('checked'),
        selfEmployed: $('#self-employed').prop('checked'),
        agreementPersonalData: $('#agreement-personal-data').prop('checked'),
        agreementRules: $('#agreement-rules').prop('checked'),
        bitrixId: clientBitrixId,
        password: additionalInfo
    }
}
