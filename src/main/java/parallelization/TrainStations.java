package parallelization;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * In this exercise, given the GPS location of a person, you must find the train station that is the closest
 * to this location. Because there are many train stations, you are asked to speed up the search using
 * two threads. Furthermore, as the computation of the distance between two locations on Earth involves quite
 * a complex formula, you are given a function that computes this distance for you.
 */
public class TrainStations {

    /**
     * Class that represents a GPS location on Earth, encoded as pair associating
     * a latitude with a longitude (both expressed in degrees).
     */
    public static class Location {
        private double latitude;
        private double longitude;

        public Location(double latitude,
                        double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }


    /**
     * Interface that represents a train station. A train station consists of a name and
     * of a GPS location on Earth. You will *not* have to implement this interface.
     */
    public interface Station {
        String getName();

        Location getLocation();
    }


    /**
     * Interface that encapsulates a function computing the distance between two
     * GPS locations on Earth. You will *not* have to implement this interface.
     */
    public interface DistanceFunction {
        double compute(Location a,
                       Location b);
    }


    /**
     * Given a pair of train stations, return the station that is the closest to the location
     * of a passenger, according to a distance function. Each train station is given as an
     * "Optional", which means that it might be absent.
     *
     * @param passenger        GPS location of the passenger.
     * @param distanceFunction The distance function to be used.
     * @param station1         The first station (might be empty).
     * @param station2         The second station (might be empty).
     * @return The closest train station, or an empty Optional (*not* the "null" value)
     * if both train stations are empty.
     */
    public static Optional<Station> getClosestStation(Location passenger,
                                                      DistanceFunction distanceFunction,
                                                      Optional<Station> station1,
                                                      Optional<Station> station2) {
        if (station1 == null || station2 == null) {
            throw new IllegalArgumentException();
        }

        // STUDENT return null;  // TODO
        // BEGIN STRIP
        if (station1.isPresent() &&
                station2.isPresent()) {
            if (distanceFunction.compute(passenger, station1.get().getLocation()) <
                    distanceFunction.compute(passenger, station2.get().getLocation())) {
                return station1;
            } else {
                return station2;
            }
        } else if (station1.isPresent()) {
            return station1;
        } else if (station2.isPresent()) {
            return station2;
        } else {
            return Optional.empty();
        }
        // END STRIP
    }


    /**
     * Sequential algorithm to find the train station that is the closest to the location
     * of a passenger, given an array of possible train stations.
     * <p>
     * In order to enable multithreading, this algorithm must consider only a subarray of the
     * provided train stations. More precisely, you are given two parameters "start" and "end",
     * and you must only consider the stations in the range from "stations[start]" to
     * "stations[end - 1]". In the case where "start == end", there is no train station to consider.
     * <p>
     * Hint: You are encouraged to use "getClosestStation()" in this method (though this is not mandatory).
     *
     * @param passenger        GPS location of the passenger.
     * @param distanceFunction The distance function to be used.
     * @param stations         The list of available train stations.
     * @param start            Start index of the subarray of train stations (inclusive).
     * @param end              End index of the subarray of train stations (exclusive).
     * @return The closest train station. The return value has to be an "Optional",
     * because the subarray may be empty (if "start == end"). If the subarray is empty,
     * the method must return an empty Optional (i.e., *not* null).
     */
    public static Optional<Station> findClosestStationSequential(Location passenger,
                                                                 DistanceFunction distanceFunction,
                                                                 Station[] stations,
                                                                 int start,
                                                                 int end) {
        if (start < 0 || start > end || end > stations.length) {
            throw new IllegalArgumentException();
        }

        // STUDENT return null;  // TODO
        // BEGIN STRIP
        if (start == end) {
            return Optional.empty();
        } else {
            Optional<Station> closest = Optional.empty();

            for (int i = start; i < end; i++) {
                closest = getClosestStation(passenger, distanceFunction, Optional.of(stations[i]), closest);
            }

            return closest;
        }
        // END STRIP
    }


    /**
     * Parallel algorithm to find the train station that is the closest to the location
     * of a passenger, given an array of possible train stations. You *must* use two
     * threads to carry on this computation, and you *must* use the provided thread pool.
     * <p>
     * The amount of work must be balanced between the two threads (i.e., the two threads must read
     * roughly the same number of train stations).
     * You are encouraged to use "findClosestStationSequential()" in this method but this is not mandatory.
     * <p>
     * You MUST catch all exceptions related to multithreading. If such an exception is thrown, you must return "null".
     * <p>
     *
     * @param passenger        GPS location of the passenger.
     * @param distanceFunction The distance function to be used.
     * @param stations         The list of available train stations.
     * @param executorService  The thread pool to be used. You must *not* call the method "Executors.newFixedThreadPool()"
     *                         to create the thread pool, neither the method "executor.shutdown()" (this is done for you
     *                         in the unit tests).
     * @return The closest train station. If the "stations" array is empty, the method must return
     * an empty Optional (i.e., *not* null).
     */
    public static Optional<Station> findClosestStationParallel(Location passenger,
                                                               DistanceFunction distanceFunction,
                                                               Station[] stations,
                                                               ExecutorService executorService) {
        // STUDENT return null;  // TODO
        // BEGIN STRIP
        Future<Optional<Station>> future1 = executorService.submit(() -> findClosestStationSequential(passenger, distanceFunction, stations, 0, stations.length / 2));
        Future<Optional<Station>> future2 = executorService.submit(() -> findClosestStationSequential(passenger, distanceFunction, stations, stations.length / 2, stations.length));

        try {
            return getClosestStation(passenger, distanceFunction, future1.get(), future2.get());
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
        // END STRIP
    }
}
