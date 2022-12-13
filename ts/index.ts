const canvas = document.getElementById("gfx") as HTMLCanvasElement;
// const view = document.getElementById("view") as HTMLDivElement;
const ctx = canvas.getContext("2d");

class Gfx {
  static erase() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.beginPath();
  }

  static drawRects(color: string, highestPoint: number, points: number[]) {
    ctx.strokeStyle = color;
    for (let i = 0, x = 0; i < points.length; i++, x += Math.abs(canvas.width / (points.length - 1))) {
      ctx.lineTo(x, canvas.height - Math.abs(canvas.height / highestPoint * points[i]));
      ctx.stroke();
    }
  }

  static resize() {
    const rect = canvas.getBoundingClientRect();
    canvas.width = rect.width;
    canvas.height = rect.height;
  }
}

var fps = 0, last = Date.now();

// x.reduce((a, b) => Math.max(a, b), -Infinity);

Gfx.resize();

window.addEventListener("resize", Gfx.resize);

window.requestAnimationFrame(function frameCallback() {
  Gfx.erase();
  Gfx.drawRects("black", 100, [0, 50, 10, 5, 90, 25, 100]);
  const now = Date.now();
  fps = now - last;
  last = now;
  setTimeout(() => {
    window.requestAnimationFrame(frameCallback);
  }, Math.floor(1000 / fps));
});