package Utils;

public class MathUtils {

    private MathUtils(){}

    public int min(int value, int min){
        return value < min ? min : value;
    }

    public int max(int value, int max){
        return value < max ? max : value;
    }

    // If value < min return min, same for max
    public static int clamp(int value, int min, int max){
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
