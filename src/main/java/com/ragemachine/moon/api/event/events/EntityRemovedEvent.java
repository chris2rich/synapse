package com.ragemachine.moon.api.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

public class EntityRemovedEvent extends EntityEvent {
  
  public EntityRemovedEvent(Entity entity) {
    super(entity);
  }
}
