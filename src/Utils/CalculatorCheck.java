package Utils;

public class CalculatorCheck {

    public static int calculateCheckDigit(String number) {
        // Check if the input is a valid 12-digit number
        number=String.format("%012d", Long.parseLong(number));
        int[] weights = {1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3};// Define the weights for each position

        // Initialize sum to 0
        int sum = 0;

        // Loop through each digit and multiply by its corresponding weight
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            sum += digit * weights[i];
        }

        // Calculate the remainder when sum is divided by 10
        int remainder = sum % 10;

        // If remainder is not 0, subtract it from 10 to get the check digit
        int checkDigit = remainder != 0 ? 10 - remainder : 0;

        return checkDigit;
    }

    public static String fullDigit(String number) {
        int checkDigit = calculateCheckDigit(number);
        return number + checkDigit;
    }

    public static void main(String[] args) {

        String number = "000000000002";
        int checkDigit = calculateCheckDigit(number);
        System.out.println(fullDigit(number));
    }
}
