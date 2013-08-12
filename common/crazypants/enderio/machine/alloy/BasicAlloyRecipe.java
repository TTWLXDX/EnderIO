package crazypants.enderio.machine.alloy;

import crazypants.enderio.ModObject;
import crazypants.enderio.machine.*;
import net.minecraft.item.ItemStack;

public class BasicAlloyRecipe implements IMachineRecipe {

  public static final int DEFAULT_ENERGY_USE = 1600;
  
  private final String uid;
  
  private float energyRequired = DEFAULT_ENERGY_USE;
  
  private ItemStack[] inputs;
  
  private ItemStack output;
  
  public BasicAlloyRecipe(ItemStack output, String uid, ItemStack... recipeInputs) {
    this.output = output.copy();
    this.uid = uid;
    inputs = new ItemStack[recipeInputs.length];
    for(int i=0;i<inputs.length;i++) {
      inputs[i] = recipeInputs[i].copy();
    }
  }
  
  @Override
  public String getUid() {
    return uid;
  }

  @Override
  public float getEnergyRequired(RecipeInput... inputs) {
    return energyRequired;
  }

  @Override
  public boolean isRecipe(RecipeInput... checking) {
    checking = getNonNullInputs(checking);
    if(inputs.length != checking.length) {
      return false;
    }
    for(RecipeInput input : checking) {
      ItemStack ing = getIngrediantForInput(input.item);
      if(ing == null || ing.stackSize > input.item.stackSize) {
        return false;
      }
    }
    return true;
  }

  private RecipeInput[] getNonNullInputs(RecipeInput[] checking) {
    int numNonNulls = 0;
    for(int i=0;i<checking.length;i++) {
      if(checking[i] != null && checking[i].item != null) {
        numNonNulls++;
      }
    }
    RecipeInput[] result = new RecipeInput[numNonNulls];
    int index = 0;
    for(int i=0;i<checking.length;i++) {
      if(checking[i] != null && checking[i].item != null) {
        result[index] = checking[i];
        index++;
      }
    }
    return result;
  }

  @Override
  public ItemStack[] getCompletedResult(RecipeInput... inputs) {
    return new ItemStack[] {output.copy()};
  }

  @Override
  public boolean isValidInput(int slotNumber, ItemStack item) {
    return getIngrediantForInput(item) != null;    
  }

  @Override
  public String getMachineName() {
    return ModObject.blockAlloySmelter.unlocalisedName;
  }

  @Override
  public int getQuantityConsumed(RecipeInput input) {
    ItemStack ing = getIngrediantForInput(input.item);
    return ing == null ? 0 : ing.stackSize;
  }
  
  private ItemStack getIngrediantForInput(ItemStack input) {
    if(input == null) {
      return null;
    }
    for(ItemStack st : inputs) {
      if(st != null && st.itemID == input.itemID) {
        return st;
      }
    }
    return null;
  }

}