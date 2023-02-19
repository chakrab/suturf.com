/*
This is an inefficient solution for calculating the number
of primes when given a number. I is just used for killing
some quality time
*/
function isPrime(n) {
    for (let i = 2; i <= Math.sqrt(n); i++) {
        if (n % i === 0) {
            return false;
        }
    }

    return n > 1;
}

function findPrimes(maxnum) {
    const primes = [];
    for (let i = 2; i <= maxnum; i++) {
        if (isPrime(i)) {
            primes.push(i);
            console.log(i);
        }
    }

    return primes.length;
}