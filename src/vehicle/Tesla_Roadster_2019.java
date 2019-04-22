package vehicle;

import uberjava.Driver;

import java.util.Scanner;

public class Tesla_Roadster_2019 extends Car {
    /**
     *
     * @param args unsued
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the Tesla_Roadster_2019 class? (y/n)");

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while (!input.equals("y") || !input.equals("n")){
            System.out.println("Please type 'y' or 'n' and hit enter.");
            input = sc.nextLine();
        }
        if (input.equals("y")){
            System.out.println("Testing code now:");
            System.out.println("Printing toString() of arbitrary Tesla_Roadster_2019");
            System.out.println((new Tesla_Roadster_2019()).toString());
        }

    }

    /**
     * constructor for Tesla_Roadster_2019
     */
    public Tesla_Roadster_2019() {
        super("Roadster", 2019, "Tesla");
        this.setMilesPerGallon(102.0);
        this.setpurchaseCost(200400);
        this.setOperatingPricePerMinute();
    }

    /**
     *
     * @return the objects toString. "Car: " + className because classname contains all relevant info;
     */
    @Override
    public String toString(){
        return "Car: " + this.getClass().getSimpleName();
    }
}
