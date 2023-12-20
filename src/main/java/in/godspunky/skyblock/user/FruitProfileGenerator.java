package in.godspunky.skyblock.user;
import java.util.Random;

public class FruitProfileGenerator {
    private static final String[] FRUITS = {
            "Apple", "Banana", "Blueberry", "Coconut", "Cucumber", "Grapes", "Kiwi",
            "Lemon", "Lime", "Mango", "Orange", "Papaya", "Peach", "Pear",
            "Pineapple", "Pomegranate", "Raspberry", "Strawberry", "Tomato",
            "Watermelon", "Zucchini"
    };

    public static String generateRandomFruitName() {
        Random random = new Random();
        int index = random.nextInt(FRUITS.length);
        return FRUITS[index];
    }
}
