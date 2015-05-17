package com.ooqle.game.entity;
/*
* @author Kenny Williams
*/

import com.ooqle.game.Point;
import com.ooqle.game.World;
import com.ooqle.game.util.Action;
import com.ooqle.game.util.Tuple;
import org.json.simple.JSONObject;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Miner extends MovableActor
{
    private int resourceLimit;

    public Miner(String name, String type, Point position, List<PImage> imgs, int rate, int animationRate, int resourceLimit)
    {
        super(name, type, position, imgs, rate, animationRate);
        this.resourceLimit = resourceLimit;
    }

    abstract Miner transform(World world);
    abstract Class nearestTypeForSearching();
    abstract Tuple<List<Point>, Boolean> applyAction(World world, Actor obj);

    public int getResourceLimit()
    {
        return resourceLimit;
    }

    public Tuple<List<Point>, Boolean> getNearest(World world, Class type)
    {
        Point pos = this.getPosition();
        Actor nearestOfType = (Actor) world.findNearestOfType(pos, type);
        return this.applyAction(world, nearestOfType);
    }

    private Miner tryTransform(World world)
    {
        Miner newObj = this.transform(world);
        if(this != newObj)
        {
            this.clearPendingActions(world);
            world.removeEntityAt(this.getPosition());
            world.addWorldObject(newObj);
            newObj.scheduleAnimation(world);
        }
        return newObj;
    }

    public Action createAction(World world)
    {
        Action a = (long currentTicks) ->
        {
            Tuple<List<Point>, Boolean> tup = this.getNearest(world, this.nearestTypeForSearching());
            boolean found = tup.getValue();
            Miner newEntity = this;

            if(found)
            {
                newEntity = this.tryTransform(world);
            }

            newEntity.scheduleAction(world, newEntity.createAction(world), currentTicks + this.getRate());
            return tup.getKey();
        };
        this.removePendingAction(a);
        return a;
    }

    public void schedule(World world, long ticks)
    {
        this.scheduleAction(world, this.createAction(world), ticks + this.getRate());
        this.scheduleAnimation(world);
    }

    public JSONObject entityJSON()
    {
        JSONObject out = super.entityJSON();
        out.put("resourceLimit", this.getResourceLimit());
        return out;
    }
}
