package sarfflow.infstorage.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import sarfflow.infstorage.ExampleMod;
import sarfflow.infstorage.inventory.InfInventory;

public class InfChestBlockEntity extends BlockEntity implements InfInventory {
    public DefaultedList<ItemStack> items;

    public InfChestBlockEntity(BlockPos pos, BlockState state) {
        super(ExampleMod.INF_CHEST_BLOCK_ENTITY, pos, state);
        this.items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtList nbtList = nbt.getList("Items", 10);
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(Math.max(nbtList.size() * 2, 1), ItemStack.EMPTY);
        int j = 0;
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
//            int j = nbtCompound.getByte("Slot") & 255;
            if (!nbtCompound.isEmpty()) stacks.set(j++, ItemStack.fromNbt(nbtCompound));
        }
        this.items = stacks;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtList nbtList = new NbtList();

        int j = 0;
        for(int i = 0; i < items.size(); ++i) {
            ItemStack itemStack = items.get(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)j++);
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            }
        }

        if (!nbtList.isEmpty()) {
            nbt.put("Items", nbtList);
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        //Insert item to an empty slot
        boolean insertToEmptySlot = this.items.get(slot).isEmpty() && !stack.isEmpty();
        int curSize = this.items.size();

        this.items.set(slot, stack);

        if (insertToEmptySlot) {
            int emptySlotNum = emptyCount();
            if (emptySlotNum == 0) {
                resizeStacks(curSize * 2);
            } else if (emptySlotNum >= curSize * 3 / 4) {
                resizeStacks((curSize - emptySlotNum) * 2);
            }
        }

        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    public void resizeStacks(int newSize) {
        DefaultedList<ItemStack> newList = DefaultedList.ofSize(newSize, ItemStack.EMPTY);
        if (this.items.size() <= newSize) {
            for (int i = 0; i < this.items.size(); i++) {
                newList.set(i, this.items.get(i));
            }
        } else {
            int j = 0;
            for (int i = 0; i < this.items.size(); i++) {
                if (!this.items.get(i).isEmpty()) newList.set(j++, this.items.get(i));
            }
        }

        this.items = newList;
    }

    public int emptyCount() {
        int empty = 0;
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).isEmpty()) empty++;
        }
        return empty;
    }

}
