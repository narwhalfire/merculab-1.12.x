package net.scottnotfound.merculab.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockChemicalBase extends Block implements IMercuLabTileProv {

    public BlockChemicalBase(Material material) {
        super(material);
    }
}
