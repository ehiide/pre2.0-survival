package mc.server.survival.managers;

import mc.server.survival.files.Configuration;
import mc.server.survival.files.Main;
import mc.server.survival.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class ATManager
{
    /*
        Synonymous for more efficient understanding.
     */

    private final Synonymous zmien = new Synonymous("zmien", new String[]{"zmien", "ustaw", "nastaw", "zrob", "przelacz"});
    private final Synonymous usun = new Synonymous("usun", new String[]{"usun", "wyczysc", "wylacz", "zresetuj", "resetuj", "wyjeb", "zabierz"});
    private final Synonymous daj = new Synonymous("daj", new String[]{"daj", "dasz", "podaruj", "podarowac", "dorecz", "doreczyc", "dodac", "dodaj", "podaj"});
    private final Synonymous wlacz = new Synonymous("wlacz", new String[]{"wlacz", "otworz", "odblokuj", "odblokowac", "start", "odpal", "pozwol"});
    private final Synonymous wylacz = new Synonymous("wylacz", new String[]{"wylacz", "zamknij", "zablokuj", "zablokowac", "stop"});
    private final Synonymous co_to = new Synonymous("co to", new String[]{"co to", "co to jest", "kto to", "kto to jest", "czym jest", "kim jest"});

    static class Synonymous
    {
        String library;
        String[] dictionary;

        public Synonymous(String library, String[] dictionary)
        {

            this.library = library;
            this. dictionary = dictionary;
        }

        public String getLibrary()
        {
            return library;
        }

        public String[] getDictionary()
        {
            return dictionary;
        }

        public boolean matchSynonymous(String string)
        {
            for (String synonym : dictionary)
            {
                if (ATManager.stringContain(string, synonym))
                    return true;
            }

            return false;
        }

        public boolean matchSynonymousRoughly(String string)
        {
            for (String synonym : dictionary)
            {
                if (string.contains(synonym))
                    return true;
            }

            return false;
        }
    }

    /*
        Main AT prefix, whose displays on the chat.
     */

    public static final String prefix = "&eANTI-TOXIC &f";

    /*
        AT settings.
     */

    public static double AT_WORD_HEURISTIC_TRESHOLD = 0.8;
    public static boolean AT_ALWAYS_LISTENER = false;

    /*
        These letters will be removed from the message sended by a player.
        This is super usefull to detect advanved word re-translation.
     */

    private static final String[] uselessChars = {" ", ".", ",", "/", ":", ";", "'", "|", "~", "=", "+", "-", "!", "_", "?", "@", "#",
                                                  "$", "%", "^", "&", "*", "(", ")", "{", "}", "[", "]", "1", "2", "3", "4", "5", "6",
                                                  "7", "8", "9", "0"};

    /*
        Function that changes unknown letters to alphanumerical ones.
    */

    private static final HashMap<String, String> unknownChar = new HashMap<String, String>();
    private static final ArrayList<String> unknownCharsList = new ArrayList<>();
    private static final ArrayList<String> knownCharsList = new ArrayList<>();
    private static final String[] unknownChars = {"ą", "ć", "ę", "ł", "ń", "ó", "ś", "€", "ź", "ż"};
    private static final String[] knownChars = {"a", "c", "e", "l", "n", "o", "s", "u", "z", "z"};

    private static void collectUnknownChars()
    {
        Collections.addAll(unknownCharsList, unknownChars);
        Collections.addAll(knownCharsList, knownChars);
        int currentChar = 0;

        for (String unknown : unknownChars)
        {
            unknownChar.put(unknownCharsList.get(currentChar), knownCharsList.get(currentChar));
            currentChar = currentChar + 1;
        }
    }

    public static HashMap<Player, Integer> at_warns = new HashMap<Player, Integer>();
    public static HashMap<Player, Boolean> at_listener = new HashMap<Player, Boolean>();

    public static int getWarns(Player player) { return at_warns.get(player); }
    public static void setWarns(Player player, int warns) { at_warns.put(player, warns); }
    public static boolean checkWarns(Player player) { return at_warns.get(player) == null; }
    public static boolean getListener(Player player) { return at_listener.get(player); }
    public static void setListener(Player player, boolean listener) { at_listener.put(player, listener); }
    public static boolean checkListener(Player player) { return at_listener.get(player) == null; }

    /*
        Listener analyzer.
     */

    private void analyze(Player player, String message)
    {
        if (zmien.matchSynonymous(message) && stringContain(message, "poranek", "ranek", "rano", "wschod", "poczatek dnia"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    player.getWorld().setTime(1000);
                }
            }.runTask(Main.getInstance());
            say(player, "Ustawiono poranek!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (zmien.matchSynonymous(message) && stringContain(message, "dzien", "jasno", "jasniej", "slonce", "sloneczne", "slonecznie") && !stringContain(message, "zachod slonca"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                { player.getWorld().setTime(6000); if (!player.getWorld().isClearWeather()) player.getWorld().setWeatherDuration(1);
                }
            }.runTask(Main.getInstance());
            say(player, "Ustawiono dzien!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (zmien.matchSynonymous(message) && stringContain(message, "zmierzch", "zachod", "zacmienie", "koniec dnia"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    player.getWorld().setTime(12000);
                }
            }.runTask(Main.getInstance());
            say(player, "Ustawiono zachod!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (zmien.matchSynonymous(message) && stringContain(message, "noc", "ciemno", "ciemniej", "ksiezyc"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    player.getWorld().setTime(18000);
                }
            }.runTask(Main.getInstance());
            say(player, "Ustawiono noc!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (zmien.matchSynonymous(message) && stringContain(message, "deszcz"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    player.getWorld().setClearWeatherDuration(1);
                }
            }.runTask(Main.getInstance());
            say(player, "Ustawiono deszcz!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (zmien.matchSynonymous(message) && stringContain(message, "pogode"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    player.getWorld().setWeatherDuration(1);
                }
            }.runTask(Main.getInstance());
            say(player, "Ustawiono sloneczna pogode!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (usun.matchSynonymous(message) && stringContain(message, "deszcz", "pogode"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    player.getWorld().setClearWeatherDuration(1);
                }
            }.runTask(Main.getInstance());
            say(player, "Usunieto deszcz!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (usun.matchSynonymous(message) && stringContain(message, "slonce", "sloneczne", "slonecznie"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    player.getWorld().setClearWeatherDuration(1);
                }
            }.runTask(Main.getInstance());
            say(player, "Usunieto sloneczna pogode!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (usun.matchSynonymous(message) && stringContain(message, "blok"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    player.getTargetBlock(null, 16).breakNaturally();
                }
            }.runTask(Main.getInstance());
            say(player, "Usunieto wybrany blok!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (daj.matchSynonymous(message) && stringContain(message, "liczba", "numer", "cyfra"))
        {
            say(player, "Niech bedzie " + new Random().nextInt(100) + "!", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (wlacz.matchSynonymousRoughly(message) && stringContain(message, "nether", "podziemia", "pieklo"))
        {
            if (!Configuration.SERVER_BLOCK_NETHER)
                say(player, "Wrota do netheru sa juz otwarte!", false);
            else
            {
                Configuration.SERVER_BLOCK_NETHER = false;
                say(player, "Wrota do netheru zostaly otwarte!", false);
            }

            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (wylacz.matchSynonymousRoughly(message) && stringContain(message, "nether", "podziemia", "pieklo"))
        {
            if (Configuration.SERVER_BLOCK_NETHER)
                say(player, "Wrota do netheru sa juz zamkniete!", false);
            else
            {
                Configuration.SERVER_BLOCK_NETHER = true;
                say(player, "Wrota do netheru zostaly zamkniete!", false);
            }
        }

        else if (wlacz.matchSynonymousRoughly(message) && stringContain(message, "end", "kres", "swiat kresu"))
        {
            if (!Configuration.SERVER_BLOCK_THE_END)
                say(player, "Wrota do endu sa juz otwarte!", false);
            else
                {
                Configuration.SERVER_BLOCK_THE_END = false;
                say(player, "Wrota do endu zostaly otwarte!", false);
            }

            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (wylacz.matchSynonymousRoughly(message) && stringContain(message, "end", "kres", "swiat kresu"))
        {
            if (Configuration.SERVER_BLOCK_THE_END)
                say(player, "Wrota do endu sa juz zamkniete!", false);
            else
                {
                Configuration.SERVER_BLOCK_THE_END = true;
                say(player, "Wrota do endu zostaly zamkniete!", false);
            }

            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (wlacz.matchSynonymousRoughly(message) && stringContain(message, "niszczenie", "niszczyc", "usuwanie"))
        {
            if (!Configuration.SERVER_TERRAIN_PROTECTION)
                say(player, "Blokowanie modyfikacji terenu jest juz wylaczone!", false);
            else
            {
                Configuration.SERVER_TERRAIN_PROTECTION = false;
                say(player, "Blokowanie modyfikacji terenu zostalo wylaczone!", false);
            }

            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (wylacz.matchSynonymousRoughly(message) && stringContain(message, "niszczenie", "niszczyc", "usuwanie"))
        {
            if (Configuration.SERVER_TERRAIN_PROTECTION)
                say(player, "Blokowanie modyfikacji terenu jest juz wlaczone!", false);
            else
            {
                Configuration.SERVER_TERRAIN_PROTECTION = true;
                say(player, "Blokowanie modyfikacji terenu zostalo wlaczone!", false);
            }

            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (wlacz.matchSynonymousRoughly(message) && stringContain(message, "ochrone", "protekcje", "spawn", "lobby", "blokowanie"))
        {
            if (!Configuration.SERVER_TERRAIN_PROTECTION)
                say(player, "Blokowanie modyfikacji terenu jest juz wylaczone!", false);
            else
            {
                Configuration.SERVER_TERRAIN_PROTECTION = false;
                say(player, "Blokowanie modyfikacji terenu zostalo wylaczone!", false);
            }

            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (wylacz.matchSynonymousRoughly(message) && stringContain(message, "ochrone", "protekcje", "spawn", "lobby", "blokowanie"))
        {
            if (Configuration.SERVER_TERRAIN_PROTECTION)
                say(player, "Blokowanie modyfikacji terenu jest juz wlaczone!", false);
            else
            {
                Configuration.SERVER_TERRAIN_PROTECTION = true;
                say(player, "Blokowanie modyfikacji terenu zostalo wlaczone!", false);
            }

            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (stringContain(message, "zabezpiecz") && stringContain(message, "spawn", "lobby", "hub", "teren"))
        {
            if (Configuration.SERVER_TERRAIN_PROTECTION)
                say(player, "Blokowanie modyfikacji terenu jest juz wlaczone!", false);
            else
            {
                Configuration.SERVER_TERRAIN_PROTECTION = true;
                say(player, "Blokowanie modyfikacji terenu zostalo wlaczone!", false);
            }

            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (co_to.matchSynonymous(message))
        {
            if (stringContain(message, "jest"))
                say(player, getDefinitionOf(message.substring(message.indexOf("jest") + 4)), false);

            else if (stringContain(message, "co to"))
                say(player, getDefinitionOf(message.substring(message.indexOf("to") + 2)), false);

            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else if (stringContain(message, "wytrzezwiej mnie", "wytrzezwiej", "trzezwy", "uratuj", "na kaca", "kaca"))
        {
            new BukkitRunnable() {@Override
                public void run() {
                    NeuroManager.set(player, 0, 0, 0, 0);
                }
            }.runTask(Main.getInstance());
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }

        else
        {
            say(player, "Nie rozumiem.", false);
            if (!AT_ALWAYS_LISTENER) setListener(player, false);
        }
    }

    /*
        Main listener for all messages on the chat.
     */

    public void check(Player player, String message)
    {
        // Lower-casing for optimal results.

        message = message.toLowerCase();

        /*
            Detector for toxic words on the chat.
         */

        collectUnknownChars();

        for (String unknown : unknownCharsList)
            message = message.replace(unknown, unknownChar.get(unknown));

        for (String uselessChar : uselessChars)
            message = message.replace(uselessChar, "");

        ArrayList<Integer> letters = new ArrayList<>();

        if (message.length() >= 3)
            for (int letter = 1; letter <= message.length(); letter++)
                if (letter > 1 && letter != message.length())
                    if (message.substring(letter - 1, letter).equalsIgnoreCase(message.substring(letter, letter + 1)))
                        letters.add(letter);

        if (letters.size() > 0)
            for (int slot : letters)
            {
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(message);
                stringBuilder.setCharAt(slot, '<');
                message = stringBuilder.toString();
            }

        message = message.replace("<", "");
        message = message.replace(">", "");

        if (!checkListener(player) && getListener(player))
            analyze(player, message);

        if (stringContain(message, "antitoxic", "antytoxic"))
            if (DPlayerManager.getInstance().getRank(player).equalsIgnoreCase("administrator"))
            {
                ArrayList<String> strings = new ArrayList<>();
                strings.add(player.getName() + ", w czym moge ci pomoc?");
                strings.add(player.getName() + ", slucham.");
                strings.add(player.getName() + ", tak?");
                strings.add(player.getName() + ", do uslug.");
                strings.add(player.getName() + ", tak panie?");
                say(player, strings, false);
                setListener(player, true);
            }
            else
                say(player, player.getName() + ", odpierdol sie ode mnie!", false);
    }

    public static void say(Player player, String message, boolean warn)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.broadcastMessage(ColorUtil.formatHEX("#80ff1f[" + TimeUtil.hour() + ":" + TimeUtil.minute() + "#80ff1f] " + prefix + message));
            }
        }.runTaskLater(Main.getInstance(), 20);
    }

    public static void say(Player player, ArrayList<String> message, boolean warn)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.broadcastMessage(ColorUtil.formatHEX("#80ff1f[" + TimeUtil.hour() + ":" + TimeUtil.minute() + "#80ff1f] " + prefix + ChatUtil.randomString(message)));
            }
        }.runTaskLater(Main.getInstance(), 20);
    }

    public static String getDefinitionOf(String word)
    {
        try
        {
            URL url = new URL("https://sjp.pwn.pl/szukaj/" + word + ".html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = reader.readLine()) != null)
                result.append(line);

            String h = result.toString();

            for (int x = 0; x < h.length(); x++)
                if (h.substring(x, x + 1).equalsIgnoreCase("«"))
                    for (int z = x; z < h.length(); z++)
                        if (h.substring(z, z + 1).equalsIgnoreCase("»"))
                            return "Jest to " + h.substring(x + 1, z) + ".";
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "Nie znam takiego slowa.";
    }

    /*
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */

    public static double similarity(String s1, String s2)
    {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length())
        { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    public static int editDistance(String s1, String s2)
    {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++)
        {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++)
            {
                if (i == 0)
                    costs[j] = j;
                else
                    {
                    if (j > 0)
                    {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static boolean stringContain(String string, String... words)
    {
        if (string.length() <= 4) return false;

        for (int x = 0; x <= string.length(); x++)
        {
            for (int z = 1; z <= string.length(); z++)
            {
                if (!(z <= x))
                {
                    for (String word : words)
                    {
                        if (similarity(string.substring(x, z), word) >= AT_WORD_HEURISTIC_TRESHOLD)
                            return true;
                    }
                }
            }
        }

        return false;
    }
}