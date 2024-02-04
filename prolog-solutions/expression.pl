variable(Name, variable(Name)).
const(Value, const(Value)).

op_add(A, B, operation(op_add, A, B)).
op_subtract(A, B, operation(op_subtract, A, B)).
op_multiply(A, B, operation(op_multiply, A, B)).
op_divide(A, B, operation(op_divide, A, B)).
op_negate(A, operation(op_negate, A)).

op_not(A, operation(op_not, A)).
op_and(A, B, operation(op_and, A, B)).
op_or(A, B, operation(op_or, A, B)).
op_xor(A, B, operation(op_xor, A, B)).
op_impl(A, B, operation(op_impl, A, B)).
op_iff(A, B, operation(op_iff, A, B)).

bin(op_add, A, B, R) :- R is A + B.
bin(op_subtract, A, B, R) :- R is A - B.
bin(op_multiply, A, B, R) :- R is A * B.
bin(op_divide, A, B, R) :- R is A / B.
un(op_negate, A, R) :- R is -A.

un(op_not, A, 1) :- A =< 0, !.
un(op_not, A, 0).
bin(op_and, A, B, 1) :- A > 0, B > 0, !.
bin(op_and, A, B, 0).
bin(op_or, A, B, 0) :- A =< 0, B =< 0, !.
bin(op_or, A, B, 1).
bin(op_xor, A, B, 1) :- A > 0, B =< 0, !; A =< 0, B > 0, !.
bin(op_xor, A, B, 0).
bin(op_impl, A, B, 1) :- A =< 0, !; B > 0, !.
bin(op_impl, A, B, 0).
bin(op_iff, A, B, 1) :- A > 0, B > 0, !; A =< 0, B =< 0, !.
bin(op_iff, A, B, 0).

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], R) :- lookup(K, T, R).

evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- atom_chars(Name, [H | _]), lookup(H, Vars, R).
evaluate(operation(Op, A), Vars, R) :-
    evaluate(A, Vars, AV),
    un(Op, AV, R).
evaluate(operation(Op, A, B), Vars, R) :-
    evaluate(A, Vars, AV),
    evaluate(B, Vars, BV),
    bin(Op, AV, BV, R).

:- load_library('alice.tuprolog.lib.DCGLibrary').

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

expr_p(variable(Name)) -->
    { nonvar(Name, atom_chars(Name, Chars)) },
    var_p(Chars),
    { Chars = [_ | _], atom_chars(Name, Chars) }.

var_p([]) --> [].
var_p([H | T]) -->
    { member(H, ['x', 'y', 'z', 'X', 'Y', 'Z']) },
    [H], var_p(T).

expr_p(const(Value)) -->
  { nonvar(Value, number_chars(Value, Chars)) },
  number_p(Chars),
  { (Chars = ['-', _ | _], !; Chars = [H | _], \+ H = '-'), number_chars(Value, Chars) }.

number_p(L) --> digits_p(L).
number_p(['-' | T]) --> ['-'], digits_p(T).

digits_p([]) --> [].
digits_p([H | T]) -->
  { member(H, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.']) },
  [H], digits_p(T).

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_and) --> ['&', '&'].
op_p(op_or) --> ['|', '|'].
op_p(op_xor) --> ['^', '^'].
op_p(op_impl) --> ['-', '>'].
op_p(op_iff) --> ['<', '-', '>'].
un_op_p(op_not) --> ['!'].
un_op_p(op_negate) --> ['n', 'e', 'g', 'a', 't', 'e'].

expr_p(operation(Op, A)) --> { nonvar(Op) -> WS = [' ']; WS = [] },
    un_op_p(Op), WS, expr_p(A).
expr_p(operation(Op, A, B)) --> { nonvar(Op) -> WS = [' ']; WS = [] },
    ['('], expr_p(A), WS, op_p(Op), WS, expr_p(B), [')'].

infix_str(E, A) :- ground(E), phrase(expr_p(E), C), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), remove_ws(C, C1), phrase(expr_p(E), C1).

remove_ws([], []).
remove_ws([' ' | T], T1) :- remove_ws(T, T1), !.
remove_ws([H | T], [H | T1]) :- remove_ws(T, T1).