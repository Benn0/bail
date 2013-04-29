
$(document).ready(function () {
    $('body').layout({

        //applyDefaultStyles: true,

        north: {
            paneSelector: ".north",
            resizable:false,
            closable: false,
            slidable: false,
            spacing_open: 0,
            minSize: 60
        },

        west: {
            paneSelector: ".west",
            slidable: false,
            minSize: 300,
            closable: false
        },

        center: {
            paneSelector: ".center",
            contentSelector: ".content"
        }
    });
});
