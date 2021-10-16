let confirmModal;
let registration;
let submitButton;
let confirmButton;

jQuery(document).ready(function ($) {
    init()
    onSubmitRegistration()
    onConfirmPhone()
    onEnterKeyPressed()
    onRetrySendClick()
})

function init() {
    confirmModal = $('#confirm-form-modal');
    registration = $('#registration')
    submitButton = $('#submit')
    confirmButton = $('#confirm')
}

function onSubmitRegistration() {
    registration.on('submit', function (event) {
        event.preventDefault()
        let agreementPersonalData = $('#agreement-personal-data').prop('checked')
        let agreementRules = $('#agreement-rules').prop('checked')
        if (!agreementPersonalData) {
            showMessage("Нужно согласие с обработкой и хранением персональных данных")
            return false
        }
        if (!agreementRules) {
            showMessage("Нужно принять правила платформы")
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
        if (confirmCode.length === 0) {
            console.log("Код не должен быть пустым")
            return false
        }
        confirm(confirmCode, clientBitrixId)
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
            confirmModal.modal('show')
        })
        .fail(function (jqXHR) {
            console.log(jqXHR.responseText);
        })
        .always(function () {
            console.log("FINISHED")
        })
}

function confirm(confirmCode, clientBitrixId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let userDTO = getUserDTO(confirmCode, clientBitrixId)

    $.post({
        url: "confirm",
        data: JSON.stringify(userDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            confirmModal.find('#response-state').removeClass('error')
            confirmModal.modal('hide')
            window.location.href="/profile"
        })
        .fail(function (jqXHR) {
            confirmModal.find('#response-state').addClass('error')
            console.log(jqXHR.responseJSON.message)
            console.log(jqXHR.status)
        })
        .always(function () {
            console.log("FINISHED")
        })
}

function onRetrySendClick() {
    $('a.retry-send').on('click', function (event) {
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

function getUserDTO(confirmCode, clientBitrixId) {
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
        bitrixId: clientBitrixId
    }
}
