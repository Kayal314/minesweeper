import javax.swing.*;

public class Cell {
    boolean hasMine;
    boolean canUseDynamite;
    int state;
    boolean hidden;
    JButton button;
    Cell(JButton button, boolean hasMine) {
        this.button = button;
        this.hasMine = hasMine;
        canUseDynamite=false;
        state = 0;
        hidden = true;
    }
}

