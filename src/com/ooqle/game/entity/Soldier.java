package com.ooqle.game.entity;

import com.ooqle.game.Game;
import com.ooqle.game.Point;
import com.ooqle.game.World;
import com.ooqle.game.util.Action;
import com.ooqle.game.util.GameUtils;
import com.ooqle.game.util.Tuple;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by augiedoebling on 6/1/15.
 */
public class Soldier extends MovableActor
{
    public Soldier(String name, Point position, List<PImage> imgs, int rate, int animationRate)
    {
        super(name, "soldier", position, imgs, rate, animationRate, 5);
    }

    public Class getGoalType()
    {
        return Goblin.class;
    }

    public Class nearestTypeForSearching()
    {
        return Goblin.class;
    }

    Tuple<List<Point>, Boolean> applyAction(World world, Actor obj)
    {
        Point pos = this.getPosition();
        if (obj == null)
        {
            return new Tuple<>(Collections.singletonList(pos), false);
        }
        Point orePt = obj.getPosition();
        if (pos.adjacent(orePt))
        {
            //attack the miner
            return new Tuple<>(new ArrayList<>(), true);
        } else
        {
            Point newPt = this.nextPosition(world, orePt);
            return new Tuple<>(world.moveWorldObject(this, newPt), false);
        }
    }

    public Tuple<List<Point>, Boolean> getNearest(World world, Class type)
    {
        Point pos = this.getPosition();
        Actor nearestOfType = (Actor) world.findNearestOfType(pos, type);
        return this.applyAction(world, nearestOfType);
    }

    public void schedule(World world, long ticks)
    {
        this.scheduleAction(world, this.createAction(world), ticks + this.getRate());
        this.scheduleAnimation(world);
    }

    public Action createAction(World world)
    {
        Action a = (long currentTicks) ->
        {
            Tuple<List<Point>, Boolean> tup = this.getNearest(world, this.nearestTypeForSearching());
            boolean found = tup.getValue();

            if (!this.isDead())
            {
                this.scheduleAction(world, this.createAction(world), currentTicks + this.getRate());
            } else
            {
                this.setImages(GameUtils.getSpriteImages(Game.getImage("images/characters/soldier/soldier_die.png"), 9));
                this.die(world);
            }

            return tup.getKey();
        };
        this.removePendingAction(a);
        return a;
    }

    @Override
    public List<PImage> getAttackImages(Point other)
    {
        List<PImage> leftimg = GameUtils.getSpriteImages(Game.getImage("images/characters/soldier/soldier_attack_left.png"), 5);
        List<PImage> rightimg = GameUtils.getSpriteImages(Game.getImage("images/characters/soldier/soldier_attack_right.png"), 5);
        if(this.getPosition().getX() == other.getX())
        {
            if(this.getPosition().getY() == other.getY() - 1)
            {
                //case T
                return leftimg;
            }
            if(this.getPosition().getY() == other.getY() + 1)
            {
                //case B
                return rightimg;
            }
        }

        if(this.getPosition().getX() == other.getX() - 1)
        {
            //case R
            return rightimg;
        }

        //case L
        return leftimg;
    }

    @Override
    public List<PImage> getMoveImages()
    {
        return GameUtils.getSpriteImages(Game.getImage("images/characters/soldier/soldier_move_left.png"), 6);
    }
}
