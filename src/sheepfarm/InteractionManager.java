//package sheepfarm;
//
//import java.util.List;
//
//public class InteractionManager {
//
//    public static void handleInteractions(List<GameObject> objects) {
//        for (int i = 0; i < objects.size(); i++) {
//            GameObject a = objects.get(i);
//            for (int j = i + 1; j < objects.size(); j++) {
//                GameObject b = objects.get(j);
//
//                if (a.canInteractWith(b)) a.interact(b);
//                if (b.canInteractWith(a)) b.interact(a);
//            }
//        }
//    }
//}
