
<!-- 鼠标相对于浏览器窗口的坐标 -->
function show_coords(event) {
    layui.use(['jquery'], function () {
        var $ = layui.$,layer = layui.layer;

        var e = document.getElementById('event');
        x = event.clientX;
        y = event.clientY;
        e.innerHTML = "(X:" + x + ",Y:" + y + ")";
    });
}
