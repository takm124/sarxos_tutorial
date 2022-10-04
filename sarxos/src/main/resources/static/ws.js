$(document).ready(function() {
    let ws = new WebSocket("ws://127.0.0.1:9090/ws/cam");
    ws.onopen = function(e) {
        if (typeof console !== 'undefined') {
            console.info('WS open');
        }
    };

    ws.onmessage = function (e) {

        var data = JSON.parse(e.data),
            type = data.type,
            i = 0,
            $webcams = $('#webcams'),
            $img = null;

        if (typeof console !== 'undefined') {
            console.info('WS message', type);
        }

        if (type === 'image') {
            console.info('');
            var $img = $("img[name='" + "Integrated Webcam 0" + "']")
                .attr("src", "data:image/jpeg;base64," + data.image)
                .addClass('shadow')
                .trigger("change");
            setTimeout(function() {
                $img
                    .removeClass('shadow')
                    .trigger("change");
            }, 1000);
        }
    };

    ws.onclose = function() {
        if (typeof console !== 'undefined') {
            console.info('WS close');
        }
    };

    ws.onerror = function(err) {
        if (typeof console !== 'undefined') {
            console.info('WS error');
        }
    };
});
