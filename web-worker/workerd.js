importScripts("prime.js");

onmessage = (e) => {
    console.log('Got message with: ' + e.data);
    let v = findPrimes(e.data);
    console.log('Prime count: ' + v);
    postMessage(v);
};