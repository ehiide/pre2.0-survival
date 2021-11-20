package mc.server.survival.items;

import org.bukkit.Material;

public class ChemistryItem
{
    private String name;
    private String[] lore;
    private Material type;
    private Affinity affinity;

    public ChemistryItem(String name, String[] lore, Material type, Affinity affinity)
    {
        this.name = name;
        this.lore = lore;
        this.type = type;
        this.affinity = affinity;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String[] getLore() { return lore; }

    public void setLore(String[] lore) { this.lore = lore; }

    public Material getType() { return type; }

    public void setType(Material type) { this.type = type; }

    public Affinity getAffinity() { return affinity; }

    public void setAffinity(Affinity affinity) { this.affinity = affinity; }

    class ExampleDrug
    {
        public ChemistryItem amfetamina = new ChemistryItem("Amfetamina", new String[]{"Chemistry 3", "Example drug"}, Material.SUGAR, new Affinity(6, 15, 25, -7));
    }
}