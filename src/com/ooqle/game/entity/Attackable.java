package com.ooqle.game.entity;
/*
* @author Kenny Williams
*/

import com.ooqle.game.World;

public interface Attackable
{
    MovableActor getTarget(World world);
}
