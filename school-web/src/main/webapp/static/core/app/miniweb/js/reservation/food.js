$(function () {
    $.ajax({
        cache: false,
        type: "post",
        processData: false,
        contentType: false,
        url: $("#contextPath").val() + "/app/Reservation/orderDesc",
        success: function (obj) {
            var jsonList = JSON.parse(obj);
            $("#table").append(jsonList[13]);
            //文字说明
            var txt = $("#table").children("div").html();
            //获取表数据
            var table = $("#table").children("table").children("tbody").find("tr").children("td").map(function () {
                return $(this).text()
            }).get();
            $("#content").append('<p>' + txt + '</p>');

            var str = '';
            str += ' <tbody>';
            for (var i = 0; i <= table.length - 3; i++) {
                if (i == 0) {
                    str += '<tr>';
                } else if (i == table.length - 1) {
                    str += '<tr>';
                } else if (i % 3 == 0) {
                    str += '</tr><tr>';
                }
                if (i == 0) {
                    str += '<td style=" width: 23%; color: #18b4ed;">' + table[i] + '</td>';
                } else if (i == 1) {
                    str += '<td style=" color: #04be02;">' + table[i] + '</td>';
                } else if (i == 2) {
                    str += '<td style=" color: #ed9627;">' + table[i] + '</td>';
                }
                else if (i >= 6 || i < 3) {
                    str += '<td>' + table[i] + '</td>';
                }
            }
            str += '</tbody>';
            $("#weitable").append(str);
        }, error: function (data) {
        }
    });

})

