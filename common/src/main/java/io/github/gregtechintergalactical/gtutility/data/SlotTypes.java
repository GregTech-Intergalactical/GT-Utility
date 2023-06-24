package io.github.gregtechintergalactical.gtutility.data;

import io.github.gregtechintergalactical.gtutility.gui.slots.SlotCrafting;
import muramasa.antimatter.gui.SlotType;
import muramasa.antimatter.gui.slot.AbstractSlot;
import muramasa.antimatter.machine.event.ContentEvent;
import muramasa.antimatter.tool.IAntimatterTool;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.EmptyHandler;
import tesseract.TesseractCapUtils;

public class SlotTypes {
    public static SlotType<AbstractSlot<?>> TOOLS = new SlotType<>("tools", (type, gui, item, i, d) -> new AbstractSlot<>(type, gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> i.getItem() instanceof IAntimatterTool || i.getItem().canBeDepleted(), ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<AbstractSlot<?>> TOOL_CHARGE = new SlotType<>("tool_charge", (type, gui, item, i, d) -> new AbstractSlot<>(type,gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> {
        if (t instanceof BlockEntity tile) {
            return TesseractCapUtils.getEnergyHandler(tile, null).map(eh -> TesseractCapUtils.getEnergyHandlerItem(i).map(inner -> ((inner.getInputVoltage() | inner.getOutputVoltage()) <= (eh.getInputVoltage() | eh.getOutputVoltage()) )).orElse(false)).orElse(false) || i.getItem() instanceof IAntimatterTool || i.getItem().canBeDepleted();
        }
        return true;
    }, ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<SlotCrafting> CRAFTING = new SlotType<>("crafting", (type, gui, item, i, d) -> new SlotCrafting(item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> true, ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<AbstractSlot<?>> PARK = new SlotType<>("park", (type, gui, item, i, d) -> new AbstractSlot<>(type, gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> true, ContentEvent.ITEM_INPUT_CHANGED);
}
