/**
 * 显示修改密码Modal
 * */
function toChangePassword() {
    const html = 
        '<div class="modal fade" id="changePassword" tabindex="-1" role="dialog" aria-labelledby="changePasswordLabel">' +
            '<div class="modal-dialog">' +
                '<div class="modal-content" style="width: 600px; margin-top: 150px;">' +
                    '<div class="modal-header">' +
                        '<h6 class="modal-title" id="changePasswordLabel">| 修改密码</h6>' +
                    '</div>' +
                    '<div class="modal-body">' +
                        '<table style="width: 80%;">' +
                            '<tr>' +
                                '<td style="width: 90px; padding-right: 5px;"><label style="font-size: 14px; font-weight: normal;" class="control-label" for="originPassword">原密码</label></td>' +
                                '<td><input id="originPassword" class="form-control" type="password"></td>' +
                            '</tr>' +
                            '<tr>' +
                                '<td style="width: 90px; padding-right: 5px;"><label style="font-size: 14px; font-weight: normal;" class="control-label" for="newPassword">新密码</label></td>' +
                                '<td><input id="newPassword" class="form-control" type="password"></td>' +
                            '</tr>' +
                            '<tr>' +
                                '<td style="width: 90px; padding-right: 5px;"><label style="font-size: 14px; font-weight: normal;" class="control-label" for="newPasswordAgain">确认密码</label></td>' +
                                '<td><input id="newPasswordAgain" class="form-control" type="password"></td>' +
                            '</tr>' +
                        '</table>' +
                    '</div>' +
                    '<div class="modal-footer">' +
                        '<button type="button" class="btn btn-primary btn-sm" onclick="change()" style="background-color: #169BD5; width: 100px;">提交</button>' +
                        '<button type="button" class="btn btn-default btn-sm" style="width: 100px;" data-dismiss="modal">取消</button>' +
                    '</div>' +
                '</div><!-- /.modal-content -->' +
            '</div><!-- /.modal -->' +
        '</div>';
    $('.wrapper').append(html);
    $("#changePassword").modal('show');
}

/**
 * 修改密码
 * */
function change() {
    $.ajax({
        type: 'post',
        url: BASE_URL + '/admin/changePassword',
        contentType: 'application/json;charset=utf-8',
        data: JSON.stringify({
            originPassword: $("#originPassword").val().trim(),
            newPassword: $("#newPassword").val().trim(),
            newPasswordAgain: $("#newPasswordAgain").val().trim()
        }),
        success: function (resp) {
            if (resp.code === 0) {
                alert(resp.error);
                $("#changePassword").modal('hide');
            } else if (resp.code === 5001) {
                const errors = resp.data;
                let msg = '';
                if(errors.originPassword) {
                    msg = msg + errors.originPassword + " ";
                }
                if(errors.newPassword) {
                    msg = msg + errors.newPassword + " ";
                }
                alert(msg);
            } else {
                alert(resp.error);
            }
        }
    });
}