Ext.define("core.public.SelectRoom.controller.SelectRoomController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.pubselect.selectroomcontroller',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function () {
    },
    control: {
        "mtfuncwindow[funcPanel=pubselect.selectroomlayout] button[ref=ssOkBtn]": {
            beforeclick: function (btn) {
                return ;
            }
        }
    }
});