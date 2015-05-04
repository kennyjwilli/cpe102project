package com.ooqle.game.entity;
/*
* @author Kenny Williams
*/

import com.ooqle.game.Point;
import com.ooqle.game.WorldModel;
import com.ooqle.game.util.GameUtils;

public class MovableActor extends AnimatedActor
{
    public MovableActor(String name, String type, Point position, int rate)
    {
        super(name, type, position, rate);
    }

    public Point nextPosition(WorldModel world, Point destPt)
    {
        int horiz = GameUtils.sign(destPt.getX() - this.getPosition().getX());
        Point newPt = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());

        if(horiz == 0 || world.isOccupied(newPt))
        {
            int vert = GameUtils.sign(destPt.getY() - this.getPosition().getY());

        }
    }
}
