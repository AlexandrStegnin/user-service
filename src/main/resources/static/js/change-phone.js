let changePhoneForm;
let changePhoneLink;
let newPhoneField;
let oldPhoneField;
let changePhoneButton;
let confirmForm;
let confirmButton;

jQuery(document).ready(function ($) {
    changePhoneForm = $('#change-phone-form-modal')
    changePhoneLink = $('a#change-phone')
    newPhoneField = $('#new-phone')
    oldPhoneField = $('#old-phone')
    changePhoneButton = $('button#change-phone')
    confirmForm = $('#confirm-form-modal')
    confirmButton = $('#confirm')
    onChangePhoneClick()
    onChangePhoneFormSubmit()
    onConfirmPhone()
})

function onChangePhoneClick() {
    changePhoneLink.on('click', function (event) {
        event.preventDefault()
        changePhoneForm.modal('show')
    })
}

function onChangePhoneFormSubmit() {
    changePhoneButton.on('click', function () {
        let oldPhone = oldPhoneField.val().trim()
        if (oldPhone === '') {
            showMessage('ВВЕДИТЕ СТАРЫЙ ТЕЛЕФОН')
            return false
        }
        let newPhone = newPhoneField.val().trim()
        if (newPhone === '') {
            showMessage('ВВЕДИТЕ НОВЫЙ ТЕЛЕФОН')
            return false
        }
        if (oldPhone === newPhone) {
            showMessage('ВВЕДИТЕ РАЗНЫЕ НОМЕРА ТЕЛЕФОНОВ')
            return false
        }
        changePhoneForm.modal('hide')
        let isOld = confirmForm.find('#is-old').val()
        sendConfirmPhoneMessage(oldPhone, newPhone, isOld)
    })
}

function onConfirmPhone() {
    confirmButton.on('click', function (event) {
        event.preventDefault()
        let confirmCode = $('#confirm-code').val()

        if (confirmCode.length === 0) {
            showMessage("НЕОБХОДИМО УКАЗАТЬ КОД")
            return false
        }
        let isOld = $('#is-old').val() === 'true'
        let changePhoneDTO = getChangePhoneDTO(confirmCode)
        if (isOld) {
            checkConfirmCode(changePhoneDTO)
        } else {
            changePhone(changePhoneDTO)
        }
    })
}

function changePhone(changePhoneDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.post({
        url: "change-phone",
        data: JSON.stringify(changePhoneDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            if (data.status === 'OK') {
                confirmForm.modal('hide')
                showMessage(data.message.toUpperCase())
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

function getChangePhoneDTO(confirmCode) {
    return {
        oldPhone: oldPhoneField.val().trim(),
        newPhone: newPhoneField.val().trim(),
        confirmCode: confirmCode,
        bitrixId: $('#client-id').val()
    }
}

function sendConfirmPhoneMessage(oldPhone, newPhone, isOld) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let url

    let changePhoneDTO

    if (isOld) {
        url = 'confirm-old-phone'
    } else {
        url = "confirm-new-phone"
    }
    changePhoneDTO = {
        oldPhone: oldPhone,
        newPhone: newPhone
    }

    $.post({
        url: url,
        data: JSON.stringify(changePhoneDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            if (data.status === 'OK') {
                showConfirmForm(isOld)
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

function checkConfirmCode(changePhoneDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.post({
        url: 'check-confirm-code',
        data: JSON.stringify(changePhoneDTO),
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            if (data.status === 'OK') {
                sendConfirmPhoneMessage(changePhoneDTO.oldPhone, changePhoneDTO.newPhone, false)
            } else {
                showMessage('КОД ПОДТВЕРЖДЕНИЯ УКАЗАН НЕ ВЕРНО')
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

function showConfirmForm(isOld) {
    let title = isOld ? 'ПОДТВЕРДИТЕ СТАРЫЙ НОМЕР' : 'ПОДТВЕРДИТЕ НОВЫЙ НОМЕР'
    confirmForm.find('#is-old').val(isOld)
    confirmForm.find('#title').text(title)
    confirmForm.find('#confirm-code').val('')
    confirmForm.modal('show')
}
