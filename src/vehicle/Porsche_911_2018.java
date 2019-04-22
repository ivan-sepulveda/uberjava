package vehicle;

public class Porsche_911_2018 extends Car {

    /**
     *
     * @param args is unused
     */
    public static void main(String[] args){
        System.out.println("Do you want to test the Porsche_911_2018 class? (y/n)");
        System.out.println("Printing toString below");
        System.out.println((new Porsche_911_2018()).toString());
    }

    /**
     * costrcutor for this car instance
     */
    public Porsche_911_2018() {
        super("911", 2018, "Porsche");
        this.setMilesPerGallon(11.5);
        this.setpurchaseCost(219800);
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
