import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {

    private static List<Passenger> generatePassengers() {

        List<Passenger> passengers = new ArrayList<>();
        Set<String> names = new HashSet<>();
        while (names.size() < 999) {
            int length = 16;
            Random r = new Random();
            String s = r.ints(48, 122)
                    .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                    .mapToObj(i -> (char) i)
                    .limit(length)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
            names.add(s);
        }
        names.add("Looser");
        List<String> namesList = new ArrayList<>(names);

        for (int i = 1; i <= namesList.size(); i++) {
            passengers.add(new Passenger(i, namesList.get(i - 1)));
        }

        return passengers;
    }


    public static <T> T getRandomElement(List<? extends T> list) {
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }


    public static int changeLooserSeat(List<String> seats, List<String> freeSeatsList) {
        String randomIndex = getRandomElement(freeSeatsList);
        seats.set(seats.indexOf(randomIndex), "Looser");
        return Integer.parseInt(randomIndex);
    }

    public static void main(String[] args) {

        int currentIndexOfLooser;
        int count = 0;
        int lastPassengerSeat = 0;
        int iteration = 1000;



        while (iteration >= 0) {
            List<Passenger> passengers = generatePassengers();

            List<String> seats = IntStream.range(1, 1001)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());

            List<String> freeSeats = IntStream.range(1, 1001)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());

            int realSeatOfLooser = passengers.stream()
                    .filter(passenger -> passenger.getName().equals("Looser"))
                    .map(Passenger::getSeat)
                    .findFirst()
                    .orElse(-1);

            String randomSeat = getRandomElement(freeSeats);

            seats.set(seats.indexOf(randomSeat), "Looser");
            passengers.removeIf(passenger -> passenger.getName().equals("Looser"));

            boolean isFinished = false;

            while (!isFinished) {

                if (realSeatOfLooser == seats.indexOf("Looser")) {
//                    System.out.println("Looser set on his own seat!");
//                    System.out.println(count);
                    if (freeSeats.size() >= 1) {
                        lastPassengerSeat++;
                    }
                    isFinished = true;
                }
                List<Integer> remainingSeat = passengers.stream().map(Passenger::getSeat).collect(Collectors.toList());
                int passengerSeat = getRandomElement(remainingSeat);

                Passenger passengerForRemove = passengers.stream()
                        .filter(passenger -> passenger.getSeat() == passengerSeat)
                        .findFirst()
                        .orElse(new Passenger(0, "Unknown"));

                if (("Looser").equals(seats.get(passengerSeat - 1))) {
                    seats.set(seats.indexOf("Looser"), passengerForRemove.getName());
                    freeSeats.removeIf(s -> s.equals(String.valueOf(passengerSeat)));
                    passengers.remove(passengerForRemove);
                    currentIndexOfLooser = changeLooserSeat(seats, freeSeats);
                    count++;
                    if (currentIndexOfLooser == realSeatOfLooser) {
//                        System.out.println("Looser set on his own seat!");
//                        System.out.println(count);
                        if (freeSeats.size() >= 2) {
                            lastPassengerSeat++;
                        }
                        isFinished = true;
                    }
                } else {
                    seats.set(seats.indexOf(String.valueOf(passengerSeat)), passengerForRemove.getName());
                    freeSeats.removeIf(s -> s.equals(String.valueOf(passengerSeat)));
                    passengers.remove(passengerForRemove);
                }
            }

            iteration--;
        }
        int notLastPassengerSeat = 1000 - lastPassengerSeat;
        System.out.println("lastPassengerSeat -> " + lastPassengerSeat);
        System.out.println("notLastPassengerSeat -> " + notLastPassengerSeat);
    }
}
