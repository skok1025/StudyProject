const log = console.log;
// 제너레이터/ 이터레이터
// - 제너레이터 : 이터레이터이자 이터러블을 생성하는 함수

function *gen() {
    yield 1;
    if (false) yield 2;
    yield 3;
}

let iter = gen();
log(iter.next())
log(iter.next())
log(iter.next())
log(iter.next())
log(iter.next())

for (const a of gen()) log(a);

console.clear();

function *odd(l) {
    for (let i = 0; i < l; i++) {
        if(i % 2) yield i;
    }
}

let iter2 = odd(10);
log(iter2.next());
log(iter2.next());
log(iter2.next());
log(iter2.next());
log(iter2.next());
log(iter2.next());

function *infinity(i =0) {
    while (true) yield i++;
}

let iter3 = infinity();
log(iter3.next());
log(iter3.next());
log(iter3.next());
log(iter3.next());
log(iter3.next());
log(iter3.next());
log(iter3.next());