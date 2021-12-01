jQuery(document).ready(function ($) {
    onSaveAdditionalFields()
})

function onSaveAdditionalFields() {
    let blocked = true
    $('#details').on('submit', function (e) {
        if (noErrors()) {
            if (blocked) {
                e.preventDefault()
                showSuccessForm('Ваши данные успешно отправлены. Вам придет смс об аккредитации, после чего Вы сможете инвестировать.')
                setTimeout(() => {
                    blocked = false
                    $('#details').submit()
                }, 2000)
            }
        } else {
            e.preventDefault()
        }
    })
}
