package expression.generic;

public class Main {
    public static void main(String[] args) throws Exception {
        Tabulator genericTabulator = new GenericTabulator();
        Object[][][] result = genericTabulator.tabulate(args[0].substring(1), args[1],
                -2, 2, -2, 2, -2, 2);
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 4; j++) {
                for (int k = 0; k <= 4; k++) {
                    System.out.println("x = " + (i - 2) + ", y = " + (j - 2) + ", z = " + (k - 2));
                    System.out.println("result = " + result[i][j][k]);
                }
                System.out.println();
            }
            System.out.println("---------------------");
        }

    }
}
