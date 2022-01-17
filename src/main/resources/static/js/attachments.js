let Attachment = function () {
}

Attachment.prototype = {
    id: 0,
    annexName: '',
    annexRead: 0,
    userId: 0,
    dateRead: new Date(),
    build: function (id, annexName, annexRead, userId, dateRead) {
        this.id = id;
        this.annexName = annexName;
        this.annexRead = annexRead;
        this.userId = userId;
        this.dateRead = dateRead;
    }
}

jQuery(document).ready(function ($) {
    checkAttachments(null);
    $('#read-attachment-table').modal({
        dismissible: false
    })
    $('#look').on('click', function () {
        $('#read-attachment').css('display', 'none');
        $('#read-attachment-table').modal('show')
    });

    $('#closed-projects').on('click', function (e) {
        e.preventDefault()
        getAttachments()
    });

    $(document).on('change', 'input[type="checkbox"]:checked', function () {
        let attachment = {
            id: $(this).data('annex-id')
        };
        $(this).attr('disabled', 'disabled');
        let d = new Date();
        let output = d.toLocaleDateString();
        $('#attachment-date-' + attachment.id).html(output);
        let attachmentId = $(this).data('annex-id');
        markRead(attachmentId);
    });
});

/**
 * Скрыть/отобразить окно с приложениями и необходимостью прочтения
 *
 * @param have
 */
function toggleAttachmentModal(have) {
    if (have === true) {
        getAttachments();
    } else {
        $("#read-attachment-table").modal('hide');
    }
}

/**
 * Проверить есть ли непрочитанные приложения к договорам инвестора
 *
 * @param login
 */
function checkAttachments(login) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let user = {
        login: login
    };

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "have-unread",
        data: JSON.stringify(user),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (have) {
            toggleAttachmentModal(have);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function (e) {
        })
}

function getAttachments() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "get-attachments",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            createAndShowAttachmentTable(data);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function (e) {
        })
}

function createAndShowAttachmentTable(attachments) {

    let body = $('div.modal__list');
    let strDate;
    body.empty();

    let header = createHeader()
    header.appendTo(body)

    $.each(attachments, function (i, el) {
        let annex = new Attachment();
        annex.build(el.id, el.annex.annexName, el.annexRead, el.userId, el.dateRead);

        let row = $('<div class="modal__list-row"></div>')
        let linkItem = $('<div class="modal__list-item"></div>')
        let link = $('<a class="link " href="/attachments/' + annex.id + '" target="_blank">' + annex.annexName + '</a>')
        link.appendTo(linkItem)
        linkItem.appendTo(row)

        strDate = annex.dateRead === null ? '' : new Date(annex.dateRead).toLocaleDateString();
        let dateItem = $('<div class="modal__list-item" id="attachment-date-' + annex.id + '">' + strDate + '</div>')
        dateItem.appendTo(row)

        let checked = annex.annexRead === 1 ? 'checked="checked"' : ''
        let disabled = annex.annexRead === 1 ? 'disabled="disabled"' : ''

        let checkboxItem = $('<div class="modal__list-item"></div>')
        let checkbox = $('' +
            '<label class="f-checkbox  ">' +
            '   <input type="checkbox" name="annex" data-annex-id="' + annex.id + '" ' + checked + ' ' + disabled + '>' +
            '       <span>' +
            '           <svg width="14" height="10" viewBox="0 0 14 10" fill="none" ' +
            '               xmlns="http://www.w3.org/2000/svg">' +
            '                   <path d="M0.872869 5.23438L5.35369 9.68324L13.4652 1.60369L12.1996 0.331676L5.35369 7.15199L2.11932 3.96236L0.872869 5.23438Z" fill="black"/>' +
            '           </svg>' +
            '       </span>' +
            '</label>')
        checkbox.appendTo(checkboxItem)
        checkboxItem.appendTo(row)
        row.appendTo(body)
    });
    $('#read-attachment-table').modal('show');
}

function createHeader() {
    return $('' +
        '<div class="modal__list-row">' +
        '   <div class="modal__list-item">Приложение</div>' +
        '   <div class="modal__list-item">Дата</div>' +
        '   <div class="modal__list-item">Ознакомлен</div>' +
        '</div>')
}

/**
 * Отметить приложение к договору, как прочитанное
 *
 * @param id
 */
function markRead(id) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let annex = {
        id: id
    };
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "mark-read",
        data: JSON.stringify(annex),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            let haveUnread = data.message === 'true'
            toggleAttachmentModal(haveUnread)
        })
        .fail(function (e) {
            console.log('Произошла ошибка - ' + e.toString());
        })
        .always(function () {
        })
}
