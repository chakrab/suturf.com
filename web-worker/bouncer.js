let ball = document.querySelector('.ball');
let ball_pos = ball.getBoundingClientRect();
let board = document.querySelector('.board');
let board_rect = board.getBoundingClientRect();
let kmsg = document.querySelector('.kmessage');
let animated = false;

function moveBall(dx, dy, dxd, dyd) {
    if (animated) {
        if (ball_pos.top <= board_rect.top) {
            dyd = 1;
        }
        if (ball_pos.bottom >= board_rect.bottom) {
            dyd = 0;
        }
        if (ball_pos.left <= board_rect.left) {
            dxd = 1;
        }
        if (ball_pos.right >= board_rect.right) {
            dxd = 0;
        }

        ball.style.top = ball_pos.top + dy * (dyd == 0 ? -1 : 1) + 'px';
        ball.style.left = ball_pos.left + dx * (dxd == 0 ? -1 : 1) + 'px';
        ball_pos = ball.getBoundingClientRect();
        requestAnimationFrame(() => {
            moveBall(dx, dy, dxd, dyd);
        });
    }
}

const myWorker = new Worker('workerd.js');

document.addEventListener('keydown', (e) => {
    if (e.key == 's') {
        kmsg.innerHTML = 's';
        let dx = Math.floor(Math.random() * 4) + 3;
        let dy = Math.floor(Math.random() * 4) + 3;
        let dxd = Math.floor(Math.random() * 2);
        let dyd = Math.floor(Math.random() * 2);
        animated = true;

        moveBall(dx, dy, dxd, dyd);
    }
    else if (e.key == 'r') {
        kmsg.innerHTML = 'r';
        animated = false;
    }
    else if (e.key == 'b') {
        kmsg.innerHTML = 'b';
        let v = calc_primes(10000000);
        alert('Number of Primes: ' + v);
    }
    else if (e.key == 'w') {
        kmsg.innerHTML = 'w';
        if (window.Worker) {
            myWorker.postMessage(10000000);
        }
    }
    else if (e.key == 'x') {
        kmsg.innerHTML = 'x';
        if (window.Worker) {
            myWorker.terminate();
        }
    }
});

myWorker.onmessage = (e) => {
    alert('Number of Primes: ' + e.data);
}