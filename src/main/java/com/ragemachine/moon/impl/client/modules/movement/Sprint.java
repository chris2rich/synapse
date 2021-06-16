package com.ragemachine.moon.impl.client.modules.movement;

import com.ragemachine.moon.api.event.events.MoveEvent;
import com.ragemachine.moon.impl.client.modules.Module;
import com.ragemachine.moon.impl.client.setting.Value;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Sprint extends Module {

    public Value<Mode> mode = addSetting(new Value("Mode", Mode.LEGIT));

    private static Sprint INSTANCE = new Sprint();

    public Sprint() {
        super("Sprint", "Modifies sprinting", Category.MOVEMENT, false, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Sprint getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Sprint();
        }
        return INSTANCE;
    }

    public enum Mode {
        LEGIT,
        RAGE
    }

    @SubscribeEvent
    public void onSprint(MoveEvent event) {
        if(event.getStage() == 1 && mode.getValue() == Mode.RAGE && (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f)) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        switch (mode.getValue()) {
            case RAGE:
                if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) && !(mc.player.isSneaking() || mc.player.collidedHorizontally || mc.player.getFoodStats().getFoodLevel() <= 6f)) {
                    mc.player.setSprinting(true);
                }
                break;
            case LEGIT:
                if (mc.gameSettings.keyBindForward.isKeyDown() && !(mc.player.isSneaking() || mc.player.isHandActive() || mc.player.collidedHorizontally || mc.player.getFoodStats().getFoodLevel() <= 6f) && mc.currentScreen == null) {
                    mc.player.setSprinting(true);
                }
                break;
        }
    }

    @Override
    public void onDisable() {
        if (!nullCheck()) {
            mc.player.setSprinting(false);
        }
    }

    @Override
    public String getDisplayInfo() {
        return mode.currentEnumName();
    }
}
