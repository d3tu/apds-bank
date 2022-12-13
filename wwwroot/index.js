var canvas = document.getElementById("gfx");
// const view = document.getElementById("view") as HTMLDivElement;
var ctx = canvas.getContext("2d");
var Gfx = /** @class */ (function () {
    function Gfx() {
    }
    Gfx.erase = function () {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.beginPath();
    };
    Gfx.drawRects = function (color, highestPoint, points) {
        ctx.strokeStyle = color;
        for (var i = 0, x = 0; i < points.length; i++, x += Math.abs(canvas.width / (points.length - 1))) {
            ctx.lineTo(x, canvas.height - Math.abs(canvas.height / highestPoint * points[i]));
            ctx.stroke();
        }
    };
    Gfx.resize = function () {
        var rect = canvas.getBoundingClientRect();
        canvas.width = rect.width;
        canvas.height = rect.height;
    };
    return Gfx;
}());
var fps = 0, last = Date.now();
// x.reduce((a, b) => Math.max(a, b), -Infinity);
Gfx.resize();
window.addEventListener("resize", Gfx.resize);
window.requestAnimationFrame(function frameCallback() {
    Gfx.erase();
    Gfx.drawRects("black", 100, [0, 50, 10, 5, 90, 25, 100]);
    var now = Date.now();
    fps = now - last;
    last = now;
    setTimeout(function () {
        window.requestAnimationFrame(frameCallback);
    }, Math.floor(1000 / fps));
});
