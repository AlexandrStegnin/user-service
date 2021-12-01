jQuery(document).ready(function ($) {
    onSaveAdditionalFields()
})

function onSaveAdditionalFields() {
    let blocked = true
    let details = $('#details')
    details.on('submit', function (e) {
        if (noErrors()) {
            if (blocked) {
                e.preventDefault()
                showSuccessForm('Ваши данные успешно отправлены. Вам придет смс об аккредитации, после чего Вы сможете инвестировать.')
                window.setTimeout(function () {
                    blocked = false
                    details.unbind('submit')
                    $('#submit').click()
                }, 1000)
            }
        } else {
            e.preventDefault()
        }
    })
}
