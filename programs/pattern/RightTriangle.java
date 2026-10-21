/**
 * Right triangle star pattern, translated from pattern/right_triangle.py.
 *
 * Prints N rows where row i (0-indexed) contains i+1 stars:
 *   *
 *   **
 *   ***
 *   ****
 *   *****
 */
public class RightTriangle {

    static void rightTriangle(int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i + 1; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        rightTriangle(5);
    }
}
