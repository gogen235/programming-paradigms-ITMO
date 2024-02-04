package expression.generic;

public class DoubleCalculate implements Calculate<Double> {

    @Override
    public Double add(Double first, Double second) {
        return first + second;
    }

    @Override
    public Double sub(Double first, Double second) {
        return first - second;
    }

    @Override
    public Double mul(Double first, Double second) {
        return first * second;
    }

    @Override
    public Double div(Double first, Double second) {
        return first / second;
    }

    @Override
    public Double minus(Double first) {
        return -first;
    }
    public Double abs(Double first) {
        return Math.abs(first);
    }
    public Double mod(Double first, Double second) {
        return first % second;
    }
    public Double square(Double first) {
        return mul(first, first);
    }

    @Override
    public Double parse(String string) {
        return Double.parseDouble(string);
    }

    @Override
    public Double parseInt(int value) {
        return (double) value;
    }
}
