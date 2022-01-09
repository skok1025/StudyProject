const log = console.log;
//const add10 = a => a + 10;
//const r = add10(10);
//log(r)
//
//const add5 = a => a +5;
//log(add5)
//log(add5(10))
//
//const f1 = () => () => 1;
//log(f1())
//
//const f2 = f1();
//log(f2)
//log(f2())

//const apply = f => f(1)
//const add2 = a => a + 2;
//log(apply(add2))
//
//const times = (f, n) => {
//  let i = -1;
//  while (++i < n) f(i)
//};
//
//times(log, 3)

const list = [1,2,3];
const str = 'abc'

//for (var i = 0; i < list.length; i++) {
//   log(list[i]);
//}
//
//for (var i = 0; i < str.length; i++) {
//   log(str[i]);
//}

for (const a of list) {
   log(a)
}

for (const a of str) {
   log(a)
}