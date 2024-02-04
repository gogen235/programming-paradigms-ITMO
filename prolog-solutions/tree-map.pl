h(null, 0).
h(node(_, _, H, _, _), H).

count_h(L, R, H) :- h(L, HL), h(R, HR), (HL < HR, H is HR + 1; H is HL + 1).
diff(L, R, D) :- h(L, HL), h(R, HR), D is HL - HR.

rotate_right(node(K1, V1, H1, node(K2, V2, H2, NL2, NR2), NR1), node(K2, V2, H4, NL2, node(K1, V1, H3, NR2, NR1))) :-
        count_h(NR2, NR1, H3), count_h(NL2, node(K1, V1, H3, NR2, NR1), H4), !.
rotate_left(node(K1, V1, H1, NL1, node(K2, V2, H2, NL2, NR2)), node(K2, V2, H4, node(K1, V1, H3, NL1, NL2), NR2)) :-
        count_h(NL1, NL2, H3), count_h(node(K1, V1, H3, NL1, NL2), NR2, H4), !.

big_rotate_left(node(K, V, H, NL, NR), R) :- rotate_right(NR, NR1), rotate_left(node(K, V, H, NL, NR1), R), !.
big_rotate_right(node(K, V, H, NL, NR), R) :- rotate_left(NL, NL1), rotate_right(node(K, V, H, NL1, NR), R), !.

map_build([], null).
map_build([(K, V) | T], TreeMap) :- map_build(T, TreeMap1), map_put(TreeMap1, K, V, TreeMap).

map_put(null, K, V, node(K, V, 1, null, null)) :- !.
map_put(node(K, V1, H, NL, NR), K, V, node(K, V, H, NL, NR)) :- !.
map_put(node(K1, V1, H, NL, NR), K, V, R) :-
        K < K1, map_put(NL, K, V, R1), balance(node(K1, V1, H, R1, NR), R), !.
map_put(node(K1, V1, H, NL, NR), K, V, R) :-
        map_put(NR, K, V, R1), balance(node(K1, V1, H, NL, R1), R), !.

balance(node(K, V, _, NL, NR), R) :-
        count_h(NL, NR, H1), diff(NL, NR, D), D is -2, balance_left(node(K, V, H1, NL, NR), R), !.
balance(node(K, V, _, NL, NR), R) :-
        count_h(NL, NR, H1), diff(NL, NR, D), D is 2, balance_right(node(K, V, H1, NL, NR), R), !.
balance(node(K, V, _, NL, NR), node(K, V, H1, NL, NR)) :- count_h(NL, NR, H1), !.
        
balance_left(node(K, V, H, NL, node(K1, V1, H1, NL1, NR1)), R) :-
        diff(NL1, NR1, H5), H5 =< 0, rotate_left(node(K, V, H, NL, node(K1, V1, H1, NL1, NR1)), R), !.
balance_left(node(K, V, H, NL, node(K1, V1, H1, NL1, NR1)), R) :-
        big_rotate_left(node(K, V, H, NL, node(K1, V1, H1, NL1, NR1)), R), !.

balance_right(node(K, V, H,  node(K1, V1, H1, NL1, NR1), NR), R) :-
        diff(NL1, NR1, H5), H5 >= 0, rotate_right(node(K, V, H, node(K1, V1, H1, NL1, NR1), NR), R), !.
balance_right(node(K, V, H, node(K1, V1, H1, NL1, NR1), NR), R) :-
        big_rotate_right(node(K, V, H, node(K1, V1, H1, NL1, NR1), NR), R), !.

map_get(node(K, V, _, _, _), K, V) :- !.
map_get(node(K1, V1, _, NL, _),  K, V) :- K < K1, map_get(NL, K, V), !.
map_get(node(K1, V1, _, _, NR), K, V) :- K1 < K, map_get(NR, K, V), !.

map_remove(null, _, null) :- !.
map_remove(node(K, V, _, null, null), K, null) :- !.
map_remove(node(K, V, _, null, NR), K, NR) :- !.
map_remove(node(K, V, _, NL, null), K, NL) :- !.
map_remove(node(K, V, _, NL, NR), K, R) :-
        map_remove_min(NR, R1, K1, V1), balance(node(K1, V1, H, NL, R1), R), !.
map_remove(node(K1, V1, H, NL, NR), K, R) :-
		K < K1, map_remove(NL, K, R1), balance(node(K1, V1, H, R1, NR), R), !.
map_remove(node(K1, V1, H, NL, NR), K, R) :-
		map_remove(NR, K, R1), balance(node(K1, V1, H, NL, R1), R), !.

map_remove_min(node(K, V, _, null, NR), NR, K, V) :- !.
map_remove_min(node(K1, V1, H, NL, NR), R, K, V) :-
        map_remove_min(NL, R1, K, V), balance(node(K1, V1, H, R1, NR), R), !.

map_getCeiling(node(K, V, _, _, _), K, V) :- !.
map_getCeiling(node(K1, V, _, null, _), K, V) :- K < K1, !.
map_getCeiling(node(K1, V1, _, NL, _), K, V) :- K < K1, map_getCeiling(NL, K, V), !.
map_getCeiling(node(K1, V, _, NL, _), K, V) :- K < K1, !.
map_getCeiling(node(K1, V1, _, _, NR), K, V) :- K1 < K, map_getCeiling(NR, K, V), !.

map_putCeil(node(K, V1, H, NL, NR), K, V, node(K, V, H, NL, NR)) :- !.
map_putCeil(node(K1, V1, H, null, NR), K, V, node(K1, V, H, null, NR)) :- K < K1, !.
map_putCeil(node(K1, V1, H, NL, NR), K, V, node(K1, V1, H, R, NR)) :- K < K1, map_putCeil(NL, K, V, R), !.
map_putCeil(node(K1, V1, H, NL, NR), K, V, node(K1, V, H, NL, NR)) :- K < K1, !.
map_putCeil(node(K1, V1, H, NL, NR), K, V, node(K1, V1, H, NL, R)) :- map_putCeil(NR, K, V, R), !.

map_putCeiling(N, K, V, R) :- map_putCeil(N, K, V, R), !.
map_putCeiling(N, K, V, N).
