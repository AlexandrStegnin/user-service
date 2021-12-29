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
    // blurElement($('.out'), 4);
    // checkAttachments(null);
    $('#look').on('click', function () {
        $('#read-attachment').css('display', 'none');
        $('#read-attachment-table').modal('show')
    });

    $('#closed-projects').on('click', function (e) {
        e.preventDefault()
        getAttachments()
    });

    // $("#read-attachment-table").on("hidden.bs.modal", function () {
    //     let readAttachment = $('#read-attachment')
    //     if (readAttachment.css('z-index') !== '-1000001') {
    //         readAttachment.css('display', 'block');
    //     }
    // });
    $(document).on('change', 'input[type="checkbox"]:checked', function () {
        let userId = $(this).data('user-id');
        let attachment = {
            id: $(this).data('attachment-id')
        };
        $(this).attr('disabled', 'disabled');
        let d = new Date();
        let output = d.toLocaleDateString();
        $('#attachment-date-' + attachment.id).html(output);
        let trId = $(this).closest('tr').attr('id');
        markRead(trId, userId, attachment, 1);
    });
});

/**
 * Скрыть/отобразить окно с приложениями и необходимостью прочтения
 *
 * @param have
 */
function toggleAttachmentModal(have) {
    let readAttachment = $('#read-attachment');
    if (have === true) {
        getAttachments();
        blurElement($('.out'), 4);
        readAttachment.css('z-index', '1000001');
        readAttachment.css('display', 'block');
        $('#look').attr('disabled', false);
        disableScroll(true);
    } else {
        $("#read-attachment-table").modal('hide');
        blurElement($('.out'), 0);
        $('div.out').css('filter', '');
        readAttachment.css('z-index', '-1000001');
        readAttachment.css('display', 'none');
        disableScroll(false);
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

/**
 * Создать таблицу с приложениями
 *
 * @param attachments
 */
function createAndShowAttachmentTable(attachments) {

    let body = $('table#attachment-table').find('tbody');
    let tr, td, a, label, input, strDate;
    body.empty();

    $.each(attachments, function (i, el) {
        let annex = new Attachment();
        annex.build(el.id, el.annex.annexName, el.annexRead, el.userId, el.dateRead);
        tr = $('<tr></tr>');
        tr.attr('id', annex.id);
        tr.appendTo(body);

        td = $('<td></td>');
        td.data('attachment-name', annex.annexName);
        a = $('<a>' + annex.annexName + '</a>');
        a.attr('href', '/attachments/' + annex.id)
            .attr('target', '_blank');
        a.appendTo(td);
        td.appendTo(tr);

        label = $('<label></label>');
        label.attr('htmlFor', 'annexId' + annex.id);
        let checked = annex.annexRead === 1 ? 'checked="checked"' : ''
        let disabled = annex.annexRead === 1 ? 'disabled="disabled"' : ''
        input = $('<label class="f-checkbox ">' +
            '<input data-user-id="' + annex.userId +'" ' +
            'data-attachment-id="' + annex.id + '" ' +
            'data-attachment-name="' + annex.annexName + '" ' +
            'type="checkbox"' + checked + ' ' + disabled + '>' +
            '<span>✓</span>');
        td = $('<td style="text-align: center"></td>');
        label.appendTo(td);
        input.appendTo(td);
        td.appendTo(tr);

        strDate = annex.dateRead === null ? '' : new Date(annex.dateRead).toLocaleDateString();

        td = $('<td>' + strDate + '</td>');
        td.attr('id', 'attachment-date-' + annex.id);
        td.appendTo(tr);
    });
    $('#read-attachment-table').modal('show');
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
            // let haveUnread = data.message === 'true'
            // toggleAttachmentModal(haveUnread)
        })
        .fail(function (e) {
            console.log('Произошла ошибка - ' + e.toString());
        })
        .always(function () {
        })
}

function blurElement(element, size) {
    let filterVal = 'blur(' + size + 'px)';
    $(element).css({
        'filter': filterVal,
        'webkitFilter': filterVal,
        'mozFilter': filterVal,
        'oFilter': filterVal,
        'msFilter': filterVal,
        'transition': 'all 0.5s ease-out',
        '-webkit-transition': 'all 0.5s ease-out',
        '-moz-transition': 'all 0.5s ease-out',
        '-o-transition': 'all 0.5s ease-out'
    });
}

function disableScroll(disable) {
    let body = $('body');
    if (disable === true) {
        body.addClass('stop-scrolling');
        body.bind('touchmove', function(e){e.preventDefault()});
    } else {
        body.removeClass('stop-scrolling');
        body.unbind('touchmove');
    }
}
