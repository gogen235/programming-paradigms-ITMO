init(MAX_N) :- assert(prime(2)), check(3, MAX_N).

check(Num, MAX_N) :- MAX_N < Num, !.
check(Num, MAX_N) :- is_prime(Num, 3), Num1 is Num + 2, check(Num1, MAX_N).

is_prime(Num, Curr_num) :- Curr_num * Curr_num > Num, !, assert(prime(Num)).
is_prime(Num, Curr_num) :- 0 is mod(Num, Curr_num), !.
is_prime(Num, Curr_num) :- Curr_num1 is Curr_num + 2, is_prime(Num, Curr_num1).

composite(N) :- \+ prime(N).

prime_divisors(1, []) :- !.
prime_divisors(Num, [Num]) :- number(Num), prime(Num), !.
prime_divisors(Num, [H | T]) :- number(Num), !, build_factorization(Num, 2, [H | T]).
prime_divisors(Num, List) :- find_num(Num, 1, List).

find_num(Num, Curr, [H]) :- !, prime(H), Num is Curr * H.
find_num(Num, Curr, [H1, H2 | T]) :- H2 >= H1,
                                     prime(H1),
                                     prime(H2),
                                     Curr1 is Curr * H1,
                                     find_num(Num, Curr1, [H2 | T]).

build_factorization(Num, Curr, [Num]) :- prime(Num), Curr * Curr > Num, !.
build_factorization(Num, Curr, [Curr | T]) :- 0 is mod(Num, Curr),
                                              Num1 is div(Num, Curr),
                                              build_factorization(Num1, Curr, T), !.
build_factorization(Num, Curr, List) :- Curr1 is Curr + 1,
                                        build_factorization(Num, Curr1, List).

cpd(Num, List) :- prime_divisors(Num, List1), cpd_list(List1, List).

cpd_list([], []) :- !.
cpd_list([H1], [(H1, 1)]) :- !.
cpd_list([H1 | T1], [(H1, S)]) :- cpd_list(T1, [(H1, S1)]), S is S1 + 1.
cpd_list([H1 | T1], [(H1, S1), (H2, S2) | T2]) :- (cpd_list(T1, [(H2, S2) | T2]), S1 is 1; 
												   cpd_list(T1, [(H1, S3), (H2, S2) | T2]), S1 is S3 + 1), H1 < H2.

make_div(_, []).
make_div([(F, S) | T1], [H | T2]) :- make_div(T1, [H | T2]); S1 is S - 1, S > 0, make_div([(F, S1) | T1], T2), H is F. 

divisors_divisors(Num, Divisors) :- cpd(Num, List), findall(R, make_div(List, R), Divisors).