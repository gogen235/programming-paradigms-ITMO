const curry = (x, y, z) => a => a(x, y, z);
const makeOperation = f => (...args) => (x, y, z) => f(...args.map(curry(x, y, z)))

const cnst = (arg) => () => arg;
const one = cnst(1);
const two = cnst(2);
const variable = (arg) => (x, y, z) => arg === "x" ? x : arg === "y" ? y : z;
const negate = makeOperation((a) => -a);
const add = makeOperation((a, b) => a + b);
const subtract = makeOperation((a, b) => a - b);
const multiply = makeOperation((a, b) => a * b);
const divide = makeOperation((a, b) => a / b);
const argMin3 = makeOperation((...args) => args.indexOf(Math.min(...args)))
const argMax3 = makeOperation((...args) => args.indexOf(Math.max(...args)))
const argMin5 = argMin3;
const argMax5 = argMax3;

const getExpr = {
    "argMax5": [argMax5, 5],
    "argMax3": [argMax3, 3],
    "argMin5": [argMin5, 5],
    "argMin3": [argMin3, 3],
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "negate": [negate, 1]
}
const parse = (expr) => {
    let stack = [];
    let exprList = expr.trim().split(/\s+/);
    for (let item of exprList) {
        if (item in getExpr) {
            stack.push(getExpr[item][0](...stack.splice(-getExpr[item][1])));
        } else {
            switch(item) {
                case "x":
                case "y":
                case "z":
                    stack.push(variable(item));
                    break;
                case "one":
                    stack.push(one);
                    break;
                case "two":
                    stack.push(two);
                    break;
                default:
                    stack.push(cnst(Number(item)));
            }
        }
    }
    return stack.pop();
}

expr = parse("x x * 2 x * - 1 +");
for (let i = 0; i <= 10; i++) {
    println(expr(i, 0, 0));
}

