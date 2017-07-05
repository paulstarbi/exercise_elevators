package elevator.sample;

import elevator.ElevatorInfo;
import elevator.Request;
import elevator.RoutingController;
import elevator.RoutingOperator;
import elevator.runner.ConsoleRunner;
import elevator.runner.GuiRunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static elevator.Request.pressElevatorButton;
import static elevator.Request.pressLevelButton;
import static elevator.runner.RunnerConfiguration.*;
import static java.util.Arrays.asList;

public class Main {

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {
//        runWithGUI();
        //or:
//         runWithConsole();
        //or:
         runWithConsole_andDataFile();
        //or:
//        runWithGUI_NoGenerator_ForRecording();
    }

    private static void runWithConsole() throws InterruptedException, ClassNotFoundException,
            InstantiationException, IOException, IllegalAccessException {
        ConsoleRunner.main(new String[]{
                FLOOR_COUNT + "=10",
                ELEVATOR_COUNT + "=5",
                ROUTING_CONTROLLER_CLASS_NAME + "=" + MyRoutingController.class.getName(),
                GENERATOR_CLASS_NAME + "=" + MyButtonPressGenerator.class.getName(),
        });
    }

    //you have to record a data file with GuiRunner before using this version
    private static void runWithConsole_andDataFile() throws InterruptedException, ClassNotFoundException,
            InstantiationException, IOException, IllegalAccessException {
        ConsoleRunner.main(new String[]{
                FLOOR_COUNT + "=5",
                ELEVATOR_COUNT + "=1",
                ROUTING_CONTROLLER_CLASS_NAME + "=" + MyRoutingController.class.getName(),
                TEST_DATA_FILE + "=" + "A5F-1E-100T" //rename to recorded data file
        });
    }

    private static void runWithGUI() throws ClassNotFoundException, IOException, InstantiationException,
            InterruptedException, IllegalAccessException, InvocationTargetException {
        GuiRunner.main(new String[]{
                FLOOR_COUNT + "=5",
                ELEVATOR_COUNT + "=2",
                ROUTING_CONTROLLER_CLASS_NAME + "=" + MyRoutingController.class.getName(),
                GENERATOR_CLASS_NAME + "=" + MyButtonPressGenerator.class.getName(),
                //or comment out the above to run with the below data file (you will have to record a data file with GuiRunner first):
                //TEST_DATA_FILE + "=" + "recorded.elevators" //rename to recorded data file
        });

    }

    private static void runWithGUI_NoGenerator_ForRecording() throws ClassNotFoundException, IOException,
            InstantiationException, InterruptedException, IllegalAccessException, InvocationTargetException {
        GuiRunner.main(new String[]{
                FLOOR_COUNT + "=10",
                ELEVATOR_COUNT + "=5",
                ROUTING_CONTROLLER_CLASS_NAME + "=" + MyRoutingController.class.getName()
        });
    }


    public static class MyRoutingController implements RoutingController {

        private RoutingOperator operator;
        private Map<Integer, LinkedList<Integer>> elevatorAvaiable;

        @Override
        public void initialize(RoutingOperator operator) {
            this.operator = operator;
            this.elevatorAvaiable = new HashMap<>();
            for (int i = 0; i <operator.getElevatorCount() ; i++) {
                elevatorAvaiable.put(i,new  LinkedList<>());
            }
            //I can observe more events than just 'elevatorButtonPressed' and 'levelButtonPressed" if I want to:
//            operator.observe().whenElevatorMoved((elevatorID, level) -> System.out.println
//                    ("Hey, I can observe elevator moves! E#" + elevatorID + " is on level " + level + ". Maybe I will reroute it now."));
            //or: operator.observe().whenElevatorRequestCompleted(...);
            //or: operator.observe().whenLevelRequestCompleted(...);
        }

        public int closestElevator(int demandLevel) {
            int elevatorID = 0;
            int last = 0;

            for (int i : elevatorAvaiable.keySet()) {
                if (operator.getElevatorInfo(i).getCurrentPosition() == demandLevel) {
                    elevatorID = i;
                    break;
                }

                if (i == 0) {
                    last = elevatorAvaiable.get(i).size();
                    elevatorID = i;

                } else if (i != 0) {
                    if (elevatorAvaiable.get(i).size() < last)
                        elevatorID = i;
                }
            }
            return elevatorID;

        }


        @Override
        public void elevatorButtonPressed(int elevatorID, Integer level) {

            elevatorAvaiable.get(elevatorID).add(level);
            elevatorAvaiable.put(elevatorID, elevatorAvaiable.get(elevatorID));
            operator.setRoute(elevatorID, elevatorAvaiable.get(elevatorID));

        }

        @Override
        public void levelButtonPressed(Integer level) {
            int someElevator = closestElevator(level);
            elevatorAvaiable.get(someElevator).add(level) ;

            operator.setRoute(someElevator,elevatorAvaiable.get(someElevator));

            //which elevator should go there?
		 //int someElevator = ???
            //operator.setRoute(someElevator, ???);
        }

    }

    public static class MyButtonPressGenerator extends Request.List {

        public MyButtonPressGenerator() {
            super(createTestData());
        }

        static Map<Long, List<Request>> createTestData() {
            Map<Long, List<Request>> reqs = new HashMap<>();//requests to be made per tick
            reqs.put(0L, asList(pressLevelButton(1), pressLevelButton(8), pressLevelButton(7)));
            reqs.put(1L, asList(pressElevatorButton(1, 5),
                    pressElevatorButton(2, 6), pressElevatorButton(3, 9)));
            reqs.put(2L, asList(pressLevelButton(0), pressLevelButton(4), pressLevelButton(9)));
            reqs.put(3L, asList(pressElevatorButton(2, 7),
                    pressElevatorButton(3, 8), pressElevatorButton(4, 2)));
            reqs.put(4L, asList(pressLevelButton(2), pressLevelButton(3), pressLevelButton(1)));
            return reqs;
        }
    }
}
