package perspective;

import perspective.*;

public class Player {

    public double x, y, rot;

    public Player() {

    }

    public void update(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight) {
        double wSpeed = 0.1;
        double rSpeed = 0.1;

        double xd = 0;
        double yd = 0;
        double rd = 0;

        if (up) xd++;
        if (down) xd--;
        if (left) yd++;
        if (right) yd--;
        if (turnLeft) rot += rSpeed;
        if (turnRight) rot -= rSpeed;

        double rCos = Math.cos(rot);
        double rSin = Math.sin(rot);

        x += (xd * rCos + yd * rSin) * wSpeed;
        x += (xd * rCos - yd * rSin) * wSpeed;
    }

    public void render() {

    }
}