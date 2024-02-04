function Const(value) {
    this.value = value;
}
Const.prototype.evaluate = function() {
    return this.value;
}
Const.prototype.diff = function() {
    return new Const(0);
}
Const.prototype.toString = function() {
    return String(this.value);
}
Const.prototype.prefix = Const.prototype.toString;
Const.prototype.postfix = Const.prototype.toString;
const zero = new Const(0);
const one = new Const(1);
const two = new Const(2);

function Variable(name) {
    this.name = name;
}
Variable.prototype.evaluate = function(x, y, z) {
    return this.name === "x" ? x : this.name === "y" ? y : z;
}
Variable.prototype.diff = function(name) {
    return this.name === name ? one : zero;
}
Variable.prototype.toString = function() {
    return this.name;
}
Variable.prototype.prefix = Variable.prototype.toString;
Variable.prototype.postfix = Variable.prototype.toString;

function Expression(...args) {
    this.args = args;
    this.diffSave = new Map();
}
let curry = (x, y, z) => (a) => a.evaluate(x, y, z);
Expression.prototype.evaluate = function(x, y, z) {
    return this.makeAction(...this.args.map(curry(x, y, z)));
}
Expression.prototype.toString = function() {
    return this.args.map((a) => (a.toString() + " ")).reduce((a, b) => a + b) + this.sign;
}
Expression.prototype.prefix = function() {
    return "(" + this.sign + this.args.map((a) => (" " + a.prefix())).reduce((a, b) => a + b) + ")";
}
Expression.prototype.postfix = function() {
    return "(" + this.args.map((a) => (a.postfix() + " ")).reduce((a, b) => a + b) + this.sign + ")"
}

function makeExpression(sign, makeAction, diff) {
    let operation = function(...args) {
        Expression.call(this, ...args);
    }
    setMethods(operation, makeAction, diff);
    operation.prototype.sign = sign;
    return operation;
}

function makeSumrecHMean(sign, makeAction, diff) {
    let operation = function(...args) {
        Expression.call(this, ...args);
        this.sign = sign + args.length;
    }
    setMethods(operation, makeAction, diff);
    return operation;
}
function setMethods(operation, makeAction, diff) {
    operation.prototype = Object.create(Expression.prototype);
    operation.prototype.makeAction = makeAction;
    operation.prototype.diff = diff;
}

function diffExpr(name, diffSave, diff) {
    if (!diffSave.has(name)) {
        diffSave.set(name, diff);
    }
    return diffSave.get(name);
}
function diffAdd(name) {
    return diffExpr(name, this.diffSave, new Add(this.args[0].diff(name), this.args[1].diff(name)));
}
function diffSubtract(name) {
    return diffExpr(name, this.diffSave, new Subtract(this.args[0].diff(name), this.args[1].diff(name)));
}
function diffMultiply(name) {
    return diffExpr(name, this.diffSave, new Add(new Multiply(this.args[0].diff(name), this.args[1]),
        new Multiply(this.args[1].diff(name), this.args[0])));
}
function diffDivide(name) {
    return diffExpr(name, this.diffSave, new Divide(new Subtract(new Multiply(this.args[0].diff(name),
        this.args[1]), new Multiply(this.args[1].diff(name), this.args[0])), new Multiply(this.args[1], this.args[1])));
}
function diffNegate(name) {
    return diffExpr(name, this.diffSave, new Negate(this.args[0].diff(name)));
}
function diffSumrecN(name) {
    return diffExpr(name, this.diffSave, this.args.map((a) => (new Divide(new Negate(a.diff(name)),
        new Multiply(a, a)))).reduce((a, b) => new Add(a, b)));
}
function diffHMeanN(name) {
    return diffExpr(name, this.diffSave, new Divide(new Const(this.args.length),
        new SumrecN(...this.args)).diff(name));
}
function diffMeansq(name) {
    return diffExpr(name, this.diffSave, new Divide(this.args.map((a) => new Multiply(two,
        new Multiply(a, a.diff(name)))).reduce((a, b) => new Add(a, b)), new Const(this.args.length)));
}
function diffRMS(name) {
    return diffExpr(name, this.diffSave, new Multiply(new Divide(new Const(1),
        new Multiply(two, new RMS(...this.args))), new Meansq(...this.args).diff(name)));
} 

let Add = makeExpression("+", (first, second) => first + second, diffAdd);
let Subtract = makeExpression("-", (first, second) => first - second, diffSubtract);
let Multiply = makeExpression("*", (first, second) => first * second, diffMultiply);
let Divide = makeExpression("/", (first, second) => first / second, diffDivide);
let Negate = makeExpression("negate", (first) => - first, diffNegate);
let SumrecN = makeSumrecHMean("sumrec",
    (...args) => args.map((a) => 1 / a).reduce((a, b) => a + b), diffSumrecN);
let Sumrec2 = SumrecN;
let Sumrec3 = SumrecN;
let Sumrec4 = SumrecN;
let Sumrec5 = SumrecN;
let HMeanN = makeSumrecHMean("hmean",
    (...args) => args.length / SumrecN.prototype.makeAction(...args), diffHMeanN);
let HMean2 = HMeanN;
let HMean3 = HMeanN;
let HMean4 = HMeanN;
let HMean5 = HMeanN;
let Meansq = makeExpression("meansq",
    (...args) => args.map((a) => a * a).reduce((a, b) => a + b) / args.length, diffMeansq);
let RMS = makeExpression("rms",
    (...args) => Math.sqrt(Meansq.prototype.makeAction(...args)), diffRMS);

const getExpr = {
    "+": [Add, 2, (a) => a === 2],
    "-": [Subtract, 2, (a) => a === 2],
    "*": [Multiply, 2, (a) => a === 2],
    "/": [Divide, 2, (a) => a === 2],
    "negate": [Negate, 1, (a) => a === 1],
    "rms": [RMS, -1, (a) => a > 0],
    "meansq": [Meansq, -1, (a) => a > 0]
}

const parse = (expr) => {
    let stack = [];
    let exprList = expr.trim().split(/\s+/);
    for (let item of exprList) {
        if (item in getExpr) {
            stack.push(new getExpr[item][0](...stack.splice(-getExpr[item][1])));
        } else if (item.slice(0, -1) === "sumrec") {
            stack.push(new SumrecN(...stack.splice(-Number(item.slice(-1)))));
        } else if (item.slice(0, -1) === "hmean") {
            stack.push(new HMeanN(...stack.splice(-Number(item.slice(-1)))));
        } else if (item === "x" || item === "y" || item === "z") {
            stack.push(new Variable(item));
        } else {
            stack.push(new Const(Number(item)));
        }
    }
    return stack.pop();
}

function UnexpectedInputError(message) {
    this.message = message;
}
UnexpectedInputError.prototype = Object.create(Error.prototype);
UnexpectedInputError.prototype.name = "UnexpectedInputError";
UnexpectedInputError.prototype.constructor = UnexpectedInputError;

const getFromStack = (stack, pos) => {
    let lastParenthesisIdx = stack.lastIndexOf("(");
    if (lastParenthesisIdx === -1) {
        throw new UnexpectedInputError("Opening parenthesis missing, position: " + pos);
    }
    let argsArr = stack.splice(lastParenthesisIdx + 1);
    stack.pop();
    if (argsArr.length > 1 + argsArr.filter((a) => !(a in getExpr)).length) {
        throw new UnexpectedInputError("Found operation in arguments, position: " + pos);
    }
    return argsArr;
}

const pushInStack = (stack, isNumOfArgsValid, operation, argsArr, pos) => {
    if (!isNumOfArgsValid(argsArr.length)) {
        throw new UnexpectedInputError("Unexpected number of arguments, position: " + pos);
    }
    stack.push(new operation(...argsArr));
}

const parseWithParenthesis = (expr, mod) => {
    let stack = [];
    let exprList = expr.split(/(\)|\(|\s+)/).filter((ch) => (!ch.match(/^\s*$/)));
    for (let i = 0; i < exprList.length; i++) {
        let item = exprList[i];
        if (item === "(" || item in getExpr) {
            stack.push(item);
        } else if (item === ")") {
            let argsArr = getFromStack(stack, i);
            let operation = mods[mod](argsArr);
            if (operation in getExpr) {
                pushInStack(stack, getExpr[operation][2], getExpr[operation][0], argsArr, i);
            } else {
                throw new UnexpectedInputError("Operation " + operation + " is unexpected, position: " + i);
            }
        } else if (item === "x" || item === "y" || item === "z") {
            stack.push(new Variable(item));
        } else if (!isNaN(Number(item))) {
            stack.push(new Const(Number(item)));
        } else {
            throw new UnexpectedInputError("Symbol " + item + " is unexpected, position: " + i)
        }
    }
    let pos = stack.length;
    if (stack.length === 0) {
        throw new UnexpectedInputError("Empty input, position: " + pos);
    }
    if (stack[0] === "(") {
        throw new UnexpectedInputError("Closing parenthesis missing, position: " + pos);
    }
    if (stack.length !== 1) {
        throw new UnexpectedInputError("Excessive info, position: " + pos)
    }
    return stack.pop();
}

const mods = {
    "prefix": (a) => a.shift(),
    "postfix": (a) => a.pop()
}
const parsePrefix = (expr) => parseWithParenthesis(expr, "prefix");
const parsePostfix = (expr) => parseWithParenthesis(expr, "postfix");