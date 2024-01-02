package clients.shopDisplay;

import middle.MiddleFactory;
import middle.OrderException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * The graphical display seen by customers
 * Represents a graphical display using Swing components
 * @author  Harry Cunningham
 * @version 2.0
 */
public class DisplayView extends JFrame implements Observer {
    private JTextArea displayTextArea;
    private DisplayController cont = null;

    /**
     * Construct the view
     *
     * @param rpc Window in which to construct
     * @param mf  Factory to deliver order and stock objects
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public DisplayView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        setTitle("Shop Display");
        setSize(400, 300);
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        displayTextArea = new JTextArea();
        displayTextArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        displayTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayTextArea);
        add(scrollPane);

        setVisible(true);
    }

    public void setController(DisplayController c) {
        cont = c;
    }

    /**
     * Called to update the display in the shop
     */
    @Override
    public void update(Observable aModelOfDisplay, Object arg) {
        try {
            Map<String, List<Integer>> res = ((DisplayModel) aModelOfDisplay).getOrderState();
            updateDisplayText(res);
        } catch (OrderException err) {
            updateDisplayTextError();
        }
    }

    private void updateDisplayText(Map<String, List<Integer>> orderState) {
        StringBuilder displayText = new StringBuilder();
        displayText.append("Orders in system\n");
        displayText.append("Waiting        : ").append(listOfOrders(orderState, "Waiting")).append("\n");
        displayText.append("Being picked   : ").append(listOfOrders(orderState, "BeingPicked")).append("\n");
        displayText.append("To Be Collected: ").append(listOfOrders(orderState, "ToBeCollected")).append("\n");

        SwingUtilities.invokeLater(() -> {
            displayTextArea.setText(displayText.toString());
            displayTextArea.setCaretPosition(0); // Scroll to the top
        });
    }

    private void updateDisplayTextError() {
        SwingUtilities.invokeLater(() -> {
            displayTextArea.setText("** Communication Failure **");
            displayTextArea.setCaretPosition(0); // Scroll to the top
        });
    }

    private String listOfOrders(Map<String, List<Integer>> map, String key) {
        return map.containsKey(key) ? map.get(key).toString() : "-No key-";
    }
}

