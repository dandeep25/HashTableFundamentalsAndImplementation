import java.util.*;


class Vehicle {
    String licensePlate;
    long entryTime;

    Vehicle(String plate) {
        this.licensePlate = plate;
        this.entryTime = System.currentTimeMillis();
    }
}

 class ParkingLotSystem {

    static final int SIZE = 500;

    Vehicle[] parkingTable = new Vehicle[SIZE];

    int occupiedSpots = 0;
    int totalProbes = 0;
    int totalParks = 0;

    /
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % SIZE;
    }

    
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (parkingTable[index] != null) {

            index = (index + 1) % SIZE;
            probes++;

            if (probes >= SIZE) {
                System.out.println("Parking Full!");
                return;
            }
        }

        parkingTable[index] = new Vehicle(plate);

        occupiedSpots++;
        totalProbes += probes;
        totalParks++;

        System.out.println("Vehicle " + plate + " parked at spot #" + index +
                " (" + probes + " probes)");
    }

   
    public void exitVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (parkingTable[index] != null && probes < SIZE) {

            if (parkingTable[index].licensePlate.equals(plate)) {

                long entryTime = parkingTable[index].entryTime;
                long exitTime = System.currentTimeMillis();

                long durationMillis = exitTime - entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);

                double fee = Math.ceil(hours) * 5;

                parkingTable[index] = null;
                occupiedSpots--;

                System.out.println("Vehicle exited from spot #" + index);
                System.out.println("Duration: " + String.format("%.2f", hours) + " hours");
                System.out.println("Fee: $" + fee);

                return;
            }

            index = (index + 1) % SIZE;
            probes++;
        }

        System.out.println("Vehicle not found!");
    }

    public void findNearestSpot() {

        for (int i = 0; i < SIZE; i++) {

            if (parkingTable[i] == null) {
                System.out.println("Nearest available spot: #" + i);
                return;
            }
        }

        System.out.println("Parking Full!");
    }

   
    public void getStatistics() {

        double occupancy = ((double) occupiedSpots / SIZE) * 100;

        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("\nParking Statistics");
        System.out.println("-------------------");
        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Average Probes: " + String.format("%.2f", avgProbes));
        System.out.println("Total Vehicles Parked: " + totalParks);
    }

    public static void main(String[] args) {

        ParkingLotSystem system = new ParkingLotSystem();

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n1. Park Vehicle");
            System.out.println("2. Exit Vehicle");
            System.out.println("3. Find Nearest Spot");
            System.out.println("4. Statistics");
            System.out.println("5. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter license plate: ");
                    String plate = sc.nextLine();
                    system.parkVehicle(plate);
                    break;

                case 2:
                    System.out.print("Enter license plate: ");
                    plate = sc.nextLine();
                    system.exitVehicle(plate);
                    break;

                case 3:
                    system.findNearestSpot();
                    break;

                case 4:
                    system.getStatistics();
                    break;

                case 5:
                    System.out.println("System closed");
                    return;
            }
        }
    }
}
