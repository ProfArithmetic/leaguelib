package com.achimala.leaguelib.tests;

import com.achimala.leaguelib.connection.LeagueAccount;
import com.achimala.leaguelib.connection.LeagueConnection;
import com.achimala.leaguelib.connection.LeagueServer;
import com.achimala.leaguelib.errors.LeagueException;
import com.achimala.leaguelib.models.LeagueChampion;
import com.achimala.leaguelib.models.LeagueSummoner;
import com.achimala.leaguelib.models.MatchHistoryEntry;
import com.achimala.util.Callback;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.*;

import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;

import java.sql.*;


public class WriteNewGames {
    // initialize stuff for leagueclient
    private static int count = 0;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition done = lock.newCondition();

    private static void incrementCount() {
        lock.lock();
        count++;
        lock.unlock();
    }

    private static void decrementCount() {
        lock.lock();
        count--;
        if(count == 0)
            done.signal();
        lock.unlock();
    }
    public static void main(String[] args) throws IOException, LeagueException, InterruptedException, SQLException, ClassNotFoundException{
        final Map<String, String> amap = new HashMap<String,String>();
        amap.put("magic_damage_dealt_player", "magic_damage_dealt");
        amap.put("largest_killing_spree" , "killing_spree");
        amap.put("num_deaths" , "deaths");
        amap.put("champions_killed" , "kills");
        amap.put("turrets_killed" , "turrets_destroyed");
        amap.put("physical_damage_dealt_player" , "physical_damage_dealt");
        amap.put("gold_earned", "gold");
        amap.put("minions_killed" , "minions");
        final Map<String, String> imap = new HashMap<String, String>();
        imap.put("3001" , "Abyssal Scepter");
        imap.put("3105" , "Aegis of the Legion");
        imap.put("1052" , "Amplifying Tome");
        imap.put("3301" , "Ancient Coin");
        imap.put("3003" , "Archangel's Staff");
        imap.put("3007" , "Archangel's Staff (Crystal Scar)");
        imap.put("3174" , "Athene's Unholy Grail");
        imap.put("3005" , "Atma's Impaler");
        imap.put("3198" , "Augment: Death");
        imap.put("3197" , "Augment: Gravity");
        imap.put("3196" , "Augment: Power");
        imap.put("3093" , "Avarice Blade");
        imap.put("1038" , "B. F. Sword");
        imap.put("3060" , "Banner of Command");
        imap.put("3102" , "Banshee's Veil");
        imap.put("3006" , "Berserker's Greaves");
        imap.put("3144" , "Bilgewater Cutlass");
        imap.put("3188" , "Blackfire Torch");
        imap.put("3153" , "Blade of the Ruined King");
        imap.put("1026" , "Blasting Wand");
        imap.put("3166" , "Bonetooth Necklace");
        imap.put("3167" , "Bonetooth Necklace");
        imap.put("3168" , "Bonetooth Necklace");
        imap.put("3169" , "Bonetooth Necklace");
        imap.put("3171" , "Bonetooth Necklace");
        imap.put("3117" , "Boots of Mobility");
        imap.put("1001" , "Boots of Speed");
        imap.put("3009" , "Boots of Swiftness");
        imap.put("1051" , "Brawler's Gloves");
        imap.put("3010" , "Catalyst the Protector");
        imap.put("1031" , "Chain Vest");
        imap.put("3028" , "Chalice of Harmony");
        imap.put("1018" , "Cloak of Agility");
        imap.put("1029" , "Cloth Armor");
        imap.put("2041" , "Crystalline Flask");
        imap.put("1042" , "Dagger");
        imap.put("3128" , "Deathfire Grasp");
        imap.put("1055" , "Doran's Blade");
        imap.put("1056" , "Doran's Ring");
        imap.put("1054" , "Doran's Shield");
        imap.put("2039" , "Elixir of Brilliance");
        imap.put("2037" , "Elixir of Fortitude");
        imap.put("3097" , "Emblem of Valor");
        imap.put("3254" , "Enchantment: Alacrity");
        imap.put("3259" , "Enchantment: Alacrity");
        imap.put("3264" , "Enchantment: Alacrity");
        imap.put("3269" , "Enchantment: Alacrity");
        imap.put("3274" , "Enchantment: Alacrity");
        imap.put("3279" , "Enchantment: Alacrity");
        imap.put("3284" , "Enchantment: Alacrity");
        imap.put("3251" , "Enchantment: Captain");
        imap.put("3256" , "Enchantment: Captain");
        imap.put("3261" , "Enchantment: Captain");
        imap.put("3266" , "Enchantment: Captain");
        imap.put("3271" , "Enchantment: Captain");
        imap.put("3276" , "Enchantment: Captain");
        imap.put("3281" , "Enchantment: Captain");
        imap.put("3253" , "Enchantment: Distortion");
        imap.put("3258" , "Enchantment: Distortion");
        imap.put("3263" , "Enchantment: Distortion");
        imap.put("3268" , "Enchantment: Distortion");
        imap.put("3273" , "Enchantment: Distortion");
        imap.put("3278" , "Enchantment: Distortion");
        imap.put("3283" , "Enchantment: Distortion");
        imap.put("3252" , "Enchantment: Furor");
        imap.put("3257" , "Enchantment: Furor");
        imap.put("3262" , "Enchantment: Furor");
        imap.put("3267" , "Enchantment: Furor");
        imap.put("3272" , "Enchantment: Furor");
        imap.put("3277" , "Enchantment: Furor");
        imap.put("3282" , "Enchantment: Furor");
        imap.put("3250" , "Enchantment: Homeguard");
        imap.put("3255" , "Enchantment: Homeguard");
        imap.put("3260" , "Enchantment: Homeguard");
        imap.put("3265" , "Enchantment: Homeguard");
        imap.put("3270" , "Enchantment: Homeguard");
        imap.put("3275" , "Enchantment: Homeguard");
        imap.put("3280" , "Enchantment: Homeguard");
        imap.put("3184" , "Entropy");
        imap.put("3123" , "Executioner's Calling");
        imap.put("2050" , "Explorer's Ward");
        imap.put("3401" , "Face of the Mountain");
        imap.put("1004" , "Faerie Charm");
        imap.put("3363" , "Farsight Orb");
        imap.put("3108" , "Fiendish Codex");
        imap.put("3092" , "Frost Queen's Claim");
        imap.put("3098" , "Frostfang");
        imap.put("3110" , "Frozen Heart");
        imap.put("3022" , "Frozen Mallet");
        imap.put("1011" , "Giant's Belt");
        imap.put("3024" , "Glacial Shroud");
        imap.put("3351" , "Greater Lens");
        imap.put("3352" , "Greater Orb");
        imap.put("3361" , "Greater Stealth Totem");
        imap.put("3350" , "Greater Totem");
        imap.put("3362" , "Greater Vision Totem");
        imap.put("3159" , "Grez's Spectral Lantern");
        imap.put("3026" , "Guardian Angel");
        imap.put("2051" , "Guardian's Horn");
        imap.put("3124" , "Guinsoo's Rageblade");
        imap.put("3136" , "Haunting Guise");
        imap.put("3175" , "Head of Kha'Zix");
        imap.put("2003" , "Health Potion");
        imap.put("3155" , "Hexdrinker");
        imap.put("3146" , "Hextech Gunblade");
        imap.put("3145" , "Hextech Revolver");
        imap.put("3187" , "Hextech Sweeper");
        imap.put("1039" , "Hunter's Machete");
        imap.put("3025" , "Iceborn Gauntlet");
        imap.put("2048" , "Ichor of Illumination");
        imap.put("2040" , "Ichor of Rage");
        imap.put("3031" , "Infinity Edge");
        imap.put("3158" , "Ionian Boots of Lucidity");
        imap.put("3067" , "Kindlegem");
        imap.put("3186" , "Kitae's Bloodrazor");
        imap.put("3035" , "Last Whisper");
        imap.put("3151" , "Liandry's Torment");
        imap.put("3100" , "Lich Bane");
        imap.put("3190" , "Locket of the Iron Solari");
        imap.put("1036" , "Long Sword");
        imap.put("3106" , "Madred's Razors");
        imap.put("2004" , "Mana Potion");
        imap.put("3004" , "Manamune");
        imap.put("3008" , "Manamune (Crystal Scar)");
        imap.put("3156" , "Maw of Malmortius");
        imap.put("3041" , "Mejai's Soulstealer");
        imap.put("3139" , "Mercurial Scimitar");
        imap.put("3111" , "Mercury's Treads");
        imap.put("3222" , "Mikael's Crucible");
        imap.put("3170" , "Moonflair Spellblade");
        imap.put("3165" , "Morellonomicon");
        imap.put("3042" , "Muramana");
        imap.put("3043" , "Muramana");
        imap.put("3115" , "Nashor's Tooth");
        imap.put("1058" , "Needlessly Large Rod");
        imap.put("1057" , "Negatron Cloak");
        imap.put("3047" , "Ninja Tabi");
        imap.put("3096" , "Nomad's Medallion");
        imap.put("1033" , "Null-Magic Mantle");
        imap.put("3180" , "Odyn's Veil");
        imap.put("3056" , "Ohmwrecker");
        imap.put("2047" , "Oracle's Extract");
        imap.put("3364" , "Oracle's Lens");
        imap.put("3112" , "Orb of Winter");
        imap.put("3084" , "Overlord's Bloodmail");
        imap.put("3044" , "Phage");
        imap.put("3046" , "Phantom Dancer");
        imap.put("1037" , "Pickaxe");
        imap.put("2052" , "Poro-Snax");
        imap.put("1062" , "Prospector's Blade");
        imap.put("1063" , "Prospector's Ring");
        imap.put("3140" , "Quicksilver Sash");
        imap.put("3089" , "Rabadon's Deathcap");
        imap.put("3143" , "Randuin's Omen");
        imap.put("3074" , "Ravenous Hydra (Melee Only)");
        imap.put("1043" , "Recurve Bow");
        imap.put("1006" , "Rejuvenation Bead");
        imap.put("3302" , "Relic Shield");
        imap.put("3027" , "Rod of Ages");
        imap.put("3029" , "Rod of Ages (Crystal Scar)");
        imap.put("1028" , "Ruby Crystal");
        imap.put("2045" , "Ruby Sightstone");
        imap.put("3085" , "Runaan's Hurricane (Ranged Only)");
        imap.put("3116" , "Rylai's Crystal Scepter");
        imap.put("3181" , "Sanguine Blade");
        imap.put("1027" , "Sapphire Crystal");
        imap.put("3342" , "Scrying Orb");
        imap.put("3191" , "Seeker's Armguard");
        imap.put("3040" , "Seraph's Embrace");
        imap.put("3048" , "Seraph's Embrace");
        imap.put("3057" , "Sheen");
        imap.put("2049" , "Sightstone");
        imap.put("3020" , "Sorcerer's Shoes");
        imap.put("3211" , "Spectre's Cowl");
        imap.put("3303" , "Spellthief's Edge");
        imap.put("1080" , "Spirit Stone");
        imap.put("3065" , "Spirit Visage");
        imap.put("3207" , "Spirit of the Ancient Golem");
        imap.put("3209" , "Spirit of the Elder Lizard");
        imap.put("3206" , "Spirit of the Spectral Wraith");
        imap.put("3087" , "Statikk Shiv");
        imap.put("2044" , "Stealth Ward");
        imap.put("3101" , "Stinger");
        imap.put("3068" , "Sunfire Cape");
        imap.put("3341" , "Sweeping Lens");
        imap.put("3131" , "Sword of the Divine");
        imap.put("3141" , "Sword of the Occult");
        imap.put("3069" , "Talisman of Ascension");
        imap.put("3070" , "Tear of the Goddess");
        imap.put("3073" , "Tear of the Goddess (Crystal Scar)");
        imap.put("3071" , "The Black Cleaver");
        imap.put("3072" , "The Bloodthirster");
        imap.put("3134" , "The Brutalizer");
        imap.put("3200" , "The Hex Core");
        imap.put("3185" , "The Lightbringer");
        imap.put("3075" , "Thornmail");
        imap.put("3077" , "Tiamat (Melee Only)");
        imap.put("2009" , "Total Biscuit of Rejuvenation");
        imap.put("2010" , "Total Biscuit of Rejuvenation");
        imap.put("3078" , "Trinity Force");
        imap.put("3023" , "Twin Shadows");
        imap.put("3290" , "Twin Shadows");
        imap.put("1053" , "Vampiric Scepter");
        imap.put("2043" , "Vision Ward");
        imap.put("3135" , "Void Staff");
        imap.put("3082" , "Warden's Mail");
        imap.put("3340" , "Warding Totem");
        imap.put("3083" , "Warmog's Armor");
        imap.put("3152" , "Will of the Ancients");
        imap.put("3091" , "Wit's End");
        imap.put("3090" , "Wooglet's Witchcap");
        imap.put("3154" , "Wriggle's Lantern");
        imap.put("3142" , "Youmuu's Ghostblade");
        imap.put("3086" , "Zeal");
        imap.put("3050" , "Zeke's Herald");
        imap.put("3172" , "Zephyr");
        imap.put("3157" , "Zhonya's Hourglass");

        // Establish our connection to database
        try{ Class.forName("com.mysql.jdbc.Driver").newInstance(); } catch (Exception e) {}
        final Connection conn = DriverManager.getConnection
                ("jdbc:mysql://localhost:3306/log_development", "root", "mypass");

        // First Grab Player Config from file and get the important information
        // WILL BE REPLACED WHEN PUSHED LIVE
        JsonParser parser = new JsonParser();
        JsonElement player_conf = parser.parse(Files.toString(new File(args[0]), Charsets.UTF_8));
        String player_c = player_conf.toString();
        String[] player_split = player_c.split(",");
        // We want to process each of these players individually so we should store them in a player array
        // each player is saved in the players array with all the info we need to save the info
        String[] players = new String[player_split.length];
        for(int i=0;i<player_split.length; i++){
            // strip out any extra nonsense that was generated
            player_split[i] = player_split[i].replace("{", "").replace("}", "").replace("\"", "");
            players[i] = player_split[i];
        }
        // Second we want to initialize our client
        final LeagueConnection c = new LeagueConnection(LeagueServer.NORTH_AMERICA);
        c.getAccountQueue().addAccount(new LeagueAccount(LeagueServer.NORTH_AMERICA, "3.14.xx", "pollinguser", args[1]));
        Map<LeagueAccount, LeagueException> exceptions = c.getAccountQueue().connectAll();
        if(exceptions != null) {
            for(LeagueAccount account : exceptions.keySet())
                System.out.println(account + " error: " + exceptions.get(account));
            return;
        }
        lock.lock();
        incrementCount();

        Boolean loop = true;
        while(loop == true){
            // we want to iterate over our player list
            // for now we will use just 1 player
            for(int i=0;i<players.length;i++){
                final String[] player_info = players[i].split(":");
                c.getSummonerService().getSummonerByName(player_info[1], new Callback<LeagueSummoner>() {
                public void onCompletion(LeagueSummoner summoner) {
                    final File f = new File(player_info[1]);
                    if (f.exists()) {
                        // we can continue & find last math
                        c.getPlayerStatsService().fillMatchHistory(summoner, new Callback<LeagueSummoner>() {
                            public void onCompletion(LeagueSummoner summoner) {
                                //grab necessary information
                                MatchHistoryEntry last_match = summoner.getMostRecentMatch();
                                LeagueChampion champ = last_match.getChampionSelectionForSummoner(summoner);
                                String recorded_match = "";

                                // grab most recent game id and what we have on record
                                int game_id = last_match.getGameId();
                                try {
                                    recorded_match = Files.readFirstLine(f, Charsets.UTF_8);
                                } catch (Exception e) {}
                                //compare the records and see if they match
                                if (recorded_match != null && recorded_match.equals(Integer.toString(last_match.getGameId()))) {
                                    System.out.print("/");
                                } else {
                                    // write that we have a more recent game that we have added
                                    byte[] to_file = Integer.toString(game_id).getBytes(Charsets.UTF_8);
                                    try{ Files.write(to_file,f); } catch (Exception e){};

                                    // grab all the stats we need to save to the db
                                    Map stats = last_match.getAllStats();
                                    Iterator iterator = stats.entrySet().iterator();
                                    //setup query string
                                    String p1 = "INSERT INTO games (champion_played,log_id,";
                                    String p2 = "VALUES ('"+champ.toString()+"',"+player_info[0]+',';
                                    while (iterator.hasNext()) {
                                        Map.Entry mapEntry = (Map.Entry) iterator.next();
                                        // we need to change some names around for our DB
                                        String entry = mapEntry.getKey().toString().toLowerCase();
                                        if(amap.containsKey(entry)){
                                            p1 += (amap.get(entry)+',');
                                        }else{
                                            if(entry.equals("win") || entry.equals(("lose"))){
                                                p1 += "winner,";
                                            }else{
                                                p1 += entry+',';
                                            }
                                        }
                                        if(entry.contains("item")){
                                            p2 += "\""+(imap.get(mapEntry.getValue().toString())+"\",");
                                        }else{
                                            if(entry.equals("win")){
                                                p2 += "true,";
                                            }else if(entry.equals("lose")){
                                                p2 += "false,";
                                            }else{
                                                p2 += (mapEntry.getValue().toString()+',');
                                            }
                                        }
                                    }
                                    // remove the last character of the string due to trailing comma AND close off value parens
                                    p1 = p1.substring(0,p1.length()-1)+')';
                                    p2 = p2.substring(0,p2.length()-1)+')';

                                    String query_string = p1+p2+';';

                                    try{
                                        Statement stmt = conn.createStatement();
                                        stmt.executeUpdate(query_string);
                                    }catch (Exception e){}


                                    System.out.println(query_string);
                                    System.out.println(champ);
                                }
                            }

                            public void onError(Exception ex) {
                            }

                        });

                    }
                    //else we should create the file
                }

                public void onError(Exception ex) {
                }
            });
            }
            Thread.sleep(20000);
        }
        System.exit(0);
    }
}
