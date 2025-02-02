import java.util.*;
import java.util.stream.*;

import javax.lang.model.util.ElementScanner6;

/*
 * The Traveller represents the entity moving through
 * the Maze.
 */

public class Traveller
{
    public Traveller (Maze maze, boolean debug)
    {
        _theMaze = maze;
        _debug = debug;
    }

    public int findAllKeys ()
    {
        Vector<Wormhole> outerWormholes = _theMaze.outerWormholes();
        Vector<Wormhole> innerWormholes = _theMaze.innerWormholes();
        Coordinate start = Util.findWormhole(outerWormholes, Maze.START).getLocation();
        Coordinate end = Util.findWormhole(outerWormholes, Maze.END).getLocation();
        HashMap<Coordinate, List<Route>> routes = findRoutes(_theMaze.outerWormholes(), _theMaze.innerWormholes());
        PriorityQueue<Journey> journeys = new PriorityQueue<Journey>(Comparator.comparingInt(r -> r.getSteps()));

        journeys.offer(new Journey(start));
        HashSet<String> journeysTaken = new HashSet<String>();

        while (journeys.size() > 0)
        {
            Journey theJourney = journeys.poll();

            if (theJourney.getLocation().equals(end))
                return theJourney.getSteps();

            if ((theJourney.levelOfMaze() < 0) || !journeysTaken.add(theJourney.name()))
            {
                if (_debug)
                    System.out.println("Journey level: "+theJourney.levelOfMaze()+" and journey "+theJourney+" duplicated.");
            }
            else
            {
                for (Route nextRoute : routes.get(theJourney.getLocation()))
                {
                    if ((theJourney.levelOfMaze() > 0) && (nextRoute.getEnd().equals(start) || nextRoute.getEnd().equals(end)))
                    {
                        if (_debug)
                            System.out.println("Journey level: "+theJourney.levelOfMaze()+" and journey "+theJourney);
                    }
                    else
                    {
                        Journey nextJourney = new Journey(nextRoute.getEnd(), theJourney.getSteps() + nextRoute.numberOfSteps(),
                                                theJourney.levelOfMaze() + nextRoute.getLevel());

                        journeys.offer(nextJourney);
                    }
                }
            }
        }

        return -1;
    }

    private HashMap<Coordinate, List<Route>> findRoutes (Vector<Wormhole> outerWormholes, Vector<Wormhole> innerWormholes)
    {
        HashMap<Coordinate, List<Route>> routesForEachCoordinate = new HashMap<Coordinate, List<Route>>();
        Set<Wormhole> outerSet = new HashSet<Wormhole>(outerWormholes);
        Iterator<Wormhole> iter = outerSet.iterator();

        while (iter.hasNext())
        {
            Wormhole toCheck = iter.next();
            int index = innerWormholes.indexOf(toCheck);

            if (_debug)
                System.out.println("Searching for: "+toCheck);

            if (index != -1) // not present?
            {
                if (_debug)
                    System.out.println("Inner wormholes contains "+toCheck);

                Coordinate innerLocation = innerWormholes.elementAt(index).getLocation();
                List<Route> outerRoutes = routesForEachCoordinate.computeIfAbsent(toCheck.getLocation(), (k) -> new ArrayList<>());

                outerRoutes.add(new Route(toCheck.getLocation(), innerLocation, 1, -1));

                List<Route> innerRoutes = routesForEachCoordinate.computeIfAbsent(innerLocation, (k) -> new ArrayList<>());

                innerRoutes.add(new Route(innerLocation, toCheck.getLocation(), 1, 1));
            }
        }
        
        Vector<Coordinate> outerCoordinates = Util.extractCoordinates(outerWormholes);
        Vector<Coordinate> innerCoordinates = Util.extractCoordinates(innerWormholes);
        Vector<Coordinate> theCollection = new Vector<Coordinate>();

        theCollection.addAll(outerCoordinates);
        theCollection.addAll(innerCoordinates);

        Enumeration<Coordinate> theIter = theCollection.elements();

        while (theIter.hasMoreElements())
        {
            Coordinate coord = theIter.nextElement();

            routesForEachCoordinate.computeIfAbsent(coord, (k) -> new ArrayList<>()).addAll(findAllRoutes(coord, theCollection));
        }

        return routesForEachCoordinate;
    }

    private List<Route> findAllRoutes (Coordinate start, Iterable<Coordinate> allLocations)
    {
        ArrayList<Route> allRoutes = new ArrayList<Route>();

        for (Coordinate to: allLocations)
        {
            if (!start.equals(to))
                shortestPath(start, to).ifPresent(allRoutes::add);
        }

        return allRoutes;
    }

    Optional<Route> shortestPath (Coordinate start, Coordinate destination)
    {
        HashMap<Coordinate, Integer> stepsTaken = new HashMap<Coordinate, Integer>();
        HashMap<Coordinate, Coordinate> originate = new HashMap<Coordinate, Coordinate>();
        PriorityQueue<Coordinate> locations = new PriorityQueue<Coordinate>((Comparator.comparingInt(pos -> Util.cost(stepsTaken, pos, destination))));

        stepsTaken.put(start, 0);
        locations.offer(start);

        while (locations.size() > 0)
        {
            Coordinate coord = locations.poll();
            int steps = stepsTaken.get(coord);

            if (coord.equals(destination))
                return Optional.of(new Route(start, destination, steps, 0));
            else
            {
                coord.directions().stream()
                        .filter(next -> _theMaze.isPassage(next) && !stepsTaken.containsKey(next))
                        .forEach(next -> {
                            stepsTaken.put(next, steps + 1);
                            originate.put(next, coord);
                            locations.offer(next);
                        });
            }
        }

        return Optional.empty();
    }

    private Maze _theMaze;
    private boolean _debug;
}