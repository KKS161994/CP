/**
 * Solid pattern, translated from pattern/solid_square.py.
 *
 * The active Python function (printPatternv1) printed a right triangle of
 * stars via string repetition: row i prints (i+1) stars.
 *   *
 *   **
 *   ***
 *   ****
 *   *****
 *
 * (The commented-out printPattern in the source printed a full NxN solid
 * square; it is preserved as a comment below, matching the original file.)
 */
public class SolidSquare {

    // def printPattern(N):
    //     for i in range(N):
    //         for j in range(N):
    //             print("*", end="")
    //         print("")

    static void printPatternV1(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("*".repeat(i + 1));
        }
    }

    public static void main(String[] args) {
        printPatternV1(5);
    }
}
