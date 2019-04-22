package exception;
import java.util.Scanner;
public class ValueUndefinedException extends RuntimeException {
    /**
     *
     * @param args is unused parameter
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the ValueUndefinedException class? (y/n)");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while (!input.equals("y") || !input.equals("n")){
            System.out.println("Please type 'y' or 'n' and hit enter.");
            input = sc.nextLine();
        }
        if (input.equals("y")){
            System.out.println("Testing code now:");
            System.out.println("Purposely throwing ValueUndefinedException");
            throw new ValueUndefinedException("Test message");
        }

    }

    /**
     *
     * @param message is the message sent by the exception. can depend on context that exception is called within
     */
    public ValueUndefinedException(String message) {
        super(message);
    }

    @Override
    public String toString(){
        return "This is an exception class. Why are you printing it's toString() ?";
    }
}