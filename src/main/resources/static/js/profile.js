jQuery(document).ready(function ($) {
    onSaveAdditionalFields()
})

function onSaveAdditionalFields() {
    $('#details').on('submit', function (e) {
        showSuccessForm('Ваши данные успешно отправлены. Вам придет смс об аккредитации, после чего Вы сможете инвестировать.')
    })
}
