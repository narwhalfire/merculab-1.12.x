package net.scottnotfound.merculab.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.scottnotfound.merculab.test.TestItem;


public class TestTileEntityProcessAB extends TileEntity implements ITickable {

    public IInventory processABInv = new InventoryBasic("Process AB", false, 2);
    private int processTime;
    private int processTimeTotal;

    private NonNullList<ItemStack> tileProcessItemStacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);

    public int getSizeInventory() {
        return this.tileProcessItemStacks.size();
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isEmpty() {
        for (ItemStack itemStack : this.tileProcessItemStacks) {
            if (itemStack.isEmpty()) {
                return false;
            }
        }
        return false;
    }

    public ItemStack getStackInSlot(int index) {
        return this.tileProcessItemStacks.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.tileProcessItemStacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.tileProcessItemStacks, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemStack = this.tileProcessItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(stack, itemStack);
        this.tileProcessItemStacks.set(index,stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.processTimeTotal = this.getProcessTime(stack);
            this.processTime = 0;
            this.markDirty();
        }
    }

    public String getName() {
        return "processab";
    }

    public int getProcessTime(ItemStack stack) {
        return 100;
    }

    private boolean canProcess() {
        ItemStack itemStack = this.tileProcessItemStacks.get(0);
        ItemStack itemStack1 = this.tileProcessItemStacks.get(1);
        ItemStack itemStacka = new ItemStack(TestItem.a);

        if (itemStack.isEmpty()) {
            return false;
        } else {
            if (itemStack != itemStacka) {
                return false;
            } else if (itemStack1.isEmpty()) {
                return true;
            } else if (itemStack.getCount() + itemStack1.getCount() <= this.getInventoryStackLimit()
                    && itemStack.getCount() + itemStack1.getCount() <= itemStack1.getMaxStackSize()) {
                return true;
            } else {
                return itemStack.getCount() + itemStack1.getCount() <= itemStack.getMaxStackSize();
            }
        }
    }

    public void processItem() {
        if (this.canProcess()) {
            ItemStack itemStack = this.tileProcessItemStacks.get(0);
            ItemStack itemStack1 = this.tileProcessItemStacks.get(1);
            ItemStack itemStacka = new ItemStack(TestItem.a);
            ItemStack itemStackb = new ItemStack(TestItem.b);

            if (itemStack1.isEmpty()) {
                this.tileProcessItemStacks.set(1, itemStackb.copy());
            } else if (itemStack1.getItem() == itemStackb.getItem()) {
                itemStack1.grow(itemStack.getCount());
            }

            itemStack.shrink(1);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.tileProcessItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.tileProcessItemStacks);
        this.processTime = compound.getInteger("ProcessTime");
        this.processTimeTotal = compound.getInteger("ProcessTimetotal");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("ProcessTime", (short)this.processTime);
        compound.setInteger("ProcessTimeTotal", (short)this.processTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.tileProcessItemStacks);

        return compound;
    }

    @Override
    public void update() {

        boolean flagDirty = false;

        if (!this.world.isRemote) {
            if (this.canProcess()) {
                ++this.processTime;
                if (this.processTime == this.processTimeTotal) {
                    this.processTime = 0;
                    this.processTimeTotal = this.getProcessTime(this.tileProcessItemStacks.get(0));
                    this.processItem();
                    flagDirty = true;
                }
            } else {
                this.processTime = 0;
            }
        }
        if (flagDirty) {
            this.markDirty();
        }
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        }
        else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5d,
                    (double)this.pos.getY() + 0.5d, (double)this.pos.getZ() + 0.5d) <= 64d;
        }
    }

    public void openInventory(EntityPlayer player) {

    }

    public void closeInventory(EntityPlayer player) {

    }

    public int getField(int id) {
        switch (id) {
            case 0:
                return this.processTime;
            case 1:
                return this.processTimeTotal;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.processTime = value;
            case 1:
                this.processTimeTotal = value;
            default:
                return;
        }
    }

    public int getFieldCount() {
        return 4;
    }

    public void clear() {
        this.tileProcessItemStacks.clear();
    }
}
