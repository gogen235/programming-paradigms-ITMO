package expression.generic;

public class IntegerCalculate implements Calculate<Integer> {
    @Override
    public Integer add(Integer first, Integer second) {
        return first + second;
    }

    @Override
    public Integer sub(Integer first, Integer second) {
        return first - second;
    }

    @Override
    public Integer mul(Integer first, Integer second) {
        return first * second;
    }

    @Override
    public Integer div(Integer first, Integer second) {
        return first / second;
    }

    @Override
    public Integer minus(Integer first) {
        return -first;
    }

    @Override
    public Integer abs(Integer first) {
        return Math.abs(first);
    }

    @Override
    public Integer square(Integer first) {
        return first * first;
    }

    @Override
    public Integer mod(Integer first, Integer second) {
        return first % second;
    }

    @Override
    public Integer parse(String string) {
        return Integer.parseInt(string);
    }

    @Override
    public Integer parseInt(int value) {
        return value;
    }
}
