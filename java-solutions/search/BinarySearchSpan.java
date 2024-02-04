package search;

public class BinarySearchSpan {
    // Pred: args[i] <= args[i + 1], for 1 <= i < args.length - 1 &&
    // we can parse args[i] as integer  for 0 <= i < args.length

    // Post: (ret1 = min idx: x <= a[idx] || ret1 = a.length && (max(a) < x || a is empty)) &&
    // ret2 = amount of numbers = x
    public static void main(String[] args) {
        // args[i] <= args[i + 1], 1 <= i < args.length - 1
        int x = Integer.parseInt(args[0]);

        int[] a = new int[args.length - 1];
        // args[i] <= args[i + 1], 1 <= i < args.length - 1 && a.length = args.length - 1
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        // a[i - 1] = args[i] && args[i] <= args[i + 1], for 1 <= i < args.length - 1 && a.length = args.length - 1
        // a[i] <= a[i + 1], for 0 <= i < a.length - 1
        int leftIdx = -1;
        int rightIdx = a.length;
        // (leftIdx = -1 && rightIdx = a.length ||
        // a[leftIdx] < x && rightIdx = a.length ||
        // leftIdx = -1 && x <= a[rightIdx] ||
        // a[leftIdx] < x <= a[rightIdx]) &&
        // leftIdx < rightIdx
        // a[i] <= a[i + 1], for 0 <= i < a.length - 1
        int iterLeft = binSearchIterLeft(leftIdx, rightIdx, x, a);
        int recLeft = binSearchRecLeft(leftIdx, rightIdx, x, a);
        // left = min idx: x >= a[idx] && max(0, leftIdx) <= idx < min(a.length, rightIdx) ||
        // left = rightIdx && (max(a[i]) < x for max(0, leftIdx) <= idx < min(a.length, rightIdx) || a is empty)

        // (leftIdx = -1 && rightIdx = a.length ||
        // a[leftIdx] <= x && rightIdx = a.length ||
        // leftIdx = -1 && x < a[rightIdx] ||
        // a[leftIdx] <= x < a[rightIdx]) &&
        // leftIdx < rightIdx
        // a[i] <= a[i + 1], for 0 <= i < a.length - 1
        int iterRight = binSearchIterRight(leftIdx, rightIdx, x, a);
        int recRight = binSearchRecRight(leftIdx, rightIdx, x, a);
        // right = min idx: x > a[idx] && max(0, leftIdx) <= idx < min(a.length, rightIdx) ||
        // right = rightIdx && (max(a[i]) < x for max(0, leftIdx) <= idx < min(a.length, rightIdx) || a is empty)

        // right - left = amount of numbers = x
        if (iterRight == recRight && iterLeft == recLeft) {
            System.out.print(iterLeft + " " + (iterRight - iterLeft)); //ret1 and ret2
        } else {
            System.out.println("you lose");
        }

    }

    // Pred: (leftIdx = -1 && rightIdx = a.length ||
    // a[leftIdx] < x && rightIdx = a.length ||
    // leftIdx = -1 && x <= a[rightIdx] ||
    // a[leftIdx] < x <= a[rightIdx]) &&
    // leftIdx < rightIdx
    // a[i] <= a[i + 1], for 0 <= i < a.length - 1

    // Post: ret = min idx: x >= a[idx] && max(0, leftIdx) <= idx < min(a.length, rightIdx) ||
    // ret = rightIdx && (max(a[i]) < x for max(0, leftIdx) <= idx < min(a.length, rightIdx) || a is empty)
    private static int binSearchRecLeft(int leftIdx, int rightIdx, int x, int[] a) {
        if (rightIdx - leftIdx <= 1) {
            // rightIdx - leftIdx <= 1 && leftIdx < rightIdx -> rightIdx - leftIdx = 1
            // leftIdx = -1 && rightIdx = 0 -> a.length = 0 -> ret = 0 = rightIdx
            // a[a.length - 1] < x && rightIdx = a.length -> min(a) < x -> ret = a.length = rightIdx
            // leftIdx = -1 && x <= a[0] -> min idx - 0 -> ret = 0 = rightIdx
            // a[leftIdx] < x <= a[rightIdx]) && rightIdx - leftIdx = 1 -> ret = rightIdx
            return rightIdx;
        }
        // rightIdx - leftIdx > 1
        int currIdx = (rightIdx + leftIdx) / 2;
        // rightIdx - leftIdx > 1 && currIdx = (rightIdx + leftIdx) / 2

        // rightIdx > 1 + leftIdx &&
        // (2 * currIdx = rightIdx + leftIdx && (rightIdx + leftIdx) % 2 == 0 ||
        // (2 * currIdx = rightIdx + leftIdx - 1 && (rightIdx + leftIdx) % 2 == 1)

        // 2 * currIdx > 1 + 2 * leftIdx && 2 * currIdx < 2 * rightIdx - 1 && (rightIdx + leftIdx) % 2 == 0 ||
        // 2 * currIdx > 2 * leftIdx && 2 * currIdx < 2 * rightIdx - 2 && (rightIdx + leftIdx) % 2 == 0

        // leftIdx < currIdx < rightIdx
        if (a[currIdx] < x) {
            // (leftIdx = -1 && rightIdx = a.length ||
            // a[leftIdx] < x && rightIdx = a.length ||
            // leftIdx = -1 && x <= a[rightIdx] ||
            // a[leftIdx] < x <= a[rightIdx]) &&
            // leftIdx < currIdx < rightIdx

            // (a[currIdx] < x && rightIdx = a.length ||
            // a[currIdx] < x <= a[rightIdx]) &&
            // currIdx < rightIdx
            return binSearchRecLeft(currIdx, rightIdx, x, a);
            // a := a[max(0, currIdx):min(a.length, rightIdx)]
            // (ret = min idx in a: x <= a[idx] ||
            // ret = rightIdx && (max(a) > x || a is empty))
            // a[currIdx] > x

            // ret = min idx: x >= a[idx] && max(0, leftIdx) <= idx < min(a.length, rightIdx) ||
            // ret = rightIdx && (max(a[idx]) < x for max(0, leftIdx) <= idx < min(a.length, rightIdx) || a is empty)
        } else {
            // (leftIdx = -1 && rightIdx = a.length ||
            // a[leftIdx] < x && rightIdx = a.length ||
            // leftIdx = -1 && x <= a[rightIdx] ||
            // a[leftIdx] < x <= a[rightIdx]) &&
            // leftIdx < currIdx < rightIdx

            // x <= a[currIdx]

            // (leftIdx = -1 && x <= a[currIdx] ||
            // a[leftIdx] < x <= a[currIdx]]) &&
            // leftIdx < currIdx
            return binSearchRecLeft(leftIdx, currIdx, x, a);
            // a := a[max(0, leftIdx):min(a.length, currIdx)]
            // ret = min idx in a: x <= a[rightIdx] ||
            // ret = currIdx && (max(a) < x || a is empty)
            // x <= a[currIdx]

            // ret = min idx: x >= a[idx] && max(0, leftIdx) <= idx < min(a.length, rightIdx) ||
            // ret = rightIdx && (max(a[i]) < x for max(0, leftIdx) <= idx < min(a.length, rightIdx) || a is empty)
        }
    }

    // Pred: (leftIdx = -1 && rightIdx = a.length ||
    // a[leftIdx] <= x && rightIdx = a.length ||
    // leftIdx = -1 && x < a[rightIdx] ||
    // a[leftIdx] <= x < a[rightIdx]) &&
    // leftIdx < rightIdx
    // a[i] <= a[i + 1], for 0 <= i < a.length - 1

    // Post: ret = min idx: x > a[idx] && max(0, leftIdx) <= idx < min(a.length, rightIdx) ||
    // ret = rightIdx && (max(a[i]) < x for max(0, leftIdx) <= idx < min(a.length, rightIdx) || a is empty)
    private static int binSearchRecRight(int leftIdx, int rightIdx, int x, int[] a) {
        if (rightIdx - leftIdx <= 1) {
            // rightIdx - leftIdx <= 1 && leftIdx < rightIdx -> rightIdx - leftIdx = 1
            // leftIdx = -1 && rightIdx = 0 -> a.length = 0 -> ret = 0 = rightIdx
            // a[a.length - 1] < x && rightIdx = a.length -> min(a) < x -> ret = a.length = rightIdx
            // leftIdx = -1 && x < a[0] -> min idx - 0 -> ret = 0 = rightIdx
            // a[leftIdx] <= x < a[rightIdx]) && rightIdx - leftIdx = 1 -> ret = rightIdx
            return rightIdx;
        }
        // rightIdx - leftIdx > 1
        int currIdx = (rightIdx + leftIdx) / 2;
        // rightIdx - leftIdx > 1 && currIdx = (rightIdx + leftIdx) / 2

        // rightIdx > 1 + leftIdx &&
        // (2 * currIdx = rightIdx + leftIdx && (rightIdx + leftIdx) % 2 == 0 ||
        // (2 * currIdx = rightIdx + leftIdx - 1 && (rightIdx + leftIdx) % 2 == 1)

        // 2 * currIdx > 1 + 2 * leftIdx && 2 * currIdx < 2 * rightIdx - 1 && (rightIdx + leftIdx) % 2 == 0 ||
        // 2 * currIdx > 2 * leftIdx && 2 * currIdx < 2 * rightIdx - 2 && (rightIdx + leftIdx) % 2 == 0

        // leftIdx < currIdx < rightIdx
        if (a[currIdx] <= x) {
            // (leftIdx = -1 && rightIdx = a.length ||
            // a[leftIdx] <= x && rightIdx = a.length ||
            // leftIdx = -1 && x < a[rightIdx] ||
            // a[leftIdx] <= x < a[rightIdx]) &&
            // leftIdx < currIdx < rightIdx

            // (a[currIdx] <= x && rightIdx = a.length ||
            // a[currIdx] <= x < a[rightIdx]) &&
            // currIdx < rightIdx

            return binSearchRecRight(currIdx, rightIdx, x, a);

            // a := a[max(0, currIdx):min(a.length, rightIdx)]
            // (ret = min idx in a: x <= a[idx] ||
            // ret = rightIdx && (max(a) > x || a is empty))
            // a[currIdx] > x

            // ret = min idx: x > a[idx] && max(0, leftIdx) <= idx < min(a.length, rightIdx) ||
            // ret = rightIdx && (max(a[idx]) < x for max(0, leftIdx) <= idx < min(a.length, rightIdx) || a is empty)
        } else {
            // (leftIdx = -1 && rightIdx = a.length ||
            // a[leftIdx] <= x && rightIdx = a.length ||
            // leftIdx = -1 && x < a[rightIdx] ||
            // a[leftIdx] <= x < a[rightIdx]) &&
            // leftIdx < currIdx < rightIdx

            // x < a[currIdx]

            // (leftIdx = -1 && x < a[currIdx] ||
            // a[leftIdx] <= x < a[currIdx]]) &&
            // leftIdx < currIdx

            return binSearchRecRight(leftIdx, currIdx, x, a);
        }
    }

    // Pred: (leftIdx = -1 && rightIdx = a.length ||
    // a[leftIdx] < x && rightIdx = a.length ||
    // leftIdx = -1 && x <= a[rightIdx] ||
    // a[leftIdx] < x <= a[rightIdx]) &&
    // leftIdx < rightIdx
    // a[i] <= a[i + 1], for 0 <= i < a.length - 1

    // Post: ret = min idx: x >= a[idx] && max(0, leftIdx) <= idx < min(a.length, rightIdx) ||
    // ret = rightIdx && (max(a[i]) < x for max(0, leftIdx) <= idx < min(a.length, rightIdx) || a is empty)
    private static int binSearchIterLeft(int leftIdx, int rightIdx, int x, int[] a) {
        while (rightIdx - leftIdx > 1) {
            // rightIdx - leftIdx > 1
            int currIdx = (rightIdx + leftIdx) / 2;
            // rightIdx - leftIdx > 1 && currIdx = (rightIdx + leftIdx) / 2

            // rightIdx > 1 + leftIdx &&
            // (2 * currIdx = rightIdx + leftIdx && (rightIdx + leftIdx) % 2 == 0 ||
            // (2 * currIdx = rightIdx + leftIdx - 1 && (rightIdx + leftIdx) % 2 == 1)

            // 2 * currIdx > 1 + 2 * leftIdx && 2 * currIdx < 2 * rightIdx - 1 && (rightIdx + leftIdx) % 2 == 0 ||
            // 2 * currIdx > 2 * leftIdx && 2 * currIdx < 2 * rightIdx - 2 && (rightIdx + leftIdx) % 2 == 0

            // leftIdx < currIdx < rightIdx
            if (a[currIdx] < x) {
                // (leftIdx = -1 && rightIdx = a.length ||
                // a[leftIdx] < x && rightIdx = a.length ||
                // leftIdx = -1 && x <= a[rightIdx] ||
                // a[leftIdx] < x <= a[rightIdx]) &&
                // leftIdx < currIdx < rightIdx

                leftIdx = currIdx;
                // a[currIdx] < x
                // a[leftIdx] < x

                // (a[leftIdx] < x && rightIdx = a.length ||
                // a[leftIdx] < x <= a[rightIdx]) &&
                // leftIdx < rightIdx
            } else {
                // (leftIdx = -1 && rightIdx = a.length ||
                // a[leftIdx] < x && rightIdx = a.length ||
                // leftIdx = -1 && x <= a[rightIdx] ||
                // a[leftIdx] < x <= a[rightIdx]) &&
                // leftIdx < currIdx < rightIdx

                rightIdx = currIdx;
                // x <= a[currIdx]
                // x <= a[rightIdx]

                // (leftIdx = -1 && x <= a[rightIdx] ||
                // a[leftIdx] < x <= a[rightIdx]) &&
                // leftIdx < rightIdx
            }
            // (leftIdx = -1 && rightIdx = a.length ||
            // a[leftIdx] < x && rightIdx = a.length ||
            // leftIdx = -1 && x <= a[rightIdx] ||
            // a[leftIdx] < x <= a[rightIdx]) &&
            // leftIdx < rightIdx
        }
        // (leftIdx = -1 && rightIdx = a.length ||
        // a[leftIdx] < x && rightIdx = a.length ||
        // leftIdx = -1 && x <= a[rightIdx] ||
        // a[leftIdx] < x <= a[rightIdx]) &&
        // (rightIdx - leftIdx <= 1 && leftIdx < rightIdx) ~ (rightIdx - leftIdx = 1)

        return rightIdx;
        // leftIdx = -1 && rightIdx = 0 -> a.length = 0 -> ret = 0 = rightIdx
        // a[a.length - 1] < x && rightIdx = a.length -> min(a) > x -> ret = a.length = rightIdx
        // leftIdx = -1 && x <= a[0] -> min idx - 0 -> ret = 0 = rightIdx
        // a[leftIdx] < x <= a[rightIdx]) && rightIdx - leftIdx = 1 -> ret = rightIdx
    }

    // Pred: (leftIdx = -1 && rightIdx = a.length ||
    // a[leftIdx] <= x && rightIdx = a.length ||
    // leftIdx = -1 && x < a[rightIdx] ||
    // a[leftIdx] <= x < a[rightIdx]) &&
    // leftIdx < rightIdx
    // a[i] <= a[i + 1], for 0 <= i < a.length - 1

    // Post: ret = min idx: x > a[idx] && max(0, leftIdx) <= idx < min(a.length, rightIdx) ||
    // ret = rightIdx && (max(a[i]) < x for max(0, leftIdx) <= idx < min(a.length, rightIdx) || a is empty)
    private static int binSearchIterRight(int leftIdx, int rightIdx, int x, int[] a) {
        while (rightIdx - leftIdx > 1) {
            // rightIdx - leftIdx > 1
            int currIdx = (rightIdx + leftIdx) / 2;
            // rightIdx - leftIdx > 1 && currIdx = (rightIdx + leftIdx) / 2

            // rightIdx > 1 + leftIdx &&
            // (2 * currIdx = rightIdx + leftIdx && (rightIdx + leftIdx) % 2 == 0 ||
            // (2 * currIdx = rightIdx + leftIdx - 1 && (rightIdx + leftIdx) % 2 == 1)

            // 2 * currIdx > 1 + 2 * leftIdx && 2 * currIdx < 2 * rightIdx - 1 && (rightIdx + leftIdx) % 2 == 0 ||
            // 2 * currIdx > 2 * leftIdx && 2 * currIdx < 2 * rightIdx - 2 && (rightIdx + leftIdx) % 2 == 0

            // leftIdx < currIdx < rightIdx
            if (a[currIdx] <= x) {
                // (leftIdx = -1 && rightIdx = a.length ||
                // a[leftIdx] <= x && rightIdx = a.length ||
                // leftIdx = -1 && x < a[rightIdx] ||
                // a[leftIdx] <= x < a[rightIdx]) &&
                // leftIdx < currInt < rightIdx

                leftIdx = currIdx;
                // a[currIdx] <= x
                // a[leftIdx] <= x

                // (a[leftIdx] <= x && rightIdx = a.length ||
                // a[leftIdx] <= x < a[rightIdx]) &&
                // leftIdx < rightIdx
            } else {
                // (leftIdx = -1 && rightIdx = a.length ||
                // a[leftIdx] <= x && rightIdx = a.length ||
                // leftIdx = -1 && x < a[rightIdx] ||
                // a[leftIdx] <= x < a[rightIdx]) &&
                // leftIdx < currInt < rightIdx

                rightIdx = currIdx;
                // x < a[currIdx]
                // x < a[rightIdx]

                // (leftIdx = -1 && x < a[rightIdx] ||
                // a[leftIdx] <= x < a[rightIdx]) &&
                // leftIdx < rightIdx
            }
            // (leftIdx = -1 && rightIdx = a.length ||
            // a[leftIdx] <= x && rightIdx = a.length ||
            // leftIdx = -1 && x < a[rightIdx] ||
            // a[leftIdx] <= x < a[rightIdx]) &&
            // leftIdx < rightIdx
        }
        // (leftIdx = -1 && rightIdx = a.length ||
        // a[leftIdx] <= x && rightIdx = a.length ||
        // leftIdx = -1 && x < a[rightIdx] ||
        // a[leftIdx] <= x < a[rightIdx]) &&
        // (rightIdx - leftIdx <= 1 && leftIdx < rightIdx) ~ (rightIdx - leftIdx = 1)

        return rightIdx;
        // leftIdx = -1 && rightIdx = 0 -> a.length = 0 -> ret = 0 = rightIdx
        // a[a.length - 1] <= x && rightIdx = a.length -> min(a) > x -> ret = a.length = rightIdx
        // leftIdx = -1 && x < a[0] -> min idx - 0 -> ret = 0 = rightIdx
        // a[leftIdx] <= x < a[rightIdx]) && rightIdx - leftIdx = 1 -> ret = rightIdx
    }
}
