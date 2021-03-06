package com.ooqle.game.entity;
/*
* @author Kenny Williams
*/

import com.ooqle.game.Point;
import com.ooqle.game.World;
import com.ooqle.game.util.Tuple;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MinerNotFull extends Miner
{
    public MinerNotFull(String name, Point position, List<PImage> imgs, int rate, int animationRate, int resourceLimit, UUID uuid)
    {
        super(name, "miner", position, imgs, rate, animationRate, resourceLimit);
        this.setUUID(uuid);
    }

    public MinerNotFull(String name, Point position, List<PImage> imgs, int rate, int animationRate, int resourceLimit)
    {
        this(name, position, imgs, rate, animationRate, resourceLimit, UUID.randomUUID());
    }

    @Override
    MovableActor transform(World world)
    {
        if(this.getResourceCount() < this.getResourceLimit())
        {
            return this;
        }
        return new MinerFull(this.getName(), this.getPosition(), this.getImages(), this.getRate(), this.getAnimationRate(), this.getResourceLimit(), this.getUUID());
    }

    @Override
    Class nearestTypeForSearching()
    {
        return Ore.class;
    }

    @Override
    Tuple<List<Point>, Boolean> applyAction(World world, Actor obj)
    {
        Point pos = this.getPosition();
        if(obj == null)
        {
            return new Tuple<>(Collections.singletonList(pos), false);
        }
        Point orePt = obj.getPosition();
        if(pos.adjacent(orePt))
        {
            this.setResourceCount(1 + this.getResourceCount());
            obj.removeEntity(world);
            return new Tuple<>(new ArrayList<>(), true);
        }else
        {
            Point newPt = this.nextPosition(world, orePt);
            return new Tuple<>(world.moveWorldObject(this, newPt), false);
        }
    }

    public void transformToFull(World world)
    {
        MinerFull newObj = new MinerFull(this.getName(), this.getPosition(), this.getImages(), this.getRate(), this.getAnimationRate(), this.getResourceLimit());
        this.clearPendingActions(world);
        world.removeEntityAt(this.getPosition());
        newObj.schedule(world, 0);
        world.addWorldObject(newObj);
        newObj.scheduleAnimation(world);

        //return newObj;
    }

    public Class getGoalType()
    {
        return Ore.class;
    }

    public String entityString()
    {
        String s = " ";
        //TODO: This ordering is bad. Possibly refactor to parent
        return super.entityString() + s + this.getResourceLimit() + s + this.getRate() + s + this.getAnimationRate();
    }
}
