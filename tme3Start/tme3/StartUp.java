package tme3;

public class StartUp {

    public static void main(String[] args) {
        //initialize first green house controls and its controller then call the gui

        GreenhouseControls gc = new GreenhouseControls();
        Controller c = new Controller(gc);

        SwingGui GUI = new SwingGui(c);


    }
}
