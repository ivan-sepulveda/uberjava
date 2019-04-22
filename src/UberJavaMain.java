import uberjava.Driver;
import uberjava.Rider;
import uberjava.Session;
import vehicle.BMW_M2_2019;
import vehicle.Car;
import vehicle.Porsche_911_2018;
import vehicle.Tesla_Roadster_2019;

public class UberJavaMain {
    /**
     *
     * @param args unused
     */
    public static void main(String[] args) {
        Driver prateek = new Driver("Prateek", true);
        prateek.assignCar(new Tesla_Roadster_2019());
        Driver jamesNaismith = new Driver("James Naismith", true);
        jamesNaismith.assignCar(new Porsche_911_2018());
        Driver ivan = new Driver("Ivan Sepulveda", false);
        ivan.assignCar(new BMW_M2_2019());
        System.out.println("All Cars");
        System.out.println(Car.getCars());
        System.out.println();
        System.out.println("\n\n");

//        System.out.println("Prateek's Session has started.");
        prateek.startSession();
        jamesNaismith.startSession();
        ivan.startSession();
//        System.out.println("Prateek's Session has ended. Summary below.");
        Session.printSessionSummary(prateek.getLastSession());
        Session.printSessionSummary(jamesNaismith.getLastSession());
        Session.printSessionSummary(ivan.getLastSession());

        Rider.printAllRiderStats();
//        System.out.println();

    }

    /**
     *
     * @return toString of this main method..
     */
    @Override
    public String toString(){
        return "This is a main method. toString() shouldn't be needed but gotta cover my bases (:";
    }

}