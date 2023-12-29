package clients.shopDisplay;

import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderException;
import middle.OrderProcessing;

import java.util.List;
import java.util.Map;
import java.util.Observable;

// File is complete but not optimal
//  Will force update of display every 2 seconds
//  Could be clever & only ask for an update of the display 
//   if it really has changed

/**
 * Only updates the display when there is a change in the order state
 * @author  Harry Cunningham
 * @version 3.0
 */
public class DisplayModel extends Observable {
    private OrderProcessing theOrder = null;
    private Map<String, List<Integer>> previousOrderState = null;

    /**
     * Set up initial connection to the order processing system
     * @param mf Factory to return an object to access the order processing system
     */
    public DisplayModel(MiddleFactory mf) {
        try {
            theOrder = mf.makeOrderProcessing();
        } catch (Exception e) {
            // Serious error in system (Should not occur)
            DEBUG.error("ModelOfDisplay: " + e.getMessage());
        }
        new Thread(this::backgroundRun).start();
    }

    /**
     * Run as a thread in the background to continually update the display
     */
    public void backgroundRun() {
        while (true) {
            try {
                Thread.sleep(2000);
                DEBUG.trace("ModelOfDisplay checking for updates");
                
                Map<String, List<Integer>> currentOrderState = theOrder.getOrderState();
                if (!orderStateEquals(previousOrderState, currentOrderState)) {
                    DEBUG.trace("ModelOfDisplay call view");
                    setChanged();
                    notifyObservers(currentOrderState);
                    previousOrderState = currentOrderState;
                }
                
            } catch (InterruptedException | OrderException e) {
                DEBUG.error("ModelOfDisplay.run()\n%s\n", e.getMessage());
            }
        }
    }

    private boolean orderStateEquals(Map<String, List<Integer>> state1, Map<String, List<Integer>> state2) {
        // Implement your logic to compare order states
        // For simplicity, assuming both are not null
        return state1.equals(state2);
    }

    /**
     * Will be called by the viewOfDisplay
     * when it is told that the view has changed
     * @return Map containing the current order state
     * @throws OrderException if there is an issue with the order processing
     */
    public synchronized Map<String, List<Integer>> getOrderState() throws OrderException {
        return theOrder.getOrderState();
    }
}
