package net.minecraft.server;

import net.minecraft.server.BlockSapling.TreeGenerator;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.craftbukkit.CraftBlockChangeDelegate;
import org.bukkit.craftbukkit.util.StructureGrowDelegate;
import org.bukkit.entity.Player;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.Random;

public class SproutSapling {

    private int i;
    private int i1 = 0;
    private int j;
    private int j1 = 0;
    private int k;
    private World world;
    private int type;
    private TreeType treeType;
    private boolean bonemeal;
    private Player player;
    private TreeGenerator object;
    private boolean flag = false;
    private BlockSapling sapling;
	
    public SproutSapling(int tempI, int tempJ, int tempK, World tempWorld, int tempType, Player tempPlayer, BlockSapling tempSapling){
        i = tempI;
        j = tempJ;
        k = tempK;
        sapling = tempSapling;
        world = tempWorld;
        type = tempType;
        player = tempPlayer;
        Random random = new Random();
        if (random.nextInt(10) == 0) {
            treeType = TreeType.BIG_TREE;
            object = new WorldGenBigTree(true);
        } else {
            treeType = TreeType.TREE;
            object = new WorldGenTrees(true);
        }
   }
    
   public void calcTree(TreeType tempType, TreeGenerator tempGenerator){
	   treeType = tempType; // CraftBukkit
       label78:
       for (i1 = 0; i1 >= -1; --i1) {
           for (j1 = 0; j1 >= -1; --j1) {
               if (sapling.a(world, i + i1, j, k + j1, type) 
            		   && sapling.a(world, i + i1 + 1, j, k + j1, type) 
            		   && sapling.a(world, i + i1, j, k + j1 + 1, type) 
            		   && sapling.a(world, i + i1 + 1, j, k + j1 + 1, type)) {
                   object = tempGenerator;
                   flag = true;
                   break label78;
               }
           }
       }
   }
   
   public void notFlag(TreeGenerator tempGenerator){
       if(!flag){
    	   j1 = 0;
    	   i1 = 0;
    	   object = tempGenerator;
       }
   }
   
   public void setTreeAndGen(TreeGenerator tempGenerator, TreeType tempType){
	   if(!flag){
          object = tempGenerator;
          treeType = tempType;
	      j1 = 0;
	      i1 = 0;
	   }
   }
   
   public boolean getFlag(){
	   return flag;
   }
   
   public void worldSetter(){
	   Block block = Blocks.AIR;
       if (flag) {
           world.setTypeAndData(i + i1, j, k + j1, block, 0, 4);
           world.setTypeAndData(i + i1 + 1, j, k + j1, block, 0, 4);
           world.setTypeAndData(i + i1, j, k + j1 + 1, block, 0, 4);
           world.setTypeAndData(i + i1 + 1, j, k + j1 + 1, block, 0, 4);
       } else {
           world.setTypeAndData(i, j, k, block, 0, 4);
       }
   }
   
   public void growTree(ItemStack itemstack) {
			Random random = new Random();
	        StructureGrowDelegate delegate = new StructureGrowDelegate(world);
	        boolean grownTree;
	        grownTree = object.generate(new CraftBlockChangeDelegate(delegate), random, i + i1, j, k + j1);
	        if (grownTree) {
	            Location location = new Location(world.getWorld(), i, j, k);
	            StructureGrowEvent event = new StructureGrowEvent(location, treeType, bonemeal, player, delegate.getBlocks());
	            org.bukkit.Bukkit.getPluginManager().callEvent(event);
	            if (event.isCancelled()) {
	                grownTree = false;
	            } else {
	                for (org.bukkit.block.BlockState state : event.getBlocks()) {
	                    state.update(true);
	                }
	                if (event.isFromBonemeal() && itemstack != null) {
	                    --itemstack.count;
	                }
	            }
	        } else if (bonemeal && itemstack != null) {
	            // We always consume bonemeal when trying to grow
	            --itemstack.count;
	        }
	        // No need to generate the tree again.
	        if (!grownTree) {
	            // CraftBukkit end
	            if (flag) {
	                world.setTypeAndData(i + i1, j, k + j1, sapling, type, 4);
	                world.setTypeAndData(i + i1 + 1, j, k + j1, sapling, type, 4);
	                world.setTypeAndData(i + i1, j, k + j1 + 1, sapling, type, 4);
	                world.setTypeAndData(i + i1 + 1, j, k + j1 + 1, sapling, type, 4);
	            } else {
	                world.setTypeAndData(i, j, k, sapling, type, 4);
	            }
	        }
	    }
}
