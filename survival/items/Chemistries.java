package mc.server.survival.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Chemistries
{
    private Chemistries() {}

    static Chemistries instance = new Chemistries();

    public static Chemistries getInstance()
    {
        return instance;
    }

    public static ChemistryItem opium = new ChemistryItem("Opium", new String[]{""}, Material.GUNPOWDER, new Affinity(404));

    // from opium
    public static ChemistryItem alprazolam = new ChemistryItem("Alprazolam", new String[]{""}, Material.GUNPOWDER, new Affinity(10));
    public static ChemistryItem metylomorfina = new ChemistryItem("Metylomorfina", new String[]{""}, Material.GUNPOWDER, new Affinity(20));
    public static ChemistryItem morfina = new ChemistryItem("Morfina", new String[]{""}, Material.GUNPOWDER, new Affinity(40));
    public static ChemistryItem heroina = new ChemistryItem("Heroina", new String[]{""}, Material.GUNPOWDER, new Affinity(90));
    public static ChemistryItem fentanyl = new ChemistryItem("Fentanyl", new String[]{""}, Material.GUNPOWDER, new Affinity(150));

    public static ChemistryItem amina = new ChemistryItem("Amina", new String[]{""}, Material.SUGAR, new Affinity(404, 0, 0, 0));

    // from amina
    public static ChemistryItem metyloamina = new ChemistryItem("Metyloamina", new String[]{""}, Material.SUGAR, new Affinity(404, 404, 404, 404));
    public static ChemistryItem metylenoamina = new ChemistryItem("Metylenoamina", new String[]{""}, Material.SUGAR, new Affinity(404, 404, 404, 404));
    public static ChemistryItem fenyloamina = new ChemistryItem("Fenyloamina", new String[]{""}, Material.SUGAR, new Affinity(404, 404, 404, 404));
    public static ChemistryItem fluoroamina = new ChemistryItem("Fluoroamina", new String[]{""}, Material.SUGAR, new Affinity(404, 404, 404, 404));
    public static ChemistryItem dimetoamina = new ChemistryItem("Dimetoamina", new String[]{""}, Material.SUGAR, new Affinity(404, 404, 404, 404));

    // from metyloamina
    public static ChemistryItem metamfetamina = new ChemistryItem("Metamfetamina", new String[]{""}, Material.SUGAR, new Affinity(25, 20, 15, -8));
    public static ChemistryItem metafedron = new ChemistryItem("Metafedron", new String[]{""}, Material.SUGAR, new Affinity(13, 31, 9, -2));
    public static ChemistryItem metylon = new ChemistryItem("Metylon", new String[]{""}, Material.SUGAR, new Affinity(17, 16, 14, -9));
    public static ChemistryItem metylometkatynon = new ChemistryItem("Metylometkatynon", new String[]{""}, Material.SUGAR, new Affinity(21, 12, -7, -2));

    // from metylenoamina
    public static ChemistryItem MDA = new ChemistryItem("MDA", new String[]{""}, Material.SUGAR, new Affinity(4, 14, 37, -5));
    public static ChemistryItem MDMA = new ChemistryItem("MDMA", new String[]{" &8> &7Opis: MDMA jest najsilniejsza substancja euforyczna", "   &7na serwerze."}, Material.SUGAR, new Affinity(55, 35, 20, 0));
    public static ChemistryItem MDPV = new ChemistryItem("MDPV", new String[]{""}, Material.SUGAR, new Affinity(-2, 9, 42, -20));

    // from fenyloamina
    public static ChemistryItem amfetamina = new ChemistryItem("Amfetamina", new String[]{""}, Material.SUGAR, new Affinity(6, 15, 25, -7));
    public static ChemistryItem mefedron = new ChemistryItem("Mefedron", new String[]{""}, Material.SUGAR, new Affinity(25, 24, 23, 4));
    public static ChemistryItem klefedron = new ChemistryItem("Klefedron", new String[]{""}, Material.SUGAR, new Affinity(21, 22, 15, 10));

    // from fluoroamina
    public static ChemistryItem fluoroamfetamina = new ChemistryItem("Fluoroamfetamina", new String[]{""}, Material.SUGAR, new Affinity(16, 23, 17, 0));
    public static ChemistryItem flefedron = new ChemistryItem("Flefedron", new String[]{""}, Material.SUGAR, new Affinity(8, 15, 14, -12));

    // from dimetoamina
    public static ChemistryItem kokaina = new ChemistryItem("Kokaina", new String[]{""}, Material.SUGAR, new Affinity(12, 50, 25, -5));
    public static ChemistryItem kleksedron = new ChemistryItem("Kleksedron", new String[]{""}, Material.SUGAR, new Affinity(10, 17, -11, 4));

    public ArrayList<ChemistryItem> getChemistries()
    {
        ArrayList<ChemistryItem> chemistries = new ArrayList<>();
        Field[] fields = this.getClass().getFields();

        for (Field field : fields)
            if (field.getType() == ChemistryItem.class)
            {
                field.setAccessible(true);

                try
                {
                    ChemistryItem chemistryItem = (ChemistryItem) field.get(Chemistries.class);
                    chemistries.add(chemistryItem);
                }
                catch (IllegalAccessException ignored)
                { }
            }

        return chemistries;
    }

    public ChemistryItem byName(String name)
    {
        for (ChemistryItem chemistryItem : getChemistries())
            if (chemistryItem.getName().equalsIgnoreCase(name))
                return chemistryItem; return null;
    }

    public boolean isKnown(ItemStack item)
    {
        for (ChemistryItem chemistryItem : getChemistries())
            if (ChemistryDrug.getDrug(chemistryItem).isSimilar(item))
                return true; return false;
    }
}