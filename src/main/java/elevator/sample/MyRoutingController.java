//package elevator.sample;
//
///**
// * Created by paul on 05.07.17.
// */
//    public static class MyRoutingController implements RoutingController {
//
//        private RoutingOperator operator;
//
//        @Override
//        public void initialize(RoutingOperator operator) {
//            this.operator = operator;
////            distributeElevators();
//            //I can observe more events than just 'elevatorButtonPressed' and 'levelButtonPressed" if I want to:
//            operator.observe().whenElevatorMoved((elevatorID, level) -> System.out.println("Hey, I can observe elevator moves! E#" + elevatorID + " is on level " + level + ". Maybe I will reroute it now."));
//            //or: operator.observe().whenElevatorRequestCompleted(...);
//            //or: operator.observe().whenLevelRequestCompleted(...);
//        }
//
//        private void demo() {
//            int currentPosition = operator.getElevatorInfo(1).getCurrentPosition();
//            ElevatorInfo.DoorState currentState = operator.getElevatorInfo(1).getCurrentState();
//            int elevatorID1 = operator.getElevatorInfo(1).getElevatorID();
//
//            List<Integer> requestQueue = operator.getElevatorInfo(1).getRequestQueue();
//            List<Integer> currentRoute = operator.getElevatorInfo(1).getCurrentRoute();
//
//            int levelCount = operator.getLevelCount();
//            int elevatorCount = operator.getElevatorCount();
//            List<ElevatorInfo> elevatorsInfo = operator.getElevatorsInfo();
//            List<Integer> levelRequestQueue = operator.getLevelRequestQueue();
////            operator.setRoute();
//        }
//
//        private void distributeElevators() {
//            Integer level = 0;
//            Integer distanceBetweenTwoLevels = operator.getLevelCount() / operator.getElevatorCount();
//            List<Integer> currentRoute;
//            for (int i = 0; i < operator.getElevatorCount(); i++) {
//                currentRoute = new ArrayList<>();
//                currentRoute.add(level);
//                operator.setRoute(i, currentRoute);
//                level += distanceBetweenTwoLevels;
//            }
//        }
//
//        private void addNewLevelToRoute(int elevatorID, Integer level) {
//            operator.setRoute(elevatorID, getCopyOfRouteWithAddedLevel(elevatorID, level));
//        }
//
//        private List<Integer> getCopyOfRouteWithAddedLevel(int elevatorID, Integer level) {
//            List<Integer> elevatorsRoute = new ArrayList<>(operator.getElevatorInfo(elevatorID).getCurrentRoute());
//            elevatorsRoute.add(level);
//            return elevatorsRoute;
//        }
//
//        private Integer closestElevator(Integer userLevel) {
//            Integer minDistanceToUser = operator.getLevelCount();
//            Integer closestElevator = operator.getElevatorInfo(0).getElevatorID();
//            for (ElevatorInfo elevator : operator.getElevatorsInfo()) {
//                Integer currentElevatorDistanceToUser = Math.abs(userLevel - elevator.getCurrentPosition());
//                if (currentElevatorDistanceToUser < minDistanceToUser) {
//                    closestElevator = elevator.getElevatorID();
//                    minDistanceToUser = currentElevatorDistanceToUser;
//                }
//            }
//            return closestElevator;
//        }
//
//        private Integer mostAvailableElevator() {
//            Integer minStagesCount = Integer.MAX_VALUE;
//            Integer elevatorWithMinStagesCount = operator.getElevatorInfo(0).getElevatorID();
//            for (ElevatorInfo elevator : operator.getElevatorsInfo()) {
//                int currentMinStagesCount = elevator.getCurrentRoute().size();
//                if (currentMinStagesCount < minStagesCount) {
//                    elevatorWithMinStagesCount = elevator.getElevatorID();
//                    minStagesCount = currentMinStagesCount;
//                }
//            }
//            return elevatorWithMinStagesCount;
//        }
//
//        private Integer bestElevator(Integer userLevel) {
//            Integer closestElevator = closestElevator(userLevel);
//            Integer mostAvailableElevator = mostAvailableElevator();
//            return closestElevator;
//        }
//
//        @Override
//        public void levelButtonPressed(Integer level) {
//            Integer bestElevator = bestElevator(level);
//            addNewLevelToRoute(bestElevator, level);
//        }
//
//        ///////////////////////////////////////////////////////////////////////////
//        //
//        ///////////////////////////////////////////////////////////////////////////
//
//        private void inElevator1(int elevatorID, Integer level) {
//            addNewLevelToRoute(elevatorID, level);
//        }
//
//        private void inElevator2(int elevatorID, Integer level) {
//            addNewLevelToRoute(elevatorID, level);
//
//            Integer closestFloor = Integer.MAX_VALUE;
//            List<Integer> requestQueue = new ArrayList<>(operator.getElevatorInfo(elevatorID).getRequestQueue());
//            List<Integer> newRouteQueue = new ArrayList<>();
//            for (Integer floor : requestQueue) {
//                Integer currentClosestFloor = Math.abs(operator.getElevatorInfo(elevatorID).getCurrentPosition() - floor);
//                if (currentClosestFloor < closestFloor) {
//                    newRouteQueue.add(currentClosestFloor);
//                    closestFloor = currentClosestFloor;
//                }
//            }
//            operator.setRoute(elevatorID, newRouteQueue);
//        }
//
//        private void inElevator3(int elevatorID, Integer level) {
//            List<Integer> currentRoute = getCopyOfRouteWithAddedLevel(elevatorID, level);
//
//            List<Integer> sortedRoute = new ArrayList<>(currentRoute);
//            Collections.sort(sortedRoute);
//
//            List<Integer> bottomLevels = new ArrayList<>();
//            List<Integer> topLevels = new ArrayList<>();
//            for (Integer floor : sortedRoute) {
//                if (floor < level) {
//                    bottomLevels.add(floor);
//                } else {
//                    topLevels.add(floor);
//                }
//            }
//            List<Integer> bottomLevelsReversed = new ArrayList<>(bottomLevels);
//            Collections.reverse(bottomLevels);
//
//            currentRoute.clear();
//            currentRoute.addAll(bottomLevelsReversed);
//            currentRoute.addAll(topLevels);
//            operator.setRoute(elevatorID, currentRoute);
//        }
//
//        @Override
//        public void elevatorButtonPressed(int elevatorID, Integer level) {
////            inElevator1(elevatorID,level);
////            inElevator2(elevatorID, level);
//            inElevator3(elevatorID, level);
//        }
//    }
//
